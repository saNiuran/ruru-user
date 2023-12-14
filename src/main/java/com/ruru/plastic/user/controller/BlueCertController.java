package com.ruru.plastic.user.controller;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.bean.PushBody;
import com.ruru.plastic.user.enume.*;
import com.ruru.plastic.user.model.BlueCert;
import com.ruru.plastic.user.model.CertificateLog;
import com.ruru.plastic.user.model.User;
import com.ruru.plastic.user.net.CurrentUser;
import com.ruru.plastic.user.net.LoginRequired;
import com.ruru.plastic.user.request.BlueCertRequest;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.service.CertificateLogService;
import com.ruru.plastic.user.service.BlueCertService;
import com.ruru.plastic.user.task.CertTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blue/cert")
public class BlueCertController {
    
    @Autowired
    private BlueCertService blueCertService;
    @Autowired
    private CertificateLogService certificateLogService;
    @Autowired
    private CertTask certTask;

    @LoginRequired
    @PostMapping("/info")
    public DataResponse<BlueCert> getBlueCertById(@RequestBody BlueCert blueCert){
        if(blueCert==null || blueCert.getId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        BlueCert blueCertById = blueCertService.getBlueCertById(blueCert.getId());
        if(blueCertById==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        return DataResponse.success(blueCertById);
    }

    @LoginRequired
    @PostMapping("/info/my")
    public DataResponse<BlueCert> getMyBlueCert(@CurrentUser User user){
        List<BlueCert> list = blueCertService.queryBlueCert(new BlueCertRequest() {{
            setUserId(user.getId());
        }});

        if(list.size()==0){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        return DataResponse.success(list.get(0));
    }

    @LoginRequired
    @PostMapping("/new")
    public DataResponse<BlueCert> createBlueCert(@RequestBody BlueCert blueCert, @CurrentUser User user){
        if(blueCert==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }

        blueCert.setUserId(user.getId());
        Msg<BlueCert> certMsg = blueCertService.createBlueCert(blueCert);

        if(StringUtils.isNotEmpty(certMsg.getErrorMsg())){
            return DataResponse.error(certMsg.getErrorMsg());
        }

        CertificateLog certificateLog = new CertificateLog();
        certificateLog.setOperatorType(OperatorTypeEnum.用户.getValue());
        certificateLog.setOperatorId(user.getId());
        certificateLog.setLordId(certMsg.getData().getId());
        certificateLog.setLordType(CertLordTypeEnum.营业执照.getValue());
        certificateLog.setCertLevel(UserCertLevelEnum.蓝V认证.getValue());
        certificateLog.setCertStatus(certMsg.getData().getStatus());
        certificateLogService.createCertificateLog(certificateLog);



        return DataResponse.success(certMsg.getData());
    }

    @LoginRequired
    @PostMapping("/update")
    public DataResponse<BlueCert> updateBlueCert(@RequestBody BlueCert blueCert, @CurrentUser User user){
        if(blueCert==null || blueCert.getId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }

        BlueCert blueCertById = blueCertService.getBlueCertById(blueCert.getId());

        if(blueCertById.getStatus().equals(CertStatusEnum.审核通过.getValue())
                || blueCertById.getStatus().equals(CertStatusEnum.失效.getValue())){
            return DataResponse.error("当前状态为"+CertStatusEnum.getEnum(blueCertById.getStatus())+"，不能修改！");
        }

        blueCert.setId(blueCert.getId());
        blueCert.setStatus(CertStatusEnum.待付款.getValue());
        Msg<BlueCert> msg = blueCertService.updateBlueCert(blueCert);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }

        certificateLogService.createCertificateLog(new CertificateLog(){{
            setOperatorType(OperatorTypeEnum.用户.getValue());
            setOperatorId(user.getId());
            setLordId(msg.getData().getId());
            setLordType(CertLordTypeEnum.营业执照.getValue());
            setCertLevel(UserCertLevelEnum.蓝V认证.getValue());
            setCertStatus(msg.getData().getStatus());
        }});


        //创建管理员通知消息
        certTask.createCertMessage(msg.getData(), NotifyCodeEnum.蓝V认证_待审核_管理,null);

        certTask.createPush(new PushBody() {{
            setNotifyCode(NotifyCodeEnum.蓝V认证_待审核_管理);
            setUserIds(Collections.singletonList(0L));

            Map<String, String> extras = new HashMap<>();
            extras.put("id",msg.getData().getId().toString());
            extras.put("notifyCode", NotifyCodeEnum.蓝V认证_待审核_管理.getCode());
            setExtras(extras);
        }});

        return DataResponse.success(msg.getData());
    }

    @LoginRequired
    @PostMapping("/delete")
    public DataResponse<BlueCert> deleteBlueCert(@RequestBody BlueCert blueCert){
        Msg<BlueCert> msg = blueCertService.deleteBlueCert(blueCert);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @LoginRequired
    @PostMapping("/filter")
    public DataResponse<PageInfo<BlueCert>> filterBlueCert(@RequestBody BlueCertRequest request){
        return DataResponse.success(blueCertService.filterBlueCert(request));
    }


    @PostMapping("/pay/success")
    public DataResponse<Void> paySuccessBlueCert(@RequestBody BlueCert blueCert){
        CertificateLog certificateLog = new CertificateLog();
        certificateLog.setOperatorType(OperatorTypeEnum.系统.getValue());
        certificateLog.setOperatorId(0L);
        certificateLog.setLordId(blueCert.getId());
        certificateLog.setLordType(CertLordTypeEnum.营业执照.getValue());
        certificateLog.setCertLevel(UserCertLevelEnum.蓝V认证.getValue());
        certificateLog.setCertStatus(CertStatusEnum.待付款.getValue());
        certificateLogService.createCertificateLog(certificateLog);


        //创建管理员通知消息
        certTask.createCertMessage(blueCertService.getBlueCertById(blueCert.getId()), NotifyCodeEnum.蓝V认证_待审核_管理, null);
        certTask.createPush(new PushBody() {{
            setNotifyCode(NotifyCodeEnum.蓝V认证_待审核_管理);
            setUserIds(Collections.singletonList(0L));

            Map<String, String> extras = new HashMap<>();
            extras.put("id", blueCert.getId().toString());
            extras.put("notifyCode", NotifyCodeEnum.蓝V认证_待审核_管理.getCode());
            setExtras(extras);
        }});
        return DataResponse.success(null);
    }
}
