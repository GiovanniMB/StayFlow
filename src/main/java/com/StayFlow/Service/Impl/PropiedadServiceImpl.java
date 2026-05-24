package com.StayFlow.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.StayFlow.service.interfaces.ILogSistemaService;
import com.StayFlow.service.interfaces.IPropiedadService;
import com.StayFlow.dto.request.PropiedadRequestDTO;
import com.StayFlow.dto.response.PropiedadResponseDTO;
import com.StayFlow.exception.BusinessException;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.mapper.PropiedadMapper;
import com.StayFlow.model.Colonia;
import com.StayFlow.model.Direccion;
import com.StayFlow.model.LogSistema.Accion;
import com.StayFlow.model.Propiedad;
import com.StayFlow.model.Rol;
import com.StayFlow.model.Servicio;
import com.StayFlow.model.Usuario;
import com.StayFlow.repository.ColoniaRepository;
import com.StayFlow.repository.DireccionRepository;
import com.StayFlow.repository.PropiedadRepository;
import com.StayFlow.repository.RolRepository;
import com.StayFlow.repository.ServicioRepository;
import com.StayFlow.repository.UsuarioRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.StayFlow.specification.PropiedadSpecification;

@Service
public class PropiedadServiceImpl implements IPropiedadService {

    private final PropiedadRepository propiedadRepository;
    private final ColoniaRepository coloniaRepository;
    private final DireccionRepository direccionRepository;
    private final UsuarioRepository usuarioRepository; 
    private final PropiedadMapper propiedadMapper;
    private final ServicioRepository servicioRepository;
    private final ILogSistemaService logSistemaService;
    private final RolRepository rolRepository;

    public PropiedadServiceImpl(PropiedadRepository propiedadRepository, 
                                ColoniaRepository coloniaRepository, 
                                DireccionRepository direccionRepository,
                                UsuarioRepository usuarioRepository, 
                                PropiedadMapper propiedadMapper,
                                ServicioRepository servicioRepository,
                                ILogSistemaService logSistemaService,
                                RolRepository rolRepository) { 
        this.propiedadRepository = propiedadRepository;
        this.coloniaRepository = coloniaRepository;
        this.direccionRepository = direccionRepository;
        this.usuarioRepository = usuarioRepository;
        this.propiedadMapper = propiedadMapper;
        this.servicioRepository = servicioRepository;
        this.logSistemaService = logSistemaService;
        this.rolRepository = rolRepository; 
    }

    @Override
    @Transactional
    public PropiedadResponseDTO crearPropiedad(PropiedadRequestDTO request) {
        Propiedad propiedad = propiedadMapper.toEntity(request);

        if (request.getSeRentaPorHabitaciones()) {
            propiedad.setPrecioNoche(BigDecimal.ZERO);
        } else {
            propiedad.setPrecioNoche(request.getPrecioNoche() != null ? request.getPrecioNoche() : BigDecimal.ZERO);
        }

        if (request.getDireccion() != null && request.getDireccion().getIdColonia() != null) {
            Integer idColonia = request.getDireccion().getIdColonia();
            Colonia colonia = coloniaRepository.findById(idColonia)
                    .orElseThrow(() -> new ResourceNotFoundException("Colonia", "id", idColonia));
            
            propiedad.getDireccion().setColonia(colonia);
            // Asigna solo los servicios oficiales del catálogo
        propiedad.setServicios(procesarServicios(request.getIdServicios()));
        
        // Empaca los personalizados como una simple lista de texto separada por comas
        if (request.getNuevosServicios() != null && !request.getNuevosServicios().isEmpty()) {
            propiedad.setAmenidadesExtra(String.join(", ", request.getNuevosServicios()));
        } else {
            propiedad.setAmenidadesExtra(null);
        }
            Direccion direccionGuardada = direccionRepository.save(propiedad.getDireccion());
            propiedad.setDireccion(direccionGuardada);
        }

        String emailAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        
        Usuario dueno = usuarioRepository.findByEmail(emailAutenticado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", emailAutenticado));

        boolean esArrendador = dueno.getRoles().stream()
                .anyMatch(rol -> rol.getNombreRol().equalsIgnoreCase("arrendador"));

        if (!esArrendador) {
            Rol rolArrendador = rolRepository.findByNombreRol("arrendador")
                    .orElseThrow(() -> new BusinessException("El rol 'arrendador' no existe en el catálogo del sistema."));
            
            dueno.getRoles().add(rolArrendador);
            usuarioRepository.save(dueno);
            
            logSistemaService.registrarLog("usuario", dueno.getIdUsuario(), Accion.UPDATE);
        }

        propiedad.setDueno(dueno);
        Propiedad propiedadGuardada = propiedadRepository.save(propiedad);
        
        logSistemaService.registrarLog("propiedad", propiedadGuardada.getIdPropiedad(), Accion.INSERT);
        
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

        String emailAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!propiedadExistente.getDueno().getEmail().equals(emailAutenticado)) {
            throw new BusinessException("No tienes permiso para editar una propiedad que no te pertenece.");
        }

        propiedadExistente.setNombreComercial(request.getNombreComercial());
        propiedadExistente.setTelefono(request.getTelefono());
        propiedadExistente.setSeRentaPorHabitaciones(request.getSeRentaPorHabitaciones());
        propiedadExistente.setDescripcion(request.getDescripcion());

        if (request.getSeRentaPorHabitaciones()) {
            propiedadExistente.setPrecioNoche(BigDecimal.ZERO);
        } else {
            propiedadExistente.setPrecioNoche(request.getPrecioNoche() != null ? request.getPrecioNoche() : BigDecimal.ZERO);
        }

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

       // Asigna solo los servicios oficiales del catálogo
        propiedadExistente.setServicios(procesarServicios(request.getIdServicios()));
        
        // Empaca los personalizados como una simple lista de texto separada por comas
        if (request.getNuevosServicios() != null && !request.getNuevosServicios().isEmpty()) {
            propiedadExistente.setAmenidadesExtra(String.join(", ", request.getNuevosServicios()));
        } else {
            propiedadExistente.setAmenidadesExtra(null);
        }

        if (request.getHoraCheckIn() != null) {
            propiedadExistente.setHoraCheckIn(request.getHoraCheckIn());
        }
        if (request.getHoraCheckOut() != null) {
            propiedadExistente.setHoraCheckOut(request.getHoraCheckOut());
        }

        Propiedad propiedadActualizada = propiedadRepository.save(propiedadExistente);
        
        logSistemaService.registrarLog("propiedad", propiedadActualizada.getIdPropiedad(), Accion.UPDATE);
        
        return propiedadMapper.toResponseDTO(propiedadActualizada);
    }

