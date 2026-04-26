package com.StayFlow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

// DTO para recibir los datos de una propiedad en las solicitudes de creación o actualización. Incluye validaciones para asegurar que se proporcionen los datos necesarios y que tengan el formato correcto. Además, incluye un objeto anidado para la dirección, que también se valida.
@Schema(description = "Datos de entrada para registrar o actualizar una propiedad") // Descripción general del DTO para Swagger
public class PropiedadRequestDTO {

    @NotBlank(message = "El nombre comercial es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre comercial debe tener entre 3 y 100 caracteres")
    @Schema(description = "Nombre comercial", example = "Hotel Paraíso")
    private String nombreComercial;

    @Size(max = 10, message = "El teléfono no puede superar los 10 caracteres")
    @Schema(description = "Teléfono de contacto", example = "5512345678") // Se espera que el cliente proporcione un número de teléfono de contacto para la propiedad, que se validará en el backend para asegurar que no supere los 10 caracteres, el frontend puede mostrar un campo de texto con formato para que el usuario ingrese el número correctamente
    private String telefono;

    @NotNull(message = "Debe indicar si se renta por habitaciones (true o false)")
    @Schema(description = "Indica si se renta por habitaciones", example = "true") // Se espera que el cliente proporcione un valor booleano para indicar si la propiedad se renta por habitaciones, el backend validará que sea un valor booleano válido y el frontend puede mostrar un toggle o checkbox para que el usuario seleccione esta opción fácilmente
    private Boolean seRentaPorHabitaciones;

    @Schema(description = "Descripción de la propiedad", example = "Hermoso hotel céntrico")
    private String descripcion;

    @NotNull(message = "La dirección de la propiedad es obligatoria")
    @Valid // Esta anotación le dice a Spring que valide también el objeto hijo (DireccionRequestDTO)
    @Schema(description = "Datos de la dirección física")
    private DireccionRequestDTO direccion;

    // Para los servicios, recibe una lista de IDs que corresponden a los servicios que ofrece la propiedad.
    @Schema(description = "Lista de IDs de los servicios que ofrece la propiedad", example = "[1, 2, 3]")
    private List<Integer> idServicios;

    // --- Constructor vacío ---
    public PropiedadRequestDTO() {
    }

    // --- Constructor con parámetros ---
    public PropiedadRequestDTO(String nombreComercial, String telefono, Boolean seRentaPorHabitaciones, String descripcion, DireccionRequestDTO direccion, List<Integer> idServicios) {
        this.nombreComercial = nombreComercial;
        this.telefono = telefono;
        this.seRentaPorHabitaciones = seRentaPorHabitaciones;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.idServicios = idServicios;
    }

    // --- Getters y Setters ---

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getSeRentaPorHabitaciones() {
        return seRentaPorHabitaciones;
    }

    public void setSeRentaPorHabitaciones(Boolean seRentaPorHabitaciones) {
        this.seRentaPorHabitaciones = seRentaPorHabitaciones;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public DireccionRequestDTO getDireccion() {
        return direccion;
    }

    public void setDireccion(DireccionRequestDTO direccion) {
        this.direccion = direccion;
    }

    public List<Integer> getIdServicios() {
        return idServicios;
    }

    public void setIdServicios(List<Integer> idServicios) {
        this.idServicios = idServicios;
    }
}