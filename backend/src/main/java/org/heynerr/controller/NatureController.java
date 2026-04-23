package org.heynerr.controller;

import org.heynerr.model.Nature;
import org.heynerr.repository.NatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/natures")
public class NatureController {

    private final NatureRepository natureRepository;

    @Autowired
    public NatureController(NatureRepository natureRepository) {
        this.natureRepository = natureRepository;
    }

    /**
     * Récupère toutes les natures disponibles
     * Utilisé pour peupler les dropdowns
     * @return liste des natures triées par code
     */
    @GetMapping
    public List<Nature> getAllNatures() {
        return natureRepository.findAll();
    }

    /**
     * Récupère une nature par son code
     * @param code le code de la nature (ex: "CHQ", "VIR")
     * @return la nature ou 404 si non trouvée
     */
    @GetMapping("/{code}")
    public Nature getNatureByCode(@PathVariable String code) {
        return natureRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Nature non trouvée: " + code));
    }
}

