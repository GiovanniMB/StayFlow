package com.StayFlow.Repository;

import com.StayFlow.model.Colonia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColoniaRepository extends JpaRepository<Colonia, Integer> {
}