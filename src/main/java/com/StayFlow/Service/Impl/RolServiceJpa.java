package com.StayFlow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.StayFlow.model.Rol;
import com.StayFlow.repository.RolRepository;
import com.StayFlow.service.interfaces.IRolService;

@Service
public class RolServiceJpa implements IRolService 
{
	@Autowired
	private RolRepository rolRepo;
	
	@Override
	public void guardar(Rol rol) 
	{
		rolRepo.save(rol);
	}

	@Override
	public void borrar(int id) 
	{
		rolRepo.deleteById(id);
	}

	@Override
	public List<Rol> buscarTodos() 
	{
		return rolRepo.findAll();
	}

}