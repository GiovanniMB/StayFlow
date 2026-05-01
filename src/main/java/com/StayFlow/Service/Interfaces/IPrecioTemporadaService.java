package com.StayFlow.Service.Interfaces;

import com.StayFlow.dto.request.PrecioTemporadaRequestDTO;
import com.StayFlow.dto.response.PrecioTemporadaResponseDTO;
import java.util.List;

public interface IPrecioTemporadaService {
    PrecioTemporadaResponseDTO crearTemporada(PrecioTemporadaRequestDTO request);
    PrecioTemporadaResponseDTO actualizarTemporada(Integer id, PrecioTemporadaRequestDTO request);
    void eliminarTemporada(Integer id);
    List<PrecioTemporadaResponseDTO> listarTemporadasActivas();
}