package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "categoriafoto")
@Schema(description = "Categorías para clasificar las fotos de habitaciones y propiedades (interior/exterior)")
public class CategoriaFoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la categoría", example = "1")
    private Integer idCategoriaFoto;

    @Schema(description = "Nombre de la categoría", example = "Fachada", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 50)
    private String nombreCategoria;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Tipo macro de la categoría", example = "exterior", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private TipoMacro tipoMacro;

    @Schema(description = "Estado de eliminación lógica", example = "false")
    private boolean estaEliminado = false;

    // Enum para tipoMacro
    public enum TipoMacro {
        @Schema(description = "Fotos del interior de la propiedad/habitación")
        interior,
        @Schema(description = "Fotos del exterior de la propiedad/habitación")
        exterior
    }

    // Constructores
    public CategoriaFoto() {}

    public CategoriaFoto(String nombreCategoria, TipoMacro tipoMacro) {
        this.nombreCategoria = nombreCategoria;
        this.tipoMacro = tipoMacro;
    }

    // Getters y Setters
    public Integer getIdCategoriaFoto() {
        return idCategoriaFoto;
    }

    public void setIdCategoriaFoto(Integer idCategoriaFoto) {
        this.idCategoriaFoto = idCategoriaFoto;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public TipoMacro getTipoMacro() {
        return tipoMacro;
    }

    public void setTipoMacro(TipoMacro tipoMacro) {
        this.tipoMacro = tipoMacro;
    }

    public boolean isEstaEliminado() {
        return estaEliminado;
    }

    public void setEstaEliminado(boolean estaEliminado) {
        this.estaEliminado = estaEliminado;
    }
}