package com.neotech.ccc.application.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Clock;

@Configuration
public class BeansConfig {

    @Bean
    @ConditionalOnMissingBean
    public WebClient webClient() {
        return WebClient.create();
    }

    @Bean
    @ConditionalOnMissingBean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

}
