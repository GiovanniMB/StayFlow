package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "rol")
@Schema(description = "Entidad que representa los roles/perfiles del sistema (arrendador, arrendatario, administrador, etc.)")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del rol", example = "1")
    private Integer idRol;

    @Schema(description = "Nombre del rol", example = "ARRENDADOR", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 30)
    private String nombreRol;

    @Schema(description = "Estado de eliminación lógica", example = "false")
    private boolean estaEliminado = false;

    // Constructores
    public Rol() {}

    public Rol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    // Getters y Setters
    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public boolean isEstaEliminado() {
        return estaEliminado;
    }

    public void setEstaEliminado(boolean estaEliminado) {
        this.estaEliminado = estaEliminado;
    }
}