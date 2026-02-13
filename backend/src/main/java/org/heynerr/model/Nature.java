package org.heynerr.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(
        name = "nature",
        uniqueConstraints = @UniqueConstraint(name = "uk_nature_code", columnNames = "code")
)
public class Nature {

    @Id
    @Column(length = 10, nullable = false, updatable = false)
    private String code; // Ex: "CHQ", "VIR", "CB", ...

    @Column(nullable = false, length = 100)
    private String label; // Ex: "Chèque", "Virement"

    @Column(nullable = false)
    private boolean requiresChequeNumber; // Ex: CHQ => true

    protected Nature() { /* for JPA */ }

    public Nature(String code, String label, boolean requiresChequeNumber) {
        this.code = code;
        this.label = label;
        this.requiresChequeNumber = requiresChequeNumber;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public boolean isRequiresChequeNumber() {
        return requiresChequeNumber;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setRequiresChequeNumber(boolean requiresChequeNumber) {
        this.requiresChequeNumber = requiresChequeNumber;
    }

    // Égalité basée sur l'identité métier (code)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Nature nature)) return false;
        return Objects.equals(code, nature.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
