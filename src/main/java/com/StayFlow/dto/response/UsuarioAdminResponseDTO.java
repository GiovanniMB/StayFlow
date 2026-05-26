package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "DTO para administrador - Datos de usuario con control de estado")
public class UsuarioAdminResponseDTO {

    @Schema(description = "ID único del usuario", example = "1")
    private Integer idUsuario;

    @Schema(description = "Nombre completo del usuario", example = "Carlos García López")
    private String nombreCompleto;

    @Schema(description = "Correo electrónico", example = "carlos@mail.com")
    private String email;

    @Schema(description = "Teléfono de contacto", example = "5512345678")
    private String telefono;

    @Schema(description = "Rol del usuario", example = "arrendador")
    private String rol;

    @Schema(description = "Estado de la cuenta", example = "true")
    private boolean activo;

    @Schema(description = "Email confirmado", example = "true")
    private boolean emailConfirmado;

    @Schema(description = "Fecha de registro", example = "2024-01-15T10:30:00")
    private LocalDateTime fechaRegistro;

    @Schema(description = "Cantidad de propiedades publicadas", example = "3")
    private Integer cantidadPropiedades;

    @Schema(description = "Cantidad de reservas realizadas", example = "12")
    private Integer cantidadReservas;

    // Constructor vacío
    public UsuarioAdminResponseDTO() {}

    // Constructor con campos principales
    public UsuarioAdminResponseDTO(Integer idUsuario, String nombreCompleto, String email, 
                                   String rol, boolean activo, LocalDateTime fechaRegistro) {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.rol = rol;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public boolean isEmailConfirmado() { return emailConfirmado; }
    public void setEmailConfirmado(boolean emailConfirmado) { this.emailConfirmado = emailConfirmado; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public Integer getCantidadPropiedades() { return cantidadPropiedades; }
    public void setCantidadPropiedades(Integer cantidadPropiedades) { this.cantidadPropiedades = cantidadPropiedades; }

    public Integer getCantidadReservas() { return cantidadReservas; }
    public void setCantidadReservas(Integer cantidadReservas) { this.cantidadReservas = cantidadReservas; }
}