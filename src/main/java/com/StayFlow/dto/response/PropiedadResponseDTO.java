package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos de salida de una propiedad")
public class PropiedadResponseDTO {
    private Integer idPropiedad;
    private Integer idDueno; // Solo enviamos el ID del dueño por seguridad
    private String nombreComercial;
    private DireccionResponseDTO direccion;
    private String telefono;
    private boolean seRentaPorHabitaciones;
    private Integer contadorReservas;
    private String descripcion;
    private List<ServicioResponseDTO> servicios;
}