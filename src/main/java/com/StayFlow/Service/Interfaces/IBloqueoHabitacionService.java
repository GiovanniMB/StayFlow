package com.StayFlow.service.interfaces;

import com.StayFlow.dto.request.BloqueoHabitacionRequestDTO;
import com.StayFlow.dto.response.BloqueoHabitacionResponseDTO;
import java.util.List;

public interface IBloqueoHabitacionService {
    BloqueoHabitacionResponseDTO crearBloqueo(BloqueoHabitacionRequestDTO request);
    List<BloqueoHabitacionResponseDTO> obtenerBloqueosActivosPorPropiedad(Integer idPropiedad);
    void eliminarBloqueo(Integer idBloqueo);
}