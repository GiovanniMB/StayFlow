package com.StayFlow.repository;

import com.StayFlow.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Integer> {
    
    // Regla de negocio: Obtener todas las reseñas de una propiedad específica
    List<Resena> findByReserva_Habitacion_Propiedad_IdPropiedadOrderByIdResenaDesc(Integer idPropiedad);
    
    // Regla de negocio: Verificar si ya existe una reseña para una reserva
    boolean existsByReserva_IdReserva(Integer idReserva);
}