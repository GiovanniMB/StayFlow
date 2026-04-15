package com.StayFlow.Service.Impl;

import com.StayFlow.Repository.LogSistemaRepository;
import com.StayFlow.Repository.UsuarioRepository;
import com.StayFlow.Service.Interfaces.ILogSistemaService;
import com.StayFlow.model.LogSistema;
import com.StayFlow.model.LogSistema.Accion;
import com.StayFlow.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
// Servicio para registrar logs de auditoría en el sistema. Se encarga de guardar cada acción relevante (INSERT, UPDATE, DELETE_LOGICO) con información del usuario que la realizó, la tabla afectada y el ID del registro modificado.
@Service
public class LogSistemaServiceImpl implements ILogSistemaService {

    private final LogSistemaRepository logSistemaRepository;
    private final UsuarioRepository usuarioRepository;

    public LogSistemaServiceImpl(LogSistemaRepository logSistemaRepository, UsuarioRepository usuarioRepository) {
        this.logSistemaRepository = logSistemaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void registrarLog(String tablaAfectada, Integer idRegistroAfectado, Accion accion) {
        try {
            // 1. Obtener la autenticación actual del SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuarioAccion = null;

            // 2. Extraer el email si el usuario está autenticado
            if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
                String email = authentication.getName();
                Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
                if (usuarioOpt.isPresent()) {
                    usuarioAccion = usuarioOpt.get();
                }
            }

            // 3. Crear y armar la entidad del Log (la fecha se pone sola en el modelo)
            LogSistema log = new LogSistema();
            log.setTablaAfectada(tablaAfectada);
            log.setIdRegistroAfectado(idRegistroAfectado);
            log.setAccion(accion);
            log.setUsuarioAccion(usuarioAccion); 

            // 4. Guardar en la base de datos
            logSistemaRepository.save(log);

        } catch (Exception e) {
            // Un error en el log no debe detener la transacción principal de negocio.
            // Se imprimime en consola para depuración técnica.
            System.err.println("Error crítico al intentar guardar log de auditoría: " + e.getMessage());
        }
    }
}