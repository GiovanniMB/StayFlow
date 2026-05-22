package com.StayFlow.security;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Constantes de seguridad para la configuración de JWT y autenticación")
public class SecurityConstants {

    private SecurityConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";

    public static final String[] PUBLIC_URLS = {
            "/api/usuarios/registro",
            "/api/usuarios/login",
            "/api/usuarios/confirmar-email",
            "/api/usuarios/refresh-token",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api-docs/**",
            "/v3/api-docs/**",
            "/api/test/**",
            "/api/propiedades/**",
            "/api/catalogos/**",
            "/ws/**"
            
    };
}