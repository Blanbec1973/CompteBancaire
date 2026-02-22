package org.heynerr.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GenerationDTO(
        LocalDate date,
        String libelle,
        String natureCode,
        BigDecimal montant
) {}