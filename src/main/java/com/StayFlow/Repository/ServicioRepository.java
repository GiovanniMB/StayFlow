package com.StayFlow.Repository;

import com.StayFlow.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
// Repositorio para la entidad Servicio, que representa los servicios (amenities) que una propiedad puede ofrecer. Extiende JpaRepository para heredar métodos CRUD básicos y permite realizar consultas personalizadas si es necesario.
@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
}