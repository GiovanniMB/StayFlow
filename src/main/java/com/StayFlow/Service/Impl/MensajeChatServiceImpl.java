package com.StayFlow.service.impl;

import com.StayFlow.dto.request.MensajeChatRequestDTO;
import com.StayFlow.dto.response.MensajeChatResponseDTO;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.mapper.MensajeChatMapper;
import com.StayFlow.model.MensajeChat;
import com.StayFlow.model.Reserva;
import com.StayFlow.model.Usuario;
import com.StayFlow.repository.MensajeChatRepository;
import com.StayFlow.repository.ReservaRepository;
import com.StayFlow.repository.UsuarioRepository;
import com.StayFlow.service.interfaces.IMensajeChatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MensajeChatServiceImpl implements IMensajeChatService {

    private final MensajeChatRepository mensajeRepository;
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MensajeChatMapper mensajeMapper;

    public MensajeChatServiceImpl(MensajeChatRepository mensajeRepository, 
                                  ReservaRepository reservaRepository, 
                                  UsuarioRepository usuarioRepository, 
                                  MensajeChatMapper mensajeMapper) {
        this.mensajeRepository = mensajeRepository;
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.mensajeMapper = mensajeMapper;
    }

    @Override
    @Transactional
    public MensajeChatResponseDTO enviarMensaje(MensajeChatRequestDTO request) {
        // 1. Busca la reserva
        Reserva reserva = reservaRepository.findById(request.getIdReserva())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        // 2. Busca al remitente
        Usuario remitente = usuarioRepository.findById(request.getIdRemitente())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario remitente no encontrado"));

        // 3. Arma la entidad
        MensajeChat nuevoMensaje = new MensajeChat();
        nuevoMensaje.setReserva(reserva);
        nuevoMensaje.setRemitente(remitente);
        nuevoMensaje.setContenido(request.getContenido());
        // (La fecha y el 'leido = false' se autogeneran en el constructor/PrePersist de la Entidad)

        // 4. Guarda en BD y mapea la respuesta
        MensajeChat mensajeGuardado = mensajeRepository.save(nuevoMensaje);
        return mensajeMapper.toResponseDTO(mensajeGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MensajeChatResponseDTO> obtenerHistorialChat(Integer idReserva) {
        // Valida que la reserva exista
        if (!reservaRepository.existsById(idReserva)) {
            throw new ResourceNotFoundException("Reserva no encontrada");
        }

        // Busca  el historial ordenado y lo mapea a DTO
        List<MensajeChat> historial = mensajeRepository.findByReserva_IdReservaOrderByFechaEnvioAsc(idReserva);
        return mensajeMapper.toResponseDTOList(historial);
    }
}