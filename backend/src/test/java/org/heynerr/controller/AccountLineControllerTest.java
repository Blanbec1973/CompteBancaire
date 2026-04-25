package org.heynerr.controller;

import org.heynerr.exception.EntityNotFoundException;
import org.heynerr.exception.TechnicalException;
import org.heynerr.service.AccountLineService;
import org.heynerr.service.SoldesService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AccountLineController.class)
@Import(AccountLineControllerTest.TestConfig.class)
class AccountLineControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountLineService accountLineService;

    @Test
    void createAccountLine_invalidDto_returns400() throws Exception {
        String invalidJson = """
        {
          "date": "2026-01-01",
          "libelle": "",
          "natureCode": "CHQ",
          "montant": null
        }
        """;

        mockMvc.perform(post("/api/accountLines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/api/accountLines"));
    }

    @Test
    void createAccountLine_technicalException_returns500() throws Exception {
        String validJson = """
        {
          "date": "2026-01-01",
          "libelle": "Test Transaction",
          "natureCode": "CHQ",
          "montant": 100.00
        }
        """;

        Mockito.doThrow(new TechnicalException("DB down", new RuntimeException()))
                .when(accountLineService).createFromDto(any());

        mockMvc.perform(post("/api/accountLines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("DB down"))
                .andExpect(jsonPath("$.path").value("/api/accountLines"));
    }

    @Test
    void updateAccountLine_notFound_returns404() throws Exception {
        String validJson = """
        {
          "date": "2026-01-01",
          "libelle": "Test Transaction",
          "natureCode": "CHQ",
          "montant": 100.00
        }
        """;

        when(accountLineService.updateFromDto(eq(999L), any()))
                .thenThrow(new EntityNotFoundException("AccountLine introuvable: 999"));

        mockMvc.perform(put("/api/accountLines/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("AccountLine introuvable: 999"))
                .andExpect(jsonPath("$.path").value("/api/accountLines/999"));
    }


    @TestConfiguration
    static class TestConfig {
        @Bean
        AccountLineService accountLineService() {
            return Mockito.mock(AccountLineService.class);
        }
        @Bean
        SoldesService soldesService() {
            return Mockito.mock(SoldesService.class);
        }
    }
}