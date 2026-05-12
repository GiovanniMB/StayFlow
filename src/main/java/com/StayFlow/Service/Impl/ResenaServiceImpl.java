package com.StayFlow.Service.Impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.StayFlow.Repository.ResenaRepository;
import com.StayFlow.Repository.ReservaRepository;
import com.StayFlow.Repository.UsuarioRepository;
import com.StayFlow.Service.Interfaces.IResenaService;
import com.StayFlow.dto.request.ResenaRequestDTO;
import com.StayFlow.dto.response.ResenaPendienteResponseDTO;
import com.StayFlow.dto.response.ResenaResponseDTO;
import com.StayFlow.exception.BusinessException;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.model.Resena;
import com.StayFlow.model.Reserva;
import com.StayFlow.model.Reserva.EstadoReserva;
import com.StayFlow.model.Usuario;

@Service
public class ResenaServiceImpl implements IResenaService {

    private final ResenaRepository resenaRepository;
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;

    public ResenaServiceImpl(ResenaRepository resenaRepository,
                             ReservaRepository reservaRepository,
                             UsuarioRepository usuarioRepository) {
        this.resenaRepository = resenaRepository;
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResenaResponseDTO> obtenerResenasPorPropiedad(Integer idPropiedad) {
        return resenaRepository.findResenasByPropiedad(idPropiedad)
                .stream()
                .map(this::toResenaResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResenaResponseDTO> obtenerResenasSobreHuesped(Integer idUsuario) {
        return resenaRepository.findResenasSobreHuesped(idUsuario)
                .stream()
                .map(this::toResenaResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResenaResponseDTO guardarResena(ResenaRequestDTO request) {
        Reserva reserva = reservaRepository.findById(request.getIdReserva())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada."));

        Usuario autor = usuarioRepository.findById(request.getIdUsuarioAutor())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autor no encontrado."));

        validarFechaDeSalida(reserva);
        validarPuntuacion(request.getPuntuacion());
        validarDuplicado(request.getIdReserva(), request.getIdUsuarioAutor());
        validarAutorPuedeResenar(reserva, autor);

        Resena nuevaResena = new Resena(
                reserva,
                autor,
                request.getPuntuacion(),
                request.getComentario()
        );

        return toResenaResponseDTO(resenaRepository.save(nuevaResena));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResenaPendienteResponseDTO> obtenerPendientesCliente(Integer idCliente) {
        if (!usuarioRepository.existsById(idCliente)) {
            throw new ResourceNotFoundException("Cliente no encontrado.");
        }

        return reservaRepository.findByCliente_IdUsuario(idCliente)
                .stream()
                .filter(this::reservaTerminadaYNoCancelada)
                .filter(reserva -> !resenaRepository.existsByReserva_IdReservaAndUsuario_IdUsuario(
                        reserva.getIdReserva(),
                        idCliente
                ))
                .map(reserva -> toPendienteClienteDTO(reserva, idCliente))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResenaPendienteResponseDTO> obtenerPendientesPropietario(Integer idPropietario) {
        if (!usuarioRepository.existsById(idPropietario)) {
            throw new ResourceNotFoundException("Propietario no encontrado.");
        }

        return reservaRepository.findByAnfitrionId(idPropietario)
                .stream()
                .filter(this::reservaTerminadaYNoCancelada)
                .filter(reserva -> !resenaRepository.existsByReserva_IdReservaAndUsuario_IdUsuario(
                        reserva.getIdReserva(),
                        idPropietario
                ))
                .map(reserva -> toPendientePropietarioDTO(reserva, idPropietario))
                .collect(Collectors.toList());
    }

    private void validarFechaDeSalida(Reserva reserva) {
        if (reserva.getFechaSalida() == null) {
            throw new BusinessException("La reserva no tiene fecha de salida registrada.");
        }

        if (!reserva.getFechaSalida().isBefore(LocalDate.now())) {
            throw new BusinessException("Aún no puedes dejar una reseña. La estancia termina el: " + reserva.getFechaSalida());
        }
    }

    private void validarPuntuacion(Integer puntuacion) {
        if (puntuacion == null) {
            throw new BusinessException("La puntuación es obligatoria.");
        }

        if (puntuacion < 1 || puntuacion > 5) {
            throw new BusinessException("La puntuación debe estar entre 1 y 5.");
        }
    }

    private void validarDuplicado(Integer idReserva, Integer idUsuarioAutor) {
        if (resenaRepository.existsByReserva_IdReservaAndUsuario_IdUsuario(idReserva, idUsuarioAutor)) {
            throw new BusinessException("Ya has dejado una reseña para esta estancia.");
        }
    }

    private void validarAutorPuedeResenar(Reserva reserva, Usuario autor) {
        if (reserva.getCliente() == null) {
            throw new BusinessException("La reserva no tiene cliente asociado.");
        }

        if (reserva.getHabitacion() == null || reserva.getHabitacion().getPropiedad() == null) {
            throw new BusinessException("La reserva no tiene propiedad asociada.");
        }

        if (reserva.getHabitacion().getPropiedad().getDueno() == null) {
            throw new BusinessException("La propiedad no tiene dueño asociado.");
        }

        Integer idAutor = autor.getIdUsuario();
        Integer idCliente = reserva.getCliente().getIdUsuario();
        Integer idDueno = reserva.getHabitacion().getPropiedad().getDueno().getIdUsuario();

        boolean esCliente = idAutor.equals(idCliente);
        boolean esDueno = idAutor.equals(idDueno);

        if (!esCliente && !esDueno) {
            throw new BusinessException("Solo el huésped o el dueño de la propiedad pueden reseñar esta reserva.");
        }
    }

    private boolean reservaTerminadaYNoCancelada(Reserva reserva) {
        return reserva.getFechaSalida() != null
                && reserva.getFechaSalida().isBefore(LocalDate.now())
                && reserva.getEstadoReserva() != EstadoReserva.cancelada;
    }

    private ResenaResponseDTO toResenaResponseDTO(Resena resena) {
        ResenaResponseDTO dto = new ResenaResponseDTO();

        dto.setIdResena(resena.getIdResena());
        dto.setPuntuacion(resena.getPuntuacion());
        dto.setComentario(resena.getComentario());

        if (resena.getReserva() != null) {
            dto.setIdReserva(resena.getReserva().getIdReserva());
        }

        if (resena.getUsuario() != null) {
            dto.setIdUsuarioAutor(resena.getUsuario().getIdUsuario());
            dto.setNombreAutor(construirNombreCompleto(resena.getUsuario()));
        }

        dto.setTipoResena(determinarTipoResena(resena));

        return dto;
    }

    private String determinarTipoResena(Resena resena) {
        if (resena.getReserva() == null
                || resena.getReserva().getCliente() == null
                || resena.getUsuario() == null) {
            return "desconocida";
        }

        Integer idAutor = resena.getUsuario().getIdUsuario();
        Integer idCliente = resena.getReserva().getCliente().getIdUsuario();

        if (idAutor.equals(idCliente)) {
            return "propiedad";
        }

        return "huesped";
    }

    private ResenaPendienteResponseDTO toPendienteClienteDTO(Reserva reserva, Integer idCliente) {
        return new ResenaPendienteResponseDTO(
                reserva.getIdReserva(),
                reserva.getHabitacion().getPropiedad().getIdPropiedad(),
                reserva.getHabitacion().getPropiedad().getNombreComercial(),
                reserva.getCliente().getIdUsuario(),
                construirNombreCompleto(reserva.getCliente()),
                reserva.getFechaEntrada(),
                reserva.getFechaSalida(),
                "cliente_a_propiedad",
                "Tu estancia ya terminó. Puedes dejar una reseña de la propiedad."
        );
    }

    private ResenaPendienteResponseDTO toPendientePropietarioDTO(Reserva reserva, Integer idPropietario) {
        return new ResenaPendienteResponseDTO(
                reserva.getIdReserva(),
                reserva.getHabitacion().getPropiedad().getIdPropiedad(),
                reserva.getHabitacion().getPropiedad().getNombreComercial(),
                reserva.getCliente().getIdUsuario(),
                construirNombreCompleto(reserva.getCliente()),
                reserva.getFechaEntrada(),
                reserva.getFechaSalida(),
                "propietario_a_huesped",
                "La estancia del huésped ya terminó. Puedes dejar una reseña del usuario."
        );
    }

    private String construirNombreCompleto(Usuario usuario) {
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
}