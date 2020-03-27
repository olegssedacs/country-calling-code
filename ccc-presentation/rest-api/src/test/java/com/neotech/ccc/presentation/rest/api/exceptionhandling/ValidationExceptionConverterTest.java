package com.neotech.ccc.presentation.rest.api.exceptionhandling;

import com.neotech.ccc.domain.model.utils.validation.ValidationException;
import com.neotech.ccc.presentation.rest.api.dto.ApiErrorType;
import com.neotech.ccc.presentation.rest.api.dto.ValidationError;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationExceptionConverterTest {

    private ValidationExceptionConverter converter = new ValidationExceptionConverter();

    @Test
    void shouldConvertAllErrors() {
        BindingResult bindingResult = new DirectFieldBindingResult(new Object(), "mock");
        bindingResult.addError(new FieldError("mock", "field1", "wrong value", false, null, null, "incorrect field"));
        bindingResult.addError(new ObjectError("mock", "wrong object"));
        var errorDetails = converter.convert(new ValidationException(bindingResult));
        assertAll("Error details",
                () -> assertEquals(HttpStatus.BAD_REQUEST, errorDetails.getStatus()),
                () -> assertEquals(ApiErrorType.VALIDATION_ERROR, errorDetails.getErrorType()),
                () -> assertEquals("Validation failed", errorDetails.getMessage()),
                () -> assertThat(errorDetails.getValidation(), hasItem(new ValidationError("mock", "incorrect field", "wrong value"))),
                () -> assertThat(errorDetails.getValidation(), hasItem(new ValidationError("mock", "wrong object", null)))
        );
    }

    @Test
    void shouldSupportValidationException() {
        assertEquals(ValidationException.class, converter.supports());
    }
}