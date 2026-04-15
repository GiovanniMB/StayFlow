package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Schema(description = "Datos para crear o actualizar una categoría de habitación") // DTO para solicitudes de creación o actualización de tipo de habitación
public class TipoHabitacionRequestDTO {

    @NotBlank(message = "El nombre del tipo es obligatorio")
    @Schema(description = "Nombre de la categoría", example = "Suite Presidencial")
    private String nombreTipo;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser al menos de 1 persona")
    @Schema(description = "Capacidad máxima de personas", example = "4")
    private Integer capacidad;

    @NotNull(message = "El precio base es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    @Schema(description = "Precio base por noche", example = "1500.50")
    private BigDecimal precioBaseNoche;

    @NotNull(message = "Debe indicar si tiene baño privado")
    @Schema(description = "Indica si tiene baño privado", example = "true")
    private Boolean tieneBanoPrivado;

    @Schema(description = "Lista de IDs de servicios (amenities) de esta habitación", example = "[4, 5]")
    private List<Integer> idServicios;
}