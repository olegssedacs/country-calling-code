package com.neotech.ccc.infrastructure.wiki;

import com.neotech.ccc.domain.model.entities.Country;
import com.neotech.ccc.domain.model.entities.CountryCallingCode;
import com.neotech.ccc.domain.model.repositories.CountryCallingCodeRepository;
import com.neotech.ccc.domain.model.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.function.TupleUtils;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Map;

@Repository
public class WikiCountryCallingCodeRepository implements CountryCallingCodeRepository {

    private final WikiCallingCodeRepository wikiCallingCodeRepository;
    private final CountryRepository wikiCountryRepository;

    @Autowired
    public WikiCountryCallingCodeRepository(WikiCallingCodeRepository wikiCallingCodeRepository,
                                            CountryRepository wikiCountryRepository) {
        this.wikiCallingCodeRepository = wikiCallingCodeRepository;
        this.wikiCountryRepository = wikiCountryRepository;
    }

    public Flux<CountryCallingCode> findAll() {
        var callingCodes = wikiCallingCodeRepository.findAll();
        return wikiCountryRepository
                .findAll()
                .collectMap(Country::getName, Country::getAlpha2Code)
                .flatMapMany(countries -> convert(callingCodes, countries));
    }

    private Flux<CountryCallingCode> convert(Flux<WikiCallingCode> callingCodes, Map<String, String> countries) {
        return callingCodes.map(code -> zipAlpha2Code(code, countries))
                           .filter(callingCodeWithAlpha2 -> !callingCodeWithAlpha2.getT2().isEmpty())
                           .groupBy(
                                   TupleUtils.function(this::toCallingCode),
                                   TupleUtils.function(this::toCountry))
                           .flatMap(this::mergeCallingCodeAndCountries);
    }

    private Tuple2<WikiCallingCode, String> zipAlpha2Code(WikiCallingCode callingCode, Map<String, String> countries) {
        return Tuples.of(
                callingCode,
                countries.getOrDefault(callingCode.getCountry(), "")
        );
    }

    private Long toCallingCode(WikiCallingCode callingCode, String alpha2Code) {
        return Long.parseLong(callingCode.getCode());
    }

    private Country toCountry(WikiCallingCode callingCode, String alpha2Code) {
        return new Country(
                callingCode.getCountry(),
                alpha2Code
        );
    }

    private Flux<CountryCallingCode> mergeCallingCodeAndCountries(GroupedFlux<Long, Country> groupedCountries) {
        return groupedCountries.collectList()
                               .map(countriesPerCode -> new CountryCallingCode(groupedCountries.key(), countriesPerCode))
                               .flux();
    }
}
