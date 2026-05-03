package com.StayFlow.service.interfaces;

import java.util.List;

import com.StayFlow.dto.response.PropiedadResponseDTO;

public interface IFavoritoService {
    // Retorna true si se agregó a favoritos, false si se eliminó
    boolean toggleFavorito(Integer idPropiedad);
    
    // Retorna las propiedades completas para dibujarlas en la UI
    List<PropiedadResponseDTO> obtenerMisFavoritos();
    
    // Retorna solo una lista de IDs [1, 5, 12] para saber qué corazones pintar de rojo en el Frontend
    List<Integer> obtenerIdsMisFavoritos();
}