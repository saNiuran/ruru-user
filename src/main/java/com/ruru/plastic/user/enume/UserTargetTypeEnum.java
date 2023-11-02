package com.ruru.plastic.user.enume;

public enum UserTargetTypeEnum {
    微信公众号(0), 微信小程序(1), 头条(2);
    private final Integer value;

    UserTargetTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static UserTargetTypeEnum getEnum(int index) {
        UserTargetTypeEnum[] c = UserTargetTypeEnum.class.getEnumConstants();
        return c[index];
    }
}
