package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuario")
@Schema(description = "Entidad que representa a los usuarios del sistema (arrendadores, arrendatarios, administradores)")
public class Usuario extends AuditoriaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del usuario", example = "1")
    private Integer idUsuario;

    @Schema(description = "Nombre de pila del usuario", example = "Carlos", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 50)
    private String nombre;

    @Schema(description = "Apellido paterno", example = "García", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 50)
    private String apellidoPaterno;

    @Schema(description = "Apellido materno", example = "López")
    @Column(length = 50)
    private String apellidoMaterno;

    @Schema(description = "Correo electrónico único para inicio de sesión", example = "carlos@mail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Schema(description = "Hash de la contraseña (BCrypt recommended)", example = "$2a$10$...", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private String passwordHash;

    @Schema(description = "URL de la foto de perfil del usuario", example = "https://storage.com/avatars/user123.jpg")
    @Column(length = 255)
    private String avatarUrl;

    @Schema(description = "Teléfono de contacto del usuario", example = "5512345678")
    @Column(length = 20)
    private String telefono;

    @Schema(description = "Código de confirmación para verificar el email (6 dígitos)", example = "123456")
    @Column(length = 6)
    private String codigoConfirmacion;

    @Schema(description = "Fecha y hora de expiración del código de confirmación")
    private LocalDateTime codigoExpiracion;

    @Schema(description = "Indica si el email del usuario ha sido verificado", example = "true")
    private boolean emailConfirmado = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_rol",
        joinColumns = @JoinColumn(name = "idUsuario"),
        inverseJoinColumns = @JoinColumn(name = "idRol")
    )
    @Schema(description = "Lista de roles asignados al usuario (arrendador, arrendatario, administrador)")
    private List<Rol> roles;

    // Constructores
    public Usuario() {}

    public Usuario(String nombre, String apellidoPaterno, String apellidoMaterno, 
                   String email, String passwordHash, String telefono) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.email = email;
        this.passwordHash = passwordHash;
        this.telefono = telefono;
        this.emailConfirmado = false;
    }

    // Getters y Setters
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCodigoConfirmacion() {
        return codigoConfirmacion;
    }

    public void setCodigoConfirmacion(String codigoConfirmacion) {
        this.codigoConfirmacion = codigoConfirmacion;
    }

    public LocalDateTime getCodigoExpiracion() {
        return codigoExpiracion;
    }

    public void setCodigoExpiracion(LocalDateTime codigoExpiracion) {
        this.codigoExpiracion = codigoExpiracion;
    }

    public boolean isEmailConfirmado() {
        return emailConfirmado;
    }

    public void setEmailConfirmado(boolean emailConfirmado) {
        this.emailConfirmado = emailConfirmado;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public String getNombreCompleto() {
        if (apellidoMaterno != null && !apellidoMaterno.isEmpty()) {
            return nombre + " " + apellidoPaterno + " " + apellidoMaterno;
        }
        return nombre + " " + apellidoPaterno;
    }

    public boolean hasRol(String nombreRol) {
        if (roles == null) return false;
        return roles.stream().anyMatch(rol -> rol.getNombreRol().equalsIgnoreCase(nombreRol));
    }

    public boolean isCodigoValido(String codigo) {
        return codigoConfirmacion != null && 
               codigoConfirmacion.equals(codigo) && 
               codigoExpiracion != null && 
               codigoExpiracion.isAfter(LocalDateTime.now());
    }
}