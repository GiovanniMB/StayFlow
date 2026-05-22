package com.StayFlow.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.StayFlow.model.PrecioTemporada;

@Repository
public interface PrecioTemporadaRepository extends JpaRepository<PrecioTemporada, Integer> {

    // El método que tambien se usa en ReservaServiceImpl
    List<PrecioTemporada> findByTipoHabitacion_IdTipoHabitacionAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqualAndEstaEliminadoFalse(
            Integer idTipoHabitacion, LocalDate fechaFin, LocalDate fechaInicio);

    // Consulta mágica para validar el Solapamiento de Fechas
    @Query("SELECT p FROM PrecioTemporada p WHERE p.tipoHabitacion.idTipoHabitacion = :idTipo AND p.estaEliminado = false AND p.fechaInicio <= :fin AND p.fechaFin >= :inicio AND (:idIgnorar IS NULL OR p.idPrecioTemporada != :idIgnorar)")
    List<PrecioTemporada> encontrarSolapamientos(
            @Param("idTipo") Integer idTipo, 
            @Param("inicio") LocalDate inicio, 
            @Param("fin") LocalDate fin, 
            @Param("idIgnorar") Integer idIgnorar);
            
    // Lista todos los que no están eliminados
    List<PrecioTemporada> findByEstaEliminadoFalse();

    List<PrecioTemporada> findByTipoHabitacion_IdTipoHabitacionInAndEstaEliminadoFalse(List<Integer> idsTipoHabitacion);
}