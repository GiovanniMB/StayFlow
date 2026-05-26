package com.StayFlow.controller;


import static com.StayFlow.security.SecurityConstants.REFRESH_TOKEN_HEADER;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.StayFlow.dto.request.LoginRequestDTO;
import com.StayFlow.dto.request.ReactivarCuentaRequestDTO;
import com.StayFlow.dto.request.RegistroRequestDTO;
import com.StayFlow.dto.response.ApiResponseDTO;
import com.StayFlow.dto.response.LoginResponseDTO;
import com.StayFlow.dto.response.UsuarioResponseDTO;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.model.Usuario;
import com.StayFlow.repository.UsuarioRepository;
import com.StayFlow.security.JwtUtil;
import com.StayFlow.service.interfaces.IUsuarioService;

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
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    @Value("${upload.dir:uploads/avatars/}")
    private String uploadDir;

    public UsuarioController(IUsuarioService usuarioService,
                             UsuarioRepository usuarioRepository,
                             JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
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

  
    @GetMapping("/perfil")
    @Operation(summary = "Obtener perfil del usuario autenticado", 
               description = "Retorna la información del usuario usando el token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<ApiResponseDTO<UsuarioResponseDTO>> getPerfilAutenticado(
            @RequestHeader("Authorization") String token) {
        
        String email = jwtUtil.extractEmail(token.substring(7));
        
        UsuarioResponseDTO data = usuarioService.getPerfilByEmail(email);
        ApiResponseDTO<UsuarioResponseDTO> response = ApiResponseDTO.success("Perfil encontrado", data);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/perfil/{id}")
    @Operation(summary = "Obtener perfil de usuario por ID (solo administradores)", 
               description = "Retorna la información completa de un usuario por su ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<UsuarioResponseDTO>> getPerfilById(@PathVariable Integer id) {
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

   
    @GetMapping("/confirmar-email")
    @Operation(summary = "Confirmar email", 
               description = "Confirma el email del usuario mediante el código de verificación enviado por email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email confirmado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Código inválido o expirado")
    })
    public ResponseEntity<ApiResponseDTO<Void>> confirmarEmail(@RequestParam String codigo) {
        usuarioService.confirmarEmail(codigo);
        return ResponseEntity.ok(ApiResponseDTO.success("Email confirmado exitosamente"));
    }

   
    @GetMapping("/recuperar-password")
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
    
    @PostMapping("/cuenta/desactivar")
    @Operation(summary = "Desactivar cuenta de usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cuenta desactivada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "La cuenta ya está desactivada")
    })
    public ResponseEntity<ApiResponseDTO<Void>> desactivarCuenta(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));
        
        usuarioService.desactivarCuenta(usuario.getIdUsuario());
        
        return ResponseEntity.ok(ApiResponseDTO.success("Cuenta desactivada exitosamente"));
    }
    
    @PostMapping("/cuenta/reactivar")
    @Operation(summary = "Reactivar cuenta desactivada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cuenta reactivada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Credenciales inválidas o cuenta ya activa"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<ApiResponseDTO<Void>> reactivarCuenta(@Valid @RequestBody ReactivarCuentaRequestDTO request) {
        usuarioService.reactivarCuenta(request);
        return ResponseEntity.ok(ApiResponseDTO.success("Cuenta reactivada exitosamente. Ya puedes iniciar sesión."));
    }
    
    
    
    @PostMapping("/avatar")
    @Operation(summary = "Subir imagen de perfil", 
               description = "Permite al usuario subir una imagen de perfil. La imagen se guarda en el servidor y se actualiza el avatarUrl.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imagen subida exitosamente"),
        @ApiResponse(responseCode = "400", description = "Archivo inválido o muy grande"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<ApiResponseDTO<String>> subirAvatar(
            @RequestHeader("Authorization") String token,
            @RequestParam("file") MultipartFile file) {
        

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error("No se ha seleccionado ningún archivo", 400));
        }
        

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error("El archivo debe ser una imagen", 400));
        }
        

        if (file.getSize() > 2 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.error("La imagen no debe superar los 2MB", 400));
        }
        
        try {

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            

            String extension = "";
            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + extension;
            Path filePath = uploadPath.resolve(fileName);
            

            Files.copy(file.getInputStream(), filePath);
            

            String imageUrl = "/uploads/avatars/" + fileName;
            

            String email = jwtUtil.extractEmail(token.substring(7));
            Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));
            

            String oldAvatar = usuario.getAvatarUrl();
            if (oldAvatar != null && oldAvatar.contains("/uploads/avatars/")) {
                String oldFileName = oldAvatar.substring(oldAvatar.lastIndexOf("/") + 1);
                Path oldFilePath = uploadPath.resolve(oldFileName);
                Files.deleteIfExists(oldFilePath);
            }
            
            usuario.setAvatarUrl(imageUrl);
            usuarioRepository.save(usuario);
            
            return ResponseEntity.ok(ApiResponseDTO.success("Imagen subida exitosamente", imageUrl));
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDTO.error("Error al guardar la imagen", 500));
        }
    }
    
    
    @DeleteMapping("/avatar")
    @Operation(summary = "Eliminar imagen de perfil", 
               description = "Elimina la imagen de perfil del usuario y restablece el avatarUrl a null")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imagen eliminada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<ApiResponseDTO<Void>> eliminarAvatar(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));
        

        String oldAvatar = usuario.getAvatarUrl();
        if (oldAvatar != null && oldAvatar.contains("/uploads/avatars/")) {
            try {
                Path uploadPath = Paths.get(uploadDir);
                String oldFileName = oldAvatar.substring(oldAvatar.lastIndexOf("/") + 1);
                Path oldFilePath = uploadPath.resolve(oldFileName);
                Files.deleteIfExists(oldFilePath);
            } catch (IOException e) {
                
            }
        }
        
        usuario.setAvatarUrl(null);
        usuarioRepository.save(usuario);
        
        return ResponseEntity.ok(ApiResponseDTO.success("Avatar eliminado exitosamente"));
    }

}