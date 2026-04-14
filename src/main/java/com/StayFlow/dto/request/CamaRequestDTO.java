package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Define la cantidad y tipo de cama a agregar en una habitación") // DTO para solicitudes de agregar camas a una habitación, especificando el tipo de cama y la cantidad
public class CamaRequestDTO {

    @NotNull(message = "El ID del tipo de cama es obligatorio")
    @Schema(description = "ID del tipo de cama (ej. 1 para Individual, 2 para King)", example = "1")
    private Integer idTipoCama;

    @NotNull(message = "La cantidad de camas es obligatoria")
    @Min(value = 1, message = "Debe agregar al menos 1 cama de este tipo")
    @Schema(description = "Número de camas de este tipo", example = "2")
    private Integer cantidad;
}