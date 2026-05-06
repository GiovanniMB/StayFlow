package com.StayFlow.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PrecioTemporadaResponseDTO {

    private Integer idPrecioTemporada;
    private Integer idTipoHabitacion;
    private String nombreTipoHabitacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal precioEspecial;

    public PrecioTemporadaResponseDTO() {}

    // Getters y Setters
    public Integer getIdPrecioTemporada() { return idPrecioTemporada; }
    public void setIdPrecioTemporada(Integer idPrecioTemporada) { this.idPrecioTemporada = idPrecioTemporada; }

    public Integer getIdTipoHabitacion() { return idTipoHabitacion; }
    public void setIdTipoHabitacion(Integer idTipoHabitacion) { this.idTipoHabitacion = idTipoHabitacion; }

    public String getNombreTipoHabitacion() { return nombreTipoHabitacion; }
    public void setNombreTipoHabitacion(String nombreTipoHabitacion) { this.nombreTipoHabitacion = nombreTipoHabitacion; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public BigDecimal getPrecioEspecial() { return precioEspecial; }
    public void setPrecioEspecial(BigDecimal precioEspecial) { this.precioEspecial = precioEspecial; }
}