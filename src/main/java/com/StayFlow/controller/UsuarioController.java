package com.StayFlow.controller;

import static com.StayFlow.security.SecurityConstants.REFRESH_TOKEN_HEADER;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.StayFlow.dto.request.LoginRequestDTO;
import com.StayFlow.dto.request.RegistroRequestDTO;
import com.StayFlow.dto.response.ApiResponseDTO;
import com.StayFlow.dto.response.LoginResponseDTO;
import com.StayFlow.dto.response.UsuarioResponseDTO;
import com.StayFlow.model.Usuario;
import com.StayFlow.Service.Interfaces.IUsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Endpoints para la gestión de usuarios, autenticación y perfiles")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @PostMapping("/registro")
    @Operation(summary = "Registrar un nuevo usuario", 
               description = "Crea una nueva cuenta de usuario. Se envía un código de verificación al email.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o email ya registrado")
    })
    public ResponseEntity<ApiResponseDTO<UsuarioResponseDTO>> registrar(@Valid @RequestBody RegistroRequestDTO request) {
        UsuarioResponseDTO data = usuarioService.registrar(request);
        ApiResponseDTO<UsuarioResponseDTO> response = ApiResponseDTO.success("Usuario registrado exitosamente", data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

   
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", 
               description = "Autentica a un usuario y retorna un token JWT para autorización")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso, retorna token JWT"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas o email no confirmado")
    })
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO data = usuarioService.login(request);
        ApiResponseDTO<LoginResponseDTO> response = ApiResponseDTO.success("Login exitoso", data);
        return ResponseEntity.ok(response);
    }

  
    @PostMapping("/refresh-token")
    @Operation(summary = "Renovar token de acceso", 
               description = "Utiliza un refresh token válido para obtener un nuevo token de acceso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token renovado exitosamente"),
        @ApiResponse(responseCode = "401", description = "Refresh token inválido o expirado")
    })
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> refreshToken(@RequestHeader(REFRESH_TOKEN_HEADER) String refreshToken) {
        LoginResponseDTO data = usuarioService.refreshToken(refreshToken);
        ApiResponseDTO<LoginResponseDTO> response = ApiResponseDTO.success("Token renovado exitosamente", data);
        return ResponseEntity.ok(response);
    }

  
    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión", 
               description = "Invalida el refresh token. El cliente debe eliminar el token de acceso localmente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sesión cerrada exitosamente"),
        @ApiResponse(responseCode = "401", description = "Refresh token inválido")
    })
    public ResponseEntity<ApiResponseDTO<Void>> logout(@RequestHeader(REFRESH_TOKEN_HEADER) String refreshToken) {
        usuarioService.logout(refreshToken);
        ApiResponseDTO<Void> response = ApiResponseDTO.success("Sesión cerrada exitosamente");
        return ResponseEntity.ok(response);
    }

  
    @GetMapping("/perfil/{id}")
    @Operation(summary = "Obtener perfil de usuario", 
               description = "Retorna la información completa de un usuario por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<ApiResponseDTO<UsuarioResponseDTO>> getPerfil(@PathVariable Integer id) {
        UsuarioResponseDTO data = usuarioService.getPerfil(id);
        ApiResponseDTO<UsuarioResponseDTO> response = ApiResponseDTO.success("Perfil encontrado", data);
        return ResponseEntity.ok(response);
    }

   
    @PutMapping("/perfil/{id}")
    @Operation(summary = "Actualizar perfil de usuario", 
               description = "Actualiza los datos del perfil de un usuario existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<ApiResponseDTO<UsuarioResponseDTO>> actualizarPerfil(@PathVariable Integer id,
                                                                                @RequestBody Usuario usuario) {
        UsuarioResponseDTO data = usuarioService.actualizarPerfil(id, usuario);
        ApiResponseDTO<UsuarioResponseDTO> response = ApiResponseDTO.success("Perfil actualizado exitosamente", data);
        return ResponseEntity.ok(response);
    }

   
    @PostMapping("/confirmar-email")
    @Operation(summary = "Confirmar email", 
               description = "Confirma el email del usuario mediante el código de verificación enviado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email confirmado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Código inválido o expirado")
    })
    public ResponseEntity<ApiResponseDTO<Void>> confirmarEmail(@RequestParam String codigo) {
        usuarioService.confirmarEmail(codigo);
        ApiResponseDTO<Void> response = ApiResponseDTO.success("Email confirmado exitosamente");
        return ResponseEntity.ok(response);
    }

   
    @PostMapping("/recuperar-password")
    @Operation(summary = "Recuperar contraseña", 
               description = "Envía un código de recuperación al email del usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Código enviado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Email no registrado")
    })
    public ResponseEntity<ApiResponseDTO<Void>> recuperarPassword(@RequestParam String email) {
        usuarioService.recuperarPassword(email);
        ApiResponseDTO<Void> response = ApiResponseDTO.success("Código de recuperación enviado al email");
        return ResponseEntity.ok(response);
    }

    
    @PostMapping("/reset-password")
    @Operation(summary = "Restablecer contraseña", 
               description = "Restablece la contraseña usando el código de recuperación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contraseña actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Código inválido o expirado")
    })
    public ResponseEntity<ApiResponseDTO<Void>> resetPassword(@RequestParam String codigo,
                                                               @RequestParam String nuevaPassword) {
        usuarioService.resetPassword(codigo, nuevaPassword);
        ApiResponseDTO<Void> response = ApiResponseDTO.success("Contraseña actualizada exitosamente");
        return ResponseEntity.ok(response);
    }
}