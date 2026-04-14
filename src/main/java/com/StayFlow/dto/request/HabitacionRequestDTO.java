package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Schema(description = "Datos para dar de alta un cuarto físico") // DTO para solicitudes de creación de una habitación física, incluyendo su número identificador y las camas que contiene
public class HabitacionRequestDTO {

    @NotBlank(message = "El número o identificador de la habitación es obligatorio")
    @Schema(description = "Identificador físico de la habitación", example = "101A")
    private String numeroHabitacion;

    @NotEmpty(message = "La habitación debe tener al menos una cama")
    @Valid
    @Schema(description = "Lista de camas que contiene la habitación")
    private List<CamaRequestDTO> camas;
}