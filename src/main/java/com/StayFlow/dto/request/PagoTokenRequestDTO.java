package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Solicitud para guardar un método de pago (tokenización)")
public class PagoTokenRequestDTO {

    @NotNull(message = "El método de pago es obligatorio")
    @Schema(description = "ID del método de pago", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idMetodoPago;

    @NotNull(message = "El tipo de tarjeta es obligatorio")
    @Schema(description = "ID del tipo de tarjeta", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idTipoTarjeta;

    @NotBlank(message = "El token del gateway es obligatorio")
    @Schema(description = "Token proporcionado por el gateway", example = "pm_123456789", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tokenGateway;

    @NotBlank(message = "Los últimos 4 dígitos son obligatorios")
    @Pattern(regexp = "^\\d{4}$", message = "Deben ser exactamente 4 dígitos")
    @Schema(description = "Últimos 4 dígitos de la tarjeta", example = "4242", requiredMode = Schema.RequiredMode.REQUIRED)
    private String ultimosDigitos;

    @NotBlank(message = "El nombre del titular es obligatorio")
    @Schema(description = "Nombre del titular de la tarjeta", example = "Juan Perez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombreTitular;

    @NotBlank(message = "La fecha de expiración es obligatoria")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{4}$", message = "Formato MM/YYYY requerido")
    @Schema(description = "Fecha de expiración (MM/YYYY)", example = "12/2025", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fechaExpiracion;

    // Getters y Setters
    public Integer getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(Integer idMetodoPago) { this.idMetodoPago = idMetodoPago; }
    public Integer getIdTipoTarjeta() { return idTipoTarjeta; }
    public void setIdTipoTarjeta(Integer idTipoTarjeta) { this.idTipoTarjeta = idTipoTarjeta; }
    public String getTokenGateway() { return tokenGateway; }
    public void setTokenGateway(String tokenGateway) { this.tokenGateway = tokenGateway; }
    public String getUltimosDigitos() { return ultimosDigitos; }
    public void setUltimosDigitos(String ultimosDigitos) { this.ultimosDigitos = ultimosDigitos; }
    public String getNombreTitular() { return nombreTitular; }
    public void setNombreTitular(String nombreTitular) { this.nombreTitular = nombreTitular; }
    public String getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(String fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
}