package com.ruru.plastic.user.enume;

public enum PushTypeEnum {
    Tags(0), Alias(1);
    private final Integer value;
    PushTypeEnum(Integer value) {
        this.value = value;
    }
    public Integer getValue(){
        return value;
    }

    public static PushTypeEnum getEnum(int index){
        PushTypeEnum[] c=PushTypeEnum.class.getEnumConstants();
        return c[index];
    }
}
