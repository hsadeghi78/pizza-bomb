package com.barad.bomb.service.impl;

import com.barad.bomb.domain.PriceHistoryEntity;
import com.barad.bomb.repository.PriceHistoryRepository;
import com.barad.bomb.service.PriceHistoryService;
import com.barad.bomb.service.dto.PriceHistoryDTO;
import com.barad.bomb.service.mapper.PriceHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PriceHistoryEntity}.
 */
@Service
@Transactional
public class PriceHistoryServiceImpl implements PriceHistoryService {

    private final Logger log = LoggerFactory.getLogger(PriceHistoryServiceImpl.class);

    private final PriceHistoryRepository priceHistoryRepository;

    private final PriceHistoryMapper priceHistoryMapper;

    public PriceHistoryServiceImpl(PriceHistoryRepository priceHistoryRepository, PriceHistoryMapper priceHistoryMapper) {
        this.priceHistoryRepository = priceHistoryRepository;
        this.priceHistoryMapper = priceHistoryMapper;
    }

    @Override
    public PriceHistoryDTO save(PriceHistoryDTO priceHistoryDTO) {
        log.debug("Request to save PriceHistory : {}", priceHistoryDTO);
        PriceHistoryEntity priceHistoryEntity = priceHistoryMapper.toEntity(priceHistoryDTO);
        priceHistoryEntity = priceHistoryRepository.save(priceHistoryEntity);
        return priceHistoryMapper.toDto(priceHistoryEntity);
    }

    @Override
    public Optional<PriceHistoryDTO> partialUpdate(PriceHistoryDTO priceHistoryDTO) {
        log.debug("Request to partially update PriceHistory : {}", priceHistoryDTO);

        return priceHistoryRepository
            .findById(priceHistoryDTO.getId())
            .map(
                existingPriceHistory -> {
                    priceHistoryMapper.partialUpdate(existingPriceHistory, priceHistoryDTO);
                    return existingPriceHistory;
                }
            )
            .map(priceHistoryRepository::save)
            .map(priceHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PriceHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PriceHistories");
        return priceHistoryRepository.findAll(pageable).map(priceHistoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PriceHistoryDTO> findOne(Long id) {
        log.debug("Request to get PriceHistory : {}", id);
        return priceHistoryRepository.findById(id).map(priceHistoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PriceHistory : {}", id);
        priceHistoryRepository.deleteById(id);
    }
}
