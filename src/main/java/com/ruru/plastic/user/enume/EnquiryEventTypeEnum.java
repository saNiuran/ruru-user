package com.ruru.plastic.user.enume;

public enum EnquiryEventTypeEnum {
    查看(0), 打电话(1), 报价(2), 发布(3);
    private final Integer value;

    EnquiryEventTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static EnquiryEventTypeEnum getEnum(int index) {
        EnquiryEventTypeEnum[] c = EnquiryEventTypeEnum.class.getEnumConstants();
        return c[index];
    }
}
