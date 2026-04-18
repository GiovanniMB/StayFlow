package com.StayFlow.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ReservaRequestDTO {

    @NotNull
    private Integer idHabitacion;

    @NotNull
    private Integer idCliente;

    @NotNull
    @Future
    private LocalDate fechaEntrada;

    @NotNull
    @Future
    private LocalDate fechaSalida;

    // Constructor vacío (Nativo)
    public ReservaRequestDTO() {
    }

    // Constructor con parámetros (Nativo)
    public ReservaRequestDTO(Integer idHabitacion, Integer idCliente, LocalDate fechaEntrada, LocalDate fechaSalida) {
        this.idHabitacion = idHabitacion;
        this.idCliente = idCliente;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
    }

    // --- TODOS LOS GETTERS Y SETTERS ---

    public Integer getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(Integer idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }
}