package com.neotech.ccc.domain.model.repositories;

import com.neotech.ccc.domain.model.entities.Country;
import reactor.core.publisher.Flux;

public interface CountryRepository {

    Flux<Country> findAll();
}
