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
import com.StayFlow.Service.Interfaces.ILogSistemaService;
import com.StayFlow.model.LogSistema.Accion; 

import java.util.List;

@Service
public class HabitacionServiceImpl implements IHabitacionService {

    private final TipoHabitacionRepository tipoHabitacionRepository;
    private final HabitacionRepository habitacionRepository;
    private final PropiedadRepository propiedadRepository;
    private final TipoCamaRepository tipoCamaRepository;
    private final ServicioRepository servicioRepository;
    private final HabitacionMapper habitacionMapper;
    private final ILogSistemaService logSistemaService;

    public HabitacionServiceImpl(TipoHabitacionRepository tipoHabitacionRepository,
                                 HabitacionRepository habitacionRepository,
                                 PropiedadRepository propiedadRepository,
                                 TipoCamaRepository tipoCamaRepository,
                                 ServicioRepository servicioRepository,
                                 ILogSistemaService logSistemaService,
                                 HabitacionMapper habitacionMapper) {
        this.tipoHabitacionRepository = tipoHabitacionRepository;
        this.habitacionRepository = habitacionRepository;
        this.propiedadRepository = propiedadRepository;
        this.tipoCamaRepository = tipoCamaRepository;
        this.servicioRepository = servicioRepository;
        this.habitacionMapper = habitacionMapper;
        this.logSistemaService = logSistemaService;
    }

    // -------------------------------------------------------------------
    // MÉTODOS PARA TIPO DE HABITACIÓN (Categorías)
    // -------------------------------------------------------------------
    // El método crearTipoHabitacion valida que la propiedad exista y que el usuario autenticado sea el dueño antes de crear una nueva categoría de habitación asociada a esa propiedad. También procesa la lista de servicios recibida en el request para asociarlos correctamente a la categoría.
    @Override
    @Transactional
    public TipoHabitacionResponseDTO crearTipoHabitacion(Integer idPropiedad, TipoHabitacionRequestDTO request) {
        Propiedad propiedad = buscarPropiedadYValidarDueno(idPropiedad);
        TipoHabitacion nuevoTipo = habitacionMapper.toTipoEntity(request);
        nuevoTipo.setPropiedad(propiedad);

        if (request.getIdServicios() != null && !request.getIdServicios().isEmpty()) {
            List<Servicio> serviciosEncontrados = servicioRepository.findAllById(request.getIdServicios());
            if (serviciosEncontrados.size() != request.getIdServicios().size()) {
                throw new BusinessException("Uno o más servicios proporcionados no existen en el catálogo.");
            }
            nuevoTipo.setServicios(serviciosEncontrados);
        }

        TipoHabitacion tipoGuardado = tipoHabitacionRepository.save(nuevoTipo);
        logSistemaService.registrarLog("tipohabitacion", tipoGuardado.getIdTipoHabitacion(), Accion.INSERT);

        return habitacionMapper.toTipoResponseDTO(tipoGuardado);
    }
    // El método obtenerTiposPorPropiedad primero valida que la propiedad exista antes de buscar los tipos de habitación asociados a esa propiedad. Esto garantiza que no se intente recuperar tipos de habitación para una propiedad que no existe, lo que podría causar confusión o errores en la aplicación.
    @Override
    @Transactional(readOnly = true)
    public List<TipoHabitacionResponseDTO> obtenerTiposPorPropiedad(Integer idPropiedad) {
        propiedadRepository.findByIdPropiedadAndEstaEliminadoFalse(idPropiedad)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad", "id", idPropiedad));
        return habitacionMapper.toTipoResponseDTOList(tipoHabitacionRepository.findByPropiedadIdPropiedadAndEstaEliminadoFalse(idPropiedad));
    }
    // El método actualizarTipoHabitacion busca el tipo de habitación por su ID, valida que el usuario autenticado sea el dueño de la propiedad a la que pertenece ese tipo de habitación, actualiza los datos del tipo de habitación y procesa la lista de servicios asociados a ese tipo. Si no se proporcionan servicios en el request, se eliminan los servicios actuales del tipo de habitación.
    @Override
    @Transactional
    public TipoHabitacionResponseDTO actualizarTipoHabitacion(Integer idTipoHabitacion, TipoHabitacionRequestDTO request) {
        TipoHabitacion tipoExistente = tipoHabitacionRepository.findByIdTipoHabitacionAndEstaEliminadoFalse(idTipoHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("TipoHabitacion", "id", idTipoHabitacion));
        validarDueno(tipoExistente.getPropiedad());

        tipoExistente.setNombreTipo(request.getNombreTipo());
        tipoExistente.setCapacidad(request.getCapacidad());
        tipoExistente.setPrecioBaseNoche(request.getPrecioBaseNoche());
        tipoExistente.setTieneBanoPrivado(request.getTieneBanoPrivado());

        if (request.getIdServicios() != null && !request.getIdServicios().isEmpty()) {
            List<Servicio> servicios = servicioRepository.findAllById(request.getIdServicios());
            tipoExistente.setServicios(servicios);
        } else {
            if(tipoExistente.getServicios() != null) tipoExistente.getServicios().clear();
        }

        TipoHabitacion tipoActualizado = tipoHabitacionRepository.save(tipoExistente);
        logSistemaService.registrarLog("tipohabitacion", tipoActualizado.getIdTipoHabitacion(), Accion.UPDATE);
        
        return habitacionMapper.toTipoResponseDTO(tipoActualizado);
    }
    // El método eliminarTipoHabitacion busca el tipo de habitación por su ID, valida que el usuario autenticado sea el dueño de la propiedad a la que pertenece ese tipo de habitación, y luego marca el tipo de habitación como eliminado en lugar de borrarlo físicamente de la base de datos. Esto permite mantener un historial de tipos de habitación eliminados y evita problemas de integridad referencial con las habitaciones físicas asociadas a ese tipo.
    @Override
    @Transactional
    public void eliminarTipoHabitacion(Integer idTipoHabitacion) {
        TipoHabitacion tipoExistente = tipoHabitacionRepository.findByIdTipoHabitacionAndEstaEliminadoFalse(idTipoHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("TipoHabitacion", "id", idTipoHabitacion));
        validarDueno(tipoExistente.getPropiedad());
        tipoExistente.setEstaEliminado(true);
        tipoHabitacionRepository.save(tipoExistente);
        logSistemaService.registrarLog("tipohabitacion", tipoExistente.getIdTipoHabitacion(), Accion.DELETE_LOGICO);
    }

