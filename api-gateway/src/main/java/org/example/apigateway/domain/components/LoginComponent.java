package org.example.apigateway.domain.components;

import org.example.apigateway.domain.models.LoginRequest;
import org.example.apigateway.domain.models.LoginResponse;
import org.jspecify.annotations.NonNull;
import reactor.core.publisher.Mono;

public interface LoginComponent {
    Mono<LoginResponse> getToken(@NonNull LoginRequest loginRequest) ;
}
