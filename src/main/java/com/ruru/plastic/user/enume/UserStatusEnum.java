package com.ruru.plastic.user.enume;

public enum UserStatusEnum {
    失效(0), 有效(1), 注销(2);
    private final Integer value;

    UserStatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static UserStatusEnum getEnum(int index) {
        UserStatusEnum[] c = UserStatusEnum.class.getEnumConstants();
        return c[index];
    }
}
