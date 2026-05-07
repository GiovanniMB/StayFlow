package com.StayFlow.Service.Impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.StayFlow.Service.Interfaces.IReservaService;
import com.StayFlow.dto.request.ReservaRequestDTO;
import com.StayFlow.dto.response.DisponibilidadResponseDTO;
import com.StayFlow.dto.response.ReservaResponseDTO;
import com.StayFlow.exception.BusinessException;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.model.Habitacion;
import com.StayFlow.model.PrecioTemporada;
import com.StayFlow.model.Reserva;
import com.StayFlow.model.Reserva.EstadoReserva;
import com.StayFlow.model.TipoHabitacion;
import com.StayFlow.model.Usuario;
import com.StayFlow.Repository.BloqueoHabitacionRepository;
import com.StayFlow.Repository.HabitacionRepository;
import com.StayFlow.Repository.PrecioTemporadaRepository;
import com.StayFlow.Repository.ReservaRepository;
import com.StayFlow.Repository.UsuarioRepository;

@Service
public class ReservaServiceImpl implements IReservaService {

    private final ReservaRepository reservaRepository;
    private final BloqueoHabitacionRepository bloqueoHabitacionRepository;
    private final PrecioTemporadaRepository precioTemporadaRepository;
    private final HabitacionRepository habitacionRepository;
    private final UsuarioRepository usuarioRepository;

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

        // 1. REGLA: Borrado Lógico (Solo busca activas) y usa el nuevo método con bloqueo pesimista
        Habitacion habitacion = habitacionRepository.findByIdHabitacionForUpdate(request.getIdHabitacion())
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no disponible o eliminada."));

        // 2. REGLA: Validación de Capacidad
        if (request.getCantidadHuespedes() > habitacion.getTipoHabitacion().getCapacidad()) {
            throw new BusinessException("La cantidad de huéspedes excede la capacidad de la habitación (" 
                + habitacion.getTipoHabitacion().getCapacidad() + ").");
        }

