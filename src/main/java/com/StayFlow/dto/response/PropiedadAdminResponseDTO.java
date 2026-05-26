package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO para administrador - Datos de propiedad con control de estado")
public class PropiedadAdminResponseDTO {

    @Schema(description = "ID de la propiedad", example = "1")
    private Integer idPropiedad;

    @Schema(description = "Nombre comercial", example = "Hotel Paraíso")
    private String nombreComercial;

    @Schema(description = "Descripción corta", example = "Hermoso hotel en la playa")
    private String descripcion;

    @Schema(description = "Estado de la propiedad", example = "PUBLICADA")
    private String estadoPropiedad;

    @Schema(description = "Si está destacada en el home", example = "true")
    private boolean destacada;

    @Schema(description = "Modalidad de renta", example = "true")
    private boolean seRentaPorHabitaciones;

    @Schema(description = "Precio por noche (si aplica)", example = "1200.00")
    private BigDecimal precioNoche;

    @Schema(description = "Nombre del anfitrión", example = "Carlos García")
    private String nombreAnfitrion;

    @Schema(description = "ID del anfitrión", example = "5")
    private Integer idAnfitrion;

    @Schema(description = "Ubicación formateada", example = "Centro, Ciudad de México, CDMX")
    private String ubicacion;

    @Schema(description = "URL de la imagen portada", example = "/uploads/foto.jpg")
    private String imagenPortada;

    @Schema(description = "Fecha de creación", example = "2024-01-15T10:30:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Calificación promedio", example = "4.5")
    private Double calificacionPromedio;

    @Schema(description = "Cantidad de reservas", example = "25")
    private Integer cantidadReservas;

    // Constructor vacío
    public PropiedadAdminResponseDTO() {}

    // Getters y Setters
    public Integer getIdPropiedad() { return idPropiedad; }
    public void setIdPropiedad(Integer idPropiedad) { this.idPropiedad = idPropiedad; }

    public String getNombreComercial() { return nombreComercial; }
    public void setNombreComercial(String nombreComercial) { this.nombreComercial = nombreComercial; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstadoPropiedad() { return estadoPropiedad; }
    public void setEstadoPropiedad(String estadoPropiedad) { this.estadoPropiedad = estadoPropiedad; }

    public boolean isDestacada() { return destacada; }
    public void setDestacada(boolean destacada) { this.destacada = destacada; }

    public boolean isSeRentaPorHabitaciones() { return seRentaPorHabitaciones; }
    public void setSeRentaPorHabitaciones(boolean seRentaPorHabitaciones) { this.seRentaPorHabitaciones = seRentaPorHabitaciones; }

    public BigDecimal getPrecioNoche() { return precioNoche; }
    public void setPrecioNoche(BigDecimal precioNoche) { this.precioNoche = precioNoche; }

    public String getNombreAnfitrion() { return nombreAnfitrion; }
    public void setNombreAnfitrion(String nombreAnfitrion) { this.nombreAnfitrion = nombreAnfitrion; }

    public Integer getIdAnfitrion() { return idAnfitrion; }
    public void setIdAnfitrion(Integer idAnfitrion) { this.idAnfitrion = idAnfitrion; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getImagenPortada() { return imagenPortada; }
    public void setImagenPortada(String imagenPortada) { this.imagenPortada = imagenPortada; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Double getCalificacionPromedio() { return calificacionPromedio; }
    public void setCalificacionPromedio(Double calificacionPromedio) { this.calificacionPromedio = calificacionPromedio; }

    public Integer getCantidadReservas() { return cantidadReservas; }
    public void setCantidadReservas(Integer cantidadReservas) { this.cantidadReservas = cantidadReservas; }
}