package org.heynerr.repository;

import org.heynerr.model.AccountLine;
import org.heynerr.model.Nature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AccountLineRepository extends JpaRepository<AccountLine, Long> {

    @Query("""
           select a
           from AccountLine a
           join fetch a.nature n
           order by a.date desc, a.id desc
           """)
    List<AccountLine> findAllWithNature();



    // Search : libellé contient (case-insensitive) OU nature.code = q (exact, pratique pour VIR/CHQ)
    @Query("""
           select a
           from AccountLine a
           where lower(a.libelle) like lower(concat('%', :q, '%'))
              or a.nature.code = :q
           order by a.date desc, a.id desc
           """)
    List<AccountLine> searchLibelleContainsOrNature(String q);

    List<AccountLine> findByPecBanqueOrderByDateDesc(LocalDate pecBanque);

    @Query("""
    SELECT a FROM AccountLine a
    WHERE a.pecBanque IS NULL
      AND a.date <= :limit
    ORDER BY a.date ASC
    """)
    List<AccountLine> findNonPointedUntil(LocalDate limit);

    List<AccountLine> findByNatureOrderByNumChequeDesc(Nature nature);



}
