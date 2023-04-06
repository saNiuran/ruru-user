package com.ruru.plastic.user.enume;

public enum UserTypeEnum {
    User(0), Admin(1);
    private Integer value;

    UserTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static UserTypeEnum getEnum(int index) {
        UserTypeEnum[] c = UserTypeEnum.class.getEnumConstants();
        return c[index];
    }
}
