package com.StayFlow.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.StayFlow.model.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    
    Optional<Pago> findByReserva_IdReservaAndEstaEliminadoFalse(Integer idReserva);
    
    List<Pago> findByReserva_IdReserva(Integer idReserva);
    
    List<Pago> findByEstadoPago_IdEstadoPago(Integer idEstadoPago);
    
    List<Pago> findByReserva_Cliente_IdUsuario(Integer idUsuario);
    
    @Modifying
    @Transactional
    @Query("UPDATE Pago p SET p.estaEliminado = true WHERE p.idPago = :idPago")
    void softDelete(Integer idPago);
}