package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "tipo_tarjeta")
@Schema(description = "Catálogo de tipos de tarjeta bancaria (Visa, Mastercard, American Express, etc.)")
public class TipoTarjeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del tipo de tarjeta", example = "1")
    private Integer idTipoTarjeta;

    @Schema(description = "Nombre del tipo de tarjeta", example = "Visa", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 30)
    private String nombre;

    @Schema(description = "Código interno del tipo de tarjeta", example = "visa", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 10)
    private String codigo;

    // Constructores
    public TipoTarjeta() {}

    public TipoTarjeta(String nombre, String codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
    }

    // Getters y Setters
    public Integer getIdTipoTarjeta() {
        return idTipoTarjeta;
    }

    public void setIdTipoTarjeta(Integer idTipoTarjeta) {
        this.idTipoTarjeta = idTipoTarjeta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}