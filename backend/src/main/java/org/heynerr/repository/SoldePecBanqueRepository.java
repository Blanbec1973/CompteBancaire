package org.heynerr.repository;

import org.heynerr.model.SoldePrisEnCompteBanque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface SoldePecBanqueRepository extends JpaRepository<SoldePrisEnCompteBanque, BigDecimal> {
}