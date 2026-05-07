package com.StayFlow.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StayFlow.model.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
    // Hereda findAll() automáticamente para obtener todos los estados
}