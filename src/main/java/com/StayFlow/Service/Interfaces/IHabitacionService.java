package com.StayFlow.Service.Interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.StayFlow.dto.request.HabitacionRequestDTO;
import com.StayFlow.dto.request.TipoHabitacionRequestDTO;
import com.StayFlow.dto.response.HabitacionResponseDTO;
import com.StayFlow.dto.response.TipoHabitacionResponseDTO;

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

    void subirFotoHabitacion(Integer idTipoHabitacion, Integer idCategoriaFoto, MultipartFile archivo, boolean esPrincipal);
    
    void subirFotoPropiedad(Integer idPropiedad, Integer idCategoriaFoto, MultipartFile archivo, boolean esPrincipal);
    
    void eliminarFoto(Integer idFoto);

}