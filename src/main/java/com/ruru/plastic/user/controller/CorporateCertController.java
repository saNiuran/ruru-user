package com.ruru.plastic.user.controller;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.bean.PushBody;
import com.ruru.plastic.user.enume.*;
import com.ruru.plastic.user.model.CertificateLog;
import com.ruru.plastic.user.model.CorporateCert;
import com.ruru.plastic.user.model.User;
import com.ruru.plastic.user.model.UserCorporateCertMatch;
import com.ruru.plastic.user.net.CurrentUser;
import com.ruru.plastic.user.net.LoginRequired;
import com.ruru.plastic.user.request.CorporateCertRequest;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.service.CertificateLogService;
import com.ruru.plastic.user.service.CorporateCertService;
import com.ruru.plastic.user.service.UserCorporateCertMatchService;
import com.ruru.plastic.user.task.CertTask;
import com.xiaoleilu.hutool.util.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/corporation/cert")
public class CorporateCertController {
    
    @Autowired
    private CorporateCertService corporateCertService;
    @Autowired
    private CertificateLogService certificateLogService;
    @Autowired
    private UserCorporateCertMatchService userCorporateCertMatchService;
    @Autowired
    private CertTask certTask;

    @LoginRequired
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

    @LoginRequired
    @PostMapping("/info/my")
    public DataResponse<CorporateCert> getMyCorporateCert(@CurrentUser User user){
        UserCorporateCertMatch userCorporateCertMatchByUserId = userCorporateCertMatchService.getUserCorporateCertMatchByUserId(user.getId());
        if(userCorporateCertMatchByUserId==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }

        CorporateCert corporateCertById = corporateCertService.getCorporateCertById(userCorporateCertMatchByUserId.getCorporateCertId());
        if(corporateCertById==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        return DataResponse.success(corporateCertById);
    }

    @LoginRequired
    @PostMapping("/new")
    public DataResponse<CorporateCert> createCorporateCert(@RequestBody CorporateCert corporateCert, @CurrentUser User user){
        if(user.getCertLevel()==null || user.getCertLevel()<(UserCertLevelEnum.个人认证.getValue())){
            return DataResponse.error("请在完成个人认证后，再进行企业认证！");
        }

        if(corporateCert==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }

        if(StringUtils.isEmpty(corporateCert.getSocialCode())){
            corporateCert.setSocialCode("RR"+ RandomUtil.randomNumbers(8));
        }

        boolean needAudit = true;

        CorporateCert corporateCertBySocialCode = corporateCertService.getCorporateCertBySocialCode(corporateCert.getSocialCode());
        if(corporateCertBySocialCode==null){
            Msg<CorporateCert> msg = corporateCertService.createCorporateCert(corporateCert);
            if(StringUtils.isNotEmpty(msg.getErrorMsg())){
                return DataResponse.error(msg.getErrorMsg());
            }
            corporateCertBySocialCode = msg.getData();
        }else{
            needAudit = false;
        }

        UserCorporateCertMatch match = new UserCorporateCertMatch();
        match.setUserId(user.getId());
        match.setCorporateCertId(corporateCertBySocialCode.getId());
        Msg<UserCorporateCertMatch> matchMsg = userCorporateCertMatchService.createUserCorporateCertMatch(match);

        if(StringUtils.isNotEmpty(matchMsg.getErrorMsg())){
            return DataResponse.error(matchMsg.getErrorMsg());
        }

        CertificateLog certificateLog = new CertificateLog();
        certificateLog.setOperatorType(OperatorTypeEnum.用户.getValue());
        certificateLog.setOperatorId(user.getId());
        certificateLog.setLordId(corporateCertBySocialCode.getId());
        certificateLog.setLordType(CertLordTypeEnum.营业执照.getValue());
        certificateLog.setCertLevel(UserCertLevelEnum.企业认证.getValue());
        certificateLog.setCertStatus(corporateCertBySocialCode.getStatus());
        certificateLogService.createCertificateLog(certificateLog);

        if(needAudit) {
            //创建管理员通知消息
            certTask.createCertMessage(matchMsg.getData(), NotifyCodeEnum.企业认证_待审核_管理, null);
            certTask.createPush(new PushBody() {{
                setNotifyCode(NotifyCodeEnum.企业认证_待审核_管理.getCode());
                setUserIds(Collections.singletonList(0L));

                Map<String, String> extras = new HashMap<>();
                extras.put("id", matchMsg.getData().getId().toString());
                extras.put("notifyCode", NotifyCodeEnum.企业认证_待审核_管理.getCode());
                setExtras(extras);
            }});
        }

        return DataResponse.success(corporateCertBySocialCode);
    }

    @LoginRequired
    @PostMapping("/update")
    public DataResponse<CorporateCert> updateCorporateCert(@RequestBody CorporateCert corporateCert, @CurrentUser User user){
        if(corporateCert==null || corporateCert.getId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }

        UserCorporateCertMatch userCorporateCertMatchByUserId = userCorporateCertMatchService.getUserCorporateCertMatchByUserId(user.getId());
        if(userCorporateCertMatchByUserId==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }

        if(!userCorporateCertMatchByUserId.getCorporateCertId().equals(corporateCert.getId())){
            return DataResponse.error("非自己的企业信息");
        }

        CorporateCert corporateCertById = corporateCertService.getCorporateCertById(userCorporateCertMatchByUserId.getCorporateCertId());

        if(corporateCertById.getStatus().equals(CertStatusEnum.审核通过.getValue())
                || corporateCertById.getStatus().equals(CertStatusEnum.失效.getValue())){
            return DataResponse.error("当前状态为"+CertStatusEnum.getEnum(corporateCertById.getStatus())+"，不能修改！");
        }

        corporateCert.setId(corporateCert.getId());
        corporateCert.setStatus(CertStatusEnum.待审核.getValue());
        Msg<CorporateCert> msg = corporateCertService.updateCorporateCert(corporateCert);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }

        userCorporateCertMatchService.updateUserCorporateCertMatch(userCorporateCertMatchByUserId); //修改更新时间，这样在前端会排序到最前面

        certificateLogService.createCertificateLog(new CertificateLog(){{
            setOperatorType(OperatorTypeEnum.用户.getValue());
            setOperatorId(user.getId());
            setLordId(msg.getData().getId());
            setLordType(CertLordTypeEnum.营业执照.getValue());
            setCertLevel(UserCertLevelEnum.企业认证.getValue());
            setCertStatus(msg.getData().getStatus());
        }});


        //创建管理员通知消息
        certTask.createCertMessage(userCorporateCertMatchByUserId, NotifyCodeEnum.企业认证_待审核_管理,null);

        certTask.createPush(new PushBody() {{
            setNotifyCode(NotifyCodeEnum.企业认证_待审核_管理.getCode());
            setUserIds(Collections.singletonList(0L));

            Map<String, String> extras = new HashMap<>();
            extras.put("id",userCorporateCertMatchByUserId.getId().toString());
            extras.put("notifyCode", NotifyCodeEnum.企业认证_待审核_管理.getCode());
            setExtras(extras);
        }});

        return DataResponse.success(msg.getData());
    }

    @LoginRequired
    @PostMapping("/delete")
    public DataResponse<Integer> deleteCorporateCert(@RequestBody CorporateCert corporateCert){
        Msg<Integer> msg = corporateCertService.deleteCorporateCert(corporateCert);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @LoginRequired
    @PostMapping("/filter")
    public DataResponse<PageInfo<CorporateCert>> filterCorporateCert(@RequestBody CorporateCertRequest request){
        return DataResponse.success(corporateCertService.filterCorporateCert(request));
    }

}
