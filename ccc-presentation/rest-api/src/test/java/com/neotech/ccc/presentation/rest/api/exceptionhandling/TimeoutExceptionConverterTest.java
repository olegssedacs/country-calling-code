package com.neotech.ccc.presentation.rest.api.exceptionhandling;

import com.neotech.ccc.presentation.rest.api.dto.ApiErrorType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TimeoutExceptionConverterTest {

    private Duration timeout = Duration.ofSeconds(10);
    private TimeoutExceptionConverter converter = new TimeoutExceptionConverter(timeout);

    @Test
    void verifyTimeoutError() {
        var errorDetails = converter.convert(new TimeoutException());
        assertAll("Error details",
                () -> assertEquals(HttpStatus.GATEWAY_TIMEOUT, errorDetails.getStatus()),
                () -> assertEquals(ApiErrorType.INTERNAL_SERVER_ERROR, errorDetails.getErrorType()),
                () -> assertEquals("Internal execution timeout after : 10 sec", errorDetails.getMessage()),
                () -> assertNull(errorDetails.getValidation())
        );
    }

    @Test
    void shouldSupportTimeoutException() {
        assertEquals(TimeoutException.class, converter.supports());
    }
}