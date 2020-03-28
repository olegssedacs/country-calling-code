package com.neotech.ccc.presentation.rest.api.detections;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Slf4j
public class PhoneNumberDetectionRouter {

    @Bean
    public RouterFunction<ServerResponse> phoneNumberRouter(PhoneNumberDetectionHandler handler) {
        return route(POST("/v1/phone-numbers/detections"), handler);
    }

}
