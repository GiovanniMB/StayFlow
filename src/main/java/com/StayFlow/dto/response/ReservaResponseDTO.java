package com.StayFlow.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReservaResponseDTO {

    private Integer idReserva;
    private Integer idHabitacion;
    private String numeroHabitacion;
    private Integer idCliente;
    private String nombreCliente;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private BigDecimal montoTotal;
    private String estadoReserva;

    // Constructor vacío
    public ReservaResponseDTO() {
    }

    // Constructor completo
    public ReservaResponseDTO(Integer idReserva, Integer idHabitacion, String numeroHabitacion, Integer idCliente, String nombreCliente, LocalDate fechaEntrada, LocalDate fechaSalida, BigDecimal montoTotal, String estadoReserva) {
        this.idReserva = idReserva;
        this.idHabitacion = idHabitacion;
        this.numeroHabitacion = numeroHabitacion;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.montoTotal = montoTotal;
        this.estadoReserva = estadoReserva;
    }

    // Genera todos los Getters y Setters aquí para las 9 propiedades...
    public Integer getIdReserva() { return idReserva; }
    public void setIdReserva(Integer idReserva) { this.idReserva = idReserva; }
    // ... haz lo mismo para el resto
}