package com.barad.bomb.service;

import com.barad.bomb.service.dto.FactorItemDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.barad.bomb.domain.FactorItemEntity}.
 */
public interface FactorItemService {
    /**
     * Save a factorItem.
     *
     * @param factorItemDTO the entity to save.
     * @return the persisted entity.
     */
    FactorItemDTO save(FactorItemDTO factorItemDTO);

    /**
     * Partially updates a factorItem.
     *
     * @param factorItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FactorItemDTO> partialUpdate(FactorItemDTO factorItemDTO);

    /**
     * Get all the factorItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FactorItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" factorItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FactorItemDTO> findOne(Long id);

    /**
     * Delete the "id" factorItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
