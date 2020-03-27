package com.neotech.ccc.infrastructure.inmemory.cache;

import com.neotech.ccc.domain.model.entities.Country;
import com.neotech.ccc.domain.model.entities.CountryCallingCode;
import com.neotech.ccc.domain.model.repositories.CountryCallingCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CountryCallingCodesMapProviderTest {

    @Mock
    private CountryCallingCodeRepository repository;
    private Duration retryDelay = Duration.ofSeconds(1);
    private long retryMaxAttempts = 2;

    private CountryCallingCode code = new CountryCallingCode(371L, List.of(new Country("Latvia", "LV")));
    private CountryCallingCodesMapProvider service;

    @BeforeEach
    void setUp() {
        service = new CountryCallingCodesMapProvider(repository, retryMaxAttempts, retryDelay);
    }

    @Test
    void shouldCollectAllCodes() {
        given(repository.findAll()).willReturn(Flux.just(code));
        StepVerifier.create(service.load())
                    .assertNext(map -> assertEquals(1, map.size()))
                    .verifyComplete();
        then(repository).should(times(1)).findAll();
    }

    @Test
    void shouldRetryIfLoadFailed() {
        given(repository.findAll()).willReturn(codesStream(1));
        StepVerifier.withVirtualTime(() -> service.load())
                    .thenAwait(Duration.ofSeconds(1))
                    .assertNext(map -> assertEquals(1, map.size()))
                    .verifyComplete();
    }

    @Test
    void shouldDoNotRetryAfterMaxAttemptsReached() {
        given(repository.findAll()).willReturn(codesStream(2));
        StepVerifier.withVirtualTime(() -> service.load())
                    .thenAwait(Duration.ofSeconds(1))
                    .thenAwait(Duration.ofSeconds(1))
                    .expectErrorMessage("Retries exhausted: 2/2")
                    .verify();
    }

    private Flux<CountryCallingCode> codesStream(int failCount) {
        return Flux.generate(
                AtomicLong::new,
                (state, sink) -> {
                    if (state.incrementAndGet() < failCount) {
                        sink.error(new RuntimeException(state.toString()));
                    } else {
                        sink.next(code);
                        sink.complete();
                    }
                    return state;
                }
        );
    }
}
