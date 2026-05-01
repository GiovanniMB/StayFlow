package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Schema(description = "ID compuesto para la relación entre categorías (tipos de habitación) y tipos de cama")
public class TipoHabitacionCamaId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID del tipo de habitación (Categoría)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idTipoHabitacion;

    @Schema(description = "ID del tipo de cama", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idTipoCama;

    public TipoHabitacionCamaId() {}

    public TipoHabitacionCamaId(Integer idTipoHabitacion, Integer idTipoCama) {
        this.idTipoHabitacion = idTipoHabitacion;
        this.idTipoCama = idTipoCama;
    }

    // Getters y Setters
    public Integer getIdTipoHabitacion() { return idTipoHabitacion; }
    public void setIdTipoHabitacion(Integer idTipoHabitacion) { this.idTipoHabitacion = idTipoHabitacion; }
    
    public Integer getIdTipoCama() { return idTipoCama; }
    public void setIdTipoCama(Integer idTipoCama) { this.idTipoCama = idTipoCama; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoHabitacionCamaId that = (TipoHabitacionCamaId) o;
        return Objects.equals(idTipoHabitacion, that.idTipoHabitacion) && 
               Objects.equals(idTipoCama, that.idTipoCama);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTipoHabitacion, idTipoCama);
    }
}