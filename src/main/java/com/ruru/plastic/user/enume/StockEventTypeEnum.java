package com.ruru.plastic.user.enume;

public enum StockEventTypeEnum {
    查看(0), 打电话(1), 砍一刀(2), 发布(3),分享(4);
    private final Integer value;

    StockEventTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static StockEventTypeEnum getEnum(int index) {
        StockEventTypeEnum[] c = StockEventTypeEnum.class.getEnumConstants();
        return c[index];
    }
}
