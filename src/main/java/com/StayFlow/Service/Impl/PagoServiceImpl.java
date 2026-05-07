package com.StayFlow.Service.Impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.StayFlow.dto.request.PagoRequestDTO;
import com.StayFlow.dto.response.PagoResponseDTO;
import com.StayFlow.exception.PaymentException;
import com.StayFlow.model.EstadoPago;
import com.StayFlow.model.MetodoPago;
import com.StayFlow.model.Pago;
import com.StayFlow.model.PagoInfo;
import com.StayFlow.model.PagoToken;
import com.StayFlow.model.Reserva;
import com.StayFlow.model.Usuario;
import com.StayFlow.Repository.EstadoPagoRepository;
import com.StayFlow.Repository.MetodoPagoRepository;
import com.StayFlow.Repository.PagoInfoRepository;
import com.StayFlow.Repository.PagoRepository;
import com.StayFlow.Repository.ReservaRepository;
import com.StayFlow.Service.Interfaces.IPagoService;
import com.StayFlow.Service.Interfaces.IPagoTokenService;

@Service
public class PagoServiceImpl implements IPagoService {

    @Autowired
    private PagoRepository pagoRepository;
    
    @Autowired
    private PagoInfoRepository pagoInfoRepository;
    
    @Autowired
    private EstadoPagoRepository estadoPagoRepository;
    
    @Autowired
    private MetodoPagoRepository metodoPagoRepository;
    
    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private IPagoTokenService pagoTokenService;

    @Autowired
    private com.StayFlow.Service.EmailService emailService;

    

    @Override
    @Transactional
    public PagoResponseDTO procesarPago(PagoRequestDTO request, Integer idUsuario) {
        // 1. Validar reserva
        Reserva reserva = reservaRepository.findById(request.getIdReserva())
            .orElseThrow(() -> new PaymentException("Reserva no encontrada"));
        
        // Validar que el usuario sea el dueño de la reserva
        if (!reserva.getCliente().getIdUsuario().equals(idUsuario)) {
            throw new PaymentException("No autorizado para pagar esta reserva");
        }
        
        // Validar que la reserva no esté cancelada
        if (reserva.getEstadoReserva() == Reserva.EstadoReserva.cancelada) {
            throw new PaymentException("No se puede pagar una reserva cancelada");
        }
        
        // Validar que no haya un pago completado previo
        if (pagoRepository.findByReserva_IdReservaAndEstaEliminadoFalse(request.getIdReserva()).isPresent()) {
            throw new PaymentException("Esta reserva ya tiene un pago registrado");
        }
        
        // 2. Validar método de pago
        MetodoPago metodo = metodoPagoRepository.findById(request.getIdMetodoPago())
            .orElseThrow(() -> new PaymentException("Método de pago inválido"));
        
        // 3. Procesar con gateway según método
        Map<String, String> gatewayResponse = procesarConGateway(request, metodo, idUsuario);
        
        // 4. Determinar estado inicial del pago
        EstadoPago estadoInicial = determinarEstadoInicial(gatewayResponse.get("status"));
        
        // 5. Crear pago
        Pago pago = new Pago();
        pago.setReserva(reserva);
        pago.setMetodoPago(metodo);
        pago.setEstadoPago(estadoInicial);
        pago.setMonto(request.getMonto());
        pago.setMoneda(request.getMoneda() != null ? request.getMoneda() : "MXN");
        pago.setFechaPago(LocalDateTime.now());
        pago.setFechaActualizacion(LocalDateTime.now());
        pago.setEstaEliminado(false);
        
        Pago savedPago = pagoRepository.save(pago);
        
        // 6. Guardar información adicional del gateway
        for (Map.Entry<String, String> entry : gatewayResponse.entrySet()) {
            if (!"status".equals(entry.getKey())) {
                PagoInfo info = new PagoInfo();
                info.setPago(savedPago);
                info.setClave(entry.getKey());
                info.setValor(entry.getValue());
                pagoInfoRepository.save(info);
            }
        }
        // DISPARAR CORREOS SI EL PAGO FUE EXITOSO ---
        if (estadoInicial.getNombre().equals("completado")) {
            confirmarReservaYEnviarCorreos(reserva);
        }
        
        // 7. Construir respuesta
        PagoResponseDTO response = new PagoResponseDTO();
        response.setIdPago(savedPago.getIdPago());
        response.setIdReserva(reserva.getIdReserva());
        response.setMetodoPago(metodo.getNombre());
        response.setEstadoPago(estadoInicial.getNombre());
        response.setMonto(savedPago.getMonto());
        response.setMoneda(savedPago.getMoneda());
        response.setFechaPago(savedPago.getFechaPago().toString());
        response.setMensaje(gatewayResponse.get("mensaje"));
        response.setTransaccionId(gatewayResponse.get("transactionId"));
        response.setUltimosDigitos(gatewayResponse.get("ultimosDigitos"));
        
        return response;



    }
    
