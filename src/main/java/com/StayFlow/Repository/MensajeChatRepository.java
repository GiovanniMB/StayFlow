package com.StayFlow.repository;

import com.StayFlow.model.MensajeChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeChatRepository extends JpaRepository<MensajeChat, Long> {
    
    // Obtiene el historial del chat ordenado cronológicamente para la UI
    List<MensajeChat> findByReserva_IdReservaOrderByFechaEnvioAsc(Integer idReserva);
    
}