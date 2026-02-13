package org.heynerr.repository;

import org.heynerr.model.Nature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NatureRepository extends JpaRepository<Nature, String> {
    // code = "CHQ", "VIR", ...
}
