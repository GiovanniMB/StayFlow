package com.StayFlow.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.StayFlow.model.Colonia;

@Repository
public interface ColoniaRepository extends JpaRepository<Colonia, Integer> {
    @Query("SELECT c FROM Colonia c WHERE c.municipio.id = :idMunicipio")
    List<Colonia> buscarPorIdMunicipio(@Param("idMunicipio") Integer idMunicipio);
}