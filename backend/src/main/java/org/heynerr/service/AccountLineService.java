package org.heynerr.service;

import org.heynerr.model.AccountLine;
import org.heynerr.model.Nature;
import org.heynerr.model.dto.AccountLineDTO;
import org.heynerr.repository.AccountLineRepository;
import org.heynerr.repository.NatureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountLineService {

    private final AccountLineRepository accountLineRepository;
    private final NatureRepository natureRepository;

    public AccountLineService(AccountLineRepository accountLineRepository, NatureRepository natureRepository) {
        this.accountLineRepository = accountLineRepository;
        this.natureRepository = natureRepository;
    }

    @Transactional
    public AccountLine createFromDto(AccountLineDTO dto) {
        Nature nature = natureRepository.findById(dto.getNatureCode())
                .orElseThrow(() -> new IllegalArgumentException("Nature inconnue: " + dto.getNatureCode()));

        AccountLine entity = new AccountLine(
                dto.getDate(),
                dto.getLibelle(),
                nature,
                dto.getNumCheque(),
                dto.getMontant(),
                dto.getPecBanque()
        );

        // La validation Bean Validation sur l'entité (ex. @AssertTrue) sera rejouée
        // par Hibernate Validator à la persistance (pre-persist / pre-update).
        return accountLineRepository.save(entity);
    }
}
