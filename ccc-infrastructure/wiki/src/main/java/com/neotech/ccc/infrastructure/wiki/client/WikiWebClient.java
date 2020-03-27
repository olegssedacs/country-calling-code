package com.neotech.ccc.infrastructure.wiki.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
public class WikiWebClient {

    private final WebClient webClient;

    @Autowired
    public WikiWebClient(WebClient webClient,
                         @Value("${wiki.rest-api.url}") String wikiApiUrl) {
        this.webClient = webClient.mutate()
                                  .baseUrl(wikiApiUrl)
                                  .build();
    }

    public Mono<WikiParse> requestParseAction(WikiParseRequest request) {
        return webClient.get()
                        .uri(uri -> buildParseQuery(uri, request))
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .exchange()
                        .flatMap(response -> response.bodyToMono(WikiParserResponse.class))
                        .map(WikiParserResponse::getParse);
    }

    private URI buildParseQuery(UriBuilder builder, WikiParseRequest request) {
        return builder.queryParam("action", "parse")
                      .queryParam("format", "json")
                      .queryParam("page", request.getPage())
                      .queryParam("section", request.getSection())
                      .queryParam("prop", request.getFormat().name().toLowerCase())
                      .build();
    }
}
