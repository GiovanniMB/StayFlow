package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Datos para crear o actualizar una categoría de habitación") // DTO para solicitudes de creación o actualización de tipo de habitación
public class TipoHabitacionRequestDTO {

    @NotBlank(message = "El nombre del tipo es obligatorio")
    @Schema(description = "Nombre de la categoría", example = "Suite Presidencial")
    private String nombreTipo;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser al menos de 1 persona")
    @Schema(description = "Capacidad máxima de personas", example = "4")
    private Integer capacidad;

    @NotNull(message = "El precio base es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    @Schema(description = "Precio base por noche", example = "1500.50")
    private BigDecimal precioBaseNoche;

    @NotNull(message = "Debe indicar si tiene baño privado")
    @Schema(description = "Indica si tiene baño privado", example = "true")
    private Boolean tieneBanoPrivado;

    @Schema(description = "Lista de IDs de servicios (amenities) de esta habitación", example = "[4, 5]")
    private List<Integer> idServicios;

    // --- Constructor vacío ---
    public TipoHabitacionRequestDTO() {
    }

    // --- Constructor con parámetros ---
    public TipoHabitacionRequestDTO(String nombreTipo, Integer capacidad, BigDecimal precioBaseNoche, Boolean tieneBanoPrivado, List<Integer> idServicios) {
        this.nombreTipo = nombreTipo;
        this.capacidad = capacidad;
        this.precioBaseNoche = precioBaseNoche;
        this.tieneBanoPrivado = tieneBanoPrivado;
        this.idServicios = idServicios;
    }

    // --- Getters y Setters ---

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

    public Boolean getTieneBanoPrivado() {
        return tieneBanoPrivado;
    }

    public void setTieneBanoPrivado(Boolean tieneBanoPrivado) {
        this.tieneBanoPrivado = tieneBanoPrivado;
    }

    public List<Integer> getIdServicios() {
        return idServicios;
    }

    public void setIdServicios(List<Integer> idServicios) {
        this.idServicios = idServicios;
    }
}