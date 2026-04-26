package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Datos para crear o actualizar una categoría de habitación") 
public class TipoHabitacionRequestDTO {

    @NotBlank(message = "El nombre del tipo es obligatorio")
    @Schema(description = "Nombre de la categoría o recámara", example = "Suite Ejecutiva")
    private String nombreTipo;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser al menos de 1 persona")
    @Schema(description = "Cantidad máxima de personas que pueden dormir aquí", example = "2")
    private Integer capacidad;

    @NotNull(message = "El precio base es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    @Schema(description = "Precio por noche de esta categoría (0 si es Casa Entera)", example = "1500.00")
    private BigDecimal precioBaseNoche;

    @NotNull(message = "Debe indicar si tiene baño privado")
    @Schema(description = "Indica si la recámara cuenta con baño al interior", example = "true")
    private Boolean tieneBanoPrivado;

    @Schema(description = "Lista de IDs de amenidades que incluye esta recámara", example = "[1, 4, 7]")
    private List<Integer> idServicios;
    
    @Schema(description = "Amenidades escritas manualmente que no están en catálogo", example = "[\"Tina de hidromasaje\"]")
    private List<String> nuevosServicios;

    @NotEmpty(message = "La categoría debe tener al menos una cama configurada")
    @Valid
    @Schema(description = "Lista de camas asignadas a esta categoría")
    private List<CamaRequestDTO> camas;

    public TipoHabitacionRequestDTO() {}

    // Getters y Setters
    public String getNombreTipo() { return nombreTipo; }
    public void setNombreTipo(String nombreTipo) { this.nombreTipo = nombreTipo; }

    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }

    public BigDecimal getPrecioBaseNoche() { return precioBaseNoche; }
    public void setPrecioBaseNoche(BigDecimal precioBaseNoche) { this.precioBaseNoche = precioBaseNoche; }

    public Boolean getTieneBanoPrivado() { return tieneBanoPrivado; }
    public void setTieneBanoPrivado(Boolean tieneBanoPrivado) { this.tieneBanoPrivado = tieneBanoPrivado; }

    public List<Integer> getIdServicios() { return idServicios; }
    public void setIdServicios(List<Integer> idServicios) { this.idServicios = idServicios; }

    public List<String> getNuevosServicios() { return nuevosServicios; }
    public void setNuevosServicios(List<String> nuevosServicios) { this.nuevosServicios = nuevosServicios; }

    public List<CamaRequestDTO> getCamas() { return camas; }
    public void setCamas(List<CamaRequestDTO> camas) { this.camas = camas; }
}