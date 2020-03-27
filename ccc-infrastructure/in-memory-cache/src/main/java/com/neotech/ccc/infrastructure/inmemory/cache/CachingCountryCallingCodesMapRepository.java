package com.neotech.ccc.infrastructure.inmemory.cache;

import com.neotech.ccc.domain.model.entities.CountryCallingCodesMap;
import com.neotech.ccc.domain.model.repositories.CountryCallingCodesMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class CachingCountryCallingCodesMapRepository implements CountryCallingCodesMapRepository {

    private final CountryCallingCodesMapProvider countryCallingCodesMapProvider;
    private final AtomicReference<CountryCallingCodesMap> codesMap;

    @Autowired
    public CachingCountryCallingCodesMapRepository(CountryCallingCodesMapProvider countryCallingCodesMapProvider) {
        this.countryCallingCodesMapProvider = countryCallingCodesMapProvider;
        this.codesMap = new AtomicReference<>();
        reloadCache();
    }

    @Override
    public Mono<CountryCallingCodesMap> find() {
        return Mono.justOrEmpty(getCachedValue())
                   .switchIfEmpty(defaultFallback());
    }

    public CountryCallingCodesMap getCachedValue() {
        return codesMap.get();
    }

    public void reloadCache() {
        countryCallingCodesMapProvider.load()
                                      .subscribe(codesMap::set);
    }

    private Mono<CountryCallingCodesMap> defaultFallback() {
        return Mono.fromSupplier(() -> new CountryCallingCodesMap(List.of()));
    }
}
