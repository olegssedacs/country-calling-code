package com.neotech.ccc.presentation.rest.api.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GlobalTimeoutWebFilterTest {

    @Mock
    private ServerWebExchange exchange;

    @Mock
    private WebFilterChain chain;

    @BeforeEach
    void setUp() {
        given(chain.filter(exchange)).willReturn(Mono.empty());
    }

    @Test
    void shouldInterruptByTimout() {
        var filter = new GlobalTimeoutWebFilter(Duration.ofMillis(0));
        StepVerifier.withVirtualTime(() -> filter.filter(exchange, chain))
                    .expectTimeout(Duration.ofMillis(1))
                    .verify();
    }

    @Test
    void shouldProceedSuccessfully() {
        var filter = new GlobalTimeoutWebFilter(Duration.ofSeconds(60));
        StepVerifier.create(filter.filter(exchange, chain))
                    .verifyComplete();
    }
}