package com.StayFlow.Service.Impl;

import com.StayFlow.Repository.ColoniaRepository;
import com.StayFlow.Repository.PropiedadRepository;
import com.StayFlow.Service.Interfaces.IPropiedadService;
import com.StayFlow.dto.request.PropiedadRequestDTO;
import com.StayFlow.dto.response.PropiedadResponseDTO;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.mapper.PropiedadMapper;
import com.StayFlow.model.Colonia;
import com.StayFlow.model.Propiedad;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PropiedadServiceImpl implements IPropiedadService {

    private final PropiedadRepository propiedadRepository;
    private final ColoniaRepository coloniaRepository;
    private final PropiedadMapper propiedadMapper;

    public PropiedadServiceImpl(PropiedadRepository propiedadRepository, 
                                ColoniaRepository coloniaRepository, 
                                PropiedadMapper propiedadMapper) {
        this.propiedadRepository = propiedadRepository;
        this.coloniaRepository = coloniaRepository;
        this.propiedadMapper = propiedadMapper;
    }

    @Override
    @Transactional
    public PropiedadResponseDTO crearPropiedad(PropiedadRequestDTO request) {
        Propiedad propiedad = propiedadMapper.toEntity(request);

        if (request.getDireccion() != null && request.getDireccion().getIdColonia() != null) {
            Integer idColonia = request.getDireccion().getIdColonia();
            // Usamos la excepcion del equipo con su constructor específico
            Colonia colonia = coloniaRepository.findById(idColonia)
                    .orElseThrow(() -> new ResourceNotFoundException("Colonia", "id", idColonia));
            
            propiedad.getDireccion().setColonia(colonia);
        }


        Propiedad propiedadGuardada = propiedadRepository.save(propiedad);
        return propiedadMapper.toResponseDTO(propiedadGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropiedadResponseDTO> obtenerTodas() {
        List<Propiedad> propiedades = propiedadRepository.findByEstaEliminadoFalse();
        return propiedadMapper.toResponseDTOList(propiedades);
    }

    @Override
    @Transactional(readOnly = true)
    public PropiedadResponseDTO obtenerPorId(Integer idPropiedad) {
        Propiedad propiedad = buscarPropiedadOArrojarExcepcion(idPropiedad);
        return propiedadMapper.toResponseDTO(propiedad);
    }

    @Override
    @Transactional
    public PropiedadResponseDTO actualizarPropiedad(Integer idPropiedad, PropiedadRequestDTO request) {
        Propiedad propiedadExistente = buscarPropiedadOArrojarExcepcion(idPropiedad);

        propiedadExistente.setNombreComercial(request.getNombreComercial());
        propiedadExistente.setTelefono(request.getTelefono());
        propiedadExistente.setSeRentaPorHabitaciones(request.isSeRentaPorHabitaciones());
        propiedadExistente.setDescripcion(request.getDescripcion());

        if (request.getDireccion() != null) {
            propiedadExistente.getDireccion().setCalle(request.getDireccion().getCalle());
            propiedadExistente.getDireccion().setNumero(request.getDireccion().getNumero());
            propiedadExistente.getDireccion().setNumeroInterior(request.getDireccion().getNumeroInterior());
            propiedadExistente.getDireccion().setLatitud(request.getDireccion().getLatitud());
            propiedadExistente.getDireccion().setLongitud(request.getDireccion().getLongitud());

            if (request.getDireccion().getIdColonia() != null) {
                Integer idColonia = request.getDireccion().getIdColonia();
                Colonia nuevaColonia = coloniaRepository.findById(idColonia)
                        .orElseThrow(() -> new ResourceNotFoundException("Colonia", "id", idColonia));
                propiedadExistente.getDireccion().setColonia(nuevaColonia);
            }
        }

        Propiedad propiedadActualizada = propiedadRepository.save(propiedadExistente);
        return propiedadMapper.toResponseDTO(propiedadActualizada);
    }

    @Override
    @Transactional
    public void eliminarPropiedad(Integer idPropiedad) {
        Propiedad propiedadExistente = buscarPropiedadOArrojarExcepcion(idPropiedad);
        propiedadExistente.setEstaEliminado(true);
        // Hibernate hace el update automáticamente al terminar el método transaccional
        propiedadRepository.save(propiedadExistente); 
    }

    private Propiedad buscarPropiedadOArrojarExcepcion(Integer idPropiedad) {
        return propiedadRepository.findByIdPropiedadAndEstaEliminadoFalse(idPropiedad)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad", "idPropiedad", idPropiedad));
    }
}