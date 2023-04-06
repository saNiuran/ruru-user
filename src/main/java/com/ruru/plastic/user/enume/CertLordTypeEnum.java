package com.ruru.plastic.user.enume;

public enum CertLordTypeEnum {
    身份证(0), 营业执照(1), 刷脸(2);
    private final Integer value;

    CertLordTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static CertLordTypeEnum getEnum(int index) {
        CertLordTypeEnum[] c = CertLordTypeEnum.class.getEnumConstants();
        return c[index];
    }
}
