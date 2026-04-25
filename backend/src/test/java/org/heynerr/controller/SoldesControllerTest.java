package org.heynerr.controller;

import org.heynerr.service.SoldesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SoldesController.class)
@Import(SoldesControllerTest.TestConfig.class)
class SoldesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SoldesService soldesService;

    @Test
    void getSoldes_retourneDTO_avecChampsAttendus() throws Exception {
        when(soldesService.getSoldePecBanque()).thenReturn(new BigDecimal("123.45"));
        when(soldesService.getSoldeFinMoisCourant()).thenReturn(new BigDecimal("999.99"));
        when(soldesService.getDateSoldeFinMois()).thenReturn(LocalDate.of(2026, 2, 1));

        mockMvc.perform(get("/api/soldes/getsoldes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                // Adapte les noms de propriétés JSON à ton SoldesDTO (voir remarque plus bas)
                .andExpect(jsonPath("$.soldePecBanque").value(123.45))
                .andExpect(jsonPath("$.soldeFinMois").value(999.99))
                .andExpect(jsonPath("$.dateCalcul").value("2026-02-01"));
    }

    @Test
    void getSoldes_retourneZeroEtNull_siServiceRetourneDefauts() throws Exception {
        when(soldesService.getSoldePecBanque()).thenReturn(BigDecimal.ZERO);
        when(soldesService.getSoldeFinMoisCourant()).thenReturn(BigDecimal.ZERO);
        when(soldesService.getDateSoldeFinMois()).thenReturn(null);

        mockMvc.perform(get("/api/soldes/getsoldes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.soldePecBanque").value(0))
                .andExpect(jsonPath("$.soldeFinMois").value(0))
                .andExpect(jsonPath("$.dateCalcul").doesNotExist()); // ou .isEmpty() selon sérialisation
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        SoldesService soldesService() {
            return Mockito.mock(SoldesService.class);
        }
    }
}