package com.StayFlow.controller;

import com.StayFlow.dto.request.PagoRequestDTO;
import com.StayFlow.dto.request.PagoTokenRequestDTO;
import com.StayFlow.dto.response.PagoResponseDTO;
import com.StayFlow.dto.response.PagoTokenResponseDTO;
import com.StayFlow.exception.PaymentException;
import com.StayFlow.model.Usuario;
import com.StayFlow.repository.UsuarioRepository;
import com.StayFlow.service.interfaces.IPagoService;
import com.StayFlow.service.interfaces.IPagoTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@Tag(name = "Pagos", description = "Endpoints para procesar pagos y gestionar métodos de pago")
public class PagoController {

    @Autowired
    private IPagoService pagoService;
    
    @Autowired
    private IPagoTokenService pagoTokenService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;  

    private Integer extractUserId(UserDetails userDetails) {
        String email = userDetails.getUsername();
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new PaymentException("Usuario no encontrado con email: " + email));
        return usuario.getIdUsuario();
    }

    @PostMapping("/procesar")
    @Operation(summary = "Procesar un pago para una reserva")
    public ResponseEntity<Map<String, Object>> procesarPago(
            @Valid @RequestBody PagoRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Integer idUsuario = extractUserId(userDetails);
        PagoResponseDTO response = pagoService.procesarPago(request, idUsuario);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Pago procesado correctamente");
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{idPago}/estado")
    @Operation(summary = "Consultar estado de un pago")
    public ResponseEntity<Map<String, Object>> obtenerEstadoPago(
            @PathVariable Integer idPago,
            @AuthenticationPrincipal UserDetails userDetails) {
        Integer idUsuario = extractUserId(userDetails);
        PagoResponseDTO response = pagoService.obtenerEstadoPago(idPago, idUsuario);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", response);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/mis-pagos")
    @Operation(summary = "Obtener todos los pagos del usuario autenticado")
    public ResponseEntity<Map<String, Object>> obtenerMisPagos(
            @AuthenticationPrincipal UserDetails userDetails) {
        Integer idUsuario = extractUserId(userDetails);
        List<PagoResponseDTO> pagos = pagoService.obtenerPagosPorUsuario(idUsuario);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", pagos);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/tokens")
    @Operation(summary = "Guardar método de pago para futuros pagos")
    public ResponseEntity<Map<String, Object>> guardarTokenPago(
            @Valid @RequestBody PagoTokenRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Integer idUsuario = extractUserId(userDetails);
        PagoTokenResponseDTO response = pagoTokenService.guardarToken(request, idUsuario);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Método de pago guardado correctamente");
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/tokens")
    @Operation(summary = "Listar métodos de pago guardados del usuario")
    public ResponseEntity<Map<String, Object>> listarTokensUsuario(
            @AuthenticationPrincipal UserDetails userDetails) {
        Integer idUsuario = extractUserId(userDetails);
        List<PagoTokenResponseDTO> tokens = pagoTokenService.listarTokensUsuario(idUsuario);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", tokens);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/tokens/{idToken}")
    @Operation(summary = "Eliminar un método de pago guardado")
    public ResponseEntity<Map<String, Object>> eliminarTokenPago(
            @PathVariable Integer idToken,
            @AuthenticationPrincipal UserDetails userDetails) {
        Integer idUsuario = extractUserId(userDetails);
        pagoTokenService.eliminarToken(idToken, idUsuario);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Método de pago eliminado correctamente");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/webhook/{gateway}")
    @Operation(summary = "Webhook para recibir confirmaciones de pasarelas externas")
    public ResponseEntity<Map<String, Object>> webhookPagos(
            @PathVariable String gateway,
            @RequestBody Object payload) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Webhook recibido para " + gateway);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/admin/transferencias/{idPago}/confirmar")
@Operation(summary = "Confirmar pago por transferencia (solo administradores)")
public ResponseEntity<Map<String, Object>> confirmarTransferencia(
        @PathVariable Integer idPago) {
    
    PagoResponseDTO response = pagoService.confirmarTransferencia(idPago);
    
    Map<String, Object> result = new HashMap<>();
    result.put("success", true);
    result.put("message", "Transferencia confirmada exitosamente");
    result.put("data", response);
    return ResponseEntity.ok(result);
}

@PutMapping("/admin/pagos/{idPago}/fallido")
@Operation(summary = "Marcar pago como fallido (solo administradores)")
public ResponseEntity<Map<String, Object>> marcarComoFallido(
        @PathVariable Integer idPago,
        @RequestParam String motivo) {
    
    PagoResponseDTO response = pagoService.marcarComoFallido(idPago, motivo);
    
    Map<String, Object> result = new HashMap<>();
    result.put("success", true);
    result.put("message", "Pago marcado como fallido");
    result.put("data", response);
    return ResponseEntity.ok(result);
}

@PostMapping("/admin/pagos/{idPago}/reembolsar")
@Operation(summary = "Reembolsar un pago (solo administradores)")
public ResponseEntity<Map<String, Object>> reembolsarPago(
        @PathVariable Integer idPago,
        @RequestParam String motivo) {
    
    PagoResponseDTO response = pagoService.reembolsarPago(idPago, motivo);
    
    Map<String, Object> result = new HashMap<>();
    result.put("success", true);
    result.put("message", "Pago reembolsado exitosamente");
    result.put("data", response);
    return ResponseEntity.ok(result);
}

@PutMapping("/admin/pagos/{idPago}/revision")
@Operation(summary = "Marcar pago en revisión (solo administradores)")
public ResponseEntity<Map<String, Object>> marcarEnRevision(
        @PathVariable Integer idPago,
        @RequestParam String motivo) {
    
    PagoResponseDTO response = pagoService.marcarEnRevision(idPago, motivo);
    
    Map<String, Object> result = new HashMap<>();
    result.put("success", true);
    result.put("message", "Pago marcado en revisión");
    result.put("data", response);
    return ResponseEntity.ok(result);
}
}