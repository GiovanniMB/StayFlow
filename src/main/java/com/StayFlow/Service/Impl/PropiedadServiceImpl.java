package com.StayFlow.Service.Impl;

import com.StayFlow.dto.request.PropiedadRequestDTO;
import com.StayFlow.dto.response.PropiedadResponseDTO;
import com.StayFlow.exception.BusinessException;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.mapper.PropiedadMapper;
import com.StayFlow.model.Colonia;
import com.StayFlow.model.Direccion;
import com.StayFlow.model.Propiedad;
import com.StayFlow.model.Usuario; 
import com.StayFlow.model.Rol; 
import org.springframework.security.core.context.SecurityContextHolder; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.StayFlow.model.Servicio;
import com.StayFlow.model.LogSistema.Accion;
import com.StayFlow.Repository.ColoniaRepository;
import com.StayFlow.Repository.DireccionRepository;
import com.StayFlow.Repository.PropiedadRepository;
import com.StayFlow.Repository.RolRepository;
import com.StayFlow.Repository.ServicioRepository;
import com.StayFlow.Repository.UsuarioRepository;
import com.StayFlow.Service.Interfaces.ILogSistemaService;
import com.StayFlow.Service.Interfaces.IPropiedadService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
            propiedad.setServicios(procesarServicios(request.getIdServicios(), request.getNuevosServicios()));
            
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

       propiedadExistente.setServicios(procesarServicios(request.getIdServicios(), request.getNuevosServicios()));

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

    private List<Servicio> procesarServicios(List<Integer> idServicios, List<String> nuevosServicios) {
        List<Servicio> serviciosFinales = new ArrayList<>();

        if (idServicios != null && !idServicios.isEmpty()) {
            List<Servicio> serviciosEncontrados = servicioRepository.findAllById(idServicios);
            if (serviciosEncontrados.size() != idServicios.size()) {
                throw new BusinessException("Uno o más servicios proporcionados no existen en el catálogo.");
            }
            serviciosFinales.addAll(serviciosEncontrados);
        }

        if (nuevosServicios != null && !nuevosServicios.isEmpty()) {
            for (String nombreNuevo : nuevosServicios) {
                String nombreLimpio = nombreNuevo.trim();
                if (!nombreLimpio.isEmpty()) {
                    Servicio servicio = servicioRepository.findByNombreServicioIgnoreCase(nombreLimpio)
                            .orElseGet(() -> {
                                Servicio nuevo = new Servicio();
                                String nombreCapitalizado = nombreLimpio.substring(0, 1).toUpperCase() + nombreLimpio.substring(1).toLowerCase();
                                nuevo.setNombreServicio(nombreCapitalizado);
                                return servicioRepository.save(nuevo);
                            });
                    
                    if (!serviciosFinales.contains(servicio)) {
                        serviciosFinales.add(servicio);
                    }
                }
            }
        }
        return serviciosFinales;
    }
}