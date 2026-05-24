package com.StayFlow.dto.response;

import java.time.LocalDateTime;

public class MensajeChatResponseDTO {
    
    private Long id;
    private Integer idReserva;
    private Integer idRemitente;
    private String nombreRemitente; 
    private String contenido;
    private LocalDateTime fechaEnvio;
    private Boolean leido;

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getIdReserva() { return idReserva; }
    public void setIdReserva(Integer idReserva) { this.idReserva = idReserva; }

    public Integer getIdRemitente() { return idRemitente; }
    public void setIdRemitente(Integer idRemitente) { this.idRemitente = idRemitente; }

    public String getNombreRemitente() { return nombreRemitente; }
    public void setNombreRemitente(String nombreRemitente) { this.nombreRemitente = nombreRemitente; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public Boolean getLeido() { return leido; }
    public void setLeido(Boolean leido) { this.leido = leido; }
}