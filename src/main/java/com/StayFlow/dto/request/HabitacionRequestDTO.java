package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "Datos para dar de alta un cuarto físico") // DTO para solicitudes de creación de una habitación física, incluyendo su número identificador y las camas que contiene
public class HabitacionRequestDTO {

    @NotBlank(message = "El número o identificador de la habitación es obligatorio")
    @Schema(description = "Identificador físico de la habitación", example = "101A") // Se espera que el cliente proporcione un número o identificador único para la habitación, que se validará en el backend para asegurar que no esté vacío y que cumpla con el formato requerido, el frontend mostrara un campo de texto para que el usuario ingrese este dato
    private String numeroHabitacion;

    @NotEmpty(message = "La habitación debe tener al menos una cama")
    @Valid
    @Schema(description = "Lista de camas que contiene la habitación") // Se espera que el cliente proporcione una lista de camas que se agregarán a la habitación, cada cama debe incluir el ID del tipo de cama y la cantidad, el backend validará que la lista no esté vacía y que cada cama tenga datos válidos, el frontend mostrara controles para agregar múltiples camas a la habitación fácilmente
    private List<CamaRequestDTO> camas;

    public HabitacionRequestDTO() {}

    public String getNumeroHabitacion() { return numeroHabitacion; }
    public void setNumeroHabitacion(String numeroHabitacion) { this.numeroHabitacion = numeroHabitacion; }

    public List<CamaRequestDTO> getCamas() { return camas; }
    public void setCamas(List<CamaRequestDTO> camas) { this.camas = camas; }
}