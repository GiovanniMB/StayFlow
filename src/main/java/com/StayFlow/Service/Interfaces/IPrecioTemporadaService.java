package com.StayFlow.service.interfaces;

import java.util.List;

import com.StayFlow.dto.request.PrecioTemporadaRequestDTO;
import com.StayFlow.dto.response.PrecioTemporadaResponseDTO;

public interface IPrecioTemporadaService {
    PrecioTemporadaResponseDTO crearTemporada(PrecioTemporadaRequestDTO request);
    PrecioTemporadaResponseDTO actualizarTemporada(Integer id, PrecioTemporadaRequestDTO request);
    void eliminarTemporada(Integer id);
    List<PrecioTemporadaResponseDTO> listarTemporadasActivas();
}