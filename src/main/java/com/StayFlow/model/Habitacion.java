package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "habitacion")
@Schema(description = "Representa una habitación física específica dentro de una propiedad")
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

    @Schema(description = "Número identificador de la habitación", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 10)
    private String numeroHabitacion;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Estado actual de disponibilidad", example = "disponible")
    @Column(columnDefinition = "enum('disponible','mantenimiento','ocupada') default 'disponible'")
    private EstadoHabitacion estado = EstadoHabitacion.disponible;

    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Schema(description = "Lista de camas en esta habitación con sus cantidades")
    private List<HabitacionTipoCama> habitacionTipoCamas = new ArrayList<>();

    public enum EstadoHabitacion {
        disponible, mantenimiento, ocupada
    }

    // Constructores
    public Habitacion() {}

    // Getters y Setters
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
    
    public List<HabitacionTipoCama> getHabitacionTipoCamas() { return habitacionTipoCamas; }
    public void setHabitacionTipoCamas(List<HabitacionTipoCama> habitacionTipoCamas) { 
        this.habitacionTipoCamas = habitacionTipoCamas; 
    }

    // Métodos de utilidad
    public void addCama(TipoCama tipoCama, int cantidad) {
        HabitacionTipoCama htc = new HabitacionTipoCama(this, tipoCama, cantidad);
        habitacionTipoCamas.add(htc);
    }

    public List<String> getNombresCamas() {
        if (habitacionTipoCamas.isEmpty()) return new ArrayList<>();
        return habitacionTipoCamas.stream()
            .map(hc -> hc.getCantidad() + "x " + hc.getTipoCama().getNombre())
            .collect(Collectors.toList());
    }

    public int getTotalCamas() {
        return habitacionTipoCamas.stream()
            .mapToInt(HabitacionTipoCama::getCantidad)
            .sum();
    }
}