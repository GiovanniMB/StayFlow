package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "habitacion_tipo_cama")
@Schema(description = "Entidad puente que relaciona habitaciones con tipos de cama, permitiendo que una habitación tenga múltiples camas de diferentes tipos")
public class HabitacionTipoCama {

    @EmbeddedId
    @Schema(description = "ID compuesto que combina habitación y tipo de cama")
    private HabitacionTipoCamaId id;

    @ManyToOne
    @MapsId("idHabitacion")
    @JoinColumn(name = "idHabitacion", nullable = false)
    @Schema(description = "Habitación que contiene las camas")
    private Habitacion habitacion;

    @ManyToOne
    @MapsId("idTipoCama")
    @JoinColumn(name = "idTipoCama", nullable = false)
    @Schema(description = "Tipo de cama (Individual, Matrimonial, Queen, King, etc.)")
    private TipoCama tipoCama;

    @Schema(description = "Cantidad de camas de este tipo en la habitación", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private Integer cantidad = 1;

    // Constructores
    public HabitacionTipoCama() {}

    public HabitacionTipoCama(Habitacion habitacion, TipoCama tipoCama, Integer cantidad) {
        this.id = new HabitacionTipoCamaId(habitacion.getIdHabitacion(), tipoCama.getIdTipoCama());
        this.habitacion = habitacion;
        this.tipoCama = tipoCama;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public HabitacionTipoCamaId getId() {
        return id;
    }

    public void setId(HabitacionTipoCamaId id) {
        this.id = id;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public TipoCama getTipoCama() {
        return tipoCama;
    }

    public void setTipoCama(TipoCama tipoCama) {
        this.tipoCama = tipoCama;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}