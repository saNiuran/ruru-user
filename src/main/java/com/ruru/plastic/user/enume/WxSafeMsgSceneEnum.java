package com.ruru.plastic.user.enume;

public enum WxSafeMsgSceneEnum {
    资料(1), 评论(2),论坛(3),社交日志(4);
    private final Integer value;

    WxSafeMsgSceneEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public static WxSafeMsgSceneEnum getEnum(int index) {
        WxSafeMsgSceneEnum[] c = WxSafeMsgSceneEnum.class.getEnumConstants();
        return c[index-1];
    }
}
