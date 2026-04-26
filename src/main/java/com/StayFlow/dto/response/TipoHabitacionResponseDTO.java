package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Datos de salida de una categoría de habitación")
public class TipoHabitacionResponseDTO {
    
    @Schema(description = "ID único de la categoría de habitación", example = "5")
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
    
    @Schema(description = "Lista de fotografías exclusivas de esta habitación")
    private List<FotoResponseDTO> fotos;
    
    @Schema(description = "Lista de camas formateada y lista para mostrarse al huésped", example = "[\"1x King\", \"2x Individual\"]")
    private List<String> detalleCamas; 
    
    @Schema(description = "Suma matemática total de camas en esta categoría", example = "3")
    private Integer totalCamas;

    public TipoHabitacionResponseDTO() {}

    // Getters y Setters
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

    public List<FotoResponseDTO> getFotos() { return fotos; }
    public void setFotos(List<FotoResponseDTO> fotos) { this.fotos = fotos; }

    public List<String> getDetalleCamas() { return detalleCamas; }
    public void setDetalleCamas(List<String> detalleCamas) { this.detalleCamas = detalleCamas; }

    public Integer getTotalCamas() { return totalCamas; }
    public void setTotalCamas(Integer totalCamas) { this.totalCamas = totalCamas; }
}