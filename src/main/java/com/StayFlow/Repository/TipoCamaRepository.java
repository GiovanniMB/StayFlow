package com.StayFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StayFlow.model.TipoCama;

@Repository
public interface TipoCamaRepository extends JpaRepository<TipoCama, Integer> {
    // Como es un catálogo, las operaciones CRUD por defecto son suficientes.
}