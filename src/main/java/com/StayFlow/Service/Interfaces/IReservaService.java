package com.StayFlow.Service.Interfaces;

import com.StayFlow.dto.request.ReservaRequestDTO;
import com.StayFlow.dto.response.DisponibilidadResponseDTO;
import com.StayFlow.dto.response.ReservaResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface IReservaService {

    // Crear una nueva reserva para una habitación y un cliente.
    ReservaResponseDTO crearReserva(ReservaRequestDTO request);

    // Obtener una reserva específica por su id.
    ReservaResponseDTO obtenerReservaPorId(Integer idReserva);

    // Obtener todas las reservas del cliente autenticado o de un cliente dado.
    List<ReservaResponseDTO> obtenerReservasPorCliente(Integer idCliente);

    // Cancelar una reserva existente.
    ReservaResponseDTO cancelarReserva(Integer idReserva);

    // Consultar disponibilidad de una habitación en un rango de fechas.
    DisponibilidadResponseDTO verificarDisponibilidad(Integer idHabitacion, LocalDate fechaEntrada, LocalDate fechaSalida);
}