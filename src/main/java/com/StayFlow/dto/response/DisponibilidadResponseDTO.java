package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "DTO de salida para consultar disponibilidad de una habitación")
public class DisponibilidadResponseDTO {

    // Id de la habitación consultada.
    private Integer idHabitacion;

    // Fecha de entrada evaluada.
    private LocalDate fechaEntrada;

    // Fecha de salida evaluada.
    private LocalDate fechaSalida;

    // Indica si la habitación puede reservarse o no.
    private boolean disponible;

    // Mensaje explicativo para frontend o debugging.
    private String mensaje;
}