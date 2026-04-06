package com.StayFlow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.StayFlow.Repository.RolRepository;
import com.StayFlow.model.Rol;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController 
{
	@Autowired
	private RolRepository repoRol;
	
	@GetMapping("/rol")
	public List<Rol> buscarTodos()
	{
		return repoRol.findAll();
	}
}
