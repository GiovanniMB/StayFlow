package com.StayFlow.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MensajeChatRequestDTO {

    @NotNull(message = "El ID de la reserva es obligatorio")
    private Integer idReserva;

    @NotNull(message = "El ID del remitente es obligatorio")
    private Integer idRemitente;

    @NotBlank(message = "El contenido del mensaje no puede estar vacío")
    private String contenido;

    // getters y setters
    public Integer getIdReserva() { return idReserva; }
    public void setIdReserva(Integer idReserva) { this.idReserva = idReserva; }

    public Integer getIdRemitente() { return idRemitente; }
    public void setIdRemitente(Integer idRemitente) { this.idRemitente = idRemitente; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
}