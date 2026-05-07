package com.StayFlow.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StayFlow.model.LogSistema;

import io.swagger.v3.oas.annotations.media.Schema;

@Repository
@Schema(description = "Repositorio para la bitácora de auditoría")
public interface LogSistemaRepository extends JpaRepository<LogSistema, Integer> {
}