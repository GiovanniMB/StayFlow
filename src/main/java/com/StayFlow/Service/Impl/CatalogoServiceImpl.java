package com.StayFlow.Service.Impl;

import com.StayFlow.Repository.ColoniaRepository;
import com.StayFlow.Repository.EstadoRepository;
import com.StayFlow.Repository.MunicipioRepository;
import com.StayFlow.Service.Interfaces.ICatalogoService;
import com.StayFlow.dto.response.CatalogoResponseDTO;
import com.StayFlow.model.Colonia;
import com.StayFlow.model.Estado;
import com.StayFlow.model.Municipio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogoServiceImpl implements ICatalogoService {

    private final EstadoRepository estadoRepository;
    private final MunicipioRepository municipioRepository;
    private final ColoniaRepository coloniaRepository;

    public CatalogoServiceImpl(EstadoRepository estadoRepository, 
                               MunicipioRepository municipioRepository, 
                               ColoniaRepository coloniaRepository) {
        this.estadoRepository = estadoRepository;
        this.municipioRepository = municipioRepository;
        this.coloniaRepository = coloniaRepository;
    }
    // Método para obtener todos los estados
    @Override
    @Transactional(readOnly = true)
    public List<CatalogoResponseDTO> obtenerTodosLosEstados() {
        List<Estado> estados = estadoRepository.findAll();
        return estados.stream()
                .map(estado -> new CatalogoResponseDTO(estado.getId(), estado.getNombre()))
                .collect(Collectors.toList());
    }
    // Método para obtener municipios por ID de estado
    @Override
    @Transactional(readOnly = true)
    public List<CatalogoResponseDTO> obtenerMunicipiosPorEstado(Integer idEstado) {
        List<Municipio> municipios = municipioRepository.buscarPorIdEstado(idEstado);
        return municipios.stream()
                .map(municipio -> new CatalogoResponseDTO(municipio.getId(), municipio.getNombre()))
                .collect(Collectors.toList());
    }
    // Método para obtener colonias por ID de municipio
    @Override
    @Transactional(readOnly = true)
    public List<CatalogoResponseDTO> obtenerColoniasPorMunicipio(Integer idMunicipio) {
        List<Colonia> colonias = coloniaRepository.buscarPorIdMunicipio(idMunicipio);
        return colonias.stream()
                .map(colonia -> new CatalogoResponseDTO(colonia.getId(), colonia.getNombre()))
                .collect(Collectors.toList());
    }
}