package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de salida de una fotografía")
public class FotoResponseDTO {
    
    @Schema(description = "ID de la fotografía en base de datos", example = "25")
    private Integer idFoto;
    
    @Schema(description = "Ruta relativa o URL pública de la imagen", example = "/uploads/uuid_foto.jpg")
    private String urlFoto;
    
    @Schema(description = "Indica si esta foto es la portada principal a mostrar", example = "true")
    private boolean esPrincipal;

    public FotoResponseDTO() {}

    public Integer getIdFoto() { return idFoto; }
    public void setIdFoto(Integer idFoto) { this.idFoto = idFoto; }

    public String getUrlFoto() { return urlFoto; }
    public void setUrlFoto(String urlFoto) { this.urlFoto = urlFoto; }

    public boolean isEsPrincipal() { return esPrincipal; }
    public void setEsPrincipal(boolean esPrincipal) { this.esPrincipal = esPrincipal; }
}