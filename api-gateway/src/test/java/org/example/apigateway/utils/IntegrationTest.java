package org.example.apigateway.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import tools.jackson.databind.ObjectMapper;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWireMock(@ConfigureWireMock(port = 0))
public abstract class IntegrationTest {

    @Autowired
    protected ObjectMapper objectMapper;

    public void stubPostUrl(String url, Object value, HttpStatus httpStatus) {
        stubFor(post(urlPathMatching(url))
                .willReturn(aResponse()
                        .withStatus(httpStatus.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(value))));
    }
}
