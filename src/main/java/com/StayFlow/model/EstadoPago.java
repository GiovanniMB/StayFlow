package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "estado_pago")
@Schema(description = "Catálogo de estados posibles para un pago (pendiente, completado, fallido, reembolsado, en_revision)")
public class EstadoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del estado de pago", example = "1")
    private Integer idEstadoPago;

    @Schema(description = "Nombre del estado de pago", example = "completado", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 30)
    private String nombre;

    // Constructores
    public EstadoPago() {}

    public EstadoPago(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public Integer getIdEstadoPago() {
        return idEstadoPago;
    }

    public void setIdEstadoPago(Integer idEstadoPago) {
        this.idEstadoPago = idEstadoPago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}