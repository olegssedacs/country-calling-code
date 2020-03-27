package com.neotech.ccc.presentation.rest.api.detections;

import com.neotech.ccc.domain.model.entities.CountryCallingCode;
import com.neotech.ccc.domain.model.entities.PhoneNumber;
import com.neotech.ccc.domain.model.services.CountryCallingCodeService;
import com.neotech.ccc.presentation.rest.api.detections.PhoneNumberDetectionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Slf4j
public class PhoneNumberDetectionRouter {

    @Bean
    public RouterFunction<ServerResponse> phoneNumberRouter(PhoneNumberDetectionHandler handler){
        return route(POST("/phone-numbers/detections"), handler);
    }

}
