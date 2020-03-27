package com.neotech.ccc.presentation.rest.api.exceptionhandling;


import com.neotech.ccc.presentation.rest.api.dto.ApiError;
import com.neotech.ccc.presentation.rest.api.dto.ApiErrorType;
import com.neotech.ccc.presentation.rest.api.dto.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.ZonedDateTime;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ExceptionHandler implements ErrorWebExceptionHandler {

    private final ExceptionConverter exceptionConverter;
    private final ServerResponseWriter serverResponseWriter;
    private final Clock clock;

    @Autowired
    public ExceptionHandler(ExceptionConverter exceptionConverter,
                            ServerResponseWriter serverResponseWriter,
                            Clock clock) {
        this.exceptionConverter = exceptionConverter;
        this.serverResponseWriter = serverResponseWriter;
        this.clock = clock;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        return Mono.fromSupplier(() -> exceptionConverter.convert(ex))
                   .map(details -> convert(exchange, details))
                   .doOnNext(error -> logException(error, ex))
                   .flatMap(error -> write(error, exchange));
    }

    private ApiError convert(ServerWebExchange exchange, ApiErrorDetails details) {
        return new ApiError(
                ZonedDateTime.now(clock),
                clock.millis(),
                exchange.getRequest().getPath().toString(),
                details.getStatus().value(),
                details.getStatus(),
                details.getErrorType(),
                details.getMessage(),
                details.getValidation(),
                exchange.getRequest().getId()
        );
    }

    private void logException(ApiError error, Throwable ex) {
        if (error.getErrorType() == ApiErrorType.INTERNAL_SERVER_ERROR) {
            log.error("Internal error. Error response : {}", error, ex);
        } else if (log.isDebugEnabled()) {
            log.debug("Client error. Error response : {}", error, ex);
        }
    }

    private Mono<Void> write(ApiError error, ServerWebExchange exchange) {
        return serverResponseWriter.write(
                error.getStatusDescription(),
                RestResponse.withError(error),
                exchange
        );
    }
}
