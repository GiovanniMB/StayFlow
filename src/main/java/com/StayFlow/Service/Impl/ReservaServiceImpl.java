package com.StayFlow.Service.Impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.StayFlow.Repository.BloqueoHabitacionRepository;
import com.StayFlow.Repository.HabitacionRepository;
import com.StayFlow.Repository.PrecioTemporadaRepository;
import com.StayFlow.Repository.ReservaRepository;
import com.StayFlow.Repository.UsuarioRepository;
import com.StayFlow.Service.Interfaces.IReservaService;
import com.StayFlow.dto.request.ReservaRequestDTO;
import com.StayFlow.dto.response.CancelacionReservaResponseDTO;
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

        // Busca la habitación con bloqueo pesimista para evitar doble reserva simultánea.
        Habitacion habitacion = habitacionRepository.findByIdHabitacionForUpdate(request.getIdHabitacion())
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no disponible o eliminada."));

        // Valida que la cantidad de huéspedes no exceda la capacidad del tipo de habitación.
        if (request.getCantidadHuespedes() > habitacion.getTipoHabitacion().getCapacidad()) {
            throw new BusinessException("La cantidad de huéspedes excede la capacidad de la habitación ("
                    + habitacion.getTipoHabitacion().getCapacidad() + ").");
        }

        Usuario cliente = usuarioRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + request.getIdCliente()));

        validarDisponibilidadInterna(
                habitacion.getIdHabitacion(),
                request.getFechaEntrada(),
                request.getFechaSalida()
        );

        BigDecimal montoTotal = calcularMontoTotal(
                habitacion.getTipoHabitacion(),
                request.getFechaEntrada(),
                request.getFechaSalida()
        );

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

        return reservaRepository.findByCliente_IdUsuario(idCliente)
                .stream()
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
    @Transactional
    public CancelacionReservaResponseDTO cancelarReservaConPenalizacion(Integer idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada."));

        if (reserva.getEstadoReserva() == EstadoReserva.cancelada) {
            throw new BusinessException("La reserva ya está cancelada.");
        }

        if (reserva.getEstadoReserva() == EstadoReserva.check_in) {
            throw new BusinessException("No se puede cancelar una reserva con check-in realizado.");
        }

        if (reserva.getEstadoReserva() == EstadoReserva.check_out) {
            throw new BusinessException("No se puede cancelar una reserva finalizada.");
        }

        if (reserva.getFechaEntrada() == null) {
            throw new BusinessException("La reserva no tiene fecha de entrada registrada.");
        }

        BigDecimal montoTotal = reserva.getMontoTotal() != null
                ? reserva.getMontoTotal()
                : BigDecimal.ZERO;

        Integer porcentajePenalizacion = calcularPorcentajePenalizacion(reserva.getFechaEntrada());
        BigDecimal montoPenalizacion = calcularMontoPenalizacion(montoTotal, porcentajePenalizacion);
        BigDecimal montoReembolso = montoTotal.subtract(montoPenalizacion);

        reserva.setEstadoReserva(EstadoReserva.cancelada);

        Reserva reservaCancelada = reservaRepository.save(reserva);

        return new CancelacionReservaResponseDTO(
                reservaCancelada.getIdReserva(),
                reservaCancelada.getEstadoReserva().name(),
                reservaCancelada.getFechaEntrada(),
                reservaCancelada.getFechaSalida(),
                montoTotal,
                porcentajePenalizacion,
                montoPenalizacion,
                montoReembolso,
                construirMensajeCancelacion(porcentajePenalizacion, montoPenalizacion, montoReembolso)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public DisponibilidadResponseDTO verificarDisponibilidad(Integer idHabitacion,
                                                             LocalDate fechaEntrada,
                                                             LocalDate fechaSalida) {
        try {
            validarFechas(fechaEntrada, fechaSalida);

            if (!habitacionRepository.existsById(idHabitacion)) {
                throw new ResourceNotFoundException("Habitación no encontrada.");
            }

            validarDisponibilidadInterna(idHabitacion, fechaEntrada, fechaSalida);

            return new DisponibilidadResponseDTO(
                    idHabitacion,
                    fechaEntrada,
                    fechaSalida,
                    true,
                    "Disponible"
            );
        } catch (BusinessException e) {
            return new DisponibilidadResponseDTO(
                    idHabitacion,
                    fechaEntrada,
                    fechaSalida,
                    false,
                    e.getMessage()
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> obtenerReservasPorHabitacion(Integer idHabitacion) {
        List<Reserva> reservas = reservaRepository.findByHabitacion_IdHabitacion(idHabitacion);

        return reservas.stream()
                .filter(r -> r.getEstadoReserva() != EstadoReserva.cancelada)
                .map(this::toReservaResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> obtenerReservasPorPropiedad(Integer idPropiedad) {
        List<Reserva> reservas = reservaRepository.findByPropiedadId(idPropiedad);

        return reservas.stream()
                .map(this::toReservaResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> obtenerReservasPorAnfitrion(Integer idAnfitrion) {
        List<Reserva> reservas = reservaRepository.findByAnfitrionId(idAnfitrion);

        return reservas.stream()
                .map(this::toReservaResponseDTO)
                .collect(Collectors.toList());
    }

    // Valida que las fechas sean correctas para una nueva reserva.
    private void validarFechas(LocalDate fechaEntrada, LocalDate fechaSalida) {
        if (fechaEntrada == null || fechaSalida == null) {
            throw new BusinessException("Fechas inválidas.");
        }

        if (fechaEntrada.isBefore(LocalDate.now())) {
            throw new BusinessException("No se permiten reservas en el pasado.");
        }

        if (!fechaEntrada.isBefore(fechaSalida)) {
            throw new BusinessException("La fecha de salida debe ser después de la entrada.");
        }
    }

    // Valida bloqueos administrativos y cruces con reservas existentes.
    private void validarDisponibilidadInterna(Integer idHabitacion,
                                              LocalDate fechaEntrada,
                                              LocalDate fechaSalida) {
        if (!bloqueoHabitacionRepository
                .findByHabitacion_IdHabitacionAndFechaInicioLessThanAndFechaFinGreaterThan(
                        idHabitacion,
                        fechaSalida,
                        fechaEntrada
                )
                .isEmpty()) {
            throw new BusinessException("La habitación tiene un bloqueo activo en esas fechas.");
        }

        if (!reservaRepository
                .findByHabitacion_IdHabitacionAndEstadoReservaNotAndFechaEntradaLessThanAndFechaSalidaGreaterThan(
                        idHabitacion,
                        EstadoReserva.cancelada,
                        fechaSalida,
                        fechaEntrada
                )
                .isEmpty()) {
            throw new BusinessException("Ya existe una reserva para esas fechas.");
        }
    }

    // Calcula el monto total sumando el precio por noche.
    private BigDecimal calcularMontoTotal(TipoHabitacion tipo, LocalDate inicio, LocalDate fin) {
        BigDecimal total = BigDecimal.ZERO;

        LocalDate actual = inicio;

        while (actual.isBefore(fin)) {
            total = total.add(obtenerPrecioPorNoche(tipo, actual));
            actual = actual.plusDays(1);
        }

        return total;
    }

    // Obtiene el precio especial si hay temporada; si no, usa precio base.
    private BigDecimal obtenerPrecioPorNoche(TipoHabitacion tipo, LocalDate fecha) {
        List<PrecioTemporada> precios = precioTemporadaRepository
                .findByTipoHabitacion_IdTipoHabitacionAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqualAndEstaEliminadoFalse(
                        tipo.getIdTipoHabitacion(),
                        fecha,
                        fecha
                );

        return !precios.isEmpty()
                ? precios.get(0).getPrecioEspecial()
                : tipo.getPrecioBaseNoche();
    }

    // Convierte Reserva a DTO enriquecido para que el frontend pueda mostrar tarjetas completas.
    private ReservaResponseDTO toReservaResponseDTO(Reserva reserva) {
        ReservaResponseDTO dto = new ReservaResponseDTO();

        dto.setIdReserva(reserva.getIdReserva());
        dto.setFechaEntrada(reserva.getFechaEntrada());
        dto.setFechaSalida(reserva.getFechaSalida());
        dto.setMontoTotal(reserva.getMontoTotal());

        if (reserva.getEstadoReserva() != null) {
            dto.setEstadoReserva(reserva.getEstadoReserva().name());
        }

        if (reserva.getHabitacion() != null) {
            Habitacion habitacion = reserva.getHabitacion();

            dto.setIdHabitacion(habitacion.getIdHabitacion());
            dto.setNumeroHabitacion(habitacion.getNumeroHabitacion());

            if (habitacion.getPropiedad() != null) {
                dto.setIdPropiedad(habitacion.getPropiedad().getIdPropiedad());
                dto.setNombrePropiedad(habitacion.getPropiedad().getNombreComercial());
                dto.setUrlFotoPropiedad(obtenerUrlFotoPropiedad(reserva));
            }
        }

        if (reserva.getCliente() != null) {
            dto.setIdCliente(reserva.getCliente().getIdUsuario());
            dto.setNombreCliente(construirNombreUsuario(reserva.getCliente()));
        }

        return dto;
    }

  
    // Obtiene una imagen de la propiedad asociada a la reserva.
    private String obtenerUrlFotoPropiedad(Reserva reserva) {
    if (reserva.getHabitacion() == null ||
            reserva.getHabitacion().getPropiedad() == null ||
            reserva.getHabitacion().getPropiedad().getFotos() == null ||
            reserva.getHabitacion().getPropiedad().getFotos().isEmpty()) {
        return null;
    }

    return reserva.getHabitacion()
            .getPropiedad()
            .getFotos()
            .get(0)
            .getUrlFoto();
}

    // Construye el nombre completo del usuario sin depender de Lombok.
    private String construirNombreUsuario(Usuario usuario) {
        StringBuilder nombreCompleto = new StringBuilder();

        if (usuario.getNombre() != null) {
            nombreCompleto.append(usuario.getNombre());
        }

        if (usuario.getApellidoPaterno() != null) {
            if (nombreCompleto.length() > 0) {
                nombreCompleto.append(" ");
            }
            nombreCompleto.append(usuario.getApellidoPaterno());
        }

        if (usuario.getApellidoMaterno() != null) {
            if (nombreCompleto.length() > 0) {
                nombreCompleto.append(" ");
            }
            nombreCompleto.append(usuario.getApellidoMaterno());
        }

        return nombreCompleto.toString().trim();
    }

    // Calcula el porcentaje de penalización según la cercanía con la fecha de entrada.
    private Integer calcularPorcentajePenalizacion(LocalDate fechaEntrada) {
        long diasAntes = ChronoUnit.DAYS.between(LocalDate.now(), fechaEntrada);

        if (diasAntes > 7) {
            return 0;
        }

        if (diasAntes >= 3) {
            return 25;
        }

        if (diasAntes >= 1) {
            return 50;
        }

        return 100;
    }

    // Calcula el monto económico de la penalización.
    private BigDecimal calcularMontoPenalizacion(BigDecimal montoTotal, Integer porcentajePenalizacion) {
        return montoTotal
                .multiply(BigDecimal.valueOf(porcentajePenalizacion))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    // Mensaje claro para mostrarlo en frontend después de cancelar.
    private String construirMensajeCancelacion(Integer porcentajePenalizacion,
                                               BigDecimal montoPenalizacion,
                                               BigDecimal montoReembolso) {
        return "Reserva cancelada. Penalización aplicada: "
                + porcentajePenalizacion
                + "%. Monto penalizado: $"
                + montoPenalizacion
                + ". Reembolso estimado: $"
                + montoReembolso
                + ".";
    }
}