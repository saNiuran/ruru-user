package com.ruru.plastic.user.controller.admin;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.bean.PushBody;
import com.ruru.plastic.user.enume.*;
import com.ruru.plastic.user.model.*;
import com.ruru.plastic.user.net.AdminRequired;
import com.ruru.plastic.user.net.CurrentAdminUser;
import com.ruru.plastic.user.request.CorporateCertRequest;
import com.ruru.plastic.user.request.MemberLogRequest;
import com.ruru.plastic.user.request.UserCorporateCertMatchRequest;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.response.UserCorporateCertMatchResponse;
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
@RequestMapping("/admin/corporation/cert")
public class AdminCorporateCertController {

    @Autowired
    private CorporateCertService corporateCertService;
    @Autowired
    private CertificateLogService certificateLogService;
    @Autowired
    private UserCorporateCertMatchService userCorporateCertMatchService;
    @Autowired
    private CertTask certTask;
    @Autowired
    private UserService userService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberLogService memberLogService;
    @Value("${cert.corporate.free.day}")
    private Integer freeDays;

    @AdminRequired
    @PostMapping("/info")
    public DataResponse<CorporateCert> getCorporateCertById(@RequestBody CorporateCert corporateCert){
        if(corporateCert==null || corporateCert.getId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        CorporateCert corporateCertById = corporateCertService.getCorporateCertById(corporateCert.getId());
        if(corporateCertById==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        return DataResponse.success(corporateCertById);
    }

    @AdminRequired
    @PostMapping("/audit/ok")
    public DataResponse<CorporateCert> auditOkCorporateCert(@RequestBody UserCorporateCertMatch userCorporateCertMatch, @CurrentAdminUser AdminUser adminUser){
        if(userCorporateCertMatch==null || userCorporateCertMatch.getId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }

        UserCorporateCertMatch userCorporateCertMatchById = userCorporateCertMatchService.getUserCorporateCertMatchById(userCorporateCertMatch.getId());
        if(userCorporateCertMatchById==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }

        User userById = userService.getUserById(userCorporateCertMatchById.getUserId());
        if(userById==null){
            return DataResponse.error("用户信息错误！");
        }
        if(userById.getCertLevel().equals(UserCertLevelEnum.未认证.getValue())){
            return DataResponse.error("个人认证后，才能审核企业认证！");
        }

        CorporateCert corporateCertById = corporateCertService.getCorporateCertById(userCorporateCertMatchById.getCorporateCertId());
        if(corporateCertById==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }

        if(!Arrays.asList(CertStatusEnum.待审核.getValue(), CertStatusEnum.审核失败.getValue()).contains(corporateCertById.getStatus())){
            return DataResponse.error("当前认证状态为"+ CertStatusEnum.getEnum(corporateCertById.getStatus()).name()+"，不能审核！");
        }

        corporateCertById.setStatus(CertStatusEnum.审核通过.getValue());
        Msg<CorporateCert> msg = corporateCertService.updateCorporateCert(corporateCertById);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }

        certificateLogService.createCertificateLog(new CertificateLog(){{
            setOperatorType(OperatorTypeEnum.工作人员.getValue());
            setOperatorId(adminUser.getId());
            setLordId(msg.getData().getId());
            setLordType(CertLordTypeEnum.营业执照.getValue());
            setCertLevel(UserCertLevelEnum.企业认证.getValue());
            setCertStatus(msg.getData().getStatus());
        }});

        Msg<User> userMsg = userService.updateUser(new User(){{
            setId(userCorporateCertMatchById.getUserId());
            setCertLevel(UserCertLevelEnum.企业认证.getValue());
        }});
        if (StringUtils.isNotEmpty(userMsg.getErrorMsg())) {
            return DataResponse.error(userMsg.getErrorMsg());
        }
        userById = userMsg.getData();


        certTask.createCertMessage(userCorporateCertMatchById, NotifyCodeEnum.企业认证_审核通过,adminUser);

        certTask.createPush(new PushBody() {{
            setNotifyCode(NotifyCodeEnum.企业认证_审核通过);
            setUserIds(Collections.singletonList(userCorporateCertMatchById.getUserId()));

            Map<String, String> extras = new HashMap<>();
            extras.put("id", userCorporateCertMatchById.getId().toString());
            extras.put("notifyCode", NotifyCodeEnum.企业认证_审核通过.getCode());
            setExtras(extras);
        }});

        //赠送7天VIP会员
        //检查是不是已经赠送过
        List<MemberLog> memberLogList = memberLogService.queryMemberLog(new MemberLogRequest() {{
            setUserId(userCorporateCertMatchById.getUserId());
            setActionType(MemberActionTypeEnum.企业认证赠送.getValue());
        }});

        if(memberLogList.size()==0){

            Member validMemberByUserId = memberService.getValidMemberByUserId(userById.getId());
            Msg<Member> memberMsg;
            if(validMemberByUserId==null){
                memberMsg = memberService.createMember(new Member() {{
                    setUserId(userCorporateCertMatchById.getUserId());
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

            if(StringUtils.isEmpty(memberMsg.getErrorMsg())){
                userById.setMemberLevel(UserMemberLevelEnum.付费用户.getValue());
                userService.updateUser(userById);
            }
            memberLogService.createMemberLog(new MemberLog(){{
                setUserId(userCorporateCertMatchById.getUserId());
                setActionType(MemberActionTypeEnum.企业认证赠送.getValue());
                setDays(freeDays);
                setRemark("企业认证赠送");
            }});
        }

        return DataResponse.success(msg.getData());
    }

    @AdminRequired
    @PostMapping("/audit/fail")
    public DataResponse<CorporateCert> auditFailCorporateCert(@RequestBody UserCorporateCertMatch userCorporateCertMatch, @CurrentAdminUser AdminUser adminUser){
        if(userCorporateCertMatch==null || userCorporateCertMatch.getId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }

        UserCorporateCertMatch userCorporateCertMatchById = userCorporateCertMatchService.getUserCorporateCertMatchById(userCorporateCertMatch.getId());
        if(userCorporateCertMatchById==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }

        User userById = userService.getUserById(userCorporateCertMatchById.getUserId());
        if(userById==null){
            return DataResponse.error("用户信息错误！");
        }
        if(userById.getCertLevel().equals(UserCertLevelEnum.未认证.getValue())){
            return DataResponse.error("个人认证后，才能审核企业认证！");
        }

        CorporateCert corporateCertById = corporateCertService.getCorporateCertById(userCorporateCertMatchById.getId());
        if(corporateCertById==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }

        if(!Arrays.asList(CertStatusEnum.待审核.getValue(), CertStatusEnum.审核通过.getValue()).contains(corporateCertById.getStatus())){
            return DataResponse.error("当前认证状态为"+ CertStatusEnum.getEnum(corporateCertById.getStatus()).name()+"，不能审核！");
        }

        boolean fromOk = corporateCertById.getStatus().equals(CertStatusEnum.审核通过.getValue());

        corporateCertById.setStatus(CertStatusEnum.审核失败.getValue());
        Msg<CorporateCert> msg = corporateCertService.updateCorporateCert(corporateCertById);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }

        certificateLogService.createCertificateLog(new CertificateLog(){{
            setOperatorType(OperatorTypeEnum.工作人员.getValue());
            setOperatorId(adminUser.getId());
            setLordId(msg.getData().getId());
            setLordType(CertLordTypeEnum.营业执照.getValue());
            setCertLevel(UserCertLevelEnum.企业认证.getValue());
            setCertStatus(msg.getData().getStatus());
        }});

        certTask.createCertMessage(userCorporateCertMatchById, NotifyCodeEnum.企业认证_审核不通过,adminUser);

        //如果原来认证成功，回退到个人认证;
        if(fromOk){
            userById.setCertLevel(UserCertLevelEnum.个人认证.getValue());
            userService.updateUser(userById);
        }

        certTask.createPush(new PushBody() {{
            setNotifyCode(NotifyCodeEnum.企业认证_审核不通过);
            setUserIds(Collections.singletonList(userCorporateCertMatchById.getUserId()));

            Map<String, String> extras = new HashMap<>();
            extras.put("id", userCorporateCertMatchById.getId().toString());
            extras.put("notifyCode", NotifyCodeEnum.企业认证_审核不通过.getCode());
            setExtras(extras);
        }});

        return DataResponse.success(msg.getData());
    }

    @AdminRequired
    @PostMapping("/delete")
    public DataResponse<Integer> deleteCorporateCert(@RequestBody CorporateCert corporateCert){
        Msg<Integer> msg = corporateCertService.deleteCorporateCert(corporateCert);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @AdminRequired
    @PostMapping("/filter")
    public DataResponse<PageInfo<CorporateCert>> filterCorporateCert(@RequestBody CorporateCertRequest request){
        return DataResponse.success(corporateCertService.filterCorporateCert(request));
    }

    @AdminRequired
    @PostMapping("/match/filter")
    public DataResponse<PageInfo<UserCorporateCertMatchResponse>> filterCorporateCert(@RequestBody UserCorporateCertMatchRequest request){
        return DataResponse.success(userCorporateCertMatchService.filterUserCorporateCertMatchResponse(request));
    }

}
