package com.StayFlow.service;

import com.StayFlow.repository.RefreshTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CleanupService {

    private static final Logger log = LoggerFactory.getLogger(CleanupService.class);
    private final RefreshTokenRepository refreshTokenRepository;

    public CleanupService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void limpiarTokensNoValidos() {
        LocalDateTime ahora = LocalDateTime.now();
        
        int eliminados = refreshTokenRepository.limpiarTokensNoValidos(ahora);
        
        if (eliminados > 0) {
            log.info("Limpieza completada: {} tokens no válidos eliminados", eliminados);
        } else {
            log.debug("Limpieza: No hay tokens no válidos para eliminar");
        }
    }

    @Transactional
    public int limpiarAhora() {
        LocalDateTime ahora = LocalDateTime.now();
        int eliminados = refreshTokenRepository.limpiarTokensNoValidos(ahora);
        log.info(" Limpieza manual: {} tokens no válidos eliminados", eliminados);
        return eliminados;
    }
}