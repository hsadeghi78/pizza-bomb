package com.barad.bomb.service.impl;

import com.barad.bomb.domain.PartnerEntity;
import com.barad.bomb.repository.PartnerRepository;
import com.barad.bomb.service.PartnerService;
import com.barad.bomb.service.dto.PartnerDTO;
import com.barad.bomb.service.mapper.PartnerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PartnerEntity}.
 */
@Service
@Transactional
public class PartnerServiceImpl implements PartnerService {

    private final Logger log = LoggerFactory.getLogger(PartnerServiceImpl.class);

    private final PartnerRepository partnerRepository;

    private final PartnerMapper partnerMapper;

    public PartnerServiceImpl(PartnerRepository partnerRepository, PartnerMapper partnerMapper) {
        this.partnerRepository = partnerRepository;
        this.partnerMapper = partnerMapper;
    }

    @Override
    public PartnerDTO save(PartnerDTO partnerDTO) {
        log.debug("Request to save Partner : {}", partnerDTO);
        PartnerEntity partnerEntity = partnerMapper.toEntity(partnerDTO);
        partnerEntity = partnerRepository.save(partnerEntity);
        return partnerMapper.toDto(partnerEntity);
    }

    @Override
    public Optional<PartnerDTO> partialUpdate(PartnerDTO partnerDTO) {
        log.debug("Request to partially update Partner : {}", partnerDTO);

        return partnerRepository
            .findById(partnerDTO.getId())
            .map(
                existingPartner -> {
                    partnerMapper.partialUpdate(existingPartner, partnerDTO);
                    return existingPartner;
                }
            )
            .map(partnerRepository::save)
            .map(partnerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PartnerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Partners");
        return partnerRepository.findAll(pageable).map(partnerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PartnerDTO> findOne(Long id) {
        log.debug("Request to get Partner : {}", id);
        return partnerRepository.findById(id).map(partnerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Partner : {}", id);
        partnerRepository.deleteById(id);
    }
}
