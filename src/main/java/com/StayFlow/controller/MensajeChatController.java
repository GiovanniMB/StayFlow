package com.StayFlow.controller;

import com.StayFlow.dto.request.MensajeChatRequestDTO;
import com.StayFlow.dto.response.ApiResponseDTO;
import com.StayFlow.dto.response.MensajeChatResponseDTO;
import com.StayFlow.service.interfaces.IMensajeChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas") 
public class MensajeChatController {

    private final IMensajeChatService chatService;
    private final SimpMessagingTemplate messagingTemplate; 

    public MensajeChatController(IMensajeChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/{idReserva}/mensajes")
    public ResponseEntity<ApiResponseDTO<List<MensajeChatResponseDTO>>> obtenerHistorial(
            @PathVariable Integer idReserva) {
        
        List<MensajeChatResponseDTO> historial = chatService.obtenerHistorialChat(idReserva);
        
        ApiResponseDTO<List<MensajeChatResponseDTO>> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Historial recuperado exitosamente");
        response.setData(historial);
        
        return ResponseEntity.ok(response);
    }

    // Túnel WebSocket (No tocar)
    @MessageMapping("/chat/{idReserva}")
    public void enviarMensajeWS(@DestinationVariable Integer idReserva, @Valid MensajeChatRequestDTO request) {
        request.setIdReserva(idReserva);
        MensajeChatResponseDTO mensajeGuardado = chatService.enviarMensaje(request);
        messagingTemplate.convertAndSend("/topic/reserva/" + idReserva, mensajeGuardado);
    }
}