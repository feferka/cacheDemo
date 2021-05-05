package com.teliacompany.cachedemo.service;

import com.teliacompany.cachedemo.domain.Dto;
import com.teliacompany.cachedemo.repository.DtoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

@SpringBootTest
public class DtoServiceTest {

    private final String id_1 = "1";
    private final String id_2 = "2";

    private final String code_a = "a";
    private final String code_b = "b";

    private final Set<String> ids_1 = Set.of(id_1);
    private final Set<String> ids_12 = Set.of(id_1, id_2);
    private final Set<String> ids_21 = Set.of(id_2, id_1);

    private final Set<String> codes_a = Set.of(code_a);
    private final Set<String> codes_ab = Set.of(code_a, code_b);
    private final Set<String> codes_ba = Set.of(code_b, code_a);

    private final Dto expectedDto1 = Dto.of(id_1, code_a, "A");
    private final Dto expectedDto2 = Dto.of(id_2, code_b, "B");

    private final List<Dto> expectedDtoList1 = List.of(expectedDto1);
    private final List<Dto> expectedDtoList12 = List.of(expectedDto1, expectedDto2);

    @Autowired
    private DtoService service;

    @Autowired
    private DtoCachedWrapper cachedWrapper;

    @Autowired
    private DtoRepository repository;

    @BeforeEach
    private void setup() {
        repository.setRepository(List.of(expectedDto1, expectedDto2));
        cachedWrapper.resetCache();
    }

    private void assertIdFetchCount(String id, int count) {
        Assertions.assertEquals(count, repository.getFetchCountsPerId(id), "Count of fetches by ID does not match.");
    }

    private void assertCodeFetchCount(String code, int count) {
        Assertions.assertEquals(count, repository.getFetchCountsPerCode(code), "Count of fetches by CODE does not match.");
    }

    private void assertDto(Dto expected, Dto actual) {
        Assertions.assertNotNull(actual, "DTO provided by the service should not be null.");
        Assertions.assertEquals(expected, actual, "Unexpected DTO provided by the service.");
    }

    private void assertDtoList(List<Dto> expected, List<Dto> actual) {
        Assertions.assertNotNull(actual, "DTO list provided by the service should not be null.");
        Assertions.assertEquals(expected.size(), actual.size(), "Unexpected size of DTO list provided by the service.");
        expected.forEach(expectedDto -> Assertions.assertTrue(actual.contains(expectedDto), "Unexpected DTO found in DTO list provided by the service."));
    }

    @Test
    public void fetchById_getCachedById() {
        // before
        assertIdFetchCount(id_1, 0);

        // fetch
        assertDto(expectedDto1, service.getById(id_1));
        assertIdFetchCount(id_1, 1);

        // get cached
        assertDto(expectedDto1, service.getById(id_1));
        assertIdFetchCount(id_1, 1);
    }

    @Test
    public void fetchById_getCachedByIds() {
        // before
        assertIdFetchCount(id_1, 0);
        assertIdFetchCount(id_2, 0);

        // fetch
        assertDto(expectedDto1, service.getById(id_1));
        assertDto(expectedDto2, service.getById(id_2));
        assertIdFetchCount(id_1, 1);
        assertIdFetchCount(id_2, 1);

        // get cached
        assertDtoList(expectedDtoList12, service.getByIds(ids_12));
        assertDtoList(expectedDtoList12, service.getByIds(ids_21));
        assertIdFetchCount(id_1, 1);
        assertIdFetchCount(id_2, 1);
    }

    @Test
    public void fetchById_getByCode() {
        // before
        assertIdFetchCount(id_1, 0);
        assertIdFetchCount(id_2, 0);
        assertCodeFetchCount(code_a, 0);
        assertCodeFetchCount(code_b, 0);

        // fetch
        assertDto(expectedDto1, service.getById(id_1));
        assertDto(expectedDto2, service.getById(id_2));
        assertIdFetchCount(id_1, 1);
        assertIdFetchCount(id_2, 1);

        // get cached
        assertDto(expectedDto1, service.getByCode(code_a));
        assertDto(expectedDto2, service.getByCode(code_b));
        assertCodeFetchCount(code_a, 0);
        assertCodeFetchCount(code_b, 0);
    }

