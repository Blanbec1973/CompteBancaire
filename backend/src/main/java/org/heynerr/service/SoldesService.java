package org.heynerr.service;


import org.heynerr.exception.TechnicalException;
import org.heynerr.model.SoldeFinMois;
import org.heynerr.model.SoldePrisEnCompteBanque;
import org.heynerr.repository.SoldeFinMoisCourantRepository;
import org.heynerr.repository.SoldePecBanqueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class SoldesService {
    private static final Logger log = LoggerFactory.getLogger(SoldesService.class);

    private final SoldePecBanqueRepository repoPec;
    private final SoldeFinMoisCourantRepository repoFinMois;

    public SoldesService(SoldePecBanqueRepository repoPec, SoldeFinMoisCourantRepository repoFinMois) {
        this.repoPec = repoPec;
        this.repoFinMois = repoFinMois;
    }

    public BigDecimal getSoldePecBanque() {
        log.debug("ENTRY getSoldePecBanque");
        long startTime = System.currentTimeMillis();
        
        try {
            BigDecimal result = repoPec.findAll()
                    .stream()
                    .findFirst()
                    .map(SoldePrisEnCompteBanque::getSoldePecBanque)
                    .orElse(BigDecimal.ZERO);
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("EXIT getSoldePecBanque: solde={}, duration={}ms", result, duration);
            
            if (result.equals(BigDecimal.ZERO)) {
                log.warn("ALERT getSoldePecBanque returned ZERO - check if DB has data");
            }
            
            return result;
        } catch (Exception ex) {
            throw new TechnicalException("ERROR getSoldePecBanque", ex);
        }
    }

    public BigDecimal getSoldeFinMoisCourant() {
        log.debug("ENTRY getSoldeFinMoisCourant");
        BigDecimal result = repoFinMois.findAll()
                .stream()
                .findFirst()
                .map(SoldeFinMois::getSoldeFinMoisCourant)
                .orElse(BigDecimal.ZERO);
        log.info("EXIT getSoldeFinMoisCourant: solde={}", result);
        return result;
    }

    public LocalDate getDateSoldeFinMois() {
        log.debug("ENTRY getDateSoldeFinMois");
        LocalDate result = repoFinMois.findAll()
                .stream()
                .findFirst()
                .map(SoldeFinMois::getDateSolde)
                .orElse(null);
        log.info("EXIT getDateSoldeFinMois: date={}", result);
        return result;
    }
}