package com.StayFlow.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.StayFlow.model.Reserva;
import com.StayFlow.model.Reserva.EstadoReserva;

import jakarta.transaction.Transactional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    // Busca reservas activas de una habitación que se crucen con un rango de fechas.
    // Esto se usa para evitar doble reserva sobre la misma habitación.
    // Se excluye un estado, por ejemplo "cancelada", para no contar reservas ya anuladas.
    List<Reserva> findByHabitacion_IdHabitacionAndEstadoReservaNotAndFechaEntradaLessThanAndFechaSalidaGreaterThan(
            Integer idHabitacion,
            EstadoReserva estado,
            LocalDate fechaSalida,
            LocalDate fechaEntrada
    );

    // Busca todas las reservas realizadas por un cliente.
    // Esto servirá para el endpoint "mis reservas" o historial del usuario.
    List<Reserva> findByCliente_IdUsuario(Integer idUsuario);

    // Busca todas las reservas asociadas a una habitación específica
    List<Reserva> findByHabitacion_IdHabitacion(Integer idHabitacion);
    // Busca todas las reservas asociadas a una propiedad específica (a través de la relación con habitación)
    List<Reserva> findByHabitacion_TipoHabitacion_Propiedad_IdPropiedad(Integer idPropiedad);
    // Busca reservas por un estado específico (Ej. Solo 'check_in')
    List<Reserva> findByEstadoReserva(EstadoReserva estado);
    // Busca reservas que coincidan con varios estados (Ej. 'confirmada' O 'check_in')
    List<Reserva> findByEstadoReservaIn(List<EstadoReserva> estados);

    @Modifying
    @Transactional
    @Query("UPDATE Reserva r SET r.estadoReserva = 'cancelada' WHERE r.estadoReserva = 'pendiente' AND r.fechaRegistro <= :limiteExpiracion")
    int cancelarReservasExpiradas(@Param("limiteExpiracion") LocalDateTime limiteExpiracion);
}