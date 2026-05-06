package com.StayFlow.dto.response;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta después de procesar un pago")
public class PagoResponseDTO {

    @Schema(description = "ID del pago generado", example = "1")
    private Integer idPago;

    @Schema(description = "ID de la reserva asociada", example = "123")
    private Integer idReserva;

    @Schema(description = "Método de pago utilizado", example = "tarjeta")
    private String metodoPago;

    @Schema(description = "Estado actual del pago", example = "completado")
    private String estadoPago;

    @Schema(description = "Monto pagado", example = "1500.00")
    private BigDecimal monto;

    @Schema(description = "Moneda del pago", example = "MXN")
    private String moneda;

    @Schema(description = "Fecha del pago", example = "2024-12-20T10:30:00")
    private String fechaPago;

    @Schema(description = "Mensaje descriptivo del resultado", example = "Pago procesado exitosamente")
    private String mensaje;

    @Schema(description = "ID de transacción del gateway", example = "txn_123456789")
    private String transaccionId;

    @Schema(description = "Últimos 4 dígitos (si aplica)", example = "4242")
    private String ultimosDigitos;

    // Getters y Setters
    public Integer getIdPago() { return idPago; }
    public void setIdPago(Integer idPago) { this.idPago = idPago; }
    public Integer getIdReserva() { return idReserva; }
    public void setIdReserva(Integer idReserva) { this.idReserva = idReserva; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public String getFechaPago() { return fechaPago; }
    public void setFechaPago(String fechaPago) { this.fechaPago = fechaPago; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public String getTransaccionId() { return transaccionId; }
    public void setTransaccionId(String transaccionId) { this.transaccionId = transaccionId; }
    public String getUltimosDigitos() { return ultimosDigitos; }
    public void setUltimosDigitos(String ultimosDigitos) { this.ultimosDigitos = ultimosDigitos; }
}