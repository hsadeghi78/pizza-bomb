package com.barad.bomb.service.impl;

import com.barad.bomb.domain.ClassificationEntity;
import com.barad.bomb.repository.ClassificationRepository;
import com.barad.bomb.service.ClassificationService;
import com.barad.bomb.service.dto.ClassificationDTO;
import com.barad.bomb.service.mapper.ClassificationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ClassificationEntity}.
 */
@Service
@Transactional
public class ClassificationServiceImpl implements ClassificationService {

    private final Logger log = LoggerFactory.getLogger(ClassificationServiceImpl.class);

    private final ClassificationRepository classificationRepository;

    private final ClassificationMapper classificationMapper;

    public ClassificationServiceImpl(ClassificationRepository classificationRepository, ClassificationMapper classificationMapper) {
        this.classificationRepository = classificationRepository;
        this.classificationMapper = classificationMapper;
    }

    @Override
    public ClassificationDTO save(ClassificationDTO classificationDTO) {
        log.debug("Request to save Classification : {}", classificationDTO);
        ClassificationEntity classificationEntity = classificationMapper.toEntity(classificationDTO);
        classificationEntity = classificationRepository.save(classificationEntity);
        return classificationMapper.toDto(classificationEntity);
    }

    @Override
    public Optional<ClassificationDTO> partialUpdate(ClassificationDTO classificationDTO) {
        log.debug("Request to partially update Classification : {}", classificationDTO);

        return classificationRepository
            .findById(classificationDTO.getId())
            .map(
                existingClassification -> {
                    classificationMapper.partialUpdate(existingClassification, classificationDTO);
                    return existingClassification;
                }
            )
            .map(classificationRepository::save)
            .map(classificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClassificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Classifications");
        return classificationRepository.findAll(pageable).map(classificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClassificationDTO> findOne(Long id) {
        log.debug("Request to get Classification : {}", id);
        return classificationRepository.findById(id).map(classificationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Classification : {}", id);
        classificationRepository.deleteById(id);
    }
}
