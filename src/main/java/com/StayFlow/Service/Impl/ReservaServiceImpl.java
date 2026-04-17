package com.StayFlow.Service.Impl;

import com.StayFlow.Repository.BloqueoHabitacionRepository;
import com.StayFlow.Repository.HabitacionRepository;
import com.StayFlow.Repository.PrecioTemporadaRepository;
import com.StayFlow.Repository.ReservaRepository;
import com.StayFlow.Repository.UsuarioRepository;
import com.StayFlow.Service.Interfaces.IReservaService;
import com.StayFlow.dto.request.ReservaRequestDTO;
import com.StayFlow.dto.response.DisponibilidadResponseDTO;
import com.StayFlow.dto.response.ReservaResponseDTO;
import com.StayFlow.exception.BusinessException;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.model.BloqueoHabitacion;
import com.StayFlow.model.Habitacion;
import com.StayFlow.model.PrecioTemporada;
import com.StayFlow.model.Reserva;
import com.StayFlow.model.TipoHabitacion;
import com.StayFlow.model.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    // Crear una nueva reserva validando fechas, bloqueos y traslapes.
    @Override
    @Transactional
    public ReservaResponseDTO crearReserva(ReservaRequestDTO request) {

        // Validar que las fechas existan y tengan sentido.
        validarFechas(request.getFechaEntrada(), request.getFechaSalida());

        // Buscar habitación existente.
        Habitacion habitacion = habitacionRepository
                .findByIdHabitacionAndEstaEliminadoFalse(request.getIdHabitacion())
                .orElseThrow(() -> new ResourceNotFoundException("Habitación", "id", request.getIdHabitacion()));

        // Buscar cliente existente.
        // Si tu UsuarioRepository tiene un método específico para no eliminados,
        // aquí se puede cambiar después.
        Usuario cliente = usuarioRepository
                .findById(request.getIdCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getIdCliente()));

        // Validar disponibilidad antes de guardar.
        validarDisponibilidadInterna(
                habitacion.getIdHabitacion(),
                request.getFechaEntrada(),
                request.getFechaSalida()
        );

        // Calcular el monto total de la estancia.
        BigDecimal montoTotal = calcularMontoTotal(
                habitacion.getTipoHabitacion(),
                request.getFechaEntrada(),
                request.getFechaSalida()
        );

        // Crear la entidad de reserva.
        Reserva reserva = new Reserva();
        reserva.setHabitacion(habitacion);
        reserva.setCliente(cliente);
        reserva.setFechaEntrada(request.getFechaEntrada());
        reserva.setFechaSalida(request.getFechaSalida());
        reserva.setMontoTotal(montoTotal);
        reserva.setEstadoReserva(Reserva.EstadoReserva.pendiente);

        // Guardar y responder en formato DTO.
        return toReservaResponseDTO(reservaRepository.save(reserva));
    }

    // Obtener una reserva puntual por su id.
    @Override
    @Transactional(readOnly = true)
    public ReservaResponseDTO obtenerReservaPorId(Integer idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", "id", idReserva));

        return toReservaResponseDTO(reserva);
    }

    // Obtener todas las reservas de un cliente.
    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> obtenerReservasPorCliente(Integer idCliente) {

        // Validar que el cliente exista.
        usuarioRepository.findById(idCliente)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", idCliente));

        List<Reserva> reservas = reservaRepository.findByCliente_IdUsuario(idCliente);
        List<ReservaResponseDTO> respuesta = new ArrayList<>();

        for (Reserva reserva : reservas) {
            respuesta.add(toReservaResponseDTO(reserva));
        }

        return respuesta;
    }

    // Cancelar una reserva si aún no está cancelada.
    @Override
    @Transactional
    public ReservaResponseDTO cancelarReserva(Integer idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", "id", idReserva));

        // Evitar cancelar dos veces la misma reserva.
        if (reserva.getEstadoReserva() == Reserva.EstadoReserva.cancelada) {
            throw new BusinessException("La reserva ya está cancelada");
        }

        // No permitir cancelar reservas ya finalizadas.
        if (reserva.getEstadoReserva() == Reserva.EstadoReserva.check_out) {
            throw new BusinessException("No se puede cancelar una reserva ya finalizada");
        }

        reserva.setEstadoReserva(Reserva.EstadoReserva.cancelada);

        return toReservaResponseDTO(reservaRepository.save(reserva));
    }

    // Revisar si una habitación está disponible en cierto rango.
    @Override
    @Transactional(readOnly = true)
    public DisponibilidadResponseDTO verificarDisponibilidad(Integer idHabitacion,
                                                             LocalDate fechaEntrada,
                                                             LocalDate fechaSalida) {

        // Validar datos básicos.
        validarFechas(fechaEntrada, fechaSalida);

        // Validar que la habitación exista.
        habitacionRepository.findByIdHabitacionAndEstaEliminadoFalse(idHabitacion)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación", "id", idHabitacion));

        DisponibilidadResponseDTO response = new DisponibilidadResponseDTO();
        response.setIdHabitacion(idHabitacion);
        response.setFechaEntrada(fechaEntrada);
        response.setFechaSalida(fechaSalida);

        // Revisar bloqueos.
        List<BloqueoHabitacion> bloqueos = bloqueoHabitacionRepository
                .findByHabitacion_IdHabitacionAndFechaInicioLessThanAndFechaFinGreaterThan(
                        idHabitacion,
                        fechaSalida,
                        fechaEntrada
                );

        if (!bloqueos.isEmpty()) {
            response.setDisponible(false);
            response.setMensaje("La habitación está bloqueada en ese rango de fechas");
            return response;
        }

        // Revisar traslapes con otras reservas activas.
        List<Reserva> traslapes = reservaRepository
                .findByHabitacion_IdHabitacionAndEstadoReservaNotAndFechaEntradaLessThanAndFechaSalidaGreaterThan(
                        idHabitacion,
                        Reserva.EstadoReserva.cancelada,
                        fechaSalida,
                        fechaEntrada
                );

        if (!traslapes.isEmpty()) {
            response.setDisponible(false);
            response.setMensaje("La habitación ya tiene una reserva activa en ese rango de fechas");
            return response;
        }

        response.setDisponible(true);
        response.setMensaje("La habitación está disponible");
        return response;
    }

    // Validar fechas obligatorias y orden lógico.
    private void validarFechas(LocalDate fechaEntrada, LocalDate fechaSalida) {

        if (fechaEntrada == null || fechaSalida == null) {
            throw new BusinessException("Las fechas de entrada y salida son obligatorias");
        }

        if (!fechaEntrada.isBefore(fechaSalida)) {
            throw new BusinessException("La fecha de entrada debe ser anterior a la fecha de salida");
        }

        if (fechaEntrada.isBefore(LocalDate.now())) {
            throw new BusinessException("No se puede reservar con fecha de entrada en el pasado");
        }
    }

    // Validar que no existan bloqueos ni reservas activas cruzadas.
    private void validarDisponibilidadInterna(Integer idHabitacion,
                                              LocalDate fechaEntrada,
                                              LocalDate fechaSalida) {

        List<BloqueoHabitacion> bloqueos = bloqueoHabitacionRepository
                .findByHabitacion_IdHabitacionAndFechaInicioLessThanAndFechaFinGreaterThan(
                        idHabitacion,
                        fechaSalida,
                        fechaEntrada
                );

        if (!bloqueos.isEmpty()) {
            throw new BusinessException("La habitación está bloqueada para las fechas seleccionadas");
        }

        List<Reserva> traslapes = reservaRepository
                .findByHabitacion_IdHabitacionAndEstadoReservaNotAndFechaEntradaLessThanAndFechaSalidaGreaterThan(
                        idHabitacion,
                        Reserva.EstadoReserva.cancelada,
                        fechaSalida,
                        fechaEntrada
                );

        if (!traslapes.isEmpty()) {
            throw new BusinessException("La habitación ya tiene una reserva activa en ese rango de fechas");
        }
    }

    // Calcular el monto total día por día.
    // Si hay precio de temporada para un día, se usa ese.
    // Si no hay precio de temporada, se usa el precio base del tipo de habitación.
    private BigDecimal calcularMontoTotal(TipoHabitacion tipoHabitacion,
                                          LocalDate fechaEntrada,
                                          LocalDate fechaSalida) {

        if (tipoHabitacion == null) {
            throw new BusinessException("La habitación no tiene un tipo de habitación asociado");
        }

        if (tipoHabitacion.getPrecioBaseNoche() == null) {
            throw new BusinessException("El tipo de habitación no tiene precio base por noche");
        }

        BigDecimal total = BigDecimal.ZERO;
        LocalDate fechaActual = fechaEntrada;

        while (fechaActual.isBefore(fechaSalida)) {
            total = total.add(obtenerPrecioPorNoche(tipoHabitacion, fechaActual));
            fechaActual = fechaActual.plusDays(1);
        }

        return total;
    }

    // Obtener el precio aplicable para una fecha concreta.
    private BigDecimal obtenerPrecioPorNoche(TipoHabitacion tipoHabitacion, LocalDate fecha) {

        List<PrecioTemporada> preciosTemporada = precioTemporadaRepository
                .findByTipoHabitacion_IdTipoHabitacionAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqualAndEstaEliminadoFalse(
                        tipoHabitacion.getIdTipoHabitacion(),
                        fecha,
                        fecha
                );

        if (!preciosTemporada.isEmpty()) {
            return preciosTemporada.get(0).getPrecioEspecial();
        }

        return tipoHabitacion.getPrecioBaseNoche();
    }

    // Convertir la entidad Reserva a DTO de respuesta.
    private ReservaResponseDTO toReservaResponseDTO(Reserva reserva) {
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

    // Armar nombre completo del cliente para respuesta.
    private String construirNombreCompleto(Usuario usuario) {
        StringBuilder nombreCompleto = new StringBuilder();

        if (usuario.getNombre() != null) {
            nombreCompleto.append(usuario.getNombre());
        }

        if (usuario.getApellidoPaterno() != null) {
            if (!nombreCompleto.isEmpty()) {
                nombreCompleto.append(" ");
            }
            nombreCompleto.append(usuario.getApellidoPaterno());
        }

        if (usuario.getApellidoMaterno() != null) {
            if (!nombreCompleto.isEmpty()) {
                nombreCompleto.append(" ");
            }
            nombreCompleto.append(usuario.getApellidoMaterno());
        }

        return nombreCompleto.toString().trim();
    }
}