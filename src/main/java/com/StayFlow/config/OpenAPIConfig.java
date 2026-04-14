package com.StayFlow.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {
    // Configuración de OpenAPI para la documentación de la API REST
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        // Configuración de la información general de la API, servidores y seguridad
        return new OpenAPI()
            .info(new Info()
                .title("StayFlow API")
                .version("1.0")
                .description("API para el sistema de reservas de habitaciones StayFlow")
                .contact(new Contact()
                    .name("StayFlow Team")
                    .email("stayflow271@gmail.com")))
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Servidor de desarrollo")
            ))
            // 1. Requerimiento global de seguridad
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            // 2. Definición del componente de seguridad (JWT Bearer)
            .components(
                new Components()
                    .addSecuritySchemes(securitySchemeName,
                        new SecurityScheme()
                            .name(securitySchemeName)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            );
    }
}