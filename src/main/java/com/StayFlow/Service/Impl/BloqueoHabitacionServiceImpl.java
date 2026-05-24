package com.StayFlow.service.Impl;

import com.StayFlow.dto.request.BloqueoHabitacionRequestDTO;
import com.StayFlow.dto.response.BloqueoHabitacionResponseDTO;
import com.StayFlow.exception.BusinessException;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.mapper.BloqueoHabitacionMapper;
import com.StayFlow.model.BloqueoHabitacion;
import com.StayFlow.model.Habitacion;
import com.StayFlow.model.Propiedad;
import com.StayFlow.model.TipoHabitacion;
import com.StayFlow.repository.BloqueoHabitacionRepository;
import com.StayFlow.repository.HabitacionRepository;
import com.StayFlow.repository.PropiedadRepository;
import com.StayFlow.repository.TipoHabitacionRepository;
import com.StayFlow.service.interfaces.IBloqueoHabitacionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BloqueoHabitacionServiceImpl implements IBloqueoHabitacionService {

    private final BloqueoHabitacionRepository bloqueoRepository;
    private final PropiedadRepository propiedadRepository;
    private final TipoHabitacionRepository tipoHabitacionRepository;
    private final HabitacionRepository habitacionRepository;
    private final BloqueoHabitacionMapper bloqueoMapper;

    public BloqueoHabitacionServiceImpl(BloqueoHabitacionRepository bloqueoRepository,
                                        PropiedadRepository propiedadRepository,
                                        TipoHabitacionRepository tipoHabitacionRepository,
                                        HabitacionRepository habitacionRepository,
                                        BloqueoHabitacionMapper bloqueoMapper) {
        this.bloqueoRepository = bloqueoRepository;
        this.propiedadRepository = propiedadRepository;
        this.tipoHabitacionRepository = tipoHabitacionRepository;
        this.habitacionRepository = habitacionRepository;
        this.bloqueoMapper = bloqueoMapper;
    }

    @Override
    @Transactional
    public BloqueoHabitacionResponseDTO crearBloqueo(BloqueoHabitacionRequestDTO request) {
        if (request.getFechaInicio().isBefore(LocalDate.now())) {
            throw new BusinessException("El mantenimiento no puede iniciar en el pasado.");
        }
        if (!request.getFechaInicio().isBefore(request.getFechaFin())) {
            throw new BusinessException("La fecha de fin debe ser posterior a la fecha de inicio.");
        }

        // Usa el Mapper para crear la entidad base
        BloqueoHabitacion bloqueo = bloqueoMapper.toEntity(request);

        // Lógica para detectar qué nivel se está bloqueando y enlazarlo
        if (request.getIdHabitacion() != null) {
            Habitacion h = habitacionRepository.findById(request.getIdHabitacion())
                    .orElseThrow(() -> new ResourceNotFoundException("Habitación física no encontrada"));
            bloqueo.setHabitacion(h);
        } else if (request.getIdTipoHabitacion() != null) {
            TipoHabitacion t = tipoHabitacionRepository.findById(request.getIdTipoHabitacion())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
            bloqueo.setTipoHabitacion(t);
        } else if (request.getIdPropiedad() != null) {
            Propiedad p = propiedadRepository.findByIdPropiedadAndEstaEliminadoFalse(request.getIdPropiedad())
                    .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada"));
            bloqueo.setPropiedad(p);
        } else {
            throw new BusinessException("Debe especificar qué propiedad, categoría o cuarto desea bloquear.");
        }

        // Usa el Mapper para devolver la respuesta
        return bloqueoMapper.toResponseDTO(bloqueoRepository.save(bloqueo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BloqueoHabitacionResponseDTO> obtenerBloqueosActivosPorPropiedad(Integer idPropiedad) {
        return bloqueoRepository.findBloqueosActivosPorPropiedad(idPropiedad).stream()
                .map(bloqueoMapper::toResponseDTO) 
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarBloqueo(Integer idBloqueo) {
        if (!bloqueoRepository.existsById(idBloqueo)) {
            throw new ResourceNotFoundException("El bloqueo especificado no existe.");
        }
        bloqueoRepository.deleteById(idBloqueo); 
    }
}