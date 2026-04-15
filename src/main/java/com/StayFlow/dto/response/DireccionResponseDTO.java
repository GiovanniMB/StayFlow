package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Datos de salida de la dirección de una propiedad")
public class DireccionResponseDTO {
    private Integer id;
    private String calle;
    private String numero;
    private String numeroInterior;
    private Integer idColonia;
    private String nombreColonia; // Útil para que el frontend no tenga que hacer otra petición
    private BigDecimal latitud;
    private BigDecimal longitud;
}