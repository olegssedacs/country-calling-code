package com.neotech.ccc.presentation.rest.api.exceptionhandling;

import com.neotech.ccc.presentation.rest.api.dto.ApiErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Primary
class GeneralExceptionConverter implements ExceptionConverter {

    private final Map<Class<? extends Throwable>, ExceptionConverter> converters;
    private final ThrowableExceptionConverter throwableExceptionConverter;

    @Autowired
    public GeneralExceptionConverter(Set<ExceptionConverter> converters,
                                     @Value("${spring.http.internal-error-message}") String internalErrorMessage) {
        this.converters = converters.stream()
                                    .collect(Collectors.toUnmodifiableMap(ExceptionConverter::supports, Function.identity()));
        this.throwableExceptionConverter = new ThrowableExceptionConverter(internalErrorMessage);
    }

    @Override
    public ApiErrorDetails convert(Throwable t) {
        return converters.getOrDefault(t.getClass(), throwableExceptionConverter)
                         .convert(t);
    }

    @Override
    public Class<? extends Throwable> supports() {
        return Throwable.class;
    }

    private static class ThrowableExceptionConverter implements ExceptionConverter {

        private final String internalErrorMessage;

        public ThrowableExceptionConverter(String internalErrorMessage) {
            this.internalErrorMessage = internalErrorMessage;
        }

        @Override
        public ApiErrorDetails convert(Throwable ex) {
            return ApiErrorDetails.builder()
                                  .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                  .errorType(ApiErrorType.INTERNAL_SERVER_ERROR)
                                  .message(internalErrorMessage)
                                  .build();
        }

        @Override
        public Class<? extends Throwable> supports() {
            return Throwable.class;
        }
    }

}
