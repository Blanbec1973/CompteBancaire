package org.heynerr.controller;

import jakarta.validation.Valid;
import org.heynerr.model.AccountLine;
import org.heynerr.model.dto.AccountLineDTO;
import org.heynerr.service.AccountLineService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account-lines")
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
}
