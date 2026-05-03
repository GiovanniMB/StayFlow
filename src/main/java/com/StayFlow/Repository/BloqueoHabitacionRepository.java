package com.StayFlow.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StayFlow.model.BloqueoHabitacion;

@Repository
public interface BloqueoHabitacionRepository extends JpaRepository<BloqueoHabitacion, Integer> {

    // Buscar bloqueos de una habitación que coincidan con un rango de fechas.
    // Si esta lista regresa elementos, la habitación no debe poder reservarse.
    List<BloqueoHabitacion> findByHabitacion_IdHabitacionAndFechaInicioLessThanAndFechaFinGreaterThan(
            Integer idHabitacion,
            LocalDate fechaSalida,
            LocalDate fechaEntrada
    );
}