package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "logsistema")
@Schema(description = "Bitácora de auditoría que registra todas las acciones importantes realizadas en el sistema")
public class LogSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del registro de log", example = "1")
    private Integer idLogSistema;

    @Schema(description = "Nombre de la tabla de base de datos afectada por la acción", example = "usuario", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 50)
    private String tablaAfectada;

    @Schema(description = "ID del registro que fue afectado en la tabla", example = "45", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private Integer idRegistroAfectado;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Tipo de acción realizada", example = "INSERT", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private Accion accion;

    @ManyToOne
    @JoinColumn(name = "idUsuarioAccion")
    @Schema(description = "Usuario que ejecutó la acción (puede ser NULL si fue un proceso automático del sistema)")
    private Usuario usuarioAccion;

    @Schema(description = "Fecha y hora exacta en que ocurrió el evento", example = "2024-12-20T10:30:00")
    @Column(nullable = false)
    private LocalDateTime fechaEvento = LocalDateTime.now();

    // Enumeración para los tipos de acción
    public enum Accion {
        @Schema(description = "Inserción de un nuevo registro")
        INSERT,
        
        @Schema(description = "Actualización de un registro existente")
        UPDATE,
        
        @Schema(description = "Eliminación lógica de un registro (cambio de estado estaEliminado = true)")
        DELETE_LOGICO,
        
        @Schema(description = "Desactivación de cuenta de usuario (soft delete)")
        DESACTIVAR,
        
        @Schema(description = "Reactivación de cuenta de usuario previamente desactivada")
        REACTIVAR,
        
        @Schema(description = "Eliminación permanente de un registro (hard delete)")
        ELIMINAR_PERMANENTE
    }

    // Constructores
    public LogSistema() {}

    public LogSistema(String tablaAfectada, Integer idRegistroAfectado, Accion accion, Usuario usuarioAccion) {
        this.tablaAfectada = tablaAfectada;
        this.idRegistroAfectado = idRegistroAfectado;
        this.accion = accion;
        this.usuarioAccion = usuarioAccion;
        this.fechaEvento = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getIdLogSistema() {
        return idLogSistema;
    }

    public void setIdLogSistema(Integer idLogSistema) {
        this.idLogSistema = idLogSistema;
    }

    public String getTablaAfectada() {
        return tablaAfectada;
    }

    public void setTablaAfectada(String tablaAfectada) {
        this.tablaAfectada = tablaAfectada;
    }

    public Integer getIdRegistroAfectado() {
        return idRegistroAfectado;
    }

    public void setIdRegistroAfectado(Integer idRegistroAfectado) {
        this.idRegistroAfectado = idRegistroAfectado;
    }

    public Accion getAccion() {
        return accion;
    }

    public void setAccion(Accion accion) {
        this.accion = accion;
    }

    public Usuario getUsuarioAccion() {
        return usuarioAccion;
    }

    public void setUsuarioAccion(Usuario usuarioAccion) {
        this.usuarioAccion = usuarioAccion;
    }

    public LocalDateTime getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(LocalDateTime fechaEvento) {
        this.fechaEvento = fechaEvento;
    }
}