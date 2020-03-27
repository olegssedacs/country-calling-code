package com.neotech.ccc.domain.model.services;

import com.neotech.ccc.domain.model.entities.CountryCallingCode;
import com.neotech.ccc.domain.model.entities.PhoneNumber;
import com.neotech.ccc.domain.model.repositories.CountryCallingCodesMapRepository;
import com.neotech.ccc.domain.model.utils.validation.ReactiveValidator;
import com.neotech.ccc.domain.model.utils.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.FieldError;
import reactor.core.publisher.Mono;

@Service
public class CountryCallingCodeService {

    private static final String VALIDATED_OBJECT_NAME = "phoneNumber";

    private final CountryCallingCodesMapRepository codesMapRepository;
    private final ReactiveValidator reactiveValidator;

    @Autowired
    public CountryCallingCodeService(CountryCallingCodesMapRepository codesMapRepository,
                                     ReactiveValidator reactiveValidator) {
        this.codesMapRepository = codesMapRepository;
        this.reactiveValidator = reactiveValidator;
    }

    public Mono<CountryCallingCode> findBy(PhoneNumber phoneNumber) {
        return reactiveValidator.validate(phoneNumber, VALIDATED_OBJECT_NAME)
                                .flatMap(this::resolve)
                                .switchIfEmpty(notFound(phoneNumber));
    }

    private Mono<CountryCallingCode> resolve(PhoneNumber phoneNumber) {
        return codesMapRepository.find()
                                 .map(codesMap -> codesMap.get(phoneNumber))
                                 .flatMap(Mono::justOrEmpty);
    }

    private Mono<CountryCallingCode> notFound(PhoneNumber phoneNumber) {
        return Mono.fromSupplier(() -> constructError(phoneNumber))
                   .map(error -> {
                       var result = new DirectFieldBindingResult(phoneNumber, VALIDATED_OBJECT_NAME);
                       result.addError(error);
                       return result;
                   })
                   .map(ValidationException::new)
                   .flatMap(Mono::error);
    }

    private FieldError constructError(PhoneNumber phoneNumber) {
        return new FieldError(
                VALIDATED_OBJECT_NAME,
                "value",
                phoneNumber.getValue(),
                false,
                null,
                null,
                "Phone number does not belong to any country"
        );
    }
}
