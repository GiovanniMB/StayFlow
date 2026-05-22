package com.StayFlow.service.Impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.StayFlow.service.interfaces.ILogSistemaService;
import com.StayFlow.service.interfaces.IReservaService;
import com.StayFlow.dto.request.ReservaRequestDTO;
import com.StayFlow.dto.response.DisponibilidadResponseDTO;
import com.StayFlow.dto.response.ReservaResponseDTO;
import com.StayFlow.exception.BusinessException;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.model.BloqueoHabitacion;
import com.StayFlow.model.Habitacion;
import com.StayFlow.model.PrecioTemporada;
import com.StayFlow.model.Reserva;
import com.StayFlow.model.Reserva.EstadoReserva;
import com.StayFlow.model.TipoHabitacion;
import com.StayFlow.model.Usuario;
import com.StayFlow.repository.BloqueoHabitacionRepository;
import com.StayFlow.repository.HabitacionRepository;
import com.StayFlow.repository.PrecioTemporadaRepository;
import com.StayFlow.repository.ReservaRepository;
import com.StayFlow.repository.UsuarioRepository;
import com.StayFlow.mapper.ReservaMapper;


@Service
public class ReservaServiceImpl implements IReservaService {

    private final ReservaRepository reservaRepository;
    private final BloqueoHabitacionRepository bloqueoHabitacionRepository;
    private final PrecioTemporadaRepository precioTemporadaRepository;
    private final HabitacionRepository habitacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ILogSistemaService logSistemaService;
    private final ReservaMapper reservaMapper;
    private final com.StayFlow.service.interfaces.IPagoService pagoService;          

    public ReservaServiceImpl(ReservaRepository reservaRepository,
                              HabitacionRepository habitacionRepository,
                              PrecioTemporadaRepository precioTemporadaRepository,
                              BloqueoHabitacionRepository bloqueoHabitacionRepository,
                              UsuarioRepository usuarioRepository,
                              ILogSistemaService logSistemaService, 
                              ReservaMapper reservaMapper,
                              com.StayFlow.service.interfaces.IPagoService pagoService) {      
        this.reservaRepository = reservaRepository;
        this.habitacionRepository = habitacionRepository;
        this.precioTemporadaRepository = precioTemporadaRepository;
        this.bloqueoHabitacionRepository = bloqueoHabitacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.logSistemaService = logSistemaService;
        this.reservaMapper = reservaMapper;         
        this.pagoService = pagoService;
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

        // Guarda en la Base de Datos
        Reserva reservaGuardada = reservaRepository.save(reserva);

        //Marca la habitación como reservada (opcional, dependiendo de la lógica de negocio)
        logSistemaService.registrarLog("reserva", reservaGuardada.getIdReserva(), com.StayFlow.model.LogSistema.Accion.INSERT);

        // Devuelve la respuesta usando el mapper
        return reservaMapper.toResponseDTO(reservaGuardada);
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
        return reservaMapper.toResponseDTO(reservaRepository.save(reserva));
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
        return reservaMapper.toResponseDTO(reservaRepository.save(reserva));
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaResponseDTO obtenerReservaPorId(Integer idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + idReserva));
        return reservaMapper.toResponseDTO(reserva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> obtenerReservasPorCliente(Integer idCliente) {
        if (!usuarioRepository.existsById(idCliente)) {
            throw new ResourceNotFoundException("Cliente no encontrado.");
        }
        return reservaRepository.findByCliente_IdUsuario(idCliente).stream()
                .map(reservaMapper::toResponseDTO)
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

        // Comprobacion en back end para la logica de los reembolsos segun la politica de cancelacion (Zona Verde, Amarilla, Roja)
        long diasFaltantes = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), reserva.getFechaEntrada());
        java.math.BigDecimal porcentajeReembolso;

        if (diasFaltantes >= 5) {
            porcentajeReembolso = new java.math.BigDecimal("1.00"); // 100% (Zona Verde)
        } else if (diasFaltantes >= 2 && diasFaltantes <= 4) {
            porcentajeReembolso = new java.math.BigDecimal("0.50"); // 50% (Zona Amarilla)
        } else {
            porcentajeReembolso = java.math.BigDecimal.ZERO;        // 0% (Zona Roja)
        }

