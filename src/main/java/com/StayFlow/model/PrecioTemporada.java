package com.StayFlow.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "precioTemporada")
public class PrecioTemporada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPrecioTemporada;
    @ManyToOne
    @JoinColumn(name = "idTipoHabitacion")
    private TipoHabitacion tipoHabitacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal precioEspecial;
    private boolean estaEliminado = false;
    
	public Integer getIdPrecioTemporada() {
		return idPrecioTemporada;
	}
	public void setIdPrecioTemporada(Integer idPrecioTemporada) {
		this.idPrecioTemporada = idPrecioTemporada;
	}
	public TipoHabitacion getTipoHabitacion() {
		return tipoHabitacion;
	}
	public void setTipoHabitacion(TipoHabitacion tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}
	public LocalDate getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public LocalDate getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}
	public BigDecimal getPrecioEspecial() {
		return precioEspecial;
	}
	public void setPrecioEspecial(BigDecimal precioEspecial) {
		this.precioEspecial = precioEspecial;
	}
	public boolean isEstaEliminado() {
		return estaEliminado;
	}
	public void setEstaEliminado(boolean estaEliminado) {
		this.estaEliminado = estaEliminado;
	}
    
    
}
