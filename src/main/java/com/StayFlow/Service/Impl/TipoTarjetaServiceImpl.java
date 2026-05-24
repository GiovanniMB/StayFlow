package com.StayFlow.service.Impl;

import com.StayFlow.dto.response.TipoTarjetaResponseDTO;
import com.StayFlow.exception.ResourceNotFoundException;
import com.StayFlow.model.TipoTarjeta;
import com.StayFlow.repository.TipoTarjetaRepository;
import com.StayFlow.service.interfaces.ITipoTarjetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TipoTarjetaServiceImpl implements ITipoTarjetaService {
    
    @Autowired
    private TipoTarjetaRepository tipoTarjetaRepository;
    
    @Override
    public List<TipoTarjetaResponseDTO> obtenerTodosLosTiposTarjeta() {
        List<TipoTarjeta> tipos = tipoTarjetaRepository.findAll();
        
        return tipos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public TipoTarjetaResponseDTO obtenerTipoTarjetaPorId(Integer id) {
        TipoTarjeta tipo = tipoTarjetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoTarjeta", "id", id));
        
        return convertToDTO(tipo);
    }
    
    @Override
    public TipoTarjetaResponseDTO obtenerTipoTarjetaPorCodigo(String codigo) {
        TipoTarjeta tipo = tipoTarjetaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("TipoTarjeta", "código", codigo));
        
        return convertToDTO(tipo);
    }
    
    private TipoTarjetaResponseDTO convertToDTO(TipoTarjeta tipo) {
        TipoTarjetaResponseDTO dto = new TipoTarjetaResponseDTO();
        dto.setIdTipoTarjeta(tipo.getIdTipoTarjeta());
        dto.setNombre(tipo.getNombre());
        dto.setCodigo(tipo.getCodigo());
        return dto;
    }
}