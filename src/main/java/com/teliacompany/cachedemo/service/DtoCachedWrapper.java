package com.teliacompany.cachedemo.service;

import com.teliacompany.cachedemo.domain.Dto;
import com.teliacompany.cachedemo.repository.DtoRepository;
import de.qaware.tools.collectioncacheableforspring.CollectionCacheable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.teliacompany.cachedemo.configuration.CacheConfiguration.CacheDtoByCode;
import static com.teliacompany.cachedemo.configuration.CacheConfiguration.CacheDtoById;

@Component
@RequiredArgsConstructor
public class DtoCachedWrapper {

    private final DtoRepository repository;
    private final DtoCacheSharing cacheSharing;

    @Nullable
    @Cacheable(cacheNames = CacheDtoById, unless = "#result == null")
    public Dto getById(@NonNull String id) {
        val dto = repository.fetchById(id);
        updateOtherCaches(CacheDtoById, dto);
        return dto;
    }

    @CollectionCacheable(CacheDtoById)
    public Map<String, Dto> getByIds(@NonNull Collection<String> ids) {
        val dtoList = repository.fetchByIds(new HashSet<>(ids));
        updateOtherCaches(CacheDtoById, dtoList);
        return dtoList.stream().collect(Collectors.toMap(Dto::getId, dto -> dto));
    }

    @Nullable
    @Cacheable(cacheNames = CacheDtoByCode, unless = "#result == null")
    public Dto getByCode(@NonNull String code) {
        val dto = repository.fetchByCode(code);
        updateOtherCaches(CacheDtoByCode, dto);
        return dto;
    }

    @CollectionCacheable(CacheDtoByCode)
    public Map<String, Dto> getByCodes(@NonNull Collection<String> codes) {
        val dtoList = repository.fetchByCodes(new HashSet<>(codes));
        updateOtherCaches(CacheDtoByCode, dtoList);
        return dtoList.stream().collect(Collectors.toMap(Dto::getCode, dto -> dto));
    }

    @CacheEvict(value = {CacheDtoById, CacheDtoByCode}, allEntries = true, beforeInvocation = true)
    public void resetCache() {
        repository.resetCounters();
    }


    private void updateOtherCaches(@NonNull String masterCache, @NonNull List<Dto> dtoList) {
        dtoList.forEach(dto -> updateOtherCaches(masterCache, dto));
    }

    private void updateOtherCaches(@NonNull String masterCache, @Nullable Dto dto) {
        if (dto == null)
            return;

        if (!masterCache.equals(CacheDtoById))
            cacheSharing.putIntoIdCache(dto);

        if (!masterCache.equals(CacheDtoByCode))
            cacheSharing.putIntoCodeCache(dto);
    }
}