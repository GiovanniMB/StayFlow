package com.StayFlow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="direccion")
public class Direccion 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String calle;
	private String numero;
	@ManyToOne
	@JoinColumn(name="idColonia")
	private Colonia colonia;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Colonia getColonia() {
		return colonia;
	}
	public void setColonia(Colonia colonia) {
		this.colonia = colonia;
	}
	@Override
	public String toString() {
		return "Direccion [id=" + id + ", calle=" + calle + ", numero=" + numero + ", colonia=" + colonia + "]";
	}
	
}
