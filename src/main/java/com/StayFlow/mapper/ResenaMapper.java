package com.StayFlow.mapper;

import com.StayFlow.dto.response.ResenaResponseDTO;
import com.StayFlow.model.Resena;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResenaMapper {

    public ResenaResponseDTO toResponseDTO(Resena resena) {
        if (resena == null) return null;

        ResenaResponseDTO dto = new ResenaResponseDTO();
        dto.setIdResena(resena.getIdResena());
        dto.setIdReserva(resena.getReserva().getIdReserva());
        dto.setNombreCliente(resena.getUsuario().getNombreCompleto());
        dto.setPuntuacion(resena.getPuntuacion());
        dto.setComentario(resena.getComentario());
        dto.setFechaCreacion(resena.getFechaRegistro()); 

        return dto;
    }

    public List<ResenaResponseDTO> toResponseDTOList(List<Resena> resenas) {
        return resenas.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }
}