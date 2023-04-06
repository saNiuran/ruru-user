package com.ruru.plastic.user.rocket;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RocketMQClient {
    /**
     * 生产者的组名
     */
    @Value("${plastic.user.producerGroup}")
    private String producerGroup;

    /**
     * NameServer 地址
     */
    @Value("${plastic.user.nameSrvAddr}")
    private String nameSrvAddr;

//    @PostConstruct
    @Bean
    public DefaultMQProducer defaultMQProducer() {
        //生产者的组名
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        //指定NameServer地址，多个地址以 ; 隔开
        producer.setNamesrvAddr(nameSrvAddr);
        producer.setVipChannelEnabled(false);
        try {
            producer.start();
            /**
             * Producer对象在使用之前必须要调用start初始化，初始化一次即可
             * 注意：切记不可以在每次发送消息时，都调用start方法
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return producer;
    }
}
