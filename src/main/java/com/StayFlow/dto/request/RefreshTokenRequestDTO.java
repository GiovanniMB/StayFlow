package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para solicitar un nuevo token usando refresh token")
public class RefreshTokenRequestDTO {

    @NotBlank(message = "El refresh token es obligatorio")
    @Schema(description = "Refresh token para obtener un nuevo access token", 
            example = "eyJhbGciOiJIUzI1NiIs...", requiredMode = Schema.RequiredMode.REQUIRED)
    private String refreshToken;

    public RefreshTokenRequestDTO() {}

    public RefreshTokenRequestDTO(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}