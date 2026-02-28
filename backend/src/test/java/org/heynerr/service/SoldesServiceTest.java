package org.heynerr.service;

import org.heynerr.model.SoldeFinMois;
import org.heynerr.model.SoldePrisEnCompteBanque;
import org.heynerr.repository.SoldeFinMoisCourantRepository;
import org.heynerr.repository.SoldePecBanqueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SoldesServiceTest {

    @Mock
    private SoldePecBanqueRepository repoPec;

    @Mock
    private SoldeFinMoisCourantRepository repoFinMois;

    @InjectMocks
    private SoldesService soldesService;

    @Test
    void getSoldePecBanque_retourneValeur_siPresente() {
        SoldePrisEnCompteBanque solde = new SoldePrisEnCompteBanque();
        solde.setSoldePecBanque(new BigDecimal("123.45"));

        when(repoPec.findAll()).thenReturn(List.of(solde));

        BigDecimal result = soldesService.getSoldePecBanque();

        assertEquals(new BigDecimal("123.45"), result);
    }

    @Test
    void getSoldePecBanque_retourneZero_siAucunSolde() {
        when(repoPec.findAll()).thenReturn(List.of());

        BigDecimal result = soldesService.getSoldePecBanque();

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void getSoldeFinMoisCourant_retourneValeur_siPresente() {
        SoldeFinMois solde = new SoldeFinMois();
        solde.setSoldeFinMoisCourant(new BigDecimal("999.99"));

        when(repoFinMois.findAll()).thenReturn(List.of(solde));

        BigDecimal result = soldesService.getSoldeFinMoisCourant();

        assertEquals(new BigDecimal("999.99"), result);
    }

    @Test
    void getSoldeFinMoisCourant_retourneZero_siVide() {
        when(repoFinMois.findAll()).thenReturn(List.of());

        BigDecimal result = soldesService.getSoldeFinMoisCourant();

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void getDateSoldeFinMois_retourneDate_siPresente() {
        SoldeFinMois solde = new SoldeFinMois();
        LocalDate date = LocalDate.of(2026, 2, 1);
        solde.setDateSolde(date);

        when(repoFinMois.findAll()).thenReturn(List.of(solde));

        LocalDate result = soldesService.getDateSoldeFinMois();

        assertEquals(date, result);
    }

    @Test
    void getDateSoldeFinMois_retourneNull_siVide() {
        when(repoFinMois.findAll()).thenReturn(List.of());

        LocalDate result = soldesService.getDateSoldeFinMois();

        assertNull(result);
    }
}