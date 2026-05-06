package com.StayFlow.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
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

    @NotNull
    @Min(value = 1, message = "Debe haber al menos 1 huésped")
    private Integer cantidadHuespedes;

    public ReservaRequestDTO() {}

    public ReservaRequestDTO(Integer idHabitacion, Integer idCliente, LocalDate fechaEntrada, LocalDate fechaSalida, Integer cantidadHuespedes) {
        this.idHabitacion = idHabitacion;
        this.idCliente = idCliente;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.cantidadHuespedes = cantidadHuespedes;
    }

    public Integer getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(Integer idHabitacion) { this.idHabitacion = idHabitacion; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(LocalDate fechaEntrada) { this.fechaEntrada = fechaEntrada; }

    public LocalDate getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDate fechaSalida) { this.fechaSalida = fechaSalida; }

    public Integer getCantidadHuespedes() { return cantidadHuespedes; }
    public void setCantidadHuespedes(Integer cantidadHuespedes) { this.cantidadHuespedes = cantidadHuespedes; }
}