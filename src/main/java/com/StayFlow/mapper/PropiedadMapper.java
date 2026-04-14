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

@Component
public class PropiedadMapper {

    // --- Mapeo a Entidades ---

    public Propiedad toEntity(PropiedadRequestDTO request) {
        if (request == null) return null;

        Propiedad propiedad = new Propiedad();
        propiedad.setNombreComercial(request.getNombreComercial());
        propiedad.setTelefono(request.getTelefono());
        propiedad.setSeRentaPorHabitaciones(request.isSeRentaPorHabitaciones());
        propiedad.setDescripcion(request.getDescripcion());
        
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
        // Nota: La Colonia se asignará en la capa de Servicio
        return direccion;
    }

    // --- Mapeo a Response DTOs ---

    public PropiedadResponseDTO toResponseDTO(Propiedad propiedad) {
        if (propiedad == null) return null;

        PropiedadResponseDTO response = new PropiedadResponseDTO();
        response.setIdPropiedad(propiedad.getIdPropiedad());
        response.setNombreComercial(propiedad.getNombreComercial());
        response.setTelefono(propiedad.getTelefono());
        response.setSeRentaPorHabitaciones(propiedad.isSeRentaPorHabitaciones());
        response.setContadorReservas(propiedad.getContadorReservas());
        response.setDescripcion(propiedad.getDescripcion());

        if (propiedad.getDueno() != null) {
            response.setIdDueno(propiedad.getDueno().getIdUsuario());
        }

        if (propiedad.getDireccion() != null) {
            response.setDireccion(toDireccionResponseDTO(propiedad.getDireccion()));
        }

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
        }

        return response;
    }

    public List<PropiedadResponseDTO> toResponseDTOList(List<Propiedad> propiedades) {
        if (propiedades == null) return null;
        return propiedades.stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }
}