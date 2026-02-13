package org.heynerr.repository;

import org.heynerr.model.AccountLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountLineRepository extends JpaRepository<AccountLine, Long> {
}
