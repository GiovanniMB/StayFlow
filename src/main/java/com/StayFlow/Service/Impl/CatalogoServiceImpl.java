package com.StayFlow.service.impl;

import com.StayFlow.repository.CategoriaFotoRepository;
import com.StayFlow.repository.ColoniaRepository;
import com.StayFlow.repository.EstadoRepository;
import com.StayFlow.repository.MunicipioRepository;
import com.StayFlow.repository.ServicioRepository;
import com.StayFlow.repository.TipoCamaRepository;
import com.StayFlow.service.interfaces.ICatalogoService;
import com.StayFlow.dto.response.CatalogoResponseDTO;
import com.StayFlow.dto.response.ServicioResponseDTO;
import com.StayFlow.dto.response.TipoCamaResponseDTO;
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
    private final ServicioRepository servicioRepository;
    private final TipoCamaRepository tipoCamaRepository;
    private final CategoriaFotoRepository categoriaFotoRepository;

    // Inyecta la dependencia pasándola como parámetro en el constructor
    public CatalogoServiceImpl(EstadoRepository estadoRepository, 
                               MunicipioRepository municipioRepository, 
                               ColoniaRepository coloniaRepository,
                               ServicioRepository servicioRepository,
                               TipoCamaRepository tipoCamaRepository,
                               CategoriaFotoRepository categoriaFotoRepository ) {
        this.estadoRepository = estadoRepository;
        this.municipioRepository = municipioRepository;
        this.coloniaRepository = coloniaRepository;
        this.servicioRepository = servicioRepository;
        this.tipoCamaRepository = tipoCamaRepository;
        this.categoriaFotoRepository = categoriaFotoRepository;
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

    @Override
    @Transactional(readOnly = true)
    public List<ServicioResponseDTO> obtenerTodosLosServicios() {
        return servicioRepository.findAll().stream()
                .filter(servicio -> !servicio.isEstaEliminado()) // Regla de negocio: Solo activos
                .map(servicio -> {
                    ServicioResponseDTO dto = new ServicioResponseDTO();
                    dto.setIdServicio(servicio.getIdServicio());
                    dto.setNombreServicio(servicio.getNombreServicio());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoCamaResponseDTO> obtenerTiposCama() {
        return tipoCamaRepository.findAll().stream()
                .map(cama -> {
                    TipoCamaResponseDTO dto = new TipoCamaResponseDTO();
                    dto.setIdTipoCama(cama.getIdTipoCama());
                    dto.setNombreCama(cama.getNombre()); 
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogoResponseDTO> obtenerCategoriasFoto() {
        return categoriaFotoRepository.findAll().stream()
                .filter(cat -> !cat.isEstaEliminado())
                .map(cat -> new CatalogoResponseDTO(cat.getIdCategoriaFoto(), cat.getNombreCategoria()))
                .collect(Collectors.toList());
    }
}