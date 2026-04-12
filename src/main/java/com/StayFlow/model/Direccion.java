package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "direccion")
@Schema(description = "Entidad que representa una dirección física con coordenadas geográficas")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la dirección", example = "1")
    private Integer id;

    @Schema(description = "Nombre de la calle", example = "Av. Reforma", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private String calle;

    @Schema(description = "Número exterior", example = "123")
    @Column(length = 50)
    private String numero;

    @Schema(description = "Número interior (departamento, oficina, etc.)", example = "2B")
    @Column(length = 50)
    private String numeroInterior;

    @ManyToOne
    @JoinColumn(name = "idColonia", nullable = false)
    @Schema(description = "Colonia a la que pertenece esta dirección")
    private Colonia colonia;

    @Schema(description = "Latitud en grados decimales (WGS84)", example = "19.432608")
    @Column(precision = 10, scale = 8)
    private BigDecimal latitud;

    @Schema(description = "Longitud en grados decimales (WGS84)", example = "-99.133209")
    @Column(precision = 11, scale = 8)
    private BigDecimal longitud;

    // Constructores
    public Direccion() {}

    public Direccion(String calle, String numero, String numeroInterior, Colonia colonia, BigDecimal latitud, BigDecimal longitud) {
        this.calle = calle;
        this.numero = numero;
        this.numeroInterior = numeroInterior;
        this.colonia = colonia;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNumeroInterior() {
        return numeroInterior;
    }

    public void setNumeroInterior(String numeroInterior) {
        this.numeroInterior = numeroInterior;
    }

    public Colonia getColonia() {
        return colonia;
    }

    public void setColonia(Colonia colonia) {
        this.colonia = colonia;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }
}