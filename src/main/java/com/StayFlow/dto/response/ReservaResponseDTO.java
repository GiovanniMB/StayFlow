package com.StayFlow.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta para mostrar información de una reserva")
public class ReservaResponseDTO {

    @Schema(description = "ID único de la reserva", example = "1")
    private Integer idReserva;

    @Schema(description = "ID de la habitación reservada", example = "5")
    private Integer idHabitacion;

    @Schema(description = "Número o identificador físico de la habitación", example = "Primer Piso")
    private String numeroHabitacion;

    @Schema(description = "ID del cliente que realizó la reserva", example = "3")
    private Integer idCliente;

    @Schema(description = "Nombre completo del cliente", example = "Carlos Pérez")
    private String nombreCliente;

    @Schema(description = "Fecha de entrada de la reserva", example = "2026-05-24")
    private LocalDate fechaEntrada;

    @Schema(description = "Fecha de salida de la reserva", example = "2026-05-29")
    private LocalDate fechaSalida;

    @Schema(description = "Monto total calculado para la reserva", example = "2500.00")
    private BigDecimal montoTotal;

    @Schema(description = "Estado actual de la reserva", example = "pendiente")
    private String estadoReserva;

    @Schema(description = "ID de la propiedad asociada a la habitación reservada", example = "1")
    private Integer idPropiedad;

    @Schema(description = "Nombre comercial de la propiedad", example = "Casa Cosimo editado")
    private String nombrePropiedad;

    @Schema(description = "URL de la fotografía principal o primera fotografía de la propiedad")
    private String urlFotoPropiedad;

    @Schema(description = "Ubicación general de la propiedad")
    private String ubicacionPropiedad;

    public ReservaResponseDTO() {
    }

    public ReservaResponseDTO(Integer idReserva,
                              Integer idHabitacion,
                              String numeroHabitacion,
                              Integer idCliente,
                              String nombreCliente,
                              LocalDate fechaEntrada,
                              LocalDate fechaSalida,
                              BigDecimal montoTotal,
                              String estadoReserva,
                              Integer idPropiedad,
                              String nombrePropiedad,
                              String urlFotoPropiedad,
                              String ubicacionPropiedad) {
        this.idReserva = idReserva;
        this.idHabitacion = idHabitacion;
        this.numeroHabitacion = numeroHabitacion;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.montoTotal = montoTotal;
        this.estadoReserva = estadoReserva;
        this.idPropiedad = idPropiedad;
        this.nombrePropiedad = nombrePropiedad;
        this.urlFotoPropiedad = urlFotoPropiedad;
        this.ubicacionPropiedad = ubicacionPropiedad;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public Integer getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(Integer idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public String getNumeroHabitacion() {
        return numeroHabitacion;
    }

    public void setNumeroHabitacion(String numeroHabitacion) {
        this.numeroHabitacion = numeroHabitacion;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
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

    public String getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(String estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    public Integer getIdPropiedad() {
        return idPropiedad;
    }

    public void setIdPropiedad(Integer idPropiedad) {
        this.idPropiedad = idPropiedad;
    }

    public String getNombrePropiedad() {
        return nombrePropiedad;
    }

    public void setNombrePropiedad(String nombrePropiedad) {
        this.nombrePropiedad = nombrePropiedad;
    }

    public String getUrlFotoPropiedad() {
        return urlFotoPropiedad;
    }

    public void setUrlFotoPropiedad(String urlFotoPropiedad) {
        this.urlFotoPropiedad = urlFotoPropiedad;
    }

    public String getUbicacionPropiedad() {
        return ubicacionPropiedad;
    }

    public void setUbicacionPropiedad(String ubicacionPropiedad) {
        this.ubicacionPropiedad = ubicacionPropiedad;
    }
}