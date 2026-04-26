package com.StayFlow.mapper;

import com.StayFlow.dto.request.TipoHabitacionRequestDTO;
import com.StayFlow.dto.response.HabitacionResponseDTO;
import com.StayFlow.dto.response.ServicioResponseDTO;
import com.StayFlow.dto.response.TipoHabitacionResponseDTO;
import com.StayFlow.model.Habitacion;
import com.StayFlow.model.Servicio;
import com.StayFlow.model.TipoHabitacion;
import org.springframework.stereotype.Component;
import com.StayFlow.model.FotoHabitacion;
import com.StayFlow.dto.response.FotoResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HabitacionMapper {

    public TipoHabitacion toTipoEntity(TipoHabitacionRequestDTO request) {
        if (request == null) return null;

        TipoHabitacion entity = new TipoHabitacion();
        entity.setNombreTipo(request.getNombreTipo());
        entity.setCapacidad(request.getCapacidad());
        entity.setPrecioBaseNoche(request.getPrecioBaseNoche());
        entity.setTieneBanoPrivado(request.getTieneBanoPrivado());
        return entity;
    }

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

        if (entity.getFotos() != null && !entity.getFotos().isEmpty()) {
            dto.setFotos(entity.getFotos().stream()
                    .filter(foto -> !foto.isEstaEliminado())
                    .map(this::toFotoResponseDTO)
                    .collect(Collectors.toList()));
        }

        // --- LAS CAMAS AHORA SE MAPEAN AQUÍ ---
        dto.setDetalleCamas(entity.getNombresCamas());
        dto.setTotalCamas(entity.getTotalCamas());

        return dto;
    }
    
    public List<TipoHabitacionResponseDTO> toTipoResponseDTOList(List<TipoHabitacion> entidades) {
        if (entidades == null) return null;
        return entidades.stream().map(this::toTipoResponseDTO).collect(Collectors.toList());
    }

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

        return dto;
    }

    public List<HabitacionResponseDTO> toHabitacionResponseDTOList(List<Habitacion> entidades) {
        if (entidades == null) return null;
        return entidades.stream().map(this::toHabitacionResponseDTO).collect(Collectors.toList());
    }
    
    private ServicioResponseDTO toServicioResponseDTO(Servicio servicio) {
        if (servicio == null) return null;
        ServicioResponseDTO dto = new ServicioResponseDTO();
        dto.setIdServicio(servicio.getIdServicio());
        dto.setNombreServicio(servicio.getNombreServicio());
        return dto;
    }

    private FotoResponseDTO toFotoResponseDTO(FotoHabitacion foto) {
        if (foto == null) return null;
        FotoResponseDTO dto = new FotoResponseDTO();
        dto.setIdFoto(foto.getIdFotoHabitacion());
        dto.setUrlFoto(foto.getUrlFoto());
        dto.setEsPrincipal(foto.isEsPrincipal());
        return dto;
    }
}