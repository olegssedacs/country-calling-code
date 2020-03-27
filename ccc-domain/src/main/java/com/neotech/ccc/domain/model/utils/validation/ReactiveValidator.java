package com.neotech.ccc.domain.model.utils.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

@Component
public class ReactiveValidator {

    private final Validator validator;

    @Autowired
    public ReactiveValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> Mono<T> validate(T object, String objectName) {
        var validate = Mono.fromRunnable(() -> validateSync(object, objectName));
        return Mono.when(validate)
                   .thenReturn(object);
    }

    public void validateSync(Object object, String objectName) {
        var bindingResult = new DirectFieldBindingResult(object, objectName);
        validator.validate(object, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
    }
}
