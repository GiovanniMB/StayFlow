package com.StayFlow.mapper;

import com.StayFlow.dto.request.DireccionRequestDTO;
import com.StayFlow.dto.request.PropiedadRequestDTO;
import com.StayFlow.dto.response.DireccionResponseDTO;
import com.StayFlow.dto.response.PropiedadResponseDTO;
import com.StayFlow.model.Direccion;
import com.StayFlow.model.Propiedad;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
import com.StayFlow.dto.response.ServicioResponseDTO;
import com.StayFlow.model.FotoHabitacion;
import com.StayFlow.dto.response.FotoResponseDTO;



@Component
public class PropiedadMapper {

    public Propiedad toEntity(PropiedadRequestDTO request) {
        if (request == null) return null;

        Propiedad propiedad = new Propiedad();
        propiedad.setNombreComercial(request.getNombreComercial());
        propiedad.setTelefono(request.getTelefono());
        propiedad.setSeRentaPorHabitaciones(request.getSeRentaPorHabitaciones());
        propiedad.setDescripcion(request.getDescripcion());
        propiedad.setPrecioNoche(request.getPrecioNoche());
        propiedad.setHoraCheckIn(request.getHoraCheckIn() != null ? request.getHoraCheckIn() : java.time.LocalTime.of(15, 0));
        propiedad.setHoraCheckOut(request.getHoraCheckOut() != null ? request.getHoraCheckOut() : java.time.LocalTime.of(11, 0));
        
        if (request.getDireccion() != null) {
            propiedad.setDireccion(toDireccionEntity(request.getDireccion()));
        }

        
        
        return propiedad;
    }

    private Direccion toDireccionEntity(DireccionRequestDTO request) {
        if (request == null) return null;
        
        Direccion direccion = new Direccion();
        direccion.setCalle(request.getCalle());
        direccion.setNumero(request.getNumero());
        direccion.setNumeroInterior(request.getNumeroInterior());
        direccion.setLatitud(request.getLatitud());
        direccion.setLongitud(request.getLongitud());
        return direccion;
    }

    public PropiedadResponseDTO toResponseDTO(Propiedad propiedad) {
        if (propiedad == null) return null;

        PropiedadResponseDTO response = new PropiedadResponseDTO();
        response.setIdPropiedad(propiedad.getIdPropiedad());
        response.setNombreComercial(propiedad.getNombreComercial());
        response.setTelefono(propiedad.getTelefono());
        response.setSeRentaPorHabitaciones(propiedad.isSeRentaPorHabitaciones());
        
        // Si marca error, verifica si Gerardo borró o renombró el contador de reservas
        response.setContadorReservas(propiedad.getContadorReservas());
        response.setDescripcion(propiedad.getDescripcion());
        response.setPrecioNoche(propiedad.getPrecioNoche());
        // Agrega estado de propiedad al DTO de respuesta
        if (propiedad.getEstadoPropiedad() != null) {
            response.setEstadoPropiedad(propiedad.getEstadoPropiedad().name());
        }
        // Mapea ID del dueño si existe
        if (propiedad.getDueno() != null) {
            response.setIdDueno(propiedad.getDueno().getIdUsuario());
        }
        // Mapea dirección si existe
        if (propiedad.getDireccion() != null) {
            response.setDireccion(toDireccionResponseDTO(propiedad.getDireccion()));
        }
        // Mapea servicios y fotos si existen
        if (propiedad.getServicios() != null && !propiedad.getServicios().isEmpty()) {
            response.setServicios(propiedad.getServicios().stream()
                .map(this::toServicioResponseDTO)
                .collect(Collectors.toList()));
        }

        if (propiedad.getFotos() != null && !propiedad.getFotos().isEmpty()) {
            response.setFotosGenerales(propiedad.getFotos().stream()
                .filter(foto -> !foto.isEstaEliminado() && foto.getTipoHabitacion() == null)
                .map(this::toFotoResponseDTO)
                .collect(Collectors.toList()));
        }

       response.setAmenidadesExtra(propiedad.getAmenidadesExtra());

        // Regla estricta: Si no tiene fotos o si es hotel y no tiene habitaciones, forzamos el estado a BORRADOR para que no se muestre en búsquedas públicas
        boolean tieneFotos = propiedad.getFotos() != null && !propiedad.getFotos().isEmpty();
        boolean tieneHabitaciones = propiedad.getTiposHabitacion() != null && !propiedad.getTiposHabitacion().isEmpty();

        // Si el estado original de BD viene nulo, lo aseguramos
        if (propiedad.getEstadoPropiedad() != null) {
            response.setEstadoPropiedad(propiedad.getEstadoPropiedad().name());
        }

        // Si falta foto, o si es hotel y no le han configurado cuartos, lo forzamos visualmente a BORRADOR
        if (!tieneFotos || (propiedad.isSeRentaPorHabitaciones() && !tieneHabitaciones)) {
            response.setEstadoPropiedad("BORRADOR"); 
        }

        response.setCalificacionPromedio(propiedad.getCalificacionPromedio() != null ? propiedad.getCalificacionPromedio() : 0.0);
        response.setCantidadResenas(propiedad.getCantidadResenas() != null ? propiedad.getCantidadResenas() : 0);

        return response;
    }

    private DireccionResponseDTO toDireccionResponseDTO(Direccion direccion) {
        if (direccion == null) return null;

        DireccionResponseDTO response = new DireccionResponseDTO();
        response.setId(direccion.getId());
        response.setCalle(direccion.getCalle());
        response.setNumero(direccion.getNumero());
        response.setNumeroInterior(direccion.getNumeroInterior());
        response.setLatitud(direccion.getLatitud());
        response.setLongitud(direccion.getLongitud());

        if (direccion.getColonia() != null) {
            response.setIdColonia(direccion.getColonia().getId());
            response.setNombreColonia(direccion.getColonia().getNombre()); 

            if (direccion.getColonia().getMunicipio() != null) {
                response.setMunicipio(direccion.getColonia().getMunicipio().getNombre());

                if (direccion.getColonia().getMunicipio().getEstado() != null) {
                    response.setEstado(direccion.getColonia().getMunicipio().getEstado().getNombre());
                }
            }
        }

        return response;
    }

    public List<PropiedadResponseDTO> toResponseDTOList(List<Propiedad> propiedades) {
        if (propiedades == null) return null;
        return propiedades.stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    private ServicioResponseDTO toServicioResponseDTO(com.StayFlow.model.Servicio servicio) {
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