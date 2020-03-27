package com.neotech.ccc.presentation.rest.api.exceptionhandling;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.MediaTypeNotSupportedStatusException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.NotAcceptableStatusException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

@Configuration
class BadRequestConverters {

    @Bean
    public ResponseStatusExceptionConverter serverWebInputExceptionConverter(){
        return () -> ServerWebInputException.class;
    }

    @Bean
    public ResponseStatusExceptionConverter mediaTypeNotSupportedStatusExceptionConverter() {
        return () -> MediaTypeNotSupportedStatusException.class;
    }

    @Bean
    public ResponseStatusExceptionConverter methodNotAllowedExceptionConverter() {
        return () -> MethodNotAllowedException.class;
    }

    @Bean
    public ResponseStatusExceptionConverter notAcceptableStatusExceptionConverter() {
        return () -> NotAcceptableStatusException.class;
    }

    @Bean
    public ResponseStatusExceptionConverter unsupportedMediaTypeStatusExceptionConverter() {
        return () -> UnsupportedMediaTypeStatusException.class;
    }

    @Bean
    public ResponseStatusExceptionConverter responseStatusExceptionConverter() {
        return () -> ResponseStatusException.class;
    }

}
