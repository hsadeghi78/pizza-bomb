package com.barad.bomb.service.impl;

import com.barad.bomb.domain.FactorEntity;
import com.barad.bomb.repository.FactorRepository;
import com.barad.bomb.service.FactorService;
import com.barad.bomb.service.dto.FactorDTO;
import com.barad.bomb.service.mapper.FactorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FactorEntity}.
 */
@Service
@Transactional
public class FactorServiceImpl implements FactorService {

    private final Logger log = LoggerFactory.getLogger(FactorServiceImpl.class);

    private final FactorRepository factorRepository;

    private final FactorMapper factorMapper;

    public FactorServiceImpl(FactorRepository factorRepository, FactorMapper factorMapper) {
        this.factorRepository = factorRepository;
        this.factorMapper = factorMapper;
    }

    @Override
    public FactorDTO save(FactorDTO factorDTO) {
        log.debug("Request to save Factor : {}", factorDTO);
        FactorEntity factorEntity = factorMapper.toEntity(factorDTO);
        factorEntity = factorRepository.save(factorEntity);
        return factorMapper.toDto(factorEntity);
    }

    @Override
    public Optional<FactorDTO> partialUpdate(FactorDTO factorDTO) {
        log.debug("Request to partially update Factor : {}", factorDTO);

        return factorRepository
            .findById(factorDTO.getId())
            .map(
                existingFactor -> {
                    factorMapper.partialUpdate(existingFactor, factorDTO);
                    return existingFactor;
                }
            )
            .map(factorRepository::save)
            .map(factorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FactorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Factors");
        return factorRepository.findAll(pageable).map(factorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FactorDTO> findOne(Long id) {
        log.debug("Request to get Factor : {}", id);
        return factorRepository.findById(id).map(factorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Factor : {}", id);
        factorRepository.deleteById(id);
    }
}
