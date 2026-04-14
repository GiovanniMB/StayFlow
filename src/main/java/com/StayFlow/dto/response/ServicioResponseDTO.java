package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos de salida para un servicio (Amenity)")
public class ServicioResponseDTO {
    private Integer idServicio;
    private String nombreServicio;
}