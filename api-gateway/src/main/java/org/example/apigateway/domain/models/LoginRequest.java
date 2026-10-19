package org.example.apigateway.domain.models;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public record LoginRequest(String username, String password) {

    public LoginRequest {
        Assert.isTrue(StringUtils.isNotBlank(username), "Username must not be empty");
        Assert.isTrue(StringUtils.isNotBlank(password), "Password must not be empty");
    }
}
