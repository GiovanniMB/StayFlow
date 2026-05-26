package com.StayFlow.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
 

    @Query("SELECT MONTH(p.fechaPago), SUM(p.monto) " +
           "FROM Pago p " +
           "WHERE YEAR(p.fechaPago) = :anio " +
           "AND p.estadoPago.nombre = 'completado' " +
           "GROUP BY MONTH(p.fechaPago) " +
           "ORDER BY MONTH(p.fechaPago)")
    List<Object[]> findIngresosAgrupadosPorMes(@Param("anio") int anio);

    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.estadoPago.nombre = 'completado'")
    BigDecimal sumMontoByEstadoCompletado();

    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.estadoPago.nombre = 'completado' " +
           "AND MONTH(p.fechaPago) = MONTH(CURRENT_DATE) " +
           "AND YEAR(p.fechaPago) = YEAR(CURRENT_DATE)")
    BigDecimal sumMontoByEstadoCompletadoDelMes();
}