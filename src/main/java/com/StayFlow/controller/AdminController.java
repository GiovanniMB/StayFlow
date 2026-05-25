package com.StayFlow.controller;

import com.StayFlow.service.CleanupService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final CleanupService cleanupService;

    public AdminController(CleanupService cleanupService) {
        this.cleanupService = cleanupService;
    }

    @PostMapping("/limpiar-tokens")
    @Operation(summary = "Limpiar tokens no válidos (solo para pruebas)")
    public ResponseEntity<String> limpiarTokens() {
        int eliminados = cleanupService.limpiarAhora();
        return ResponseEntity.ok("Se eliminaron " + eliminados + " tokens no válidos (expirados o desactivados)");
    }
}