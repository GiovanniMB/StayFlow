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
            .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
            .components(new Components()
                .addSecuritySchemes("BearerAuth", new SecurityScheme()
                    .name("BearerAuth")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("Ingresa el token JWT. Ejemplo: eyJhbGciOiJIUzI1NiIs...")
                )
            );
    }
}