package com.example.Mangxahoi.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class SwaggerConfig{

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info().title("JavaInUse Authentication Service"))
                .addSecurityItem(new SecurityRequirement().addList("JavaInUseSecurityScheme"))
                .components(new Components().addSecuritySchemes("JavaInUseSecurityScheme", new SecurityScheme()
                        .name("JavaInUseSecurityScheme").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));

    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("api-service-v1")
                .packagesToScan("com.example.Mangxahoi.controller")
                .build();
    }

}
