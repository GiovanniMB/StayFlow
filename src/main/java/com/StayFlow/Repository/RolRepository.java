package com.StayFlow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.StayFlow.model.Rol;

import io.swagger.v3.oas.annotations.media.Schema;

public interface RolRepository extends JpaRepository<Rol, Integer> 
{
	@Schema(description = "Busca un rol por su nombre")
    Optional<Rol> findByNombreRol(String nombreRol);
	
	@Schema(description = "Verifica si ya existe un rol con el nombre proporcionado")
    boolean existsByNombreRol(String nombreRol);

    @Schema(description = "Busca roles activos (no eliminados lógicamente)")
    List<Rol> findByEstaEliminadoFalse();
    
    @Schema(description = "Obtiene todos los roles excepto los eliminados")
    @Query("SELECT r FROM Rol r WHERE r.estaEliminado = false")
    List<Rol> findRolesActivos();
    
    @Schema(description = "Eliminación lógica de un rol")
    @Modifying
    @Transactional
    @Query("UPDATE Rol r SET r.estaEliminado = true WHERE r.idRol = :idRol")
    int eliminarLogico(@Param("idRol") Integer idRol);
    
    @Query("SELECT r FROM Rol r WHERE LOWER(r.nombreRol) = LOWER(:nombreRol)")
    Optional<Rol> buscarPorNombreRolIgnoreCase(@Param("nombreRol") String nombreRol);
}