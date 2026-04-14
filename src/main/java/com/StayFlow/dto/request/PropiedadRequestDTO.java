package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
// DTO para recibir los datos de una propiedad en las solicitudes de creación o actualización. Incluye validaciones para asegurar que se proporcionen los datos necesarios y que tengan el formato correcto. Además, incluye un objeto anidado para la dirección, que también se valida.
@Getter
@Setter
@Schema(description = "Datos de entrada para registrar o actualizar una propiedad") // Descripción general del DTO para Swagger
public class PropiedadRequestDTO {

    @NotBlank(message = "El nombre comercial es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre comercial debe tener entre 3 y 100 caracteres")
    @Schema(description = "Nombre comercial", example = "Hotel Paraíso")
    private String nombreComercial;

    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    @Schema(description = "Teléfono de contacto", example = "5512345678")
    private String telefono;

    @NotNull(message = "Debe indicar si se renta por habitaciones (true o false)")
    @Schema(description = "Indica si se renta por habitaciones", example = "true")
    private Boolean seRentaPorHabitaciones;

    @Schema(description = "Descripción de la propiedad", example = "Hermoso hotel céntrico")
    private String descripcion;

    @NotNull(message = "La dirección de la propiedad es obligatoria")
    @Valid // Esta anotación le dice a Spring que valide también el objeto hijo (DireccionRequestDTO)
    @Schema(description = "Datos de la dirección física")
    private DireccionRequestDTO direccion;

    // Para los servicios, recibe una lista de IDs que corresponden a los servicios que ofrece la propiedad.
    @Schema(description = "Lista de IDs de los servicios (amenities) que ofrece la propiedad", example = "[1, 2, 3]")
    private List<Integer> idServicios;
}