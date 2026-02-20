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
public class SoldePecBanque {

    @Id
    @Column(name = "soldepecbanque")
    private BigDecimal soldePecBanque;

    public BigDecimal getSoldePecBanque() {
        return soldePecBanque;
    }
}
