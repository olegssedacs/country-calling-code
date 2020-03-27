package com.neotech.ccc.presentation.rest.api.exceptionhandling;

import com.neotech.ccc.domain.model.utils.validation.ValidationException;
import com.neotech.ccc.presentation.rest.api.dto.ApiErrorType;
import com.neotech.ccc.presentation.rest.api.dto.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
class ValidationExceptionConverter implements ExceptionConverter {

    @Override
    public ApiErrorDetails convert(Throwable t) {
        var errors = convertErrors((ValidationException) t);
        return ApiErrorDetails.builder()
                              .status(HttpStatus.BAD_REQUEST)
                              .errorType(ApiErrorType.VALIDATION_ERROR)
                              .message("Validation failed")
                              .validation(errors)
                              .build();
    }

    private List<ValidationError> convertErrors(ValidationException e) {
        var bindingResult = e.getBindingResult();
        var objectErrors = bindingResult.getGlobalErrors()
                                        .stream()
                                        .map(this::convertOne);
        var fieldErrors = bindingResult.getFieldErrors()
                                       .stream()
                                       .map(this::convertOne);
        return Stream.concat(objectErrors, fieldErrors)
                     .collect(toList());
    }

    private ValidationError convertOne(ObjectError error) {
        return new ValidationError(
                error.getObjectName(),
                error.getDefaultMessage(),
                null
        );
    }

    private ValidationError convertOne(FieldError error) {
        return new ValidationError(
                error.getObjectName(),
                error.getDefaultMessage(),
                error.getRejectedValue()
        );
    }

    @Override
    public Class<? extends Throwable> supports() {
        return ValidationException.class;
    }
}
