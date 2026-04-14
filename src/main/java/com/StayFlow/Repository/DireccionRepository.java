package com.StayFlow.Repository;

import com.StayFlow.model.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Direccion.
 */
@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Integer> {
    // Para Dirección, con las operaciones CRUD por defecto por ahora.
}