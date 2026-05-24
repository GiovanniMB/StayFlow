package com.StayFlow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StayFlow.model.PagoInfo;

@Repository
public interface PagoInfoRepository extends JpaRepository<PagoInfo, Integer> {
    List<PagoInfo> findByPago_IdPago(Integer idPago);
    void deleteByPago_IdPago(Integer idPago);
}