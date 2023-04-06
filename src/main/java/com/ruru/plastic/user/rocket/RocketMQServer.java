package com.ruru.plastic.user.rocket;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RocketMQServer {
    /**
     * 消费者的组名
     */
    @Value("${plastic.user.consumer}")
    private String consumerGroup;

    /**
     * NameServer 地址
     */
    @Value("${plastic.user.nameSrvAddr}")
    private String nameSrvAddr;

    @PostConstruct
    public void defaultMQOrderConsumer() {
        //消费者的组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        //指定NameServer地址，多个地址以 ; 隔开
        consumer.setNamesrvAddr(nameSrvAddr);
        consumer.setVipChannelEnabled(false);
        try {
            //订阅topic tag
            consumer.subscribe("RURU_USER",
                    "userCityParse" +  //用户地址解析
                            "||userAccount" + //动账操作
                            "||saveUserWx"  //保存获取的用户微信信息
            );
            //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
            //如果非第一次启动，那么按照上次消费的位置继续消费
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.registerMessageListener((MessageListenerConcurrently) (list, context) -> {
                try {
                    for (MessageExt messageExt : list) {
                        if("saveUserWx".equals(messageExt.getTags())){
                            String messageBody = new String(messageExt.getBody(), RemotingHelper.DEFAULT_CHARSET);
//                            WxUser wxUser = JSON.parseObject(messageBody, WxUser.class);
//                            wxUserService.createWxUser(wxUser);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //稍后再试
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                //消费成功
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
