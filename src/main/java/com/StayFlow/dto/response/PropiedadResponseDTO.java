package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal; // Importación necesaria para el precio
import java.time.LocalTime;
import java.util.List;

@Schema(description = "Datos de salida de una propiedad")
public class PropiedadResponseDTO {
    
    private Integer idPropiedad;
    
    @Schema(description = "ID del usuario propietario", example = "3")
    private Integer idDueno; // Solo enviamos el ID del dueño por seguridad
    
    @Schema(description = "Nombre del alojamiento", example = "Hotel Paraíso")
    private String nombreComercial;
    
    @Schema(description = "Detalles de la dirección y geolocalización")
    private DireccionResponseDTO direccion;
    
    @Schema(description = "Teléfono de contacto de la recepción o anfitrión", example = "5512345678")
    private String telefono;
    
    @Schema(description = "Define la modalidad del alojamiento (true=Hotel, false=Casa Entera)", example = "true")
    private boolean seRentaPorHabitaciones;
    
    @Schema(description = "Cantidad de veces que se ha reservado (útil para ranking)", example = "15")
    private Integer contadorReservas;
    
    @Schema(description = "Descripción narrativa del lugar redactada por el anfitrión", example = "Un lugar mágico en el centro...")
    private String descripcion;

    //Variable para el precio por noche, que es un dato crucial para los usuarios al momento de elegir una propiedad
    @Schema(description = "Precio base por noche de la propiedad", example = "1200.50")
    private BigDecimal precioNoche;
    
    @Schema(description = "Estado actual de la propiedad", example = "PUBLICADA")
    private String estadoPropiedad;

    @Schema(description = "Lista de servicios generales de la propiedad (Wifi, Alberca, etc.)")
    private List<ServicioResponseDTO> servicios;

    @Schema(description = "Lista de fotos generales de la propiedad")
    private List<FotoResponseDTO> fotosGenerales;

    @Schema(description = "Amenidades extra que no están en la lista de servicios estándar", example = "Vista al mar, Balcón privado")
    private String amenidadesExtra;

    //check in y check out
    @Schema(description = "Hora a partir de la cual el huésped puede hacer check-in", example = "15:00:00")
    private LocalTime horaCheckIn;

    @Schema(description = "Hora límite para que el huésped haga check-out", example = "11:00:00")
    private LocalTime horaCheckOut;

    @Schema(description = "Calificación promedio de la propiedad basada en las reseñas", example = "4.5")
    private Double calificacionPromedio;

    @Schema(description = "Cantidad total de reseñas que tiene la propiedad", example = "25")
    private Integer cantidadResenas;

    @Schema(description = "Precio mínimo de habitación (si aplica)", example = "500.00")
    private BigDecimal precioMinimo;

    @Schema(description = "Precio máximo de habitación (si aplica)", example = "1500.00")
    private BigDecimal precioMaximo;

    // Constructor vacío
    public PropiedadResponseDTO() {
    }

    // Constructor completo
    public PropiedadResponseDTO(Integer idPropiedad, Integer idDueno, String nombreComercial, DireccionResponseDTO direccion, String telefono, boolean seRentaPorHabitaciones, Integer contadorReservas, String descripcion, BigDecimal precioNoche, List<ServicioResponseDTO> servicios, String amenidadesExtra) {
        this.idPropiedad = idPropiedad;
        this.idDueno = idDueno;
        this.nombreComercial = nombreComercial;
        this.direccion = direccion;
        this.telefono = telefono;
        this.seRentaPorHabitaciones = seRentaPorHabitaciones;
        this.contadorReservas = contadorReservas;
        this.descripcion = descripcion;
        this.precioNoche = precioNoche; 
        this.servicios = servicios;
        this.amenidadesExtra = amenidadesExtra;
    }

    // --- Getters y Setters ---

    public Integer getIdPropiedad() {
        return idPropiedad;
    }

    public void setIdPropiedad(Integer idPropiedad) {
        this.idPropiedad = idPropiedad;
    }

    public Integer getIdDueno() {
        return idDueno;
    }

    public void setIdDueno(Integer idDueno) {
        this.idDueno = idDueno;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public DireccionResponseDTO getDireccion() {
        return direccion;
    }

    public void setDireccion(DireccionResponseDTO direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isSeRentaPorHabitaciones() {
        return seRentaPorHabitaciones;
    }

    public void setSeRentaPorHabitaciones(boolean seRentaPorHabitaciones) {
        this.seRentaPorHabitaciones = seRentaPorHabitaciones;
    }

    public Integer getContadorReservas() {
        return contadorReservas;
    }

    public void setContadorReservas(Integer contadorReservas) {
        this.contadorReservas = contadorReservas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioNoche() {
        return precioNoche;
    }

    public void setPrecioNoche(BigDecimal precioNoche) {
        this.precioNoche = precioNoche;
    }

    public List<ServicioResponseDTO> getServicios() {
        return servicios;
    }

    public void setServicios(List<ServicioResponseDTO> servicios) {
        this.servicios = servicios;
    }

    public List<FotoResponseDTO> getFotosGenerales() {
        return fotosGenerales;
    }

    public void setFotosGenerales(List<FotoResponseDTO> fotosGenerales) {
        this.fotosGenerales = fotosGenerales;
    }

    public String getEstadoPropiedad() {
        return estadoPropiedad;
    }

    public void setEstadoPropiedad(String estadoPropiedad) {
        this.estadoPropiedad = estadoPropiedad;
    }

    public String getAmenidadesExtra() {
        return amenidadesExtra;
    }

    public void setAmenidadesExtra(String amenidadesExtra) {
        this.amenidadesExtra = amenidadesExtra;
    }

    public LocalTime getHoraCheckIn() {
        return horaCheckIn;
    }

    public void setHoraCheckIn(LocalTime horaCheckIn) {
        this.horaCheckIn = horaCheckIn;
    }

    public LocalTime getHoraCheckOut() {
        return horaCheckOut;
    }

    public void setHoraCheckOut(LocalTime horaCheckOut) {
        this.horaCheckOut = horaCheckOut;
    }

    public Double getCalificacionPromedio() {
        return calificacionPromedio;
    }

    public void setCalificacionPromedio(Double calificacionPromedio) {
        this.calificacionPromedio = calificacionPromedio;
    }

    public Integer getCantidadResenas() {
        return cantidadResenas;
    }

    public void setCantidadResenas(Integer cantidadResenas) {
        this.cantidadResenas = cantidadResenas;
    }

    public BigDecimal getPrecioMinimo() {
        return precioMinimo;
    }

    public void setPrecioMinimo(BigDecimal precioMinimo) {
        this.precioMinimo = precioMinimo;
    }

    public BigDecimal getPrecioMaximo() {
        return precioMaximo;
    }

    public void setPrecioMaximo(BigDecimal precioMaximo) {
        this.precioMaximo = precioMaximo;
    }
}