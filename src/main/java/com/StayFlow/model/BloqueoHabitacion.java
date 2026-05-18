package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "bloqueohabitacion")
@Schema(description = "Registra periodos donde una propiedad, categoría o habitación física no está disponible (mantenimiento, reparaciones, etc.)")
public class BloqueoHabitacion extends AuditoriaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del bloqueo", example = "1")
    private Integer idBloqueoHabitacion;

    // Niveles de bloqueo (Todos pueden ser nulos, pero al menos uno debe tener valor al guardar)
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPropiedad", nullable = true)
    @Schema(description = "Propiedad entera que se encuentra bloqueada")
    private Propiedad propiedad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTipoHabitacion", nullable = true)
    @Schema(description = "Categoría entera que se encuentra bloqueada (Ej. Todas las Suites)")
    private TipoHabitacion tipoHabitacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idHabitacion", nullable = true)
    @Schema(description = "Habitación física específica que se encuentra bloqueada")
    private Habitacion habitacion;

    

    //Datos del bloqueo

    @Schema(description = "Fecha de inicio del bloqueo", example = "2024-12-20", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin del bloqueo", example = "2024-12-25", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private LocalDate fechaFin;

    @Schema(description = "Motivo del bloqueo", example = "Mantenimiento programado - reparación de tubería")
    private String motivo;

    //Constructores
    public BloqueoHabitacion() {}

    //Getters y Setters
    public Integer getIdBloqueoHabitacion() { return idBloqueoHabitacion; }
    public void setIdBloqueoHabitacion(Integer idBloqueoHabitacion) { this.idBloqueoHabitacion = idBloqueoHabitacion; }

    public Propiedad getPropiedad() { return propiedad; }
    public void setPropiedad(Propiedad propiedad) { this.propiedad = propiedad; }

    public TipoHabitacion getTipoHabitacion() { return tipoHabitacion; }
    public void setTipoHabitacion(TipoHabitacion tipoHabitacion) { this.tipoHabitacion = tipoHabitacion; }

    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}