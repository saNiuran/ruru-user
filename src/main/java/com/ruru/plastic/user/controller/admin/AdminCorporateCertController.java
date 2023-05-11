package com.ruru.plastic.user.controller.admin;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.enume.*;
import com.ruru.plastic.user.model.*;
import com.ruru.plastic.user.net.AdminRequired;
import com.ruru.plastic.user.net.CurrentAdminUser;
import com.ruru.plastic.user.request.CorporateCertRequest;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.service.CertificateLogService;
import com.ruru.plastic.user.service.CorporateCertService;
import com.ruru.plastic.user.service.UserCorporateCertMatchService;
import com.ruru.plastic.user.service.UserService;
import com.ruru.plastic.user.task.CertTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

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
            setOperatorType(OperatorTypeEnum.管理员.getValue());
            setOperatorId(adminUser.getId());
            setLordId(msg.getData().getId());
            setLordType(CertLordTypeEnum.营业执照.getValue());
            setCertLevel(UserCertLevelEnum.企业认证.getValue());
            setCertStatus(msg.getData().getStatus());
        }});

        userService.updateUser(new User(){{
            setId(userCorporateCertMatchById.getUserId());
            setCertLevel(UserCertLevelEnum.企业认证.getValue());
        }});

        certTask.createCertMessage(userCorporateCertMatchById, NotifyCodeEnum.企业认证_审核通过);

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
        CorporateCert corporateCertById = corporateCertService.getCorporateCertById(userCorporateCertMatchById.getId());
        if(corporateCertById==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }

        if(!Arrays.asList(CertStatusEnum.待审核.getValue(), CertStatusEnum.审核通过.getValue()).contains(corporateCertById.getStatus())){
            return DataResponse.error("当前认证状态为"+ CertStatusEnum.getEnum(corporateCertById.getStatus()).name()+"，不能审核！");
        }

        corporateCertById.setStatus(CertStatusEnum.审核失败.getValue());
        Msg<CorporateCert> msg = corporateCertService.updateCorporateCert(corporateCertById);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }

        certificateLogService.createCertificateLog(new CertificateLog(){{
            setOperatorType(OperatorTypeEnum.管理员.getValue());
            setOperatorId(adminUser.getId());
            setLordId(msg.getData().getId());
            setLordType(CertLordTypeEnum.营业执照.getValue());
            setCertLevel(UserCertLevelEnum.企业认证.getValue());
            setCertStatus(msg.getData().getStatus());
        }});

        certTask.createCertMessage(userCorporateCertMatchById, NotifyCodeEnum.企业认证_审核不通过);

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

}
