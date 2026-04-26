package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "Datos para dar de alta un cuarto físico") // DTO para solicitudes de creación de una habitación física, incluyendo su número identificador y las camas que contiene
public class HabitacionRequestDTO {

    @NotBlank(message = "El número o identificador de la habitación es obligatorio")
    @Schema(description = "Identificador físico de la puerta", example = "101A")
    private String numeroHabitacion;

    @NotEmpty(message = "La habitación debe tener al menos una cama")
    @Valid
    @Schema(description = "Lista de camas que contiene la habitación")
    private List<CamaRequestDTO> camas;

    // --- Constructor vacío ---
    public HabitacionRequestDTO() {
    }

    // --- Constructor con parámetros ---
    public HabitacionRequestDTO(String numeroHabitacion, List<CamaRequestDTO> camas) {
        this.numeroHabitacion = numeroHabitacion;
        this.camas = camas;
    }

    // --- Getters y Setters ---

    public String getNumeroHabitacion() {
        return numeroHabitacion;
    }

    public void setNumeroHabitacion(String numeroHabitacion) {
        this.numeroHabitacion = numeroHabitacion;
    }

    public List<CamaRequestDTO> getCamas() {
        return camas;
    }

    public void setCamas(List<CamaRequestDTO> camas) {
        this.camas = camas;
    }
}