package com.barad.bomb.service.impl;

import com.barad.bomb.domain.PartyInformationEntity;
import com.barad.bomb.repository.PartyInformationRepository;
import com.barad.bomb.service.PartyInformationService;
import com.barad.bomb.service.dto.PartyInformationDTO;
import com.barad.bomb.service.mapper.PartyInformationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PartyInformationEntity}.
 */
@Service
@Transactional
public class PartyInformationServiceImpl implements PartyInformationService {

    private final Logger log = LoggerFactory.getLogger(PartyInformationServiceImpl.class);

    private final PartyInformationRepository partyInformationRepository;

    private final PartyInformationMapper partyInformationMapper;

    public PartyInformationServiceImpl(
        PartyInformationRepository partyInformationRepository,
        PartyInformationMapper partyInformationMapper
    ) {
        this.partyInformationRepository = partyInformationRepository;
        this.partyInformationMapper = partyInformationMapper;
    }

    @Override
    public PartyInformationDTO save(PartyInformationDTO partyInformationDTO) {
        log.debug("Request to save PartyInformation : {}", partyInformationDTO);
        PartyInformationEntity partyInformationEntity = partyInformationMapper.toEntity(partyInformationDTO);
        partyInformationEntity = partyInformationRepository.save(partyInformationEntity);
        return partyInformationMapper.toDto(partyInformationEntity);
    }

    @Override
    public Optional<PartyInformationDTO> partialUpdate(PartyInformationDTO partyInformationDTO) {
        log.debug("Request to partially update PartyInformation : {}", partyInformationDTO);

        return partyInformationRepository
            .findById(partyInformationDTO.getId())
            .map(
                existingPartyInformation -> {
                    partyInformationMapper.partialUpdate(existingPartyInformation, partyInformationDTO);
                    return existingPartyInformation;
                }
            )
            .map(partyInformationRepository::save)
            .map(partyInformationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PartyInformationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PartyInformations");
        return partyInformationRepository.findAll(pageable).map(partyInformationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PartyInformationDTO> findOne(Long id) {
        log.debug("Request to get PartyInformation : {}", id);
        return partyInformationRepository.findById(id).map(partyInformationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PartyInformation : {}", id);
        partyInformationRepository.deleteById(id);
    }
}
