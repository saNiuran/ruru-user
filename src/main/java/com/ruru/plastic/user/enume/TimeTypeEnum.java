package com.ruru.plastic.user.enume;

public enum TimeTypeEnum {
    创建时间(0), 有效时间(1);
    private final Integer value;

    TimeTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static TimeTypeEnum getEnum(int index) {
        TimeTypeEnum[] c = TimeTypeEnum.class.getEnumConstants();
        return c[index];
    }
}
