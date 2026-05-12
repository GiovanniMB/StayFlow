package com.StayFlow.Service.Interfaces;

import java.util.List;

import com.StayFlow.dto.request.ResenaRequestDTO;
import com.StayFlow.dto.response.ResenaPendienteResponseDTO;
import com.StayFlow.dto.response.ResenaResponseDTO;

public interface IResenaService {

    List<ResenaResponseDTO> obtenerResenasPorPropiedad(Integer idPropiedad);

    List<ResenaResponseDTO> obtenerResenasSobreHuesped(Integer idUsuario);

    ResenaResponseDTO guardarResena(ResenaRequestDTO request);

    List<ResenaPendienteResponseDTO> obtenerPendientesCliente(Integer idCliente);

    List<ResenaPendienteResponseDTO> obtenerPendientesPropietario(Integer idPropietario);
}