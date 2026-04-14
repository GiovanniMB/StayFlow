package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos de entrada para registrar o actualizar una propiedad")
public class PropiedadRequestDTO {

    @Schema(description = "Nombre comercial", example = "Hotel Paraíso")
    private String nombreComercial;

    @Schema(description = "Teléfono de contacto", example = "5512345678")
    private String telefono;

    @Schema(description = "Indica si se renta por habitaciones", example = "true")
    private boolean seRentaPorHabitaciones;

    @Schema(description = "Descripción de la propiedad", example = "Hermoso hotel céntrico")
    private String descripcion;

    @Schema(description = "Datos de la dirección física")
    private DireccionRequestDTO direccion;
}