package com.StayFlow.service;

import com.StayFlow.dto.request.AdminBloqueoRequestDTO;
import com.StayFlow.dto.response.*;
import com.StayFlow.exception.BusinessException;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.model.*;
import com.StayFlow.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UsuarioRepository usuarioRepository;
    private final PropiedadRepository propiedadRepository;
    private final ReservaRepository reservaRepository;
    private final BloqueoHabitacionRepository bloqueoRepository;
    private final HabitacionRepository habitacionRepository;
    private final RolRepository rolRepository;
    private final TipoHabitacionRepository tipoHabitacionRepository;

    public AdminService(UsuarioRepository usuarioRepository,
                        PropiedadRepository propiedadRepository,
                        ReservaRepository reservaRepository,
                        BloqueoHabitacionRepository bloqueoRepository,
                        HabitacionRepository habitacionRepository,
                        RolRepository rolRepository,
                        TipoHabitacionRepository tipoHabitacionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.propiedadRepository = propiedadRepository;
        this.reservaRepository = reservaRepository;
        this.bloqueoRepository = bloqueoRepository;
        this.habitacionRepository = habitacionRepository;
        this.rolRepository = rolRepository;
        this.tipoHabitacionRepository = tipoHabitacionRepository;
    }

    // ========== USUARIOS ==========
    
    public List<UsuarioAdminResponseDTO> obtenerTodosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirUsuarioAdminADTO)
                .collect(Collectors.toList());
    }

    public UsuarioAdminResponseDTO obtenerUsuarioPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return convertirUsuarioAdminADTO(usuario);
    }
    @Transactional
    public UsuarioAdminResponseDTO cambiarRolUsuario(Integer id, String nombreRol) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        

        String rolNormalizado = nombreRol.toLowerCase();
        System.out.println(rolNormalizado);
        Rol nuevoRol = rolRepository.findByNombreRol(rolNormalizado)
                .orElseThrow(() -> new BusinessException("Rol inválido. Debe ser 'ARRENDADOR' o 'ARRENDATARIO'"));
        
        usuario.getRoles().clear();
        usuario.getRoles().add(nuevoRol);
        usuario = usuarioRepository.save(usuario);
        return convertirUsuarioAdminADTO(usuario);
    }

    @Transactional
    public void bloquearUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        usuario.setEstaEliminado(true);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void desbloquearUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        usuario.setEstaEliminado(false);
        usuarioRepository.save(usuario);
    }

    // ========== PROPIEDADES ==========
    
    public List<PropiedadAdminResponseDTO> obtenerTodasPropiedades() {
        return propiedadRepository.findAll().stream()
                .filter(p -> !p.isEstaEliminado())
                .map(this::convertirPropiedadADTO)
                .collect(Collectors.toList());
    }

    public PropiedadAdminResponseDTO obtenerPropiedadPorId(Integer id) {
        Propiedad propiedad = propiedadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada"));
        return convertirPropiedadADTO(propiedad);
    }

    @Transactional
    public void eliminarPropiedad(Integer id) {
        Propiedad propiedad = propiedadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada"));
        propiedad.setEstaEliminado(true);
        propiedadRepository.save(propiedad);
    }

    @Transactional
    public void aprobarPropiedad(Integer id) {
        Propiedad propiedad = propiedadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada"));
        propiedad.setEstadoPropiedad(Propiedad.EstadoPropiedad.PUBLICADA);
        propiedadRepository.save(propiedad);
    }

    // ========== RESERVAS ==========
    
    public List<ReservaAdminResponseDTO> obtenerTodasReservas() {
        return reservaRepository.findAll().stream()
                .map(this::convertirReservaADTO)
                .collect(Collectors.toList());
    }

    public List<ReservaAdminResponseDTO> obtenerReservasPorPropiedad(Integer idPropiedad) {
        return reservaRepository.findByHabitacion_TipoHabitacion_Propiedad_IdPropiedad(idPropiedad).stream()
                .map(this::convertirReservaADTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelarReservaAdmin(Integer id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));
        reserva.setEstadoReserva(Reserva.EstadoReserva.cancelada);
        
        Habitacion habitacion = reserva.getHabitacion();
        if (habitacion != null) {
            habitacion.setEstado(Habitacion.EstadoHabitacion.disponible);
            habitacionRepository.save(habitacion);
        }
        
        reservaRepository.save(reserva);
    }

    // ========== BLOQUEOS ==========
    
    public List<BloqueoAdminResponseDTO> obtenerTodosBloqueos() {
        return bloqueoRepository.findAll().stream()
                .map(this::convertirBloqueoADTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void crearBloqueo(AdminBloqueoRequestDTO request) {
        BloqueoHabitacion bloqueo = new BloqueoHabitacion();
        bloqueo.setFechaInicio(request.getFechaInicio());
        bloqueo.setFechaFin(request.getFechaFin());
        bloqueo.setMotivo(request.getMotivo());
        
        if (request.getIdPropiedad() != null) {
            Propiedad propiedad = propiedadRepository.findById(request.getIdPropiedad())
                    .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada"));
            bloqueo.setPropiedad(propiedad);
        } else if (request.getIdTipoHabitacion() != null) {
            TipoHabitacion tipo = tipoHabitacionRepository.findById(request.getIdTipoHabitacion())
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de habitación no encontrado"));
            bloqueo.setTipoHabitacion(tipo);
        } else if (request.getIdHabitacion() != null) {
            Habitacion habitacion = habitacionRepository.findById(request.getIdHabitacion())
                    .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada"));
            bloqueo.setHabitacion(habitacion);
        } else {
            throw new BusinessException("Debe especificar propiedad, tipo de habitación o habitación para el bloqueo");
        }
        
        bloqueoRepository.save(bloqueo);
    }

    @Transactional
    public void eliminarBloqueo(Integer id) {
        bloqueoRepository.deleteById(id);
    }

    // ========== ESTADÍSTICAS ==========
    
    public EstadisticasAdminResponseDTO obtenerEstadisticas() {
        long totalUsuarios = usuarioRepository.count();
        
        // Contar usuarios activos (estaEliminado = false)
        long usuariosActivos = 0;
        for (Usuario u : usuarioRepository.findAll()) {
            if (!u.isEstaEliminado()) {
                usuariosActivos++;
            }
        }
        
        long totalPropiedades = (int) propiedadRepository.count();
        long totalReservas = reservaRepository.count();
        long reservasConfirmadas = reservaRepository.countByEstadoReserva(Reserva.EstadoReserva.confirmada);
        
        // Calcular propiedades publicadas y en borrador
        long propiedadesPublicadas = 0;
        long propiedadesBorrador = 0;
        for (Propiedad p : propiedadRepository.findAll()) {
            if (p.getEstadoPropiedad() == Propiedad.EstadoPropiedad.PUBLICADA) {
                propiedadesPublicadas++;
            } else if (p.getEstadoPropiedad() == Propiedad.EstadoPropiedad.BORRADOR) {
                propiedadesBorrador++;
            }
        }
        
        long reservasPendientes = reservaRepository.countByEstadoReserva(Reserva.EstadoReserva.pendiente);
        long reservasCanceladas = reservaRepository.countByEstadoReserva(Reserva.EstadoReserva.cancelada);
        
        EstadisticasAdminResponseDTO estadisticas = new EstadisticasAdminResponseDTO();
        estadisticas.setTotalUsuarios(totalUsuarios);
        estadisticas.setUsuariosActivos(usuariosActivos);
        estadisticas.setUsuariosBloqueados(totalUsuarios - usuariosActivos);
        estadisticas.setTotalPropiedades(totalPropiedades);
        estadisticas.setPropiedadesPublicadas(propiedadesPublicadas);
        estadisticas.setPropiedadesBorrador(propiedadesBorrador);
        estadisticas.setTotalReservas(totalReservas);
        estadisticas.setReservasConfirmadas(reservasConfirmadas);
        estadisticas.setReservasPendientes(reservasPendientes);
        estadisticas.setReservasCanceladas(reservasCanceladas);
        
        return estadisticas;
    }

    // ========== MÉTODOS PRIVADOS ==========
    
    private UsuarioAdminResponseDTO convertirUsuarioAdminADTO(Usuario usuario) {
        UsuarioAdminResponseDTO dto = new UsuarioAdminResponseDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombreCompleto(usuario.getNombreCompleto());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        
        String rol = usuario.hasRol("ARRENDADOR") ? "arrendador" : 
                     usuario.hasRol("ADMIN") ? "administrador" : "arrendatario";
        dto.setRol(rol);
        
        dto.setActivo(!usuario.isEstaEliminado());
        dto.setEmailConfirmado(usuario.isEmailConfirmado());
        dto.setFechaRegistro(usuario.getFechaRegistro());
        
        int cantidadPropiedades = propiedadRepository.findByDuenoIdUsuarioAndEstaEliminadoFalse(usuario.getIdUsuario()).size();
        dto.setCantidadPropiedades(cantidadPropiedades);
        
        return dto;
    }

    private PropiedadAdminResponseDTO convertirPropiedadADTO(Propiedad propiedad) {
        PropiedadAdminResponseDTO dto = new PropiedadAdminResponseDTO();
        dto.setIdPropiedad(propiedad.getIdPropiedad());
        dto.setNombreComercial(propiedad.getNombreComercial());
        dto.setDescripcion(propiedad.getDescripcion());
        dto.setEstadoPropiedad(propiedad.getEstadoPropiedad() != null ? propiedad.getEstadoPropiedad().name() : "BORRADOR");
        dto.setDestacada(false);
        dto.setSeRentaPorHabitaciones(propiedad.isSeRentaPorHabitaciones());
        dto.setPrecioNoche(propiedad.getPrecioNoche());
        dto.setFechaCreacion(propiedad.getFechaRegistro());
        
        if (propiedad.getDueno() != null) {
            dto.setNombreAnfitrion(propiedad.getDueno().getNombreCompleto());
            dto.setIdAnfitrion(propiedad.getDueno().getIdUsuario());
        }
        
        if (propiedad.getDireccion() != null && propiedad.getDireccion().getColonia() != null) {
            String ubicacion = propiedad.getDireccion().getColonia().getNombre();
            if (propiedad.getDireccion().getColonia().getMunicipio() != null) {
                ubicacion += ", " + propiedad.getDireccion().getColonia().getMunicipio().getNombre();
                if (propiedad.getDireccion().getColonia().getMunicipio().getEstado() != null) {
                    ubicacion += ", " + propiedad.getDireccion().getColonia().getMunicipio().getEstado().getNombre();
                }
            }
            dto.setUbicacion(ubicacion);
        }
        
        if (propiedad.getFotos() != null && !propiedad.getFotos().isEmpty()) {
            propiedad.getFotos().stream()
                .filter(FotoHabitacion::isEsPrincipal)
                .findFirst()
                .ifPresentOrElse(
                    foto -> dto.setImagenPortada(foto.getUrlFoto()),
                    () -> dto.setImagenPortada(propiedad.getFotos().get(0).getUrlFoto())
                );
        }
        
        dto.setCalificacionPromedio(propiedad.getCalificacionPromedio());
        
        int cantidadReservas = 0;
        if (propiedad.getTiposHabitacion() != null) {
            for (TipoHabitacion tipo : propiedad.getTiposHabitacion()) {
                if (tipo.getHabitaciones() != null) {
                    for (Habitacion habitacion : tipo.getHabitaciones()) {
                        cantidadReservas += reservaRepository.findByHabitacion_IdHabitacion(habitacion.getIdHabitacion()).size();
                    }
                }
            }
        }
        dto.setCantidadReservas(cantidadReservas);
        
        return dto;
    }

    private ReservaAdminResponseDTO convertirReservaADTO(Reserva reserva) {
        ReservaAdminResponseDTO dto = new ReservaAdminResponseDTO();
        dto.setIdReserva(reserva.getIdReserva());
        dto.setFechaEntrada(reserva.getFechaEntrada());
        dto.setFechaSalida(reserva.getFechaSalida());
        dto.setMontoTotal(reserva.getMontoTotal());
        dto.setEstadoReserva(reserva.getEstadoReserva() != null ? reserva.getEstadoReserva().name() : null);
        dto.setFechaCreacion(reserva.getFechaRegistro());
        
        if (reserva.getFechaEntrada() != null && reserva.getFechaSalida() != null) {
            long noches = java.time.temporal.ChronoUnit.DAYS.between(reserva.getFechaEntrada(), reserva.getFechaSalida());
            dto.setNoches((int) noches);
        }
        
        if (reserva.getCliente() != null) {
            dto.setNombreCliente(reserva.getCliente().getNombreCompleto());
            dto.setIdCliente(reserva.getCliente().getIdUsuario());
        }
        
        if (reserva.getHabitacion() != null && reserva.getHabitacion().getPropiedad() != null) {
            dto.setNombrePropiedad(reserva.getHabitacion().getPropiedad().getNombreComercial());
            dto.setIdPropiedad(reserva.getHabitacion().getPropiedad().getIdPropiedad());
        }
        
        return dto;
    }

    private BloqueoAdminResponseDTO convertirBloqueoADTO(BloqueoHabitacion bloqueo) {
        BloqueoAdminResponseDTO dto = new BloqueoAdminResponseDTO();
        dto.setIdBloqueo(bloqueo.getIdBloqueoHabitacion());
        
        if (bloqueo.getPropiedad() != null) {
            dto.setNivelBloqueo("PROPIEDAD");
            dto.setNombrePropiedad(bloqueo.getPropiedad().getNombreComercial());
            dto.setIdAfectado(bloqueo.getPropiedad().getIdPropiedad());
        } else if (bloqueo.getTipoHabitacion() != null) {
            dto.setNivelBloqueo("CATEGORIA");
            dto.setIdAfectado(bloqueo.getTipoHabitacion().getIdTipoHabitacion());
            if (bloqueo.getTipoHabitacion().getPropiedad() != null) {
                dto.setNombrePropiedad(bloqueo.getTipoHabitacion().getPropiedad().getNombreComercial());
            }
        } else if (bloqueo.getHabitacion() != null) {
            dto.setNivelBloqueo("CUARTO");
            dto.setIdAfectado(bloqueo.getHabitacion().getIdHabitacion());
            if (bloqueo.getHabitacion().getPropiedad() != null) {
                dto.setNombrePropiedad(bloqueo.getHabitacion().getPropiedad().getNombreComercial());
            }
        } else {
            dto.setNivelBloqueo("GLOBAL");
        }
        
        dto.setFechaInicio(bloqueo.getFechaInicio());
        dto.setFechaFin(bloqueo.getFechaFin());
        dto.setMotivo(bloqueo.getMotivo());
        
        return dto;
    }
}