package com.barad.bomb.service;

import com.barad.bomb.service.dto.FoodTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.barad.bomb.domain.FoodTypeEntity}.
 */
public interface FoodTypeService {
    /**
     * Save a foodType.
     *
     * @param foodTypeDTO the entity to save.
     * @return the persisted entity.
     */
    FoodTypeDTO save(FoodTypeDTO foodTypeDTO);

    /**
     * Partially updates a foodType.
     *
     * @param foodTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FoodTypeDTO> partialUpdate(FoodTypeDTO foodTypeDTO);

    /**
     * Get all the foodTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FoodTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" foodType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FoodTypeDTO> findOne(Long id);

    /**
     * Delete the "id" foodType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
