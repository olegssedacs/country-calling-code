package com.neotech.ccc.infrastructure.wiki.client;

import lombok.Value;

@Value
public class WikiParseRequest {

    private String page;
    private String section;
    private Format format;

    public enum Format {
        TEXT, WIKITEXT
    }

}
