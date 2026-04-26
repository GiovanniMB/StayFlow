package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
@Schema(description = "Clase base abstracta que proporciona campos de auditoría para todas las entidades del sistema")
public abstract class AuditoriaBase {

    @Schema(description = "Fecha y hora de creación del registro", example = "2024-01-15T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(updatable = false)
    private LocalDateTime fechaRegistro;

    @Schema(description = "Fecha y hora de la última modificación del registro", example = "2024-01-20T15:45:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaActualizacion;

    @Schema(description = "Indicador de eliminación lógica (soft delete)", example = "false")
    private boolean estaEliminado = false;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    public boolean isEstaEliminado() { return estaEliminado; }
    public void setEstaEliminado(boolean estaEliminado) { this.estaEliminado = estaEliminado; }
}