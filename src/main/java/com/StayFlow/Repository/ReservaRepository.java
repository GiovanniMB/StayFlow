package com.StayFlow.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import com.StayFlow.model.Reserva;
import com.StayFlow.model.Reserva.EstadoReserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    // Busca reservas de una habitación que se crucen con un rango de fechas.
    // Se usa para evitar doble reserva.
    // El estado indicado se excluye, normalmente "cancelada".
    List<Reserva> findByHabitacion_IdHabitacionAndEstadoReservaNotAndFechaEntradaLessThanAndFechaSalidaGreaterThan(
            Integer idHabitacion,
            EstadoReserva estado,
            LocalDate fechaSalida,
            LocalDate fechaEntrada
    );

    // Busca todas las reservas realizadas por un cliente.
    // Se usa para "Mis reservas" e historial del usuario.
    List<Reserva> findByCliente_IdUsuario(Integer idUsuario);

    // Busca todas las reservas asociadas a una habitación específica.
    // Se usa para consultar ocupación o historial por habitación.
    List<Reserva> findByHabitacion_IdHabitacion(Integer idHabitacion);

    // Busca todas las reservas asociadas a una propiedad.
    // Se accede desde Reserva -> Habitacion -> Propiedad.
    @Query("SELECT r FROM Reserva r WHERE r.habitacion.propiedad.idPropiedad = :idPropiedad")
    List<Reserva> findByPropiedadId(@Param("idPropiedad") Integer idPropiedad);

    // Busca todas las reservas de las propiedades de un anfitrión.
    // Se usa para que el propietario pueda ver reservas de sus alojamientos.
    @Query("SELECT r FROM Reserva r WHERE r.habitacion.propiedad.dueno.idUsuario = :idAnfitrion")
    List<Reserva> findByAnfitrionId(@Param("idAnfitrion") Integer idAnfitrion);
}