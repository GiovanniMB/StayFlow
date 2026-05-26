package com.StayFlow.service.interfaces;

import com.StayFlow.dto.request.ResenaRequestDTO;
import com.StayFlow.dto.response.ResenaResponseDTO;
import java.util.List;

public interface IResenaService {
    ResenaResponseDTO crearResena(ResenaRequestDTO request, Integer idUsuarioAutenticado);
    List<ResenaResponseDTO> obtenerResenasPorPropiedad(Integer idPropiedad);
}