package com.ruru.plastic.user.controller;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.enume.StatusEnum;
import com.ruru.plastic.user.enume.UserStatusEnum;
import com.ruru.plastic.user.enume.UserTypeEnum;
import com.ruru.plastic.user.feign.BidFeignService;
import com.ruru.plastic.user.feign.SmsFeignService;
import com.ruru.plastic.user.model.*;
import com.ruru.plastic.user.net.CurrentUser;
import com.ruru.plastic.user.net.LoginRequired;
import com.ruru.plastic.user.redis.RedisService;
import com.ruru.plastic.user.request.LogonRequest;
import com.ruru.plastic.user.request.UserRequest;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.response.UserResponse;
import com.ruru.plastic.user.task.PushTask;
import com.ruru.plastic.user.task.TokenTask;
import com.ruru.plastic.user.bean.*;
import com.ruru.plastic.user.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private TokenTask tokenTask;
    @Autowired
    private PushTask pushTask;
    @Autowired
    private SmsFeignService smsFeignService;
    @Autowired
    private BidFeignService bidFeignService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private UserCorporateCertMatchService userCorporateCertMatchService;
    @Autowired
    private PersonalCertService personalCertService;
    @Autowired
    private CorporateCertService corporateCertService;
    @Autowired
    private ThirdPartyService thirdPartyService;
    @Autowired
    private AdminUserService adminUserService;

    @LoginRequired
    @PostMapping("/info")
    public DataResponse<UserResponse> getUserById(@RequestBody User user) {
        if (user == null || user.getId() == null) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        User userById = userService.getUserById(user.getId());
        if (userById == null) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        UserResponse response = getUserResponseById(user.getId());
        return DataResponse.success(response);
    }

    @PostMapping("/info/uid")
    public DataResponse<UserResponse> getUserByUid(@RequestBody ThirdParty thirdParty) {
        if (thirdParty == null || thirdParty.getType() == null || StringUtils.isEmpty(thirdParty.getUid())) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        ThirdParty thirdPartyByTypeAndUid = thirdPartyService.getThirdPartyByTypeAndUid(thirdParty);
        if (thirdPartyByTypeAndUid == null) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        UserResponse response = getUserResponseById(thirdPartyByTypeAndUid.getUserId());
        return DataResponse.success(response);
    }

    @LoginRequired
    @PostMapping("/call")
    public DataResponse<String> callUserById(@RequestBody User user) {
        if (user == null || user.getId() == null) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        User userById = userService.getUserById(user.getId());
        if (userById == null) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        return DataResponse.success(userById.getMobile());
    }

    private UserResponse getUserResponseById(Long userId) {
        User userById = userService.getUserById(userId);
//        userById.setMobile(MyStringUtils.getHidePhone(userById.getMobile()));

        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(userById, response);
        response.setMember(memberService.getValidMemberByUserId(userId));
        response.setUserAccount(userAccountService.getUserAccountByUserId(userId));
        if (userById.getCompanyId() != null) {
            response.setCompany(companyService.getCompanyById(userById.getCompanyId()));
        }
        PersonalCert personalCertByUserId = personalCertService.getPersonalCertByUserId(userId);
        if(personalCertByUserId!=null){
            response.setPersonalCertStatus(personalCertByUserId.getStatus());
        }
        UserCorporateCertMatch userCorporateCertMatchByUserId = userCorporateCertMatchService.getUserCorporateCertMatchByUserId(userId);
        if(userCorporateCertMatchByUserId!=null){
            CorporateCert corporateCertById = corporateCertService.getCorporateCertById(userCorporateCertMatchByUserId.getCorporateCertId());
            if(corporateCertById!=null){
                response.setCorporateCertStatus(corporateCertById.getStatus());
            }
        }
        return response;
    }

    @Description("这个信息最全")
    @LoginRequired
    @PostMapping("/pack")
    public DataResponse<UserPack> getUserPackById(@RequestBody User user) {
        if (user == null || user.getId() == null) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        User userById = userService.getUserById(user.getId());
        if (userById == null) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }

        UserResponse response = getUserResponseById(user.getId());

        UserPack pack = new UserPack();
        BeanUtils.copyProperties(response, pack);

        UserCounter counter = new UserCounter();
        DataResponse<UserCounter> messageDataResponse = smsFeignService.countMessage(user);
        if (messageDataResponse.getRetCode() == 0) {
            counter.setMessageCounter(messageDataResponse.getData().getMessageCounter());
            counter.setUnreadMessageCounter(messageDataResponse.getData().getUnreadMessageCounter());
        }
        DataResponse<UserCounter> userCounterDataResponse = bidFeignService.countEnquiryAndQuotation(user);
        if (userCounterDataResponse.getRetCode() == 0) {
            counter.setEnquiryCounter(userCounterDataResponse.getData().getEnquiryCounter());
            counter.setQuotationCounter(userCounterDataResponse.getData().getQuotationCounter());
            counter.setFavoriteEnquiryCounter(userCounterDataResponse.getData().getFavoriteEnquiryCounter());
        }
        pack.setUserCounter(counter);


        pack.setPersonalCert(personalCertService.getPersonalCertByUserId(user.getId()));

        UserCorporateCertMatch userCorporateCertMatchByUserId = userCorporateCertMatchService.getUserCorporateCertMatchByUserId(user.getId());
        if (userCorporateCertMatchByUserId != null) {
            pack.setCorporateCert(corporateCertService.getCorporateCertById(userCorporateCertMatchByUserId.getCorporateCertId()));
        }

        return DataResponse.success(pack);
    }

    @Description("主要给Feign调用，前端不能调用")
    @LoginRequired
    @PostMapping("/my")
    public DataResponse<User> getCurrentUser(@CurrentUser User user) {
        return DataResponse.success(user);
    }


    @LoginRequired
    @PostMapping("/token/check")
    public DataResponse<User> tokenCheck(@CurrentUser User user) {
        return DataResponse.success(user);
    }

    /**
     * 用户登录，返回用户token
     */
    @PostMapping("/userLogin")
    public DataResponse<UserResponse> userLogin(@RequestBody LogonRequest request, HttpServletRequest servletRequest) {
        if (request == null || StringUtils.isEmpty(request.getMobile()) || StringUtils.isEmpty(request.getSmsCode())) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        //校验手机号是不是有效
        Matcher tmpMatcher = Constants.CHINA_MOBILE_PATTERN.matcher(request.getMobile());
        if (!tmpMatcher.find()) {
            return DataResponse.error("无效电话号码");
        }
        //检查smsCode是不是有效  (测试系统，不校验验证码)
        if (!Arrays.asList("13901655769", "18057942731","18818881888").contains(request.getMobile())) {
            String s = redisService.getSmsCode(Constants.SMS_CODE_LOGIN + ":" + request.getMobile());
            if (StringUtils.isEmpty(s)) {
                return DataResponse.error("验证码已经失效！");
            }
            if (!s.equals(request.getSmsCode())) {
                return DataResponse.error("验证码错误！");
            }
        }

        //检查是不是已经有用户 且有效的
        User userByMobile = userService.getValidUserByMobile(request.getMobile());
        if (userByMobile == null) {
            //新用户
            userByMobile = new User();
            BeanUtils.copyProperties(request, userByMobile);
            Msg<User> msg = userService.createUser(userByMobile);
            if (StringUtils.isNotEmpty(msg.getErrorMsg())) {
                return DataResponse.error(msg.getErrorMsg());
            }

            userByMobile = msg.getData();

            //开通财务账户 userAccount
            userAccountService.createUserAccount(new UserAccount() {{
                setUserId(msg.getData().getId());
            }});
        }

        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(userByMobile, response);
        response.setToken(tokenTask.createToken(userByMobile.getId(), Integer.parseInt(servletRequest.getHeader("appType")), servletRequest.getHeader("deviceCode"), UserTypeEnum.User.getValue()));
        if(userByMobile.getAdminId()!=null && userByMobile.getAdminId()>0){
            AdminUser adminUserById = adminUserService.getAdminUserById(userByMobile.getAdminId());
            if(adminUserById!=null && adminUserById.getStatus().equals(StatusEnum.可用.getValue())) {
                response.setAdminToken(tokenTask.createToken(userByMobile.getAdminId(), Integer.parseInt(servletRequest.getHeader("appType")), servletRequest.getHeader("deviceCode"), UserTypeEnum.Admin.getValue()));
            }
        }
        return DataResponse.success(response);
    }


    /**
     * 用户退出登录
     */
    @LoginRequired
    @RequestMapping(value = "/userLogout", method = RequestMethod.POST)
    public DataResponse<Void> userLogout(@CurrentUser User user) {
        redisService.expireUserInfo(user.getId(), UserTypeEnum.User.getValue());
        if (user.getAdminId() != 0) {
            redisService.expireUserInfo(user.getAdminId(), UserTypeEnum.Admin.getValue());
        }
        return DataResponse.success(null);
    }

    /**
     * 用户注销
     */
    @LoginRequired
    @RequestMapping(value = "/userLogOff", method = RequestMethod.POST)
    public DataResponse<Void> userLogOff(@CurrentUser User user) {
        Msg<User> msg = userService.deleteUser(user);
        if (StringUtils.isNotEmpty(msg.getErrorMsg())) {
            return DataResponse.error(msg.getErrorMsg());
        }
        //清除token
        redisService.extendUserInfo(user.getId(), UserTypeEnum.User.getValue());
        if (user.getAdminId() != 0) {
            redisService.expireUserInfo(user.getAdminId(), UserTypeEnum.Admin.getValue());
        }
        //下架此用户的所有信息，包括 询价 和 报价
        pushTask.sendUserLogoff(user);

        return DataResponse.success(null);
    }

    @LoginRequired
    @PostMapping("/update")
    public DataResponse<User> updateUser(@RequestBody User user) {
        user.setMobile(null); //这里不能改手机号，手机号更改走单独的接口
        Msg<User> userMsg = userService.updateUser(user);
        if (StringUtils.isNotEmpty(userMsg.getErrorMsg())) {
            return DataResponse.error(userMsg.getErrorMsg());
        }
        return DataResponse.success(userMsg.getData());
    }

    @LoginRequired
    @PostMapping("/search")
    public DataResponse<List<Long>> searchUser(@RequestBody UserRequest request) {
        Msg<List<User>> listMsg = userService.searchUser(request);
        if (StringUtils.isNotEmpty(listMsg.getErrorMsg())) {
            return DataResponse.error(listMsg.getErrorMsg());
        }
        return DataResponse.success(listMsg.getData().stream().map(User::getId).collect(Collectors.toList()));
    }

    @PostMapping("/query/mobiles")
    public DataResponse<List<String>> queryMobilesByUserIds(@RequestBody UserRequest request) {
        if (request == null || request.getIdList() == null || request.getIdList().size() == 0) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        Msg<List<String>> listMsg = userService.queryMobilesByUserIds(request.getIdList());
        if (StringUtils.isNotEmpty(listMsg.getErrorMsg())) {
            return DataResponse.error(listMsg.getErrorMsg());
        }
        return DataResponse.success(listMsg.getData());
    }


    @LoginRequired
    @PostMapping("/update/mobile")
    public DataResponse<Void> updateUserMobile(@RequestBody User mUser, @CurrentUser User user) {

        //校验这个新手机号码，是不是被他人占用
        User userByMobile = userService.getValidUserByMobile(mUser.getMobile());
        if (userByMobile != null && userByMobile.getStatus().equals(UserStatusEnum.有效.getValue())) {
            return DataResponse.error("新号码已经被注册使用！");
        }

        //更新用户信息
        user.setMobile(mUser.getMobile());
        Msg<User> userMsg = userService.updateUser(user);
        if (StringUtils.isNotEmpty(userMsg.getErrorMsg())) {
            return DataResponse.error(userMsg.getErrorMsg());
        }

        return DataResponse.success(null);
    }


    @PostMapping("/last/three")
    public DataResponse<List<User>> queryLastThreeUser() {
        PageInfo<User> userPageInfo = userService.filterUser(new UserRequest() {{
            setPage(1);
            setSize(3);
        }});
        return DataResponse.success(userPageInfo.getList());
    }

    @PostMapping("/member/valid")
    public DataResponse<Boolean> memberValid(@RequestBody User user) {
        if (user == null || user.getId() == null) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        Member validMemberByUserId = memberService.getValidMemberByUserId(user.getId());
        if (validMemberByUserId != null && validMemberByUserId.getStatus().equals(StatusEnum.可用.getValue())) {
            return DataResponse.success(true);
        }
        return DataResponse.success(false);
    }
}
