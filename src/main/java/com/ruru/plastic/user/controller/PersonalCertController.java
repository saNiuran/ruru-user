package com.ruru.plastic.user.controller;

import com.github.pagehelper.PageInfo;
import com.ruru.plastic.user.bean.Constants;
import com.ruru.plastic.user.bean.Msg;
import com.ruru.plastic.user.bean.PushBody;
import com.ruru.plastic.user.enume.*;
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
import com.ruru.plastic.user.service.UserService;
import com.ruru.plastic.user.task.CertTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/personal/cert")
public class PersonalCertController {

    @Autowired
    private PersonalCertService personalCertService;
    @Autowired
    private CertificateLogService certificateLogService;
    @Autowired
    private CertTask certTask;
    @Autowired
    private UserService userService;

    @LoginRequired
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

    @LoginRequired
    @PostMapping("/info/my")
    public DataResponse<PersonalCert> getMyPersonalCert(@CurrentUser User user) {
        PersonalCert personalCertByUserId = personalCertService.getPersonalCertByUserId(user.getId());
        if (personalCertByUserId == null) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }
        return DataResponse.success(personalCertByUserId);
    }

    @LoginRequired
    @PostMapping("/new")
    public DataResponse<PersonalCert> createPersonalCert(@RequestBody PersonalCert personalCert, @CurrentUser User user) {
        personalCert.setUserId(user.getId());
        Msg<PersonalCert> msg = personalCertService.createPersonalCert(personalCert);
        if (StringUtils.isNotEmpty(msg.getErrorMsg())) {
            return DataResponse.error(msg.getErrorMsg());
        }

        certificateLogService.createCertificateLog(new CertificateLog() {{
            setOperatorType(OperatorTypeEnum.用户.getValue());
            setOperatorId(user.getId());
            setLordId(msg.getData().getId());
            setLordType(CertLordTypeEnum.身份证.getValue());
            setCertLevel(UserCertLevelEnum.个人认证.getValue());
            setCertStatus(msg.getData().getStatus());
        }});

        //创建管理员通知消息
        certTask.createCertMessage(msg.getData(), NotifyCodeEnum.个人认证_待审核_管理, null);

        certTask.createPush(new PushBody() {{
            setNotifyCode(NotifyCodeEnum.个人认证_待审核_管理.getCode());
            setUserIds(Collections.singletonList(0L));

            Map<String, String> extras = new HashMap<>();
            extras.put("id", msg.getData().getId().toString());
            extras.put("notifyCode", NotifyCodeEnum.个人认证_待审核_管理.getCode());
            setExtras(extras);
        }});

        return DataResponse.success(msg.getData());
    }

    @LoginRequired
    @PostMapping("/update")
    public DataResponse<PersonalCert> updatePersonalCert(@RequestBody PersonalCert personalCert, @CurrentUser User user) {
        PersonalCert personalCertByUserId = personalCertService.getPersonalCertByUserId(user.getId());
        if (personalCertByUserId == null) {
            return DataResponse.error(Constants.ERROR_NO_INFO);
        }

        if (personalCertByUserId.getStatus().equals(CertStatusEnum.审核通过.getValue())
                || personalCertByUserId.getStatus().equals(CertStatusEnum.失效.getValue())) {
            return DataResponse.error("当前状态为" + CertStatusEnum.getEnum(personalCertByUserId.getStatus()) + "，不能修改！");
        }

        personalCert.setId(personalCertByUserId.getId());
        personalCert.setStatus(CertStatusEnum.待审核.getValue());
        Msg<PersonalCert> msg = personalCertService.updatePersonalCert(personalCert);
        if (StringUtils.isNotEmpty(msg.getErrorMsg())) {
            return DataResponse.error(msg.getErrorMsg());
        }

        certificateLogService.createCertificateLog(new CertificateLog() {{
            setOperatorType(OperatorTypeEnum.用户.getValue());
            setOperatorId(user.getId());
            setLordId(msg.getData().getId());
            setLordType(CertLordTypeEnum.身份证.getValue());
            setCertLevel(UserCertLevelEnum.个人认证.getValue());
            setCertStatus(msg.getData().getStatus());
        }});

        //创建管理员通知消息
        certTask.createCertMessage(msg.getData(), NotifyCodeEnum.个人认证_待审核_管理, null);

        certTask.createPush(new PushBody() {{
            setNotifyCode(NotifyCodeEnum.个人认证_待审核_管理.getCode());
            setUserIds(Collections.singletonList(0L));

            Map<String, String> extras = new HashMap<>();
            extras.put("id", msg.getData().getId().toString());
            extras.put("notifyCode", NotifyCodeEnum.个人认证_待审核_管理.getCode());
            setExtras(extras);
        }});

        return DataResponse.success(msg.getData());
    }

    @LoginRequired
    @PostMapping("/delete")
    public DataResponse<Integer> deletePersonalCert(@RequestBody PersonalCert personalCert) {
        Msg<Integer> msg = personalCertService.deletePersonalCert(personalCert);
        if (StringUtils.isNotEmpty(msg.getErrorMsg())) {
            return DataResponse.error(msg.getErrorMsg());
        }
        return DataResponse.success(msg.getData());
    }

    @LoginRequired
    @PostMapping("/filter")
    public DataResponse<PageInfo<PersonalCertResponse>> filterPersonalCertResponse(@RequestBody PersonalCertRequest request) {
        PageInfo<PersonalCert> pageInfo = personalCertService.filterPersonalCert(request);
        PageInfo<PersonalCertResponse> responsePageInfo = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, responsePageInfo);

        List<PersonalCertResponse> responseList = new ArrayList<>();

        for (PersonalCert personalCert : pageInfo.getList()) {
            responseList.add(getPersonalCertResponseById(personalCert.getId()));
        }
        responsePageInfo.setList(responseList);

        return DataResponse.success(responsePageInfo);
    }

    private PersonalCertResponse getPersonalCertResponseById(Long id) {
        PersonalCert personalCertByUserId = personalCertService.getPersonalCertById(id);
        if (personalCertByUserId == null) {
            return null;
        }
        PersonalCertResponse response = new PersonalCertResponse();
        BeanUtils.copyProperties(personalCertByUserId, response);
        response.setUser(userService.getUserById(personalCertByUserId.getUserId()));
        return response;
    }

}
