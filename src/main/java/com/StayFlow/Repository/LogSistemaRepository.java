package com.StayFlow.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.StayFlow.model.LogSistema;

@Repository
public interface LogSistemaRepository extends JpaRepository<LogSistema, Integer> {

    @Query("SELECT l FROM LogSistema l ORDER BY l.fechaEvento DESC")
    List<LogSistema> findTopNOrderByFechaEventoDesc(Pageable pageable);
    
    @Query("SELECT l FROM LogSistema l WHERE l.accion = :accion ORDER BY l.fechaEvento DESC")
    List<LogSistema> findTopNByAccionOrderByFechaEventoDesc(@Param("accion") LogSistema.Accion accion, Pageable pageable);
    
    default List<LogSistema> findTopNOrderByFechaEventoDesc(int limit) {
        return findTopNOrderByFechaEventoDesc(PageRequest.of(0, limit));
    }
    
    default List<LogSistema> findTopNByAccionOrderByFechaEventoDesc(LogSistema.Accion accion, int limit) {
        return findTopNByAccionOrderByFechaEventoDesc(accion, PageRequest.of(0, limit));
    }
}