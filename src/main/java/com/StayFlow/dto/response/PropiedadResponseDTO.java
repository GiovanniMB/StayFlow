package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Datos de salida de una propiedad")
public class PropiedadResponseDTO {
    
    private Integer idPropiedad;
    private Integer idDueno; // Solo enviamos el ID del dueño por seguridad
    private String nombreComercial;
    private DireccionResponseDTO direccion;
    private String telefono;
    private boolean seRentaPorHabitaciones;
    private Integer contadorReservas;
    private String descripcion;
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