package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorito")
@Schema(description = "Entidad que registra las propiedades favoritas de los usuarios")
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del favorito", example = "1")
    private Integer idFavorito;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    @Schema(description = "Usuario que marcó la propiedad como favorita")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "idPropiedad", nullable = false)
    @Schema(description = "Propiedad marcada como favorita")
    private Propiedad propiedad;

    @Schema(description = "Fecha y hora en que se marcó como favorito", example = "2024-12-20T10:30:00")
    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Constructores
    public Favorito() {}

    public Favorito(Usuario usuario, Propiedad propiedad) {
        this.usuario = usuario;
        this.propiedad = propiedad;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getIdFavorito() {
        return idFavorito;
    }

    public void setIdFavorito(Integer idFavorito) {
        this.idFavorito = idFavorito;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}