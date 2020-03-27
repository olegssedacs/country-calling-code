package com.neotech.ccc.presentation.rest.api.dto;

import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.List;

@Value
public class ApiError {

    private final ZonedDateTime serverTimestamp;
    private final long epochMillisTimestamp;
    private final String path;
    private final int statusCode;
    private final HttpStatus statusDescription;
    private final ApiErrorType errorType;
    private final String message;
    private final List<ValidationError> validation;
    private final String requestId;

}
