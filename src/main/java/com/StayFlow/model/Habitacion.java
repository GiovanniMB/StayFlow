package com.StayFlow.model;

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
@Table(name = "habitacion")
public class Habitacion extends AuditoriaBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHabitacion;
    @ManyToOne
    @JoinColumn(name = "idPropiedad")
    private Propiedad propiedad;
    @ManyToOne
    @JoinColumn(name = "idTipoHabitacion")
    private TipoHabitacion tipoHabitacion;
    private String numeroHabitacion;
    @Enumerated(EnumType.STRING)
    private Estado estado;
    public enum Estado { disponible, mantenimiento, ocupada }
    
    
	public Integer getIdHabitacion() {
		return idHabitacion;
	}
	public void setIdHabitacion(Integer idHabitacion) {
		this.idHabitacion = idHabitacion;
	}
	public Propiedad getPropiedad() {
		return propiedad;
	}
	public void setPropiedad(Propiedad propiedad) {
		this.propiedad = propiedad;
	}
	public TipoHabitacion getTipoHabitacion() {
		return tipoHabitacion;
	}
	public void setTipoHabitacion(TipoHabitacion tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}
	public String getNumeroHabitacion() {
		return numeroHabitacion;
	}
	public void setNumeroHabitacion(String numeroHabitacion) {
		this.numeroHabitacion = numeroHabitacion;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
}