       // Calcula el dinero real a devolver
        java.math.BigDecimal montoAReembolsar = reserva.getMontoTotal().multiply(porcentajeReembolso);

        // Procesamiento del reembolso (Solo si le toca dinero de vuelta)
        if (montoAReembolsar.compareTo(java.math.BigDecimal.ZERO) > 0) {
            try {
                pagoService.reembolsarPorReserva(reserva.getIdReserva(), montoAReembolsar);
                System.out.println("✅ Reembolso exitoso: Se devolvieron $" + montoAReembolsar + " MXN al cliente.");
            } catch (Exception e) {
                // Si el reembolso falla (ej. banco rechazado), detenemos la cancelación
                throw new BusinessException("No se pudo procesar el reembolso en el banco: " + e.getMessage());
            }
        } else {
            System.out.println("🛑 Cancelación en Zona Roja. No hay reembolso aplicable ($0.00).");
        }

        // Libera la habitación
        Habitacion habitacion = reserva.getHabitacion();
        habitacion.setEstado(Habitacion.EstadoHabitacion.disponible);
        habitacionRepository.save(habitacion);

        // Cancela la reserva
        reserva.setEstadoReserva(EstadoReserva.cancelada);
        logSistemaService.registrarLog("reserva", reserva.getIdReserva(), com.StayFlow.model.LogSistema.Accion.UPDATE);

        return reservaMapper.toResponseDTO(reservaRepository.save(reserva));
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

    //Metodos auxiliares privados para validaciones, cálculos y conversiones

    private void validarFechas(LocalDate fechaEntrada, LocalDate fechaSalida) {
        if (fechaEntrada == null || fechaSalida == null) throw new BusinessException("Fechas inválidas.");
        if (fechaEntrada.isBefore(LocalDate.now())) throw new BusinessException("No se permiten reservas en el pasado.");
        if (!fechaEntrada.isBefore(fechaSalida)) throw new BusinessException("La fecha de salida debe ser después de la entrada.");
    }

    private void validarDisponibilidadInterna(Integer idHabitacion, LocalDate fechaEntrada, LocalDate fechaSalida) {
        // 1. Extrae la habitación para conocer su jerarquía (a qué Categoría y Propiedad pertenece)
        Habitacion habitacion = habitacionRepository.findById(idHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada."));

        Integer idTipo = habitacion.getTipoHabitacion().getIdTipoHabitacion();
        Integer idPropiedad = habitacion.getPropiedad().getIdPropiedad();

        // 2. GR agrego esta nueva regla: Bloqueos de Mantenimiento en 3 Niveles
        List<BloqueoHabitacion> bloqueosConflictivos = bloqueoHabitacionRepository.findBloqueosConflictivos(
                idHabitacion, 
                idTipo, 
                idPropiedad, 
                fechaEntrada, 
                fechaSalida
        );

        if (!bloqueosConflictivos.isEmpty()) {
            throw new BusinessException("No es posible reservar. El alojamiento, categoría o cuarto físico se encuentra en mantenimiento durante esas fechas.");
        }

        // 3. Conflictos con otras reservas (Solo busca activas, no canceladas)
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


    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> obtenerReservasPorHabitacion(Integer idHabitacion) {
        // Obtiene las reservas usando el repositorio
        List<Reserva> reservas = reservaRepository.findByHabitacion_IdHabitacion(idHabitacion);
        
        // Convierte la lista de entidades a una lista de DTOs usando el mapper
        return reservaMapper.toResponseDTOList(reservas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> obtenerReservasPorPropiedad(Integer idPropiedad) {
        List<Reserva> reservas = reservaRepository.findByHabitacion_TipoHabitacion_Propiedad_IdPropiedad(idPropiedad);
        return reservaMapper.toResponseDTOList(reservas);
    }
    
    @Override
    @Transactional
    public void actualizarEstadoReserva(Integer idReserva, EstadoReserva nuevoEstado) {
        Reserva reserva = reservaRepository.findById(idReserva)
             .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        reserva.setEstadoReserva(nuevoEstado);
        reservaRepository.save(reserva);
 }
}