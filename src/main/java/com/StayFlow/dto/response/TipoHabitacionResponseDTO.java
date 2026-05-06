package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

// DTO para respuestas que contienen información detallada de un tipo de habitación, incluyendo su ID, nombre, capacidad, precio base por noche, si tiene baño privado y los servicios asociados.
@Schema(description = "Datos de salida de una categoría de habitación")
public class TipoHabitacionResponseDTO {
    
    private Integer idTipoHabitacion;
    
    @Schema(description = "ID de la propiedad a la que pertenece", example = "1")
    private Integer idPropiedad;
    
    @Schema(description = "Nombre de la categoría o recámara", example = "Suite Ejecutiva")
    private String nombreTipo;
    
    @Schema(description = "Capacidad máxima de personas", example = "2")
    private Integer capacidad;
    
    @Schema(description = "Precio base por noche", example = "1600.00")
    private BigDecimal precioBaseNoche;
    
    @Schema(description = "Indica si cuenta con baño privado interior", example = "true")
    private boolean tieneBanoPrivado;
    
    @Schema(description = "Lista de amenidades incluidas en la habitación")
    private List<ServicioResponseDTO> servicios;

    // 👇 Aquí está la nueva variable para las fotos
    @Schema(description = "Lista de fotografías asociadas a la categoría de habitación")
    private List<FotoResponseDTO> fotos;

    // --- Constructor vacío ---
    public TipoHabitacionResponseDTO() {
    }

    // --- Constructor con parámetros (¡Actualizado con fotos!) ---
    public TipoHabitacionResponseDTO(Integer idTipoHabitacion, Integer idPropiedad, String nombreTipo, Integer capacidad, BigDecimal precioBaseNoche, boolean tieneBanoPrivado, List<ServicioResponseDTO> servicios, List<FotoResponseDTO> fotos) {
        this.idTipoHabitacion = idTipoHabitacion;
        this.idPropiedad = idPropiedad;
        this.nombreTipo = nombreTipo;
        this.capacidad = capacidad;
        this.precioBaseNoche = precioBaseNoche;
        this.tieneBanoPrivado = tieneBanoPrivado;
        this.servicios = servicios;
        this.fotos = fotos;
    }

    // --- Getters y Setters ---

    public Integer getIdTipoHabitacion() {
        return idTipoHabitacion;
    }

    public void setIdTipoHabitacion(Integer idTipoHabitacion) {
        this.idTipoHabitacion = idTipoHabitacion;
    }

    public Integer getIdPropiedad() {
        return idPropiedad;
    }

    public void setIdPropiedad(Integer idPropiedad) {
        this.idPropiedad = idPropiedad;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public BigDecimal getPrecioBaseNoche() {
        return precioBaseNoche;
    }

    public void setPrecioBaseNoche(BigDecimal precioBaseNoche) {
        this.precioBaseNoche = precioBaseNoche;
    }

    public boolean isTieneBanoPrivado() {
        return tieneBanoPrivado;
    }

    public void setTieneBanoPrivado(boolean tieneBanoPrivado) {
        this.tieneBanoPrivado = tieneBanoPrivado;
    }

    public List<ServicioResponseDTO> getServicios() {
        return servicios;
    }

    public void setServicios(List<ServicioResponseDTO> servicios) {
        this.servicios = servicios;
    }

    // --- Getters y Setters de Fotos ---
    public List<FotoResponseDTO> getFotos() {
        return fotos;
    }

    public void setFotos(List<FotoResponseDTO> fotos) {
        this.fotos = fotos;
    }
}