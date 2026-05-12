package com.StayFlow.Service.Interfaces;

import java.time.LocalDate;
import java.util.List;

import com.StayFlow.dto.request.ReservaRequestDTO;
import com.StayFlow.dto.response.DisponibilidadResponseDTO;
import com.StayFlow.dto.response.ReservaResponseDTO;
import com.StayFlow.dto.response.CancelacionReservaResponseDTO;

public interface IReservaService {

    // Métodos
    ReservaResponseDTO crearReserva(ReservaRequestDTO request);
    
    ReservaResponseDTO obtenerReservaPorId(Integer idReserva);
    
    List<ReservaResponseDTO> obtenerReservasPorCliente(Integer idCliente);
    
    ReservaResponseDTO cancelarReserva(Integer idReserva);
    
    DisponibilidadResponseDTO verificarDisponibilidad(Integer idHabitacion, LocalDate fechaEntrada, LocalDate fechaSalida);
    
    ReservaResponseDTO registrarCheckIn(Integer idReserva);
    
    ReservaResponseDTO registrarCheckOut(Integer idReserva);

    List<ReservaResponseDTO> obtenerReservasPorHabitacion(Integer idHabitacion);

    List<ReservaResponseDTO> obtenerReservasPorPropiedad(Integer idPropiedad);

    List<ReservaResponseDTO> obtenerReservasPorAnfitrion(Integer idAnfitrion);
    
    CancelacionReservaResponseDTO cancelarReservaConPenalizacion(Integer idReserva);
}