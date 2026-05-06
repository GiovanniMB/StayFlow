package com.StayFlow.controller;

import com.StayFlow.dto.request.PrecioTemporadaRequestDTO;
import com.StayFlow.dto.response.PrecioTemporadaResponseDTO;
import com.StayFlow.service.interfaces.IPrecioTemporadaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/temporadas")
@CrossOrigin(origins = "*") 
public class PrecioTemporadaController {

    private final IPrecioTemporadaService precioTemporadaService;

    public PrecioTemporadaController(IPrecioTemporadaService precioTemporadaService) {
        this.precioTemporadaService = precioTemporadaService;
    }

    @PostMapping("/crear")
    public ResponseEntity<PrecioTemporadaResponseDTO> crearTemporada(@Valid @RequestBody PrecioTemporadaRequestDTO request) {
        return new ResponseEntity<>(precioTemporadaService.crearTemporada(request), HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<PrecioTemporadaResponseDTO> actualizarTemporada(
            @PathVariable Integer id, @Valid @RequestBody PrecioTemporadaRequestDTO request) {
        return ResponseEntity.ok(precioTemporadaService.actualizarTemporada(id, request));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarTemporada(@PathVariable Integer id) {
        precioTemporadaService.eliminarTemporada(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listar")
    public ResponseEntity<List<PrecioTemporadaResponseDTO>> listarTemporadas() {
        return ResponseEntity.ok(precioTemporadaService.listarTemporadasActivas());
    }
}