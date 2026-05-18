package com.StayFlow.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.StayFlow.model.BloqueoHabitacion;

@Repository
public interface BloqueoHabitacionRepository extends JpaRepository<BloqueoHabitacion, Integer> {

    // 1. Escudo para las reservas
    @Query("SELECT b FROM BloqueoHabitacion b " +
           "LEFT JOIN b.habitacion h " +
           "LEFT JOIN b.tipoHabitacion th " +
           "LEFT JOIN b.propiedad p " +
           "WHERE (h.idHabitacion = :idHabitacion OR " +
           " th.idTipoHabitacion = :idTipoHabitacion OR " +
           " p.idPropiedad = :idPropiedad) " +
           "AND b.fechaInicio <= :fechaSalida AND b.fechaFin >= :fechaEntrada")
    List<BloqueoHabitacion> findBloqueosConflictivos(
            @Param("idHabitacion") Integer idHabitacion,
            @Param("idTipoHabitacion") Integer idTipoHabitacion,
            @Param("idPropiedad") Integer idPropiedad,
            @Param("fechaEntrada") LocalDate fechaEntrada,
            @Param("fechaSalida") LocalDate fechaSalida);

    // 2. 🔥 LA CURA A LA AMNESIA: LEFT JOINS EXPLÍCITOS PARA EVITAR QUE SE BORREN FILAS 🔥
    @Query("SELECT b FROM BloqueoHabitacion b " +
           "LEFT JOIN b.propiedad p " +
           "LEFT JOIN b.tipoHabitacion th " +
           "LEFT JOIN th.propiedad thP " +
           "LEFT JOIN b.habitacion h " +
           "LEFT JOIN h.tipoHabitacion hTh " +
           "LEFT JOIN hTh.propiedad hThP " +
           "WHERE (p.idPropiedad = :idPropiedad OR " +
           " thP.idPropiedad = :idPropiedad OR " +
           " hThP.idPropiedad = :idPropiedad) " +
           "AND b.fechaFin >= CURRENT_DATE")
    List<BloqueoHabitacion> findBloqueosActivosPorPropiedad(@Param("idPropiedad") Integer idPropiedad);
}