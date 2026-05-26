package com.StayFlow.dto.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Solicitud para procesar un pago")
public class PagoRequestDTO {

    @NotNull(message = "El ID de reserva es obligatorio")
    @Schema(description = "ID de la reserva a pagar", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idReserva;

    @NotNull(message = "El método de pago es obligatorio")
    @Schema(description = "ID del método de pago (1:tarjeta, 2:transferencia, 3:efectivo, 4:paypal)", 
            example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idMetodoPago;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    @Schema(description = "Monto a pagar", example = "1500.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal monto;

    @Pattern(regexp = "^(MXN|USD|EUR)$", message = "Moneda debe ser MXN, USD o EUR")
    @Schema(description = "Código de moneda (ISO 4217)", example = "MXN", defaultValue = "MXN")
    private String moneda = "MXN";

    @Schema(description = "Token del gateway de pagos (para tarjeta/PayPal)", example = "tok_visa_4242")
    private String tokenGateway;

    @Schema(description = "ID de token guardado (opcional, para pagos one-click)", example = "5")
    private Integer idPagoToken;

    // Getters y Setters
    public Integer getIdReserva() { return idReserva; }
    public void setIdReserva(Integer idReserva) { this.idReserva = idReserva; }
    public Integer getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(Integer idMetodoPago) { this.idMetodoPago = idMetodoPago; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public String getTokenGateway() { return tokenGateway; }
    public void setTokenGateway(String tokenGateway) { this.tokenGateway = tokenGateway; }
    public Integer getIdPagoToken() { return idPagoToken; }
    public void setIdPagoToken(Integer idPagoToken) { this.idPagoToken = idPagoToken; }
}