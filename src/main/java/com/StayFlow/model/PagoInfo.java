package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "pago_info")
@Schema(description = "Información adicional y no sensible de los pagos (últimos dígitos de tarjeta, referencias de transferencia, tokens, etc.)")
public class PagoInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del detalle de pago", example = "1")
    private Integer idPagoInfo;

    @ManyToOne
    @JoinColumn(name = "idPago", nullable = false)
    @Schema(description = "Pago al que pertenece esta información adicional")
    private Pago pago;

    @Schema(description = "Clave o tipo de información", example = "ultimosDigitos", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 50)
    private String clave;

    @Schema(description = "Valor de la información", example = "4242", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 255)
    private String valor;

    // Constructores
    public PagoInfo() {}

    public PagoInfo(Pago pago, String clave, String valor) {
        this.pago = pago;
        this.clave = clave;
        this.valor = valor;
    }

    // Getters y Setters
    public Integer getIdPagoInfo() {
        return idPagoInfo;
    }

    public void setIdPagoInfo(Integer idPagoInfo) {
        this.idPagoInfo = idPagoInfo;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}