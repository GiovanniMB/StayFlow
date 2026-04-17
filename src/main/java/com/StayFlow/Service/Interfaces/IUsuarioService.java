package com.StayFlow.service.interfaces;

import com.StayFlow.dto.request.LoginRequestDTO;
import com.StayFlow.dto.request.ReactivarCuentaRequestDTO;
import com.StayFlow.dto.request.RegistroRequestDTO;
import com.StayFlow.dto.response.LoginResponseDTO;
import com.StayFlow.dto.response.UsuarioResponseDTO;
import com.StayFlow.model.Usuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Interfaz de servicios para la gestión de usuarios del sistema")
public interface IUsuarioService {

    @Operation(summary = "Registrar un nuevo usuario", description = "Crea una cuenta nueva y envía código de confirmación por email")
    UsuarioResponseDTO registrar(RegistroRequestDTO request);

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y genera tokens JWT de acceso y refresco")
    LoginResponseDTO login(LoginRequestDTO request);

    @Operation(summary = "Renovar token de acceso", description = "Utiliza un refresh token válido para generar un nuevo access token")
    LoginResponseDTO refreshToken(String refreshToken);

    @Operation(summary = "Cerrar sesión", description = "Invalida el refresh token del usuario")
    void logout(String refreshToken);

    @Operation(summary = "Obtener perfil", description = "Retorna los datos completos de un usuario por su ID")
    UsuarioResponseDTO getPerfil(Integer idUsuario);

    @Operation(summary = "Actualizar perfil", description = "Actualiza los datos del perfil de usuario")
    UsuarioResponseDTO actualizarPerfil(Integer idUsuario, Usuario usuarioActualizado);

    @Operation(summary = "Confirmar email", description = "Activa la cuenta del usuario mediante código de verificación")
    void confirmarEmail(String codigo);

    @Operation(summary = "Recuperar contraseña", description = "Envía un código de recuperación al email del usuario")
    void recuperarPassword(String email);

    @Operation(summary = "Restablecer contraseña", description = "Cambia la contraseña usando el código de recuperación")
    void resetPassword(String codigo, String nuevaPassword);
    
    @Operation(summary = "Desactivar cuenta de usuario", description = "Realiza un soft delete de la cuenta.")
    void desactivarCuenta(Integer idUsuario);

    @Operation(summary = "Reactivar cuenta de usuario", description = "Re-activa una cuenta previamente desactivada.")
    void reactivarCuenta(ReactivarCuentaRequestDTO request);
}