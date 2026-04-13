package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "DTO para la respuesta de datos de usuario (sin campos sensibles como passwordHash)")
public class UsuarioResponseDTO {

    @Schema(description = "ID único del usuario", example = "1")
    private Integer idUsuario;

    @Schema(description = "Nombre del usuario", example = "Carlos")
    private String nombre;

    @Schema(description = "Apellido paterno", example = "García")
    private String apellidoPaterno;

    @Schema(description = "Apellido materno", example = "López")
    private String apellidoMaterno;

    @Schema(description = "Correo electrónico", example = "carlos@mail.com")
    private String email;

    @Schema(description = "URL de la foto de perfil", example = "https://storage.com/avatar.jpg")
    private String avatarUrl;

    @Schema(description = "Teléfono de contacto", example = "5512345678")
    private String telefono;

    @Schema(description = "Email confirmado", example = "true")
    private boolean emailConfirmado;

    @Schema(description = "Lista de roles del usuario", example = "[\"arrendatario\"]")
    private List<String> roles;

    @Schema(description = "Fecha de registro", example = "2024-01-15T10:30:00")
    private LocalDateTime fechaRegistro;

    @Schema(description = "Fecha de última actualización", example = "2024-01-20T15:45:00")
    private LocalDateTime fechaActualizacion;

    public UsuarioResponseDTO() {}


    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }

    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public boolean isEmailConfirmado() { return emailConfirmado; }
    public void setEmailConfirmado(boolean emailConfirmado) { this.emailConfirmado = emailConfirmado; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}