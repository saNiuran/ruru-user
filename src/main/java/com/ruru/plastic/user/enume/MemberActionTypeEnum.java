package com.ruru.plastic.user.enume;

public enum MemberActionTypeEnum {
    创建(0), 展期(1), 过期(2), 个人认证赠送(3), 企业认证赠送(4);
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
