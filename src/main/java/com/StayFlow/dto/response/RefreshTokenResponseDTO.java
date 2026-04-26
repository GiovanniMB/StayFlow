package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para la respuesta de renovación de token")
public class RefreshTokenResponseDTO {

    @Schema(description = "Nuevo token JWT de acceso", example = "eyJhbGciOiJIUzI1NiIs...")
    private String token;

    @Schema(description = "Nuevo refresh token", example = "eyJhbGciOiJIUzI1NiIs...")
    private String refreshToken;

    public RefreshTokenResponseDTO() {}

    public RefreshTokenResponseDTO(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}