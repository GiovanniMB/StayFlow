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
import com.StayFlow.model.TipoHabitacionCama;
import java.util.ArrayList;
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
        dto.setAmenidadesExtra(entity.getAmenidadesExtra());
        // Mapea los servicios asociados a este tipo de habitación
        if (entity.getServicios() != null) {
            dto.setServicios(entity.getServicios().stream()
                    .map(this::toServicioResponseDTO)
                    .collect(Collectors.toList()));
        }

        if (entity.getFotos() != null) {
            dto.setFotos(entity.getFotos().stream()
                    .map(this::toFotoResponseDTO)
                    .collect(Collectors.toList()));
        }

        // Empacamos las camas para la CATEGORÍA
        if (entity.getCamas() != null && !entity.getCamas().isEmpty()) {
            int totalCamas = 0;
            List<String> listaCamas = new java.util.ArrayList<>();
            
            for (com.StayFlow.model.TipoHabitacionCama relacionCama : entity.getCamas()) {
                totalCamas += relacionCama.getCantidad();
                String nombreCama = relacionCama.getTipoCama().getNombre();
                listaCamas.add(relacionCama.getCantidad() + " " + nombreCama);
            }
            dto.setTotalCamas(totalCamas);
            dto.setDetalleCamas(listaCamas);
        } else {
            dto.setTotalCamas(0);
            dto.setDetalleCamas(java.util.List.of("Sin camas"));
        }

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

            // Empacar el total y el detalle de las camas respetando el modelo OTA
            if (entity.getTipoHabitacion().getCamas() != null && !entity.getTipoHabitacion().getCamas().isEmpty()) {
                int totalCamas = 0;
                
                // 🔥 CORRECCIÓN: Usamos una Lista en lugar de un String 🔥
                List<String> listaCamas = new ArrayList<>();

                for (TipoHabitacionCama relacionCama : entity.getTipoHabitacion().getCamas()) {
                    totalCamas += relacionCama.getCantidad();
                    
                    String nombreCama = relacionCama.getTipoCama().getNombre(); 
                    
                    // Agregamos cada tipo de cama como un elemento de la lista (Ej. "1 King size")
                    listaCamas.add(relacionCama.getCantidad() + " " + nombreCama);
                }

                dto.setTotalCamas(totalCamas);
                dto.setDetalleCamas(listaCamas); // Ahora sí, pasamos una Lista a una Lista
            } else {
                dto.setTotalCamas(0);
                // Si no hay camas, devolvemos una lista con un solo mensaje
                dto.setDetalleCamas(List.of("Sin camas configuradas")); 
            }
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