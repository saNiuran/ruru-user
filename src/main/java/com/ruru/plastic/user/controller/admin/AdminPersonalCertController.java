package com.ruru.plastic.user.controller.admin;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.enume.CertLordTypeEnum;
import com.ruru.plastic.user.enume.CertStatusEnum;
import com.ruru.plastic.user.enume.OperatorTypeEnum;
import com.ruru.plastic.user.enume.UserCertLevelEnum;
import com.ruru.plastic.user.model.AdminUser;
import com.ruru.plastic.user.model.CertificateLog;
import com.ruru.plastic.user.model.PersonalCert;
import com.ruru.plastic.user.net.AdminRequired;
import com.ruru.plastic.user.net.CurrentAdminUser;
import com.ruru.plastic.user.request.PersonalCertRequest;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.response.PersonalCertResponse;
import com.ruru.plastic.user.service.CertificateLogService;
import com.ruru.plastic.user.service.PersonalCertService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/admin/personal/cert")
public class AdminPersonalCertController {
    
    @Autowired
    private PersonalCertService personalCertService;
    @Autowired
    private CertificateLogService certificateLogService;

    @AdminRequired
    @PostMapping("/info")
    public DataResponse<PersonalCert> getPersonalCertById(@RequestBody PersonalCert personalCert){
        if(personalCert==null || personalCert.getId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        PersonalCert personalCertById = personalCertService.getPersonalCertById(personalCert.getId());
        if(personalCertById==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        return DataResponse.success(personalCertById);
    }

    @AdminRequired
    @PostMapping("/audit/ok")
    public DataResponse<PersonalCert> createPersonalCert(@RequestBody PersonalCert personalCert, @CurrentAdminUser AdminUser adminUser){
        if(personalCert==null || personalCert.getId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        PersonalCert personalCertById = personalCertService.getPersonalCertById(personalCert.getId());
        if(personalCertById==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }

        if(!Arrays.asList(CertStatusEnum.待审核.getValue(), CertStatusEnum.审核失败.getValue()).contains(personalCertById.getStatus())){
            return DataResponse.error("当前认证状态为"+ CertStatusEnum.getEnum(personalCertById.getStatus()).name()+"，不能审核！");
        }

        personalCertById.setStatus(CertStatusEnum.审核通过.getValue());
        Msg<PersonalCert> msg = personalCertService.updatePersonalCert(personalCertById);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }

        certificateLogService.createCertificateLog(new CertificateLog(){{
            setOperatorType(OperatorTypeEnum.管理员.getValue());
            setOperatorId(adminUser.getId());
            setLordId(msg.getData().getId());
            setCertLevel(UserCertLevelEnum.个人认证.getValue());
            setLordType(CertLordTypeEnum.身份证.getValue());
            setCertStatus(msg.getData().getStatus());
        }});

        return DataResponse.success(msg.getData());
    }

    @AdminRequired
    @PostMapping("/audit/fail")
    public DataResponse<PersonalCert> updatePersonalCert(@RequestBody PersonalCert personalCert, @CurrentAdminUser AdminUser adminUser){
        if(personalCert==null || personalCert.getId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        PersonalCert personalCertById = personalCertService.getPersonalCertById(personalCert.getId());
        if(personalCertById==null){
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }

        if(!Arrays.asList(CertStatusEnum.待审核.getValue(), CertStatusEnum.审核通过.getValue()).contains(personalCertById.getStatus())){
            return DataResponse.error("当前认证状态为"+ CertStatusEnum.getEnum(personalCertById.getStatus()).name()+"，不能审核！");
        }

        personalCertById.setStatus(CertStatusEnum.审核失败.getValue());
        Msg<PersonalCert> msg = personalCertService.updatePersonalCert(personalCertById);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }

        certificateLogService.createCertificateLog(new CertificateLog(){{
            setOperatorType(OperatorTypeEnum.管理员.getValue());
            setOperatorId(adminUser.getId());
            setLordId(msg.getData().getId());
            setLordType(CertLordTypeEnum.身份证.getValue());
            setCertLevel(UserCertLevelEnum.个人认证.getValue());
            setCertStatus(msg.getData().getStatus());
        }});

        return DataResponse.success(msg.getData());
    }

    @AdminRequired
    @PostMapping("/delete")
    public DataResponse<Integer> deletePersonalCert(@RequestBody PersonalCert personalCert){
        Msg<Integer> msg = personalCertService.deletePersonalCert(personalCert);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @AdminRequired
    @PostMapping("/filter")
    public DataResponse<PageInfo<PersonalCertResponse>> filterPersonalCertResponse(@RequestBody PersonalCertRequest request){
        return DataResponse.success(personalCertService.filterPersonalCertResponse(request));
    }

}
