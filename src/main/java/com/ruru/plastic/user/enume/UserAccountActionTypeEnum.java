package com.ruru.plastic.user.enume;

public enum UserAccountActionTypeEnum {
    创建(0), 存入保证金(1), 提取保证金(2), 赎回保证金(3),注销(4);
    private final Integer value;

    UserAccountActionTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static UserAccountActionTypeEnum getEnum(int index) {
        UserAccountActionTypeEnum[] c = UserAccountActionTypeEnum.class.getEnumConstants();
        return c[index];
    }
}
