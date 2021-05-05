package com.teliacompany.cachedemo.service;

import com.teliacompany.cachedemo.domain.Dto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@CacheConfig
public class DtoService {

    private final DtoCachedWrapper cachedWrapper;

    @Nullable
    public Dto getById(@NonNull String id) {
        return cachedWrapper.getById(id);
    }

    public List<Dto> getByIds(@NonNull Set<String> ids) {
        val map = cachedWrapper.getByIds(ids);
        return new ArrayList<>(map.values());
    }

    @Nullable
    public Dto getByCode(@NonNull String code) {
        return cachedWrapper.getByCode(code);
    }

    public List<Dto> getByCodes(@NonNull Set<String> codes) {
        val map = cachedWrapper.getByCodes(codes);
        return new ArrayList<>(map.values());
    }
}
