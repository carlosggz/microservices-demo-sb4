package org.example.apigateway.infrastructure.components;

import org.example.apigateway.domain.components.LoginComponent;
import org.example.apigateway.domain.models.LoginRequest;
import org.example.apigateway.domain.models.LoginResponse;
import org.example.apigateway.infrastructure.config.AuthSettings;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
public class KeycloakLoginComponent implements LoginComponent {

    private final AuthSettings authSettings;
    private final WebClient loginWebClient;
    private final Duration timeout;

    public KeycloakLoginComponent(
            AuthSettings authSettings,
            WebClient loginWebClient,
            @Value("${app.timeout-seconds}") int timeoutSeconds) {
        this.authSettings = authSettings;
        this.loginWebClient = loginWebClient;
        this.timeout = Duration.ofSeconds(timeoutSeconds);
    }

    @Override
    public Mono<LoginResponse> getToken(@NonNull LoginRequest loginRequest) {
        return loginWebClient
                .post()
                .uri(authSettings.getTokenUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(getBody(loginRequest)))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(LoginResponse.class)
                .onErrorMap(e -> new ResponseStatusException(e instanceof WebClientResponseException.Unauthorized
                        ? HttpStatus.UNAUTHORIZED
                        : HttpStatus.BAD_REQUEST))
                .timeout(timeout);
    }

    private MultiValueMap<String, String> getBody(LoginRequest loginRequest) {
        return new LinkedMultiValueMap<>(Map.of(
                "grant_type", List.of(authSettings.getGrantType()),
                "client_id", List.of(authSettings.getClientId()),
                "client_secret", List.of(authSettings.getClientSecret()),
                "username", List.of(loginRequest.username()),
                "password", List.of(loginRequest.password()),
                "scope", List.of(authSettings.getScope())
        ));
    }
}
