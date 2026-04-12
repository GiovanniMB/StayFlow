package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "colonia")
@Schema(description = "Entidad que representa una colonia, barrio o asentamiento dentro de un municipio")
public class Colonia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la colonia", example = "1")
    private Integer id;

    @Schema(description = "Nombre de la colonia", example = "Centro", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 100)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "idMunicipio", nullable = false)
    @Schema(description = "Municipio al que pertenece esta colonia")
    private Municipio municipio;

    // Constructores
    public Colonia() {}

    public Colonia(String nombre, Municipio municipio) {
        this.nombre = nombre;
        this.municipio = municipio;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }
}