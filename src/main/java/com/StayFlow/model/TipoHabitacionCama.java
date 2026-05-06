package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "tipohabitacion_cama")
@Schema(description = "Entidad puente que relaciona Categorías con Tipos de Cama")
public class TipoHabitacionCama {

    @EmbeddedId
    private TipoHabitacionCamaId id;

    @ManyToOne
    @MapsId("idTipoHabitacion")
    @JoinColumn(name = "idTipoHabitacion", nullable = false)
    @Schema(description = "Categoría que contiene las camas")
    private TipoHabitacion tipoHabitacion;

    @ManyToOne
    @MapsId("idTipoCama")
    @JoinColumn(name = "idTipoCama", nullable = false)
    @Schema(description = "Tipo de cama (Individual, Matrimonial, Queen, King, etc.)")
    private TipoCama tipoCama;

    @Schema(description = "Cantidad de camas de este tipo en la categoría", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private Integer cantidad = 1;

    public TipoHabitacionCama() {}

    public TipoHabitacionCama(TipoHabitacion tipoHabitacion, TipoCama tipoCama, Integer cantidad) {
        this.id = new TipoHabitacionCamaId(tipoHabitacion.getIdTipoHabitacion(), tipoCama.getIdTipoCama());
        this.tipoHabitacion = tipoHabitacion;
        this.tipoCama = tipoCama;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public TipoHabitacionCamaId getId() { return id; }
    public void setId(TipoHabitacionCamaId id) { this.id = id; }

    public TipoHabitacion getTipoHabitacion() { return tipoHabitacion; }
    public void setTipoHabitacion(TipoHabitacion tipoHabitacion) { this.tipoHabitacion = tipoHabitacion; }

    public TipoCama getTipoCama() { return tipoCama; }
    public void setTipoCama(TipoCama tipoCama) { this.tipoCama = tipoCama; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}