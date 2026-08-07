package com.parivar.census.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI censusOpenAPI() {

        final String securitySchemeName = "Bearer Authentication";

        return new OpenAPI()

                .info(new Info()
                        .title("Parivar Census Management API")
                        .version("1.0")
                        .description("REST APIs for Parivar Census Management System")
                        .contact(new Contact()
                                .name("Subhash Nayak")
                                .email("admin@parivar.com")))

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName))

                .schemaRequirement(
                        securitySchemeName,
                        new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"));
    }
}