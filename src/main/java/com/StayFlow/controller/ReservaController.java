package com.StayFlow.controller;

import com.StayFlow.dto.request.ReservaRequestDTO;
import com.StayFlow.dto.response.ApiResponseDTO;
import com.StayFlow.dto.response.DisponibilidadResponseDTO;
import com.StayFlow.dto.response.ReservaResponseDTO;
import com.StayFlow.service.interfaces.IReservaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReservaController {

    private final IReservaService reservaService;

    // Constructor nativo para inyección de dependencias (Reemplaza a @RequiredArgsConstructor)
    public ReservaController(IReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping("/reservas")
    public ResponseEntity<ApiResponseDTO<ReservaResponseDTO>> crearReserva(@Valid @RequestBody ReservaRequestDTO request) {
        ReservaResponseDTO reserva = reservaService.crearReserva(request);
        
        ApiResponseDTO<ReservaResponseDTO> response = new ApiResponseDTO<>();
        // Ajusta estos setters si los nombres en tu ApiResponseDTO son ligeramente distintos
        response.setSuccess(true);
        response.setMessage("Reserva creada exitosamente");
        response.setData(reserva);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/reservas/{idReserva}")
    public ResponseEntity<ApiResponseDTO<ReservaResponseDTO>> obtenerReservaPorId(@PathVariable Integer idReserva) {
        ReservaResponseDTO reserva = reservaService.obtenerReservaPorId(idReserva);
        
        ApiResponseDTO<ReservaResponseDTO> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Reserva obtenida exitosamente");
        response.setData(reserva);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/clientes/{idCliente}/reservas")
    public ResponseEntity<ApiResponseDTO<List<ReservaResponseDTO>>> obtenerReservasPorCliente(@PathVariable Integer idCliente) {
        List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorCliente(idCliente);
        
        ApiResponseDTO<List<ReservaResponseDTO>> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Reservas del cliente obtenidas exitosamente");
        response.setData(reservas);
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reservas/{idReserva}/cancelar")
    public ResponseEntity<ApiResponseDTO<ReservaResponseDTO>> cancelarReserva(@PathVariable Integer idReserva) {
        ReservaResponseDTO reservaCancelada = reservaService.cancelarReserva(idReserva);
        
        ApiResponseDTO<ReservaResponseDTO> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Reserva cancelada exitosamente");
        response.setData(reservaCancelada);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/habitaciones/{idHabitacion}/disponibilidad")
    public ResponseEntity<ApiResponseDTO<DisponibilidadResponseDTO>> verificarDisponibilidad(
            @PathVariable Integer idHabitacion,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaEntrada,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaSalida) {
        
        DisponibilidadResponseDTO disponibilidad = reservaService.verificarDisponibilidad(idHabitacion, fechaEntrada, fechaSalida);
        
        ApiResponseDTO<DisponibilidadResponseDTO> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Consulta de disponibilidad completada");
        response.setData(disponibilidad);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/habitaciones/{idHabitacion}/reservas")
    public ResponseEntity<ApiResponseDTO<List<ReservaResponseDTO>>> obtenerReservasPorHabitacion(@PathVariable Integer idHabitacion) {
    List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorHabitacion(idHabitacion);
    
    ApiResponseDTO<List<ReservaResponseDTO>> response = new ApiResponseDTO<>();
    response.setSuccess(true);
    response.setMessage("Reservas de la habitación obtenidas exitosamente");
    response.setData(reservas);
    
    return ResponseEntity.ok(response);
}
}