    @Test
    public void fetchById_getByCodes() {
        // before
        assertIdFetchCount(id_1, 0);
        assertIdFetchCount(id_2, 0);
        assertCodeFetchCount(code_a, 0);
        assertCodeFetchCount(code_b, 0);

        // fetch
        assertDto(expectedDto1, service.getById(id_1));
        assertDto(expectedDto2, service.getById(id_2));
        assertIdFetchCount(id_1, 1);
        assertIdFetchCount(id_2, 1);

        // get cached
        assertDtoList(expectedDtoList12, service.getByCodes(codes_ab));
        assertDtoList(expectedDtoList12, service.getByCodes(codes_ba));
        assertCodeFetchCount(code_a, 0);
        assertCodeFetchCount(code_b, 0);
    }

    @Test
    public void fetchByIds_getCachedById() {
        // before
        assertIdFetchCount(id_1, 0);

        // fetch
        assertDtoList(expectedDtoList1, service.getByIds(ids_1));
        assertIdFetchCount(id_1, 1);

        // get cached
        assertDto(expectedDto1, service.getById(id_1));
        assertIdFetchCount(id_1, 1);
    }

    @Test
    public void fetchByIds_getCachedByIds() {
        // before
        assertIdFetchCount(id_1, 0);
        assertIdFetchCount(id_2, 0);

        // fetch
        assertDtoList(expectedDtoList12, service.getByIds(ids_12));
        assertIdFetchCount(id_1, 1);
        assertIdFetchCount(id_2, 1);

        // get cached
        assertDtoList(expectedDtoList12, service.getByIds(ids_12));
        assertDtoList(expectedDtoList12, service.getByIds(ids_21));
        assertIdFetchCount(id_1, 1);
        assertIdFetchCount(id_2, 1);
    }

    @Test
    public void fetchByIds_getByCode() {
        // before
        assertIdFetchCount(id_1, 0);
        assertIdFetchCount(id_2, 0);
        assertCodeFetchCount(code_a, 0);
        assertCodeFetchCount(code_b, 0);

        // fetch
        assertDtoList(expectedDtoList12, service.getByIds(ids_12));
        assertIdFetchCount(id_1, 1);
        assertIdFetchCount(id_2, 1);

        // get cached
        assertDto(expectedDto1, service.getByCode(code_a));
        assertDto(expectedDto2, service.getByCode(code_b));
        assertCodeFetchCount(code_a, 0);
        assertCodeFetchCount(code_b, 0);
    }

    @Test
    public void fetchByIds_getByCodes() {
        // before
        assertIdFetchCount(id_1, 0);
        assertIdFetchCount(id_2, 0);
        assertCodeFetchCount(code_a, 0);
        assertCodeFetchCount(code_b, 0);

        // fetch
        assertDtoList(expectedDtoList12, service.getByIds(ids_12));
        assertIdFetchCount(id_1, 1);
        assertIdFetchCount(id_2, 1);

        // get cached
        assertDtoList(expectedDtoList12, service.getByCodes(codes_ab));
        assertDtoList(expectedDtoList12, service.getByCodes(codes_ba));
        assertCodeFetchCount(code_a, 0);
        assertCodeFetchCount(code_b, 0);
    }

    @Test
    public void fetchByCode_getCachedByCode() {
        // before
        assertCodeFetchCount(code_a, 0);

        // fetch
        assertDto(expectedDto1, service.getByCode(code_a));
        assertCodeFetchCount(code_a, 1);

        // get cached
        assertDto(expectedDto1, service.getByCode(code_a));
        assertCodeFetchCount(code_a, 1);
    }

    @Test
    public void fetchByCode_getCachedByCodes() {
        // before
        assertCodeFetchCount(code_a, 0);
        assertCodeFetchCount(code_b, 0);

        // fetch
        assertDto(expectedDto1, service.getByCode(code_a));
        assertDto(expectedDto2, service.getByCode(code_b));
        assertCodeFetchCount(code_a, 1);
        assertCodeFetchCount(code_b, 1);

        // get cached
        assertDtoList(expectedDtoList12, service.getByCodes(Set.of(code_a, code_b)));
        assertDtoList(expectedDtoList12, service.getByCodes(Set.of(code_b, code_a)));
        assertCodeFetchCount(code_a, 1);
        assertCodeFetchCount(code_b, 1);
    }

