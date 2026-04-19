package com.StayFlow.Service.Interfaces;

import com.StayFlow.dto.response.CatalogoResponseDTO;
import java.util.List;

public interface ICatalogoService {
    List<CatalogoResponseDTO> obtenerTodosLosEstados();
    List<CatalogoResponseDTO> obtenerMunicipiosPorEstado(Integer idEstado);
    List<CatalogoResponseDTO> obtenerColoniasPorMunicipio(Integer idMunicipio);
}