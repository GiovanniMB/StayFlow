package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para respuesta de tipo de tarjeta")
public class TipoTarjetaResponseDTO {
    
    @Schema(description = "ID del tipo de tarjeta", example = "1")
    private Integer idTipoTarjeta;
    
    @Schema(description = "Nombre del tipo de tarjeta", example = "Visa")
    private String nombre;
    
    @Schema(description = "Código del tipo de tarjeta", example = "visa")
    private String codigo;
    
    public TipoTarjetaResponseDTO() {}
    
    public TipoTarjetaResponseDTO(Integer idTipoTarjeta, String nombre, String codigo) {
        this.idTipoTarjeta = idTipoTarjeta;
        this.nombre = nombre;
        this.codigo = codigo;
    }
    
    // Getters y Setters
    public Integer getIdTipoTarjeta() {
        return idTipoTarjeta;
    }
    
    public void setIdTipoTarjeta(Integer idTipoTarjeta) {
        this.idTipoTarjeta = idTipoTarjeta;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}