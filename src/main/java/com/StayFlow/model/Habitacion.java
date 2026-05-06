package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "habitacion")
@Schema(description = "Representa una habitación física (ficha de inventario) dentro de una propiedad")
public class Habitacion extends AuditoriaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la habitación", example = "1")
    private Integer idHabitacion;

    @ManyToOne
    @JoinColumn(name = "idPropiedad")
    @Schema(description = "Propiedad a la que pertenece la habitación")
    private Propiedad propiedad;

    @ManyToOne
    @JoinColumn(name = "idTipoHabitacion")
    @Schema(description = "Categoría o tipo de la habitación")
    private TipoHabitacion tipoHabitacion;

    @Schema(description = "Número identificador de la puerta", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 100)
    private String numeroHabitacion;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Estado actual de disponibilidad", example = "disponible")
    @Column(columnDefinition = "enum('disponible','mantenimiento','ocupada') default 'disponible'")
    private EstadoHabitacion estado = EstadoHabitacion.disponible;

    public enum EstadoHabitacion {
        disponible, mantenimiento, ocupada
    }

    public Habitacion() {}

    public Integer getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(Integer idHabitacion) { this.idHabitacion = idHabitacion; }
    
    public Propiedad getPropiedad() { return propiedad; }
    public void setPropiedad(Propiedad propiedad) { this.propiedad = propiedad; }
    
    public TipoHabitacion getTipoHabitacion() { return tipoHabitacion; }
    public void setTipoHabitacion(TipoHabitacion tipoHabitacion) { this.tipoHabitacion = tipoHabitacion; }
    
    public String getNumeroHabitacion() { return numeroHabitacion; }
    public void setNumeroHabitacion(String numeroHabitacion) { this.numeroHabitacion = numeroHabitacion; }
    
    public EstadoHabitacion getEstado() { return estado; }
    public void setEstado(EstadoHabitacion estado) { this.estado = estado; }
}