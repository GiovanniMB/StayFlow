package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Datos de entrada para la dirección de una propiedad")
public class DireccionRequestDTO {
    
    @Schema(description = "Calle", example = "Av. Reforma")
    private String calle;
    
    @Schema(description = "Número exterior", example = "123")
    private String numero;
    
    @Schema(description = "Número interior", example = "2B")
    private String numeroInterior;
    
    @Schema(description = "ID de la colonia correspondiente", example = "5")
    private Integer idColonia;
    
    @Schema(description = "Latitud geográfica", example = "19.432608")
    private BigDecimal latitud;
    
    @Schema(description = "Longitud geográfica", example = "-99.133209")
    private BigDecimal longitud;
}