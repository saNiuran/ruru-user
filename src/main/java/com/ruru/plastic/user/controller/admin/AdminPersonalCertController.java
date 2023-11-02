package com.ruru.plastic.user.controller.admin;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.bean.PushBody;
import com.ruru.plastic.user.enume.*;
import com.ruru.plastic.user.model.*;
import com.ruru.plastic.user.net.AdminRequired;
import com.ruru.plastic.user.net.CurrentAdminUser;
import com.ruru.plastic.user.request.MemberLogRequest;
import com.ruru.plastic.user.request.PersonalCertRequest;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.response.PersonalCertResponse;
import com.ruru.plastic.user.service.*;
import com.ruru.plastic.user.task.CertTask;
import com.xiaoleilu.hutool.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/admin/personal/cert")
public class AdminPersonalCertController {

    @Autowired
    private PersonalCertService personalCertService;
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
    @Value("${cert.personal.free.day}")
    private Integer freeDays;

    @AdminRequired
    @PostMapping("/info")
    public DataResponse<PersonalCert> getPersonalCertById(@RequestBody PersonalCert personalCert) {
        if (personalCert == null || personalCert.getId() == null) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        PersonalCert personalCertById = personalCertService.getPersonalCertById(personalCert.getId());
        if (personalCertById == null) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        return DataResponse.success(personalCertById);
    }

    @AdminRequired
    @PostMapping("/audit/ok")
    public DataResponse<PersonalCert> createPersonalCert(@RequestBody PersonalCert personalCert, @CurrentAdminUser AdminUser adminUser) {
        if (personalCert == null || personalCert.getId() == null) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        PersonalCert personalCertById = personalCertService.getPersonalCertById(personalCert.getId());
        if (personalCertById == null) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }

        User userById = userService.getUserById(personalCertById.getUserId());
        if(userById==null){
            return DataResponse.error("用户信息错误！");
        }
        if(userById.getCertLevel().equals(UserCertLevelEnum.企业认证.getValue())){
            return DataResponse.error("用户已经企业认证，不能再审核个人认证");
        }

        if (!Arrays.asList(CertStatusEnum.待审核.getValue(), CertStatusEnum.审核失败.getValue()).contains(personalCertById.getStatus())) {
            return DataResponse.error("当前认证状态为" + CertStatusEnum.getEnum(personalCertById.getStatus()).name() + "，不能审核！");
        }

        personalCertById.setStatus(CertStatusEnum.审核通过.getValue());
        Msg<PersonalCert> msg = personalCertService.updatePersonalCert(personalCertById);
        if (StringUtils.isNotEmpty(msg.getErrorMsg())) {
            return DataResponse.error(msg.getErrorMsg());
        }
        Msg<User> userMsg = userService.updateUser(new User() {{
            setId(personalCertById.getUserId());
            setCertLevel(UserCertLevelEnum.个人认证.getValue());
        }});
        if (StringUtils.isNotEmpty(userMsg.getErrorMsg())) {
            return DataResponse.error(userMsg.getErrorMsg());
        }
        userById = userMsg.getData();

        certificateLogService.createCertificateLog(new CertificateLog() {{
            setOperatorType(OperatorTypeEnum.工作人员.getValue());
            setOperatorId(adminUser.getId());
            setLordId(msg.getData().getId());
            setCertLevel(UserCertLevelEnum.个人认证.getValue());
            setLordType(CertLordTypeEnum.身份证.getValue());
            setCertStatus(msg.getData().getStatus());
        }});


        certTask.createCertMessage(personalCertById, NotifyCodeEnum.个人认证_审核通过, adminUser);
        certTask.createPush(new PushBody() {{
            setNotifyCode(NotifyCodeEnum.个人认证_审核通过);
            setUserIds(Collections.singletonList(personalCertById.getUserId()));

            Map<String, String> extras = new HashMap<>();
            extras.put("id", personalCertById.getId().toString());
            extras.put("notifyCode", NotifyCodeEnum.个人认证_审核通过.getCode());
            setExtras(extras);
        }});

        //赠送7天VIP会员
        //检查是不是已经赠送过
        List<MemberLog> memberLogList = memberLogService.queryMemberLog(new MemberLogRequest() {{
            setUserId(personalCertById.getUserId());
            setActionType(MemberActionTypeEnum.个人认证赠送.getValue());
        }});

