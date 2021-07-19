package com.barad.bomb.service.impl;

import com.barad.bomb.domain.FactorItemEntity;
import com.barad.bomb.repository.FactorItemRepository;
import com.barad.bomb.service.FactorItemService;
import com.barad.bomb.service.dto.FactorItemDTO;
import com.barad.bomb.service.mapper.FactorItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FactorItemEntity}.
 */
@Service
@Transactional
public class FactorItemServiceImpl implements FactorItemService {

    private final Logger log = LoggerFactory.getLogger(FactorItemServiceImpl.class);

    private final FactorItemRepository factorItemRepository;

    private final FactorItemMapper factorItemMapper;

    public FactorItemServiceImpl(FactorItemRepository factorItemRepository, FactorItemMapper factorItemMapper) {
        this.factorItemRepository = factorItemRepository;
        this.factorItemMapper = factorItemMapper;
    }

    @Override
    public FactorItemDTO save(FactorItemDTO factorItemDTO) {
        log.debug("Request to save FactorItem : {}", factorItemDTO);
        FactorItemEntity factorItemEntity = factorItemMapper.toEntity(factorItemDTO);
        factorItemEntity = factorItemRepository.save(factorItemEntity);
        return factorItemMapper.toDto(factorItemEntity);
    }

    @Override
    public Optional<FactorItemDTO> partialUpdate(FactorItemDTO factorItemDTO) {
        log.debug("Request to partially update FactorItem : {}", factorItemDTO);

        return factorItemRepository
            .findById(factorItemDTO.getId())
            .map(
                existingFactorItem -> {
                    factorItemMapper.partialUpdate(existingFactorItem, factorItemDTO);
                    return existingFactorItem;
                }
            )
            .map(factorItemRepository::save)
            .map(factorItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FactorItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FactorItems");
        return factorItemRepository.findAll(pageable).map(factorItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FactorItemDTO> findOne(Long id) {
        log.debug("Request to get FactorItem : {}", id);
        return factorItemRepository.findById(id).map(factorItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FactorItem : {}", id);
        factorItemRepository.deleteById(id);
    }
}