    // -------------------------------------------------------------------
    // MÉTODOS PARA HABITACIÓN (Cuartos Físicos)
    // -------------------------------------------------------------------
    // El método crearHabitacion primero valida que el tipo de habitación exista y que el usuario autenticado sea el dueño de la propiedad a la que pertenece ese tipo de habitación antes de crear una nueva habitación física asociada a ese tipo. También procesa la lista de camas recibida en el request para asociarlas correctamente a la habitación.
    @Override
    @Transactional
    public HabitacionResponseDTO crearHabitacion(Integer idTipoHabitacion, HabitacionRequestDTO request) {
        TipoHabitacion tipoHabitacion = tipoHabitacionRepository.findByIdTipoHabitacionAndEstaEliminadoFalse(idTipoHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("TipoHabitacion", "id", idTipoHabitacion));
        validarDueno(tipoHabitacion.getPropiedad());

        Habitacion nuevaHabitacion = new Habitacion();
        nuevaHabitacion.setPropiedad(tipoHabitacion.getPropiedad());
        nuevaHabitacion.setTipoHabitacion(tipoHabitacion);
        nuevaHabitacion.setNumeroHabitacion(request.getNumeroHabitacion());

        procesarCamas(nuevaHabitacion, request.getCamas());

        Habitacion habitacionGuardada = habitacionRepository.save(nuevaHabitacion);
        logSistemaService.registrarLog("habitacion", habitacionGuardada.getIdHabitacion(), Accion.INSERT);

        return habitacionMapper.toHabitacionResponseDTO(habitacionGuardada);
    }
    // El método obtenerHabitacionesPorTipo primero valida que el tipo de habitación exista antes de buscar las habitaciones asociadas a ese tipo. Esto garantiza que no se intente recuperar habitaciones para un tipo que no existe, lo que podría causar confusión o errores en la aplicación.
    @Override
    @Transactional(readOnly = true)
    public List<HabitacionResponseDTO> obtenerHabitacionesPorTipo(Integer idTipoHabitacion) {
        tipoHabitacionRepository.findByIdTipoHabitacionAndEstaEliminadoFalse(idTipoHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("TipoHabitacion", "id", idTipoHabitacion));
        return habitacionMapper.toHabitacionResponseDTOList(habitacionRepository.findByTipoHabitacionIdTipoHabitacionAndEstaEliminadoFalse(idTipoHabitacion));
    }
    // El método actualizarHabitacion busca la habitación por su ID, valida que el usuario autenticado sea el dueño de la propiedad a la que pertenece la habitación, actualiza los datos de la habitación y procesa las camas asociadas a esa habitación. Si no se proporcionan camas en el request, se eliminan las camas actuales de la habitación.
    @Override
    @Transactional
    public HabitacionResponseDTO actualizarHabitacion(Integer idHabitacion, HabitacionRequestDTO request) {
        Habitacion habitacionExistente = habitacionRepository.findByIdHabitacionAndEstaEliminadoFalse(idHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("Habitacion", "id", idHabitacion));
        validarDueno(habitacionExistente.getPropiedad());

        habitacionExistente.setNumeroHabitacion(request.getNumeroHabitacion());
        
        // Limpiamos las camas actuales y asignamos las nuevas
        habitacionExistente.getHabitacionTipoCamas().clear();
        procesarCamas(habitacionExistente, request.getCamas());

        Habitacion habitacionActualizada = habitacionRepository.save(habitacionExistente);
        logSistemaService.registrarLog("habitacion", habitacionActualizada.getIdHabitacion(), Accion.UPDATE);

        return habitacionMapper.toHabitacionResponseDTO(habitacionActualizada);
    }
    // El método eliminarHabitacion busca la habitación por su ID, valida que el usuario autenticado sea el dueño de la propiedad a la que pertenece la habitación, y luego marca la habitación como eliminada en lugar de borrarla físicamente de la base de datos. Esto permite mantener un historial de habitaciones eliminadas y evita problemas de integridad referencial con reservas u otras entidades relacionadas.
    @Override
    @Transactional
    public void eliminarHabitacion(Integer idHabitacion) {
        Habitacion habitacionExistente = habitacionRepository.findByIdHabitacionAndEstaEliminadoFalse(idHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("Habitacion", "id", idHabitacion));
        validarDueno(habitacionExistente.getPropiedad());
        habitacionExistente.setEstaEliminado(true);
        habitacionRepository.save(habitacionExistente);
        
        logSistemaService.registrarLog("habitacion", idHabitacion, Accion.DELETE_LOGICO);
    }

