package com.StayFlow.Service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.StayFlow.model.Rol;
import com.StayFlow.Repository.RolRepository;
import com.StayFlow.Service.Interfaces.IRolService;

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