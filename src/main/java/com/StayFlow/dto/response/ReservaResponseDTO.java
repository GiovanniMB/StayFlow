package com.StayFlow.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReservaResponseDTO {

    private Integer idReserva;
    private Integer idHabitacion;
    private String numeroHabitacion;
    private Integer idCliente;
    private String nombreCliente;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private BigDecimal montoTotal;
    private String estadoReserva;

    // Constructor vacío
    public ReservaResponseDTO() {
    }

    // Constructor completo
    public ReservaResponseDTO(Integer idReserva, Integer idHabitacion, String numeroHabitacion, Integer idCliente, String nombreCliente, LocalDate fechaEntrada, LocalDate fechaSalida, BigDecimal montoTotal, String estadoReserva) {
        this.idReserva = idReserva;
        this.idHabitacion = idHabitacion;
        this.numeroHabitacion = numeroHabitacion;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.montoTotal = montoTotal;
        this.estadoReserva = estadoReserva;
    }

    // --- TODOS LOS GETTERS Y SETTERS ---

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
}