package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
@Schema(description = "Registro de pagos asociados a las reservaciones del sistema (normalizado con catálogos)")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del pago", example = "1")
    private Integer idPago;

    @ManyToOne
    @JoinColumn(name = "idReserva", nullable = false)
    @Schema(description = "Reserva a la que pertenece este pago")
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "idMetodoPago", nullable = false)
    @Schema(description = "Método de pago utilizado (relación con catálogo metodo_pago)")
    private MetodoPago metodoPago;

    @ManyToOne
    @JoinColumn(name = "idEstadoPago", nullable = false)
    @Schema(description = "Estado actual del pago (relación con catálogo estado_pago)")
    private EstadoPago estadoPago;

    @Schema(description = "Monto del pago", example = "1500.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Schema(description = "Código de moneda (ISO 4217)", example = "MXN", defaultValue = "MXN")
    @Column(length = 3)
    private String moneda = "MXN";

    @Schema(description = "Fecha y hora en que se realizó el pago", example = "2024-12-20T10:30:00")
    @Column(nullable = false)
    private LocalDateTime fechaPago = LocalDateTime.now();

    @Schema(description = "Fecha y hora de la última actualización del pago")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    @Schema(description = "Estado de eliminación lógica", example = "false")
    private boolean estaEliminado = false;

    // Constructores
    public Pago() {}

    public Pago(Reserva reserva, MetodoPago metodoPago, EstadoPago estadoPago, 
                BigDecimal monto, String moneda) {
        this.reserva = reserva;
        this.metodoPago = metodoPago;
        this.estadoPago = estadoPago;
        this.monto = monto;
        this.moneda = moneda != null ? moneda : "MXN";
        this.fechaPago = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getIdPago() {
        return idPago;
    }

    public void setIdPago(Integer idPago) {
        this.idPago = idPago;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public boolean isEstaEliminado() {
        return estaEliminado;
    }

    public void setEstaEliminado(boolean estaEliminado) {
        this.estaEliminado = estaEliminado;
    }
}