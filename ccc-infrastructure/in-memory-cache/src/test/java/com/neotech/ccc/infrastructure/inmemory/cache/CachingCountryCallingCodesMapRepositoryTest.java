package com.neotech.ccc.infrastructure.inmemory.cache;

import com.neotech.ccc.domain.model.entities.CountryCallingCodesMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CachingCountryCallingCodesMapRepositoryTest {

    @Mock
    private CountryCallingCodesMap codesMapOld;

    @Mock
    private CountryCallingCodesMap codesMapNew;

    @Mock
    private CountryCallingCodesMapProvider codesMapProvider;

    @Test
    void shouldReturnEmptyMapWhenNotLoaded() {
        given(codesMapProvider.load()).willReturn(Mono.empty());
        var repository = new CachingCountryCallingCodesMapRepository(codesMapProvider);
        StepVerifier.create(repository.find())
                    .assertNext(map -> assertEquals(0, map.size()))
                    .verifyComplete();
    }

    @Test
    void shouldUpdateStateWithNewMap() {
        given(codesMapProvider.load()).willReturn(Mono.just(codesMapOld))
                                      .willReturn(Mono.just(codesMapNew));
        var repository = new CachingCountryCallingCodesMapRepository(codesMapProvider);
        StepVerifier.create(repository.find())
                    .expectNext(codesMapOld)
                    .verifyComplete();
        repository.reloadCache();
        StepVerifier.create(repository.find())
                    .expectNext(codesMapNew)
                    .verifyComplete();
    }
}