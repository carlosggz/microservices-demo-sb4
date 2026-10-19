package org.example.apigateway.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.apigateway.domain.components.LoginComponent;
import org.example.apigateway.domain.models.LoginRequest;
import org.example.apigateway.domain.models.LoginResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final LoginComponent loginComponent;

    public Mono<LoginResponse> getToken(@NonNull LoginRequest loginRequest) {
        Assert.notNull(loginRequest, "Login request must not be null");

        return loginComponent
                .getToken(loginRequest)
                .doOnError(throwable -> log.error("Error occurred: {}", throwable.getMessage()));
    }
}
