package com.StayFlow.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PrecioTemporadaRequestDTO {

    @NotNull(message = "El ID del tipo de habitación es obligatorio")
    private Integer idTipoHabitacion;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @FutureOrPresent(message = "La fecha de fin no puede ser en el pasado")
    private LocalDate fechaFin;

    @NotNull(message = "El precio especial es obligatorio")
    @Min(value = 1, message = "El precio debe ser mayor a 0")
    private BigDecimal precioEspecial;

    public PrecioTemporadaRequestDTO() {}

    // Getters y Setters
    public Integer getIdTipoHabitacion() { return idTipoHabitacion; }
    public void setIdTipoHabitacion(Integer idTipoHabitacion) { this.idTipoHabitacion = idTipoHabitacion; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public BigDecimal getPrecioEspecial() { return precioEspecial; }
    public void setPrecioEspecial(BigDecimal precioEspecial) { this.precioEspecial = precioEspecial; }
}