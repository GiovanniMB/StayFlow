package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de salida para mostrar reseñas")
public class ResenaResponseDTO {

    private Integer idResena;
    private Integer idReserva;
    private Integer idUsuarioAutor;
    private String nombreAutor;
    private Integer puntuacion;
    private String comentario;
    private String tipoResena;

    public ResenaResponseDTO() {
    }

    public ResenaResponseDTO(Integer idResena,
                             Integer idReserva,
                             Integer idUsuarioAutor,
                             String nombreAutor,
                             Integer puntuacion,
                             String comentario,
                             String tipoResena) {
        this.idResena = idResena;
        this.idReserva = idReserva;
        this.idUsuarioAutor = idUsuarioAutor;
        this.nombreAutor = nombreAutor;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.tipoResena = tipoResena;
    }

    public Integer getIdResena() {
        return idResena;
    }

    public void setIdResena(Integer idResena) {
        this.idResena = idResena;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public Integer getIdUsuarioAutor() {
        return idUsuarioAutor;
    }

    public void setIdUsuarioAutor(Integer idUsuarioAutor) {
        this.idUsuarioAutor = idUsuarioAutor;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getTipoResena() {
        return tipoResena;
    }

    public void setTipoResena(String tipoResena) {
        this.tipoResena = tipoResena;
    }
}