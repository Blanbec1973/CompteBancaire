package org.heynerr.model.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record AccountLineReadDTO(
        Long id,
        LocalDate date,
        String libelle,
        String natureCode,
        String natureLabel,   // pratique pour l’affichage immédiat côté front
        Long numCheque,
        BigDecimal montant,
        LocalDate pecBanque,
        Instant createdAt,
        Instant updatedAt
) {}
