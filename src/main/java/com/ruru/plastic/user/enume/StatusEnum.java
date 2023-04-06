package com.ruru.plastic.user.enume;

public enum StatusEnum {
    不可用(0), 可用(1);
    private final Integer value;

    StatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static StatusEnum getEnum(int index) {
        StatusEnum[] c = StatusEnum.class.getEnumConstants();
        return c[index];
    }
}
