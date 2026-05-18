package com.StayFlow.controller;

import com.StayFlow.dto.request.MensajeChatRequestDTO;
import com.StayFlow.dto.response.ApiResponseDTO;
import com.StayFlow.dto.response.MensajeChatResponseDTO;
import com.StayFlow.service.interfaces.IMensajeChatService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class MensajeChatController {

    private final IMensajeChatService chatService;

    public MensajeChatController(IMensajeChatService chatService) {
        this.chatService = chatService;
    }

    // Endpoint para enviar un mensaje
    @PostMapping("/{idReserva}/mensajes")
    public ResponseEntity<ApiResponseDTO<MensajeChatResponseDTO>> enviarMensaje(
            @PathVariable Integer idReserva,
            @Valid @RequestBody MensajeChatRequestDTO request) {
        
        // Asegura que el ID de la URL coincida con el del Body por seguridad
        request.setIdReserva(idReserva);
        
        MensajeChatResponseDTO mensaje = chatService.enviarMensaje(request);
        
        ApiResponseDTO<MensajeChatResponseDTO> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Mensaje enviado");
        response.setData(mensaje);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Endpoint para leer el historial
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
}