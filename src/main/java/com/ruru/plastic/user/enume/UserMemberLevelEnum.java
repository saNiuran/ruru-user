package com.ruru.plastic.user.enume;

public enum UserMemberLevelEnum {
    普通用户(0), 付费用户(1);
    private final Integer value;

    UserMemberLevelEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static UserMemberLevelEnum getEnum(int index) {
        UserMemberLevelEnum[] c = UserMemberLevelEnum.class.getEnumConstants();
        return c[index];
    }
}
