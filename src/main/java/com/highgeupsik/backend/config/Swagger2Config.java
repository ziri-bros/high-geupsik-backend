package com.highgeupsik.backend.config;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class Swagger2Config {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
            .securityContexts(singletonList(securityContext()))
            .securitySchemes(singletonList(apiKey()))
            .produces(singleton("application/json"))
            .consumes(singleton("application/json"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("하이급식 API 문서")
            .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(securityReference())
            .operationSelector(o -> true)
            .build();
    }

    List<SecurityReference> securityReference() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "전부 접근 가능");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return singletonList(new SecurityReference("JWT", authorizationScopes));
    }
}