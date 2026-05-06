package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago_token")
@Schema(description = "Tokens de pago almacenados para usuarios")
public class PagoToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del token de pago", example = "1")
    private Integer idPagoToken;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    @Schema(description = "Usuario propietario del método de pago guardado")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "idMetodoPago", nullable = false)
    @Schema(description = "Método de pago asociado")
    private MetodoPago metodoPago;

    @ManyToOne
    @JoinColumn(name = "idTipoTarjeta", nullable = false)
    @Schema(description = "Tipo de tarjeta")
    private TipoTarjeta tipoTarjeta;

    @Column(name = "token_gateway", nullable = false, length = 255)
    @Schema(description = "Token proporcionado por el gateway de pagos")
    private String tokenGateway;

    @Column(name = "ultimosDigitos", nullable = false, length = 4)  // 🔥 SIN guión bajo
    @Schema(description = "Últimos 4 dígitos de la tarjeta")
    private String ultimosDigitos;

    @Column(name = "nombreTitular", length = 100)  // 🔥 SIN guión bajo
    @Schema(description = "Nombre del titular de la tarjeta")
    private String nombreTitular;

    @Column(name = "fechaExpiracion", length = 7)  // 🔥 SIN guión bajo
    @Schema(description = "Fecha de expiración de la tarjeta (MM/YYYY)")
    private String fechaExpiracion;

    @Column(name = "activo")
    @Schema(description = "Indica si el token está activo")
    private boolean activo = true;

    @Column(name = "fechaCreacion", nullable = false)  // 🔥 SIN guión bajo, como está en BD
    @Schema(description = "Fecha y hora de creación del token")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Constructores
    public PagoToken() {}

    public PagoToken(Usuario usuario, MetodoPago metodoPago, TipoTarjeta tipoTarjeta,
                     String tokenGateway, String ultimosDigitos, String nombreTitular, 
                     String fechaExpiracion) {
        this.usuario = usuario;
        this.metodoPago = metodoPago;
        this.tipoTarjeta = tipoTarjeta;
        this.tokenGateway = tokenGateway;
        this.ultimosDigitos = ultimosDigitos;
        this.nombreTitular = nombreTitular;
        this.fechaExpiracion = fechaExpiracion;
        this.fechaCreacion = LocalDateTime.now();
        this.activo = true;
    }

    // Getters y Setters (mantén los que ya tienes)
    public Integer getIdPagoToken() {
        return idPagoToken;
    }

    public void setIdPagoToken(Integer idPagoToken) {
        this.idPagoToken = idPagoToken;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public TipoTarjeta getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(TipoTarjeta tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    public String getTokenGateway() {
        return tokenGateway;
    }

    public void setTokenGateway(String tokenGateway) {
        this.tokenGateway = tokenGateway;
    }

    public String getUltimosDigitos() {
        return ultimosDigitos;
    }

    public void setUltimosDigitos(String ultimosDigitos) {
        this.ultimosDigitos = ultimosDigitos;
    }

    public String getNombreTitular() {
        return nombreTitular;
    }

    public void setNombreTitular(String nombreTitular) {
        this.nombreTitular = nombreTitular;
    }

    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}