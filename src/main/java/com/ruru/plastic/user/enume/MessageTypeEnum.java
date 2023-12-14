package com.ruru.plastic.user.enume;

public enum MessageTypeEnum {
    询价(0),
    报价(1),
    询价审核(2),
    个人认证(3),
    企业认证(4),
    平台(5),
    库存(6),
    砍价(7),
    库存审核(8),
    蓝V认证(9),
    ;

    private final Integer value;
    MessageTypeEnum(Integer value) {
        this.value = value;
    }
    public Integer getValue(){
        return value;
    }
    public static MessageTypeEnum getEnum(int index){
        MessageTypeEnum[] c= MessageTypeEnum.class.getEnumConstants();
        return c[index];
    }
}
