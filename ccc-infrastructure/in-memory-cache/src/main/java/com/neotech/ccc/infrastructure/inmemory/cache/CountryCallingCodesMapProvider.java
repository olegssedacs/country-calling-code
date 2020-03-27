package com.neotech.ccc.infrastructure.inmemory.cache;

import com.neotech.ccc.domain.model.entities.CountryCallingCodesMap;
import com.neotech.ccc.domain.model.repositories.CountryCallingCodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@Slf4j
public class CountryCallingCodesMapProvider {

    private final CountryCallingCodeRepository repository;
    private final Duration retryDelay;
    private final long retryMaxAttempts;

    @Autowired
    public CountryCallingCodesMapProvider(CountryCallingCodeRepository repository,
                                          @Value("${spring.cache.in-memory.country-calling-codes-map.reload.retry-max-attemts}") long retryMaxAttempts,
                                          @Value("${spring.cache.in-memory.country-calling-codes-map.reload.retry-delay}") Duration retryDelay) {
        this.repository = repository;
        this.retryDelay = retryDelay;
        this.retryMaxAttempts = retryMaxAttempts;
    }

    public Mono<CountryCallingCodesMap> load() {
        log.info("Starting countries calling codes loading");
        var retryPolicy = Retry.fixedDelay(retryMaxAttempts, retryDelay);
        return repository.findAll()
                         .collectList()
                         .map(CountryCallingCodesMap::new)
                         .doOnError(t -> log.error("Reload failed with", t))
                         .retryWhen(retryPolicy)
                         .doOnNext(map -> log.info("Finished countries calling codes loading"));
    }
}
