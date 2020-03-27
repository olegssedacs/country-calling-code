package com.neotech.ccc.domain.model.repositories;

import com.neotech.ccc.domain.model.entities.CountryCallingCode;
import reactor.core.publisher.Flux;

public interface CountryCallingCodeRepository {

    Flux<CountryCallingCode> findAll();

}
