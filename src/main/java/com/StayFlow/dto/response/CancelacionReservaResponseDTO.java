package com.StayFlow.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta para cancelación de reserva con penalización")
public class CancelacionReservaResponseDTO {

    private Integer idReserva;
    private String estadoReserva;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private BigDecimal montoTotal;
    private Integer porcentajePenalizacion;
    private BigDecimal montoPenalizacion;
    private BigDecimal montoReembolso;
    private String mensaje;

    public CancelacionReservaResponseDTO() {
    }

    public CancelacionReservaResponseDTO(Integer idReserva,
                                         String estadoReserva,
                                         LocalDate fechaEntrada,
                                         LocalDate fechaSalida,
                                         BigDecimal montoTotal,
                                         Integer porcentajePenalizacion,
                                         BigDecimal montoPenalizacion,
                                         BigDecimal montoReembolso,
                                         String mensaje) {
        this.idReserva = idReserva;
        this.estadoReserva = estadoReserva;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.montoTotal = montoTotal;
        this.porcentajePenalizacion = porcentajePenalizacion;
        this.montoPenalizacion = montoPenalizacion;
        this.montoReembolso = montoReembolso;
        this.mensaje = mensaje;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public String getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(String estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Integer getPorcentajePenalizacion() {
        return porcentajePenalizacion;
    }

    public void setPorcentajePenalizacion(Integer porcentajePenalizacion) {
        this.porcentajePenalizacion = porcentajePenalizacion;
    }

    public BigDecimal getMontoPenalizacion() {
        return montoPenalizacion;
    }

    public void setMontoPenalizacion(BigDecimal montoPenalizacion) {
        this.montoPenalizacion = montoPenalizacion;
    }

    public BigDecimal getMontoReembolso() {
        return montoReembolso;
    }

    public void setMontoReembolso(BigDecimal montoReembolso) {
        this.montoReembolso = montoReembolso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}