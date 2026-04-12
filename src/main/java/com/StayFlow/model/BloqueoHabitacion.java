package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "bloqueohabitacion")
@Schema(description = "Registra periodos donde una habitación no está disponible para reserva (mantenimiento, reparaciones, etc.)")
public class BloqueoHabitacion extends AuditoriaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del bloqueo", example = "1")
    private Integer idBloqueoHabitacion;

    @ManyToOne
    @JoinColumn(name = "idHabitacion")
    @Schema(description = "Habitación que se encuentra bloqueada")
    private Habitacion habitacion;

    @Schema(description = "Fecha de inicio del bloqueo", example = "2024-12-20", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin del bloqueo", example = "2024-12-25", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private LocalDate fechaFin;

    @Schema(description = "Motivo del bloqueo", example = "Mantenimiento programado - reparación de aire acondicionado")
    private String motivo;

    // Constructores
    public BloqueoHabitacion() {}

    public BloqueoHabitacion(Habitacion habitacion, LocalDate fechaInicio, LocalDate fechaFin, String motivo) {
        this.habitacion = habitacion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.motivo = motivo;
    }

    // Getters y Setters
    public Integer getIdBloqueoHabitacion() {
        return idBloqueoHabitacion;
    }

    public void setIdBloqueoHabitacion(Integer idBloqueoHabitacion) {
        this.idBloqueoHabitacion = idBloqueoHabitacion;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}