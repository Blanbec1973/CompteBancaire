package org.heynerr.service;

import org.heynerr.model.AccountLine;
import org.heynerr.model.Nature;
import org.heynerr.model.dto.AccountLineDTO;
import org.heynerr.model.dto.AccountLineReadDTO;
import org.heynerr.model.dto.GenerationDTO;
import org.heynerr.repository.AccountLineRepository;
import org.heynerr.repository.NatureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class AccountLineService {

    private final AccountLineRepository accountLineRepository;
    private final NatureRepository natureRepository;

    public AccountLineService(AccountLineRepository accountLineRepository, NatureRepository natureRepository) {
        this.accountLineRepository = accountLineRepository;
        this.natureRepository = natureRepository;
    }

    @Transactional
    public AccountLine createFromDto(AccountLineDTO dto) {
        Nature nature = natureRepository.findById(dto.getNatureCode())
                .orElseThrow(() -> new IllegalArgumentException("Nature inconnue: " + dto.getNatureCode()));

        AccountLine entity = new AccountLine(
                dto.getDate(),
                dto.getLibelle(),
                nature,
                dto.getNumCheque(),
                dto.getMontant(),
                dto.getPecBanque()
        );

        // La validation Bean Validation sur l'entité (ex. @AssertTrue) sera rejouée
        // par Hibernate Validator à la persistance (pre-persist / pre-update).
        return accountLineRepository.save(entity);
    }


    @Transactional(readOnly = true)
    public List<AccountLineReadDTO> findAll() {
        return accountLineRepository.findAllWithNature()
                .stream()
                .map(this::toReadDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AccountLine> search(String q) {
        // On cherche : libellé contient q (case-insensitive) OU nature.code = q (exact)
        return accountLineRepository.searchLibelleContainsOrNature(q);
    }

    @Transactional
    public AccountLineReadDTO updateFromDto(Long id, AccountLineDTO dto) {
        AccountLine entity = accountLineRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "AccountLine introuvable: " + id));

        Nature nature = natureRepository.findById(dto.getNatureCode())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Nature inconnue: " + dto.getNatureCode()));

        entity.setDate(dto.getDate());
        entity.setLibelle(dto.getLibelle());
        entity.setNature(nature);
        entity.setNumCheque(dto.getNumCheque());
        entity.setMontant(dto.getMontant());
        entity.setPecBanque(dto.getPecBanque());

        return toReadDto(accountLineRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<AccountLineReadDTO> findNonPointed() {
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusMonths(1).withDayOfMonth(15);

        return accountLineRepository.findNonPointedUntil(limit)
                .stream()
                .map(this::toReadDto)
                .toList();
    }

    public List<AccountLineReadDTO> findAllPointedAtDate(LocalDate datePointed) {
        return accountLineRepository.findByPecBanqueOrderByIdDesc(datePointed)
                .stream()
                .map(this::toReadDto)
                .toList();
    }

    @Transactional
    public AccountLineReadDTO pointer(Long id, LocalDate datePointage) {
        AccountLine al = accountLineRepository.findById(id)
                .orElseThrow();

        al.setPecBanque(datePointage);

        return toReadDto(accountLineRepository.save(al));
    }


    @Transactional
    public List<AccountLineReadDTO> generateAnnual(GenerationDTO dto) {

        LocalDate date = dto.date();
        int year = date.getYear();

        List<AccountLineReadDTO> results = new ArrayList<>();

        while (date.getYear() == year) {

            AccountLine al = new AccountLine();
            al.setDate(date);
            al.setLibelle(dto.libelle());
            al.setMontant(dto.montant());
            al.setNature(natureRepository.getReferenceById(dto.natureCode()));
            al.setNumCheque(null);
            al.setPecBanque(null);

            AccountLine saved = accountLineRepository.save(al);
            results.add(toReadDto(saved));

            date = date.plusMonths(1);
        }

        return results;
    }

    private AccountLineReadDTO toReadDto(AccountLine a) {
        return new AccountLineReadDTO(
                a.getId(),
                a.getDate(),
                a.getLibelle(),
                a.getNature().getCode(),
                a.getNature().getLabel(),      // <- info utile au front
                a.getNumCheque(),
                a.getMontant(),
                a.getPecBanque(),
                a.getCreatedAt(),
                a.getUpdatedAt()
        );
    }


}
