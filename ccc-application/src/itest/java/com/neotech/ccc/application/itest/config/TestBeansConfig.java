package com.neotech.ccc.application.itest.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.mockito.BDDMockito.given;

@Configuration
public class TestBeansConfig {

    @Bean
    @Primary
    public Clock clock(){
        var clock = Mockito.mock(Clock.class);
        LocalDateTime mockTime = LocalDateTime.of(2020, 3, 27, 12, 59, 0);
        given(clock.getZone()).willReturn(ZoneOffset.UTC);
        given(clock.instant()).willReturn(mockTime.toInstant(ZoneOffset.UTC));
        given(clock.millis()).willReturn(1585328252447L);
        return clock;
    }

}
