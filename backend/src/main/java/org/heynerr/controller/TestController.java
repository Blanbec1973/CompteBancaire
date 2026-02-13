package org.heynerr.controller;

import org.heynerr.model.AccountLine;
import org.heynerr.model.Nature;
import org.heynerr.repository.AccountLineRepository;
import org.heynerr.repository.NatureRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
public class TestController {
    private final AccountLineRepository accountLineRepository;
    private final NatureRepository natureRepository;

    public TestController(AccountLineRepository accountLineRepository, NatureRepository natureRepository) {
        this.accountLineRepository = accountLineRepository;
        this.natureRepository = natureRepository;
    }

    @GetMapping("/api/test")
    public String testDatabase() {
        // Récupérer une nature déjà en base
        Nature vir = natureRepository.findById("VIR")
                .orElseThrow(() -> new IllegalStateException("Nature 'VIR' absente : seed manquant"));

        AccountLine accountLine = new AccountLine(
                LocalDate.now(),
                "Test",
                vir,                 // ⚠️ ne plus passer null ici
                null,                // numCheque facultatif pour VIR
                BigDecimal.valueOf(10.00),
                null
        );

        accountLineRepository.save(accountLine);
        return "✅ Ligne créée avec id : " + accountLine.getId();
    }

    @GetMapping("/api/test-chq")
    public String testCheque() {
        Nature chq = natureRepository.findById("CHQ")
                .orElseThrow(() -> new IllegalStateException("Nature 'CHQ' absente : seed manquant"));

        AccountLine accountLine = new AccountLine(
                LocalDate.now(),
                "Règlement chèque",
                chq,
                123456L,                      // ⚠️ requis car CHQ.requiresChequeNumber = true
                new BigDecimal("100.00"),
                null
        );

        accountLineRepository.save(accountLine);
        return "✅ CHQ créé avec id : " + accountLine.getId();
    }
    @GetMapping("/api/test-chq-invalid")
    public String testChequeInvalid() {
        Nature chq = natureRepository.findById("CHQ")
                .orElseThrow(() -> new IllegalStateException("Nature 'CHQ' absente : seed manquant"));

        AccountLine accountLine = new AccountLine(
                LocalDate.now(),
                "Chèque sans numéro",
                chq,
                null,                          // ❌ invalide si ta règle @AssertTrue est en place
                new BigDecimal("50.00"),
                null
        );

        accountLineRepository.save(accountLine); // -> Bean Validation & exception
        return "❌ Tu ne devrais pas voir ce message ; une exception doit se produire.";
    }


}
