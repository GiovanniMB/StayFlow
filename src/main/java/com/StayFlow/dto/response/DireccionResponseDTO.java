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

    public DireccionResponseDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; } // Solo para casos donde se quiera mostrar el ID de la dirección

    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; } // Solo para casos donde se quiera mostrar la calle de la dirección

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; } // Solo para casos donde se quiera mostrar el número exterior de la dirección

    public String getNumeroInterior() { return numeroInterior; }
    public void setNumeroInterior(String numeroInterior) { this.numeroInterior = numeroInterior; } // Solo para casos donde se quiera mostrar el número interior de la dirección

    public Integer getIdColonia() { return idColonia; }
    public void setIdColonia(Integer idColonia) { this.idColonia = idColonia; } // Solo para casos donde se quiera mostrar el ID de la colonia

    public String getNombreColonia() { return nombreColonia; }
    public void setNombreColonia(String nombreColonia) { this.nombreColonia = nombreColonia; } // Solo para casos donde se quiera mostrar el nombre de la colonia, útil para que el frontend no tenga que hacer otra petición para obtenerlo a partir del ID

    public BigDecimal getLatitud() { return latitud; }
    public void setLatitud(BigDecimal latitud) { this.latitud = latitud; }

    public BigDecimal getLongitud() { return longitud; }
    public void setLongitud(BigDecimal longitud) { this.longitud = longitud; }
}