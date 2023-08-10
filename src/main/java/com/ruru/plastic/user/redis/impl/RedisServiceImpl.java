package com.ruru.plastic.user.redis.impl;

import com.alibaba.fastjson.JSON;
import com.ruru.plastic.user.enume.UserTypeEnum;
import com.ruru.plastic.user.model.ThirdParty;
import com.ruru.plastic.user.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.ruru.plastic.user.bean.Constants.*;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String getUserInfo(Long userId, Integer userType, String paramKey) {
        String key;
        if (userType.equals(UserTypeEnum.User.getValue())) {
            key = REDIS_KEY_USER_TOKEN + userId;
        } else {
            key = REDIS_KEY_ADMIN_USER_TOKEN + userId;
        }
        return (String) stringRedisTemplate.opsForHash().get(key, paramKey);
    }

    @Override
    public void setUserInfo(Long userId, Integer userType, String paramKey, String paramValue) {
        String key;
        if (userType.equals(UserTypeEnum.User.getValue())) {
            key = REDIS_KEY_USER_TOKEN + userId;
        } else {
            key = REDIS_KEY_ADMIN_USER_TOKEN + userId;
        }
        stringRedisTemplate.opsForHash().put(key, paramKey, paramValue);
    }

    @Override
    public void extendUserInfo(Long userId, Integer userType) {
        String key;
        if (userType.equals(UserTypeEnum.User.getValue())) {
            key = REDIS_KEY_USER_TOKEN + userId;
        } else {
            key = REDIS_KEY_ADMIN_USER_TOKEN + userId;
        }
        stringRedisTemplate.expire(key, 7, TimeUnit.DAYS);
    }

    @Override
    public void expireUserInfo(Long userId, Integer userType) {
        String key;
        if (userType.equals(UserTypeEnum.User.getValue())) {
            key = REDIS_KEY_USER_TOKEN + userId;
        } else {
            key = REDIS_KEY_ADMIN_USER_TOKEN + userId;
        }
        stringRedisTemplate.expire(key, 1, TimeUnit.MICROSECONDS);
    }

    @Override
    public void removeUserInfo(Long userId, Integer userType, String paramKey) {
        String key;
        if (userType.equals(UserTypeEnum.User.getValue())) {
            key = REDIS_KEY_USER_TOKEN + userId;
        } else {
            key = REDIS_KEY_ADMIN_USER_TOKEN + userId;
        }
        stringRedisTemplate.opsForHash().delete(key, paramKey);
    }


    @Override
    public String getSmsCode(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void setThirdPartyInfo(Long userId, ThirdParty thirdParty){
        String key = REDIS_KEY_USER_THIRD_PARTY_LOGON+ userId;
        stringRedisTemplate.opsForValue().set(key,JSON.toJSONString(thirdParty));
    }

    @Override
    public ThirdParty getThirdPartyInfo(Long userId){
        String key = REDIS_KEY_USER_THIRD_PARTY_LOGON+ userId;
        String s = stringRedisTemplate.opsForValue().get(key);
        if(StringUtils.isEmpty(s)){
            return null;
        }
        return JSON.parseObject(s, ThirdParty.class);
    }

    @Override
    public void expireThirdPartyInfo(Long userId){
        String key = REDIS_KEY_USER_THIRD_PARTY_LOGON+ userId;
        stringRedisTemplate.expire(key,1,TimeUnit.MICROSECONDS);
    }
}
