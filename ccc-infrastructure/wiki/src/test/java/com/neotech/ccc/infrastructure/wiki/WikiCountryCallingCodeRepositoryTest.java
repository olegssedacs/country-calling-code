package com.neotech.ccc.infrastructure.wiki;

import com.neotech.ccc.domain.model.entities.Country;
import com.neotech.ccc.domain.model.entities.CountryCallingCode;
import com.neotech.ccc.domain.model.repositories.CountryCallingCodeRepository;
import com.neotech.ccc.domain.model.repositories.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class WikiCountryCallingCodeRepositoryTest {

    @Mock
    private WikiCallingCodeRepository wikiCallingCodeRepository;

    @Mock
    private CountryRepository countryRepository;

    private CountryCallingCodeRepository repository;

    @BeforeEach
    void setUp() {
        repository = new WikiCountryCallingCodeRepository(wikiCallingCodeRepository, countryRepository);
    }

    @Test
    void shouldBeEmptyWhenCountiesNotLoaded() {
        given(countryRepository.findAll()).willReturn(Flux.empty());
        given(wikiCallingCodeRepository.findAll()).willReturn(Flux.just(new WikiCallingCode("Latvia", "371")));
        StepVerifier.create(repository.findAll())
                    .verifyComplete();
    }

    @Test
    void shouldBeEmptyWhenCallingCodesNotLoaded() {
        given(countryRepository.findAll()).willReturn(Flux.just(new Country("Latvia", "LV")));
        given(wikiCallingCodeRepository.findAll()).willReturn(Flux.empty());
        StepVerifier.create(repository.findAll())
                    .verifyComplete();
    }

    @Test
    void shouldMapAllCodesWithCountryCodeOnly() {
        given(countryRepository.findAll()).willReturn(Flux.just(
                new Country("Latvia", "LV"),
                new Country("Lithuania", "LT"),
                new Country("Estonia", "EE")
        ));
        given(wikiCallingCodeRepository.findAll()).willReturn(Flux.just(
                new WikiCallingCode("Latvia", "371"),
                new WikiCallingCode("Lithuania", "372")
        ));
        StepVerifier.create(repository.findAll())
                    .expectNext(new CountryCallingCode(371L, singletonList(new Country("Latvia", "LV"))))
                    .expectNext(new CountryCallingCode(372L, singletonList(new Country("Lithuania", "LT"))))
                    .verifyComplete();
    }

    @Test
    void shouldGroupCountriesByCode() {
        given(countryRepository.findAll()).willReturn(Flux.just(
                new Country("United States", "US"),
                new Country("Canada", "CA")
        ));
        given(wikiCallingCodeRepository.findAll()).willReturn(Flux.just(
                new WikiCallingCode("United States", "1"),
                new WikiCallingCode("Canada", "1")
        ));
        StepVerifier.create(repository.findAll())
                    .assertNext(code -> {
                        assertEquals(1L, code.getCallingCode());
                        assertEquals(2, code.getCountries().size());
                        assertThat(code.getCountries(), hasItem(new Country("United States", "US")));
                        assertThat(code.getCountries(), hasItem(new Country("Canada", "CA")));
                    })
                    .verifyComplete();
    }
}