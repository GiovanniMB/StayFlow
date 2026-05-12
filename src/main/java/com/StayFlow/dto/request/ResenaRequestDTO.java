package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para crear una reseña")
public class ResenaRequestDTO {

    @NotNull(message = "El id de la reserva es obligatorio")
    @Schema(description = "Id de la reserva asociada a la reseña", example = "1")
    private Integer idReserva;

    @NotNull(message = "El id del usuario autor es obligatorio")
    @Schema(description = "Id del usuario que escribe la reseña", example = "5")
    private Integer idUsuarioAutor;

    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 5, message = "La puntuación máxima es 5")
    @Schema(description = "Puntuación de 1 a 5", example = "5")
    private Integer puntuacion;

    @Schema(description = "Comentario escrito por el usuario", example = "Excelente estancia, muy recomendable")
    private String comentario;

    public ResenaRequestDTO() {
    }

    public ResenaRequestDTO(Integer idReserva, Integer idUsuarioAutor, Integer puntuacion, String comentario) {
        this.idReserva = idReserva;
        this.idUsuarioAutor = idUsuarioAutor;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
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
}