package com.ruru.plastic.user.enume;

public enum NotifyCodeEnum {
    异地登录("401","您的账号在其他设备登录！"),
    询价_待审核_管理("801","有新的询价待审核，请及时处理！"),
    询价_审核通过("802","您的询价被审核通过！"),
    询价_审核不通过("803","您的询价未能通过审核，请编辑后再提交！"),
    询价_审核展示中("804","您的询价已展示成功！"),
    询价_新询价("805","您有新的询价，请及时查看！"),
    报价_创建("901","您的询盘有新的报价，请及时查看"),
    报价_更新("902","您的询盘有报价更新，请及时查看"),
    报价_撤回("903","您的询盘有报价撤销，请及时查看"),
    报价_中标("904","您的报价已中标，请及时查看"),
    报价_未中标("905","您的报价未中标，请及时查看"),
    ;
    private final String code;
    private final String content;

    NotifyCodeEnum(String code, String content) {
        this.code = code;
        this.content = content;
    }
    public String getCode() {
        return code;
    }

    public String getContent() {
        return content;
    }
}
