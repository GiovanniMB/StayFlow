package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "servicio")
@Schema(description = "Catálogo de servicios que pueden ofrecer las propiedades o tipos de habitación (Wifi, estacionamiento, piscina, etc.)")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del servicio", example = "1")
    private Integer idServicio;

    @Schema(description = "Nombre del servicio", example = "Wifi de alta velocidad", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 50)
    private String nombreServicio;

    @Schema(description = "Estado de eliminación lógica", example = "false")
    private boolean estaEliminado = false;

    // Constructores
    public Servicio() {}

    public Servicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    // Getters y Setters
    public Integer getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Integer idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public boolean isEstaEliminado() {
        return estaEliminado;
    }

    public void setEstaEliminado(boolean estaEliminado) {
        this.estaEliminado = estaEliminado;
    }
}