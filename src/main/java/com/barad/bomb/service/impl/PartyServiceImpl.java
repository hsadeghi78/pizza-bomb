package com.barad.bomb.service.impl;

import com.barad.bomb.domain.PartyEntity;
import com.barad.bomb.repository.PartyRepository;
import com.barad.bomb.service.PartyService;
import com.barad.bomb.service.dto.PartyDTO;
import com.barad.bomb.service.mapper.PartyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PartyEntity}.
 */
@Service
@Transactional
public class PartyServiceImpl implements PartyService {

    private final Logger log = LoggerFactory.getLogger(PartyServiceImpl.class);

    private final PartyRepository partyRepository;

    private final PartyMapper partyMapper;

    public PartyServiceImpl(PartyRepository partyRepository, PartyMapper partyMapper) {
        this.partyRepository = partyRepository;
        this.partyMapper = partyMapper;
    }

    @Override
    public PartyDTO save(PartyDTO partyDTO) {
        log.debug("Request to save Party : {}", partyDTO);
        PartyEntity partyEntity = partyMapper.toEntity(partyDTO);
        partyEntity = partyRepository.save(partyEntity);
        return partyMapper.toDto(partyEntity);
    }

    @Override
    public Optional<PartyDTO> partialUpdate(PartyDTO partyDTO) {
        log.debug("Request to partially update Party : {}", partyDTO);

        return partyRepository
            .findById(partyDTO.getId())
            .map(
                existingParty -> {
                    partyMapper.partialUpdate(existingParty, partyDTO);
                    return existingParty;
                }
            )
            .map(partyRepository::save)
            .map(partyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PartyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Parties");
        return partyRepository.findAll(pageable).map(partyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PartyDTO> findOne(Long id) {
        log.debug("Request to get Party : {}", id);
        return partyRepository.findById(id).map(partyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Party : {}", id);
        partyRepository.deleteById(id);
    }
}
