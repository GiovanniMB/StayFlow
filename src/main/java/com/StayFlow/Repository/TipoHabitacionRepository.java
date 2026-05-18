package com.StayFlow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StayFlow.model.TipoHabitacion;

@Repository
public interface TipoHabitacionRepository extends JpaRepository<TipoHabitacion, Integer> {

    // Buscar todas las categorías (tipos) de una propiedad específica
    List<TipoHabitacion> findByPropiedadIdPropiedadAndEstaEliminadoFalse(Integer idPropiedad);

    // Buscar un tipo de habitación específico asegurando que no esté eliminado
    Optional<TipoHabitacion> findByIdTipoHabitacionAndEstaEliminadoFalse(Integer idTipoHabitacion);
}