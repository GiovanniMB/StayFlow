package com.StayFlow.dto.response;

import java.time.LocalDate;

public class DisponibilidadResponseDTO {

    private Integer idHabitacion;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private boolean disponible;
    private String mensaje;

    // 1. Constructor vacío (Nativo)
    public DisponibilidadResponseDTO() {
    }

    // 2. Constructor con todos los parámetros (Nativo)
    public DisponibilidadResponseDTO(Integer idHabitacion, LocalDate fechaEntrada, LocalDate fechaSalida, boolean disponible, String mensaje) {
        this.idHabitacion = idHabitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.disponible = disponible;
        this.mensaje = mensaje;
    }

    // 3. Getters y Setters (Nativos)

    public Integer getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(Integer idHabitacion) {
        this.idHabitacion = idHabitacion;
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

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}