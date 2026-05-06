package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para la respuesta de inicio de sesión")
public class LoginResponseDTO {

    @Schema(description = "Token JWT de acceso", example = "eyJhbGciOiJIUzI1NiIs...")
    private String token;

    @Schema(description = "Refresh token", example = "eyJhbGciOiJIUzI1NiIs...")
    private String refreshToken;

    @Schema(description = "Email del usuario", example = "carlos@mail.com")
    private String email;

    @Schema(description = "Nombre completo del usuario", example = "Carlos García López")
    private String nombreCompleto;

    @Schema(description = "Rol del usuario", example = "arrendatario")
    private String rol;

    public LoginResponseDTO() {}


    public LoginResponseDTO(String token, String refreshToken, String email, String nombreCompleto, String rol) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.email = email;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }


    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}