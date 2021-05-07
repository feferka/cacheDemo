package com.teliacompany.cachedemo.service;

import com.teliacompany.cachedemo.configuration.CacheConfiguration;
import com.teliacompany.cachedemo.domain.Dto;
import lombok.NonNull;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

@SuppressWarnings("UnusedReturnValue")
@Component
public class DtoCacheSharing {

    @CachePut(cacheNames = CacheConfiguration.CacheDtoById, key = "#dto.id", condition = "#dto != null && #dto.id != null")
    public Dto putIntoIdCache(@NonNull Dto dto) {
        return dto;
    }

    @CachePut(cacheNames = CacheConfiguration.CacheDtoByCode, key = "#dto.code", condition = "#dto != null && #dto.code != null")
    public Dto putIntoCodeCache(@NonNull Dto dto) {
        return dto;
    }
}
