package com.ruru.plastic.user.task;

import com.ruru.plastic.user.bean.Message;
import com.ruru.plastic.user.bean.PushBody;
import com.ruru.plastic.user.enume.MessageTypeEnum;
import com.ruru.plastic.user.enume.OperatorTypeEnum;
import com.ruru.plastic.user.feign.SmsFeignService;
import com.ruru.plastic.user.model.AdminUser;
import com.ruru.plastic.user.model.PersonalCert;
import com.ruru.plastic.user.enume.NotifyCodeEnum;
import com.ruru.plastic.user.model.UserCorporateCertMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CertTask {

    @Autowired
    private SmsFeignService smsFeignService;
    @Async("taskExecutor")
    public void createCertMessage(UserCorporateCertMatch match, NotifyCodeEnum notifyCodeEnum, AdminUser adminUser){
        Message message = new Message();
        if(notifyCodeEnum.equals(NotifyCodeEnum.企业认证_待审核_管理)) {
            message.setToId(0L);
            message.setTitle("管理消息");
        }else{
            message.setToId(match.getUserId());
            message.setTitle("系统消息");
        }
        if(adminUser==null) {
            message.setOperatorType(OperatorTypeEnum.系统.getValue());
            message.setOperatorId(0L);
        }else{
            message.setOperatorType(OperatorTypeEnum.工作人员.getValue());
            message.setOperatorId(adminUser.getId());
        }
        message.setType(MessageTypeEnum.企业认证.getValue());
        message.setContent(notifyCodeEnum.getContent());
        message.setRelationId(match.getId());
        smsFeignService.createMessage(message);
    }

    @Async("taskExecutor")
    public void createCertMessage(PersonalCert personalCert, NotifyCodeEnum notifyCodeEnum, AdminUser adminUser){
        Message message = new Message();
        if(notifyCodeEnum.equals(NotifyCodeEnum.个人认证_待审核_管理)) {
            message.setToId(0L);
            message.setTitle("管理消息");
        }else{
            message.setToId(personalCert.getUserId());
            message.setTitle("系统消息");
        }
        if(adminUser==null) {
            message.setOperatorType(OperatorTypeEnum.系统.getValue());
            message.setOperatorId(0L);
        }else{
            message.setOperatorType(OperatorTypeEnum.工作人员.getValue());
            message.setOperatorId(adminUser.getId());
        }
        message.setType(MessageTypeEnum.个人认证.getValue());
        message.setContent(notifyCodeEnum.getContent());
        message.setRelationId(personalCert.getId());
        smsFeignService.createMessage(message);
    }

    @Async("taskExecutor")
    public void createPush(PushBody pushBody) {
        smsFeignService.createPush(pushBody);
    }
}