        if(memberLogList.size()==0){

            Member validMemberByUserId = memberService.getValidMemberByUserId(userById.getId());
            Msg<Member> memberMsg;
            if(validMemberByUserId==null){
                memberMsg = memberService.createMember(new Member() {{
                    setUserId(personalCertById.getUserId());
                    setStatus(StatusEnum.可用.getValue());
                    setBeginTime(new Date());
                    setOverTime(DateUtil.offsiteDay(new Date(), freeDays));
                }});
            }else{
                validMemberByUserId.setOverTime(DateUtil.offsiteDay(validMemberByUserId.getOverTime(),freeDays));
                memberMsg = memberService.updateMember(validMemberByUserId);
            }
            if(StringUtils.isNotEmpty(memberMsg.getErrorMsg())){
                return DataResponse.error(memberMsg.getErrorMsg());
            }

            userById.setMemberLevel(UserMemberLevelEnum.付费用户.getValue());
            userService.updateUser(userById);
            memberLogService.createMemberLog(new MemberLog(){{
                setUserId(personalCertById.getUserId());
                setActionType(MemberActionTypeEnum.个人认证赠送.getValue());
                setDays(freeDays);
                setRemark("个人认证赠送");
            }});
        }

        return DataResponse.success(msg.getData());
    }

    @AdminRequired
    @PostMapping("/audit/fail")
    public DataResponse<PersonalCert> updatePersonalCert(@RequestBody PersonalCert personalCert, @CurrentAdminUser AdminUser adminUser) {
        if (personalCert == null || personalCert.getId() == null) {
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        PersonalCert personalCertById = personalCertService.getPersonalCertById(personalCert.getId());
        if (personalCertById == null) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }

        if (!Arrays.asList(CertStatusEnum.待审核.getValue(), CertStatusEnum.审核通过.getValue()).contains(personalCertById.getStatus())) {
            return DataResponse.error("当前认证状态为" + CertStatusEnum.getEnum(personalCertById.getStatus()).name() + "，不能审核！");
        }

        User userById = userService.getUserById(personalCertById.getUserId());
        if(userById==null){
            return DataResponse.error("用户信息错误！");
        }

        if(userById.getCertLevel().equals(UserCertLevelEnum.企业认证.getValue())){
            return DataResponse.error("用户已经企业认证，不能再审核个人认证");
        }

        boolean fromOk = personalCertById.getStatus().equals(CertStatusEnum.审核通过.getValue());

        personalCertById.setStatus(CertStatusEnum.审核失败.getValue());
        Msg<PersonalCert> msg = personalCertService.updatePersonalCert(personalCertById);
        if (StringUtils.isNotEmpty(msg.getErrorMsg())) {
            return DataResponse.error(msg.getErrorMsg());
        }

        certificateLogService.createCertificateLog(new CertificateLog() {{
            setOperatorType(OperatorTypeEnum.工作人员.getValue());
            setOperatorId(adminUser.getId());
            setLordId(msg.getData().getId());
            setLordType(CertLordTypeEnum.身份证.getValue());
            setCertLevel(UserCertLevelEnum.个人认证.getValue());
            setCertStatus(msg.getData().getStatus());
        }});

        certTask.createCertMessage(personalCertById, NotifyCodeEnum.个人认证_审核不通过, adminUser);

        //如果原来认证成功，回退到未认证;
        if(fromOk){
            userById.setCertLevel(UserCertLevelEnum.未认证.getValue());
            userService.updateUser(userById);
        }

        certTask.createPush(new PushBody() {{
            setNotifyCode(NotifyCodeEnum.个人认证_审核不通过);
            setUserIds(Collections.singletonList(personalCertById.getUserId()));

            Map<String, String> extras = new HashMap<>();
            extras.put("id", personalCertById.getId().toString());
            extras.put("notifyCode", NotifyCodeEnum.个人认证_审核不通过.getCode());
            setExtras(extras);
        }});

        return DataResponse.success(msg.getData());
    }

    @AdminRequired
    @PostMapping("/delete")
    public DataResponse<Integer> deletePersonalCert(@RequestBody PersonalCert personalCert) {
        Msg<Integer> msg = personalCertService.deletePersonalCert(personalCert);
        if (StringUtils.isNotEmpty(msg.getErrorMsg())) {
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @AdminRequired
    @PostMapping("/filter")
    public DataResponse<PageInfo<PersonalCertResponse>> filterPersonalCertResponse(@RequestBody PersonalCertRequest request) {
        return DataResponse.success(personalCertService.filterPersonalCertResponse(request));
    }

}
