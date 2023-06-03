package com.ruru.plastic.user.enume;

public enum  ResponseEnum implements CommonEnum{
    SUCCESS(200,"请求成功","网络请求成功"),
    ERROR_USER_NOT_EXIST(40100, "用户不存在","当前用户不存在"),
    ERROR_TOKEN_TIMEOUT(40101, "token失效","登录信息过期，请重新登录"),
    ERROR_TOKEN(40102, "token错误","登录信息错误，请重新登录"),
    EMPTY_TOKEN(40103, "token不存在","登录信息错误，请重新登录"),
    ERROR_USER_FORBIDDEN(40104, "用户被禁用","当前用户已被禁用"),
    METHOD_NOT_ALLOW(40500,"请求失败","请求方法不支持"),
    ERROR(500,"请求失败","服务器异常"),
    ERROR_CODE_NOT_EXIST(500, "验证码无效","验证码无效，请重新获取"),
    ERROR_USER_NO_REPUTATION(500, "用户信誉度过低","当前用户信誉度小于等于0"),
    ERROR_USER_ACCOUNT_NOT_EXIST(500, "账户不存在","当前用户账户信息不存在"),
    ERROR_USER_ACCOUNT_FORBIDDEN(500, "账户被禁用","当前用户账户被禁用"),
    ERROR_USER_NO_COM_AUTH(500, "用户未进行企业认证","当前用户未进行企业认证"),
    ERROR_USER_NO_PER_AUTH(500, "用户未进行个人","当前用户未进行个人认证"),
    ERROR_STATUS_NOT_EXIST(40600, "状态值不存在","当前状态值不存在"),
    ERROR_USER_STATUS_CLOSE(40700, "用户已被禁用","当前用户已被禁用"),
    ;

    private int code; //状态
    private String message; // 信息
    private String desc;    //详细描述

    ResponseEnum(int code, String message, String desc) {
        this.code = code;
        this.message = message;
        this.desc = desc;
    }

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
