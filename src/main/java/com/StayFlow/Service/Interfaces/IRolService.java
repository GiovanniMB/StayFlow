package com.StayFlow.Service.Interfaces;

import java.util.List;

import com.StayFlow.model.Rol;

public interface IRolService 
{
	void guardar(Rol rol);
	void borrar(int id);
	List<Rol> buscarTodos();
}