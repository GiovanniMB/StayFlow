package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;
// DTO para respuestas que contienen información detallada de un tipo de habitación, incluyendo su ID, nombre, capacidad, precio base por noche, si tiene baño privado y los servicios asociados.
@Getter
@Setter
@Schema(description = "Datos de salida de una categoría de habitación")
public class TipoHabitacionResponseDTO {
    private Integer idTipoHabitacion;
    private Integer idPropiedad;
    private String nombreTipo;
    private Integer capacidad;
    private BigDecimal precioBaseNoche;
    private boolean tieneBanoPrivado;
    private List<ServicioResponseDTO> servicios;
}