    // -------------------------------------------------------------------
    // MÉTODOS AUXILIARES
    // -------------------------------------------------------------------
    // Este método busca la propiedad por su ID y valida que el usuario autenticado sea el dueño de la propiedad. Si la propiedad no existe o el usuario no es el dueño, se lanzan las excepciones correspondientes.
    private Propiedad buscarPropiedadYValidarDueno(Integer idPropiedad) {
        Propiedad propiedad = propiedadRepository.findByIdPropiedadAndEstaEliminadoFalse(idPropiedad)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad", "id", idPropiedad));
        validarDueno(propiedad);
        return propiedad;
    }
// Este método verifica que el usuario autenticado sea el dueño de la propiedad antes de permitir cualquier operación que modifique los tipos de habitación o las habitaciones físicas asociadas a esa propiedad. Si el usuario no es el dueño, se lanza una excepción de negocio indicando que no tiene permiso para realizar la acción.
    private void validarDueno(Propiedad propiedad) {
        String emailAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        if (propiedad.getDueno() == null || !propiedad.getDueno().getEmail().equals(emailAutenticado)) {
            throw new BusinessException("Acceso denegado: No tienes permiso para modificar una propiedad que no te pertenece.");
        }
    }
    // Este método procesa la lista de camas recibida en el request, validando que cada tipo de cama exista y luego asociándola a la habitación con la cantidad especificada. Si no se proporcionan camas, simplemente no se agregan a la habitación.
    private void procesarCamas(Habitacion habitacion, List<CamaRequestDTO> camasRequest) {
        if (camasRequest != null && !camasRequest.isEmpty()) {
            for (CamaRequestDTO camaDTO : camasRequest) {
                TipoCama tipoCama = tipoCamaRepository.findById(camaDTO.getIdTipoCama())
                        .orElseThrow(() -> new ResourceNotFoundException("TipoCama", "id", camaDTO.getIdTipoCama()));
                habitacion.addCama(tipoCama, camaDTO.getCantidad());
            }
        }
    }
}