    private Map<String, String> procesarConGateway(PagoRequestDTO request, MetodoPago metodo, Integer idUsuario) {
        Map<String, String> response = new HashMap<>();
        
        switch (metodo.getNombre().toLowerCase()) {
            case "tarjeta":
                return procesarTarjeta(request, idUsuario);
            case "paypal":
                return procesarPaypal(request);
            case "transferencia":
                response.put("status", "PENDIENTE");
                response.put("mensaje", "Transferencia pendiente de confirmación. Por favor realiza la transferencia y envía el comprobante.");
                response.put("transactionId", "TRF-" + System.currentTimeMillis());
                break;
            case "efectivo":
                response.put("status", "PENDIENTE");
                response.put("mensaje", "Pago en efectivo pendiente de registro en sucursal.");
                response.put("transactionId", "EFEC-" + System.currentTimeMillis());
                break;
            default:
                throw new PaymentException("Método de pago no implementado: " + metodo.getNombre());
        }
        
        return response;
    }

    @Override
@Transactional
public PagoResponseDTO confirmarTransferencia(Integer idPago) {
    Pago pago = pagoRepository.findById(idPago)
        .orElseThrow(() -> new PaymentException("Pago no encontrado"));
    
    if (!pago.getMetodoPago().getNombre().equals("transferencia")) {
        throw new PaymentException("Solo se pueden confirmar pagos por transferencia");
    }
    
    if (!pago.getEstadoPago().getNombre().equals("pendiente")) {
        throw new PaymentException("El pago no está pendiente");
    }
    
    EstadoPago completado = estadoPagoRepository.findByNombre("completado")
        .orElseThrow(() -> new PaymentException("Estado no encontrado"));
    pago.setEstadoPago(completado);

    confirmarReservaYEnviarCorreos(pago.getReserva());
    
    return convertToDTO(pagoRepository.save(pago));
}

@Override
@Transactional
public PagoResponseDTO reembolsarPago(Integer idPago, String motivo) {
    Pago pago = pagoRepository.findById(idPago)
        .orElseThrow(() -> new PaymentException("Pago no encontrado"));
    
    if (!pago.getEstadoPago().getNombre().equals("completado")) {
        throw new PaymentException("Solo se pueden reembolsar pagos completados");
    }
    
    // Guardar motivo del reembolso en pago_info
    PagoInfo info = new PagoInfo();
    info.setPago(pago);
    info.setClave("motivo_reembolso");
    info.setValor(motivo);
    pagoInfoRepository.save(info);
    
    EstadoPago reembolsado = estadoPagoRepository.findByNombre("reembolsado")
        .orElseThrow(() -> new PaymentException("Estado no encontrado"));
    pago.setEstadoPago(reembolsado);
    
    return convertToDTO(pagoRepository.save(pago));
}
    
    private Map<String, String> procesarTarjeta(PagoRequestDTO request, Integer idUsuario) {
        Map<String, String> response = new HashMap<>();
        
        if (request.getIdPagoToken() != null) {
            // Usar token guardado
            PagoToken token = pagoTokenService.validarYObtenerToken(request.getIdPagoToken(), idUsuario);
            response.put("status", "EXITOSO");
            response.put("transactionId", "TXN-" + System.currentTimeMillis());
            response.put("mensaje", "Pago con tarjeta tokenizada exitoso");
            response.put("ultimosDigitos", token.getUltimosDigitos());
        } else if (request.getTokenGateway() != null && !request.getTokenGateway().isEmpty()) {
            // Token temporal del frontend
            response.put("status", "EXITOSO");
            response.put("transactionId", "TXN-" + System.currentTimeMillis());
            response.put("mensaje", "Pago con tarjeta exitoso");
            response.put("ultimosDigitos", "****");
        } else {
            throw new PaymentException("Se requiere token de pago o tarjeta guardada para pagar con tarjeta");
        }
        
        return response;
    }
    
