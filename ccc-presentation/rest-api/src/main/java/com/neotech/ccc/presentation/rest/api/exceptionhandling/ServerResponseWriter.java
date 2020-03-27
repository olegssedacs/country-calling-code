package com.neotech.ccc.presentation.rest.api.exceptionhandling;

import com.neotech.ccc.presentation.rest.api.dto.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Component
class ServerResponseWriter {

    private final List<HttpMessageWriter<?>> messageWriters;

    @Autowired
    public ServerResponseWriter(ServerCodecConfigurer serverCodecConfigurer) {
        this.messageWriters = Collections.unmodifiableList(serverCodecConfigurer.getWriters());
    }

    public Mono<Void> write(HttpStatus status, RestResponse response, ServerWebExchange exchange) {
        return ServerResponse.status(status)
                             .bodyValue(response)
                             .flatMap(serverResponse -> {
                                 setContentType(exchange);
                                 return serverResponse.writeTo(exchange, new ResponseContext());
                             });
    }

    private void setContentType(ServerWebExchange exchange) {
        // force content-type since writeTo won't overwrite response header values
        exchange.getResponse()
                .getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);
    }

    private class ResponseContext implements ServerResponse.Context {

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return messageWriters;
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return Collections.emptyList();
        }
    }
}
