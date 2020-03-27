package com.neotech.ccc.application;

import wiremock.com.github.jknack.handlebars.internal.Files;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertNotNull;

public final class TestFilesUtils {

    private TestFilesUtils() {
    }

    public static String readFile(String resourcePath) {
        try {
            var testResource = TestFilesUtils.class.getResourceAsStream(resourcePath);
            assertNotNull(String.format("Test resource file '%s' not found.", resourcePath), testResource);
            return Files.read(testResource, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
