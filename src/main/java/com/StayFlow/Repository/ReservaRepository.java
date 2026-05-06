package com.StayFlow.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StayFlow.model.Reserva;
import com.StayFlow.model.Reserva.EstadoReserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    // Buscar reservas activas de una habitación que se crucen con un rango de fechas.
    // Esto se usa para evitar doble reserva sobre la misma habitación.
    // Se excluye un estado, por ejemplo "cancelada", para no contar reservas ya anuladas.
    List<Reserva> findByHabitacion_IdHabitacionAndEstadoReservaNotAndFechaEntradaLessThanAndFechaSalidaGreaterThan(
            Integer idHabitacion,
            EstadoReserva estado,
            LocalDate fechaSalida,
            LocalDate fechaEntrada
    );

    // Buscar todas las reservas realizadas por un cliente.
    // Esto servirá para el endpoint "mis reservas" o historial del usuario.
    List<Reserva> findByCliente_IdUsuario(Integer idUsuario);
}