package com.barad.bomb.service.impl;

import com.barad.bomb.domain.CriticismEntity;
import com.barad.bomb.repository.CriticismRepository;
import com.barad.bomb.service.CriticismService;
import com.barad.bomb.service.dto.CriticismDTO;
import com.barad.bomb.service.mapper.CriticismMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CriticismEntity}.
 */
@Service
@Transactional
public class CriticismServiceImpl implements CriticismService {

    private final Logger log = LoggerFactory.getLogger(CriticismServiceImpl.class);

    private final CriticismRepository criticismRepository;

    private final CriticismMapper criticismMapper;

    public CriticismServiceImpl(CriticismRepository criticismRepository, CriticismMapper criticismMapper) {
        this.criticismRepository = criticismRepository;
        this.criticismMapper = criticismMapper;
    }

    @Override
    public CriticismDTO save(CriticismDTO criticismDTO) {
        log.debug("Request to save Criticism : {}", criticismDTO);
        CriticismEntity criticismEntity = criticismMapper.toEntity(criticismDTO);
        criticismEntity = criticismRepository.save(criticismEntity);
        return criticismMapper.toDto(criticismEntity);
    }

    @Override
    public Optional<CriticismDTO> partialUpdate(CriticismDTO criticismDTO) {
        log.debug("Request to partially update Criticism : {}", criticismDTO);

        return criticismRepository
            .findById(criticismDTO.getId())
            .map(
                existingCriticism -> {
                    criticismMapper.partialUpdate(existingCriticism, criticismDTO);
                    return existingCriticism;
                }
            )
            .map(criticismRepository::save)
            .map(criticismMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CriticismDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Criticisms");
        return criticismRepository.findAll(pageable).map(criticismMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CriticismDTO> findOne(Long id) {
        log.debug("Request to get Criticism : {}", id);
        return criticismRepository.findById(id).map(criticismMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Criticism : {}", id);
        criticismRepository.deleteById(id);
    }
}
