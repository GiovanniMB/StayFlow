package com.StayFlow.service.interfaces;

import com.StayFlow.dto.response.TipoTarjetaResponseDTO;
import java.util.List;

public interface ITipoTarjetaService {
    
    List<TipoTarjetaResponseDTO> obtenerTodosLosTiposTarjeta();
    
    TipoTarjetaResponseDTO obtenerTipoTarjetaPorId(Integer id);
    
    TipoTarjetaResponseDTO obtenerTipoTarjetaPorCodigo(String codigo);
}