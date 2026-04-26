package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Datos de salida de la dirección de una propiedad")
public class DireccionResponseDTO {
    
    @Schema(description = "ID de la dirección en base de datos", example = "5")
    private Integer id;
    
    @Schema(description = "Calle principal", example = "Av. Reforma")
    private String calle;
    
    @Schema(description = "Número exterior", example = "123")
    private String numero;
    
    @Schema(description = "Número interior (si aplica)", example = "Int 4B")
    private String numeroInterior;
    
    @Schema(description = "ID de la colonia en el catálogo", example = "450")
    private Integer idColonia;
    
    @Schema(description = "Nombre en texto de la colonia", example = "Roma Norte")
    private String nombreColonia; 
    
    @Schema(description = "Nombre en texto del municipio o alcaldía", example = "Cuauhtémoc")
    private String municipio;
    
    @Schema(description = "Nombre en texto del estado", example = "Ciudad de México")
    private String estado;
    
    @Schema(description = "Latitud para renderizar el mapa", example = "19.420300")
    private BigDecimal latitud;
    
    @Schema(description = "Longitud para renderizar el mapa", example = "-99.163100")
    private BigDecimal longitud;

    public DireccionResponseDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; } 

    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; } 

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; } 

    public String getNumeroInterior() { return numeroInterior; }
    public void setNumeroInterior(String numeroInterior) { this.numeroInterior = numeroInterior; } 

    public Integer getIdColonia() { return idColonia; }
    public void setIdColonia(Integer idColonia) { this.idColonia = idColonia; } 

    public String getNombreColonia() { return nombreColonia; }
    public void setNombreColonia(String nombreColonia) { this.nombreColonia = nombreColonia; } 

    // --- GETTERS Y SETTERS NUEVOS ---
    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    // ---------------------------------

    public BigDecimal getLatitud() { return latitud; }
    public void setLatitud(BigDecimal latitud) { this.latitud = latitud; }

    public BigDecimal getLongitud() { return longitud; }
    public void setLongitud(BigDecimal longitud) { this.longitud = longitud; }
}