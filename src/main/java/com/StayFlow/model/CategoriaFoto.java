package com.StayFlow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categoriaFoto")
public class CategoriaFoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCategoriaFoto;
    private String nombreCategoria;
    @Enumerated(EnumType.STRING)
    private TipoMacro tipoMacro;
    private boolean estaEliminado = false;

    public enum TipoMacro { interior, exterior }

	public Integer getIdCategoriaFoto() {
		return idCategoriaFoto;
	}

	public void setIdCategoriaFoto(Integer idCategoriaFoto) {
		this.idCategoriaFoto = idCategoriaFoto;
	}

	public String getNombreCategoria() {
		return nombreCategoria;
	}

	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}

	public TipoMacro getTipoMacro() {
		return tipoMacro;
	}

	public void setTipoMacro(TipoMacro tipoMacro) {
		this.tipoMacro = tipoMacro;
	}

	public boolean isEstaEliminado() {
		return estaEliminado;
	}

	public void setEstaEliminado(boolean estaEliminado) {
		this.estaEliminado = estaEliminado;
	}

}
