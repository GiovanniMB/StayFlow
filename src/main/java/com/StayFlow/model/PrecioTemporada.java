package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "preciotemporada")
@Schema(description = "Precios especiales para tipos de habitación durante temporadas específicas (fin de año, vacaciones, etc.)")
public class PrecioTemporada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del precio de temporada", example = "1")
    private Integer idPrecioTemporada;

    @ManyToOne
    @JoinColumn(name = "idTipoHabitacion", nullable = false)
    @Schema(description = "Tipo de habitación al que aplica este precio especial")
    private TipoHabitacion tipoHabitacion;

    @Schema(description = "Fecha de inicio de la temporada", example = "2024-12-20", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin de la temporada", example = "2025-01-10", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private LocalDate fechaFin;

    @Schema(description = "Precio especial por noche durante esta temporada", example = "2500.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioEspecial;

    @Schema(description = "Estado de eliminación lógica", example = "false")
    private boolean estaEliminado = false;

    // Constructores
    public PrecioTemporada() {}

    public PrecioTemporada(TipoHabitacion tipoHabitacion, LocalDate fechaInicio, 
                           LocalDate fechaFin, BigDecimal precioEspecial) {
        this.tipoHabitacion = tipoHabitacion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.precioEspecial = precioEspecial;
    }

    // Getters y Setters
    public Integer getIdPrecioTemporada() {
        return idPrecioTemporada;
    }

    public void setIdPrecioTemporada(Integer idPrecioTemporada) {
        this.idPrecioTemporada = idPrecioTemporada;
    }

    public TipoHabitacion getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(TipoHabitacion tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigDecimal getPrecioEspecial() {
        return precioEspecial;
    }

    public void setPrecioEspecial(BigDecimal precioEspecial) {
        this.precioEspecial = precioEspecial;
    }

    public boolean isEstaEliminado() {
        return estaEliminado;
    }

    public void setEstaEliminado(boolean estaEliminado) {
        this.estaEliminado = estaEliminado;
    }
}