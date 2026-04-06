package com.StayFlow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "resena")
public class Resena extends AuditoriaBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idResena;
    @ManyToOne
    @JoinColumn(name = "idReserva")
    private Reserva reserva;
    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;
    private Integer puntuacion;
    private String comentario;
    
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
