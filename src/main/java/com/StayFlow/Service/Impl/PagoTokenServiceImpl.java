package com.StayFlow.service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.StayFlow.dto.request.PagoTokenRequestDTO;
import com.StayFlow.dto.response.PagoTokenResponseDTO;
import com.StayFlow.exception.PaymentException;
import com.StayFlow.model.MetodoPago;
import com.StayFlow.model.PagoToken;
import com.StayFlow.model.TipoTarjeta;
import com.StayFlow.model.Usuario;
import com.StayFlow.repository.MetodoPagoRepository;
import com.StayFlow.repository.PagoTokenRepository;
import com.StayFlow.repository.TipoTarjetaRepository;
import com.StayFlow.repository.UsuarioRepository;
import com.StayFlow.service.interfaces.IPagoTokenService;

@Service
public class PagoTokenServiceImpl implements IPagoTokenService {

    @Autowired
    private PagoTokenRepository pagoTokenRepository;
    
    @Autowired
    private MetodoPagoRepository metodoPagoRepository;
    
    @Autowired
    private TipoTarjetaRepository tipoTarjetaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public PagoTokenResponseDTO guardarToken(PagoTokenRequestDTO request, Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new PaymentException("Usuario no encontrado"));
        
        MetodoPago metodo = metodoPagoRepository.findById(request.getIdMetodoPago())
            .orElseThrow(() -> new PaymentException("Método de pago inválido"));
        
        TipoTarjeta tipoTarjeta = tipoTarjetaRepository.findById(request.getIdTipoTarjeta())
            .orElseThrow(() -> new PaymentException("Tipo de tarjeta inválido"));
        
        // Verificar si este token ya existe para este usuario
        if (pagoTokenRepository.existsByUsuario_IdUsuarioAndTokenGatewayAndActivoTrue(idUsuario, request.getTokenGateway())) {
            throw new PaymentException("Este método de pago ya está registrado");
        }
        
        PagoToken token = new PagoToken();
        token.setUsuario(usuario);
        token.setMetodoPago(metodo);
        token.setTipoTarjeta(tipoTarjeta);
        token.setTokenGateway(request.getTokenGateway());
        token.setUltimosDigitos(request.getUltimosDigitos());
        token.setNombreTitular(request.getNombreTitular());
        token.setFechaExpiracion(request.getFechaExpiracion());
        token.setActivo(true);
        
        PagoToken saved = pagoTokenRepository.save(token);
        
        PagoTokenResponseDTO response = new PagoTokenResponseDTO();
        response.setIdPagoToken(saved.getIdPagoToken());
        response.setMetodoPago(metodo.getNombre());
        response.setTipoTarjeta(tipoTarjeta.getNombre());
        response.setUltimosDigitos(saved.getUltimosDigitos());
        response.setNombreTitular(saved.getNombreTitular());
        response.setFechaExpiracion(saved.getFechaExpiracion());
        
        return response;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PagoTokenResponseDTO> listarTokensUsuario(Integer idUsuario) {
        return pagoTokenRepository.findByUsuario_IdUsuarioAndActivoTrue(idUsuario)
            .stream()
            .map(token -> {
                PagoTokenResponseDTO dto = new PagoTokenResponseDTO();
                dto.setIdPagoToken(token.getIdPagoToken());
                dto.setMetodoPago(token.getMetodoPago().getNombre());
                dto.setTipoTarjeta(token.getTipoTarjeta() != null ? token.getTipoTarjeta().getNombre() : null);
                dto.setUltimosDigitos(token.getUltimosDigitos());
                dto.setNombreTitular(token.getNombreTitular());
                dto.setFechaExpiracion(token.getFechaExpiracion());
                return dto;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void eliminarToken(Integer idToken, Integer idUsuario) {
        PagoToken token = pagoTokenRepository.findByIdPagoTokenAndUsuario_IdUsuarioAndActivoTrue(idToken, idUsuario)
            .orElseThrow(() -> new PaymentException("Token no encontrado o ya inactivo"));
        token.setActivo(false);
        pagoTokenRepository.save(token);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PagoToken validarYObtenerToken(Integer idToken, Integer idUsuario) {
        return pagoTokenRepository.findByIdPagoTokenAndUsuario_IdUsuarioAndActivoTrue(idToken, idUsuario)
            .orElseThrow(() -> new PaymentException("Token de pago inválido o expirado"));
    }
}