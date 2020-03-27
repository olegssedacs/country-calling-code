package com.neotech.ccc.presentation.rest.api.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.WebFluxConfigurer;

public class WebConfig {

//    @Bean(name = "objectMapper")
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        return objectMapper;
//    }
//
//    @Bean
//    public Jackson2JsonEncoder jackson2JsonEncoder(@Qualifier("objectMapper") ObjectMapper objectMapper) {
//        return new Jackson2JsonEncoder(objectMapper);
//    }
//
//    @Bean
//    public Jackson2JsonDecoder jackson2JsonDecoder(@Qualifier("objectMapper") ObjectMapper objectMapper) {
//        return new Jackson2JsonDecoder(objectMapper);
//    }
//
//    @Bean
//    public WebFluxConfigurer webFluxConfigurer(Jackson2JsonEncoder jackson2JsonEncoder,
//                                               Jackson2JsonDecoder jackson2JsonDecoder) {
//        return new WebFluxConfigurer() {
//            @Override
//            public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
//                configurer.defaultCodecs().jackson2JsonEncoder(jackson2JsonEncoder);
//                configurer.defaultCodecs().jackson2JsonDecoder(jackson2JsonDecoder);
//            }
//        };
//
//    }

}
