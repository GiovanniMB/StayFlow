package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta con información de un estado de pago del catálogo")
public class EstadoPagoResponseDTO {

    @Schema(description = "ID del estado de pago", example = "1")
    private Integer idEstadoPago;

    @Schema(description = "Nombre del estado de pago", example = "completado")
    private String nombre;

    // Getters y Setters
    public Integer getIdEstadoPago() { return idEstadoPago; }
    public void setIdEstadoPago(Integer idEstadoPago) { this.idEstadoPago = idEstadoPago; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}