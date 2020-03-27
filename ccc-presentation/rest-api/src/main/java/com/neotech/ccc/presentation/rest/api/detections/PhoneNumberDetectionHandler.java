package com.neotech.ccc.presentation.rest.api.detections;

import com.neotech.ccc.domain.model.entities.PhoneNumber;
import com.neotech.ccc.domain.model.services.CountryCallingCodeService;
import com.neotech.ccc.presentation.rest.api.dto.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class PhoneNumberDetectionHandler implements HandlerFunction<ServerResponse> {

    private final CountryCallingCodeService service;

    @Autowired
    public PhoneNumberDetectionHandler(CountryCallingCodeService service) {
        this.service = service;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.bodyToMono(PhoneNumberRequest.class)
                      .map(PhoneNumberRequest::getPhoneNumber)
                      .onErrorReturn(new PhoneNumber())
                      .flatMap(service::findBy)
                      .map(RestResponse::withData)
                      .flatMap(callingCode -> ServerResponse.ok().bodyValue(callingCode));
    }
}
