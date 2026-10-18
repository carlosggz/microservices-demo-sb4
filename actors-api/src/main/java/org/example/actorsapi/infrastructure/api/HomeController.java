package org.example.actorsapi.infrastructure.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;

@Controller
@RequestMapping("/")
public class HomeController {

    @Value("${springdoc.swagger-ui.path:/swagger-ui}")
    private String swaggerUiPath;

    @Value("${springdoc.api-docs.path:/api-docs}")
    private String apiDocsPath;

    @Value("${app.swagger.enabled:false}")
    private boolean swaggerEnabled;

    @GetMapping
    public Rendering home() {
        return Rendering.view("index")
                .modelAttribute("swaggerUiPath", swaggerUiPath)
                .modelAttribute("apiDocsPath", apiDocsPath)
                .modelAttribute("swaggerEnabled", swaggerEnabled)
                .build();
    }
}
