package com.neotech.ccc.application.itest;

import org.apache.tika.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertNotNull;

public final class TestFilesUtils {

    private TestFilesUtils(){}

    public static String readFile(String resourcePath) {
        try {
            var testResource = TestFilesUtils.class.getResourceAsStream(resourcePath);
            assertNotNull(String.format("Test resource file '%s' not found.", resourcePath), testResource);
            return IOUtils.toString(testResource, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
