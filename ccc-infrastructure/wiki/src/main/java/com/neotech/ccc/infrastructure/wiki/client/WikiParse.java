package com.neotech.ccc.infrastructure.wiki.client;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WikiParse {

    private static final String KEY = "*";

    public static WikiParse withContent(String content) {
        return content == null
               ? new WikiParse()
               : new WikiParse(Collections.singletonMap(KEY, content));
    }

    @JsonProperty("text")
    @JsonAlias("wikitext")
    private Map<String, String> text = Collections.emptyMap();

    @JsonIgnore
    public String getContent() {
        return text.getOrDefault(KEY, "");
    }
}
