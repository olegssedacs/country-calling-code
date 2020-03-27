package com.neotech.ccc.presentation.rest.api.exceptionhandling;

import com.neotech.ccc.presentation.rest.api.dto.ApiErrorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Component
class TimeoutExceptionConverter implements ExceptionConverter {

    private final Duration timeout;

    public TimeoutExceptionConverter(@Value("${spring.http.global-timeout}") Duration timeout) {
        this.timeout = timeout;
    }

    @Override
    public ApiErrorDetails convert(Throwable t) {
        return ApiErrorDetails.builder()
                              .status(HttpStatus.GATEWAY_TIMEOUT)
                              .errorType(ApiErrorType.INTERNAL_SERVER_ERROR)
                              .message(String.format("Internal execution timeout after : %d sec", timeout.getSeconds()))
                              .build();
    }

    @Override
    public Class<? extends Throwable> supports() {
        return TimeoutException.class;
    }
}