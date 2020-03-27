package com.neotech.ccc.infrastructure.wiki;

import com.neotech.ccc.domain.model.entities.Country;
import com.neotech.ccc.infrastructure.wiki.client.WikiParse;
import com.neotech.ccc.infrastructure.wiki.client.WikiParseRequest;
import com.neotech.ccc.infrastructure.wiki.client.WikiWebClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class WikiCountryRepositoryTest {

    @Mock
    private WikiWebClient wikiWebClient;

    private String page = "List_of_country_calling_codes";
    private String section = "1";
    private WikiCountryRepository service;

    @BeforeEach
    void setUp() {
        service = new WikiCountryRepository(wikiWebClient, page, section);
    }

    @Test
    void verifyWikiInvocation() {
        mockWikiResponse(null);
        StepVerifier.create(service.findAll())
                    .verifyComplete();
        ArgumentCaptor<WikiParseRequest> requestCaptor = ArgumentCaptor.forClass(WikiParseRequest.class);
        then(wikiWebClient).should().requestParseAction(requestCaptor.capture());
        WikiParseRequest wikiRequest = requestCaptor.getValue();
        assertAll("Wiki argument",
                () -> assertSame(WikiParseRequest.Format.WIKITEXT, wikiRequest.getFormat()),
                () -> assertSame(page, wikiRequest.getPage()),
                () -> assertSame(section, wikiRequest.getSection())
        );
    }

    @Test
    void shouldParseAllCountriesWithAlpha2Codes() {
        mockWikiResponse(""
                + "|[[Area code 242|+1 242]]: [[The Bahamas|BS]]<br />\n"
                + "[[Canada|CA]], [[United States|US]]"
                + "| [[+20]]: [[Egypt|EG]]"
                + "[[Telephone numbers in the Dominican Republic|+1 809]]: [[Dominican Republic|DO]]<br />"
                + "| align=center |[[+7]]: [[Russia|RU]], [[Kazakhstan|KZ]]"
        );
        StepVerifier.create(service.findAll())
                    .expectNext(new Country("The Bahamas", "BS"))
                    .expectNext(new Country("Canada", "CA"))
                    .expectNext(new Country("United States", "US"))
                    .expectNext(new Country("Egypt", "EG"))
                    .expectNext(new Country("Dominican Republic", "DO"))
                    .expectNext(new Country("Russia", "RU"))
                    .expectNext(new Country("Kazakhstan", "KZ"))
                    .verifyComplete();
    }

    @Test
    void shouldBeEmptyIfNoCountriesPresent() {
        mockWikiResponse("no countries");
        StepVerifier.create(service.findAll())
                    .verifyComplete();
    }

    @Test
    void shouldBeEmptyWhenNoWikitextPresent() {
        mockWikiResponse(null);
        StepVerifier.create(service.findAll())
                    .verifyComplete();
    }

    private void mockWikiResponse(String data) {
        WikiParse parse = WikiParse.withContent(data);
        given(wikiWebClient.requestParseAction(any()))
                .willReturn(Mono.just(parse));
    }
}