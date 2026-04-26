package com.StayFlow.repository;

import com.StayFlow.model.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Integer> {

    List<Habitacion> findByTipoHabitacionIdTipoHabitacionAndEstaEliminadoFalse(Integer idTipoHabitacion);

    List<Habitacion> findByPropiedadIdPropiedadAndEstaEliminadoFalse(Integer idPropiedad);

    // Método clave para el borrado lógico en reservas
    Optional<Habitacion> findByIdHabitacionAndEstaEliminadoFalse(Integer idHabitacion);
}