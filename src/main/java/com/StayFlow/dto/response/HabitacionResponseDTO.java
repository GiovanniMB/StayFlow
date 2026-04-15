package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

// DTO para respuestas que contienen información detallada de una habitación física, incluyendo su ID, número identificador, estado actual, tipo de habitación al que pertenece y un resumen de las camas que contiene.
@Schema(description = "Datos de salida de una habitación física")
public class HabitacionResponseDTO {
    private Integer idHabitacion;
    private Integer idTipoHabitacion;
    private String nombreTipoHabitacion;
    private String numeroHabitacion;
    private String estado;
    private List<String> detalleCamas; // Devolverá ej: ["1x King", "2x Individual"]
    private Integer totalCamas;

    public HabitacionResponseDTO() {}

    public Integer getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(Integer idHabitacion) { this.idHabitacion = idHabitacion; } 

    public Integer getIdTipoHabitacion() { return idTipoHabitacion; }
    public void setIdTipoHabitacion(Integer idTipoHabitacion) { this.idTipoHabitacion = idTipoHabitacion; }

    public String getNombreTipoHabitacion() { return nombreTipoHabitacion; }
    public void setNombreTipoHabitacion(String nombreTipoHabitacion) { this.nombreTipoHabitacion = nombreTipoHabitacion; }

    public String getNumeroHabitacion() { return numeroHabitacion; }
    public void setNumeroHabitacion(String numeroHabitacion) { this.numeroHabitacion = numeroHabitacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<String> getDetalleCamas() { return detalleCamas; }
    public void setDetalleCamas(List<String> detalleCamas) { this.detalleCamas = detalleCamas; } // ["1x King", "2x Individual"]

    public Integer getTotalCamas() { return totalCamas; }
    public void setTotalCamas(Integer totalCamas) { this.totalCamas = totalCamas; } // Solo para casos donde se quiera mostrar el total de camas, aunque se
    //  puede obtener contando el tamaño de la lista detalleCamas, es útil tenerlo como un campo separado para evitar que el frontend tenga que hacer esa cuenta cada vez que reciba la respuesta,
    //  especialmente si solo necesita mostrar el número total de camas sin importar su tipo.
}