package com.StayFlow.service.Impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.StayFlow.service.interfaces.IPrecioTemporadaService;
import com.StayFlow.dto.request.PrecioTemporadaRequestDTO;
import com.StayFlow.dto.response.PrecioTemporadaResponseDTO;
import com.StayFlow.exception.BusinessException;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.mapper.PrecioTemporadaMapper;
import com.StayFlow.model.PrecioTemporada;
import com.StayFlow.model.TipoHabitacion;
import com.StayFlow.repository.PrecioTemporadaRepository;
import com.StayFlow.repository.TipoHabitacionRepository;

@Service
public class PrecioTemporadaServiceImpl implements IPrecioTemporadaService {

    private final PrecioTemporadaRepository precioTemporadaRepository;
    private final TipoHabitacionRepository tipoHabitacionRepository;
    private final PrecioTemporadaMapper precioTemporadaMapper; 

    public PrecioTemporadaServiceImpl(PrecioTemporadaRepository precioTemporadaRepository, 
                                      TipoHabitacionRepository tipoHabitacionRepository,
                                      PrecioTemporadaMapper precioTemporadaMapper) {
        this.precioTemporadaRepository = precioTemporadaRepository;
        this.tipoHabitacionRepository = tipoHabitacionRepository;
        this.precioTemporadaMapper = precioTemporadaMapper;
    }

    @Override
    @Transactional
    public PrecioTemporadaResponseDTO crearTemporada(PrecioTemporadaRequestDTO request) {
        validarFechas(request);
        validarSolapamiento(request.getIdTipoHabitacion(), request.getFechaInicio(), request.getFechaFin(), null);

        TipoHabitacion tipoHabitacion = tipoHabitacionRepository.findById(request.getIdTipoHabitacion())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de habitación no encontrado."));

        PrecioTemporada temporada = new PrecioTemporada();
        temporada.setTipoHabitacion(tipoHabitacion);
        temporada.setFechaInicio(request.getFechaInicio());
        temporada.setFechaFin(request.getFechaFin());
        temporada.setPrecioEspecial(request.getPrecioEspecial());

        return precioTemporadaMapper.toResponseDTO(precioTemporadaRepository.save(temporada));
    }

    @Override
    @Transactional
    public PrecioTemporadaResponseDTO actualizarTemporada(Integer id, PrecioTemporadaRequestDTO request) {
        validarFechas(request);
        validarSolapamiento(request.getIdTipoHabitacion(), request.getFechaInicio(), request.getFechaFin(), id);

        PrecioTemporada temporada = precioTemporadaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Temporada no encontrada con id: " + id));

        if (temporada.isEstaEliminado()) {
            throw new BusinessException("No se puede editar una temporada eliminada.");
        }

        TipoHabitacion tipoHabitacion = tipoHabitacionRepository.findById(request.getIdTipoHabitacion())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de habitación no encontrado."));

        temporada.setTipoHabitacion(tipoHabitacion);
        temporada.setFechaInicio(request.getFechaInicio());
        temporada.setFechaFin(request.getFechaFin());
        temporada.setPrecioEspecial(request.getPrecioEspecial());

        return precioTemporadaMapper.toResponseDTO(precioTemporadaRepository.save(temporada));
    }

    @Override
    @Transactional
    public void eliminarTemporada(Integer id) {
        PrecioTemporada temporada = precioTemporadaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Temporada no encontrada."));
        
        temporada.setEstaEliminado(true);
        precioTemporadaRepository.save(temporada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrecioTemporadaResponseDTO> listarTemporadasActivas() {
        return precioTemporadaMapper.toResponseDTOList(precioTemporadaRepository.findByEstaEliminadoFalse());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrecioTemporadaResponseDTO> listarTemporadasPorTipos(List<Integer> ids) {
        return precioTemporadaMapper.toResponseDTOList(precioTemporadaRepository.findByTipoHabitacion_IdTipoHabitacionInAndEstaEliminadoFalse(ids));
    }


    private void validarFechas(PrecioTemporadaRequestDTO request) {
        if (!request.getFechaInicio().isBefore(request.getFechaFin()) && !request.getFechaInicio().isEqual(request.getFechaFin())) {
            throw new BusinessException("La fecha de inicio debe ser anterior o igual a la fecha de fin.");
        }
    }

    private void validarSolapamiento(Integer idTipo, java.time.LocalDate inicio, java.time.LocalDate fin, Integer idIgnorar) {
        List<PrecioTemporada> conflictos = precioTemporadaRepository.encontrarSolapamientos(idTipo, inicio, fin, idIgnorar);
        if (!conflictos.isEmpty()) {
            throw new BusinessException("Las fechas se solapan con otra temporada ya existente para este tipo de habitación.");
        }
    }
}