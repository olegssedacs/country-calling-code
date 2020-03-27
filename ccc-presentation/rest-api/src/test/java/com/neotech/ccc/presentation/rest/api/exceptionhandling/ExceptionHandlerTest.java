package com.neotech.ccc.presentation.rest.api.exceptionhandling;

import com.neotech.ccc.presentation.rest.api.dto.ApiError;
import com.neotech.ccc.presentation.rest.api.dto.ApiErrorType;
import com.neotech.ccc.presentation.rest.api.dto.RestResponse;
import com.neotech.ccc.presentation.rest.api.dto.ValidationError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerTest {

    @Mock
    private ExceptionConverter exceptionConverter;
    @Mock
    private ServerResponseWriter serverResponseWriter;
    @Mock
    private Clock clock;

    @InjectMocks
    private ExceptionHandler handler;

    @Captor
    private ArgumentCaptor<RestResponse> responseCaptor;

    private HttpStatus status = HttpStatus.BAD_REQUEST;
    private ApiErrorType errorType = ApiErrorType.VALIDATION_ERROR;
    private String message = "Error message";
    private List<ValidationError> validation = List.of();

    private ApiErrorDetails errorDetails = new ApiErrorDetails(
            status,
            errorType,
            message,
            validation
    );

    private MockServerHttpRequest request = MockServerHttpRequest.get("/test")
                                                                 .build();
    private MockServerWebExchange exchange = MockServerWebExchange.from(request);

    @BeforeEach
    void setUp() {
        LocalDateTime mockTime = LocalDateTime.of(2020, 3, 27, 12, 59, 0);
        given(clock.getZone()).willReturn(ZoneOffset.UTC);
        given(clock.instant()).willReturn(mockTime.toInstant(ZoneOffset.UTC));
        given(clock.millis()).willReturn(1L);

        given(serverResponseWriter.write(any(), any(), any())).willReturn(Mono.empty());
        given(exceptionConverter.convert(any())).willReturn(errorDetails);
    }

    @Test
    void shouldCreateErrorAndWriteErrorResponse() {
        StepVerifier.create(handler.handle(exchange, new RuntimeException("mock")))
                    .verifyComplete();
        then(serverResponseWriter).should().write(eq(status), responseCaptor.capture(), eq(exchange));
        var restResponse = responseCaptor.getValue();

        assertAll("Rest response",
                () -> assertNull(restResponse.getData()),
                () -> assertNotNull(restResponse.getError()),
                () -> assertEquals(ApiError.class, restResponse.getError().getClass())
        );
        var error = (ApiError) restResponse.getError();
        assertAll("Error",
                () -> assertSame(errorType, error.getErrorType()),
                () -> assertSame(status, error.getStatusDescription()),
                () -> assertEquals(status.value(), error.getStatusCode()),
                () -> assertSame(1L, error.getEpochMillisTimestamp()),
                () -> assertEquals(ZonedDateTime.of(2020, 3, 27, 12, 59, 0, 0, ZoneOffset.UTC), error.getServerTimestamp()),
                () -> assertEquals(ObjectUtils.getIdentityHexString(request), error.getRequestId()),
                () -> assertSame(validation, error.getValidation()),
                () -> assertEquals("/test", error.getPath()),
                () -> assertSame(message, error.getMessage())
        );
    }
}