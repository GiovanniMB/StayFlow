package com.StayFlow.controller;

import com.StayFlow.Service.Interfaces.IReservaService;
import com.StayFlow.dto.request.ReservaRequestDTO;
import com.StayFlow.dto.response.ApiResponseDTO;
import com.StayFlow.dto.response.DisponibilidadResponseDTO;
import com.StayFlow.dto.response.ReservaResponseDTO;
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
@RequestMapping("/api")
@Tag(name = "Reservas", description = "Gestión de reservas y disponibilidad de habitaciones")
public class ReservaController {

    private final IReservaService reservaService;

    public ReservaController(IReservaService reservaService) {
        this.reservaService = reservaService;
    }

    // Crear una nueva reserva
    @PostMapping("/reservas")
    @Operation(summary = "Crear una reserva")
    public ResponseEntity<ApiResponseDTO<ReservaResponseDTO>> crearReserva(
            @Valid @RequestBody ReservaRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseDTO.success(
                        "Reserva creada exitosamente",
                        reservaService.crearReserva(request)
                )
        );
    }

    // Obtener una reserva por su id
    @GetMapping("/reservas/{idReserva}")
    @Operation(summary = "Obtener una reserva por id")
    public ResponseEntity<ApiResponseDTO<ReservaResponseDTO>> obtenerReservaPorId(
            @PathVariable Integer idReserva) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        "Reserva recuperada correctamente",
                        reservaService.obtenerReservaPorId(idReserva)
                )
        );
    }

    // Obtener todas las reservas de un cliente
    @GetMapping("/clientes/{idCliente}/reservas")
    @Operation(summary = "Obtener reservas por cliente")
    public ResponseEntity<ApiResponseDTO<List<ReservaResponseDTO>>> obtenerReservasPorCliente(
            @PathVariable Integer idCliente) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        "Reservas recuperadas correctamente",
                        reservaService.obtenerReservasPorCliente(idCliente)
                )
        );
    }

    // Cancelar una reserva existente
    @PutMapping("/reservas/{idReserva}/cancelar")
    @Operation(summary = "Cancelar una reserva")
    public ResponseEntity<ApiResponseDTO<ReservaResponseDTO>> cancelarReserva(
            @PathVariable Integer idReserva) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        "Reserva cancelada exitosamente",
                        reservaService.cancelarReserva(idReserva)
                )
        );
    }

    // Verificar disponibilidad de una habitación en un rango de fechas
    @GetMapping("/habitaciones/{idHabitacion}/disponibilidad")
    @Operation(summary = "Verificar disponibilidad de una habitación")
    public ResponseEntity<ApiResponseDTO<DisponibilidadResponseDTO>> verificarDisponibilidad(
            @PathVariable Integer idHabitacion,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaEntrada,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaSalida) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        "Disponibilidad consultada correctamente",
                        reservaService.verificarDisponibilidad(idHabitacion, fechaEntrada, fechaSalida)
                )
        );
    }
}