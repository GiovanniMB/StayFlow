package com.StayFlow.controller;

import com.StayFlow.Service.Interfaces.IPropiedadService;
import com.StayFlow.dto.request.PropiedadRequestDTO;
import com.StayFlow.dto.response.ApiResponseDTO;
import com.StayFlow.dto.response.PropiedadResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// Controlador REST para la gestión de propiedades
@RestController
@RequestMapping("/api/propiedades")
@Tag(name = "Propiedades", description = "API para la gestión del CRUD de propiedades") // Etiqueta para la documentación de Swagger
public class PropiedadController {

    private final IPropiedadService propiedadService;

    // Inyección de dependencias por constructor
    public PropiedadController(IPropiedadService propiedadService) {
        this.propiedadService = propiedadService;
    }

    // Endpoint para crear una nueva propiedad
    @PostMapping
    @Operation(summary = "Crear una nueva propiedad")
    public ResponseEntity<ApiResponseDTO<PropiedadResponseDTO>> crearPropiedad(@RequestBody PropiedadRequestDTO request) {
        PropiedadResponseDTO response = propiedadService.crearPropiedad(request);
        // Uso el método estático created() del equipo para retornar un 201
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.created("Propiedad creada exitosamente", response));
    }

    // Endpoint para obtener todas las propiedades activas
    @GetMapping
    @Operation(summary = "Obtener todas las propiedades activas")
    public ResponseEntity<ApiResponseDTO<List<PropiedadResponseDTO>>> obtenerTodas() {
        List<PropiedadResponseDTO> propiedades = propiedadService.obtenerTodas();
        return ResponseEntity.ok(ApiResponseDTO.success("Propiedades recuperadas exitosamente", propiedades));
    }

    // Endpoint para obtener una propiedad por su ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una propiedad por su ID")
    public ResponseEntity<ApiResponseDTO<PropiedadResponseDTO>> obtenerPorId(@PathVariable Integer id) {
        PropiedadResponseDTO response = propiedadService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Propiedad recuperada exitosamente", response));
    }

    // Endpoint para actualizar una propiedad existente
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una propiedad existente")
    public ResponseEntity<ApiResponseDTO<PropiedadResponseDTO>> actualizarPropiedad(
            @PathVariable Integer id, 
            @RequestBody PropiedadRequestDTO request) {
        
        PropiedadResponseDTO response = propiedadService.actualizarPropiedad(id, request);
        return ResponseEntity.ok(ApiResponseDTO.success("Propiedad actualizada exitosamente", response));
    }

    // Endpoint para eliminar una propiedad (borrado lógico)
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una propiedad (Borrado lógico)")
    public ResponseEntity<ApiResponseDTO<Void>> eliminarPropiedad(@PathVariable Integer id) {
        propiedadService.eliminarPropiedad(id);
        // Retornamos un mensaje de éxito sin datos adicionales
        return ResponseEntity.ok(ApiResponseDTO.success("Propiedad eliminada exitosamente"));
    }
}