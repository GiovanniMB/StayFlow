package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Schema(description = "ID compuesto para la relación entre habitaciones y tipos de cama")
public class HabitacionTipoCamaId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID de la habitación", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idHabitacion;

    @Schema(description = "ID del tipo de cama", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idTipoCama;

    public HabitacionTipoCamaId() {}

    public HabitacionTipoCamaId(Integer idHabitacion, Integer idTipoCama) {
        this.idHabitacion = idHabitacion;
        this.idTipoCama = idTipoCama;
    }

    // Getters y Setters
    public Integer getIdHabitacion() { 
        return idHabitacion; 
    }
    
    public void setIdHabitacion(Integer idHabitacion) { 
        this.idHabitacion = idHabitacion; 
    }
    
    public Integer getIdTipoCama() { 
        return idTipoCama; 
    }
    
    public void setIdTipoCama(Integer idTipoCama) { 
        this.idTipoCama = idTipoCama; 
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HabitacionTipoCamaId that = (HabitacionTipoCamaId) o;
        return Objects.equals(idHabitacion, that.idHabitacion) && 
               Objects.equals(idTipoCama, that.idTipoCama);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idHabitacion, idTipoCama);
    }
}