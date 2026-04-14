package com.StayFlow.Repository;

import com.StayFlow.model.TipoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoHabitacionRepository extends JpaRepository<TipoHabitacion, Integer> {

    // Buscar todas las categorías (tipos) de una propiedad específica
    List<TipoHabitacion> findByPropiedadIdPropiedadAndEstaEliminadoFalse(Integer idPropiedad);

    // Buscar un tipo de habitación específico asegurando que no esté eliminado
    Optional<TipoHabitacion> findByIdTipoHabitacionAndEstaEliminadoFalse(Integer idTipoHabitacion);
}