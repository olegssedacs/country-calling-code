package com.neotech.ccc.presentation.rest.api.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalTimeoutWebFilter implements WebFilter {

    private final Duration timeout;

    public GlobalTimeoutWebFilter(@Value("${spring.http.global-timeout}") Duration timeout) {
        this.timeout = timeout;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange).timeout(timeout);
    }

}