    @Test
    public void fetchByCode_getById() {
        // before
        assertIdFetchCount(id_1, 0);
        assertIdFetchCount(id_2, 0);
        assertCodeFetchCount(code_a, 0);
        assertCodeFetchCount(code_b, 0);

        // fetch
        assertDto(expectedDto1, service.getByCode(code_a));
        assertDto(expectedDto2, service.getByCode(code_b));
        assertCodeFetchCount(code_a, 1);
        assertCodeFetchCount(code_b, 1);

        // get cached
        assertDto(expectedDto1, service.getById(id_1));
        assertDto(expectedDto2, service.getById(id_2));
        assertIdFetchCount(id_1, 0);
        assertIdFetchCount(id_2, 0);
    }

    @Test
    public void fetchByCode_getByIds() {
        // before
        assertIdFetchCount(id_1, 0);
        assertIdFetchCount(id_2, 0);
        assertCodeFetchCount(code_a, 0);
        assertCodeFetchCount(code_b, 0);

        // fetch
        assertDto(expectedDto1, service.getByCode(code_a));
        assertDto(expectedDto2, service.getByCode(code_b));
        assertCodeFetchCount(code_a, 1);
        assertCodeFetchCount(code_b, 1);

        // get cached
        assertDtoList(expectedDtoList12, service.getByIds(ids_12));
        assertDtoList(expectedDtoList12, service.getByIds(ids_21));
        assertIdFetchCount(id_1, 0);
        assertIdFetchCount(id_2, 0);
    }

    @Test
    public void fetchByCodes_getCachedByCode() {
        // before
        assertCodeFetchCount(code_a, 0);

        // fetch
        assertDtoList(expectedDtoList1, service.getByCodes(codes_a));
        assertCodeFetchCount(code_a, 1);

        // get cached
        assertDto(this.expectedDto1, service.getByCode(code_a));
        assertCodeFetchCount(code_a, 1);
    }

    @Test
    public void fetchByCodes_getCachedByCodes() {
        // before
        assertCodeFetchCount(code_a, 0);
        assertCodeFetchCount(code_b, 0);

        // fetch
        assertDtoList(expectedDtoList12, service.getByCodes(codes_ab));
        assertCodeFetchCount(code_a, 1);
        assertCodeFetchCount(code_b, 1);

        // get cached
        assertDtoList(expectedDtoList12, service.getByCodes(codes_ab));
        assertDtoList(expectedDtoList12, service.getByCodes(codes_ba));
        assertCodeFetchCount(code_a, 1);
        assertCodeFetchCount(code_b, 1);
    }

    @Test
    public void fetchByCodes_getById() {
        // before
        assertIdFetchCount(id_1, 0);
        assertIdFetchCount(id_2, 0);
        assertCodeFetchCount(code_a, 0);
        assertCodeFetchCount(code_b, 0);

        // fetch
        assertDtoList(expectedDtoList12, service.getByCodes(codes_ab));
        assertCodeFetchCount(code_a, 1);
        assertCodeFetchCount(code_b, 1);

        // get cached
        assertDto(expectedDto1, service.getById(id_1));
        assertDto(expectedDto2, service.getById(id_2));
        assertIdFetchCount(id_1, 0);
        assertIdFetchCount(id_2, 0);
    }

    @Test
    public void fetchByCodes_getByIds() {
        // before
        assertIdFetchCount(id_1, 0);
        assertIdFetchCount(id_2, 0);
        assertCodeFetchCount(code_a, 0);
        assertCodeFetchCount(code_b, 0);

        // fetch
        assertDtoList(expectedDtoList12, service.getByCodes(codes_ab));
        assertCodeFetchCount(code_a, 1);
        assertCodeFetchCount(code_b, 1);

        // get cached
        assertDtoList(expectedDtoList12, service.getByIds(ids_12));
        assertDtoList(expectedDtoList12, service.getByIds(ids_21));
        assertIdFetchCount(id_1, 0);
        assertIdFetchCount(id_2, 0);
    }
}