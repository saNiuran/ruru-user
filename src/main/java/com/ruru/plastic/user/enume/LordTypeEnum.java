package com.ruru.plastic.user.enume;

public enum LordTypeEnum {
    询价(0), 库存(1);
    private final Integer value;

    LordTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static LordTypeEnum getEnum(int index) {
        LordTypeEnum[] c = LordTypeEnum.class.getEnumConstants();
        return c[index];
    }
}
