package com.ruru.plastic.user.response;


import com.ruru.plastic.user.enume.ResponseEnum;

import java.io.Serializable;

public class DataResponse<T> implements Serializable {
    private String message;
    private int retCode;
    private T data;

    public DataResponse() {
    }

    private DataResponse(T data) {
        this.retCode = 0;
        this.message = "成功";
        this.data = data;
    }

    private DataResponse(CodeResponse cm) {
        if (cm == null) {
            return;
        }
        this.retCode = cm.getRetCode();
        this.message = cm.getMessage();
    }

    /**
     * 成功时候的调用
     */
    public static <T> DataResponse<T> success(T data) {
        return new DataResponse<T>(data);
    }

    /**
     * 成功，不需要传入参数
     */
    @SuppressWarnings("unchecked")
    public static <T> DataResponse<T> success() {
        return (DataResponse<T>) success("");
    }

    /**
     * 失败时候的调用
     */
    public static <T> DataResponse<T> error(CodeResponse cm) {
        return new DataResponse<T>(cm);
    }

    public static <T> DataResponse<T> error(String str) {
        return new DataResponse<T>(new CodeResponse(500,str));
    }

    public static <T> DataResponse<T> failParas() {
        return new DataResponse<T>(new CodeResponse(500,"参数传递错误！"));
    }

    public static <T> DataResponse<T> error() {
        return new DataResponse<T>(new CodeResponse(500,"请求异常"));
    }
    /**
     * 失败时候的调用,扩展消息参数
     */
    public static <T> DataResponse<T> error(CodeResponse cm, String msg) {
        cm.setMessage(cm.getMessage() + "--" + msg);
        return new DataResponse<T>(cm);
    }

    public static <T> DataResponse<T> error(ResponseEnum responseEnum) {
        CodeResponse cm = new CodeResponse(responseEnum.getCode(),responseEnum.getMessage());
        return new DataResponse<T>(cm);
    }

    public static <T> DataResponse<T> error(ResponseEnum responseEnum, String msg) {
        CodeResponse cm = new CodeResponse(responseEnum.getCode(),responseEnum.getMessage());
        cm.setMessage(cm.getMessage() + "--" + msg);
        return new DataResponse<T>(cm);
    }


    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getRetCode() {
        return retCode;
    }
}

