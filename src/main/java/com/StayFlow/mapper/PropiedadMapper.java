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

    // --- Mapeo a Entidades ---
    // Método principal para mapear un PropiedadRequestDTO a una Propiedad, incluyendo su dirección. Los servicios se asignarán en la capa de Servicio, por lo que no se incluyen en este método.
    public Propiedad toEntity(PropiedadRequestDTO request) {
        if (request == null) return null;

        Propiedad propiedad = new Propiedad();
        propiedad.setNombreComercial(request.getNombreComercial());
        propiedad.setTelefono(request.getTelefono());
        propiedad.setSeRentaPorHabitaciones(request.getSeRentaPorHabitaciones());
        propiedad.setDescripcion(request.getDescripcion());
        propiedad.setPrecioNoche(request.getPrecioNoche());
        
        if (request.getDireccion() != null) {
            propiedad.setDireccion(toDireccionEntity(request.getDireccion()));
        }
        
        return propiedad;
    }

    // Mapeo de DireccionRequestDTO a Direccion
    private Direccion toDireccionEntity(DireccionRequestDTO request) {
        if (request == null) return null;
        
        Direccion direccion = new Direccion();
        direccion.setCalle(request.getCalle());
        direccion.setNumero(request.getNumero());
        direccion.setNumeroInterior(request.getNumeroInterior());
        direccion.setLatitud(request.getLatitud());
        direccion.setLongitud(request.getLongitud());
        //La Colonia se asignará en la capa de Servicio
        return direccion;
    }

    //Mapeo a Response DTOs
    // Método principal para mapear una Propiedad a PropiedadResponseDTO, incluyendo sus relaciones (Dueño, Dirección, Servicios)
    public PropiedadResponseDTO toResponseDTO(Propiedad propiedad) {
        if (propiedad == null) return null;

        PropiedadResponseDTO response = new PropiedadResponseDTO();
        response.setIdPropiedad(propiedad.getIdPropiedad());
        response.setNombreComercial(propiedad.getNombreComercial());
        response.setTelefono(propiedad.getTelefono());
        response.setSeRentaPorHabitaciones(propiedad.isSeRentaPorHabitaciones());
        response.setContadorReservas(propiedad.getContadorReservas());
        response.setDescripcion(propiedad.getDescripcion());
        response.setPrecioNoche(propiedad.getPrecioNoche());

        if (propiedad.getDueno() != null) {
            response.setIdDueno(propiedad.getDueno().getIdUsuario());
        }

        if (propiedad.getDireccion() != null) {
            response.setDireccion(toDireccionResponseDTO(propiedad.getDireccion()));
        }

        if (propiedad.getServicios() != null && !propiedad.getServicios().isEmpty()) {
            response.setServicios(propiedad.getServicios().stream()
                .map(this::toServicioResponseDTO)
                .collect(Collectors.toList()));
        }

        if (propiedad.getFotos() != null && !propiedad.getFotos().isEmpty()) {
            response.setFotosGenerales(propiedad.getFotos().stream()
                .filter(foto -> !foto.isEstaEliminado() && foto.getTipoHabitacion() == null) // Solo las de la propiedad
                .map(this::toFotoResponseDTO)
                .collect(Collectors.toList()));
        }
                
        return response;
    }

    // Mapeo de Direccion a DireccionResponseDTO
    private DireccionResponseDTO toDireccionResponseDTO(Direccion direccion) {
        if (direccion == null) return null;

        DireccionResponseDTO response = new DireccionResponseDTO();
        response.setId(direccion.getId());
        response.setCalle(direccion.getCalle());
        response.setNumero(direccion.getNumero());
        response.setNumeroInterior(direccion.getNumeroInterior());
        response.setLatitud(direccion.getLatitud());
        response.setLongitud(direccion.getLongitud());

        // --- EXTRACCIÓN PROFUNDA PARA EL BUSCADOR DE REACT (Sin Código Postal) ---
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

    // Método para mapear una lista de Propiedad a una lista de PropiedadResponseDTO
    public List<PropiedadResponseDTO> toResponseDTOList(List<Propiedad> propiedades) {
        if (propiedades == null) return null;
        return propiedades.stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    // Mapeo de Servicio a ServicioResponseDTO
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