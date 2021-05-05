package com.teliacompany.cachedemo.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfiguration {

    public static final String
            CacheDtoById = "CacheDtoById",
            CacheDtoByCode = "CacheDtoByCode";

    @Bean
    public CacheManager dtoCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(CacheDtoById, CacheDtoByCode);
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterAccess(1000, TimeUnit.SECONDS)
                .ticker(Ticker.systemTicker())
                .recordStats());
        return cacheManager;
    }
}
