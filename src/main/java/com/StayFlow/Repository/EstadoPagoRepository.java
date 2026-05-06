package com.StayFlow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StayFlow.model.EstadoPago;

@Repository
public interface EstadoPagoRepository extends JpaRepository<EstadoPago, Integer> {
    Optional<EstadoPago> findByNombre(String nombre);
}