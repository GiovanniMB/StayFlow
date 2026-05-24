package com.StayFlow.service.interfaces;
import java.time.LocalDate;
import java.util.List;

import com.StayFlow.dto.request.PropiedadRequestDTO;
import com.StayFlow.dto.response.PropiedadResponseDTO;

public interface IPropiedadService {
    PropiedadResponseDTO crearPropiedad(PropiedadRequestDTO request);
    List<PropiedadResponseDTO> obtenerTodas();
    PropiedadResponseDTO obtenerPorId(Integer idPropiedad);
    PropiedadResponseDTO actualizarPropiedad(Integer idPropiedad, PropiedadRequestDTO request);
    void eliminarPropiedad(Integer idPropiedad);
    //método para buscar disponibilidad
    List<PropiedadResponseDTO> obtenerPropiedadesDisponibles(LocalDate fechaEntrada, LocalDate fechaSalida);
    List<PropiedadResponseDTO> obtenerPropiedadesPorAnfitrion(Integer idAnfitrion);
    List<PropiedadResponseDTO> obtenerCatalogoPublico();

    org.springframework.data.domain.Page<PropiedadResponseDTO> buscarCatalogoPaginado(
        String termino, LocalDate checkin, LocalDate checkout, List<Integer> servicios, java.math.BigDecimal precioMaximo, int page, int size);
}