package com.StayFlow.dto.response;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para mostrar reservas finalizadas pendientes de reseñar")
public class ResenaPendienteResponseDTO {

    private Integer idReserva;
    private Integer idPropiedad;
    private String nombrePropiedad;
    private Integer idHuesped;
    private String nombreHuesped;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private String tipoPendiente;
    private String mensaje;

    public ResenaPendienteResponseDTO() {
    }

    public ResenaPendienteResponseDTO(Integer idReserva,
                                      Integer idPropiedad,
                                      String nombrePropiedad,
                                      Integer idHuesped,
                                      String nombreHuesped,
                                      LocalDate fechaEntrada,
                                      LocalDate fechaSalida,
                                      String tipoPendiente,
                                      String mensaje) {
        this.idReserva = idReserva;
        this.idPropiedad = idPropiedad;
        this.nombrePropiedad = nombrePropiedad;
        this.idHuesped = idHuesped;
        this.nombreHuesped = nombreHuesped;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.tipoPendiente = tipoPendiente;
        this.mensaje = mensaje;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
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

    public Integer getIdHuesped() {
        return idHuesped;
    }

    public void setIdHuesped(Integer idHuesped) {
        this.idHuesped = idHuesped;
    }

    public String getNombreHuesped() {
        return nombreHuesped;
    }

    public void setNombreHuesped(String nombreHuesped) {
        this.nombreHuesped = nombreHuesped;
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

    public String getTipoPendiente() {
        return tipoPendiente;
    }

    public void setTipoPendiente(String tipoPendiente) {
        this.tipoPendiente = tipoPendiente;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}