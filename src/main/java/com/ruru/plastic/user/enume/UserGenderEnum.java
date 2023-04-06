package com.ruru.plastic.user.enume;

public enum UserGenderEnum {
    女(0), 男(1),保密(2);
    private final Integer value;

    UserGenderEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static UserGenderEnum getEnum(int index) {
        UserGenderEnum[] c = UserGenderEnum.class.getEnumConstants();
        return c[index];
    }
}
