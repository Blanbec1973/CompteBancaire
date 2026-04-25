package org.heynerr.controller;

import jakarta.validation.Valid;
import org.heynerr.logging.LogSanitizer;
import org.heynerr.model.AccountLine;
import org.heynerr.model.dto.AccountLineDTO;
import org.heynerr.model.dto.AccountLineReadDTO;
import org.heynerr.model.dto.GenerationDTO;
import org.heynerr.model.dto.PointageDTO;
import org.heynerr.service.AccountLineService;
import org.heynerr.service.SoldesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/accountLines")
public class AccountLineController {
    private static final Logger log = LoggerFactory.getLogger(AccountLineController.class);

    private final AccountLineService service;
    private final SoldesService soldesService;

    public AccountLineController(AccountLineService service, SoldesService soldesService) {
        this.service = service;
        this.soldesService = soldesService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountLine create(@Valid @RequestBody AccountLineDTO dto) {
        log.info("API POST /accountLines: received dto with libelle={}, nature={}",
                LogSanitizer.sanitize(dto.getLibelle()),
                LogSanitizer.sanitize(dto.getNatureCode()));
        try {
            AccountLine created = service.createFromDto(dto);
            log.info("API POST /accountLines: created successfully, id={}", created.getId());
            return created;
        } catch (Exception ex) {
            log.error("API POST /accountLines: FAILED", ex);
            throw ex;
        }
    }


    // READ - tout
    @GetMapping
    public List<AccountLineReadDTO> findAll() {
        log.debug("API GET /accountLines");
        return service.findAll();
    }

    // SEARCH - par libellé et/ou natureCode (q)
    @GetMapping("/search")
    public List<AccountLineReadDTO> search(@RequestParam("q") String q) {
        log.info("API GET /accountLines/search: query='{}'",
                LogSanitizer.sanitize(q));
        List<AccountLineReadDTO> results = service.search(q);
        log.info("API GET /accountLines/search: found {} results", results.size());
        return results;
    }

    // UPDATE
    @PutMapping("/{id}")
    public AccountLineReadDTO update(
            @PathVariable Long id,
            @RequestBody AccountLineDTO dto) {
        return service.updateFromDto(id, dto);
    }
    
    @GetMapping("/getsoldepecbanque")
    public Map<String, BigDecimal> getSoldePecBanque() {
        BigDecimal solde = soldesService.getSoldePecBanque();
        return Map.of("soldePecBanque", solde);
    }

    @GetMapping("/non-pointed")
    public List<AccountLineReadDTO> getNonPointed() {
        return service.findNonPointed();
    }

    @GetMapping("/pointed/{datePointed}")
    public List<AccountLineReadDTO> getPointedAtDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datePointed) {
        return service.findAllPointedAtDate(datePointed);
    }

    @PutMapping("/{id}/pointage")
    public AccountLineReadDTO pointer(
            @PathVariable Long id,
            @RequestBody PointageDTO dto
    ) {
        return service.pointer(id, dto.pecBanque());
    }

    @PostMapping("/generateAnnual")
    public List<AccountLineReadDTO> generateAnnual(@RequestBody GenerationDTO dto) {
        return service.generateAnnual(dto);
    }

    @GetMapping("/listcheques")
    public List<AccountLineReadDTO> listCheques() {
        return service.listCheque();
    }


}
