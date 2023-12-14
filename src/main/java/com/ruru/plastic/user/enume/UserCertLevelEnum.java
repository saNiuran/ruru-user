package com.ruru.plastic.user.enume;

public enum UserCertLevelEnum {
    未认证(0), 个人认证(1), 企业认证(2), 蓝V认证(3);
    private final Integer value;

    UserCertLevelEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static UserCertLevelEnum getEnum(int index) {
        UserCertLevelEnum[] c = UserCertLevelEnum.class.getEnumConstants();
        return c[index];
    }
}
