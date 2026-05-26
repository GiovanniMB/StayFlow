package com.StayFlow.controller;

import com.StayFlow.dto.request.ResenaRequestDTO;
import com.StayFlow.dto.response.ApiResponseDTO;
import com.StayFlow.dto.response.ResenaResponseDTO;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.model.Usuario;
import com.StayFlow.repository.UsuarioRepository;
import com.StayFlow.service.interfaces.IResenaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
@Tag(name = "Reseñas", description = "API para la gestión de valoraciones y comentarios de propiedades")
public class ResenaController {

    private final IResenaService resenaService;
    private final UsuarioRepository usuarioRepository;

    public ResenaController(IResenaService resenaService, UsuarioRepository usuarioRepository) {
        this.resenaService = resenaService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ResenaResponseDTO>> crearResena(
            @Valid @RequestBody ResenaRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        // 1. Busca el usuario en la BD usando el email (username) extraído del token JWT
        Usuario usuarioAutenticado = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado en el sistema"));
        
        // 2. Pasa el ID real al servicio
        ResenaResponseDTO resena = resenaService.crearResena(request, usuarioAutenticado.getIdUsuario());
        
        ApiResponseDTO<ResenaResponseDTO> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Reseña publicada con éxito");
        response.setData(resena);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Este endpoint es PÚBLICO (No requiere Token JWT)
    @GetMapping("/propiedad/{idPropiedad}")
    public ResponseEntity<ApiResponseDTO<List<ResenaResponseDTO>>> obtenerResenasDePropiedad(@PathVariable Integer idPropiedad) {
        List<ResenaResponseDTO> resenas = resenaService.obtenerResenasPorPropiedad(idPropiedad);
        ApiResponseDTO<List<ResenaResponseDTO>> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setMessage("Reseñas recuperadas con éxito");
        response.setData(resenas);
        return ResponseEntity.ok(response);
    }
}