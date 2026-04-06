package com.StayFlow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fotoHabitacion")
public class FotoHabitacion extends AuditoriaBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFotoHabitacion;
    @ManyToOne
    @JoinColumn(name = "idTipoHabitacion")
    private TipoHabitacion tipoHabitacion;
    @ManyToOne
    @JoinColumn(name = "idCategoriaFoto")
    private CategoriaFoto categoriaFoto;
    private String urlFoto;
    private boolean esPrincipal;
    
	public Integer getIdFotoHabitacion() {
		return idFotoHabitacion;
	}
	public void setIdFotoHabitacion(Integer idFotoHabitacion) {
		this.idFotoHabitacion = idFotoHabitacion;
	}
	public TipoHabitacion getTipoHabitacion() {
		return tipoHabitacion;
	}
	public void setTipoHabitacion(TipoHabitacion tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}
	public CategoriaFoto getCategoriaFoto() {
		return categoriaFoto;
	}
	public void setCategoriaFoto(CategoriaFoto categoriaFoto) {
		this.categoriaFoto = categoriaFoto;
	}
	public String getUrlFoto() {
		return urlFoto;
	}
	public void setUrlFoto(String urlFoto) {
		this.urlFoto = urlFoto;
	}
	public boolean isEsPrincipal() {
		return esPrincipal;
	}
	public void setEsPrincipal(boolean esPrincipal) {
		this.esPrincipal = esPrincipal;
	}
    
    
}
