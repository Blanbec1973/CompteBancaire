package org.heynerr.controller;

import jakarta.validation.Valid;
import org.heynerr.model.AccountLine;
import org.heynerr.model.dto.AccountLineDTO;
import org.heynerr.model.dto.AccountLineReadDTO;
import org.heynerr.service.AccountLineService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accountLines")
public class AccountLineController {

    private final AccountLineService service;

    public AccountLineController(AccountLineService service) {
        this.service = service;
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
    public AccountLine update(@PathVariable Long id, @Valid @RequestBody AccountLineDTO dto) {
        return service.updateFromDto(id, dto);
    }

}
