package com.StayFlow.Repository;

import com.StayFlow.model.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {
    
    @Query("SELECT m FROM Municipio m WHERE m.estado.id = :idEstado")
    List<Municipio> buscarPorIdEstado(@Param("idEstado") Integer idEstado);
}