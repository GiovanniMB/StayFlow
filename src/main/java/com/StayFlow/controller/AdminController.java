package com.StayFlow.controller;

import com.StayFlow.dto.request.AdminBloqueoRequestDTO;
import com.StayFlow.dto.response.*;
import com.StayFlow.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ========== USUARIOS ==========
    
    @GetMapping("/usuarios")
    @Operation(summary = "Obtener todos los usuarios")
    public ResponseEntity<List<UsuarioAdminResponseDTO>> obtenerTodosUsuarios() {
        return ResponseEntity.ok(adminService.obtenerTodosUsuarios());
    }

    @GetMapping("/usuarios/{id}")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<UsuarioAdminResponseDTO> obtenerUsuarioPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(adminService.obtenerUsuarioPorId(id));
    }

    @PutMapping("/usuarios/{id}/rol")
    @Operation(summary = "Cambiar rol de usuario (arrendador/arrendatario)")
    public ResponseEntity<UsuarioAdminResponseDTO> cambiarRolUsuario(@PathVariable Integer id, @RequestParam String rol) {
        return ResponseEntity.ok(adminService.cambiarRolUsuario(id, rol));
    }

    @PostMapping("/usuarios/{id}/bloquear")
    @Operation(summary = "Bloquear usuario")
    public ResponseEntity<Void> bloquearUsuario(@PathVariable Integer id) {
        adminService.bloquearUsuario(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/usuarios/{id}/desbloquear")
    @Operation(summary = "Desbloquear usuario")
    public ResponseEntity<Void> desbloquearUsuario(@PathVariable Integer id) {
        adminService.desbloquearUsuario(id);
        return ResponseEntity.ok().build();
    }

    // ========== PROPIEDADES ==========
    
    @GetMapping("/propiedades")
    @Operation(summary = "Obtener todas las propiedades")
    public ResponseEntity<List<PropiedadAdminResponseDTO>> obtenerTodasPropiedades() {
        return ResponseEntity.ok(adminService.obtenerTodasPropiedades());
    }

    @GetMapping("/propiedades/{id}")
    @Operation(summary = "Obtener propiedad por ID")
    public ResponseEntity<PropiedadAdminResponseDTO> obtenerPropiedadPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(adminService.obtenerPropiedadPorId(id));
    }

    @DeleteMapping("/propiedades/{id}")
    @Operation(summary = "Eliminar propiedad (soft delete)")
    public ResponseEntity<Void> eliminarPropiedad(@PathVariable Integer id) {
        adminService.eliminarPropiedad(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/propiedades/{id}/aprobar")
    @Operation(summary = "Aprobar propiedad (cambiar estado a publicada)")
    public ResponseEntity<Void> aprobarPropiedad(@PathVariable Integer id) {
        adminService.aprobarPropiedad(id);
        return ResponseEntity.ok().build();
    }

    // ========== RESERVAS ==========
    
    @GetMapping("/reservas")
    @Operation(summary = "Obtener todas las reservas")
    public ResponseEntity<List<ReservaAdminResponseDTO>> obtenerTodasReservas() {
        return ResponseEntity.ok(adminService.obtenerTodasReservas());
    }

    @GetMapping("/reservas/propiedad/{idPropiedad}")
    @Operation(summary = "Obtener reservas por propiedad")
    public ResponseEntity<List<ReservaAdminResponseDTO>> obtenerReservasPorPropiedad(@PathVariable Integer idPropiedad) {
        return ResponseEntity.ok(adminService.obtenerReservasPorPropiedad(idPropiedad));
    }

    @PostMapping("/reservas/{id}/cancelar")
    @Operation(summary = "Cancelar reserva (admin)")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Integer id) {
        adminService.cancelarReservaAdmin(id);
        return ResponseEntity.ok().build();
    }

    // ========== BLOQUEOS ==========
    
    @GetMapping("/bloqueos")
    @Operation(summary = "Obtener todos los bloqueos")
    public ResponseEntity<List<BloqueoAdminResponseDTO>> obtenerTodosBloqueos() {
        return ResponseEntity.ok(adminService.obtenerTodosBloqueos());
    }

    @PostMapping("/bloqueos")
    @Operation(summary = "Crear bloqueo")
    public ResponseEntity<Void> crearBloqueo(@RequestBody AdminBloqueoRequestDTO request) {
        adminService.crearBloqueo(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/bloqueos/{id}")
    @Operation(summary = "Eliminar bloqueo")
    public ResponseEntity<Void> eliminarBloqueo(@PathVariable Integer id) {
        adminService.eliminarBloqueo(id);
        return ResponseEntity.ok().build();
    }

    // ========== ESTADÍSTICAS ==========
    
    @GetMapping("/estadisticas")
    @Operation(summary = "Obtener estadísticas generales")
    public ResponseEntity<EstadisticasAdminResponseDTO> obtenerEstadisticas() {
        return ResponseEntity.ok(adminService.obtenerEstadisticas());
    }
}