package com.ruru.plastic.user.controller.admin;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.bean.PushBody;
import com.ruru.plastic.user.bean.SystemConfig;
import com.ruru.plastic.user.enume.*;
import com.ruru.plastic.user.feign.ConfigFeignService;
import com.ruru.plastic.user.model.*;
import com.ruru.plastic.user.net.AdminRequired;
import com.ruru.plastic.user.net.CurrentAdminUser;
import com.ruru.plastic.user.request.CorporateCertRequest;
import com.ruru.plastic.user.request.MemberLogRequest;
import com.ruru.plastic.user.request.BlueCertRequest;
import com.ruru.plastic.user.request.PersonalCertRequest;
import com.ruru.plastic.user.response.BlueCertResponse;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.service.*;
import com.ruru.plastic.user.task.CertTask;
import com.xiaoleilu.hutool.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/admin/blue/cert")
public class AdminBlueCertController {

    @Autowired
    private BlueCertService blueCertService;
    @Autowired
    private CertificateLogService certificateLogService;
    @Autowired
    private CertTask certTask;
    @Autowired
    private UserService userService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberLogService memberLogService;
    @Autowired
    private PersonalCertService personalCertService;
    @Autowired
    private CorporateCertService corporateCertService;
    @Autowired
    private UserCorporateCertMatchService userCorporateCertMatchService;
    @Value("${cert.blue.free.day}")
    private Integer freeDays;
    @Autowired
    private ConfigFeignService configFeignService;

    @AdminRequired
    @PostMapping("/info")
    public DataResponse<BlueCert> getBlueCertById(@RequestBody BlueCert blueCert) {
        if (blueCert == null || blueCert.getId() == null) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        BlueCert blueCertById = blueCertService.getBlueCertById(blueCert.getId());
        if (blueCertById == null) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        return DataResponse.success(blueCertById);
    }

    @AdminRequired
    @PostMapping("/audit/ok")
    public DataResponse<BlueCertResponse> createBlueCert(@RequestBody BlueCert blueCert, @CurrentAdminUser AdminUser adminUser) {
        if (blueCert == null || blueCert.getId() == null) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        BlueCert blueCertById = blueCertService.getBlueCertById(blueCert.getId());
        if (blueCertById == null) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }

        User userById = userService.getUserById(blueCertById.getUserId());
        if(userById==null){
            return DataResponse.error("用户信息错误！");
        }

        if (!Arrays.asList(CertStatusEnum.待审核.getValue(), CertStatusEnum.审核失败.getValue()).contains(blueCertById.getStatus())) {
            return DataResponse.error("当前认证状态为" + CertStatusEnum.getEnum(blueCertById.getStatus()).name() + "，不能审核！");
        }

        blueCertById.setStatus(CertStatusEnum.审核通过.getValue());
        Msg<BlueCert> msg = blueCertService.updateBlueCert(blueCertById);
        if (StringUtils.isNotEmpty(msg.getErrorMsg())) {
            return DataResponse.error(msg.getErrorMsg());
        }
        Msg<User> userMsg = userService.updateUser(new User() {{
            setId(blueCertById.getUserId());
            setCertLevel(getMaxCertLevel(blueCertById.getUserId()));
        }});
        if (StringUtils.isNotEmpty(userMsg.getErrorMsg())) {
            return DataResponse.error(userMsg.getErrorMsg());
        }
        userById = userMsg.getData();

        certificateLogService.createCertificateLog(new CertificateLog() {{
            setOperatorType(OperatorTypeEnum.工作人员.getValue());
            setOperatorId(adminUser.getId());
            setLordId(msg.getData().getId());
            setCertLevel(UserCertLevelEnum.蓝V认证.getValue());
            setLordType(CertLordTypeEnum.身份证.getValue());
            setCertStatus(msg.getData().getStatus());
        }});

        certTask.createCertMessage(blueCertById, NotifyCodeEnum.蓝V认证_审核通过, adminUser);
        certTask.createPush(new PushBody() {{
            setNotifyCode(NotifyCodeEnum.蓝V认证_审核通过);
            setUserIds(Collections.singletonList(blueCertById.getUserId()));

            Map<String, String> extras = new HashMap<>();
            extras.put("id", blueCertById.getId().toString());
            extras.put("notifyCode", NotifyCodeEnum.蓝V认证_审核通过.getCode());
            setExtras(extras);
        }});

        //赠送90天VIP会员
        //检查是不是活动还在继续
        boolean active = false;
        DataResponse<SystemConfig> dataResponse = configFeignService.getSystemConfigByName(new SystemConfig() {{
            setName("blue_v_donate_member_3");
        }});
        if(dataResponse.getRetCode()==0 && dataResponse.getData().getValue().equals("1")){
            active = true;
        }

