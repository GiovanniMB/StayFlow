package com.StayFlow.controller;

import com.StayFlow.dto.request.HabitacionRequestDTO;
import com.StayFlow.dto.request.TipoHabitacionRequestDTO;
import com.StayFlow.dto.response.ApiResponseDTO;
import com.StayFlow.dto.response.HabitacionResponseDTO;
import com.StayFlow.dto.response.TipoHabitacionResponseDTO;
import com.StayFlow.service.interfaces.IHabitacionService;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Habitaciones", description = "Gestión de categorías y cuartos físicos de las propiedades") // Agrega una etiqueta para agrupar los endpoints relacionados con habitaciones en la documentación de Swagger
public class HabitacionController {

    private final IHabitacionService habitacionService;

    public HabitacionController(IHabitacionService habitacionService) {
        this.habitacionService = habitacionService;
    }

    // --- Endpoints para TipoHabitacion (Categorías) ---
    // El endpoint crearTipoHabitacion permite a los propietarios crear una nueva categoría de habitación para una propiedad específica. Esto es útil para que los propietarios puedan organizar sus habitaciones físicas en categorías que tengan características comunes, como el tipo de cama, los servicios incluidos o el precio por noche.
    @PostMapping("/propiedades/{idPropiedad}/tipos-habitacion")
    @Operation(summary = "Crear una nueva categoría de habitación para una propiedad")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
        @ApiResponse(responseCode = "404", description = "Propiedad no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<TipoHabitacionResponseDTO>> crearTipoHabitacion(
            @Parameter(description = "ID de la propiedad padre", example = "1") @PathVariable Integer idPropiedad,
            @Valid @RequestBody TipoHabitacionRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.created("Categoría creada exitosamente", habitacionService.crearTipoHabitacion(idPropiedad, request)));
    }
    // El endpoint obtenerTiposPorPropiedad permite listar todas las categorías de habitación asociadas a una propiedad específica. Esto es útil para que los propietarios puedan ver y gestionar las categorías de habitación que han definido para cada una de sus propiedades.
    @GetMapping("/propiedades/{idPropiedad}/tipos-habitacion")
    @Operation(summary = "Obtener todas las categorías de habitación de una propiedad")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categorías recuperadas correctamente"),
        @ApiResponse(responseCode = "404", description = "Propiedad no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<List<TipoHabitacionResponseDTO>>> obtenerTiposPorPropiedad(
            @Parameter(description = "ID de la propiedad", example = "1") @PathVariable Integer idPropiedad) {
        return ResponseEntity.ok(ApiResponseDTO.success("Categorías recuperadas correctamente", habitacionService.obtenerTiposPorPropiedad(idPropiedad)));
    }
    // El endpoint actualizarTipoHabitacion permite modificar el nombre, la descripción, el precio por noche y los servicios asociados a una categoría de habitación específica. Esto es útil para mantener actualizada la información de las categorías de habitación, especialmente si hay cambios en los servicios ofrecidos o en los precios.
    @PutMapping("/tipos-habitacion/{idTipoHabitacion}")
    @Operation(summary = "Actualizar una categoría de habitación")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<TipoHabitacionResponseDTO>> actualizarTipoHabitacion(
            @Parameter(description = "ID de la categoría a actualizar", example = "5") @PathVariable Integer idTipoHabitacion,
            @Valid @RequestBody TipoHabitacionRequestDTO request) {
        return ResponseEntity.ok(ApiResponseDTO.success("Categoría actualizada", habitacionService.actualizarTipoHabitacion(idTipoHabitacion, request)));
    }
    // El endpoint eliminarTipoHabitacion realiza un borrado lógico de la categoría de habitación, lo que significa que no se elimina físicamente de la base de datos, sino que se marca como eliminada para mantener la integridad referencial y permitir posibles recuperaciones futuras.
    @DeleteMapping("/tipos-habitacion/{idTipoHabitacion}")
    @Operation(summary = "Eliminar una categoría de habitación (Borrado lógico)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<Void>> eliminarTipoHabitacion(
            @Parameter(description = "ID de la categoría a eliminar", example = "5") @PathVariable Integer idTipoHabitacion) {
        habitacionService.eliminarTipoHabitacion(idTipoHabitacion);
        return ResponseEntity.ok(ApiResponseDTO.success("Categoría eliminada exitosamente"));
    }

    // --- Endpoints para Habitacion (Cuartos Físicos) ---
    // El endpoint crearHabitacion permite a los propietarios dar de alta un nuevo cuarto físico dentro de una categoría específica. Esto es útil para que los propietarios puedan gestionar las habitaciones físicas asociadas a cada tipo de habitación que han definido para su propiedad.
    @PostMapping("/tipos-habitacion/{idTipoHabitacion}/habitaciones")
    @Operation(summary = "Dar de alta un cuarto físico dentro de una categoría")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Habitación registrada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<HabitacionResponseDTO>> crearHabitacion(
            @Parameter(description = "ID de la categoría padre", example = "5") @PathVariable Integer idTipoHabitacion,
            @Valid @RequestBody HabitacionRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.created("Habitación registrada exitosamente", habitacionService.crearHabitacion(idTipoHabitacion, request)));
    }
    // El endpoint obtenerHabitacionesPorTipo permite listar todos los cuartos físicos asociados a una categoría específica. Esto es útil para que los propietarios puedan ver y gestionar las habitaciones que pertenecen a cada tipo de habitación que han definido para su propiedad.
    @GetMapping("/tipos-habitacion/{idTipoHabitacion}/habitaciones")
    @Operation(summary = "Listar todos los cuartos físicos de una categoría")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Habitaciones recuperadas correctamente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<List<HabitacionResponseDTO>>> obtenerHabitacionesPorTipo(
            @Parameter(description = "ID de la categoría", example = "5") @PathVariable Integer idTipoHabitacion) {
        return ResponseEntity.ok(ApiResponseDTO.success("Habitaciones recuperadas correctamente", habitacionService.obtenerHabitacionesPorTipo(idTipoHabitacion)));
    }
    // El endpoint actualizarHabitacion permite modificar el número o la cantidad de camas de un cuarto físico específico. Esto es útil para mantener actualizada la información de las habitaciones, especialmente si hay cambios en la configuración o en la disponibilidad de camas.
    @PutMapping("/habitaciones/{idHabitacion}")
    @Operation(summary = "Actualizar el número o camas de un cuarto físico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Habitación actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<HabitacionResponseDTO>> actualizarHabitacion(
            @Parameter(description = "ID de la habitación física", example = "10") @PathVariable Integer idHabitacion,
            @Valid @RequestBody HabitacionRequestDTO request) {
        return ResponseEntity.ok(ApiResponseDTO.success("Habitación actualizada", habitacionService.actualizarHabitacion(idHabitacion, request)));
    }
    // El endpoint eliminarHabitacion realiza un borrado lógico de la habitación, lo que significa que no se elimina físicamente de la base de datos, sino que se marca como eliminada para mantener la integridad referencial y permitir posibles recuperaciones futuras.
    @DeleteMapping("/habitaciones/{idHabitacion}")
    @Operation(summary = "Eliminar un cuarto físico (Borrado lógico)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Habitación eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<Void>> eliminarHabitacion(
            @Parameter(description = "ID de la habitación a eliminar", example = "10") @PathVariable Integer idHabitacion) {
        habitacionService.eliminarHabitacion(idHabitacion);
        return ResponseEntity.ok(ApiResponseDTO.success("Habitación eliminada exitosamente"));
    }

    // Endpoints para Fotografías
    @PostMapping(value = "/tipos-habitacion/{idTipoHabitacion}/fotos", consumes = "multipart/form-data")
    @Operation(summary = "Subir una fotografía exclusiva para una categoría/recámara")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Fotografía guardada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Archivo corrupto o resolución insuficiente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<Void>> subirFotoHabitacion(
            @Parameter(description = "ID de la categoría", example = "5") @PathVariable Integer idTipoHabitacion,
            @Parameter(description = "ID de clasificación de la foto (baño, fachada, etc)", example = "1") @RequestParam("idCategoriaFoto") Integer idCategoriaFoto,
            @Parameter(description = "¿Es la foto principal de portada?", example = "true") @RequestParam("esPrincipal") boolean esPrincipal,
            @Parameter(description = "Archivo físico de la imagen") @RequestParam("archivo") MultipartFile archivo) {
            
        habitacionService.subirFotoHabitacion(idTipoHabitacion, idCategoriaFoto, archivo, esPrincipal);
        return ResponseEntity.ok(ApiResponseDTO.success("Fotografía de habitación guardada exitosamente"));
    }

    @PostMapping(value = "/propiedades/{idPropiedad}/fotos", consumes = "multipart/form-data")
    @Operation(summary = "Subir una fotografía general para toda la propiedad (Fachada, Alberca, etc.)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Fotografía guardada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Archivo corrupto o resolución insuficiente"),
        @ApiResponse(responseCode = "404", description = "Propiedad no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<Void>> subirFotoPropiedad(
            @Parameter(description = "ID de la propiedad", example = "1") @PathVariable Integer idPropiedad,
            @Parameter(description = "ID de clasificación de la foto", example = "2") @RequestParam("idCategoriaFoto") Integer idCategoriaFoto,
            @Parameter(description = "¿Es la portada principal de todo el alojamiento?", example = "true") @RequestParam("esPrincipal") boolean esPrincipal,
            @Parameter(description = "Archivo físico de la imagen") @RequestParam("archivo") MultipartFile archivo) {
            
        habitacionService.subirFotoPropiedad(idPropiedad, idCategoriaFoto, archivo, esPrincipal);
        return ResponseEntity.ok(ApiResponseDTO.success("Fotografía general de la propiedad guardada exitosamente"));
    }

    @DeleteMapping("/fotos/{idFoto}")
    @Operation(summary = "Eliminar una fotografía (Borrado lógico y físico)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Fotografía eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Fotografía no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<Void>> eliminarFoto(
            @Parameter(description = "ID de la foto a eliminar", example = "15") @PathVariable Integer idFoto) {
        habitacionService.eliminarFoto(idFoto);
        return ResponseEntity.ok(ApiResponseDTO.success("Fotografía eliminada exitosamente"));
    }

    @GetMapping("/propiedades/catalogo/{idPropiedad}/tipos-habitacion")
    @Operation(summary = "Obtener catálogo público de habitaciones de una propiedad")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Catálogo recuperado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Propiedad no encontrada")
    })
    public ResponseEntity<ApiResponseDTO<List<TipoHabitacionResponseDTO>>> obtenerTiposPublico(
            @Parameter(description = "ID de la propiedad", example = "1") @PathVariable Integer idPropiedad) {
        return ResponseEntity.ok(ApiResponseDTO.success("Habitaciones recuperadas", habitacionService.obtenerTiposPorPropiedad(idPropiedad)));
    }
}