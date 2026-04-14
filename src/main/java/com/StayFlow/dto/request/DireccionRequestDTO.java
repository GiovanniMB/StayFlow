package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
// DTO para recibir los datos de la dirección de una propiedad en las solicitudes de creación o actualización. Incluye validaciones para asegurar que se proporcionen los datos necesarios y que tengan el formato correcto.
@Getter
@Setter
@Schema(description = "Datos de entrada para la dirección de una propiedad") // Descripción general del DTO para Swagger
public class DireccionRequestDTO {
    
    @NotBlank(message = "La calle es obligatoria")
    @Schema(description = "Calle", example = "Av. Reforma")
    private String calle;
    
    @Schema(description = "Número exterior", example = "123")
    private String numero;
    
    @Schema(description = "Número interior", example = "2B")
    private String numeroInterior;
    
    @NotNull(message = "El ID de la colonia es obligatorio")
    @Schema(description = "ID de la colonia correspondiente", example = "5")
    private Integer idColonia;
    
    @NotNull(message = "La latitud es obligatoria")
    @Schema(description = "Latitud geográfica", example = "19.432608")
    private BigDecimal latitud;
    
    @NotNull(message = "La longitud es obligatoria")
    @Schema(description = "Longitud geográfica", example = "-99.133209")
    private BigDecimal longitud;
}