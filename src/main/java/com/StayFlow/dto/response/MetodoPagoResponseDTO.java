package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta con información de un método de pago del catálogo")
public class MetodoPagoResponseDTO {

    @Schema(description = "ID del método de pago", example = "1")
    private Integer idMetodoPago;

    @Schema(description = "Nombre del método de pago", example = "tarjeta")
    private String nombre;

    // Getters y Setters
    public Integer getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(Integer idMetodoPago) { this.idMetodoPago = idMetodoPago; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}