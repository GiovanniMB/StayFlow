package com.StayFlow.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.StayFlow.Service.Interfaces.IResenaService;
import com.StayFlow.dto.request.ResenaRequestDTO;
import com.StayFlow.dto.response.ResenaPendienteResponseDTO;
import com.StayFlow.dto.response.ResenaResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/resenas")
@CrossOrigin(origins = "http://localhost:5173")
public class ResenaController {

    private final IResenaService resenaService;

    public ResenaController(IResenaService resenaService) {
        this.resenaService = resenaService;
    }

    // Muestra las reseñas visibles de una propiedad.
    @GetMapping("/propiedad/{idPropiedad}")
    public ResponseEntity<List<ResenaResponseDTO>> listarPorPropiedad(@PathVariable Integer idPropiedad) {
        return ResponseEntity.ok(resenaService.obtenerResenasPorPropiedad(idPropiedad));
    }

    // Muestra las reseñas hechas sobre un huésped.
    @GetMapping("/huesped/{idUsuario}")
    public ResponseEntity<List<ResenaResponseDTO>> listarSobreHuesped(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(resenaService.obtenerResenasSobreHuesped(idUsuario));
    }

    // Crea una reseña de huésped a propiedad o de propietario a huésped.
    @PostMapping("/crear")
    public ResponseEntity<ResenaResponseDTO> crear(@Valid @RequestBody ResenaRequestDTO request) {
        return ResponseEntity.ok(resenaService.guardarResena(request));
    }

    // Notificación calculada: reservas terminadas pendientes de reseña por parte del cliente.
    @GetMapping("/pendientes/cliente/{idCliente}")
    public ResponseEntity<List<ResenaPendienteResponseDTO>> pendientesCliente(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(resenaService.obtenerPendientesCliente(idCliente));
    }

    // Notificación calculada: reservas terminadas pendientes de reseña por parte del propietario.
    @GetMapping("/pendientes/propietario/{idPropietario}")
    public ResponseEntity<List<ResenaPendienteResponseDTO>> pendientesPropietario(@PathVariable Integer idPropietario) {
        return ResponseEntity.ok(resenaService.obtenerPendientesPropietario(idPropietario));
    }
}