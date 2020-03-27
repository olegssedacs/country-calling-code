package com.neotech.ccc.presentation.rest.api.exceptionhandling;

import com.neotech.ccc.presentation.rest.api.dto.ApiErrorType;
import org.springframework.web.server.ResponseStatusException;

public interface ResponseStatusExceptionConverter extends ExceptionConverter {

    @Override
    default ApiErrorDetails convert(Throwable ex) {
        var exception = (ResponseStatusException) ex;
        return ApiErrorDetails.builder()
                              .status(exception.getStatus())
                              .errorType(ApiErrorType.CLIENT_BAD_USAGE_ERROR)
                              .message(exception.getReason())
                              .build();
    }
}
