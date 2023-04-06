package com.ruru.plastic.user.enume;

public enum RoleLevelEnum {
    员工(0), 经理(1);
    private Integer value;

    RoleLevelEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static RoleLevelEnum getEnum(int index) {
        RoleLevelEnum[] c = RoleLevelEnum.class.getEnumConstants();
        return c[index];
    }
}
