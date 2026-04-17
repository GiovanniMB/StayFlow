package com.StayFlow.Repository;

import com.StayFlow.model.PrecioTemporada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrecioTemporadaRepository extends JpaRepository<PrecioTemporada, Integer> {

    // Buscar precios especiales activos para un tipo de habitación en una fecha dada.
    // Se filtra por estaEliminadoFalse para ignorar precios desactivados lógicamente.
    // Más adelante este método se puede usar para cálculo simple o cálculo por día.
    List<PrecioTemporada> findByTipoHabitacion_IdTipoHabitacionAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqualAndEstaEliminadoFalse(
            Integer idTipoHabitacion,
            LocalDate fecha,
            LocalDate fecha2
    );
}