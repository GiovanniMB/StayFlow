package com.StayFlow.Service.Interfaces;

import com.StayFlow.dto.request.HabitacionRequestDTO;
import com.StayFlow.dto.request.TipoHabitacionRequestDTO;
import com.StayFlow.dto.response.HabitacionResponseDTO;
import com.StayFlow.dto.response.TipoHabitacionResponseDTO;

import java.util.List;

public interface IHabitacionService {
    
    // --- Operaciones para TipoHabitacion (Categorías) ---
    TipoHabitacionResponseDTO crearTipoHabitacion(Integer idPropiedad, TipoHabitacionRequestDTO request);
    List<TipoHabitacionResponseDTO> obtenerTiposPorPropiedad(Integer idPropiedad);
    TipoHabitacionResponseDTO actualizarTipoHabitacion(Integer idTipoHabitacion, TipoHabitacionRequestDTO request);
    void eliminarTipoHabitacion(Integer idTipoHabitacion);
    
    // --- Operaciones para Habitacion (Físicas) ---
    HabitacionResponseDTO crearHabitacion(Integer idTipoHabitacion, HabitacionRequestDTO request);
    List<HabitacionResponseDTO> obtenerHabitacionesPorTipo(Integer idTipoHabitacion);
    HabitacionResponseDTO actualizarHabitacion(Integer idHabitacion, HabitacionRequestDTO request);
    void eliminarHabitacion(Integer idHabitacion);
}