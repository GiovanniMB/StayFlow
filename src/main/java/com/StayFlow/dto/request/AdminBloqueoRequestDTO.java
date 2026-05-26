package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "DTO para crear bloqueos desde el panel de administración")
public class AdminBloqueoRequestDTO {

    @Schema(description = "ID de la propiedad (si aplica)", example = "5")
    private Integer idPropiedad;

    @Schema(description = "ID del tipo de habitación (si aplica)", example = "3")
    private Integer idTipoHabitacion;

    @Schema(description = "ID de la habitación física (si aplica)", example = "10")
    private Integer idHabitacion;

    @Schema(description = "Fecha de inicio", example = "2024-06-15")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin", example = "2024-06-20")
    private LocalDate fechaFin;

    @Schema(description = "Motivo del bloqueo", example = "Mantenimiento programado")
    private String motivo;

    // Getters y Setters
    public Integer getIdPropiedad() { return idPropiedad; }
    public void setIdPropiedad(Integer idPropiedad) { this.idPropiedad = idPropiedad; }

    public Integer getIdTipoHabitacion() { return idTipoHabitacion; }
    public void setIdTipoHabitacion(Integer idTipoHabitacion) { this.idTipoHabitacion = idTipoHabitacion; }

    public Integer getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(Integer idHabitacion) { this.idHabitacion = idHabitacion; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}