package com.neotech.ccc.infrastructure.wiki;

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
class WikiCallingCodeRepositoryTest {

    @Mock
    private WikiWebClient wikiWebClient;

    private String page = "List_of_country_calling_codes";
    private String section = "11";
    private WikiCallingCodeRepository service;

    @BeforeEach
    void setUp() {
        service = new WikiCallingCodeRepository(wikiWebClient, page, section);
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
                () -> assertSame(WikiParseRequest.Format.TEXT, wikiRequest.getFormat()),
                () -> assertSame(page, wikiRequest.getPage()),
                () -> assertSame(section, wikiRequest.getSection())
        );
    }

    @Test
    void shouldParseAllCodes() {
        mockWikiResponse(""
                + "<table>"
                + "<tr><td>USA</td><td>1</td></tr>"
                + "<tr><td>Latvia</td><td>371</td></tr>"
                + "</table>"
        );
        StepVerifier.create(service.findAll())
                    .expectNext(new WikiCallingCode("USA", "1"))
                    .expectNext(new WikiCallingCode("Latvia", "371"))
                    .verifyComplete();
    }

    @Test
    void shouldRemoveAllNonNumberValues() {
        mockWikiResponse(""
                + "<table>"
                + "<tr><td>Cayman Island</td><td>+1 345 &#91;notes 1&#93;</td></tr>"
                + "</table>"
        );
        StepVerifier.create(service.findAll())
                    .expectNext(new WikiCallingCode("Cayman Island", "1345"))
                    .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenTableCellsIsLessThan2() {
        mockWikiResponse(""
                + "<table>"
                + "<tr><td>Cayman Island</td></tr>"
                + "</table>"
        );
        StepVerifier.create(service.findAll())
                    .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenNotHtml() {
        mockWikiResponse("not html");
        StepVerifier.create(service.findAll())
                    .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenTableNotPresent() {
        mockWikiResponse("<body></body>");
        StepVerifier.create(service.findAll())
                    .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenTableIsEmpty() {
        mockWikiResponse("<table></table>");
        StepVerifier.create(service.findAll())
                    .verifyComplete();
    }

    private void mockWikiResponse(String data) {
        WikiParse parse = WikiParse.withContent(data);
        given(wikiWebClient.requestParseAction(any()))
                .willReturn(Mono.just(parse));
    }
}