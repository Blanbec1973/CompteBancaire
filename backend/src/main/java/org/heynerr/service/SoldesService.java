package org.heynerr.service;


import org.heynerr.model.SoldePecBanque;
import org.heynerr.repository.SoldePecBanqueRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SoldesService {

    private final SoldePecBanqueRepository repo;

    public SoldesService(SoldePecBanqueRepository repo) {
        this.repo = repo;
    }

    public BigDecimal getSoldePecBanque() {
        return repo.findAll()
                .stream()
                .findFirst()
                .map(SoldePecBanque::getSoldePecBanque)
                .orElse(BigDecimal.ZERO);
    }
}