package com.StayFlow.repository;

import com.StayFlow.model.FotoHabitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FotoHabitacionRepository extends JpaRepository<FotoHabitacion, Integer> {
    // Buscar las fotos asociadas a un tipo de habitación específico
    List<FotoHabitacion> findByTipoHabitacionIdTipoHabitacionAndEstaEliminadoFalse(Integer idTipoHabitacion);
    
}