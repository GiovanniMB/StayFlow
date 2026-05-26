package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "DTO para administrador - Datos de bloqueo/mantenimiento")
public class BloqueoAdminResponseDTO {

    @Schema(description = "ID del bloqueo", example = "1")
    private Integer idBloqueo;

    @Schema(description = "Nivel del bloqueo (PROPIEDAD, CATEGORIA, CUARTO, GLOBAL)", example = "PROPIEDAD")
    private String nivelBloqueo;

    @Schema(description = "Nombre de la propiedad afectada", example = "Hotel Paraíso")
    private String nombrePropiedad;

    @Schema(description = "ID del elemento afectado", example = "5")
    private Integer idAfectado;

    @Schema(description = "Fecha de inicio", example = "2024-06-15")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin", example = "2024-06-20")
    private LocalDate fechaFin;

    @Schema(description = "Motivo del bloqueo", example = "Mantenimiento de alberca")
    private String motivo;

    @Schema(description = "Creado por", example = "admin@stayflow.com")
    private String creadoPor;

    // Constructor vacío
    public BloqueoAdminResponseDTO() {}

    // Getters y Setters
    public Integer getIdBloqueo() { return idBloqueo; }
    public void setIdBloqueo(Integer idBloqueo) { this.idBloqueo = idBloqueo; }

    public String getNivelBloqueo() { return nivelBloqueo; }
    public void setNivelBloqueo(String nivelBloqueo) { this.nivelBloqueo = nivelBloqueo; }

    public String getNombrePropiedad() { return nombrePropiedad; }
    public void setNombrePropiedad(String nombrePropiedad) { this.nombrePropiedad = nombrePropiedad; }

    public Integer getIdAfectado() { return idAfectado; }
    public void setIdAfectado(Integer idAfectado) { this.idAfectado = idAfectado; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }
}