package com.StayFlow.Service.Impl;

import com.StayFlow.Repository.*;
import com.StayFlow.Service.Interfaces.IHabitacionService;
import com.StayFlow.dto.request.CamaRequestDTO;
import com.StayFlow.dto.request.HabitacionRequestDTO;
import com.StayFlow.dto.request.TipoHabitacionRequestDTO;
import com.StayFlow.dto.response.HabitacionResponseDTO;
import com.StayFlow.dto.response.TipoHabitacionResponseDTO;
import com.StayFlow.exception.BusinessException;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.mapper.HabitacionMapper;
import com.StayFlow.model.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class HabitacionServiceImpl implements IHabitacionService {

    private final TipoHabitacionRepository tipoHabitacionRepository;
    private final HabitacionRepository habitacionRepository;
    private final PropiedadRepository propiedadRepository;
    private final TipoCamaRepository tipoCamaRepository;
    private final ServicioRepository servicioRepository;
    private final HabitacionMapper habitacionMapper;

    // Inyección de dependencias
    public HabitacionServiceImpl(TipoHabitacionRepository tipoHabitacionRepository,
                                 HabitacionRepository habitacionRepository,
                                 PropiedadRepository propiedadRepository,
                                 TipoCamaRepository tipoCamaRepository,
                                 ServicioRepository servicioRepository,
                                 HabitacionMapper habitacionMapper) {
        this.tipoHabitacionRepository = tipoHabitacionRepository;
        this.habitacionRepository = habitacionRepository;
        this.propiedadRepository = propiedadRepository;
        this.tipoCamaRepository = tipoCamaRepository;
        this.servicioRepository = servicioRepository;
        this.habitacionMapper = habitacionMapper;
    }

    // -------------------------------------------------------------------
    // MÉTODOS PARA TIPO DE HABITACIÓN (Categorías)
    // -------------------------------------------------------------------

    @Override
    @Transactional
    public TipoHabitacionResponseDTO crearTipoHabitacion(Integer idPropiedad, TipoHabitacionRequestDTO request) {
        Propiedad propiedad = buscarPropiedadYValidarDueno(idPropiedad);

        TipoHabitacion nuevoTipo = habitacionMapper.toTipoEntity(request);
        nuevoTipo.setPropiedad(propiedad);

        // Asignar Servicios si vienen en el request
        if (request.getIdServicios() != null && !request.getIdServicios().isEmpty()) {
            List<Servicio> serviciosEncontrados = servicioRepository.findAllById(request.getIdServicios());
            if (serviciosEncontrados.size() != request.getIdServicios().size()) {
                throw new BusinessException("Uno o más servicios proporcionados no existen en el catálogo.");
            }
            nuevoTipo.setServicios(serviciosEncontrados);
        } else {
            nuevoTipo.setServicios(new ArrayList<>());
        }

        TipoHabitacion tipoGuardado = tipoHabitacionRepository.save(nuevoTipo);
        return habitacionMapper.toTipoResponseDTO(tipoGuardado);
    }
    // El método para obtener tipos de habitación por propiedad también valida que la propiedad exista antes de buscar sus tipos
    @Override
    @Transactional(readOnly = true)
    public List<TipoHabitacionResponseDTO> obtenerTiposPorPropiedad(Integer idPropiedad) {
        // Validamos que la propiedad exista antes de buscar sus tipos
        propiedadRepository.findByIdPropiedadAndEstaEliminadoFalse(idPropiedad)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad", "id", idPropiedad));
                
        List<TipoHabitacion> tipos = tipoHabitacionRepository.findByPropiedadIdPropiedadAndEstaEliminadoFalse(idPropiedad);
        return habitacionMapper.toTipoResponseDTOList(tipos);
    }

    // -------------------------------------------------------------------
    // MÉTODOS PARA HABITACIÓN (Cuartos Físicos)
    // -------------------------------------------------------------------

    @Override
    @Transactional
    public HabitacionResponseDTO crearHabitacion(Integer idTipoHabitacion, HabitacionRequestDTO request) {
        TipoHabitacion tipoHabitacion = tipoHabitacionRepository.findByIdTipoHabitacionAndEstaEliminadoFalse(idTipoHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("TipoHabitacion", "id", idTipoHabitacion));

        // Validamos que el usuario sea el dueño de la propiedad a la que pertenece esta categoría
        validarDueno(tipoHabitacion.getPropiedad());

        Habitacion nuevaHabitacion = new Habitacion();
        nuevaHabitacion.setPropiedad(tipoHabitacion.getPropiedad());
        nuevaHabitacion.setTipoHabitacion(tipoHabitacion);
        nuevaHabitacion.setNumeroHabitacion(request.getNumeroHabitacion());

        // Procesar las camas usando el método addCama de la entidad
        if (request.getCamas() != null && !request.getCamas().isEmpty()) {
            for (CamaRequestDTO camaDTO : request.getCamas()) {
                TipoCama tipoCama = tipoCamaRepository.findById(camaDTO.getIdTipoCama())
                        .orElseThrow(() -> new ResourceNotFoundException("TipoCama", "id", camaDTO.getIdTipoCama()));
                nuevaHabitacion.addCama(tipoCama, camaDTO.getCantidad());
            }
        }

        Habitacion habitacionGuardada = habitacionRepository.save(nuevaHabitacion);
        return habitacionMapper.toHabitacionResponseDTO(habitacionGuardada);
    }
    // El método para obtener habitaciones por tipo también valida que el tipo de habitación exista antes de buscar sus habitaciones
    @Override
    @Transactional(readOnly = true)
    public List<HabitacionResponseDTO> obtenerHabitacionesPorTipo(Integer idTipoHabitacion) {
        tipoHabitacionRepository.findByIdTipoHabitacionAndEstaEliminadoFalse(idTipoHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("TipoHabitacion", "id", idTipoHabitacion));

        List<Habitacion> habitaciones = habitacionRepository.findByTipoHabitacionIdTipoHabitacionAndEstaEliminadoFalse(idTipoHabitacion);
        return habitacionMapper.toHabitacionResponseDTOList(habitaciones);
    }

    // -------------------------------------------------------------------
    // MÉTODOS AUXILIARES DE SEGURIDAD Y VALIDACIÓN
    // -------------------------------------------------------------------

    // Este método busca la propiedad por ID y valida que el usuario autenticado sea el dueño
    private Propiedad buscarPropiedadYValidarDueno(Integer idPropiedad) {
        Propiedad propiedad = propiedadRepository.findByIdPropiedadAndEstaEliminadoFalse(idPropiedad)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad", "id", idPropiedad));
        validarDueno(propiedad);
        return propiedad;
    }
    // Este método valida que el usuario autenticado sea el dueño de la propiedad
    private void validarDueno(Propiedad propiedad) {
        String emailAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        if (propiedad.getDueno() == null || !propiedad.getDueno().getEmail().equals(emailAutenticado)) {
            throw new BusinessException("Acceso denegado: No tienes permiso para modificar una propiedad que no te pertenece.");
        }
    }
}