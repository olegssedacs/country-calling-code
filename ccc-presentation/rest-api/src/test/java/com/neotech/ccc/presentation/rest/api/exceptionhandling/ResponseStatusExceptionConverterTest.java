package com.neotech.ccc.presentation.rest.api.exceptionhandling;

import com.neotech.ccc.presentation.rest.api.dto.ApiErrorType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ResponseStatusExceptionConverterTest {

    private ResponseStatusExceptionConverter converter = new ResponseStatusExceptionConverter() {
        @Override
        public Class<? extends Throwable> supports() {
            return MethodNotAllowedException.class;
        }
    };

    @Test
    void verifyClientError() {
        var errorDetails = converter.convert(new MethodNotAllowedException(HttpMethod.GET, List.of(HttpMethod.POST)));
        assertAll("Error details",
                () -> assertEquals(HttpStatus.METHOD_NOT_ALLOWED, errorDetails.getStatus()),
                () -> assertEquals(ApiErrorType.CLIENT_BAD_USAGE_ERROR, errorDetails.getErrorType()),
                () -> assertEquals("Request method 'GET' not supported", errorDetails.getMessage()),
                () -> assertNull(errorDetails.getValidation())
        );
    }
}