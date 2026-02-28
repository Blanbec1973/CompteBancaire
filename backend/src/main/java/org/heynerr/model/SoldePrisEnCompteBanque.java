package org.heynerr.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Entity
@Immutable
@Table(name="v_soldepecbanque")
@SuppressWarnings("unused")
public class SoldePrisEnCompteBanque {

    @Id
    @Column(name = "soldepecbanque")
    private BigDecimal soldePecBanque;

    // ✅ Obligatoire pour JPA/Hibernate (public ou protected)
    public SoldePrisEnCompteBanque() {
    }

    // ✅ Pratique pour tes tests/fixtures
    public SoldePrisEnCompteBanque(BigDecimal soldePecBanque) {
        this.soldePecBanque = soldePecBanque;
    }

    public BigDecimal getSoldePecBanque() {
        return soldePecBanque;
    }

    public void setSoldePecBanque(BigDecimal soldePecBanque) {
        this.soldePecBanque = soldePecBanque;
    }
}
