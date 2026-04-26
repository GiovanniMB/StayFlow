package com.StayFlow.repository;

import com.StayFlow.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
    // Hereda findAll() automáticamente para obtener todos los estados
}