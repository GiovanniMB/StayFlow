package com.StayFlow.service.interfaces;

import com.StayFlow.dto.request.MensajeChatRequestDTO;
import com.StayFlow.dto.response.MensajeChatResponseDTO;
import java.util.List;

public interface IMensajeChatService {
    MensajeChatResponseDTO enviarMensaje(MensajeChatRequestDTO request);
    List<MensajeChatResponseDTO> obtenerHistorialChat(Integer idReserva);
}