package org.heynerr.controller;

import org.heynerr.model.dto.SoldesDTO;
import org.heynerr.service.SoldesService;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/soldes")
public class SoldesController {

    private final SoldesService soldesService;

    public SoldesController(SoldesService soldesService) {
        this.soldesService = soldesService;
    }

    @GetMapping("/getsoldes")
    public SoldesDTO getSoldes() {
        return new SoldesDTO(
                soldesService.getSoldePecBanque(),
                soldesService.getSoldeFinMoisCourant(),
                soldesService.getDateSoldeFinMois()
        );
    }
}
