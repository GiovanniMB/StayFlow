package com.StayFlow.mapper;

import com.StayFlow.dto.response.ReservaResponseDTO;
import com.StayFlow.model.Reserva;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservaMapper {

    public ReservaResponseDTO toResponseDTO(Reserva reserva) {
        if (reserva == null) return null;

        ReservaResponseDTO dto = new ReservaResponseDTO();
        dto.setIdReserva(reserva.getIdReserva());
        
        if (reserva.getHabitacion() != null) {
            dto.setIdHabitacion(reserva.getHabitacion().getIdHabitacion());
            dto.setNumeroHabitacion(reserva.getHabitacion().getNumeroHabitacion());
            
            // Extracción de datos de la Propiedad para la UI
            try {
                var habitacion = reserva.getHabitacion();
                var propiedad = habitacion != null ? habitacion.getPropiedad() : null;
                
                if (propiedad != null) {
                    dto.setIdPropiedad(propiedad.getIdPropiedad());
                    
                    String nombre = propiedad.getNombreComercial();
                    dto.setNombrePropiedad(nombre != null ? nombre : "Alojamiento StayFlow");
                    
                    String urlImagen = null;

                    // Busca la foto en la categoria de la habitacion
                    if (habitacion.getTipoHabitacion() != null) {
                        try {
                            var fotosTipo = habitacion.getTipoHabitacion().getFotos();
                            if (fotosTipo != null && !fotosTipo.isEmpty()) {
                                urlImagen = fotosTipo.get(0).getUrlFoto();
                            }
                        } catch (Exception e) {
                            // Silencioso en caso de error de lectura de la entidad
                        }
                    }

                    // Si no es hotel, recuperamos la foto de la propiedad entera
                    if (urlImagen == null && propiedad.getFotos() != null && !propiedad.getFotos().isEmpty()) {
                        urlImagen = propiedad.getFotos().get(0).getUrlFoto();
                    }
                    
                    dto.setImagenPortada(urlImagen); 
                }
            } catch (Exception e) {
                System.out.println("Error crítico al mapear la foto: " + e.getMessage());
            }
        }
        
        if (reserva.getCliente() != null) {
            dto.setIdCliente(reserva.getCliente().getIdUsuario());
            dto.setNombreCliente(reserva.getCliente().getNombre() + " " + reserva.getCliente().getApellidoPaterno());
        }
        
        dto.setFechaEntrada(reserva.getFechaEntrada());
        dto.setFechaSalida(reserva.getFechaSalida());
        dto.setMontoTotal(reserva.getMontoTotal());
        
        if (reserva.getEstadoReserva() != null) {
            dto.setEstadoReserva(reserva.getEstadoReserva().name());
        }
        
        return dto;
    }

    public List<ReservaResponseDTO> toResponseDTOList(List<Reserva> reservas) {
        if (reservas == null) return null;
        return reservas.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }
}