package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "resena")
@Schema(description = "Reseñas y calificaciones dejadas por los huéspedes después de su estancia")
public class Resena extends AuditoriaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la reseña", example = "1")
    private Integer idResena;

    @ManyToOne
    @JoinColumn(name = "idReserva", nullable = false)
    @Schema(description = "Reserva a la que pertenece esta reseña")
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    @Schema(description = "Usuario que escribió la reseña")
    private Usuario usuario;

    @Schema(description = "Puntuación del 1 al 5", example = "5", minimum = "1", maximum = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private Integer puntuacion;

    @Schema(description = "Comentario o reseña escrita", example = "Excelente servicio, muy recomendable")
    @Column(columnDefinition = "TEXT")
    private String comentario;

    // Constructores
    public Resena() {}

    public Resena(Reserva reserva, Usuario usuario, Integer puntuacion, String comentario) {
        this.reserva = reserva;
        this.usuario = usuario;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
    }

    // Getters y Setters
    public Integer getIdResena() {
        return idResena;
    }

    public void setIdResena(Integer idResena) {
        this.idResena = idResena;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}