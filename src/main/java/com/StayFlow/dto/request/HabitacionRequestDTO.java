package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Datos para dar de alta un cuarto físico") 
public class HabitacionRequestDTO {

    @NotBlank(message = "El número o identificador de la habitación es obligatorio")
    @Schema(description = "Identificador físico de la puerta", example = "101A")
    private String numeroHabitacion;

    
    public HabitacionRequestDTO() {
    }

    public HabitacionRequestDTO(String numeroHabitacion) {
        this.numeroHabitacion = numeroHabitacion;
    }

    public String getNumeroHabitacion() {
        return numeroHabitacion;
    }

    public void setNumeroHabitacion(String numeroHabitacion) {
        this.numeroHabitacion = numeroHabitacion;
    }
}