        Usuario cliente = usuarioRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + request.getIdCliente()));

        validarDisponibilidadInterna(habitacion.getIdHabitacion(), request.getFechaEntrada(), request.getFechaSalida());

        BigDecimal montoTotal = calcularMontoTotal(habitacion.getTipoHabitacion(), request.getFechaEntrada(), request.getFechaSalida());

        Reserva reserva = new Reserva();
        reserva.setHabitacion(habitacion);
        reserva.setCliente(cliente);
        reserva.setFechaEntrada(request.getFechaEntrada());
        reserva.setFechaSalida(request.getFechaSalida());
        reserva.setMontoTotal(montoTotal);
        reserva.setEstadoReserva(EstadoReserva.pendiente);

        return toReservaResponseDTO(reservaRepository.save(reserva));
    }

    @Override
    @Transactional
    public ReservaResponseDTO registrarCheckIn(Integer idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada."));

        if (reserva.getEstadoReserva() != EstadoReserva.pendiente) {
            throw new BusinessException("Solo se puede hacer Check-In en reservas pendientes.");
        }

        // REGLA: Actualización de Estado de Habitación a OCUPADA
        reserva.setEstadoReserva(EstadoReserva.check_in);
        Habitacion habitacion = reserva.getHabitacion();
        habitacion.setEstado(Habitacion.EstadoHabitacion.ocupada);

        habitacionRepository.save(habitacion);
        return toReservaResponseDTO(reservaRepository.save(reserva));
    }

    @Override
    @Transactional
    public ReservaResponseDTO registrarCheckOut(Integer idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada."));

        if (reserva.getEstadoReserva() != EstadoReserva.check_in) {
            throw new BusinessException("No se puede hacer Check-Out si no se ha hecho Check-In.");
        }

        // REGLA: Actualización de Estado de Habitación a DISPONIBLE
        reserva.setEstadoReserva(EstadoReserva.check_out);
        Habitacion habitacion = reserva.getHabitacion();
        habitacion.setEstado(Habitacion.EstadoHabitacion.disponible);

        habitacionRepository.save(habitacion);
        return toReservaResponseDTO(reservaRepository.save(reserva));
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
            throw new ResourceNotFoundException("Cliente no encontrado.");
        }
        return reservaRepository.findByCliente_IdUsuario(idCliente).stream()
                .map(this::toReservaResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReservaResponseDTO cancelarReserva(Integer idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada."));

        if (reserva.getEstadoReserva() == EstadoReserva.cancelada) {
            throw new BusinessException("La reserva ya está cancelada.");
        }

        reserva.setEstadoReserva(EstadoReserva.cancelada);
        return toReservaResponseDTO(reservaRepository.save(reserva));
    }

    @Override
    @Transactional(readOnly = true)
    public DisponibilidadResponseDTO verificarDisponibilidad(Integer idHabitacion, LocalDate fechaEntrada, LocalDate fechaSalida) {
        try {
            validarFechas(fechaEntrada, fechaSalida);
            if (!habitacionRepository.existsById(idHabitacion)) {
                throw new ResourceNotFoundException("Habitación no encontrada.");
            }
            validarDisponibilidadInterna(idHabitacion, fechaEntrada, fechaSalida);
            return new DisponibilidadResponseDTO(idHabitacion, fechaEntrada, fechaSalida, true, "Disponible");
        } catch (BusinessException e) {
            return new DisponibilidadResponseDTO(idHabitacion, fechaEntrada, fechaSalida, false, e.getMessage());
        }
    }

    // --- MÉTODOS AUXILIARES ---

    private void validarFechas(LocalDate fechaEntrada, LocalDate fechaSalida) {
        if (fechaEntrada == null || fechaSalida == null) throw new BusinessException("Fechas inválidas.");
        if (fechaEntrada.isBefore(LocalDate.now())) throw new BusinessException("No se permiten reservas en el pasado.");
        if (!fechaEntrada.isBefore(fechaSalida)) throw new BusinessException("La fecha de salida debe ser después de la entrada.");
    }

    private void validarDisponibilidadInterna(Integer idHabitacion, LocalDate fechaEntrada, LocalDate fechaSalida) {
        // REGLA: Bloqueos de Habitación
        if (!bloqueoHabitacionRepository.findByHabitacion_IdHabitacionAndFechaInicioLessThanAndFechaFinGreaterThan(idHabitacion, fechaSalida, fechaEntrada).isEmpty()) {
            throw new BusinessException("La habitación tiene un bloqueo activo en esas fechas.");
        }
        // Conflictos con otras reservas
        if (!reservaRepository.findByHabitacion_IdHabitacionAndEstadoReservaNotAndFechaEntradaLessThanAndFechaSalidaGreaterThan(idHabitacion, EstadoReserva.cancelada, fechaSalida, fechaEntrada).isEmpty()) {
            throw new BusinessException("Ya existe una reserva para esas fechas.");
        }
    }

    private BigDecimal calcularMontoTotal(TipoHabitacion tipo, LocalDate inicio, LocalDate fin) {
        BigDecimal total = BigDecimal.ZERO;
        LocalDate actual = inicio;
        while (actual.isBefore(fin)) {
            total = total.add(obtenerPrecioPorNoche(tipo, actual));
            actual = actual.plusDays(1);
        }
        return total;
    }

    private BigDecimal obtenerPrecioPorNoche(TipoHabitacion tipo, LocalDate fecha) {
        List<PrecioTemporada> precios = precioTemporadaRepository.findByTipoHabitacion_IdTipoHabitacionAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqualAndEstaEliminadoFalse(tipo.getIdTipoHabitacion(), fecha, fecha);
        return !precios.isEmpty() ? precios.get(0).getPrecioEspecial() : tipo.getPrecioBaseNoche();
    }

    private ReservaResponseDTO toReservaResponseDTO(Reserva reserva) {
        ReservaResponseDTO dto = new ReservaResponseDTO();
        dto.setIdReserva(reserva.getIdReserva());
        if (reserva.getHabitacion() != null) {
            dto.setIdHabitacion(reserva.getHabitacion().getIdHabitacion());
            dto.setNumeroHabitacion(reserva.getHabitacion().getNumeroHabitacion());
        }
        if (reserva.getCliente() != null) {
            dto.setIdCliente(reserva.getCliente().getIdUsuario());
            dto.setNombreCliente(reserva.getCliente().getNombre() + " " + reserva.getCliente().getApellidoPaterno());
        }
        dto.setFechaEntrada(reserva.getFechaEntrada());
        dto.setFechaSalida(reserva.getFechaSalida());
        dto.setMontoTotal(reserva.getMontoTotal());
        if (reserva.getEstadoReserva() != null) dto.setEstadoReserva(reserva.getEstadoReserva().name());
        return dto;
    }
}