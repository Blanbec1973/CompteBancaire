package org.heynerr.repository;

import org.heynerr.model.SoldePecBanque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface SoldePecBanqueRepository extends JpaRepository<SoldePecBanque, BigDecimal> {
}