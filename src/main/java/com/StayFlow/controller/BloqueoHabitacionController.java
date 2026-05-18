package com.StayFlow.controller;

import com.StayFlow.dto.request.BloqueoHabitacionRequestDTO;
import com.StayFlow.dto.response.ApiResponseDTO;
import com.StayFlow.dto.response.BloqueoHabitacionResponseDTO;
import com.StayFlow.service.interfaces.IBloqueoHabitacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bloqueos")
public class BloqueoHabitacionController {

    private final IBloqueoHabitacionService bloqueoService;

    public BloqueoHabitacionController(IBloqueoHabitacionService bloqueoService) {
        this.bloqueoService = bloqueoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<BloqueoHabitacionResponseDTO>> crearBloqueo(@Valid @RequestBody BloqueoHabitacionRequestDTO request) {
        BloqueoHabitacionResponseDTO response = bloqueoService.crearBloqueo(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.created("Bloqueo de mantenimiento registrado", response));
    }

    @GetMapping("/propiedad/{idPropiedad}")
    public ResponseEntity<ApiResponseDTO<List<BloqueoHabitacionResponseDTO>>> obtenerBloqueosPorPropiedad(@PathVariable Integer idPropiedad) {
        List<BloqueoHabitacionResponseDTO> bloqueos = bloqueoService.obtenerBloqueosActivosPorPropiedad(idPropiedad);
        return ResponseEntity.ok(ApiResponseDTO.success("Bloqueos activos recuperados", bloqueos));
    }

    @DeleteMapping("/{idBloqueo}")
    public ResponseEntity<ApiResponseDTO<Void>> eliminarBloqueo(@PathVariable Integer idBloqueo) {
        bloqueoService.eliminarBloqueo(idBloqueo);
        return ResponseEntity.ok(ApiResponseDTO.success("Bloqueo de mantenimiento eliminado correctamente"));
    }
}