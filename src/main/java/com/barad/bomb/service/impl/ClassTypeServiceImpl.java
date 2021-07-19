package com.barad.bomb.service.impl;

import com.barad.bomb.domain.ClassTypeEntity;
import com.barad.bomb.repository.ClassTypeRepository;
import com.barad.bomb.service.ClassTypeService;
import com.barad.bomb.service.dto.ClassTypeDTO;
import com.barad.bomb.service.mapper.ClassTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ClassTypeEntity}.
 */
@Service
@Transactional
public class ClassTypeServiceImpl implements ClassTypeService {

    private final Logger log = LoggerFactory.getLogger(ClassTypeServiceImpl.class);

    private final ClassTypeRepository classTypeRepository;

    private final ClassTypeMapper classTypeMapper;

    public ClassTypeServiceImpl(ClassTypeRepository classTypeRepository, ClassTypeMapper classTypeMapper) {
        this.classTypeRepository = classTypeRepository;
        this.classTypeMapper = classTypeMapper;
    }

    @Override
    public ClassTypeDTO save(ClassTypeDTO classTypeDTO) {
        log.debug("Request to save ClassType : {}", classTypeDTO);
        ClassTypeEntity classTypeEntity = classTypeMapper.toEntity(classTypeDTO);
        classTypeEntity = classTypeRepository.save(classTypeEntity);
        return classTypeMapper.toDto(classTypeEntity);
    }

    @Override
    public Optional<ClassTypeDTO> partialUpdate(ClassTypeDTO classTypeDTO) {
        log.debug("Request to partially update ClassType : {}", classTypeDTO);

        return classTypeRepository
            .findById(classTypeDTO.getId())
            .map(
                existingClassType -> {
                    classTypeMapper.partialUpdate(existingClassType, classTypeDTO);
                    return existingClassType;
                }
            )
            .map(classTypeRepository::save)
            .map(classTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClassTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ClassTypes");
        return classTypeRepository.findAll(pageable).map(classTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClassTypeDTO> findOne(Long id) {
        log.debug("Request to get ClassType : {}", id);
        return classTypeRepository.findById(id).map(classTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClassType : {}", id);
        classTypeRepository.deleteById(id);
    }
}
