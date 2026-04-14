package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
// DTO para respuestas que contienen información detallada de una habitación física, incluyendo su ID, número identificador, estado actual, tipo de habitación al que pertenece y un resumen de las camas que contiene.
@Getter
@Setter
@Schema(description = "Datos de salida de una habitación física")
public class HabitacionResponseDTO {
    private Integer idHabitacion;
    private Integer idTipoHabitacion;
    private String nombreTipoHabitacion;
    private String numeroHabitacion;
    private String estado;
    private List<String> detalleCamas; // Devolverá ej: ["1x King", "2x Individual"]
    private Integer totalCamas;
}