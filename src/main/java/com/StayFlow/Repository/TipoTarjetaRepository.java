package com.StayFlow.repository;

import com.StayFlow.model.TipoTarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TipoTarjetaRepository extends JpaRepository<TipoTarjeta, Integer> {
    Optional<TipoTarjeta> findByCodigo(String codigo);
    Optional<TipoTarjeta> findByNombre(String nombre);
}