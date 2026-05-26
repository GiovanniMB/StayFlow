package com.StayFlow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Estadísticas generales para el dashboard de administrador")
public class EstadisticasAdminResponseDTO {

    @Schema(description = "Total de usuarios registrados", example = "150")
    private long totalUsuarios;

    @Schema(description = "Usuarios activos", example = "120")
    private long usuariosActivos;

    @Schema(description = "Usuarios bloqueados", example = "30")
    private long usuariosBloqueados;

    @Schema(description = "Total de propiedades", example = "45")
    private long totalPropiedades;

    @Schema(description = "Propiedades publicadas", example = "38")
    private long propiedadesPublicadas;

    @Schema(description = "Propiedades en borrador", example = "7")
    private long propiedadesBorrador;

    @Schema(description = "Total de reservas", example = "320")
    private long totalReservas;

    @Schema(description = "Reservas confirmadas", example = "280")
    private long reservasConfirmadas;

    @Schema(description = "Reservas pendientes", example = "25")
    private long reservasPendientes;

    @Schema(description = "Reservas canceladas", example = "15")
    private long reservasCanceladas;

    @Schema(description = "Ingresos totales", example = "450000.00")
    private BigDecimal ingresosTotales;

    @Schema(description = "Ingresos del mes actual", example = "35000.00")
    private BigDecimal ingresosMes;

    // Constructor vacío
    public EstadisticasAdminResponseDTO() {}

    // Constructor con parámetros principales
    public EstadisticasAdminResponseDTO(long totalUsuarios, long usuariosActivos, 
                                        long totalPropiedades, long totalReservas, 
                                        long reservasConfirmadas) {
        this.totalUsuarios = totalUsuarios;
        this.usuariosActivos = usuariosActivos;
        this.usuariosBloqueados = totalUsuarios - usuariosActivos;
        this.totalPropiedades = totalPropiedades;
        this.totalReservas = totalReservas;
        this.reservasConfirmadas = reservasConfirmadas;
    }

    // Getters y Setters
    public long getTotalUsuarios() { return totalUsuarios; }
    public void setTotalUsuarios(long totalUsuarios) { this.totalUsuarios = totalUsuarios; }

    public long getUsuariosActivos() { return usuariosActivos; }
    public void setUsuariosActivos(long usuariosActivos) { this.usuariosActivos = usuariosActivos; }

    public long getUsuariosBloqueados() { return usuariosBloqueados; }
    public void setUsuariosBloqueados(long usuariosBloqueados) { this.usuariosBloqueados = usuariosBloqueados; }

    public long getTotalPropiedades() { return totalPropiedades; }
    public void setTotalPropiedades(long totalPropiedades) { this.totalPropiedades = totalPropiedades; }

    public long getPropiedadesPublicadas() { return propiedadesPublicadas; }
    public void setPropiedadesPublicadas(long propiedadesPublicadas) { this.propiedadesPublicadas = propiedadesPublicadas; }

    public long getPropiedadesBorrador() { return propiedadesBorrador; }
    public void setPropiedadesBorrador(long propiedadesBorrador) { this.propiedadesBorrador = propiedadesBorrador; }

    public long getTotalReservas() { return totalReservas; }
    public void setTotalReservas(long totalReservas) { this.totalReservas = totalReservas; }

    public long getReservasConfirmadas() { return reservasConfirmadas; }
    public void setReservasConfirmadas(long reservasConfirmadas) { this.reservasConfirmadas = reservasConfirmadas; }

    public long getReservasPendientes() { return reservasPendientes; }
    public void setReservasPendientes(long reservasPendientes) { this.reservasPendientes = reservasPendientes; }

    public long getReservasCanceladas() { return reservasCanceladas; }
    public void setReservasCanceladas(long reservasCanceladas) { this.reservasCanceladas = reservasCanceladas; }

    public BigDecimal getIngresosTotales() { return ingresosTotales; }
    public void setIngresosTotales(BigDecimal ingresosTotales) { this.ingresosTotales = ingresosTotales; }

    public BigDecimal getIngresosMes() { return ingresosMes; }
    public void setIngresosMes(BigDecimal ingresosMes) { this.ingresosMes = ingresosMes; }
}