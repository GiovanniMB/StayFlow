package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "tipo_cama")
@Schema(description = "Catálogo de tipos de cama disponibles en las habitaciones (Individual, Matrimonial, Queen, King, etc.)")
public class TipoCama {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del tipo de cama", example = "1")
    private Integer idTipoCama;

    @Schema(description = "Nombre del tipo de cama", example = "King", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 50)
    private String nombre;

    // Constructores
    public TipoCama() {}

    public TipoCama(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public Integer getIdTipoCama() {
        return idTipoCama;
    }

    public void setIdTipoCama(Integer idTipoCama) {
        this.idTipoCama = idTipoCama;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}