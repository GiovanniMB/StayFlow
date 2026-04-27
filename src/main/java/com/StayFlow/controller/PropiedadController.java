package com.StayFlow.controller;

import com.StayFlow.dto.request.PropiedadRequestDTO;
import com.StayFlow.dto.response.ApiResponseDTO;
import com.StayFlow.dto.response.PropiedadResponseDTO;
import com.StayFlow.Service.Interfaces.IPropiedadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

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
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Propiedad creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error en la validación de los datos enviados")
    })
    public ResponseEntity<ApiResponseDTO<PropiedadResponseDTO>> crearPropiedad(
            @Valid @RequestBody PropiedadRequestDTO request) { // @Valid para activar la validación de los campos del DTO
        
        PropiedadResponseDTO response = propiedadService.crearPropiedad(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.created("Propiedad creada exitosamente", response));
    }

    // Endpoint para obtener todas las propiedades activas
    @GetMapping
    @Operation(summary = "Obtener todas las propiedades activas")
    @ApiResponse(responseCode = "200", description = "Lista de propiedades recuperada exitosamente")
    public ResponseEntity<ApiResponseDTO<List<PropiedadResponseDTO>>> obtenerTodas() {
        List<PropiedadResponseDTO> propiedades = propiedadService.obtenerTodas();
        return ResponseEntity.ok(ApiResponseDTO.success("Propiedades recuperadas exitosamente", propiedades));
    }

    // Endpoint para obtener una propiedad por su ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una propiedad por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Propiedad recuperada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Propiedad no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<PropiedadResponseDTO>> obtenerPorId(
            @Parameter(description = "ID de la propiedad", example = "1") @PathVariable Integer id) {
        PropiedadResponseDTO response = propiedadService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Propiedad recuperada exitosamente", response));
    }

    // Endpoint para actualizar una propiedad existente
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una propiedad existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Propiedad actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error de validación de datos"),
        @ApiResponse(responseCode = "404", description = "Propiedad no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<PropiedadResponseDTO>> actualizarPropiedad(
            @Parameter(description = "ID de la propiedad a actualizar", example = "1") @PathVariable Integer id, 
            @Valid @RequestBody PropiedadRequestDTO request) { // @Valid para activar la validación de los campos del DTO
        
        PropiedadResponseDTO response = propiedadService.actualizarPropiedad(id, request);
        return ResponseEntity.ok(ApiResponseDTO.success("Propiedad actualizada exitosamente", response));
    }

    // Endpoint para eliminar una propiedad (borrado lógico)
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una propiedad (Borrado lógico)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Propiedad eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Propiedad no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<Void>> eliminarPropiedad(
            @Parameter(description = "ID de la propiedad a eliminar", example = "1") @PathVariable Integer id) {
        propiedadService.eliminarPropiedad(id);
        // Retornamos un mensaje de éxito sin datos adicionales
        return ResponseEntity.ok(ApiResponseDTO.success("Propiedad eliminada exitosamente"));
    }

    @GetMapping("/catalogo")
    @Operation(summary = "Obtener el catálogo público (Escaparate para huéspedes)")
    @ApiResponse(responseCode = "200", description = "Catálogo de propiedades recuperado exitosamente")
    public ResponseEntity<ApiResponseDTO<List<PropiedadResponseDTO>>> obtenerCatalogoPublico() {
        // Por ahora reutilizamos tu lógica existente que trae las propiedades activas
        List<PropiedadResponseDTO> catalogo = propiedadService.obtenerTodas();
        return ResponseEntity.ok(ApiResponseDTO.success("Catálogo recuperado exitosamente", catalogo));
    }

    @GetMapping("/catalogo/{id}")
    @Operation(summary = "Obtener el detalle público de una propiedad")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detalle de propiedad recuperado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Propiedad no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<PropiedadResponseDTO>> obtenerDetallePublico(
            @Parameter(description = "ID de la propiedad", example = "1") @PathVariable Integer id) {
        PropiedadResponseDTO response = propiedadService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Detalle recuperado exitosamente", response));
    }
}