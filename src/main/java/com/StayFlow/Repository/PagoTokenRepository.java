package com.StayFlow.Repository;

import com.StayFlow.model.PagoToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoTokenRepository extends JpaRepository<PagoToken, Integer> {
    
    List<PagoToken> findByUsuario_IdUsuarioAndActivoTrue(Integer idUsuario);
    
    Optional<PagoToken> findByIdPagoTokenAndUsuario_IdUsuarioAndActivoTrue(Integer idToken, Integer idUsuario);
    
    @Query("SELECT CASE WHEN COUNT(pt) > 0 THEN true ELSE false END FROM PagoToken pt WHERE pt.usuario.idUsuario = :idUsuario AND pt.tokenGateway = :tokenGateway AND pt.activo = true")
    boolean existsByUsuario_IdUsuarioAndTokenGatewayAndActivoTrue(@Param("idUsuario") Integer idUsuario, @Param("tokenGateway") String tokenGateway);
}