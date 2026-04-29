package org.heynerr.controller;

import org.heynerr.model.Nature;
import org.heynerr.repository.NatureRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NatureController.class)
@Import(NatureControllerTest.TestConfig.class)
class NatureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NatureRepository natureRepository;

    @Test
    void getAllNatures_retourneListe_avecToutesLesNatures() throws Exception {
        Nature chq = new Nature("CHQ", "Chèque", true);
        Nature vir = new Nature("VIR", "Virement", false);

        List<Nature> natures = Arrays.asList(chq, vir);

        when(natureRepository.findAll()).thenReturn(natures);

        mockMvc.perform(get("/api/natures"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].code").value("CHQ"))
                .andExpect(jsonPath("$[0].label").value("Chèque"))
                .andExpect(jsonPath("$[0].requiresChequeNumber").value(true))
                .andExpect(jsonPath("$[1].code").value("VIR"))
                .andExpect(jsonPath("$[1].label").value("Virement"))
                .andExpect(jsonPath("$[1].requiresChequeNumber").value(false));
    }

    @Test
    void getAllNatures_retourneListeVide_siAucuneNature() throws Exception {
        when(natureRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/natures"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getNatureByCode_retourneNature_quandCodeExiste() throws Exception {
        Nature nature = new Nature("CHQ", "Chèque", true);

        when(natureRepository.findById("CHQ")).thenReturn(Optional.of(nature));

        mockMvc.perform(get("/api/natures/CHQ"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.code").value("CHQ"))
                .andExpect(jsonPath("$.label").value("Chèque"))
                .andExpect(jsonPath("$.requiresChequeNumber").value(true));
    }

    @Test
    void getNatureByCode_retourne404_quandCodeNExistePas() throws Exception {
        when(natureRepository.findById("INEXISTANT"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/natures/INEXISTANT"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getNatureByCode_retourne404_avecMessageErreur_quandNonTrouvee() throws Exception {
        when(natureRepository.findById("INVALID"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/natures/INVALID"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        NatureRepository natureRepository() {
            return Mockito.mock(NatureRepository.class);
        }
    }
}

