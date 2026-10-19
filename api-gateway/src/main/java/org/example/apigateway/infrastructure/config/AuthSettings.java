package org.example.apigateway.infrastructure.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.auth")
@Getter
@Setter
@NoArgsConstructor
public class AuthSettings {
    String clientId;
    String clientSecret;
    String tokenUrl;
    String grantType;
    String scope;
}
