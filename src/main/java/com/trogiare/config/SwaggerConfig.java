package com.trogiare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String DEFAULT_INCLUDE_PATTERN = "/api/v1/.*";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).pathMapping("/")
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select().apis(RequestHandlerSelectors.basePackage("com.trogiare.controller"))
                .paths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN)).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("Trogiare api", "The Trogiare REST API.", "API v1.0.0", "Terms of service",
                new Contact("Trogiare", "tung24112000@gmail.com",
                        "tung24112000@gmail.com"),
                "License of API", "API license URL", Collections.emptyList());
    }

    private ApiKey apiKey() {
        return new ApiKey("ACCESS-TOKEN", AUTHORIZATION_HEADER, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN)).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[] {
                new AuthorizationScope("global", "accessEverything") };
        List<SecurityReference> refs = new ArrayList<>();
        refs.add(new SecurityReference("ACCESS-TOKEN", authorizationScopes));
        return refs;
    }
}
