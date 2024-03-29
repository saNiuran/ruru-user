package com.ruru.plastic.user.controller;

import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.enume.ResponseEnum;
import com.ruru.plastic.user.enume.StatusEnum;
import com.ruru.plastic.user.enume.UserTypeEnum;
import com.ruru.plastic.user.feign.ChannelFeignService;
import com.ruru.plastic.user.model.AdminUser;
import com.ruru.plastic.user.model.ThirdParty;
import com.ruru.plastic.user.model.User;
import com.ruru.plastic.user.model.UserAccount;
import com.ruru.plastic.user.net.CurrentUser;
import com.ruru.plastic.user.net.LoginRequired;
import com.ruru.plastic.user.redis.RedisService;
import com.ruru.plastic.user.request.ThirdPartyRequest;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.response.UserResponse;
import com.ruru.plastic.user.service.AdminUserService;
import com.ruru.plastic.user.service.ThirdPartyService;
import com.ruru.plastic.user.service.UserAccountService;
import com.ruru.plastic.user.service.UserService;
import com.ruru.plastic.user.task.TokenTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.ruru.plastic.user.bean.Constants.SMS_CODE_THIRD_PARTY_BIND;

@RestController
@RequestMapping("/third/party")
public class ThirdPartyController {

    @Autowired
    private ThirdPartyService thirdPartyService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private TokenTask tokenTask;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ChannelFeignService channelFeignService;

    @Description("注册的时候，先检测是不是已经绑定过；如果绑定过，前端直接调用登录接口；如果没有绑定过，前端调用绑定接口")
    @PostMapping("/register")
    public DataResponse<ThirdParty> thirdPartyRegister(@RequestBody ThirdParty thirdParty) {
        if (thirdParty == null || thirdParty.getType() == null || StringUtils.isEmpty(thirdParty.getUid())) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        ThirdParty thirdPartyByTypeAndUid = thirdPartyService.getThirdPartyByTypeAndUid(thirdParty);
        if (thirdPartyByTypeAndUid != null) {
            if (thirdPartyByTypeAndUid.getUserId() != null) {
                return DataResponse.success(thirdPartyByTypeAndUid);
            }
        }
        return DataResponse.error(Constants.ERROR_NO_INFO);
    }

    @PostMapping("/login")
    public DataResponse<UserResponse> thirdPartyLogin(@RequestBody ThirdParty thirdParty, HttpServletRequest request) {
        if (thirdParty == null || thirdParty.getType() == null || StringUtils.isEmpty(thirdParty.getUid())) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }

        if (StringUtils.isEmpty(request.getHeader("appType"))) {
            return DataResponse.error("Header App参数错误");
        }
        if (StringUtils.isEmpty(request.getHeader("deviceCode"))) {
            return DataResponse.error("Header device code参数错误");
        }
        Integer appType = Integer.parseInt(request.getHeader("appType"));
        String deviceCode = request.getHeader("deviceCode");

        ThirdParty existedInfo = thirdPartyService.getThirdPartyByTypeAndUid(thirdParty);
        if (null == existedInfo || existedInfo.getUserId() == null) {
            return DataResponse.error("未绑定");
        }

        UserResponse response = new UserResponse();
        User user = userService.getUserById(existedInfo.getUserId());
        BeanUtils.copyProperties(user, response);

        redisService.setThirdPartyInfo(existedInfo.getUserId(),thirdParty);

