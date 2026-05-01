package com.StayFlow.model;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "tipohabitacion")
@Schema(description = "Define las categorías o tipos de habitación disponibles en una propiedad (Suite, Doble, Individual, etc.)")
public class TipoHabitacion extends AuditoriaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del tipo de habitación", example = "1")
    private Integer idTipoHabitacion;

    @ManyToOne
    @JoinColumn(name = "idPropiedad", nullable = false)
    @Schema(description = "Propiedad a la que pertenece este tipo de habitación")
    private Propiedad propiedad;

    @Schema(description = "Nombre de la categoría", example = "Suite Presidencial", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 50)
    private String nombreTipo;

    @Schema(description = "Capacidad máxima de personas", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private Integer capacidad;

    @Schema(description = "Precio base por noche fuera de temporada", example = "2500.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioBaseNoche;

    @Schema(description = "Indica si la habitación tiene baño privado", example = "true")
    private boolean tieneBanoPrivado = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "tipohabitacionservicio",
        joinColumns = @JoinColumn(name = "idTipoHabitacion"),
        inverseJoinColumns = @JoinColumn(name = "idServicio")
    )
    @Schema(description = "Lista de servicios incluidos en este tipo de habitación")
    private List<Servicio> servicios;

    @OneToMany(mappedBy = "tipoHabitacion")
    @Schema(description = "Lista de fotografías asociadas a esta categoría")
    private List<FotoHabitacion> fotos;

    @OneToMany(mappedBy = "tipoHabitacion", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Schema(description = "Lista de camas configuradas para esta categoría")
    private List<TipoHabitacionCama> camas = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "tipoHabitacion")
    private List<Habitacion> habitaciones;

    // Constructores
    public TipoHabitacion() {}

    public TipoHabitacion(Propiedad propiedad, String nombreTipo, Integer capacidad, 
                          BigDecimal precioBaseNoche, boolean tieneBanoPrivado) {
        this.propiedad = propiedad;
        this.nombreTipo = nombreTipo;
        this.capacidad = capacidad;
        this.precioBaseNoche = precioBaseNoche;
        this.tieneBanoPrivado = tieneBanoPrivado;
    }

    // Getters y Setters
    public Integer getIdTipoHabitacion() {
        return idTipoHabitacion;
    }

    public void setIdTipoHabitacion(Integer idTipoHabitacion) {
        this.idTipoHabitacion = idTipoHabitacion;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public BigDecimal getPrecioBaseNoche() {
        return precioBaseNoche;
    }

    public void setPrecioBaseNoche(BigDecimal precioBaseNoche) {
        this.precioBaseNoche = precioBaseNoche;
    }

    public boolean isTieneBanoPrivado() {
        return tieneBanoPrivado;
    }

    public void setTieneBanoPrivado(boolean tieneBanoPrivado) {
        this.tieneBanoPrivado = tieneBanoPrivado;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public List<FotoHabitacion> getFotos() { 
        return fotos; 
    }
    
    public void setFotos(List<FotoHabitacion> fotos) { 
        this.fotos = fotos; 
    }

    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(List<Habitacion> habitaciones) {
        this.habitaciones = habitaciones;
    }

    // Getters y setters de camas
    public List<TipoHabitacionCama> getCamas() { return camas; }
    public void setCamas(List<TipoHabitacionCama> camas) { this.camas = camas; }

    public void addCama(TipoCama tipoCama, int cantidad) {
        TipoHabitacionCama thc = new TipoHabitacionCama(this, tipoCama, cantidad);
        camas.add(thc);
    }

    public List<String> getNombresCamas() {
        if (camas.isEmpty()) return new java.util.ArrayList<>();
        return camas.stream()
            .map(hc -> hc.getCantidad() + "x " + hc.getTipoCama().getNombre())
            .collect(java.util.stream.Collectors.toList());
    }

    public int getTotalCamas() {
        return camas.stream()
            .mapToInt(TipoHabitacionCama::getCantidad)
            .sum();
    }
}
