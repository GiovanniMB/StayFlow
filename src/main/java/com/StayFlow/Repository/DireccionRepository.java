package com.StayFlow.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StayFlow.model.Direccion;

/*Repositorio para la entidad Direccion*/

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Integer> {
    // Para Dirección, con las operaciones CRUD por defecto 
}