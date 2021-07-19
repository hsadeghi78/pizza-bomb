package com.barad.bomb.service.impl;

import com.barad.bomb.domain.FactorStatusHistoryEntity;
import com.barad.bomb.repository.FactorStatusHistoryRepository;
import com.barad.bomb.service.FactorStatusHistoryService;
import com.barad.bomb.service.dto.FactorStatusHistoryDTO;
import com.barad.bomb.service.mapper.FactorStatusHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FactorStatusHistoryEntity}.
 */
@Service
@Transactional
public class FactorStatusHistoryServiceImpl implements FactorStatusHistoryService {

    private final Logger log = LoggerFactory.getLogger(FactorStatusHistoryServiceImpl.class);

    private final FactorStatusHistoryRepository factorStatusHistoryRepository;

    private final FactorStatusHistoryMapper factorStatusHistoryMapper;

    public FactorStatusHistoryServiceImpl(
        FactorStatusHistoryRepository factorStatusHistoryRepository,
        FactorStatusHistoryMapper factorStatusHistoryMapper
    ) {
        this.factorStatusHistoryRepository = factorStatusHistoryRepository;
        this.factorStatusHistoryMapper = factorStatusHistoryMapper;
    }

    @Override
    public FactorStatusHistoryDTO save(FactorStatusHistoryDTO factorStatusHistoryDTO) {
        log.debug("Request to save FactorStatusHistory : {}", factorStatusHistoryDTO);
        FactorStatusHistoryEntity factorStatusHistoryEntity = factorStatusHistoryMapper.toEntity(factorStatusHistoryDTO);
        factorStatusHistoryEntity = factorStatusHistoryRepository.save(factorStatusHistoryEntity);
        return factorStatusHistoryMapper.toDto(factorStatusHistoryEntity);
    }

    @Override
    public Optional<FactorStatusHistoryDTO> partialUpdate(FactorStatusHistoryDTO factorStatusHistoryDTO) {
        log.debug("Request to partially update FactorStatusHistory : {}", factorStatusHistoryDTO);

        return factorStatusHistoryRepository
            .findById(factorStatusHistoryDTO.getId())
            .map(
                existingFactorStatusHistory -> {
                    factorStatusHistoryMapper.partialUpdate(existingFactorStatusHistory, factorStatusHistoryDTO);
                    return existingFactorStatusHistory;
                }
            )
            .map(factorStatusHistoryRepository::save)
            .map(factorStatusHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FactorStatusHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FactorStatusHistories");
        return factorStatusHistoryRepository.findAll(pageable).map(factorStatusHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FactorStatusHistoryDTO> findOne(Long id) {
        log.debug("Request to get FactorStatusHistory : {}", id);
        return factorStatusHistoryRepository.findById(id).map(factorStatusHistoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FactorStatusHistory : {}", id);
        factorStatusHistoryRepository.deleteById(id);
    }
}
