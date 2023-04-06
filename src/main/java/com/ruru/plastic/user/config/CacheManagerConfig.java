package com.ruru.plastic.user.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@EnableCaching
@Configuration
public class CacheManagerConfig {
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
//        log("生成RedisCacheManager缓存管理器...");
        RedisCacheConfiguration config10s = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(10))
                .disableCachingNullValues();

        RedisCacheConfiguration config60s = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(60))
                .disableCachingNullValues();

        RedisCacheConfiguration config60m = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues();

        RedisCacheConfiguration config6h = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(6))
                .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> configurationMap = new LinkedHashMap<>(3);
        configurationMap.put(CacheNames.CACHE_10SECS,config10s);
        configurationMap.put(CacheNames.CACHE_60SECS,config60s);
        configurationMap.put(CacheNames.CACHE_60MINS,config60m);
        configurationMap.put(CacheNames.CACHE_6HOURS,config6h);


        return RedisCacheManager.builder(factory)
                .withInitialCacheConfigurations(configurationMap)
                .cacheDefaults(config6h)
                .build();

    }

    public interface CacheNames {
        /** 10秒缓存组 */
        String CACHE_10SECS = "user:dbCache:config10s";
        /** 1分钟缓存组 */
        String CACHE_60SECS = "user:dbCache:config60s";
        /** 60分钟缓存组 */
        String CACHE_60MINS = "user:dbCache:config60m";
        /** 6小时缓存组 */
        String CACHE_6HOURS = "user:dbCache:config6h";
    }
}
