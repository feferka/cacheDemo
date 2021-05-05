package com.teliacompany.cachedemo.repository;

import com.teliacompany.cachedemo.domain.Dto;
import lombok.NonNull;
import lombok.Setter;
import lombok.val;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class DtoRepository {

    private final Map<String, Integer> fetchByIdCounter = new HashMap<>();
    private final Map<String, Integer> fetchByCodeCounter = new HashMap<>();

    @Setter
    private List<Dto> repository = new ArrayList<>();

    @Nullable
    public Dto fetchById(@NonNull String id) {
        System.out.println("fetching by id: " + id);
        incrementIdCounter(id);
        return repository.stream()
                .filter(dto -> id.equals(dto.getId()))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public Dto fetchByCode(@NonNull String code) {
        System.out.println("fetching by code: " + code);
        incrementCodeCounter(code);
        return repository.stream()
                .filter(dto -> code.equals(dto.getCode()))
                .findFirst()
                .orElse(null);
    }

    public List<Dto> fetchByIds(@NonNull Set<String> ids) {
        System.out.println("fetching by ids: " + String.join(", ", ids));
        ids.forEach(this::incrementIdCounter);
        return ids.stream()
                .flatMap(id -> repository.stream()
                        .filter(dto -> id.equals(dto.getId())))
                .collect(Collectors.toList());
    }

    public List<Dto> fetchByCodes(@NonNull Set<String> codes) {
        System.out.println("fetching by codes: " + String.join(", ", codes));
        codes.forEach(this::incrementCodeCounter);
        return codes.stream()
                .flatMap(code -> repository.stream()
                        .filter(dto -> code.equals(dto.getCode())))
                .collect(Collectors.toList());
    }

    public void resetCounters() {
        fetchByIdCounter.clear();
        fetchByCodeCounter.clear();
    }

    public int getFetchCountsPerId(String id) {
        fetchByIdCounter.putIfAbsent(id, 0);
        return fetchByIdCounter.get(id);
    }

    public int getFetchCountsPerCode(String code) {
        fetchByCodeCounter.putIfAbsent(code, 0);
        return fetchByCodeCounter.get(code);
    }

    private void incrementIdCounter(String id) {
        fetchByIdCounter.putIfAbsent(id, 0);
        val count = fetchByIdCounter.get(id) + 1;
        fetchByIdCounter.put(id, count);
    }

    private void incrementCodeCounter(String code) {
        fetchByCodeCounter.putIfAbsent(code, 0);
        val count = fetchByCodeCounter.get(code) + 1;
        fetchByCodeCounter.put(code, count);
    }
}
