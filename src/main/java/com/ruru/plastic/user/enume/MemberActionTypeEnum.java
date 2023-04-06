package com.ruru.plastic.user.enume;

public enum MemberActionTypeEnum {
    创建(0), 展期(1), 过期(2);
    private final Integer value;

    MemberActionTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static MemberActionTypeEnum getEnum(int index) {
        MemberActionTypeEnum[] c = MemberActionTypeEnum.class.getEnumConstants();
        return c[index];
    }
}
