package org.heynerr.service;


import org.heynerr.model.SoldeFinMois;
import org.heynerr.model.SoldePrisEnCompteBanque;
import org.heynerr.repository.SoldeFinMoisCourantRepository;
import org.heynerr.repository.SoldePecBanqueRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class SoldesService {

    private final SoldePecBanqueRepository repoPec;
    private final SoldeFinMoisCourantRepository repoFinMois;

    public SoldesService(SoldePecBanqueRepository repoPec, SoldeFinMoisCourantRepository repoFinMois) {
        this.repoPec = repoPec;
        this.repoFinMois = repoFinMois;
    }

    public BigDecimal getSoldePecBanque() {
        return repoPec.findAll()
                .stream()
                .findFirst()
                .map(SoldePrisEnCompteBanque::getSoldePecBanque)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal getSoldeFinMoisCourant() {
        return repoFinMois.findAll()
                .stream()
                .findFirst()
                .map(SoldeFinMois::getSoldeFinMoisCourant)
                .orElse(BigDecimal.ZERO);
    }

    public LocalDate getDateSoldeFinMois() {
        return repoFinMois.findAll()
                .stream()
                .findFirst()
                .map(SoldeFinMois::getDateSolde)
                .orElse(null);
    }
}