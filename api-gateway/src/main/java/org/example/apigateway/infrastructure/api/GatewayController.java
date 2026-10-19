package org.example.apigateway.infrastructure.api;

import lombok.RequiredArgsConstructor;
import org.example.apigateway.application.LoginService;
import org.example.apigateway.domain.models.LoginRequest;
import org.example.apigateway.domain.models.LoginResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequestMapping("/api/v1/gateway")
@RestController
@RequiredArgsConstructor
public class GatewayController {

    private final LoginService loginService;

    @GetMapping("/ping")
    public Mono<String> ping() {
        return Mono.just("Welcome to Api Gateway - " + LocalDateTime.now());
    }

    @PostMapping("/login")
    public Mono<LoginResponse> authenticate(@RequestBody LoginRequest loginRequest) {

        return loginService
                .getToken(loginRequest);
    }
}
