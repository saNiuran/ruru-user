package com.ruru.plastic.user.enume;

public enum AppTypeEnum {
    安卓(0), 苹果(1);
    private final Integer value;

    AppTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static AppTypeEnum getEnum(int index) {
        AppTypeEnum[] c = AppTypeEnum.class.getEnumConstants();
        return c[index];
    }
}
