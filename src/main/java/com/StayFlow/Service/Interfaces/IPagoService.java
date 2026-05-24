package com.StayFlow.service.interfaces;

import java.util.List;

import com.StayFlow.dto.request.PagoRequestDTO;
import com.StayFlow.dto.response.PagoResponseDTO;

public interface IPagoService {
    
    PagoResponseDTO procesarPago(PagoRequestDTO request, Integer idUsuario);
    
    PagoResponseDTO obtenerEstadoPago(Integer idPago, Integer idUsuario);
    
    List<PagoResponseDTO> obtenerPagosPorUsuario(Integer idUsuario);

    PagoResponseDTO reembolsarPorReserva(Integer idReserva, java.math.BigDecimal montoReembolso);

    PagoResponseDTO confirmarTransferencia(Integer idPago);
PagoResponseDTO marcarComoFallido(Integer idPago, String motivo);
PagoResponseDTO reembolsarPago(Integer idPago, String motivo);
PagoResponseDTO marcarEnRevision(Integer idPago, String motivo);
}