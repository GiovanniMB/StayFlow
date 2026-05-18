package com.StayFlow.dto.response;

import java.time.LocalDate;

public class BloqueoHabitacionResponseDTO {

    private Integer idBloqueoHabitacion;
    private String nivelBloqueo; // "Propiedad", "Categoría" o "Cuarto"
    private String nombreAfectado; // "Todo el edificio", "Suite Presidencial", "Puerta 101"
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String motivo;
    private Integer idAfectado;

    // Getters y Setters
    public Integer getIdBloqueoHabitacion() { return idBloqueoHabitacion; }
    public void setIdBloqueoHabitacion(Integer idBloqueoHabitacion) { this.idBloqueoHabitacion = idBloqueoHabitacion; }

    public String getNivelBloqueo() { return nivelBloqueo; }
    public void setNivelBloqueo(String nivelBloqueo) { this.nivelBloqueo = nivelBloqueo; }

    public String getNombreAfectado() { return nombreAfectado; }
    public void setNombreAfectado(String nombreAfectado) { this.nombreAfectado = nombreAfectado; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public Integer getIdAfectado() { return idAfectado; }
    public void setIdAfectado(Integer idAfectado) { this.idAfectado = idAfectado; }
}