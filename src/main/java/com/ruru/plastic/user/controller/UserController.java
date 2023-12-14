package com.ruru.plastic.user.controller;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.enume.*;
import com.ruru.plastic.user.feign.*;
import com.ruru.plastic.user.model.*;
import com.ruru.plastic.user.net.CurrentUser;
import com.ruru.plastic.user.net.LoginRequired;
import com.ruru.plastic.user.redis.RedisService;
import com.ruru.plastic.user.request.LogonRequest;
import com.ruru.plastic.user.request.UserMatchRequest;
import com.ruru.plastic.user.request.UserRequest;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.response.UserMatchResponse;
import com.ruru.plastic.user.response.UserResponse;
import com.ruru.plastic.user.task.PushTask;
import com.ruru.plastic.user.task.TokenTask;
import com.ruru.plastic.user.bean.*;
import com.ruru.plastic.user.service.*;
import com.ruru.plastic.user.utils.MyStringUtils;
import com.xiaoleilu.hutool.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserWxService userWxService;
    @Autowired
    private UserMatchService userMatchService;
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
    @Autowired
    private ConfigFeignService configFeignService;
    @Autowired
    private FinanceFeignService financeFeignService;
    @Autowired
    private ChannelFeignService channelFeignService;
    @Autowired
    private BoardFeignService boardFeignService;

    @LoginRequired
    @PostMapping("/info")
    public DataResponse<UserResponse> getUserById(@RequestBody User user) {
        return getAnonUserById(user);
    }

    @PostMapping("/info/anon")
    public DataResponse<UserResponse> getAnonUserById(@RequestBody User user) {
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

    @PostMapping("/info/anon/mobile")
    public DataResponse<Integer> isUserByMobile(@RequestBody User user) {
        if (user == null || StringUtils.isEmpty(user.getMobile())) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        User userById = userService.getValidUserByMobile(user.getMobile());
        if (userById == null) {
            return DataResponse.success(0);
        }
        return DataResponse.success(1);
    }

    @PostMapping("/info/simple/anon")
    public DataResponse<User> getAnonSimpleUserById(@RequestBody User user) {
        if (user == null || user.getId() == null) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        User userById = userService.getUserById(user.getId());
        if (userById == null) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        return DataResponse.success(userById);
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

    @PostMapping("/info/admin/simple")
    public DataResponse<User> getUserByAdminUserId(@RequestBody User user){
        if(user==null || user.getAdminId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        User userByAdminUserId = userService.getUserByAdminUserId(user.getAdminId());
        if(userByAdminUserId==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        return DataResponse.success(userByAdminUserId);
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
        if(userById==null){
            return null;
        }

        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(userById, response);

        ThirdParty thirdPartyInfo = redisService.getThirdPartyInfo(userId);
        if(null!=thirdPartyInfo){
            response.setUid(thirdPartyInfo.getUid());
            response.setThirdType(thirdPartyInfo.getType());
        }

        response.setMember(memberService.getValidMemberByUserId(userId));
        response.setUserAccount(userAccountService.getUserAccountByUserId(userId));
        if (userById.getCompanyId() != null) {
            response.setCompany(companyService.getCompanyById(userById.getCompanyId()));
        }
        return response;
    }

    private UserResponse getUserResponseByIdHiddenMobile(Long userId){
        UserResponse userResponseById = getUserResponseById(userId);
        if(userResponseById==null){
            return null;
        }
        userResponseById.setMobile(MyStringUtils.getHidePhone(userResponseById.getMobile()));
        return userResponseById;
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

        UserResponse response = getUserResponseByIdHiddenMobile(user.getId());
        if(response==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }

        UserPack pack = new UserPack();
        BeanUtils.copyProperties(response, pack);

        UserCounter counter = new UserCounter();
        DataResponse<UserCounter> userCounterDataResponse = bidFeignService.countEnquiryAndStock(user);
        if (userCounterDataResponse.getRetCode() == 0) {
            BeanUtils.copyProperties(userCounterDataResponse.getData(),counter);
        }

        DataResponse<UserCounter> messageDataResponse = smsFeignService.countMessage(user);
        if (messageDataResponse.getRetCode() == 0) {
            counter.setMessageCounter(messageDataResponse.getData().getMessageCounter());
            counter.setUnreadMessageCounter(messageDataResponse.getData().getUnreadMessageCounter());
        }

        DataResponse<Integer> reviewDataResponse = boardFeignService.queryReviewLog(new ReviewLogRequest() {{
            setOperatorId(user.getId());
        }});
        if(reviewDataResponse.getRetCode()==0){
            counter.setReviewCounter(reviewDataResponse.getData());
        }
        pack.setUserCounter(counter);

        DataResponse<Integer> unreadReplyOnReview = boardFeignService.countUnreadReplyOnReview(new Review() {{
            setReviewerId(user.getId());
        }});
        if(unreadReplyOnReview.getRetCode()==0){
            counter.setUnreadReplyCounter(unreadReplyOnReview.getData());
        }

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
        if (!Arrays.asList("13901655769", "18057942731","18818881888","13584851021").contains(request.getMobile())) {
            String s = redisService.getSmsCode(Constants.SMS_CODE_LOGIN + ":" + request.getMobile());
            if (StringUtils.isEmpty(s)) {
                return DataResponse.error("验证码已经失效！");
            }
            if (!s.equals(request.getSmsCode())) {
                return DataResponse.error("验证码错误！");
            }
        }

        //检查是不是已经有用户 且有效的
        User userByMobile = userService.getUserByMobile(request.getMobile());
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

            //通知channel模块
            channelFeignService.registerUserSuccess(msg.getData());

        }else if(userByMobile.getStatus().equals(UserStatusEnum.注销.getValue())){
//            if(userByMobile.getUpdateTime().getTime() > DateUtil.offsiteDay(new Date(),-15).getTime()) {
                //刚注销15天，给反悔期15天
//                userByMobile.setStatus(UserStatusEnum.有效.getValue());
//                userService.updateUser(userByMobile);
//            }else
            if(userByMobile.getUpdateTime().getTime() > DateUtil.offsiteDay(new Date(),-365).getTime()){
                //小于1年，不让再注册
                return DataResponse.error("您的账户已主动注销，请在"+(365-Math.round(new Date().getTime()-userByMobile.getUpdateTime().getTime())/(24*3600*1000.0))+"天后再注册和登录");
            }else{

                userByMobile.setMobile(userByMobile.getMobile()+".");  //把原来的电话+小数点
                userService.updateUser(userByMobile);

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
     * 公众号用户登录，返回用户token (appType===2)
     */
    @PostMapping("/userLogin/h5mp")
    public DataResponse<UserMatchResponse> userLoginH5Mp(@RequestBody LogonRequest request, HttpServletRequest servletRequest) {
        if (request == null || StringUtils.isEmpty(request.getMobile()) || StringUtils.isEmpty(request.getSmsCode())
                || StringUtils.isEmpty(servletRequest.getHeader("openId")) || StringUtils.isEmpty(servletRequest.getHeader("appType"))) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }

        String openId = servletRequest.getHeader("openId");

        UserWx wx = userWxService.getUserWxByOpenId(openId);
        if(wx==null){
            Msg<UserWx> wxMsg = userWxService.createUserWx(new UserWx() {{
                setOpenid(openId);
                setType(servletRequest.getIntHeader("appType"));
            }});
            if(StringUtils.isNotEmpty(wxMsg.getErrorMsg())){
                return DataResponse.error(wxMsg.getErrorMsg());
            }
            wx = wxMsg.getData();
        }

        //校验手机号是不是有效
        Matcher tmpMatcher = Constants.CHINA_MOBILE_PATTERN.matcher(request.getMobile());
        if (!tmpMatcher.find()) {
            return DataResponse.error("无效电话号码");
        }
        //检查smsCode是不是有效  (测试系统，不校验验证码)
        if (!Arrays.asList("13901655769", "18057942731","18818881888","13584851021").contains(request.getMobile())) {
            String s = redisService.getSmsCode(Constants.SMS_CODE_LOGIN + ":" + request.getMobile());
            if (StringUtils.isEmpty(s)) {
                return DataResponse.error("验证码已经失效！");
            }
            if (!s.equals(request.getSmsCode())) {
                return DataResponse.error("验证码错误！");
            }
        }

        //检查是不是已经有用户 且有效的
        User userByMobile = userService.getUserByMobile(request.getMobile());
        if (userByMobile == null || !userByMobile.getStatus().equals(UserStatusEnum.有效.getValue())) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }

        UserMatchRequest userMatchRequest = new UserMatchRequest();
        userMatchRequest.setUserId(userByMobile.getId());
        userMatchRequest.setTargetId(wx.getId());
        userMatchRequest.setTargetType(UserTargetTypeEnum.微信公众号.getValue());
        List<UserMatch> userMatches = userMatchService.queryUserMatch(userMatchRequest);

        UserMatch userMatch;
        if(userMatches.size()==0){
            userMatch = userMatchService.createUserMatch(userMatchRequest).getData();
        }else{
            userMatch = userMatches.get(0);
        }

        UserMatchResponse userMatchResponse = new UserMatchResponse();
        BeanUtils.copyProperties(userMatch,userMatchResponse);

        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(userByMobile, response);
        response.setToken(tokenTask.createToken(userByMobile.getId(), servletRequest.getIntHeader("appType"), openId, UserTypeEnum.User.getValue()));
        userMatchResponse.setUser(response);
        userMatchResponse.setUserWx(wx);

        return DataResponse.success(userMatchResponse);
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
            redisService.expireThirdPartyInfo(user.getId());
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
            redisService.expireThirdPartyInfo(user.getId());
        }
        //下架此用户的所有信息，包括 询价 和 报价
        pushTask.sendUserLogoff(user);

        return DataResponse.success(null);
    }

    @LoginRequired
    @PostMapping("/update")
    public DataResponse<User> updateUser(@RequestBody User user) {
        user.setMobile(null); //这里不能改手机号，手机号更改走单独的接口
        user.setAdminId(null);  //防止被篡改管理员去权限
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
        User userByMobile = userService.getUserByMobile(mUser.getMobile());
        if (userByMobile != null && !userByMobile.getStatus().equals(UserStatusEnum.失效.getValue())) {
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

    @LoginRequired
    @PostMapping("/privilege/check")
    public DataResponse<Integer> checkMyPrivilege(@RequestBody EnquiryEventLog enquiryEventLog, @CurrentUser User me){
        if(enquiryEventLog==null || enquiryEventLog.getEventType()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        if(!enquiryEventLog.getEventType().equals(EnquiryEventTypeEnum.发布.getValue()) && enquiryEventLog.getEnquiryId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }

        //检查此次事件是否当日重复事件
        if(!enquiryEventLog.getEventType().equals(EnquiryEventTypeEnum.发布.getValue())) {        //非发布询价动作
            DataResponse<Integer> logOfTodayDataResponse = bidFeignService.sameEnquiryEventLogOfToday(new EnquiryEventLog() {{
                setEnquiryId(enquiryEventLog.getEnquiryId());
                setUserId(me.getId());
                setEventType(enquiryEventLog.getEventType());
            }});

            if (logOfTodayDataResponse.getRetCode() != 0) {
                return DataResponse.error(logOfTodayDataResponse.getMessage());
            }

            if (logOfTodayDataResponse.getData().equals(1)) {  //今天此条信息操作有重复的，不检查权限
                return DataResponse.success(1);
            }
        }

        //下面表示是不重复，继续检查
        UserResponse userResponseById = getUserResponseById(me.getId());
        Privilege mPrivilege = new Privilege();
        mPrivilege.setLordType(LordTypeEnum.询价.getValue());
        mPrivilege.setAction(enquiryEventLog.getEventType());
        assert userResponseById != null;
        mPrivilege.setMemberLevel(userResponseById.getMemberLevel());
        Long deposit = userResponseById.getUserAccount().getDeposit();
        if(deposit==null || deposit==0){
            mPrivilege.setDepositConfigId(0L);
        }else{
            DataResponse<List<DepositConfig>> listDataResponse = financeFeignService.listValidDepositConfig();
            if(listDataResponse.getRetCode()!=0){
                return DataResponse.error(listDataResponse.getMessage());
            }
            mPrivilege.setDepositConfigId(0L);
            for(DepositConfig config: listDataResponse.getData()){
                if(config.getAmount()<=deposit){
                    mPrivilege.setDepositConfigId(config.getId());
                }else{
                    break;
                }
            }
        }
        DataResponse<List<Privilege>> dataResponse = configFeignService.queryPrivilege(mPrivilege);
        if(dataResponse.getRetCode()!=0){
            return DataResponse.error(dataResponse.getMessage());
        }

        if(dataResponse.getData().size()>0){
            mPrivilege = dataResponse.getData().get(0);

            if(mPrivilege.getValue()==null){
                if(me.getMemberLevel()!=null && me.getMemberLevel()>0){
                    return DataResponse.success(1);
                }else {
                    return DataResponse.success(0);
                }

            }else if(mPrivilege.getValue()==0){     //表示不限制
                return DataResponse.success(1);
            }else{
                //检查数字有没有超出
                DataResponse<EventCounter> dataResponse1 = bidFeignService.countEnquiryAndStockOfToday(new User() {{
                    setId(me.getId());
                }});
                if(dataResponse1.getRetCode()!=0){
                    return DataResponse.error(dataResponse1.getMessage());
                }
                EventCounter counter  = dataResponse1.getData();
                boolean result = true;
                switch (EnquiryEventTypeEnum.getEnum(enquiryEventLog.getEventType())){
                    case 发布:
                        if(counter.getEnquiryPostCounter()>=mPrivilege.getValue()) {
                            result = false;
                        }
                        break;
                    case 报价:
                        if(counter.getQuotationCounter()>=mPrivilege.getValue()) {
                            result = false;
                        }
                        break;
                    case 查看:
                        if(counter.getEnquiryCheckCounter()>=mPrivilege.getValue()) {
                            result = false;
                        }
                        break;
                    case 打电话:
                        if(counter.getEnquiryCallCounter()>=mPrivilege.getValue()) {
                            result = false;
                        }
                        break;
                }
                return DataResponse.success(result?1:0);
            }
        }
        return DataResponse.error("权限配置错误！");
    }

    @LoginRequired
    @PostMapping("/privilege/check/stock")
    public DataResponse<Integer> checkMyPrivilege(@RequestBody StockEventLog enquiryEventLog, @CurrentUser User me){
        if(enquiryEventLog==null || enquiryEventLog.getEventType()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        if(!enquiryEventLog.getEventType().equals(StockEventTypeEnum.发布.getValue()) && enquiryEventLog.getStockId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }

        //检查此次事件是否当日重复事件
        if(!enquiryEventLog.getEventType().equals(StockEventTypeEnum.发布.getValue())) {        //非发布询价动作
            DataResponse<Integer> logOfTodayDataResponse = bidFeignService.sameStockEventLogOfToday(new StockEventLog() {{
                setStockId(enquiryEventLog.getStockId());
                setUserId(me.getId());
                setEventType(enquiryEventLog.getEventType());
            }});

            if (logOfTodayDataResponse.getRetCode() != 0) {
                return DataResponse.error(logOfTodayDataResponse.getMessage());
            }

            if (logOfTodayDataResponse.getData().equals(1)) {  //今天此条信息操作有重复的，不检查权限
                return DataResponse.success(1);
            }
        }

        //下面表示是不重复，继续检查
        UserResponse userResponseById = getUserResponseById(me.getId());
        Privilege mPrivilege = new Privilege();
        mPrivilege.setLordType(LordTypeEnum.库存.getValue());
        mPrivilege.setAction(enquiryEventLog.getEventType());
        assert userResponseById != null;
        mPrivilege.setMemberLevel(userResponseById.getMemberLevel());
        Long deposit = userResponseById.getUserAccount().getDeposit();
        if(deposit==null || deposit==0){
            mPrivilege.setDepositConfigId(0L);
        }else{
            DataResponse<List<DepositConfig>> listDataResponse = financeFeignService.listValidDepositConfig();
            if(listDataResponse.getRetCode()!=0){
                return DataResponse.error(listDataResponse.getMessage());
            }
            mPrivilege.setDepositConfigId(0L);
            for(DepositConfig config: listDataResponse.getData()){
                if(config.getAmount()<=deposit){
                    mPrivilege.setDepositConfigId(config.getId());
                }else{
                    break;
                }
            }
        }
        DataResponse<List<Privilege>> dataResponse = configFeignService.queryPrivilege(mPrivilege);
        if(dataResponse.getRetCode()!=0){
            return DataResponse.error(dataResponse.getMessage());
        }

        if(dataResponse.getData().size()>0){
            mPrivilege = dataResponse.getData().get(0);

            if(mPrivilege.getValue()==null){
                if(me.getMemberLevel()!=null && me.getMemberLevel()>0){
                    return DataResponse.success(1);
                }else {
                    return DataResponse.success(0);
                }

            }else if(mPrivilege.getValue()==0){     //表示不限制
                return DataResponse.success(1);
            }else{
                //检查数字有没有超出
                DataResponse<EventCounter> dataResponse1 = bidFeignService.countEnquiryAndStockOfToday(new User() {{
                    setId(me.getId());
                }});
                if(dataResponse1.getRetCode()!=0){
                    return DataResponse.error(dataResponse1.getMessage());
                }
                EventCounter counter  = dataResponse1.getData();
                boolean result = true;
                switch (StockEventTypeEnum.getEnum(enquiryEventLog.getEventType())){
                    case 发布:
                        if(counter.getStockPostCounter()>=mPrivilege.getValue()) {
                            result = false;
                        }
                        break;
                    case 砍一刀:
                        if(counter.getQuotationCounter()>=mPrivilege.getValue()) {
                            result = false;
                        }
                        break;
                    case 查看:
                        if(counter.getStockCheckCounter()>=mPrivilege.getValue()) {
                            result = false;
                        }
                        break;
                    case 打电话:
                        if(counter.getStockCallCounter()>=mPrivilege.getValue()) {
                            result = false;
                        }
                        break;
                }
                return DataResponse.success(result?1:0);
            }
        }
        return DataResponse.error("权限配置错误！");
    }



    @Description("bid模块接口，不能暴露给前端")
    @PostMapping("/query/id/list")
    public DataResponse<List<Long>> queryUserIdList(@RequestBody User user){
        return DataResponse.success(userService.queryUser(user).stream().map(User::getId).collect(Collectors.toList()));
    }
}
