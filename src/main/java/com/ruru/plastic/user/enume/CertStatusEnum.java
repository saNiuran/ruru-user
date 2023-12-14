package com.ruru.plastic.user.enume;

public enum CertStatusEnum {
    待审核(0), 审核通过(1), 审核失败(2), 失效(3), 待付款(4);
    private final Integer value;

    CertStatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static CertStatusEnum getEnum(int index) {
        CertStatusEnum[] c = CertStatusEnum.class.getEnumConstants();
        return c[index];
    }
}
