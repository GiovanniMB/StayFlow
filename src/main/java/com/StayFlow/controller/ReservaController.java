package com.StayFlow.controller;

import com.StayFlow.dto.request.ReservaRequestDTO;
import com.StayFlow.dto.response.ApiResponseDTO;
import com.StayFlow.dto.response.DisponibilidadResponseDTO;
import com.StayFlow.dto.response.MensajeChatResponseDTO;
import com.StayFlow.dto.response.ReservaResponseDTO;
import com.StayFlow.model.Reserva.EstadoReserva; 
import com.StayFlow.service.interfaces.IReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservas") 
@Tag(name = "Reservas", description = "API para la gestión del flujo de reservaciones y disponibilidad")
public class ReservaController {
    
    private final IReservaService reservaService;
    private final SimpMessagingTemplate messagingTemplate;

    public ReservaController(IReservaService reservaService, SimpMessagingTemplate messagingTemplate) {
        this.reservaService = reservaService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ReservaResponseDTO>> crearReserva(@Valid @RequestBody ReservaRequestDTO request) {
        ReservaResponseDTO reserva = reservaService.crearReserva(request);
        ApiResponseDTO<ReservaResponseDTO> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Reserva creada exitosamente");
        response.setData(reserva);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/habitacion/{idHabitacion}")
    public ResponseEntity<ApiResponseDTO<List<ReservaResponseDTO>>> obtenerReservasPorHabitacion(@PathVariable Integer idHabitacion) {
        List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorHabitacion(idHabitacion);
        ApiResponseDTO<List<ReservaResponseDTO>> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Reservas recuperadas exitosamente");
        response.setData(reservas);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/propiedad/{idPropiedad}")
    public ResponseEntity<ApiResponseDTO<List<ReservaResponseDTO>>> obtenerReservasPorPropiedad(@PathVariable Integer idPropiedad) {
        List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorPropiedad(idPropiedad);
        ApiResponseDTO<List<ReservaResponseDTO>> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Reservas recuperadas exitosamente");
        response.setData(reservas);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<ApiResponseDTO<List<ReservaResponseDTO>>> obtenerReservasPorCliente(@PathVariable Integer idCliente) {
        List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorCliente(idCliente);
        ApiResponseDTO<List<ReservaResponseDTO>> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Reservas recuperadas exitosamente");
        response.setData(reservas);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/habitacion/{idHabitacion}/disponibilidad")
    public ResponseEntity<ApiResponseDTO<DisponibilidadResponseDTO>> consultarDisponibilidad(
            @PathVariable Integer idHabitacion,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaEntrada,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaSalida) {
        
DisponibilidadResponseDTO disponibilidad = reservaService.verificarDisponibilidad(idHabitacion, fechaEntrada, fechaSalida);
        ApiResponseDTO<DisponibilidadResponseDTO> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Disponibilidad consultada exitosamente");
        response.setData(disponibilidad);
        return ResponseEntity.ok(response);
    }

    // check in conectado a la bd
    @PutMapping("/{idReserva}/checkin")
    public ResponseEntity<ApiResponseDTO<String>> hacerCheckIn(@PathVariable Integer idReserva) {
        
        // Actualiza la Base de Datos
        reservaService.actualizarEstadoReserva(idReserva, EstadoReserva.check_in);
        
        // Crea el mensaje del chat
        MensajeChatResponseDTO msjSistema = new MensajeChatResponseDTO();
        msjSistema.setIdReserva(idReserva);
        msjSistema.setIdRemitente(0); 
        msjSistema.setNombreRemitente("SISTEMA");
        msjSistema.setContenido("🛎️ El huésped ha realizado su Check-In exitosamente.");
        msjSistema.setFechaEnvio(LocalDateTime.now());
      
        // Lo transmite
        messagingTemplate.convertAndSend("/topic/reserva/" + idReserva, msjSistema);
        
        ApiResponseDTO<String> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Check-in realizado con éxito");
        return ResponseEntity.ok(response);
    }

    // check out conectado a la bd
    @PutMapping("/{idReserva}/checkout")
    public ResponseEntity<ApiResponseDTO<String>> hacerCheckOut(@PathVariable Integer idReserva) {
        
        reservaService.actualizarEstadoReserva(idReserva, EstadoReserva.check_out);
        
        MensajeChatResponseDTO msjSistema = new MensajeChatResponseDTO();
        msjSistema.setIdReserva(idReserva);
        msjSistema.setIdRemitente(0); 
        msjSistema.setNombreRemitente("SISTEMA");
        msjSistema.setContenido("🚪 El huésped ha finalizado su estancia (Check-Out).");
        msjSistema.setFechaEnvio(LocalDateTime.now());
        
        messagingTemplate.convertAndSend("/topic/reserva/" + idReserva, msjSistema);
        
        ApiResponseDTO<String> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Check-out realizado con éxito");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{idReserva}/cancelar")
    @Operation(summary = "Cancelar una reserva y aplicar políticas de reembolso")
    public ResponseEntity<ApiResponseDTO<ReservaResponseDTO>> cancelarReserva(
            @Parameter(description = "ID de la reserva") @PathVariable Integer idReserva) {
        return ResponseEntity.ok(ApiResponseDTO.success(
                "Reserva cancelada exitosamente", 
                reservaService.cancelarReserva(idReserva)
        ));
    }
}