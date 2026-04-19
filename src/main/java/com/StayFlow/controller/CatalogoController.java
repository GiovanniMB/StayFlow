package com.StayFlow.controller;

import com.StayFlow.Service.Interfaces.ICatalogoService;
import com.StayFlow.dto.response.CatalogoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
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
    @GetMapping("/estados")
    public ResponseEntity<List<CatalogoResponseDTO>> obtenerEstados() {
        return ResponseEntity.ok(catalogoService.obtenerTodosLosEstados());
    }
    //  Endpoint para obtener municipios por estado
    @Operation(summary = "Obtener municipios por estado", description = "Devuelve los municipios que pertenecen a un ID de estado específico.")
    @GetMapping("/estados/{idEstado}/municipios")
    public ResponseEntity<List<CatalogoResponseDTO>> obtenerMunicipiosPorEstado(@PathVariable Integer idEstado) {
        return ResponseEntity.ok(catalogoService.obtenerMunicipiosPorEstado(idEstado));
    }
    // Endpoint para obtener colonias por municipio
    @Operation(summary = "Obtener colonias por municipio", description = "Devuelve las colonias que pertenecen a un ID de municipio específico.")
    @GetMapping("/municipios/{idMunicipio}/colonias")
    public ResponseEntity<List<CatalogoResponseDTO>> obtenerColoniasPorMunicipio(@PathVariable Integer idMunicipio) {
        return ResponseEntity.ok(catalogoService.obtenerColoniasPorMunicipio(idMunicipio));
    }
}