package com.StayFlow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.StayFlow.model.PagoToken;

@Repository
public interface PagoTokenRepository extends JpaRepository<PagoToken, Integer> {
    
    List<PagoToken> findByUsuario_IdUsuarioAndActivoTrue(Integer idUsuario);
    
    Optional<PagoToken> findByIdPagoTokenAndUsuario_IdUsuarioAndActivoTrue(Integer idToken, Integer idUsuario);
    
    @Modifying
    @Transactional
    @Query("UPDATE PagoToken pt SET pt.activo = false WHERE pt.idPagoToken = :idToken")
    void desactivarToken(Integer idToken);
    
    boolean existsByUsuario_IdUsuarioAndTokenGatewayAndActivoTrue(Integer idUsuario, String tokenGateway);
}