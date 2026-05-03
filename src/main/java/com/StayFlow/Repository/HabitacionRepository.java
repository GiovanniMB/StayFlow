package com.StayFlow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.StayFlow.model.Habitacion;

import jakarta.persistence.LockModeType;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Integer> {

    List<Habitacion> findByTipoHabitacionIdTipoHabitacionAndEstaEliminadoFalse(Integer idTipoHabitacion);

    List<Habitacion> findByPropiedadIdPropiedadAndEstaEliminadoFalse(Integer idPropiedad);

    // Método clave para el borrado lógico en reservas
    Optional<Habitacion> findByIdHabitacionAndEstaEliminadoFalse(Integer idHabitacion);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT h FROM Habitacion h WHERE h.idHabitacion = :idHabitacion AND h.estaEliminado = false")
    Optional<Habitacion> findByIdHabitacionForUpdate(@Param("idHabitacion") Integer idHabitacion);
}