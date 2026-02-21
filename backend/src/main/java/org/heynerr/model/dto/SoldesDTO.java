package org.heynerr.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SoldesDTO {

    private final BigDecimal soldePecBanque;
    private final BigDecimal soldeFinMois;
    private final LocalDate dateCalcul;

    public SoldesDTO(BigDecimal soldePecBanque, BigDecimal soldeFinMois, LocalDate dateCalcul) {
        this.soldePecBanque = soldePecBanque;
        this.soldeFinMois = soldeFinMois;
        this.dateCalcul = dateCalcul;
    }

    public BigDecimal getSoldePecBanque() {
        return soldePecBanque;
    }

    public BigDecimal getSoldeFinMois() {
        return soldeFinMois;
    }

    public LocalDate getDateCalcul() {
        return dateCalcul;
    }
}