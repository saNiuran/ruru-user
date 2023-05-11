package com.ruru.plastic.user.controller;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.enume.CertLordTypeEnum;
import com.ruru.plastic.user.enume.NotifyCodeEnum;
import com.ruru.plastic.user.enume.OperatorTypeEnum;
import com.ruru.plastic.user.enume.UserCertLevelEnum;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        Msg<CorporateCert> msg = corporateCertService.createCorporateCert(corporateCert);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }

        Msg<UserCorporateCertMatch> matchMsg = userCorporateCertMatchService.createUserCorporateCertMatch(new UserCorporateCertMatch() {{
            setUserId(user.getId());
            setCorporateCertId(msg.getData().getId());
        }});

        certificateLogService.createCertificateLog(new CertificateLog(){{
            setOperatorType(OperatorTypeEnum.用户.getValue());
            setOperatorId(user.getId());
            setLordId(msg.getData().getId());
            setLordType(CertLordTypeEnum.营业执照.getValue());
            setCertLevel(UserCertLevelEnum.企业认证.getValue());
            setCertStatus(msg.getData().getStatus());
        }});

        //创建管理员通知消息
        certTask.createCertMessage(matchMsg.getData(), NotifyCodeEnum.企业认证_待审核_管理);

        return DataResponse.success(msg.getData());
    }

    @LoginRequired
    @PostMapping("/update")
    public DataResponse<CorporateCert> updateCorporateCert(@RequestBody CorporateCert corporateCert, @CurrentUser User user){
        Msg<CorporateCert> msg = corporateCertService.updateCorporateCert(corporateCert);
        if(StringUtils.isNotEmpty(msg.getErrorMsg())){
            return DataResponse.error(msg.getErrorMsg());
        }

        certificateLogService.createCertificateLog(new CertificateLog(){{
            setOperatorType(OperatorTypeEnum.用户.getValue());
            setOperatorId(user.getId());
            setLordId(msg.getData().getId());
            setLordType(CertLordTypeEnum.营业执照.getValue());
            setCertLevel(UserCertLevelEnum.企业认证.getValue());
            setCertStatus(msg.getData().getStatus());
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
