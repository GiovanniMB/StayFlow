package com.StayFlow.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reserva")
@Schema(description = "Registro de reservaciones realizadas por clientes en habitaciones específicas")
public class Reserva extends AuditoriaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la reserva", example = "1")
    private Integer idReserva;

   @ManyToOne
    @JoinColumn(name = "idHabitacion", nullable = false)
    @Schema(description = "Habitación reservada")
    @JsonIgnoreProperties({"reservas", "propietario"})
    private Habitacion habitacion;

    @ManyToOne
    @JoinColumn(name = "idCliente", nullable = false)
    @Schema(description = "Usuario cliente que realiza la reserva")
    @JsonIgnoreProperties({"reservas", "password", "rol"})
    private Usuario cliente;

    @Schema(description = "Fecha de entrada (check-in)", example = "2024-12-20", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private LocalDate fechaEntrada;

    @Schema(description = "Fecha de salida (check-out)", example = "2024-12-25", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private LocalDate fechaSalida;

    @Schema(description = "Monto total de la reserva", example = "7500.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Estado actual de la reserva", example = "confirmada", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private EstadoReserva estadoReserva = EstadoReserva.pendiente;

    // Enumeración para los estados de reserva
    public enum EstadoReserva {
        @Schema(description = "Reserva pendiente de confirmación")
        pendiente,
        @Schema(description = "Reserva confirmada por el anfitrión")
        confirmada,
        @Schema(description = "Cliente ya realizó check-in")
        check_in,
        @Schema(description = "Cliente realizó check-out")
        check_out,
        @Schema(description = "Reserva cancelada")
        cancelada
    }

    // Constructores
    public Reserva() {}

    public Reserva(Habitacion habitacion, Usuario cliente, LocalDate fechaEntrada, 
                   LocalDate fechaSalida, BigDecimal montoTotal, EstadoReserva estadoReserva) {
        this.habitacion = habitacion;
        this.cliente = cliente;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.montoTotal = montoTotal;
        this.estadoReserva = estadoReserva != null ? estadoReserva : EstadoReserva.pendiente;
    }

    // Getters y Setters
    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
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

    public EstadoReserva getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(EstadoReserva estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    // Métodos de utilidad
    public long getNumeroNoches() {
        return java.time.temporal.ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
    }

    public boolean isActiva() {
        return estadoReserva == EstadoReserva.confirmada || 
               estadoReserva == EstadoReserva.pendiente;
    }

    public boolean isEnCurso() {
        LocalDate hoy = LocalDate.now();
        return estadoReserva == EstadoReserva.confirmada && 
               !fechaEntrada.isAfter(hoy) && 
               !fechaSalida.isBefore(hoy);
    }
}