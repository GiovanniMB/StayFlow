package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "refreshtoken")
@Schema(description = "Entidad que representa los tokens de refresco para renovar el acceso")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del refresh token", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Token de refresco", example = "eyJhbGciOiJIUzI1NiIs...")
    private String token;

    @Column(name = "fecha_expiracion", nullable = false)  
    @Schema(description = "Fecha de expiración del token")
    private LocalDateTime fechaExpiracion;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)  
    @Schema(description = "Usuario al que pertenece este refresh token")
    private Usuario usuario;

    @Column(nullable = false)
    @Schema(description = "Token activo", example = "true")
    private boolean activo = true;

    public RefreshToken() {}

    public RefreshToken(String token, LocalDateTime fechaExpiracion, Usuario usuario) {
        this.token = token;
        this.fechaExpiracion = fechaExpiracion;
        this.usuario = usuario;
        this.activo = true;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public LocalDateTime getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(LocalDateTime fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}