package com.neotech.ccc.domain.model.utils.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class ReactiveValidatorTest {

    @Mock
    private Validator springValidator;

    @InjectMocks
    private ReactiveValidator reactiveValidator;

    private Object target = new Object();
    private String targetName = "target name";

    @Test
    void shouldReturnSameObjectWhenValid() {
        willDoNothing().given(springValidator).validate(eq(target), any());
        StepVerifier.create(reactiveValidator.validate(target, targetName))
                    .expectNext(target)
                    .verifyComplete();
    }

    @Test
    void shouldFailWhenObjectIsNotValid() {
        var error = new ObjectError("", "");
        willAnswer(invocation -> {
            var bindingResult = invocation.getArgument(1, BindingResult.class);
            bindingResult.addError(error);
            return null;
        }).given(springValidator).validate(eq(target), any());
        StepVerifier.create(reactiveValidator.validate(target, targetName))
                    .verifyErrorSatisfies(t -> {
                        assertEquals(ValidationException.class, t.getClass());
                        var bindingResult = ((ValidationException) t).getBindingResult();
                        assertEquals(0, bindingResult.getFieldErrorCount());
                        assertEquals(1, bindingResult.getGlobalErrorCount());
                        assertSame(error, bindingResult.getGlobalError());
                    });
    }
}