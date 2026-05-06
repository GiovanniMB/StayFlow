package com.StayFlow.controller;

import com.StayFlow.service.interfaces.ICatalogoService;
import com.StayFlow.dto.response.CatalogoResponseDTO;
import com.StayFlow.dto.response.ServicioResponseDTO;
import com.StayFlow.dto.response.TipoCamaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogos")
@Tag(name = "Catálogos Geográficos", description = "Endpoints de solo lectura para alimentar dropdowns del Frontend")
public class CatalogoController {

    private final ICatalogoService catalogoService;
    // Constructor para inyectar el servicio de catálogos
    public CatalogoController(ICatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }
    // Endpoint para obtener todos los estados
    @Operation(summary = "Obtener todos los estados", description = "Devuelve la lista completa de estados disponibles.")
    @ApiResponse(responseCode = "200", description = "Lista de estados recuperada correctamente")
    @GetMapping("/estados")
    public ResponseEntity<List<CatalogoResponseDTO>> obtenerEstados() {
        return ResponseEntity.ok(catalogoService.obtenerTodosLosEstados());
    }
    //  Endpoint para obtener municipios por estado
    @Operation(summary = "Obtener municipios por estado", description = "Devuelve los municipios que pertenecen a un ID de estado específico.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de municipios recuperada correctamente"),
        @ApiResponse(responseCode = "404", description = "Estado no encontrado")
    })
    @GetMapping("/estados/{idEstado}/municipios")
    public ResponseEntity<List<CatalogoResponseDTO>> obtenerMunicipiosPorEstado(
            @Parameter(description = "ID del estado", example = "14") @PathVariable Integer idEstado) {
        return ResponseEntity.ok(catalogoService.obtenerMunicipiosPorEstado(idEstado));
    }
    // Endpoint para obtener colonias por municipio
    @Operation(summary = "Obtener colonias por municipio", description = "Devuelve las colonias que pertenecen a un ID de municipio específico.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de colonias recuperada correctamente"),
        @ApiResponse(responseCode = "404", description = "Municipio no encontrado")
    })
    @GetMapping("/municipios/{idMunicipio}/colonias")
    public ResponseEntity<List<CatalogoResponseDTO>> obtenerColoniasPorMunicipio(
            @Parameter(description = "ID del municipio", example = "39") @PathVariable Integer idMunicipio) {
        return ResponseEntity.ok(catalogoService.obtenerColoniasPorMunicipio(idMunicipio));
    }
    // Endpoint para obtener todos los servicios (Amenities)
    @Operation(summary = "Obtener catálogo de servicios", description = "Devuelve la lista de amenidades (Wifi, Alberca, etc.) disponibles.")
    @ApiResponse(responseCode = "200", description = "Lista de servicios recuperada correctamente")
    @GetMapping("/servicios")
    public ResponseEntity<List<ServicioResponseDTO>> obtenerServicios() {
        return ResponseEntity.ok(catalogoService.obtenerTodosLosServicios());
    }

    //Endpoint oara ibtener los tipos de cama disponibles
    @Operation(summary = "Obtener el catálogo de tipos de cama")
    @ApiResponse(responseCode = "200", description = "Lista de tipos de cama recuperada correctamente")
    @GetMapping("/tipos-cama")
    public ResponseEntity<List<TipoCamaResponseDTO>> obtenerTiposCama() {
        return ResponseEntity.ok(catalogoService.obtenerTiposCama());
    }
    
    @Operation(summary = "Obtener categorías de fotografías", description = "Devuelve las opciones para clasificar una foto (Fachada, Baño, etc.)")
    @ApiResponse(responseCode = "200", description = "Lista de categorías fotográficas recuperada correctamente")
    @GetMapping("/categorias-foto")
    public ResponseEntity<List<CatalogoResponseDTO>> obtenerCategoriasFoto() {
        return ResponseEntity.ok(catalogoService.obtenerCategoriasFoto());
    }
}