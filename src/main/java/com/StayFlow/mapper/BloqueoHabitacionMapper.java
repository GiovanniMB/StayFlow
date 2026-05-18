package com.StayFlow.mapper;

import com.StayFlow.dto.request.BloqueoHabitacionRequestDTO;
import com.StayFlow.dto.response.BloqueoHabitacionResponseDTO;
import com.StayFlow.model.BloqueoHabitacion;
import org.springframework.stereotype.Component;

@Component
public class BloqueoHabitacionMapper {

    // Convierte lo que envía React (el DTO) a lo que entiende la Base de Datos (Entity)
    public BloqueoHabitacion toEntity(BloqueoHabitacionRequestDTO request) {
        if (request == null) return null;
        
        BloqueoHabitacion bloqueo = new BloqueoHabitacion();
        bloqueo.setFechaInicio(request.getFechaInicio());
        bloqueo.setFechaFin(request.getFechaFin());
        bloqueo.setMotivo(request.getMotivo());
        
        return bloqueo;
    }

    // Convierte lo que escupe la Base de Datos (Entity) a lo que necesita React (DTO)
    public BloqueoHabitacionResponseDTO toResponseDTO(BloqueoHabitacion b) {
        if (b == null) return null;
        
        BloqueoHabitacionResponseDTO dto = new BloqueoHabitacionResponseDTO();
        dto.setIdBloqueoHabitacion(b.getIdBloqueoHabitacion());
        dto.setFechaInicio(b.getFechaInicio());
        dto.setFechaFin(b.getFechaFin());
        dto.setMotivo(b.getMotivo());

        // Lógica de presentación para el Frontend
        if (b.getHabitacion() != null) {
            dto.setNivelBloqueo("CUARTO");
            dto.setNombreAfectado("Puerta: " + b.getHabitacion().getNumeroHabitacion());
            dto.setIdAfectado(b.getHabitacion().getIdHabitacion()); 
        } else if (b.getTipoHabitacion() != null) {
            dto.setNivelBloqueo("CATEGORIA");
            dto.setNombreAfectado("Categoría: " + b.getTipoHabitacion().getNombreTipo());
            dto.setIdAfectado(b.getTipoHabitacion().getIdTipoHabitacion()); 
        } else if (b.getPropiedad() != null) {
            dto.setNivelBloqueo("PROPIEDAD");
            dto.setNombreAfectado("Todo el alojamiento: " + b.getPropiedad().getNombreComercial());
            dto.setIdAfectado(b.getPropiedad().getIdPropiedad()); 
        }

        return dto;
    }
}