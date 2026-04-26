package com.StayFlow.repository;

import com.StayFlow.model.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Integer> {

    // Buscar todas las habitaciones físicas de una categoría específica (Ej. todas las Suites)
    List<Habitacion> findByTipoHabitacionIdTipoHabitacionAndEstaEliminadoFalse(Integer idTipoHabitacion);

    // Buscar todas las habitaciones físicas de una propiedad completa
    List<Habitacion> findByPropiedadIdPropiedadAndEstaEliminadoFalse(Integer idPropiedad);

    // Buscar una habitación específica por su ID
    Optional<Habitacion> findByIdHabitacionAndEstaEliminadoFalse(Integer idHabitacion);
}