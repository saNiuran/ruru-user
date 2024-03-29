package com.ruru.plastic.user.redis;

import com.ruru.plastic.user.model.ThirdParty;

public interface RedisService {

    ////////////////////// user token
    String getUserInfo(Long userId, Integer userType, String paramKey);
    void setUserInfo(Long userId, Integer userType, String paramKey, String paramValue);
    void extendUserInfo(Long userId, Integer userType);
    void expireUserInfo(Long userId, Integer userType);
    void removeUserInfo(Long userId, Integer userType, String paramKey);

    String getSmsCode(String key);

    void setThirdPartyInfo(Long userId, ThirdParty thirdParty);

    ThirdParty getThirdPartyInfo(Long userId);

    void expireThirdPartyInfo(Long userId);
}
