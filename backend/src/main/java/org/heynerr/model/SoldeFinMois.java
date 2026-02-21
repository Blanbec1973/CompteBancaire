package org.heynerr.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Immutable
@Table(name="v_soldefinmoiscourant")
public class SoldeFinMois {

    @Id
    @Column(name = "datesolde")
    private LocalDate dateSolde;

    @Column(name = "soldefinmoiscourant")
    private BigDecimal soldeFinMoisCourant;

    public BigDecimal getSoldeFinMoisCourant() {
        return soldeFinMoisCourant;
    }

    public LocalDate getDateSolde() {
        return dateSolde;
    }
}
