package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "DTO de salida con la información de una reserva")
public class ReservaResponseDTO {

    // Identificador único de la reserva.
    private Integer idReserva;

    // Id de la habitación reservada.
    private Integer idHabitacion;

    // Número de habitación para mostrarlo fácilmente en frontend.
    private String numeroHabitacion;

    // Id del cliente que hizo la reserva.
    private Integer idCliente;

    // Nombre completo del cliente para mostrar en respuestas.
    private String nombreCliente;

    // Fecha de entrada registrada en la reserva.
    private LocalDate fechaEntrada;

    // Fecha de salida registrada en la reserva.
    private LocalDate fechaSalida;

    // Monto total calculado por backend.
    private BigDecimal montoTotal;

    // Estado actual de la reserva.
    private String estadoReserva;
}