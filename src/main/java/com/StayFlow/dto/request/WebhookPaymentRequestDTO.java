package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload recibido de pasarelas externas (webhook)")
public class WebhookPaymentRequestDTO {

    @Schema(description = "Evento del gateway", example = "payment.completed")
    private String event;

    @Schema(description = "ID de transacción del gateway", example = "txn_123456789")
    private String transactionId;

    @Schema(description = "ID interno del pago (si lo conocen)")
    private Integer idPago;

    @Schema(description = "Gateway de origen", example = "stripe")
    private String gateway;

    @Schema(description = "Nuevo estado", example = "succeeded")
    private String status;

    // Getters y Setters
    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public Integer getIdPago() { return idPago; }
    public void setIdPago(Integer idPago) { this.idPago = idPago; }
    public String getGateway() { return gateway; }
    public void setGateway(String gateway) { this.gateway = gateway; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}