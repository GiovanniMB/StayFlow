package com.StayFlow.mapper;

import com.StayFlow.dto.response.PrecioTemporadaResponseDTO;
import com.StayFlow.model.PrecioTemporada;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PrecioTemporadaMapper {

    public PrecioTemporadaResponseDTO toResponseDTO(PrecioTemporada temporada) {
        if (temporada == null) return null;

        PrecioTemporadaResponseDTO dto = new PrecioTemporadaResponseDTO();
        dto.setIdPrecioTemporada(temporada.getIdPrecioTemporada());
        dto.setFechaInicio(temporada.getFechaInicio());
        dto.setFechaFin(temporada.getFechaFin());
        dto.setPrecioEspecial(temporada.getPrecioEspecial());
        
        if (temporada.getTipoHabitacion() != null) {
            dto.setIdTipoHabitacion(temporada.getTipoHabitacion().getIdTipoHabitacion());
            dto.setNombreTipoHabitacion(temporada.getTipoHabitacion().getNombreTipo());
        }
        return dto;
    }

    public List<PrecioTemporadaResponseDTO> toResponseDTOList(List<PrecioTemporada> temporadas) {
        if (temporadas == null) return null;
        return temporadas.stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }
}