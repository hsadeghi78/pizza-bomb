package com.barad.bomb.service;

import com.barad.bomb.service.dto.FoodDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.barad.bomb.domain.FoodEntity}.
 */
public interface FoodService {
    /**
     * Save a food.
     *
     * @param foodDTO the entity to save.
     * @return the persisted entity.
     */
    FoodDTO save(FoodDTO foodDTO);

    /**
     * Partially updates a food.
     *
     * @param foodDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FoodDTO> partialUpdate(FoodDTO foodDTO);

    /**
     * Get all the foods.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FoodDTO> findAll(Pageable pageable);

    /**
     * Get the "id" food.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FoodDTO> findOne(Long id);

    /**
     * Delete the "id" food.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
