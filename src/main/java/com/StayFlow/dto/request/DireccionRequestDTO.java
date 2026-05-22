package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
//Esto fue util en las pruebas de swagger ya que en el front no se ingresan directamente los ids
// DTO para recibir los datos de la dirección de una propiedad en las solicitudes de creación o actualización. Incluye validaciones para asegurar que se proporcionen los datos necesarios y que tengan el formato correcto.
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
    @Schema(description = "ID de la colonia correspondiente", example = "5") // Se espera que el cliente proporcione el ID de la colonia, que se validará en el backend para asegurar que corresponde a una colonia válida, el frontend puede mostrar una lista de colonias disponibles para que el usuario seleccione el ID correcto
    private Integer idColonia;
    
    @NotNull(message = "La latitud es obligatoria")
    @Schema(description = "Latitud geográfica", example = "19.432608") // Se espera que el cliente proporcione la latitud de la propiedad, que se validará en el backend para asegurar que es un valor numérico válido, el frontend mostrara un mapa para que el usuario seleccione la ubicación exacta y se obtengan automáticamente las coordenadas de latitud y longitud
    private BigDecimal latitud;
    
    @NotNull(message = "La longitud es obligatoria")
    @Schema(description = "Longitud geográfica", example = "-99.133209")
    private BigDecimal longitud;

    // --- Constructor vacío ---
    public DireccionRequestDTO() {
    }

    // --- Constructor con parámetros ---
    public DireccionRequestDTO(String calle, String numero, String numeroInterior, Integer idColonia, BigDecimal latitud, BigDecimal longitud) {
        this.calle = calle;
        this.numero = numero;
        this.numeroInterior = numeroInterior;
        this.idColonia = idColonia;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // --- Getters y Setters ---

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNumeroInterior() {
        return numeroInterior;
    }

    public void setNumeroInterior(String numeroInterior) {
        this.numeroInterior = numeroInterior;
    }

    public Integer getIdColonia() {
        return idColonia;
    }

    public void setIdColonia(Integer idColonia) {
        this.idColonia = idColonia;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }
}