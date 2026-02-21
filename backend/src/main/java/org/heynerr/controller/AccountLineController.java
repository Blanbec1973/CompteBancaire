package org.heynerr.controller;

import jakarta.validation.Valid;
import org.heynerr.model.AccountLine;
import org.heynerr.model.dto.AccountLineDTO;
import org.heynerr.model.dto.AccountLineReadDTO;
import org.heynerr.model.dto.PointageDTO;
import org.heynerr.service.AccountLineService;
import org.heynerr.service.SoldesService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accountLines")
public class AccountLineController {

    private final AccountLineService service;
    private final SoldesService soldesService;

    public AccountLineController(AccountLineService service, SoldesService soldesService) {
        this.service = service;
        this.soldesService = soldesService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountLine create(@Valid @RequestBody AccountLineDTO dto) {
        return service.createFromDto(dto);
    }


    // READ - tout
    @GetMapping
    public List<AccountLineReadDTO> findAll() {
        return service.findAll();
    }

    // SEARCH - par libellé et/ou natureCode (q)
    @GetMapping("/search")
    public List<AccountLine> search(@RequestParam("q") String q) {
        return service.search(q);
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
        return service.findAllNotPointedOrderByDateAsc();
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

}
