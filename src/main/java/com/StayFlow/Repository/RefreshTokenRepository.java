package com.StayFlow.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.StayFlow.model.RefreshToken;
import com.StayFlow.model.Usuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Repository
@Schema(description = "Repositorio para operaciones de refresh tokens")
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Schema(description = "Busca un refresh token por su valor")
    Optional<RefreshToken> findByToken(String token);

    @Schema(description = "Busca un refresh token activo por usuario")
    Optional<RefreshToken> findByUsuarioAndActivoTrue(Usuario usuario);

    @Schema(description = "Elimina todos los refresh tokens expirados")
    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken rt WHERE rt.fechaExpiracion < :now")
    int deleteAllExpiredTokens(@org.springframework.data.repository.query.Param("now") LocalDateTime now);

    @Schema(description = "Desactiva todos los refresh tokens de un usuario")
    @Modifying
    @Transactional
    @Query("UPDATE RefreshToken rt SET rt.activo = false WHERE rt.usuario = :usuario")
    int desactivarTokensPorUsuario(Usuario usuario);
}