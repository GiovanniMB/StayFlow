package com.StayFlow.controller;

import com.StayFlow.dto.request.PagoRequestDTO;
import com.StayFlow.dto.request.PagoTokenRequestDTO;
import com.StayFlow.dto.response.PagoResponseDTO;
import com.StayFlow.dto.response.PagoTokenResponseDTO;
import com.StayFlow.service.interfaces.IPagoService;
import com.StayFlow.service.interfaces.IPagoTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test/pagos")
public class TestPagoController {

    @Autowired
    private IPagoService pagoService;
    
    @Autowired
    private IPagoTokenService pagoTokenService;

    @PostMapping("/tokens")
    public ResponseEntity<Map<String, Object>> guardarToken(
            @RequestBody PagoTokenRequestDTO request,
            @RequestParam(required = true) Integer idUsuario) {  // 🔥 CAMBIADO: required = true, sin defaultValue
        
        PagoTokenResponseDTO response = pagoTokenService.guardarToken(request, idUsuario);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Método de pago guardado para usuario ID: " + idUsuario);
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/tokens")
    public ResponseEntity<Map<String, Object>> listarTokens(
            @RequestParam(required = true) Integer idUsuario) {  // 🔥 CAMBIADO: required = true
        
        List<PagoTokenResponseDTO> tokens = pagoTokenService.listarTokensUsuario(idUsuario);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Tokens del usuario ID: " + idUsuario);
        result.put("data", tokens);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/tokens/{idToken}")
    public ResponseEntity<Map<String, Object>> eliminarToken(
            @PathVariable Integer idToken,
            @RequestParam(required = true) Integer idUsuario) {  // 🔥 CAMBIADO: required = true
        
        pagoTokenService.eliminarToken(idToken, idUsuario);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Token eliminado para usuario ID: " + idUsuario);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/procesar")
    public ResponseEntity<Map<String, Object>> procesarPago(
            @RequestBody PagoRequestDTO request,
            @RequestParam(required = true) Integer idUsuario) {  // 🔥 CAMBIADO: required = true
        
        PagoResponseDTO response = pagoService.procesarPago(request, idUsuario);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Pago procesado para usuario ID: " + idUsuario);
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{idPago}/estado")
    public ResponseEntity<Map<String, Object>> obtenerEstado(
            @PathVariable Integer idPago,
            @RequestParam(required = true) Integer idUsuario) {  // 🔥 CAMBIADO: required = true
        
        PagoResponseDTO response = pagoService.obtenerEstadoPago(idPago, idUsuario);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/mis-pagos")
    public ResponseEntity<Map<String, Object>> misPagos(
            @RequestParam(required = true) Integer idUsuario) {  // 🔥 CAMBIADO: required = true
        
        List<PagoResponseDTO> pagos = pagoService.obtenerPagosPorUsuario(idUsuario);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Pagos del usuario ID: " + idUsuario);
        result.put("data", pagos);
        return ResponseEntity.ok(result);
    }
}