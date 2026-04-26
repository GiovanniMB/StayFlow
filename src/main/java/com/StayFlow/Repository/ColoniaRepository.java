package com.StayFlow.repository;

import com.StayFlow.model.Colonia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ColoniaRepository extends JpaRepository<Colonia, Integer> {
    @Query("SELECT c FROM Colonia c WHERE c.municipio.id = :idMunicipio")
    List<Colonia> buscarPorIdMunicipio(@Param("idMunicipio") Integer idMunicipio);
}