    private Map<String, String> procesarPaypal(PagoRequestDTO request) {
        Map<String, String> response = new HashMap<>();
        if (request.getTokenGateway() == null || request.getTokenGateway().isEmpty()) {
            throw new PaymentException("Se requiere token de PayPal");
        }
        response.put("status", "EXITOSO");
        response.put("transactionId", "PP-" + System.currentTimeMillis());
        response.put("mensaje", "Pago con PayPal exitoso");
        response.put("ultimosDigitos", "paypal@email.com");
        return response;
    }
    
    private EstadoPago determinarEstadoInicial(String statusGateway) {
        if ("EXITOSO".equals(statusGateway)) {
            return estadoPagoRepository.findByNombre("completado")
                .orElseThrow(() -> new PaymentException("Estado 'completado' no encontrado en catálogo"));
        } else if ("PENDIENTE".equals(statusGateway)) {
            return estadoPagoRepository.findByNombre("pendiente")
                .orElseThrow(() -> new PaymentException("Estado 'pendiente' no encontrado en catálogo"));
        } else {
            return estadoPagoRepository.findByNombre("fallido")
                .orElseThrow(() -> new PaymentException("Estado 'fallido' no encontrado en catálogo"));
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public PagoResponseDTO obtenerEstadoPago(Integer idPago, Integer idUsuario) {
        Pago pago = pagoRepository.findById(idPago)
            .orElseThrow(() -> new PaymentException("Pago no encontrado"));
        
        if (!pago.getReserva().getCliente().getIdUsuario().equals(idUsuario)) {
            throw new PaymentException("No autorizado para ver este pago");
        }
        
        PagoResponseDTO response = new PagoResponseDTO();
        response.setIdPago(pago.getIdPago());
        response.setIdReserva(pago.getReserva().getIdReserva());
        response.setMetodoPago(pago.getMetodoPago().getNombre());
        response.setEstadoPago(pago.getEstadoPago().getNombre());
        response.setMonto(pago.getMonto());
        response.setMoneda(pago.getMoneda());
        response.setFechaPago(pago.getFechaPago().toString());
        
        pagoInfoRepository.findByPago_IdPago(idPago).stream()
            .filter(info -> "ultimosDigitos".equals(info.getClave()))
            .findFirst()
            .ifPresent(info -> response.setUltimosDigitos(info.getValor()));
        
        return response;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PagoResponseDTO> obtenerPagosPorUsuario(Integer idUsuario) {
        return pagoRepository.findByReserva_Cliente_IdUsuario(idUsuario)
            .stream()
            .map(pago -> {
                PagoResponseDTO dto = new PagoResponseDTO();
                dto.setIdPago(pago.getIdPago());
                dto.setIdReserva(pago.getReserva().getIdReserva());
                dto.setMetodoPago(pago.getMetodoPago().getNombre());
                dto.setEstadoPago(pago.getEstadoPago().getNombre());
                dto.setMonto(pago.getMonto());
                dto.setMoneda(pago.getMoneda());
                dto.setFechaPago(pago.getFechaPago().toString());
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
@Transactional
public PagoResponseDTO marcarComoFallido(Integer idPago, String motivo) {
    Pago pago = pagoRepository.findById(idPago)
        .orElseThrow(() -> new PaymentException("Pago no encontrado"));
    
    if (pago.getEstadoPago().getNombre().equals("completado")) {
        throw new PaymentException("No se puede marcar como fallido un pago ya completado");
    }
    
    // Guardar motivo
    PagoInfo info = new PagoInfo();
    info.setPago(pago);
    info.setClave("motivo_fallo");
    info.setValor(motivo);
    pagoInfoRepository.save(info);
    
    EstadoPago fallido = estadoPagoRepository.findByNombre("fallido")
        .orElseThrow(() -> new PaymentException("Estado 'fallido' no encontrado"));
    pago.setEstadoPago(fallido);
    
    return convertToDTO(pagoRepository.save(pago));
}

@Override
@Transactional
public PagoResponseDTO marcarEnRevision(Integer idPago, String motivo) {
    Pago pago = pagoRepository.findById(idPago)
        .orElseThrow(() -> new PaymentException("Pago no encontrado"));
    
    // Guardar motivo
    PagoInfo info = new PagoInfo();
    info.setPago(pago);
    info.setClave("motivo_revision");
    info.setValor(motivo);
    pagoInfoRepository.save(info);
    
    EstadoPago enRevision = estadoPagoRepository.findByNombre("en_revision")
        .orElseThrow(() -> new PaymentException("Estado 'en_revision' no encontrado"));
    pago.setEstadoPago(enRevision);
    
    return convertToDTO(pagoRepository.save(pago));
}

// Método auxiliar para convertir Pago a DTO
private PagoResponseDTO convertToDTO(Pago pago) {
    PagoResponseDTO dto = new PagoResponseDTO();
    dto.setIdPago(pago.getIdPago());
    dto.setIdReserva(pago.getReserva().getIdReserva());
    dto.setMetodoPago(pago.getMetodoPago().getNombre());
    dto.setEstadoPago(pago.getEstadoPago().getNombre());
    dto.setMonto(pago.getMonto());
    dto.setMoneda(pago.getMoneda());
    dto.setFechaPago(pago.getFechaPago().toString());
    return dto;
}

private void confirmarReservaYEnviarCorreos(Reserva reserva) {
        // 1. Cambiamos el estado de la reserva a confirmada
        reserva.setEstadoReserva(Reserva.EstadoReserva.confirmada);
        reservaRepository.save(reserva);

        // 2. Enviamos los correos
        try {
            Usuario cliente = reserva.getCliente();
            
            // OJO AQUÍ: Verifica si en tu clase Propiedad el dueño se obtiene con getPropietario(), getUsuario() o getArrendador()
            Usuario anfitrion = reserva.getHabitacion().getPropiedad().getDueno(); 

            // --- Correo para el Cliente ---
            String asuntoCliente = "¡Tu reserva en StayFlow está confirmada!";
            String mensajeCliente = "Hola " + cliente.getNombreCompleto() + ",\n\n" +
                    "Tu pago ha sido procesado con éxito y tu reserva está confirmada.\n\n" +
                    "Detalles de tu estancia:\n" +
                    "- Habitación: " + reserva.getHabitacion().getNumeroHabitacion() + "\n" +
                    "- Entrada: " + reserva.getFechaEntrada() + "\n" +
                    "- Salida: " + reserva.getFechaSalida() + "\n" +
                    "- Monto Total Pagado: $" + reserva.getMontoTotal() + " MXN\n\n" +
                    "¡Gracias por confiar en StayFlow!";
            emailService.enviarCorreo(cliente.getEmail(), asuntoCliente, mensajeCliente);

            // --- Correo para el Dueño/Anfitrión ---
            if (anfitrion != null) {
                String asuntoAnfitrion = "¡Nueva reserva confirmada en tu propiedad!";
                String mensajeAnfitrion = "Hola " + anfitrion.getNombreCompleto() + ",\n\n" +
                        "¡Excelentes noticias! Tienes una nueva reserva pagada y confirmada.\n\n" +
                        "Detalles:\n" +
                        "- Huésped: " + cliente.getNombreCompleto() + "\n" +
                        "- Habitación: " + reserva.getHabitacion().getNumeroHabitacion() + "\n" +
                        "- Entrada: " + reserva.getFechaEntrada() + "\n" +
                        "- Salida: " + reserva.getFechaSalida() + "\n\n" +
                        "¡Prepárate para recibir a tu huésped!";
                emailService.enviarCorreo(anfitrion.getEmail(), asuntoAnfitrion, mensajeAnfitrion);
            }
        } catch (Exception e) {
            // Si el correo falla, no detenemos la transacción del pago, solo registramos el error
            System.err.println("Error al enviar correos de confirmación: " + e.getMessage());
        }
    }

}