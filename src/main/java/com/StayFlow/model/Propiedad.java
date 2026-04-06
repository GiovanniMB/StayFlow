package com.StayFlow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "propiedad")
public class Propiedad extends AuditoriaBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPropiedad;
    @ManyToOne
    @JoinColumn(name = "idDueno")
    private Usuario dueno;
    private String nombreComercial;
    @ManyToOne
    @JoinColumn(name = "idDireccion")
    private Direccion direccion;
    private String telefono;
    
	public Integer getIdPropiedad() {
		return idPropiedad;
	}
	public void setIdPropiedad(Integer idPropiedad) {
		this.idPropiedad = idPropiedad;
	}
	public Usuario getDueno() {
		return dueno;
	}
	public void setDueno(Usuario dueno) {
		this.dueno = dueno;
	}
	public String getNombreComercial() {
		return nombreComercial;
	}
	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
	}
	
	public Direccion getDireccion() {
		return direccion;
	}
	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
  
}