package org.example.moviesapi.infrastructure.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String home(Model model) {
        model.addAttribute("swaggerUiPath", swaggerUiPath);
        model.addAttribute("apiDocsPath", apiDocsPath);
        model.addAttribute("swaggerEnabled", swaggerEnabled);
        return "index";
    }
}
