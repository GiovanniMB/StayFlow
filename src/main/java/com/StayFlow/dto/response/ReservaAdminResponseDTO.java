package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "DTO para administrador - Datos de reserva")
public class ReservaAdminResponseDTO {

    @Schema(description = "ID de la reserva", example = "1")
    private Integer idReserva;

    @Schema(description = "Nombre del cliente", example = "Juan Pérez")
    private String nombreCliente;

    @Schema(description = "ID del cliente", example = "10")
    private Integer idCliente;

    @Schema(description = "Nombre de la propiedad", example = "Hotel Paraíso")
    private String nombrePropiedad;

    @Schema(description = "ID de la propiedad", example = "5")
    private Integer idPropiedad;

    @Schema(description = "Fecha de entrada", example = "2024-06-15")
    private LocalDate fechaEntrada;

    @Schema(description = "Fecha de salida", example = "2024-06-20")
    private LocalDate fechaSalida;

    @Schema(description = "Monto total", example = "6000.00")
    private BigDecimal montoTotal;

    @Schema(description = "Estado de la reserva", example = "CONFIRMADA")
    private String estadoReserva;

    @Schema(description = "Número de noches", example = "5")
    private Integer noches;

    @Schema(description = "Fecha de creación de la reserva", example = "2024-06-01T10:30:00")
    private LocalDateTime fechaCreacion;

    // Constructor vacío
    public ReservaAdminResponseDTO() {}

    // Getters y Setters
    public Integer getIdReserva() { return idReserva; }
    public void setIdReserva(Integer idReserva) { this.idReserva = idReserva; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public String getNombrePropiedad() { return nombrePropiedad; }
    public void setNombrePropiedad(String nombrePropiedad) { this.nombrePropiedad = nombrePropiedad; }

    public Integer getIdPropiedad() { return idPropiedad; }
    public void setIdPropiedad(Integer idPropiedad) { this.idPropiedad = idPropiedad; }

    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(LocalDate fechaEntrada) { this.fechaEntrada = fechaEntrada; }

    public LocalDate getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDate fechaSalida) { this.fechaSalida = fechaSalida; }

    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }

    public String getEstadoReserva() { return estadoReserva; }
    public void setEstadoReserva(String estadoReserva) { this.estadoReserva = estadoReserva; }

    public Integer getNoches() { return noches; }
    public void setNoches(Integer noches) { this.noches = noches; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}