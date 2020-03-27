package com.neotech.ccc.presentation.rest.api.exceptionhandling;

import com.neotech.ccc.presentation.rest.api.dto.ApiErrorType;
import com.neotech.ccc.presentation.rest.api.dto.ValidationError;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.List;

@Data
@Builder
class ApiErrorDetails {

    private HttpStatus status;
    private ApiErrorType errorType;
    private String message;
    private List<ValidationError> validation;

}
