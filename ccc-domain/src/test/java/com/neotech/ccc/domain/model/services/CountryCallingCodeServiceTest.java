package com.neotech.ccc.domain.model.services;

import com.neotech.ccc.domain.model.entities.CountryCallingCode;
import com.neotech.ccc.domain.model.entities.CountryCallingCodesMap;
import com.neotech.ccc.domain.model.entities.PhoneNumber;
import com.neotech.ccc.domain.model.repositories.CountryCallingCodesMapRepository;
import com.neotech.ccc.domain.model.utils.validation.ReactiveValidator;
import com.neotech.ccc.domain.model.utils.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CountryCallingCodeServiceTest {

    @Mock
    private CountryCallingCodesMapRepository codesMapRepository;

    @Mock
    private ReactiveValidator reactiveValidator;

    @Mock
    private CountryCallingCodesMap codesMap;

    @InjectMocks
    private CountryCallingCodeService service;

    private PhoneNumber phoneNumber = new PhoneNumber("12345");
    private CountryCallingCode callingCode = new CountryCallingCode();

    @BeforeEach
    void setUp() {
        given(reactiveValidator.validate(phoneNumber, "phoneNumber")).willReturn(Mono.just(phoneNumber));
    }

    @Test
    void shouldValidateAndResolvePhoneNumber() {
        given(codesMapRepository.find()).willReturn(Mono.just(codesMap));
        given(codesMap.get(phoneNumber)).willReturn(Optional.of(callingCode));
        StepVerifier.create(service.findBy(phoneNumber))
                    .expectNext(callingCode)
                    .verifyComplete();
    }

    @Test
    void shouldFailWhenValidationFails() {
        given(reactiveValidator.validate(phoneNumber, "phoneNumber"))
                .willReturn(Mono.error(new RuntimeException("mock")));
        StepVerifier.create(service.findBy(phoneNumber))
                    .expectErrorMessage("mock")
                    .verify();
        then(codesMapRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldReturnErrorWhenCodeNotFound() {
        given(codesMapRepository.find()).willReturn(Mono.just(codesMap));
        given(codesMap.get(phoneNumber)).willReturn(Optional.empty());
        StepVerifier.create(service.findBy(phoneNumber))
                    .verifyErrorSatisfies(t -> {
                        assertEquals(ValidationException.class, t.getClass());
                        var bindingResult = ((ValidationException) t).getBindingResult();
                        assertEquals(1, bindingResult.getFieldErrorCount());
                        assertEquals(0, bindingResult.getGlobalErrorCount());
                        var error = bindingResult.getFieldError();
                        assertEquals("phoneNumber", error.getObjectName());
                        assertEquals("value", error.getField());
                        assertEquals("Phone number does not belong to any country", error.getDefaultMessage());
                    });
    }
}