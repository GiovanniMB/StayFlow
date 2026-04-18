package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de salida para un servicio (Amenity)")
public class ServicioResponseDTO {
    
    private Integer idServicio;
    private String nombreServicio;

    // --- Constructor vacío ---
    public ServicioResponseDTO() {
    }

    // --- Constructor con parámetros ---
    public ServicioResponseDTO(Integer idServicio, String nombreServicio) {
        this.idServicio = idServicio;
        this.nombreServicio = nombreServicio;
    }

    // --- Getters y Setters ---

    public Integer getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Integer idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }
}