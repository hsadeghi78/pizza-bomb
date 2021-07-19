package com.barad.bomb.service.impl;

import com.barad.bomb.domain.ConsumeMaterialEntity;
import com.barad.bomb.repository.ConsumeMaterialRepository;
import com.barad.bomb.service.ConsumeMaterialService;
import com.barad.bomb.service.dto.ConsumeMaterialDTO;
import com.barad.bomb.service.mapper.ConsumeMaterialMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ConsumeMaterialEntity}.
 */
@Service
@Transactional
public class ConsumeMaterialServiceImpl implements ConsumeMaterialService {

    private final Logger log = LoggerFactory.getLogger(ConsumeMaterialServiceImpl.class);

    private final ConsumeMaterialRepository consumeMaterialRepository;

    private final ConsumeMaterialMapper consumeMaterialMapper;

    public ConsumeMaterialServiceImpl(ConsumeMaterialRepository consumeMaterialRepository, ConsumeMaterialMapper consumeMaterialMapper) {
        this.consumeMaterialRepository = consumeMaterialRepository;
        this.consumeMaterialMapper = consumeMaterialMapper;
    }

    @Override
    public ConsumeMaterialDTO save(ConsumeMaterialDTO consumeMaterialDTO) {
        log.debug("Request to save ConsumeMaterial : {}", consumeMaterialDTO);
        ConsumeMaterialEntity consumeMaterialEntity = consumeMaterialMapper.toEntity(consumeMaterialDTO);
        consumeMaterialEntity = consumeMaterialRepository.save(consumeMaterialEntity);
        return consumeMaterialMapper.toDto(consumeMaterialEntity);
    }

    @Override
    public Optional<ConsumeMaterialDTO> partialUpdate(ConsumeMaterialDTO consumeMaterialDTO) {
        log.debug("Request to partially update ConsumeMaterial : {}", consumeMaterialDTO);

        return consumeMaterialRepository
            .findById(consumeMaterialDTO.getId())
            .map(
                existingConsumeMaterial -> {
                    consumeMaterialMapper.partialUpdate(existingConsumeMaterial, consumeMaterialDTO);
                    return existingConsumeMaterial;
                }
            )
            .map(consumeMaterialRepository::save)
            .map(consumeMaterialMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConsumeMaterialDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ConsumeMaterials");
        return consumeMaterialRepository.findAll(pageable).map(consumeMaterialMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConsumeMaterialDTO> findOne(Long id) {
        log.debug("Request to get ConsumeMaterial : {}", id);
        return consumeMaterialRepository.findById(id).map(consumeMaterialMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConsumeMaterial : {}", id);
        consumeMaterialRepository.deleteById(id);
    }
}
