package com.ruru.plastic.user.bean;


import java.util.regex.Pattern;

public class Constants {

    public static final String ERROR_PARAMETER = "参数错误！";
    public static final String ERROR_NO_INFO = "信息不存在！";
    public static final String ERROR_DUPLICATE_INFO = "信息重复！";

    public static final String REDIS_KEY_USER_TOKEN = "user:token:";
    public static final String REDIS_KEY_ADMIN_USER_TOKEN = "user:adminToken:";
    public static final String REDIS_KEY_USER_THIRD_PARTY_LOGON = "user:thirdParty:logon:";

    public final static String ACCESS_TOKEN = "token";
    public final static String ACCESS_TOKEN_ADMIN = "adminToken";

    public final static String SMS_CODE_LOGIN = "smsCodeLogin";
    public final static String SMS_CODE_THIRD_PARTY_BIND = "smsCodeBind";

    public final static String SMS_CODE_CHANGE_MOBILE = "smsCodeChangeMobile";
    public final static String SINGLE_PRODUCT_EXPERT = "SINGLE_PRODUCT_EXPERT";

    public static final Pattern CHINA_MOBILE_PATTERN = Pattern.compile("^((13[0-9])|(14[0-1,4-9])|(15[0-3,5-9])|(16[2,5-7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$");

}
