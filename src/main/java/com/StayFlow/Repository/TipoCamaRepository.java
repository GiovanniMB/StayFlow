package com.StayFlow.repository;

import com.StayFlow.model.TipoCama;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoCamaRepository extends JpaRepository<TipoCama, Integer> {
    // Como es un catálogo, las operaciones CRUD por defecto son suficientes.
}