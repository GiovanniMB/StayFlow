package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta con información del método de pago guardado")
public class PagoTokenResponseDTO {

    @Schema(description = "ID del token guardado", example = "1")
    private Integer idPagoToken;

    @Schema(description = "Método de pago", example = "tarjeta")
    private String metodoPago;

    @Schema(description = "Tipo de tarjeta", example = "Visa")
    private String tipoTarjeta;

    @Schema(description = "Últimos 4 dígitos", example = "4242")
    private String ultimosDigitos;

    @Schema(description = "Nombre del titular", example = "Juan Perez")
    private String nombreTitular;

    @Schema(description = "Fecha de expiración", example = "12/2025")
    private String fechaExpiracion;

    // Getters y Setters
    public Integer getIdPagoToken() { return idPagoToken; }
    public void setIdPagoToken(Integer idPagoToken) { this.idPagoToken = idPagoToken; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getTipoTarjeta() { return tipoTarjeta; }
    public void setTipoTarjeta(String tipoTarjeta) { this.tipoTarjeta = tipoTarjeta; }
    public String getUltimosDigitos() { return ultimosDigitos; }
    public void setUltimosDigitos(String ultimosDigitos) { this.ultimosDigitos = ultimosDigitos; }
    public String getNombreTitular() { return nombreTitular; }
    public void setNombreTitular(String nombreTitular) { this.nombreTitular = nombreTitular; }
    public String getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(String fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
}