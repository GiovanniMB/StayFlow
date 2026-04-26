package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
    
    @Schema(description = "Lista de servicios generales de la propiedad (Wifi, Alberca, etc.)")
    private List<ServicioResponseDTO> servicios;

    // --- Constructor vacío ---
    public PropiedadResponseDTO() {
    }

    // --- Constructor con parámetros ---
    public PropiedadResponseDTO(Integer idPropiedad, Integer idDueno, String nombreComercial, DireccionResponseDTO direccion, String telefono, boolean seRentaPorHabitaciones, Integer contadorReservas, String descripcion, List<ServicioResponseDTO> servicios) {
        this.idPropiedad = idPropiedad;
        this.idDueno = idDueno;
        this.nombreComercial = nombreComercial;
        this.direccion = direccion;
        this.telefono = telefono;
        this.seRentaPorHabitaciones = seRentaPorHabitaciones;
        this.contadorReservas = contadorReservas;
        this.descripcion = descripcion;
        this.servicios = servicios;
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

    public List<ServicioResponseDTO> getServicios() {
        return servicios;
    }

    public void setServicios(List<ServicioResponseDTO> servicios) {
        this.servicios = servicios;
    }
}