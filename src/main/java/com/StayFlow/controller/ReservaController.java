package com.StayFlow.controller;

import com.StayFlow.dto.request.ReservaRequestDTO;
import com.StayFlow.dto.response.ApiResponseDTO;
import com.StayFlow.dto.response.DisponibilidadResponseDTO;
import com.StayFlow.dto.response.ReservaResponseDTO;
import com.StayFlow.service.interfaces.IReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservas") 
@Tag(name = "Reservas", description = "API para la gestión del flujo de reservaciones y disponibilidad de cuartos")
public class ReservaController {

    private final IReservaService reservaService;

    public ReservaController(IReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva reserva de habitación")
    public ResponseEntity<ApiResponseDTO<ReservaResponseDTO>> crearReserva(@Valid @RequestBody ReservaRequestDTO request) {
        ReservaResponseDTO reserva = reservaService.crearReserva(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Reserva creada exitosamente", reserva));
    }

    @GetMapping("/habitaciones/{idHabitacion}/disponibilidad")
    @Operation(summary = "Verificar si una habitación está libre en fechas específicas")
    public ResponseEntity<ApiResponseDTO<DisponibilidadResponseDTO>> verificarDisponibilidad(
            @PathVariable Integer idHabitacion,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaEntrada,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaSalida) {
        
        DisponibilidadResponseDTO disponibilidad = reservaService.verificarDisponibilidad(idHabitacion, fechaEntrada, fechaSalida);
        return ResponseEntity.ok(ApiResponseDTO.success("Consulta de disponibilidad completada", disponibilidad));
    }

    @GetMapping("/habitaciones/{idHabitacion}")
    @Operation(summary = "Obtener el listado de reservas activas de una habitación")
    public ResponseEntity<ApiResponseDTO<List<ReservaResponseDTO>>> obtenerReservasPorHabitacion(@PathVariable Integer idHabitacion) {
        List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorHabitacion(idHabitacion);
        return ResponseEntity.ok(ApiResponseDTO.success("Reservas de la habitación obtenidas exitosamente", reservas));
    }

    @GetMapping("/propiedades/{idPropiedad}/reservas")
    @Operation(summary = "Obtener todas las reservas de una propiedad completa")
    public ResponseEntity<ApiResponseDTO<List<ReservaResponseDTO>>> obtenerReservasPorPropiedad(@PathVariable Integer idPropiedad) {
        List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorPropiedad(idPropiedad);
        return ResponseEntity.ok(ApiResponseDTO.success("Reservas de la propiedad obtenidas exitosamente", reservas));
    }

    @GetMapping("/cliente/{idCliente}")
    @Operation(summary = "Obtener el historial de viajes/reservas de un cliente específico")
    public ResponseEntity<ApiResponseDTO<List<ReservaResponseDTO>>> obtenerReservasPorCliente(@PathVariable Integer idCliente) {
        List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorCliente(idCliente);
        return ResponseEntity.ok(ApiResponseDTO.success("Tus reservas han sido recuperadas", reservas));
    }
}