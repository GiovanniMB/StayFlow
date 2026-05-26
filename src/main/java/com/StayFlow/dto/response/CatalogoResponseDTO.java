package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO genérico para devolver elementos de listas desplegables (Catálogos)")
public class CatalogoResponseDTO {

    @Schema(description = "ID único del elemento geográfico", example = "1") 
    private Integer id;

    @Schema(description = "Nombre del elemento geográfico", example = "Jalisco") 
    private String nombre;
    
    public CatalogoResponseDTO() {
    }

    public CatalogoResponseDTO(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}