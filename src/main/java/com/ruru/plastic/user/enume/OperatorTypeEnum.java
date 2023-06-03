package com.ruru.plastic.user.enume;

public enum OperatorTypeEnum {
    用户(0), 工作人员(1), 系统(2);
    private final Integer value;

    OperatorTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static OperatorTypeEnum getEnum(int index) {
        OperatorTypeEnum[] c = OperatorTypeEnum.class.getEnumConstants();
        return c[index];
    }
}