        response.setToken(tokenTask.createToken(user.getId(), appType, deviceCode, UserTypeEnum.User.getValue()));
        if(user.getAdminId()!=null && user.getAdminId()>0){
            AdminUser adminUserById = adminUserService.getAdminUserById(user.getAdminId());
            if(adminUserById!=null && adminUserById.getStatus().equals(StatusEnum.可用.getValue())) {
                response.setAdminToken(tokenTask.createToken(user.getAdminId(), Integer.parseInt(request.getHeader("appType")), request.getHeader("deviceCode"), UserTypeEnum.Admin.getValue()));
            }
        }
        return DataResponse.success(response);
    }

    @PostMapping("/bind")
    public DataResponse<UserResponse> bindThirdPartyUser(@RequestBody ThirdPartyRequest request, HttpServletRequest servletRequest) {
        if (request == null || request.getType() == null || StringUtils.isEmpty(request.getUid())
                || StringUtils.isEmpty(request.getSmsCode()) || StringUtils.isEmpty(request.getMobile())) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }

        ThirdParty thirdPartyByTypeAndUid = thirdPartyService.getThirdPartyByTypeAndUid(request);
        if (thirdPartyByTypeAndUid == null) {
            Msg<ThirdParty> thirdPartyMsg = thirdPartyService.createThirdParty(new ThirdParty() {{
                setUid(request.getUid());
                setType(request.getType());
                setName(request.getName());
                setIconurl(request.getIconurl());
                setGender(request.getGender());
            }});

            if(StringUtils.isNotEmpty(thirdPartyMsg.getErrorMsg())){
                return DataResponse.error(thirdPartyMsg.getErrorMsg());
            }
            thirdPartyByTypeAndUid = thirdPartyMsg.getData();
        }

        //校验smsCode是否有效
        String s = redisTemplate.opsForValue().get(SMS_CODE_THIRD_PARTY_BIND + ":" + request.getMobile());
        if (StringUtils.isEmpty(s) || !request.getSmsCode().equals(s)) {
            return DataResponse.error(ResponseEnum.ERROR_CODE_NOT_EXIST);
        }

        User userByMobile = userService.getValidUserByMobile(request.getMobile());
        if (userByMobile != null) {
            thirdPartyByTypeAndUid.setUserId(userByMobile.getId());
            thirdPartyService.updateThirdParty(thirdPartyByTypeAndUid);

            if(StringUtils.isNotEmpty(thirdPartyByTypeAndUid.getName())) {
                userByMobile.setNickName(thirdPartyByTypeAndUid.getName());
            }
            if(StringUtils.isNotEmpty(thirdPartyByTypeAndUid.getIconurl())) {
                userByMobile.setAvatar(thirdPartyByTypeAndUid.getIconurl());
            }
            if(StringUtils.isNotEmpty(thirdPartyByTypeAndUid.getGender())){
                userByMobile.setGender(Integer.parseInt(thirdPartyByTypeAndUid.getGender()));
            }
            userService.updateUser(userByMobile);

            return thirdPartyLogin(thirdPartyService.getThirdPartyById(thirdPartyByTypeAndUid.getId()), servletRequest);
        }

        //新用户，创建
        User xUser = new User();
        xUser.setMobile(request.getMobile());
        if(StringUtils.isNotEmpty(thirdPartyByTypeAndUid.getName())) {
            xUser.setNickName(thirdPartyByTypeAndUid.getName());
        }
        if(StringUtils.isNotEmpty(thirdPartyByTypeAndUid.getIconurl())) {
            xUser.setAvatar(thirdPartyByTypeAndUid.getIconurl());
        }
        if(StringUtils.isNotEmpty(thirdPartyByTypeAndUid.getGender())){
            xUser.setGender(Integer.parseInt(thirdPartyByTypeAndUid.getGender()));
        }
        Msg<User> userMsg = userService.createUser(xUser);
        if (StringUtils.isNotEmpty(userMsg.getErrorMsg())) {
            return DataResponse.error(userMsg.getErrorMsg());
        }

        //开通财务账户 userAccount
        userAccountService.createUserAccount(new UserAccount() {{
            setUserId(userMsg.getData().getId());
        }});

        //通知channel模块
        channelFeignService.registerUserSuccess(userMsg.getData());

        thirdPartyByTypeAndUid.setUserId(userMsg.getData().getId());
        Msg<ThirdParty> thirdPartyMsg = thirdPartyService.updateThirdParty(thirdPartyByTypeAndUid);
        if (StringUtils.isNotEmpty(thirdPartyMsg.getErrorMsg())) {
            return DataResponse.error(thirdPartyMsg.getErrorMsg());
        }

        return thirdPartyLogin(thirdPartyMsg.getData(), servletRequest);
    }

    @LoginRequired
    @PostMapping("/unbind")
    public DataResponse<ThirdParty> unbindThirdParty(@RequestBody ThirdParty thirdParty, @CurrentUser User user) {
        if (thirdParty == null || StringUtils.isEmpty(thirdParty.getUid()) || thirdParty.getType()==null) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        ThirdParty thirdPartyById = thirdPartyService.getThirdPartyByTypeAndUid(new ThirdParty(){{
            setUid(thirdParty.getUid());
            setType(thirdParty.getType());
            setUserId(user.getId());
        }});
        if (thirdPartyById == null) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        Msg<ThirdParty> thirdPartyMsg = thirdPartyService.unBindingThirdParty(thirdPartyById);
        if (StringUtils.isNotEmpty(thirdPartyMsg.getErrorMsg())) {
            return DataResponse.error(thirdPartyMsg.getErrorMsg());
        }

        redisService.expireThirdPartyInfo(thirdPartyById.getUserId());

        return DataResponse.success(thirdPartyMsg.getData());
    }
}
