package com.StayFlow.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

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
    
    // UI Fields (Para renderizado de tarjetas en frontend)
    private Integer idPropiedad;
    private String nombrePropiedad;
    private String imagenPortada;

    private LocalTime horaCheckIn;
    private LocalTime horaCheckOut;

    public ReservaResponseDTO() {}

    public ReservaResponseDTO(Integer idReserva, Integer idHabitacion, String numeroHabitacion, Integer idCliente, String nombreCliente, LocalDate fechaEntrada, LocalDate fechaSalida, BigDecimal montoTotal, String estadoReserva, Integer idPropiedad, String nombrePropiedad, String imagenPortada, LocalTime horaCheckIn, LocalTime horaCheckOut) {
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
        this.imagenPortada = imagenPortada;
        this.horaCheckIn = horaCheckIn;
        this.horaCheckOut = horaCheckOut;
    }

    // Getters y Setters

    public Integer getIdReserva() { return idReserva; }
    public void setIdReserva(Integer idReserva) { this.idReserva = idReserva; }

    public Integer getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(Integer idHabitacion) { this.idHabitacion = idHabitacion; }

    public String getNumeroHabitacion() { return numeroHabitacion; }
    public void setNumeroHabitacion(String numeroHabitacion) { this.numeroHabitacion = numeroHabitacion; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(LocalDate fechaEntrada) { this.fechaEntrada = fechaEntrada; }

    public LocalDate getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDate fechaSalida) { this.fechaSalida = fechaSalida; }

    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }

    public String getEstadoReserva() { return estadoReserva; }
    public void setEstadoReserva(String estadoReserva) { this.estadoReserva = estadoReserva; }

    public Integer getIdPropiedad() { return idPropiedad; }
    public void setIdPropiedad(Integer idPropiedad) { this.idPropiedad = idPropiedad; }

    public String getNombrePropiedad() { return nombrePropiedad; }
    public void setNombrePropiedad(String nombrePropiedad) { this.nombrePropiedad = nombrePropiedad; }

    public String getImagenPortada() { return imagenPortada; }
    public void setImagenPortada(String imagenPortada) { this.imagenPortada = imagenPortada; }

    public LocalTime getHoraCheckIn() { return horaCheckIn; }
    public void setHoraCheckIn(LocalTime horaCheckIn) { this.horaCheckIn = horaCheckIn; }

    public LocalTime getHoraCheckOut() { return horaCheckOut; }
    public void setHoraCheckOut(LocalTime horaCheckOut) { this.horaCheckOut = horaCheckOut; }
}