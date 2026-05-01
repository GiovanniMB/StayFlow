package com.StayFlow.Service.Interfaces;

import com.StayFlow.dto.request.PropiedadRequestDTO;
import com.StayFlow.dto.response.PropiedadResponseDTO;

import java.time.LocalDate;

import java.util.List;

public interface IPropiedadService {
    PropiedadResponseDTO crearPropiedad(PropiedadRequestDTO request);
    List<PropiedadResponseDTO> obtenerTodas();
    PropiedadResponseDTO obtenerPorId(Integer idPropiedad);
    PropiedadResponseDTO actualizarPropiedad(Integer idPropiedad, PropiedadRequestDTO request);
    void eliminarPropiedad(Integer idPropiedad);
    // Nuevo método para buscar disponibilidad
    List<PropiedadResponseDTO> obtenerPropiedadesDisponibles(LocalDate fechaEntrada, LocalDate fechaSalida);
}