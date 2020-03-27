package com.neotech.ccc.infrastructure.wiki;

import com.neotech.ccc.domain.model.entities.Country;
import com.neotech.ccc.domain.model.repositories.CountryRepository;
import com.neotech.ccc.infrastructure.wiki.client.WikiParse;
import com.neotech.ccc.infrastructure.wiki.client.WikiParseRequest;
import com.neotech.ccc.infrastructure.wiki.client.WikiWebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WikiCountryRepository implements CountryRepository {

    private static Pattern COUNTRY_PATTERN = Pattern.compile("(\\[\\[)(?<countryName>[A-Za-z ]{1,50})(\\|)(?<countryCode>[A-Z]{2})(]])");

    private final WikiWebClient wikiWebClient;
    private final String page;
    private final String section;

    public WikiCountryRepository(WikiWebClient wikiWebClient,
                                 @Value("${wiki.rest-api.country-calling-codes.page}") String page,
                                 @Value("${wiki.rest-api.country-calling-codes.section-ids.tree-list}") String section) {
        this.wikiWebClient = wikiWebClient;
        this.page = page;
        this.section = section;
    }

    public Flux<Country> findAll() {
        return Mono.fromSupplier(this::createCountriesRequest)
                   .flatMap(wikiWebClient::requestParseAction)
                   .map(WikiParse::getContent)
                   .flatMapMany(this::parseWikitext);
    }

    private WikiParseRequest createCountriesRequest() {
        return new WikiParseRequest(
                this.page,
                this.section,
                WikiParseRequest.Format.WIKITEXT
        );
    }

    private Flux<Country> parseWikitext(String wikitext) {
        return Flux.generate(
                () -> COUNTRY_PATTERN.matcher(wikitext),
                (matcher, sink) -> {
                    if (matcher.find()){
                        Country country = convertOne(matcher);
                        sink.next(country);
                    } else {
                        sink.complete();
                    }
                    return matcher;
                }
        );
    }

    private Country convertOne(Matcher matcher){
        return new Country(
                matcher.group("countryName"),
                matcher.group("countryCode")
        );
    }

}
