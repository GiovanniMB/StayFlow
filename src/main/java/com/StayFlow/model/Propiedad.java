package com.StayFlow.model;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "propiedad")
@Schema(description = "Representa una propiedad u hotel registrado en el sistema")
public class Propiedad extends AuditoriaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la propiedad", example = "1")
    private Integer idPropiedad;

    @ManyToOne
    @JoinColumn(name = "idDueno")
    @Schema(description = "Usuario dueño o administrador de la propiedad")
    private Usuario dueno;

    @Schema(description = "Nombre comercial del hotel o propiedad", example = "Hotel Paraíso", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 100)
    private String nombreComercial;

    @ManyToOne
    @JoinColumn(name = "idDireccion")
    @Schema(description = "Dirección física completa de la propiedad")
    private Direccion direccion;

    @Schema(description = "Teléfono de contacto de la propiedad", example = "5512345678")
    @Column(length = 20)
    private String telefono;

    @Schema(description = "Indica si la propiedad se renta por habitaciones individuales (TRUE) o como casa completa (FALSE)", example = "true")
    private boolean seRentaPorHabitaciones = false;

    @Schema(description = "Contador de reservas totales de la propiedad", example = "150")
    private Integer contadorReservas = 0;

    @Schema(description = "Descripción detallada de la propiedad", example = "Hermoso hotel boutique en el centro de la ciudad")
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "propiedad_servicio",
        joinColumns = @JoinColumn(name = "idPropiedad"),
        inverseJoinColumns = @JoinColumn(name = "idServicio")
    )
    @Schema(description = "Lista de servicios generales que ofrece esta propiedad")
    private List<Servicio> servicios;

    @OneToMany(mappedBy = "propiedad")
    private List<FotoHabitacion> fotos;

    @Column(name = "precioNoche", precision = 10, scale = 2)
    private BigDecimal precioNoche;

    @OneToMany(mappedBy = "propiedad")
    private List<TipoHabitacion> tiposHabitacion;

    public enum EstadoPropiedad {
        BORRADOR,   // Incompleta (sin fotos/cuartos), no visible al público
        PUBLICADA,  // Lista y visible en el catálogo
        OCULTA      // Pausada manualmente por el anfitrión
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_propiedad", nullable = true) 
    private EstadoPropiedad estadoPropiedad = EstadoPropiedad.PUBLICADA; 

    @Schema(description = "Amenidades extra personalizadas escritas por el usuario")
    @Column(columnDefinition = "TEXT")
    private String amenidadesExtra;

    // Constructores
    public Propiedad() {}

    // Getters y Setters
    public Integer getIdPropiedad() {
        return idPropiedad;
    }

    public void setIdPropiedad(Integer idPropiedad) {
        this.idPropiedad = idPropiedad;
    }

    public Usuario getDueno() {
        return dueno;
    }

    public void setDueno(Usuario dueno) {
        this.dueno = dueno;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
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

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public List<TipoHabitacion> getTiposHabitacion() {
        return tiposHabitacion;
    }

    public void setTiposHabitacion(List<TipoHabitacion> tiposHabitacion) {
        this.tiposHabitacion = tiposHabitacion;
    }

    // Método de utilidad
    public void incrementarContadorReservas() {
        if (this.contadorReservas == null) {
            this.contadorReservas = 0;
        }
        this.contadorReservas++;
    }

    public List<FotoHabitacion> getFotos() { 
        return fotos; 
    }

    public void setFotos(List<FotoHabitacion> fotos) { 
        this.fotos = fotos; 
    }

    public BigDecimal getPrecioNoche() {
        return precioNoche;
    }

    public void setPrecioNoche(BigDecimal precioNoche) {
        this.precioNoche = precioNoche;
    }

    public EstadoPropiedad getEstadoPropiedad() {
        return estadoPropiedad;
    }

    public void setEstadoPropiedad(EstadoPropiedad estadoPropiedad) {
        this.estadoPropiedad = estadoPropiedad;
    }

    public String getAmenidadesExtra() {
        return amenidadesExtra;
    }
    public void setAmenidadesExtra(String amenidadesExtra) {
        this.amenidadesExtra = amenidadesExtra;
    }
}