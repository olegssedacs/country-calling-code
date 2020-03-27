package com.neotech.ccc.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.neotech.ccc.application.TestFilesUtils.readFile;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "classpath:application-itest.yml"
)
@ActiveProfiles("itest")
public class PhoneNumberDetectionTest {

    @LocalServerPort
    private Integer serverPort;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                                     .baseUrl(String.format("http://localhost:%d", serverPort))
                                     .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                     .build();
    }

    @Test
    void shouldReturnArrayOfCodes() {
        testEndpoint(
                "/jsons/exists/request.json",
                "/jsons/exists/response.json"
        );
    }

    @Test
    void shouldReturnValidationErrorWhenCodeNotFound() {
        testEndpoint(
                "/jsons/notfound/request.json",
                "/jsons/notfound/response.json"
        );
    }

    @Test
    void shouldReturnValidationErrorWhenRequestIsNotJson() {
        testEndpoint(
                "/jsons/notjson/request.json",
                "/jsons/notjson/response.json"
        );
    }

    private void testEndpoint(String pathToRequest, String pathToResponse) {
        var request = readFile(pathToRequest);
        var response = readFile(pathToResponse);
        webTestClient.post()
                     .uri("/phone-numbers/detections")
                     .bodyValue(request)
                     .exchange()
                     .expectBody()
                     .json(response);
    }
}
