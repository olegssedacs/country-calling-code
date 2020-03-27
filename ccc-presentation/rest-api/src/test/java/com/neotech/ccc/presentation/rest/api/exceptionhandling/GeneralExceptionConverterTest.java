package com.neotech.ccc.presentation.rest.api.exceptionhandling;

import com.neotech.ccc.presentation.rest.api.dto.ApiErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GeneralExceptionConverterTest {

    @Mock
    private ExceptionConverter runtimeExceptionConverter;

    private String internalErrorMessage = "Testing";

    private GeneralExceptionConverter converter;

    @BeforeEach
    void setUp() {
        Class<? extends Throwable> supportClass = RuntimeException.class;
        Mockito.doReturn(supportClass).when(runtimeExceptionConverter).supports();
        converter = new GeneralExceptionConverter(Set.of(runtimeExceptionConverter), internalErrorMessage);
    }

    @Test
    void shouldInvokeSuitableConverter() {
        var errorDetails = ApiErrorDetails.builder().build();
        given(runtimeExceptionConverter.convert(any(RuntimeException.class)))
                .willReturn(errorDetails);
        var converted = converter.convert(new RuntimeException());
        assertSame(converted, errorDetails);
    }

    @Test
    void shouldHandleInternallyWhenSuitableConverterNotFound() {
        var internalError = converter.convert(new Exception());
        assertAll("Internal error",
                () -> assertEquals(internalErrorMessage, internalError.getMessage()),
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, internalError.getStatus()),
                () -> assertEquals(ApiErrorType.INTERNAL_SERVER_ERROR, internalError.getErrorType()),
                () -> assertNull(internalError.getValidation())
        );
    }

    @Test
    void shouldSupportThrowable() {
        assertEquals(Throwable.class, converter.supports());
    }

}