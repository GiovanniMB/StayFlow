package com.StayFlow.controller;

import com.StayFlow.dto.response.PropiedadResponseDTO;
import com.StayFlow.Service.Interfaces.IFavoritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favoritos")
@Tag(name = "Favoritos", description = "Gestión de propiedades guardadas por el usuario huésped")
public class FavoritoController {

    private final IFavoritoService favoritoService;

    public FavoritoController(IFavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    // Endpoint para el corazon de favorito, quitar o poner
    @PostMapping("/toggle/{idPropiedad}")
    @Operation(summary = "Alternar estado de favorito", description = "Si la propiedad ya es favorita, la remueve. Si no lo es, la agrega a la lista de favoritos del usuario.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado de favorito alternado correctamente"),
        @ApiResponse(responseCode = "404", description = "Propiedad no encontrada")
    })
    public ResponseEntity<Map<String, Object>> toggleFavorito(
            @Parameter(description = "ID de la propiedad a marcar/desmarcar", example = "1") @PathVariable Integer idPropiedad) {
        boolean agregado = favoritoService.toggleFavorito(idPropiedad);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("agregado", agregado); // React usará esto para pintar el corazón rojo o gris
        response.put("message", agregado ? "Propiedad agregada a favoritos" : "Propiedad eliminada de favoritos");
        
        return ResponseEntity.ok(response);
    }

    //End point para dibujar la pantalla de mis favoritos
    @GetMapping("/mis-favoritos")
    @Operation(summary = "Obtener las propiedades favoritas del usuario autenticado")
    @ApiResponse(responseCode = "200", description = "Lista de propiedades favoritas recuperada exitosamente")
    public ResponseEntity<Map<String, Object>> obtenerMisFavoritos() {
        List<PropiedadResponseDTO> favoritos = favoritoService.obtenerMisFavoritos();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", favoritos); 
        
        return ResponseEntity.ok(response);
    }

    // Endpoint para saber que favoritos estan marcados
    @GetMapping("/mis-favoritos/ids")
    @Operation(summary = "Obtener solo los IDs de las propiedades favoritas", description = "Útil para inicializar el estado visual de los corazones en la pantalla de inicio.")
    @ApiResponse(responseCode = "200", description = "Lista de IDs recuperada exitosamente")
    public ResponseEntity<Map<String, Object>> obtenerIdsMisFavoritos() {
        List<Integer> ids = favoritoService.obtenerIdsMisFavoritos();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", ids); 
        
        return ResponseEntity.ok(response);
    }
}