package com.StayFlow.mapper;

import com.StayFlow.dto.request.TipoHabitacionRequestDTO;
import com.StayFlow.dto.response.HabitacionResponseDTO;
import com.StayFlow.dto.response.ServicioResponseDTO;
import com.StayFlow.dto.response.TipoHabitacionResponseDTO;
import com.StayFlow.model.Habitacion;
import com.StayFlow.model.Servicio;
import com.StayFlow.model.TipoHabitacion;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
// Mapper para convertir entre entidades y DTOs relacionados con Habitaciones y Tipos de Habitaciones. Centraliza la lógica de transformación de datos para mantener el código limpio y organizado.
@Component
public class HabitacionMapper {

    // --- Mapeo para TipoHabitacion (Categoría) ---

    public TipoHabitacion toTipoEntity(TipoHabitacionRequestDTO request) {
        if (request == null) return null;

        TipoHabitacion entity = new TipoHabitacion();
        entity.setNombreTipo(request.getNombreTipo());
        entity.setCapacidad(request.getCapacidad());
        entity.setPrecioBaseNoche(request.getPrecioBaseNoche());
        entity.setTieneBanoPrivado(request.getTieneBanoPrivado());
        // Propiedad y Servicios se inyectan en la capa de Servicio
        return entity;
    }

    // El toEntity de Habitacion se hará en el Service porque requiere buscar Tipos de Cama en BD.
    public TipoHabitacionResponseDTO toTipoResponseDTO(TipoHabitacion entity) {
        if (entity == null) return null;

        TipoHabitacionResponseDTO dto = new TipoHabitacionResponseDTO();
        dto.setIdTipoHabitacion(entity.getIdTipoHabitacion());
        dto.setNombreTipo(entity.getNombreTipo());
        dto.setCapacidad(entity.getCapacidad());
        dto.setPrecioBaseNoche(entity.getPrecioBaseNoche());
        dto.setTieneBanoPrivado(entity.isTieneBanoPrivado());

        if (entity.getPropiedad() != null) {
            dto.setIdPropiedad(entity.getPropiedad().getIdPropiedad());
        }

        if (entity.getServicios() != null && !entity.getServicios().isEmpty()) {
            dto.setServicios(entity.getServicios().stream()
                    .map(this::toServicioResponseDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
    // Método para convertir una lista de entidades TipoHabitacion a una lista de DTOs TipoHabitacionResponseDTO
    public List<TipoHabitacionResponseDTO> toTipoResponseDTOList(List<TipoHabitacion> entidades) {
        if (entidades == null) return null;
        return entidades.stream().map(this::toTipoResponseDTO).collect(Collectors.toList());
    }

    // --- Mapeo para Habitacion (Física) ---
    // Nota: El toEntity de Habitacion se hará en el Service porque requiere buscar Tipos de Cama en BD.

    public HabitacionResponseDTO toHabitacionResponseDTO(Habitacion entity) {
        if (entity == null) return null;

        HabitacionResponseDTO dto = new HabitacionResponseDTO();
        dto.setIdHabitacion(entity.getIdHabitacion());
        dto.setNumeroHabitacion(entity.getNumeroHabitacion());
        dto.setEstado(entity.getEstado() != null ? entity.getEstado().name() : null);

        if (entity.getTipoHabitacion() != null) {
            dto.setIdTipoHabitacion(entity.getTipoHabitacion().getIdTipoHabitacion());
            dto.setNombreTipoHabitacion(entity.getTipoHabitacion().getNombreTipo());
        }

        // Usamos los métodos de utilidad de la entidad que hizo tu equipo
        dto.setDetalleCamas(entity.getNombresCamas());
        dto.setTotalCamas(entity.getTotalCamas());

        return dto;
    }

    public List<HabitacionResponseDTO> toHabitacionResponseDTOList(List<Habitacion> entidades) {
        if (entidades == null) return null;
        return entidades.stream().map(this::toHabitacionResponseDTO).collect(Collectors.toList());
    }

    // --- Helper ---
    private ServicioResponseDTO toServicioResponseDTO(Servicio servicio) {
        if (servicio == null) return null;
        ServicioResponseDTO dto = new ServicioResponseDTO();
        dto.setIdServicio(servicio.getIdServicio());
        dto.setNombreServicio(servicio.getNombreServicio());
        return dto;
    }
}