    @Override
    @Transactional
    public void eliminarPropiedad(Integer idPropiedad) {
        Propiedad propiedadExistente = buscarPropiedadOArrojarExcepcion(idPropiedad);
        
        String emailAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!propiedadExistente.getDueno().getEmail().equals(emailAutenticado)) {
            throw new BusinessException("No tienes permiso para eliminar una propiedad que no te pertenece.");
        }

        propiedadExistente.setEstaEliminado(true);
        propiedadRepository.save(propiedadExistente); 
        
        logSistemaService.registrarLog("propiedad", idPropiedad, Accion.DELETE_LOGICO);
    }

    private Propiedad buscarPropiedadOArrojarExcepcion(Integer idPropiedad) {
        return propiedadRepository.findByIdPropiedadAndEstaEliminadoFalse(idPropiedad)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad", "idPropiedad", idPropiedad));
    }

    private List<Servicio> procesarServicios(List<Integer> idServicios) {
        List<Servicio> serviciosFinales = new ArrayList<>();
        if (idServicios != null && !idServicios.isEmpty()) {
            List<Servicio> serviciosEncontrados = servicioRepository.findAllById(idServicios);
            if (serviciosEncontrados.size() != idServicios.size()) {
                throw new BusinessException("Uno o más servicios proporcionados no existen en el catálogo.");
            }
            serviciosFinales.addAll(serviciosEncontrados);
        }
        return serviciosFinales;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropiedadResponseDTO> obtenerPropiedadesDisponibles(LocalDate fechaEntrada, LocalDate fechaSalida) {
        
        // 1. Reglas de negocio: Validar que las fechas sean correctas
        if (fechaEntrada == null || fechaSalida == null) {
            throw new BusinessException("Las fechas de entrada y salida son obligatorias para la búsqueda.");
        }
        if (fechaEntrada.isBefore(LocalDate.now())) {
            throw new BusinessException("La fecha de entrada no puede ser en el pasado.");
        }
        if (!fechaEntrada.isBefore(fechaSalida)) {
            throw new BusinessException("La fecha de salida debe ser estrictamente posterior a la fecha de entrada.");
        }

        // 2. Ejecuta el Query OTA
        // Llama al método en PropiedadRepository
        List<Propiedad> propiedadesDisponibles = propiedadRepository.findDisponiblesByFechas(fechaEntrada, fechaSalida);

        // 3. Convierte la lista de Entidades a DTOs para el Frontend
        return propiedadMapper.toResponseDTOList(propiedadesDisponibles);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropiedadResponseDTO> obtenerPropiedadesPorAnfitrion(Integer idAnfitrion) {
        List<Propiedad> propiedades = propiedadRepository.findByDuenoIdUsuarioAndEstaEliminadoFalse(idAnfitrion);
        return propiedadMapper.toResponseDTOList(propiedades);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropiedadResponseDTO> obtenerCatalogoPublico() {
        // Llamada al método del repositorio que trae solo propiedades con fotos y habitaciones, sin importar fechas
        List<Propiedad> propiedades = propiedadRepository.findPropiedadesPublicasCompletas();
        return propiedadMapper.toResponseDTOList(propiedades);
    }


    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<PropiedadResponseDTO> buscarCatalogoPaginado(
        String termino, LocalDate checkin, LocalDate checkout, List<Integer> servicios, java.math.BigDecimal precioMaximo, int page, int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        
        // Ejecuta la búsqueda dinámica
        org.springframework.data.domain.Page<Propiedad> propiedadesPaginadas = 
            propiedadRepository.findAll(PropiedadSpecification.buscarConFiltros(termino, checkin, checkout, servicios, precioMaximo), pageable);

        // Convierte el Page de Entidades a un Page de DTOs usando el mapper
        return propiedadesPaginadas.map(propiedadMapper::toResponseDTO);
    }
    
}