        if(active) {
            //检查是不是已经赠送过
            List<MemberLog> memberLogList = memberLogService.queryMemberLog(new MemberLogRequest() {{
                setUserId(blueCertById.getUserId());
                setActionType(MemberActionTypeEnum.蓝V认证赠送.getValue());
            }});

            if (memberLogList.size() == 0) {

                Member validMemberByUserId = memberService.getValidMemberByUserId(userById.getId());
                Msg<Member> memberMsg;
                if (validMemberByUserId == null) {
                    memberMsg = memberService.createMember(new Member() {{
                        setUserId(blueCertById.getUserId());
                        setStatus(StatusEnum.可用.getValue());
                        setBeginTime(new Date());
                        setOverTime(DateUtil.offsiteDay(new Date(), freeDays));
                    }});
                } else {
                    validMemberByUserId.setOverTime(DateUtil.offsiteDay(validMemberByUserId.getOverTime(), freeDays));
                    memberMsg = memberService.updateMember(validMemberByUserId);
                }
                if (StringUtils.isNotEmpty(memberMsg.getErrorMsg())) {
                    return DataResponse.error(memberMsg.getErrorMsg());
                }

                userById.setMemberLevel(UserMemberLevelEnum.付费用户.getValue());
                userService.updateUser(userById);
                memberLogService.createMemberLog(new MemberLog() {{
                    setUserId(blueCertById.getUserId());
                    setActionType(MemberActionTypeEnum.蓝V认证赠送.getValue());
                    setDays(freeDays);
                    setRemark("蓝V认证赠送");
                }});
            }
        }
        return DataResponse.success(getBlueCertResponseById(msg.getData().getId()));
    }

    @AdminRequired
    @PostMapping("/audit/fail")
    public DataResponse<BlueCertResponse> updateBlueCert(@RequestBody BlueCert blueCert, @CurrentAdminUser AdminUser adminUser) {
        if (blueCert == null || blueCert.getId() == null) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        BlueCert blueCertById = blueCertService.getBlueCertById(blueCert.getId());
        if (blueCertById == null) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }

        if (!Arrays.asList(CertStatusEnum.待审核.getValue(), CertStatusEnum.审核通过.getValue()).contains(blueCertById.getStatus())) {
            return DataResponse.error("当前认证状态为" + CertStatusEnum.getEnum(blueCertById.getStatus()).name() + "，不能审核！");
        }

        User userById = userService.getUserById(blueCertById.getUserId());
        if(userById==null){
            return DataResponse.error("用户信息错误！");
        }

        boolean fromOk = blueCertById.getStatus().equals(CertStatusEnum.审核通过.getValue());

        blueCertById.setStatus(CertStatusEnum.审核失败.getValue());
        Msg<BlueCert> msg = blueCertService.updateBlueCert(blueCertById);
        if (StringUtils.isNotEmpty(msg.getErrorMsg())) {
            return DataResponse.error(msg.getErrorMsg());
        }

        certificateLogService.createCertificateLog(new CertificateLog() {{
            setOperatorType(OperatorTypeEnum.工作人员.getValue());
            setOperatorId(adminUser.getId());
            setLordId(msg.getData().getId());
            setLordType(CertLordTypeEnum.身份证.getValue());
            setCertLevel(UserCertLevelEnum.蓝V认证.getValue());
            setCertStatus(msg.getData().getStatus());
        }});

        certTask.createCertMessage(blueCertById, NotifyCodeEnum.蓝V认证_审核不通过, adminUser);

        //如果原来认证成功，回退到最大认证;
        if(fromOk){
            userById.setCertLevel(getMaxCertLevel(blueCertById.getUserId()));
            userService.updateUser(userById);
        }

        certTask.createPush(new PushBody() {{
            setNotifyCode(NotifyCodeEnum.蓝V认证_审核不通过);
            setUserIds(Collections.singletonList(blueCertById.getUserId()));

            Map<String, String> extras = new HashMap<>();
            extras.put("id", blueCertById.getId().toString());
            extras.put("notifyCode", NotifyCodeEnum.蓝V认证_审核不通过.getCode());
            setExtras(extras);
        }});

        return DataResponse.success(getBlueCertResponseById(msg.getData().getId()));
    }



    @AdminRequired
    @PostMapping("/delete")
    public DataResponse<BlueCert> deleteBlueCert(@RequestBody BlueCert blueCert) {
        Msg<BlueCert> msg = blueCertService.deleteBlueCert(blueCert);
        if (StringUtils.isNotEmpty(msg.getErrorMsg())) {
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @AdminRequired
    @PostMapping("/filter")
    public DataResponse<PageInfo<BlueCertResponse>> filterBlueCertResponse(@RequestBody BlueCertRequest request) {
        PageInfo<BlueCert> pageInfo = blueCertService.filterBlueCert(request);
        PageInfo<BlueCertResponse> responsePageInfo = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo,responsePageInfo);

        List<BlueCertResponse> responseList = new ArrayList<>();

        for(BlueCert blueCert: pageInfo.getList()){
            responseList.add(getBlueCertResponseById(blueCert.getId()));
        }
        responsePageInfo.setList(responseList);

        return DataResponse.success(responsePageInfo);
    }

    private BlueCertResponse getBlueCertResponseById(Long id){
        BlueCert blueCertById = blueCertService.getBlueCertById(id);
        if(blueCertById==null){
            return null;
        }
        BlueCertResponse response = new BlueCertResponse();
        BeanUtils.copyProperties(blueCertById,response);
        response.setUser(userService.getUserById(blueCertById.getUserId()));
        return response;
    }

    private int getMaxCertLevel(Long userId){
        List<BlueCert> list = blueCertService.queryBlueCert(new BlueCertRequest() {{
            setUserId(userId);
            setStatus(CertStatusEnum.审核通过.getValue());
        }});
        if(list.size()>0){
            return UserCertLevelEnum.蓝V认证.getValue();
        }

        UserCorporateCertMatch matchByUserId = userCorporateCertMatchService.getUserCorporateCertMatchByUserId(userId);
        if(matchByUserId!=null){
            List<CorporateCert> list1 = corporateCertService.queryCorporateCert(new CorporateCertRequest() {{
                setId(matchByUserId.getCorporateCertId());
            }});
            if(list1.size()>0){
                return UserCertLevelEnum.企业认证.getValue();
            }
        }

        List<PersonalCert> list2 = personalCertService.queryPersonalCert(new PersonalCertRequest() {{
            setUserId(userId);
            setStatus(CertStatusEnum.审核通过.getValue());
        }});
        if(list2.size()>0){
            return UserCertLevelEnum.个人认证.getValue();
        }

        return UserCertLevelEnum.未认证.getValue();
    }

}
