package com.StayFlow.repository;

import com.StayFlow.model.Propiedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Propiedad.
 * Maneja las operaciones de base de datos heredando de JpaRepository.
 */
@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Integer> {

    // Devuelve todas las propiedades que NO han sido eliminadas lógicamente
    List<Propiedad> findByEstaEliminadoFalse();

    // Busca una propiedad por su ID, asegurándose de que no esté eliminada
    Optional<Propiedad> findByIdPropiedadAndEstaEliminadoFalse(Integer idPropiedad);
    
    // (Opcional por ahora) buscar propiedades por dueño
    // List<Propiedad> findByDuenoIdUsuarioAndEstaEliminadoFalse(Integer idDueno);
}