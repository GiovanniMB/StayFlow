package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de salida de una habitación física (Ficha de inventario)")
public class HabitacionResponseDTO {
    
    @Schema(description = "ID de la puerta o cuarto físico", example = "8")
    private Integer idHabitacion;
    
    @Schema(description = "ID de la categoría a la que pertenece", example = "2")
    private Integer idTipoHabitacion;
    
    @Schema(description = "Nombre de la categoría padre", example = "Suite Ejecutiva")
    private String nombreTipoHabitacion;
    
    @Schema(description = "Identificador físico de la puerta", example = "101A")
    private String numeroHabitacion;
    
    @Schema(description = "Estado actual del cuarto (disponible, ocupada, mantenimiento)", example = "disponible")
    private String estado;

    public HabitacionResponseDTO() {}

    public Integer getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(Integer idHabitacion) { this.idHabitacion = idHabitacion; } 

    public Integer getIdTipoHabitacion() { return idTipoHabitacion; }
    public void setIdTipoHabitacion(Integer idTipoHabitacion) { this.idTipoHabitacion = idTipoHabitacion; }

    public String getNombreTipoHabitacion() { return nombreTipoHabitacion; }
    public void setNombreTipoHabitacion(String nombreTipoHabitacion) { this.nombreTipoHabitacion = nombreTipoHabitacion; }

    public String getNumeroHabitacion() { return numeroHabitacion; }
    public void setNumeroHabitacion(String numeroHabitacion) { this.numeroHabitacion = numeroHabitacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}