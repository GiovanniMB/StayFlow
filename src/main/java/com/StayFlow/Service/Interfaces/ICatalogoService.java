package com.StayFlow.Service.Interfaces;

import java.util.List;

import com.StayFlow.dto.response.CatalogoResponseDTO;
import com.StayFlow.dto.response.ServicioResponseDTO;
import com.StayFlow.dto.response.TipoCamaResponseDTO;

public interface ICatalogoService {
    List<CatalogoResponseDTO> obtenerTodosLosEstados();
    List<CatalogoResponseDTO> obtenerMunicipiosPorEstado(Integer idEstado);
    List<CatalogoResponseDTO> obtenerColoniasPorMunicipio(Integer idMunicipio);
    List<ServicioResponseDTO> obtenerTodosLosServicios();
    List<TipoCamaResponseDTO> obtenerTiposCama();
    List<CatalogoResponseDTO> obtenerCategoriasFoto();
}