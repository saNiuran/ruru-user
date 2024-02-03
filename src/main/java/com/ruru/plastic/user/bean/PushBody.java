package com.ruru.plastic.user.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@ToString
public class PushBody implements Serializable {
    private List<Long> userIds;
    private Integer pushType;   //别名 or 标签
    private String notifyTitle;
    private String msgTitle;
    private String msgContent;
    private String notifyCode; //推送消息编码
    private Map<String, String> extras;
}

