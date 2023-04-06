package com.ruru.plastic.user.bean;

import java.io.Serializable;

public class Msg<T> implements Serializable {
    private T data;
    private String errorMsg;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Msg() {
    }

    public Msg(T data) {
        this.errorMsg = "";
        this.data = data;
    }

    public static <T> Msg<T> success(T data) {
        return new Msg<T>(data);
    }

    public static <T> Msg<T> error(String message) {
        Msg<T> msg = new Msg<>();
        msg.setErrorMsg(message);
        return msg;
    }
}
