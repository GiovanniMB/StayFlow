package com.StayFlow.Repository;

import com.StayFlow.model.LogSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogSistemaRepository extends JpaRepository<LogSistema, Integer> {
    // Solo para escritura de bitácora, no pondre métodos de búsqueda por ahora.
}