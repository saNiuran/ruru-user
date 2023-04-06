package com.ruru.plastic.user.controller;

import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.enume.ResponseEnum;
import com.ruru.plastic.user.enume.UserTypeEnum;
import com.ruru.plastic.user.model.ThirdParty;
import com.ruru.plastic.user.model.User;
import com.ruru.plastic.user.model.UserAccount;
import com.ruru.plastic.user.request.ThirdPartyRequest;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.response.UserResponse;
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

        if (StringUtils.isEmpty(request.getHeader("app"))) {
            return DataResponse.error("Header App参数错误");
        }
        if (StringUtils.isEmpty(request.getHeader("deviceCode"))) {
            return DataResponse.error("Header device code参数错误");
        }
        Integer app = Integer.parseInt(request.getHeader("app"));
        String deviceCode = request.getHeader("deviceCode");

        ThirdParty existedInfo = thirdPartyService.getThirdPartyByTypeAndUid(thirdParty);
        if (null == existedInfo || existedInfo.getUserId() == null) {
            return DataResponse.error("未绑定");
        }

        UserResponse response = new UserResponse();
        User user = userService.getUserById(existedInfo.getUserId());
        BeanUtils.copyProperties(user, response);
        response.setToken(tokenTask.createToken(user.getId(), app, deviceCode, UserTypeEnum.User.getValue()));

        return DataResponse.success(response);
    }

    @PostMapping("/bind")
    public DataResponse<UserResponse> bindThirdPartyUser(@RequestBody ThirdPartyRequest request, HttpServletRequest servletRequest) {
        if (request == null || request.getId() == null || StringUtils.isEmpty(request.getSmsCode())
                || StringUtils.isEmpty(request.getMobile())) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }

        ThirdParty thirdPartyById = thirdPartyService.getThirdPartyById(request.getId());
        if (thirdPartyById == null) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }

        //校验smsCode是否有效
        String s = redisTemplate.opsForValue().get(SMS_CODE_THIRD_PARTY_BIND + request.getMobile());
        if (StringUtils.isEmpty(s) || !request.getSmsCode().equals(s)) {
            return DataResponse.error(ResponseEnum.ERROR_CODE_NOT_EXIST);
        }

        User userByMobile = userService.getValidUserByMobile(request.getMobile());
        if (userByMobile != null) {
            return thirdPartyLogin(thirdPartyService.getThirdPartyById(request.getId()), servletRequest);
        }

        //新用户，创建
        User xUser = new User();
        xUser.setMobile(request.getMobile());
        Msg<User> userMsg = userService.createUser(xUser);
        if (StringUtils.isNotEmpty(userMsg.getErrorMsg())) {
            return DataResponse.error(userMsg.getErrorMsg());
        }

        //开通财务账户 userAccount
        userAccountService.createUserAccount(new UserAccount() {{
            setUserId(userMsg.getData().getId());
        }});

        thirdPartyById.setUserId(userMsg.getData().getId());
        Msg<ThirdParty> thirdPartyMsg = thirdPartyService.updateThirdParty(thirdPartyById);
        if (StringUtils.isNotEmpty(thirdPartyMsg.getErrorMsg())) {
            return DataResponse.error(thirdPartyMsg.getErrorMsg());
        }

        return thirdPartyLogin(thirdPartyMsg.getData(), servletRequest);
    }

    @PostMapping("/unbind")
    public DataResponse<ThirdParty> unbindThirdParty(@RequestBody ThirdParty thirdParty) {
        if (thirdParty == null || thirdParty.getId() == null) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        ThirdParty thirdPartyById = thirdPartyService.getThirdPartyById(thirdParty.getId());
        if (thirdPartyById == null) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        Msg<ThirdParty> thirdPartyMsg = thirdPartyService.unBindingThirdParty(thirdPartyById);
        if (StringUtils.isNotEmpty(thirdPartyMsg.getErrorMsg())) {
            return DataResponse.error(thirdPartyMsg.getErrorMsg());
        }
        return DataResponse.success(thirdPartyMsg.getData());
    }
}
