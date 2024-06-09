package com.ecogo.ecomove_web_service.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI learningPlatformOpenApi(){
        var openApi = new OpenAPI();
        openApi
                .info(new Info()
                        .title("EcoMove Web Services - WX52")
                        .description("EcoMove application REST API documentation.")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0")
                                .url("springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("EcoMove application wiki Documentation")
                        .url("eco-move.wiki.github.io/docs"));
        return openApi;
    }
}