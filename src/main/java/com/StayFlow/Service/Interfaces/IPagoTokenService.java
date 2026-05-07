package com.StayFlow.Service.Interfaces;

import java.util.List;

import com.StayFlow.dto.request.PagoTokenRequestDTO;
import com.StayFlow.dto.response.PagoTokenResponseDTO;
import com.StayFlow.model.PagoToken;

public interface IPagoTokenService {
    
    PagoTokenResponseDTO guardarToken(PagoTokenRequestDTO request, Integer idUsuario);
    
    List<PagoTokenResponseDTO> listarTokensUsuario(Integer idUsuario);
    
    void eliminarToken(Integer idToken, Integer idUsuario);
    
    PagoToken validarYObtenerToken(Integer idToken, Integer idUsuario);
}