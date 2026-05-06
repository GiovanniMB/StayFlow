package com.StayFlow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "fotohabitacion")
@Schema(description = "Fotos asociadas a los tipos de habitación o fotos generales de la propiedad")
public class FotoHabitacion extends AuditoriaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la foto", example = "1")
    private Integer idFotoHabitacion;

    @ManyToOne
    @JoinColumn(name = "idPropiedad")
    @Schema(description = "Propiedad a la que pertenece esta foto (obligatorio para saber de qué hotel es)")
    private Propiedad propiedad;

    @ManyToOne
    @JoinColumn(name = "idTipoHabitacion")
    @Schema(description = "Tipo de habitación al que pertenece esta foto (NULL para fotos generales de la propiedad)")
    private TipoHabitacion tipoHabitacion;

    @ManyToOne
    @JoinColumn(name = "idCategoriaFoto")
    @Schema(description = "Categoría de la foto (interior/exterior)")
    private CategoriaFoto categoriaFoto;

    @Schema(description = "URL de la foto almacenada", example = "https://storage.com/hoteles/fachada.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private String urlFoto;

    @Schema(description = "Indica si es la foto principal del tipo de habitación o de la propiedad", example = "true")
    private boolean esPrincipal = false;

    // Constructores
    public FotoHabitacion() {}

    // Constructor actualizado para incluir Propiedad
    public FotoHabitacion(Propiedad propiedad, TipoHabitacion tipoHabitacion, CategoriaFoto categoriaFoto, String urlFoto, boolean esPrincipal) {
        this.propiedad = propiedad;
        this.tipoHabitacion = tipoHabitacion;
        this.categoriaFoto = categoriaFoto;
        this.urlFoto = urlFoto;
        this.esPrincipal = esPrincipal;
    }

    // Getters y Setters
    public Integer getIdFotoHabitacion() {
        return idFotoHabitacion;
    }

    public void setIdFotoHabitacion(Integer idFotoHabitacion) {
        this.idFotoHabitacion = idFotoHabitacion;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }

    public TipoHabitacion getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(TipoHabitacion tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    public CategoriaFoto getCategoriaFoto() {
        return categoriaFoto;
    }

    public void setCategoriaFoto(CategoriaFoto categoriaFoto) {
        this.categoriaFoto = categoriaFoto;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public boolean isEsPrincipal() {
        return esPrincipal;
    }

    public void setEsPrincipal(boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
    }
}