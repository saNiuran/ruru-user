package com.ruru.plastic.user.task;

import com.ruru.plastic.user.bean.Message;
import com.ruru.plastic.user.enume.MessageTypeEnum;
import com.ruru.plastic.user.enume.OperatorTypeEnum;
import com.ruru.plastic.user.feign.SmsFeignService;
import com.ruru.plastic.user.model.UserCorporateCertMatch;
import com.ruru.plastic.user.enume.NotifyCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CertTask {

    @Autowired
    private SmsFeignService smsFeignService;
    @Async("taskExecutor")
    public void createCertMessage(UserCorporateCertMatch match, NotifyCodeEnum notifyCodeEnum){
        Message message = new Message();
        if(notifyCodeEnum.equals(NotifyCodeEnum.企业认证_待审核_管理)) {
            message.setToId(0L);
            message.setTitle("管理消息");
        }else{
            message.setToId(match.getUserId());
            message.setTitle("系统消息");
        }
        message.setOperatorType(OperatorTypeEnum.系统.getValue());
        message.setOperatorId(0L);
        message.setType(MessageTypeEnum.询价.getValue());
        message.setContent(notifyCodeEnum.getContent());
        message.setRelationId(match.getId());
        smsFeignService.createMessage(message);
    }
}
