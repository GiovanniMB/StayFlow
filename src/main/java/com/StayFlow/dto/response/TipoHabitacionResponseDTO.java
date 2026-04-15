package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

// DTO para respuestas que contienen información detallada de un tipo de habitación, incluyendo su ID, nombre, capacidad, precio base por noche, si tiene baño privado y los servicios asociados.
@Schema(description = "Datos de salida de una categoría de habitación")
public class TipoHabitacionResponseDTO {
    private Integer idTipoHabitacion;
    private Integer idPropiedad;
    private String nombreTipo;
    private Integer capacidad;
    private BigDecimal precioBaseNoche;
    private boolean tieneBanoPrivado;
    private List<ServicioResponseDTO> servicios;

    public TipoHabitacionResponseDTO() {}

    public Integer getIdTipoHabitacion() { return idTipoHabitacion; }
    public void setIdTipoHabitacion(Integer idTipoHabitacion) { this.idTipoHabitacion = idTipoHabitacion; }

    public Integer getIdPropiedad() { return idPropiedad; }
    public void setIdPropiedad(Integer idPropiedad) { this.idPropiedad = idPropiedad; }

    public String getNombreTipo() { return nombreTipo; }
    public void setNombreTipo(String nombreTipo) { this.nombreTipo = nombreTipo; }

    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }

    public BigDecimal getPrecioBaseNoche() { return precioBaseNoche; }
    public void setPrecioBaseNoche(BigDecimal precioBaseNoche) { this.precioBaseNoche = precioBaseNoche; }

    public boolean isTieneBanoPrivado() { return tieneBanoPrivado; }
    public void setTieneBanoPrivado(boolean tieneBanoPrivado) { this.tieneBanoPrivado = tieneBanoPrivado; }

    public List<ServicioResponseDTO> getServicios() { return servicios; }
    public void setServicios(List<ServicioResponseDTO> servicios) { this.servicios = servicios; }
}