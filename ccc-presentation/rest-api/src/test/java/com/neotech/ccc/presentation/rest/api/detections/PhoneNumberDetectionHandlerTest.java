package com.neotech.ccc.presentation.rest.api.detections;

import com.neotech.ccc.domain.model.entities.Country;
import com.neotech.ccc.domain.model.entities.CountryCallingCode;
import com.neotech.ccc.domain.model.entities.PhoneNumber;
import com.neotech.ccc.domain.model.services.CountryCallingCodeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PhoneNumberDetectionHandlerTest {

    @Mock
    private ServerRequest serverRequest;

    @Mock
    private CountryCallingCodeService service;

    @InjectMocks
    private PhoneNumberDetectionHandler handler;

    private CountryCallingCode resultCode = new CountryCallingCode(371L, List.of(new Country("Latvia", "LV")));

    @Test
    void shouldInvokeServiceAndBuildResponse() {
        given(serverRequest.bodyToMono(PhoneNumberRequest.class)).willReturn(Mono.just(PhoneNumberRequest.fromString("371000")));
        given(service.findBy(any())).willReturn(Mono.just(resultCode));
        StepVerifier.create(handler.handle(serverRequest))
                    .assertNext(serverResponse -> {
                        assertEquals(HttpStatus.OK, serverResponse.statusCode());
                    }).verifyComplete();
        then(service).should(times(1)).findBy(eq(new PhoneNumber("371000")));
    }

    @Test
    void shouldInvokeServiceWithEmptyPhoneWhenBodyCannotBeRead() {
        given(serverRequest.bodyToMono(PhoneNumberRequest.class))
                .willReturn(Mono.error(new DecodingException("mock")));
        given(service.findBy(any())).willReturn(Mono.just(resultCode));
        StepVerifier.create(handler.handle(serverRequest))
                    .assertNext(serverResponse -> {
                        assertEquals(HttpStatus.OK, serverResponse.statusCode());
                    }).verifyComplete();
        then(service).should(times(1)).findBy(eq(new PhoneNumber()));
    }
}