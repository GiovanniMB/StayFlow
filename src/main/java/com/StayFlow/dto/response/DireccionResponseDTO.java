package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

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

    // --- Constructor vacío ---
    public DireccionResponseDTO() {
    }

    // --- Constructor con parámetros ---
    public DireccionResponseDTO(Integer id, String calle, String numero, String numeroInterior, Integer idColonia, String nombreColonia, BigDecimal latitud, BigDecimal longitud) {
        this.id = id;
        this.calle = calle;
        this.numero = numero;
        this.numeroInterior = numeroInterior;
        this.idColonia = idColonia;
        this.nombreColonia = nombreColonia;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // --- Getters y Setters ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getNombreColonia() {
        return nombreColonia;
    }

    public void setNombreColonia(String nombreColonia) {
        this.nombreColonia = nombreColonia;
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