package com.StayFlow.service.interfaces;

import com.StayFlow.dto.request.PropiedadRequestDTO;
import com.StayFlow.dto.response.PropiedadResponseDTO;

import java.util.List;

public interface IPropiedadService {
    PropiedadResponseDTO crearPropiedad(PropiedadRequestDTO request);
    List<PropiedadResponseDTO> obtenerTodas();
    PropiedadResponseDTO obtenerPorId(Integer idPropiedad);
    PropiedadResponseDTO actualizarPropiedad(Integer idPropiedad, PropiedadRequestDTO request);
    void eliminarPropiedad(Integer idPropiedad);
}