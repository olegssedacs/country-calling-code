package com.neotech.ccc.presentation.rest.api.content;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
@ConditionalOnProperty(value = "spring.http.enable-static-content", havingValue = "true")
public class StaticResourcesRouterFunction {

    @Bean
    public RouterFunction<ServerResponse> index(@Value("classpath:/static/index.html") Resource html) {
        return route(GET("/"), request -> ok().contentType(MediaType.TEXT_HTML).bodyValue(html));
    }

    @Bean
    public RouterFunction<ServerResponse> staticResources() {
        return RouterFunctions.resources(
                "/static/**",
                new ClassPathResource("static/")
        );
    }
}
