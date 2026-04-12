package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "metodo_pago")
@Schema(description = "Catálogo de métodos de pago disponibles en el sistema (tarjeta, transferencia, efectivo, paypal, stripe, mercado_pago, etc.)")
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del método de pago", example = "1")
    private Integer idMetodoPago;

    @Schema(description = "Nombre del método de pago", example = "tarjeta", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 50)
    private String nombre;

    // Constructores
    public MetodoPago() {}

    public MetodoPago(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public Integer getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(Integer idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}