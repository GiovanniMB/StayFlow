package com.StayFlow.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipoHabitacion")
public class TipoHabitacion extends AuditoriaBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTipoHabitacion;
    @ManyToOne
    @JoinColumn(name = "idPropiedad")
    private Propiedad propiedad;
    private String nombreTipo;
    private Integer capacidad;
    private BigDecimal precioBaseNoche;

    @ManyToMany
    @JoinTable(
        name = "tipoHabitacionServicio",
        joinColumns = @JoinColumn(name = "idTipoHabitacion"),
        inverseJoinColumns = @JoinColumn(name = "idServicio")
    )
    private List<Servicio> servicios;

	public Integer getIdTipoHabitacion() {
		return idTipoHabitacion;
	}

	public void setIdTipoHabitacion(Integer idTipoHabitacion) {
		this.idTipoHabitacion = idTipoHabitacion;
	}

	public Propiedad getPropiedad() {
		return propiedad;
	}

	public void setPropiedad(Propiedad propiedad) {
		this.propiedad = propiedad;
	}

	public String getNombreTipo() {
		return nombreTipo;
	}

	public void setNombreTipo(String nombreTipo) {
		this.nombreTipo = nombreTipo;
	}

	public Integer getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(Integer capacidad) {
		this.capacidad = capacidad;
	}

	public BigDecimal getPrecioBaseNoche() {
		return precioBaseNoche;
	}

	public void setPrecioBaseNoche(BigDecimal precioBaseNoche) {
		this.precioBaseNoche = precioBaseNoche;
	}

	public List<Servicio> getServicios() {
		return servicios;
	}

	public void setServicios(List<Servicio> servicios) {
		this.servicios = servicios;
	}
}