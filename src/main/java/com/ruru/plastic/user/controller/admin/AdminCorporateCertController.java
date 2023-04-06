package com.ruru.plastic.user.controller.admin;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.enume.CertLordTypeEnum;
import com.ruru.plastic.user.enume.CertStatusEnum;
import com.ruru.plastic.user.enume.UserCertLevelEnum;
import com.ruru.plastic.user.model.CertificateLog;
import com.ruru.plastic.user.model.CorporateCert;
import com.ruru.plastic.user.request.CorporateCertRequest;
import com.ruru.plastic.user.response.CorporateCertResponse;
import com.ruru.plastic.user.response.DataResponse;
import com.ruru.plastic.user.service.CertificateLogService;
import com.ruru.plastic.user.service.CorporateCertService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/admin/corporation/auth")
public class AdminCorporateCertController {

    @Autowired
    private CorporateCertService corporateCertService;
    @Autowired
    private CertificateLogService certificateLogService;

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

    @PostMapping("/audit/ok")
    public DataResponse<CorporateCert> createCorporateCert(@RequestBody CorporateCert corporateCert){
        if(corporateCert==null || corporateCert.getId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        CorporateCert corporateCertById = corporateCertService.getCorporateCertById(corporateCert.getId());
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
            setUserId(msg.getData().getUserId());
            setLordId(msg.getData().getId());
            setLordType(CertLordTypeEnum.营业执照.getValue());
            setCertLevel(UserCertLevelEnum.企业认证.getValue());
            setCertStatus(msg.getData().getStatus());
        }});

        return DataResponse.success(msg.getData());
    }

    @PostMapping("/audit/fail")
    public DataResponse<CorporateCert> updateCorporateCert(@RequestBody CorporateCert corporateCert){
        if(corporateCert==null || corporateCert.getId()==null){
            return DataResponse.error(Constants.ERROR_PARAMETER);
        }
        CorporateCert corporateCertById = corporateCertService.getCorporateCertById(corporateCert.getId());
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
            setUserId(msg.getData().getUserId());
            setLordId(msg.getData().getId());
            setLordType(CertLordTypeEnum.营业执照.getValue());
            setCertLevel(UserCertLevelEnum.企业认证.getValue());
            setCertStatus(msg.getData().getStatus());
        }});

        return DataResponse.success(msg.getData());
    }

    @PostMapping("/delete")
    public DataResponse<Integer> deleteCorporateCert(@RequestBody CorporateCert corporateCert){
        Msg<Integer> msg = corporateCertService.deleteCorporateCert(corporateCert);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @PostMapping("/filter")
    public DataResponse<PageInfo<CorporateCertResponse>> filterCorporateCertResponse(@RequestBody CorporateCertRequest request){
        return DataResponse.success(corporateCertService.filterCorporateCertResponse(request));
    }

}
