package org.example.apigateway.infrastructure.api;

import lombok.val;
import org.example.apigateway.application.LoginService;
import org.example.apigateway.domain.models.LoginRequest;
import org.example.apigateway.domain.models.LoginResponse;
import org.example.apigateway.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@AutoConfigureWebTestClient
class GatewayControllerTest extends IntegrationTest {

    @MockitoSpyBean
    private LoginService loginService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenCallAuthenticatesItReturnsTheResponse() {
        //given
        val request = new LoginRequest("username", "password");
        val response = new LoginResponse("token", 1, 2, "refresh", "type", "scope");
        doReturn(Mono.just(response))
                .when(loginService)
                .getToken(request);

        //when
        webTestClient
                .post()
                .uri("/api/v1/gateway/login")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginResponse.class)
                .isEqualTo(response);
    }

    @Test
    void whenCallAuthenticatesWithInvalidCredentialsItReturnsUnauthorize() {
        //given
        val request = new LoginRequest("username", "password");
        doReturn(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED)))
                .when(loginService)
                .getToken(request);

        //when
        webTestClient
                .post()
                .uri("/api/v1/gateway/login")
                .bodyValue(request)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenCallAuthenticatesWithInvalidValuesItReturnsBadRequest() {
        //given
        val request = new LoginRequest("username", "password");
        doReturn(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST)))
                .when(loginService)
                .getToken(request);

        //when
        webTestClient
                .post()
                .uri("/api/v1/gateway/login")
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenCallPingItReturnsAMessage() {
        webTestClient
                .get()
                .uri("/api/v1/gateway/ping")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(message -> assertTrue(message.startsWith("Welcome to Api Gateway - ")));
    }
}