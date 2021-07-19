package com.barad.bomb.service.impl;

import com.barad.bomb.domain.FoodTypeEntity;
import com.barad.bomb.repository.FoodTypeRepository;
import com.barad.bomb.service.FoodTypeService;
import com.barad.bomb.service.dto.FoodTypeDTO;
import com.barad.bomb.service.mapper.FoodTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FoodTypeEntity}.
 */
@Service
@Transactional
public class FoodTypeServiceImpl implements FoodTypeService {

    private final Logger log = LoggerFactory.getLogger(FoodTypeServiceImpl.class);

    private final FoodTypeRepository foodTypeRepository;

    private final FoodTypeMapper foodTypeMapper;

    public FoodTypeServiceImpl(FoodTypeRepository foodTypeRepository, FoodTypeMapper foodTypeMapper) {
        this.foodTypeRepository = foodTypeRepository;
        this.foodTypeMapper = foodTypeMapper;
    }

    @Override
    public FoodTypeDTO save(FoodTypeDTO foodTypeDTO) {
        log.debug("Request to save FoodType : {}", foodTypeDTO);
        FoodTypeEntity foodTypeEntity = foodTypeMapper.toEntity(foodTypeDTO);
        foodTypeEntity = foodTypeRepository.save(foodTypeEntity);
        return foodTypeMapper.toDto(foodTypeEntity);
    }

    @Override
    public Optional<FoodTypeDTO> partialUpdate(FoodTypeDTO foodTypeDTO) {
        log.debug("Request to partially update FoodType : {}", foodTypeDTO);

        return foodTypeRepository
            .findById(foodTypeDTO.getId())
            .map(
                existingFoodType -> {
                    foodTypeMapper.partialUpdate(existingFoodType, foodTypeDTO);
                    return existingFoodType;
                }
            )
            .map(foodTypeRepository::save)
            .map(foodTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FoodTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FoodTypes");
        return foodTypeRepository.findAll(pageable).map(foodTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FoodTypeDTO> findOne(Long id) {
        log.debug("Request to get FoodType : {}", id);
        return foodTypeRepository.findById(id).map(foodTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FoodType : {}", id);
        foodTypeRepository.deleteById(id);
    }
}
