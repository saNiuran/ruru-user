package com.ruru.plastic.user.task;

import com.alibaba.fastjson.JSON;
import com.ruru.plastic.user.model.User;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class PushTask {
    @Autowired
    private DefaultMQProducer producer;

    /**
     * 发送 mq消息
     * @param topic MQTopic
     * @param messageTag  消息标签
     */
    @Async("taskExecutor")
    public void sendMQ(String topic, String messageTag, byte[] body) {
        try {
            Message message = new Message(topic, messageTag, body);
            producer.send(message);
        } catch (MQClientException | InterruptedException | RemotingException | MQBrokerException e) {
            e.printStackTrace();
        }
    }

    //发送下架用户所有询价消息
    @Async("taskExecutor")
    public void sendUserLogoff(User user){
        try {
            sendMQ("RURU_BID", "Enquiry_Offline", JSON.toJSONString(user).getBytes(RemotingHelper.DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
