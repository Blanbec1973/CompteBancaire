package org.heynerr.model.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountLineDTO {

    @NotNull
    private LocalDate date;

    @NotBlank
    private String libelle;

    @NotBlank
    private String natureCode; // ex: "CHQ", "VIR"

    private Long numCheque;

    @NotNull
    @DecimalMin(value = "0.00") // ou autoriser négatif si débit/crédit ?
    private BigDecimal montant;

    private LocalDate pecBanque;

    // Getters/Setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getNatureCode() { return natureCode; }
    public void setNatureCode(String natureCode) { this.natureCode = natureCode; }

    public Long getNumCheque() { return numCheque; }
    public void setNumCheque(Long numCheque) { this.numCheque = numCheque; }

    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }

    public LocalDate getPecBanque() { return pecBanque; }
    public void setPecBanque(LocalDate pecBanque) { this.pecBanque = pecBanque; }
}
