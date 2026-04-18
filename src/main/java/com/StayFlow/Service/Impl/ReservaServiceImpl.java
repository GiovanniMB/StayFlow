package com.StayFlow.Service.Impl;

import com.StayFlow.dto.request.ReservaRequestDTO;
import com.StayFlow.dto.response.DisponibilidadResponseDTO;
import com.StayFlow.dto.response.ReservaResponseDTO;
import com.StayFlow.exception.BusinessException;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.model.*;
import com.StayFlow.Repository.*;
import com.StayFlow.Service.Interfaces.IReservaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaServiceImpl implements IReservaService {

    // Dependencias inyectadas de forma nativa
    private final ReservaRepository reservaRepository;
    private final BloqueoHabitacionRepository bloqueoHabitacionRepository;
    private final PrecioTemporadaRepository precioTemporadaRepository;
    private final HabitacionRepository habitacionRepository;
    private final UsuarioRepository usuarioRepository;

    // Constructor nativo para inyección de dependencias (Reemplaza a @RequiredArgsConstructor)
    public ReservaServiceImpl(ReservaRepository reservaRepository,
                              BloqueoHabitacionRepository bloqueoHabitacionRepository,
                              PrecioTemporadaRepository precioTemporadaRepository,
                              HabitacionRepository habitacionRepository,
                              UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.bloqueoHabitacionRepository = bloqueoHabitacionRepository;
        this.precioTemporadaRepository = precioTemporadaRepository;
        this.habitacionRepository = habitacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public ReservaResponseDTO crearReserva(ReservaRequestDTO request) {
        validarFechas(request.getFechaEntrada(), request.getFechaSalida());

        Habitacion habitacion = habitacionRepository.findById(request.getIdHabitacion())
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con id: " + request.getIdHabitacion()));

        Usuario cliente = usuarioRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + request.getIdCliente()));

        validarDisponibilidadInterna(habitacion.getIdHabitacion(), request.getFechaEntrada(), request.getFechaSalida());

        BigDecimal montoTotal = calcularMontoTotal(habitacion.getTipoHabitacion(), request.getFechaEntrada(), request.getFechaSalida());

        // Creación de la entidad sin patrón Builder
        Reserva reserva = new Reserva();
        reserva.setHabitacion(habitacion);
        reserva.setCliente(cliente);
        reserva.setFechaEntrada(request.getFechaEntrada());
        reserva.setFechaSalida(request.getFechaSalida());
        reserva.setMontoTotal(montoTotal);
        reserva.setEstadoReserva(EstadoReserva.pendiente); // Asignación del estado inicial

        Reserva reservaGuardada = reservaRepository.save(reserva);

        return toReservaResponseDTO(reservaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaResponseDTO obtenerReservaPorId(Integer idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + idReserva));
        return toReservaResponseDTO(reserva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> obtenerReservasPorCliente(Integer idCliente) {
        if (!usuarioRepository.existsById(idCliente)) {
            throw new ResourceNotFoundException("Cliente no encontrado con id: " + idCliente);
        }

        List<Reserva> reservas = reservaRepository.findByCliente_IdUsuario(idCliente);
        
        return reservas.stream()
                .map(this::toReservaResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReservaResponseDTO cancelarReserva(Integer idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + idReserva));

        if (reserva.getEstadoReserva() == EstadoReserva.cancelada) {
            throw new BusinessException("La reserva ya se encuentra cancelada.");
        }

        if (reserva.getEstadoReserva() == EstadoReserva.check_out) {
            throw new BusinessException("No se puede cancelar una reserva que ya ha finalizado (check-out).");
        }

        reserva.setEstadoReserva(EstadoReserva.cancelada);
        Reserva reservaActualizada = reservaRepository.save(reserva);

        return toReservaResponseDTO(reservaActualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public DisponibilidadResponseDTO verificarDisponibilidad(Integer idHabitacion, LocalDate fechaEntrada, LocalDate fechaSalida) {
        try {
            validarFechas(fechaEntrada, fechaSalida);
            
            if (!habitacionRepository.existsById(idHabitacion)) {
                throw new ResourceNotFoundException("Habitación no encontrada con id: " + idHabitacion);
            }
            
            validarDisponibilidadInterna(idHabitacion, fechaEntrada, fechaSalida);
            
            // Si pasa las validaciones, está disponible (usando constructor nativo)
            return new DisponibilidadResponseDTO(idHabitacion, fechaEntrada, fechaSalida, true, "La habitación está disponible para las fechas solicitadas.");
            
        } catch (BusinessException e) {
            // Si hay conflicto de fechas o bloqueos, atrapamos la excepción de negocio para retornar el DTO en false
            return new DisponibilidadResponseDTO(idHabitacion, fechaEntrada, fechaSalida, false, e.getMessage());
        }
    }

    // --- MÉTODOS AUXILIARES ---

    private void validarFechas(LocalDate fechaEntrada, LocalDate fechaSalida) {
        if (fechaEntrada == null || fechaSalida == null) {
            throw new BusinessException("Las fechas de entrada y salida son obligatorias.");
        }
        if (fechaEntrada.isBefore(LocalDate.now())) {
            throw new BusinessException("La fecha de entrada no puede estar en el pasado.");
        }
        if (!fechaEntrada.isBefore(fechaSalida)) {
            throw new BusinessException("La fecha de entrada debe ser anterior a la fecha de salida.");
        }
    }

    private void validarDisponibilidadInterna(Integer idHabitacion, LocalDate fechaEntrada, LocalDate fechaSalida) {
        // Validar si hay bloqueos en esas fechas
        List<BloqueoHabitacion> bloqueos = bloqueoHabitacionRepository
                .findByHabitacion_IdHabitacionAndFechaInicioLessThanAndFechaFinGreaterThan(idHabitacion, fechaSalida, fechaEntrada);
        
        if (!bloqueos.isEmpty()) {
            throw new BusinessException("La habitación se encuentra bloqueada en las fechas seleccionadas.");
        }

        // Validar si hay reservas activas en esas fechas (excluyendo canceladas)
        List<Reserva> reservasConflictivas = reservaRepository
                .findByHabitacion_IdHabitacionAndEstadoReservaNotAndFechaEntradaLessThanAndFechaSalidaGreaterThan(
                        idHabitacion, EstadoReserva.cancelada, fechaSalida, fechaEntrada);
        
        if (!reservasConflictivas.isEmpty()) {
            throw new BusinessException("La habitación ya cuenta con una reserva activa para estas fechas.");
        }
    }

    private BigDecimal calcularMontoTotal(TipoHabitacion tipoHabitacion, LocalDate fechaEntrada, LocalDate fechaSalida) {
        BigDecimal montoTotal = BigDecimal.ZERO;
        LocalDate fechaActual = fechaEntrada;

        while (fechaActual.isBefore(fechaSalida)) {
            BigDecimal precioNoche = obtenerPrecioPorNoche(tipoHabitacion, fechaActual);
            montoTotal = montoTotal.add(precioNoche);
            fechaActual = fechaActual.plusDays(1);
        }

        return montoTotal;
    }

    private BigDecimal obtenerPrecioPorNoche(TipoHabitacion tipoHabitacion, LocalDate fecha) {
        // Busca si hay un precio especial de temporada para esta fecha
        List<PrecioTemporada> preciosTemporada = precioTemporadaRepository
                .findByTipoHabitacion_IdTipoHabitacionAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqualAndEstaEliminadoFalse(
                        tipoHabitacion.getIdTipoHabitacion(), fecha, fecha);

        if (!preciosTemporada.isEmpty()) {
            // Si hay un precio de temporada activo, usa el primero que encuentre
            return preciosTemporada.get(0).getPrecioEspecial();
        }

        // Si no hay precio de temporada, retorna el precio base
        return tipoHabitacion.getPrecioBaseNoche();
    }

    private ReservaResponseDTO toReservaResponseDTO(Reserva reserva) {
        // Mapeo manual usando el constructor con parámetros o setters (sin Builder)
        ReservaResponseDTO dto = new ReservaResponseDTO();
        
        dto.setIdReserva(reserva.getIdReserva());
        
        if (reserva.getHabitacion() != null) {
            dto.setIdHabitacion(reserva.getHabitacion().getIdHabitacion());
            dto.setNumeroHabitacion(reserva.getHabitacion().getNumeroHabitacion());
        }
        
        if (reserva.getCliente() != null) {
            dto.setIdCliente(reserva.getCliente().getIdUsuario());
            dto.setNombreCliente(construirNombreCompleto(reserva.getCliente()));
        }
        
        dto.setFechaEntrada(reserva.getFechaEntrada());
        dto.setFechaSalida(reserva.getFechaSalida());
        dto.setMontoTotal(reserva.getMontoTotal());
        
        if (reserva.getEstadoReserva() != null) {
            dto.setEstadoReserva(reserva.getEstadoReserva().name());
        }
        
        return dto;
    }

    private String construirNombreCompleto(Usuario usuario) {
        String nombre = usuario.getNombre() != null ? usuario.getNombre() : "";
        String paterno = usuario.getApellidoPaterno() != null ? usuario.getApellidoPaterno() : "";
        String materno = usuario.getApellidoMaterno() != null ? usuario.getApellidoMaterno() : "";
        
        return String.format("%s %s %s", nombre, paterno, materno).trim();
    }
}