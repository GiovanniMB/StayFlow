package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "DTO para solicitar la creación de una reserva")
public class ReservaRequestDTO {

    // Id de la habitación que se desea reservar.
    @NotNull(message = "El id de la habitación es obligatorio")
    @Schema(description = "Id de la habitación a reservar", example = "1")
    private Integer idHabitacion;

    // Id del cliente que realizará la reserva.
    // Más adelante esto puede salir del usuario autenticado por JWT.
    @NotNull(message = "El id del cliente es obligatorio")
    @Schema(description = "Id del cliente que realiza la reserva", example = "5")
    private Integer idCliente;

    // Fecha de entrada solicitada.
    @NotNull(message = "La fecha de entrada es obligatoria")
    @Future(message = "La fecha de entrada debe ser futura")
    @Schema(description = "Fecha de entrada", example = "2026-04-20")
    private LocalDate fechaEntrada;

    // Fecha de salida solicitada.
    @NotNull(message = "La fecha de salida es obligatoria")
    @Future(message = "La fecha de salida debe ser futura")
    @Schema(description = "Fecha de salida", example = "2026-04-25")
    private LocalDate fechaSalida;
}