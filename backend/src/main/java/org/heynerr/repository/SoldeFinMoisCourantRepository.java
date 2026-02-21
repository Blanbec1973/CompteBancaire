package org.heynerr.repository;

import org.heynerr.model.SoldeFinMois;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface SoldeFinMoisCourantRepository extends JpaRepository<SoldeFinMois, BigDecimal> {
}