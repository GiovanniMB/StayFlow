package com.StayFlow.mapper;

import com.StayFlow.dto.response.MensajeChatResponseDTO;
import com.StayFlow.model.MensajeChat;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MensajeChatMapper {

    
    public MensajeChatResponseDTO toResponseDTO(MensajeChat entity) {
        if (entity == null) return null;

        MensajeChatResponseDTO dto = new MensajeChatResponseDTO();
        dto.setId(entity.getId());
        dto.setContenido(entity.getContenido());
        dto.setFechaEnvio(entity.getFechaEnvio());
        dto.setLeido(entity.getLeido());

        // relacion con reserva
        if (entity.getReserva() != null) {
            dto.setIdReserva(entity.getReserva().getIdReserva());
        }

        // relación con el Usuario (Remitente)
        if (entity.getRemitente() != null) {
            dto.setIdRemitente(entity.getRemitente().getIdUsuario());
            // Concatena el nombre para que el Frontend ya lo reciba listo
            dto.setNombreRemitente(entity.getRemitente().getNombre() + " " + entity.getRemitente().getApellidoPaterno());
        }

        return dto;
    }

    // convierte Lista de Entidades a Lista de DTOs
    public List<MensajeChatResponseDTO> toResponseDTOList(List<MensajeChat> entidades) {
        if (entidades == null) return null;
        return entidades.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}