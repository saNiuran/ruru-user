package com.ruru.plastic.user.controller;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.enume.CertLordTypeEnum;
import com.ruru.plastic.user.enume.OperatorTypeEnum;
import com.ruru.plastic.user.enume.UserCertLevelEnum;
import com.ruru.plastic.user.model.CertificateLog;
import com.ruru.plastic.user.model.PersonalCert;
import com.ruru.plastic.user.model.User;
import com.ruru.plastic.user.net.CurrentUser;
import com.ruru.plastic.user.net.LoginRequired;
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

@RestController
@RequestMapping("/personal/auth")
public class PersonalCertController {
    
    @Autowired
    private PersonalCertService personalCertService;
    @Autowired
    private CertificateLogService certificateLogService;

    @LoginRequired
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

    @LoginRequired
    @PostMapping("/new")
    public DataResponse<PersonalCert> createPersonalCert(@RequestBody PersonalCert personalCert, @CurrentUser User user){
        Msg<PersonalCert> msg = personalCertService.createPersonalCert(personalCert);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }

        certificateLogService.createCertificateLog(new CertificateLog(){{
            setOperatorType(OperatorTypeEnum.用户.getValue());
            setOperatorId(user.getId());
            setLordId(msg.getData().getId());
            setLordType(CertLordTypeEnum.身份证.getValue());
            setCertLevel(UserCertLevelEnum.个人认证.getValue());
            setCertStatus(msg.getData().getStatus());
        }});

        return DataResponse.success(msg.getData());
    }

    @LoginRequired
    @PostMapping("/update")
    public DataResponse<PersonalCert> updatePersonalCert(@RequestBody PersonalCert personalCert, @CurrentUser User user){
        Msg<PersonalCert> msg = personalCertService.updatePersonalCert(personalCert);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }

        certificateLogService.createCertificateLog(new CertificateLog(){{
            setOperatorType(OperatorTypeEnum.用户.getValue());
            setOperatorId(user.getId());
            setLordId(msg.getData().getId());
            setLordType(CertLordTypeEnum.身份证.getValue());
            setCertLevel(UserCertLevelEnum.个人认证.getValue());
            setCertStatus(msg.getData().getStatus());
        }});

        return DataResponse.success(msg.getData());
    }

    @LoginRequired
    @PostMapping("/delete")
    public DataResponse<Integer> deletePersonalCert(@RequestBody PersonalCert personalCert){
        Msg<Integer> msg = personalCertService.deletePersonalCert(personalCert);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @LoginRequired
    @PostMapping("/filter")
    public DataResponse<PageInfo<PersonalCertResponse>> filterPersonalCertResponse(@RequestBody PersonalCertRequest request){
        return DataResponse.success(personalCertService.filterPersonalCertResponse(request));
    }

}
