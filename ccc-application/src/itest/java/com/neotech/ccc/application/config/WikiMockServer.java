package com.neotech.ccc.application.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.neotech.ccc.application.TestFilesUtils.readFile;
import static org.springframework.cloud.contract.wiremock.WireMockSpring.options;

@Component
public class WikiMockServer implements DisposableBean {

    private final WireMockServer wireMockServer;
    private final URI uri;
    private final String url;
    private final String page;
    private final String treeListSection;
    private final String alphabeticalListSection;

    public WikiMockServer(@Value("${wiki.rest-api.url}") String url,
                          @Value("${wiki.rest-api.country-calling-codes.page}") String page,
                          @Value("${wiki.rest-api.country-calling-codes.section-ids.tree-list}") String treeListSection,
                          @Value("${wiki.rest-api.country-calling-codes.section-ids.alphabetical-listing-by-country}") String alphabeticalListSection) {
        this.url = url;
        this.page = page;
        this.treeListSection = treeListSection;
        this.alphabeticalListSection = alphabeticalListSection;
        this.uri = URI.create(url);
        wireMockServer = new WireMockServer(options().port(uri.getPort()));
        wireMockServer.start();
        mockTreeListResponse();
        mockAlphabeticalListResponse();
    }

    @Override
    public void destroy() {
        wireMockServer.stop();
    }

    private void mockTreeListResponse() {
        var response = readFile("/wiki/tree-list-response.json");
        wireMockServer.stubFor(get(urlPathEqualTo(uri.getPath()))
                .withQueryParam("action", equalTo("parse"))
                .withQueryParam("format", equalTo("json"))
                .withQueryParam("page", equalTo(page))
                .withQueryParam("section", equalTo(treeListSection))
                .withQueryParam("prop", equalTo("wikitext"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-type", "application/json; charset=utf-8")
                        .withBody(response)));
    }

    private void mockAlphabeticalListResponse() {
        var response = readFile("/wiki/alphabetical-listing-by-country-response.json");
        wireMockServer.stubFor(get(urlPathEqualTo(uri.getPath()))
                .withQueryParam("action", equalTo("parse"))
                .withQueryParam("format", equalTo("json"))
                .withQueryParam("page", equalTo(page))
                .withQueryParam("section", equalTo(alphabeticalListSection))
                .withQueryParam("prop", equalTo("text"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-type", "application/json; charset=utf-8")
                        .withBody(response)));
    }
}
