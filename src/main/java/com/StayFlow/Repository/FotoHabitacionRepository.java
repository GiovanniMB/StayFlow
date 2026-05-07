package com.StayFlow.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StayFlow.model.FotoHabitacion;

@Repository
public interface FotoHabitacionRepository extends JpaRepository<FotoHabitacion, Integer> {
    // Buscar las fotos asociadas a un tipo de habitación específico
    List<FotoHabitacion> findByTipoHabitacionIdTipoHabitacionAndEstaEliminadoFalse(Integer idTipoHabitacion);
    
}