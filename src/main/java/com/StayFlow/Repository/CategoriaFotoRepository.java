package com.StayFlow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StayFlow.model.CategoriaFoto;

@Repository
public interface CategoriaFotoRepository extends JpaRepository<CategoriaFoto, Integer> {
    // Buscar categorías por tipo macro (interior/exterior) que no estén eliminadas
    List<CategoriaFoto> findByTipoMacroAndEstaEliminadoFalse(CategoriaFoto.TipoMacro tipoMacro);
}