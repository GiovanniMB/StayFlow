package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal; // Importación necesaria para el precio
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

    // 👇 Nueva variable agregada para mostrar el precio
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

    // --- Constructor vacío ---
    public PropiedadResponseDTO() {
    }

    // --- Constructor con parámetros actualizado ---
    public PropiedadResponseDTO(Integer idPropiedad, Integer idDueno, String nombreComercial, DireccionResponseDTO direccion, String telefono, boolean seRentaPorHabitaciones, Integer contadorReservas, String descripcion, BigDecimal precioNoche, List<ServicioResponseDTO> servicios, String amenidadesExtra) {
        this.idPropiedad = idPropiedad;
        this.idDueno = idDueno;
        this.nombreComercial = nombreComercial;
        this.direccion = direccion;
        this.telefono = telefono;
        this.seRentaPorHabitaciones = seRentaPorHabitaciones;
        this.contadorReservas = contadorReservas;
        this.descripcion = descripcion;
        this.precioNoche = precioNoche; // Asignación del nuevo campo
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

    // 👇 Nuevos métodos Getter y Setter
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
}