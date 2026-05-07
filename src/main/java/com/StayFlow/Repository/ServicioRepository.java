package com.StayFlow.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StayFlow.model.Servicio;
// Repositorio para la entidad Servicio, que representa los servicios (amenities) que una propiedad puede ofrecer. Extiende JpaRepository para heredar métodos CRUD básicos y permite realizar consultas personalizadas si es necesario.
@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
    // Busca un servicio exactamente por nombre sin importar mayúsculas/minúsculas
    Optional<Servicio> findByNombreServicioIgnoreCase(String nombreServicio);
}