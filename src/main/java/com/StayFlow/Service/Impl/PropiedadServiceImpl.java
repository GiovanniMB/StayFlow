package com.StayFlow.Service.Impl;

import com.StayFlow.Repository.ColoniaRepository;
import com.StayFlow.Repository.DireccionRepository;
import com.StayFlow.Repository.PropiedadRepository;
import com.StayFlow.Repository.UsuarioRepository; 
import com.StayFlow.Repository.RolRepository;
import com.StayFlow.Service.Interfaces.IPropiedadService;
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
import com.StayFlow.Repository.ServicioRepository;
import com.StayFlow.model.Servicio;
import com.StayFlow.Service.Interfaces.ILogSistemaService;
import com.StayFlow.model.LogSistema.Accion;

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

        // 1. Resolver y guardar la Dirección y Colonia
        if (request.getDireccion() != null && request.getDireccion().getIdColonia() != null) {
            Integer idColonia = request.getDireccion().getIdColonia();
            Colonia colonia = coloniaRepository.findById(idColonia)
                    .orElseThrow(() -> new ResourceNotFoundException("Colonia", "id", idColonia));
            
            propiedad.getDireccion().setColonia(colonia);

            if (request.getIdServicios() != null && !request.getIdServicios().isEmpty()) {
                List<Servicio> serviciosEncontrados = servicioRepository.findAllById(request.getIdServicios());
                
                if (serviciosEncontrados.size() != request.getIdServicios().size()) {
                    throw new BusinessException("Uno o más servicios proporcionados no existen en el catálogo.");
                }
                propiedad.setServicios(serviciosEncontrados);
            } else {
                propiedad.setServicios(new ArrayList<>());
            }
            
            Direccion direccionGuardada = direccionRepository.save(propiedad.getDireccion());
            propiedad.setDireccion(direccionGuardada);
        }

        // 2. EXTRAER USUARIO DEL TOKEN 
        // Spring Security guarda el email en el 'Name' durante la autenticación
        String emailAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        
        Usuario dueno = usuarioRepository.findByEmail(emailAutenticado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", emailAutenticado));

        // 3. REGLA DE NEGOCIO: ¿Es arrendador? (Lógica de Promoción Dinámica)
        // Se comprueba si en su lista de roles alguno coincide con "arrendador"
        boolean esArrendador = dueno.getRoles().stream()
                .anyMatch(rol -> rol.getNombreRol().equalsIgnoreCase("arrendador"));

        if (!esArrendador) {
            // En lugar de lanzar error se busca el rol en el catálogo
            Rol rolArrendador = rolRepository.findByNombreRol("arrendador")
                    .orElseThrow(() -> new BusinessException("El rol 'arrendador' no existe en el catálogo del sistema."));
            
            // Le agregamos el nuevo rol conservando los que ya tenga (ej. arrendatario)
            dueno.getRoles().add(rolArrendador);
            usuarioRepository.save(dueno);
            
            // Registramos en auditoría que este usuario fue promovido automáticamente
            logSistemaService.registrarLog("usuario", dueno.getIdUsuario(), Accion.UPDATE);
        }

        // 4. Asignar el dueño a la propiedad
        propiedad.setDueno(dueno);

        // 5. Guardar en BD
        Propiedad propiedadGuardada = propiedadRepository.save(propiedad);
        
        // 6. Registrar en auditoría
        logSistemaService.registrarLog("propiedad", propiedadGuardada.getIdPropiedad(), Accion.INSERT);
        
        return propiedadMapper.toResponseDTO(propiedadGuardada);
    }


    // MÉTODOS DE LECTURA Y ACTUALIZACIÓN/ELIMINACIÓN CON REGLAS DE NEGOCIO
    @Override
    @Transactional(readOnly = true)
    public List<PropiedadResponseDTO> obtenerTodas() {
        List<Propiedad> propiedades = propiedadRepository.findByEstaEliminadoFalse();
        return propiedadMapper.toResponseDTOList(propiedades);
    }

    // OBTENER PROPIEDAD POR ID CON REGLA DE NEGOCIO: Solo el dueño o un arrendatario pueden verla
    @Override
    @Transactional(readOnly = true)
    public PropiedadResponseDTO obtenerPorId(Integer idPropiedad) {
        Propiedad propiedad = buscarPropiedadOArrojarExcepcion(idPropiedad);
        return propiedadMapper.toResponseDTO(propiedad);
    }

    //  ACTUALIZAR PROPIEDAD CON REGLA DE NEGOCIO: Solo el dueño puede editar su propiedad
    @Override
    @Transactional
    public PropiedadResponseDTO actualizarPropiedad(Integer idPropiedad, PropiedadRequestDTO request) {
        Propiedad propiedadExistente = buscarPropiedadOArrojarExcepcion(idPropiedad);

        // REGLA DE NEGOCIO: Solo el dueño puede editar su propiedad
        String emailAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!propiedadExistente.getDueno().getEmail().equals(emailAutenticado)) {
            throw new BusinessException("No tienes permiso para editar una propiedad que no te pertenece.");
        }

        propiedadExistente.setNombreComercial(request.getNombreComercial());
        propiedadExistente.setTelefono(request.getTelefono());
        propiedadExistente.setSeRentaPorHabitaciones(request.getSeRentaPorHabitaciones());
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

        if (request.getIdServicios() != null && !request.getIdServicios().isEmpty()) {
            List<Servicio> serviciosEncontrados = servicioRepository.findAllById(request.getIdServicios());
            
            if (serviciosEncontrados.size() != request.getIdServicios().size()) {
                throw new BusinessException("Uno o más servicios proporcionados no existen en el catálogo.");
            }
            propiedadExistente.setServicios(serviciosEncontrados);
        } else {
            propiedadExistente.setServicios(new ArrayList<>());
        }

        Propiedad propiedadActualizada = propiedadRepository.save(propiedadExistente);
        
        // Registrar en auditoría
        logSistemaService.registrarLog("propiedad", propiedadActualizada.getIdPropiedad(), Accion.UPDATE);
        
        return propiedadMapper.toResponseDTO(propiedadActualizada);
    }

    // ELIMINAR PROPIEDAD CON REGLA DE NEGOCIO: Solo el dueño puede eliminar su propiedad (marcar como eliminado lógico)
    @Override
    @Transactional
    public void eliminarPropiedad(Integer idPropiedad) {
        Propiedad propiedadExistente = buscarPropiedadOArrojarExcepcion(idPropiedad);
        
        // REGLA DE NEGOCIO: Solo el dueño puede eliminar su propiedad
        String emailAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!propiedadExistente.getDueno().getEmail().equals(emailAutenticado)) {
            throw new BusinessException("No tienes permiso para eliminar una propiedad que no te pertenece.");
        }

        propiedadExistente.setEstaEliminado(true);
        propiedadRepository.save(propiedadExistente); 
        
        // Registrar en auditoría
        logSistemaService.registrarLog("propiedad", idPropiedad, Accion.DELETE_LOGICO);
    }

    // MÉTODO AUXILIAR PARA BUSCAR PROPIEDAD O LANZAR EXCEPCIÓN SI NO EXISTE O ESTÁ ELIMINADA
    private Propiedad buscarPropiedadOArrojarExcepcion(Integer idPropiedad) {
        return propiedadRepository.findByIdPropiedadAndEstaEliminadoFalse(idPropiedad)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad", "idPropiedad", idPropiedad));
    }
}