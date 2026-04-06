package com.StayFlow.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@MappedSuperclass
public abstract class AuditoriaBase 
{

    @Column(updatable = false)
    private LocalDateTime fechaRegistro;

    private LocalDateTime fechaActualizacion;

    private boolean estaEliminado = false;

    @PrePersist
    protected void onCreate() 
    {
        fechaRegistro = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() 
    {
        fechaActualizacion = LocalDateTime.now();
    }

    
    public LocalDateTime getFechaRegistro() 
    {
    	return fechaRegistro; 
    }
    
    public void setFechaRegistro(LocalDateTime fechaRegistro) 
    { 
    	this.fechaRegistro = fechaRegistro; 
    }

    public LocalDateTime getFechaActualizacion()
    {
    	return fechaActualizacion; 
    }
    
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) 
    {
    	this.fechaActualizacion = fechaActualizacion;
    }

	public boolean isEstaEliminado() 
	{
		return estaEliminado;
	}

	public void setEstaEliminado(boolean estaEliminado) 
	{
		this.estaEliminado = estaEliminado;
	}

   
}