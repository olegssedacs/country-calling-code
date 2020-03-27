package com.neotech.ccc.infrastructure.wiki;

import com.neotech.ccc.infrastructure.wiki.client.WikiParse;
import com.neotech.ccc.infrastructure.wiki.client.WikiParseRequest;
import com.neotech.ccc.infrastructure.wiki.client.WikiWebClient;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
class WikiCallingCodeRepository {

    private final WikiWebClient wikiWebClient;
    private final String page;
    private final String section;

    public WikiCallingCodeRepository(WikiWebClient wikiWebClient,
                                     @Value("${wiki.rest-api.country-calling-codes.page}") String page,
                                     @Value("${wiki.rest-api.country-calling-codes.section-ids.alphabetical-listing-by-country}") String section) {
        this.wikiWebClient = wikiWebClient;
        this.page = page;
        this.section = section;
    }

    public Flux<WikiCallingCode> findAll() {
        return Mono.fromSupplier(this::createCallingCodesRequest)
                   .flatMap(wikiWebClient::requestParseAction)
                   .map(WikiParse::getContent)
                   .flatMapMany(this::parseHtml);
    }

    private WikiParseRequest createCallingCodesRequest() {
        return new WikiParseRequest(
                this.page,
                this.section,
                WikiParseRequest.Format.TEXT
        );
    }

    private Flux<WikiCallingCode> parseHtml(String html) {
        return Mono.fromSupplier(() -> Jsoup.parseBodyFragment(html))
                   .map(doc -> doc.getElementsByTag("tr"))
                   .flatMapMany(Flux::fromIterable)
                   .map(row -> row.getElementsByTag("td"))
                   .filter(cell -> cell.size() > 1)
                   .flatMap(this::convert);
    }

    private String normalizeCode(String code) {
        return code.replaceAll("(\\[.*?])|([^0-9.])", "");
    }

    private Flux<WikiCallingCode> convert(Elements elements) {
        var country = elements.get(0)
                              .text();
        var codes = elements.get(1)
                            .text()
                            .split(",");
        return Flux.fromArray(codes)
                   .map(this::normalizeCode)
                   .map(code -> new WikiCallingCode(country, code));
    }

}
