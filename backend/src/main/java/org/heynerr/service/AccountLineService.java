package org.heynerr.service;

import org.heynerr.logging.LogSanitizer;
import org.heynerr.model.AccountLine;
import org.heynerr.model.Nature;
import org.heynerr.model.dto.AccountLineDTO;
import org.heynerr.model.dto.AccountLineReadDTO;
import org.heynerr.model.dto.GenerationDTO;
import org.heynerr.repository.AccountLineRepository;
import org.heynerr.repository.NatureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class AccountLineService {
    private static final Logger log = LoggerFactory.getLogger(AccountLineService.class);

    private final AccountLineRepository accountLineRepository;
    private final NatureRepository natureRepository;

    public AccountLineService(AccountLineRepository accountLineRepository, NatureRepository natureRepository) {
        this.accountLineRepository = accountLineRepository;
        this.natureRepository = natureRepository;
    }

    @Transactional
    public AccountLine createFromDto(AccountLineDTO dto) {
        if (log.isInfoEnabled())
            log.info("ENTRY createFromDto: date={}, libelle={}, nature={}, montant={}",
                    dto.getDate(),
                    LogSanitizer.sanitize(dto.getLibelle()),
                    LogSanitizer.sanitize(dto.getNatureCode()),
                    dto.getMontant());
        

        Nature nature = natureRepository.findById(dto.getNatureCode())
                .orElseThrow(() -> {
                    log.warn("Nature not found: code={}", dto.getNatureCode());
                    return new ResponseStatusException(NOT_FOUND, "Unknown nature : " + dto.getNatureCode());
                });

        AccountLine entity = new AccountLine(
                dto.getDate(),
                dto.getLibelle(),
                nature,
                dto.getNumCheque(),
                dto.getMontant(),
                dto.getPecBanque()
        );

        AccountLine saved = accountLineRepository.save(entity);
        log.info("EXIT createFromDto: created id={}, date={}", saved.getId(), saved.getDate());
        return saved;
    }


    @Transactional(readOnly = true)
    public List<AccountLineReadDTO> findAll() {
        log.debug("ENTRY findAll");
        long startTime = System.currentTimeMillis();

        List<AccountLineReadDTO> results = accountLineRepository.findAllWithNature()
                .stream()
                .map(this::toReadDto)
                .toList();

        long duration = System.currentTimeMillis() - startTime;
        log.info("EXIT findAll: returned {} entries in {}ms", results.size(), duration);

        if (duration > 5000) {
            log.warn("SLOW_QUERY findAll: {}ms (should be < 5s)", duration);
        }

        return results;

    }

    @Transactional(readOnly = true)
    public List<AccountLineReadDTO> search(String q) {
        if (log.isInfoEnabled())
            log.info("ENTRY search: query='{}'", LogSanitizer.sanitize(q));

        List<AccountLine> lines = accountLineRepository.searchLibelleContainsOrNature(q);
        List<AccountLineReadDTO> results = lines.stream()
                .map(this::toReadDto)
                .toList();
        log.info("EXIT search: found {} results", results.size());
        return results;
    }

    @Transactional
    public AccountLineReadDTO updateFromDto(Long id, AccountLineDTO dto) {
        if (log.isInfoEnabled())
            log.info("ENTRY updateFromDto: id={}, nature={}", id,
                    dto.getNatureCode());

        AccountLine entity = accountLineRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "AccountLine introuvable: " + id)
                );

        Nature nature = natureRepository.findById(dto.getNatureCode())
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Update failed: nature not found, code=" + dto.getNatureCode())
                );

        entity.setDate(dto.getDate());
        entity.setLibelle(dto.getLibelle());
        entity.setNature(nature);
        entity.setNumCheque(dto.getNumCheque());
        entity.setMontant(dto.getMontant());
        entity.setPecBanque(dto.getPecBanque());

        AccountLineReadDTO result = toReadDto(accountLineRepository.save(entity));
        log.info("EXIT updateFromDto: id={} updated", id);
        return result;
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
        return accountLineRepository.findByPecBanqueOrderByDateDesc(datePointed)
                .stream()
                .map(this::toReadDto)
                .toList();
    }

    @Transactional
    public AccountLineReadDTO pointer(Long id, LocalDate datePointage) {
        log.debug("ENTRY pointer: id={}, datePointage={}", id, datePointage);

        AccountLine al = accountLineRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "AccountLine not found : " + id)
                );

        LocalDate previousValue = al.getPecBanque();
        al.setPecBanque(datePointage);

        AccountLineReadDTO result = toReadDto(accountLineRepository.save(al));
        log.info("EXIT pointer: id={}, pecBanque changed from {} to {}", id, previousValue, datePointage);
        return result;
    }


    @Transactional
    public List<AccountLineReadDTO> generateAnnual(GenerationDTO dto) {
        if (log.isInfoEnabled())
            log.info("ENTRY generateAnnual: startDate={}, nature={}", dto.date(),
                    dto.natureCode());
        
        LocalDate date = dto.date();
        int year = date.getYear();
        List<AccountLineReadDTO> results = new ArrayList<>();
        int created = 0;
        Nature nature = natureRepository.findById(dto.natureCode())
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Unknown nature : " + dto.natureCode())
                );

        while (date.getYear() == year) {
            AccountLine al = new AccountLine();
            al.setDate(date);
            al.setLibelle(dto.libelle());
            al.setMontant(dto.montant());
            al.setNature(nature);
            al.setNumCheque(null);
            al.setPecBanque(null);

            AccountLine saved = accountLineRepository.save(al);
            results.add(toReadDto(saved));
            created++;

            log.debug("Generated entry: date={}, id={}", date, saved.getId());
            date = date.plusMonths(1);
        }

        log.info("EXIT generateAnnual: created {} entries for year {}", created, year);

        return results;
    }

    public List<AccountLineReadDTO> listCheque() {
        return accountLineRepository.findByNatureOrderByNumChequeDesc(new Nature("CHQ", "Chèque", true))
                .stream()
                .map(this::toReadDto)
                .toList();
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
