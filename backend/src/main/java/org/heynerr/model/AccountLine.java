package org.heynerr.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "account_line",
       indexes = {
            @Index(name = "idx_accountline_nature", columnList = "nature_code")
        })
public class AccountLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private String libelle;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "nature_code",
            referencedColumnName = "code",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_accountline_nature")
    )
    private Nature nature;

    private Long numCheque;
    @Column(nullable = false)
    private BigDecimal montant;
    private LocalDate pecBanque;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;

    public AccountLine() {
    }

    public AccountLine(LocalDate date, String libelle, Nature nature, Long numCheque,
                       BigDecimal montant, LocalDate pecBanque) {
        this.date = date;
        this.libelle = libelle;
        this.nature = nature;
        this.numCheque = numCheque;
        this.montant = montant;
        this.pecBanque = pecBanque;
    }


    // --- Validation métier trans-champ ---
    @AssertTrue(message = "Un numéro de chèque est requis pour la nature 'Chèque'.")
    public boolean isChequeNumberValidWhenRequired() {
        if (nature == null) return true; // @NotNull gère le null séparément
        if (nature.isRequiresChequeNumber()) {
            return numCheque != null;
        }
        return true;
    }


    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Nature getNature() {
        return nature;
    }

    public void setNature(Nature nature) {
        this.nature = nature;
    }

    public Long getNumCheque() {
        return numCheque;
    }

    public void setNumCheque(Long numCheque) {
        this.numCheque = numCheque;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDate getPecBanque() {
        return pecBanque;
    }

    public void setPecBanque(LocalDate pecBanque) {
        this.pecBanque = pecBanque;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}
