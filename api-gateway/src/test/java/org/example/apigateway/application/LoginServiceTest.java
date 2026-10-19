package org.example.apigateway.application;

import lombok.val;
import org.example.apigateway.domain.models.LoginRequest;
import org.example.apigateway.domain.models.LoginResponse;
import org.example.apigateway.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.test.StepVerifier;

class LoginServiceTest extends IntegrationTest  {
    @Autowired
    private LoginService loginService;

    @Test
    void whenLoginWithValidCredentialsShouldReturnToken() {
        //given
        val expectedResponse = new LoginResponse("token", 100, 200, "refresh", "type", "scope");

        stubPostUrl("/token", expectedResponse, HttpStatus.OK);

        //when/then
        StepVerifier
                .create(loginService.getToken(new LoginRequest("user", "password")))
                .expectNext(expectedResponse)
                .verifyComplete();
    }

    @Test
    void whenLoginWithInvalidCredentialsShouldReturnError() {
        //given
        stubPostUrl("/token", "some error", HttpStatus.UNAUTHORIZED);

        //when/then
        StepVerifier
                .create(loginService.getToken(new LoginRequest("user", "password")))
                .expectErrorMatches(e -> e instanceof ResponseStatusException r && r.getStatusCode() == HttpStatus.UNAUTHORIZED)
                .verify();
    }
}