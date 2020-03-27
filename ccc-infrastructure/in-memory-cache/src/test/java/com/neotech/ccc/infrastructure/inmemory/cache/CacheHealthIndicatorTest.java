package com.neotech.ccc.infrastructure.inmemory.cache;

import com.neotech.ccc.domain.model.entities.CountryCallingCodesMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CacheHealthIndicatorTest {

    @Mock
    private CountryCallingCodesMap codesMap;

    @Mock
    private CachingCountryCallingCodesMapRepository repository;

    @InjectMocks
    private CacheHealthIndicator indicator;

    @Test
    void mustBeDownIfCacheIsNotLoaded() {
        given(repository.getCachedValue()).willReturn(null);
        assertEquals(Status.DOWN, indicator.health().getStatus());
    }

    @Test
    void mustBeUpIfCacheLoaded() {
        given(repository.getCachedValue()).willReturn(codesMap);
        assertEquals(Status.UP, indicator.health().getStatus());
    }
}