package com.ruru.plastic.user.controller;

import com.ruru.plastic.user.config.CacheManagerConfig;
import com.ruru.plastic.user.enume.CertStatusEnum;
import com.ruru.plastic.user.enume.UserCertLevelEnum;
import com.ruru.plastic.user.enume.UserGenderEnum;
import com.ruru.plastic.user.enume.UserMemberLevelEnum;
import com.ruru.plastic.user.response.DataResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/basic/enum")
public class BasicDataController {

    @Cacheable(cacheNames = CacheManagerConfig.CacheNames.CACHE_60MINS, key = "'listUserCertLevelEnum'")
    @GetMapping("/user/cert/level")
    public DataResponse<List<Map<String, Object>>> listUserCertLevelEnum() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserCertLevelEnum e : UserCertLevelEnum.values()) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("value", e.getValue());
            map.put("label", e.name());
            list.add(map);
        }
        return DataResponse.success(list);
    }

    @Cacheable(cacheNames = CacheManagerConfig.CacheNames.CACHE_60MINS, key = "'listCertStatusEnum'")
    @GetMapping("/cert/status")
    public DataResponse<List<Map<String, Object>>> listCertStatusEnum() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CertStatusEnum e : CertStatusEnum.values()) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("value", e.getValue());
            map.put("label", e.name());
            list.add(map);
        }
        return DataResponse.success(list);
    }

    @Cacheable(cacheNames = CacheManagerConfig.CacheNames.CACHE_60MINS, key = "'listUserGenderEnum'")
    @GetMapping("/user/gender")
    public DataResponse<List<Map<String, Object>>> listUserGenderEnum() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserGenderEnum e : UserGenderEnum.values()) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("value", e.getValue());
            map.put("label", e.name());
            list.add(map);
        }
        return DataResponse.success(list);
    }

    @Cacheable(cacheNames = CacheManagerConfig.CacheNames.CACHE_60MINS, key = "'listUserMemberLevelEnum'")
    @GetMapping("/user/member/level")
    public DataResponse<List<Map<String, Object>>> listUserMemberLevelEnum() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserMemberLevelEnum e : UserMemberLevelEnum.values()) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("value", e.getValue());
            map.put("label", e.name());
            list.add(map);
        }
        return DataResponse.success(list);
    }

}
