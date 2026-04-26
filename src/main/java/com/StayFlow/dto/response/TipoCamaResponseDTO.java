package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de salida para un tipo de cama")
public class TipoCamaResponseDTO {
    
    @Schema(description = "ID único del tipo de cama en el catálogo", example = "2")
    private Integer idTipoCama;
    
    @Schema(description = "Nombre comercial de la cama", example = "Matrimonial")
    private String nombreCama;

    public TipoCamaResponseDTO() {}

    public Integer getIdTipoCama() { return idTipoCama; }
    public void setIdTipoCama(Integer idTipoCama) { this.idTipoCama = idTipoCama; }

    public String getNombreCama() { return nombreCama; }
    public void setNombreCama(String nombreCama) { this.nombreCama = nombreCama; }
}