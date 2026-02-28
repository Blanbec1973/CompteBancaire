package org.heynerr.controller;

import org.heynerr.model.SoldeFinMois;
import org.heynerr.model.SoldePrisEnCompteBanque;
import org.heynerr.repository.SoldeFinMoisCourantRepository;
import org.heynerr.repository.SoldePecBanqueRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SoldesControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SoldePecBanqueRepository repoPec;

    @Autowired
    private SoldeFinMoisCourantRepository repoFinMois;

    @Test
    void getSoldes_finDuMoisCourant_estCorrecte() throws Exception {

        // --- GIVEN ---
        repoPec.save(
                new SoldePrisEnCompteBanque(new BigDecimal("200.00"))
        );

        LocalDate finDuMois = LocalDate.now()
                .withDayOfMonth(LocalDate.now().lengthOfMonth());

        repoFinMois.save(
                new SoldeFinMois(new BigDecimal("1500.00"), finDuMois)
        );

        // --- WHEN / THEN ---
        mockMvc.perform(get("/api/soldes/getsoldes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.soldePecBanque").value(200.00))
                .andExpect(jsonPath("$.soldeFinMois").value(1500.00))
                .andExpect(jsonPath("$.dateCalcul").value(finDuMois.toString()));
    }
}
