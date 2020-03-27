package com.neotech.ccc.domain.model.repositories;

import com.neotech.ccc.domain.model.entities.CountryCallingCodesMap;
import reactor.core.publisher.Mono;

public interface CountryCallingCodesMapRepository {

    Mono<CountryCallingCodesMap> find();

}
