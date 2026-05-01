package com.StayFlow.Repository;

import com.StayFlow.model.Propiedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Propiedad.
 * Maneja las operaciones de base de datos heredando de JpaRepository.
 */
@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Integer> {

    // Devuelve todas las propiedades que NO han sido eliminadas lógicamente
    List<Propiedad> findByEstaEliminadoFalse();

    // Busca una propiedad por su ID, asegurándose de que no esté eliminada
    Optional<Propiedad> findByIdPropiedadAndEstaEliminadoFalse(Integer idPropiedad);
    
    // List<Propiedad> findByDuenoIdUsuarioAndEstaEliminadoFalse(Integer idDueno);

    @Query("SELECT DISTINCT p FROM Propiedad p " +
           "JOIN p.tiposHabitacion th " + 
           "JOIN th.habitaciones h " +    
           "WHERE p.estaEliminado = false " +
           "AND h.estaEliminado = false " +
           "AND NOT EXISTS (" +
           "   SELECT r FROM Reserva r " +
           "   WHERE r.habitacion = h " +
           "   AND r.estadoReserva <> 'cancelada' " +
           "   AND r.fechaEntrada < :fechaSalida " +
           "   AND r.fechaSalida > :fechaEntrada" +
           ")")
    List<Propiedad> findDisponiblesByFechas(
            @Param("fechaEntrada") LocalDate fechaEntrada, 
            @Param("fechaSalida") LocalDate fechaSalida
    );
}