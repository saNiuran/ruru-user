package com.ruru.plastic.user.redis;

public interface RedisService {

    ////////////////////// user token
    String getUserInfo(Long userId, Integer userType, String paramKey);
    void setUserInfo(Long userId, Integer userType, String paramKey, String paramValue);
    void extendUserInfo(Long userId, Integer userType);
    void expireUserInfo(Long userId, Integer userType);
    void removeUserInfo(Long userId, Integer userType, String paramKey);

    ///////////////////////// sms code time
    String getSmsCodeTime(String mobile);
    void setSmsCodeTime(String mobile);

}
