package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensajechat")
@Schema(description = "Entidad que representa los mensajes enviados en el chat entre usuarios sobre una reserva específica")
public class MensajeChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del mensaje", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idReserva", nullable = false)
    @Schema(description = "Reserva a la que pertenece esta conversación")
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "idRemitente", nullable = false)
    @Schema(description = "Usuario que envió el mensaje")
    private Usuario remitente;

    @Schema(description = "Contenido del mensaje", example = "Hola, ¿a qué hora puedo hacer el check-in?", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 255)
    private String contenido;

    @Schema(description = "Fecha y hora de envío del mensaje", example = "2024-12-20T10:30:00")
    @Column(columnDefinition = "DATETIME(6)")
    private LocalDateTime fechaEnvio = LocalDateTime.now();

    @Schema(description = "Indicador de si el mensaje ha sido leído por el destinatario", example = "false")
    private Boolean leido = false;

    // Constructores
    public MensajeChat() {}

    public MensajeChat(Reserva reserva, Usuario remitente, String contenido) {
        this.reserva = reserva;
        this.remitente = remitente;
        this.contenido = contenido;
        this.fechaEnvio = LocalDateTime.now();
        this.leido = false;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public Usuario getRemitente() {
        return remitente;
    }

    public void setRemitente(Usuario remitente) {
        this.remitente = remitente;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Boolean getLeido() {
        return leido;
    }

    public void setLeido(Boolean leido) {
        this.leido = leido;
    }
}