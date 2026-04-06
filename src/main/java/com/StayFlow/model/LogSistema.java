package com.StayFlow.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "logSistema")
public class LogSistema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLogSistema;
    private String tablaAfectada;
    private Integer idRegistroAfectado;
    @Enumerated(EnumType.STRING)
    private Accion accion;
    @ManyToOne
    @JoinColumn(name = "idUsuarioAccion")
    private Usuario usuarioAccion;
    private LocalDateTime fechaEvento = LocalDateTime.now();

    public enum Accion { INSERT, UPDATE, DELETE_LOGICO }

	public Integer getIdLogSistema() {
		return idLogSistema;
	}

	public void setIdLogSistema(Integer idLogSistema) {
		this.idLogSistema = idLogSistema;
	}

	public String getTablaAfectada() {
		return tablaAfectada;
	}

	public void setTablaAfectada(String tablaAfectada) {
		this.tablaAfectada = tablaAfectada;
	}

	public Integer getIdRegistroAfectado() {
		return idRegistroAfectado;
	}

	public void setIdRegistroAfectado(Integer idRegistroAfectado) {
		this.idRegistroAfectado = idRegistroAfectado;
	}

	public Accion getAccion() {
		return accion;
	}

	public void setAccion(Accion accion) {
		this.accion = accion;
	}

	public Usuario getUsuarioAccion() {
		return usuarioAccion;
	}

	public void setUsuarioAccion(Usuario usuarioAccion) {
		this.usuarioAccion = usuarioAccion;
	}

	public LocalDateTime getFechaEvento() {
		return fechaEvento;
	}

	public void setFechaEvento(LocalDateTime fechaEvento) {
		this.fechaEvento = fechaEvento;
	}
   
}
