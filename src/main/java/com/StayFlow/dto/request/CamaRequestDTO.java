package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Define la cantidad y tipo de cama a agregar en una habitación") // DTO para solicitudes de agregar camas a una habitación, especificando el tipo de cama y la cantidad
public class CamaRequestDTO {

    @NotNull(message = "El ID del tipo de cama es obligatorio")
    @Schema(description = "ID del tipo de cama (ej. 1 para Individual, 2 para King)", example = "1")
    private Integer idTipoCama;

    @NotNull(message = "La cantidad de camas es obligatoria")
    @Min(value = 1, message = "Debe agregar al menos 1 cama de este tipo")
    @Schema(description = "Número de camas de este tipo", example = "2")
    private Integer cantidad;

    // 1. Constructor vacío
    public CamaRequestDTO() {
    }

    // 2. Constructor con parámetros
    public CamaRequestDTO(Integer idTipoCama, Integer cantidad) {
        this.idTipoCama = idTipoCama;
        this.cantidad = cantidad;
    }

    // 3. Getters y Setters

    public Integer getIdTipoCama() {
        return idTipoCama;
    }

    public void setIdTipoCama(Integer idTipoCama) {
        this.idTipoCama = idTipoCama;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}