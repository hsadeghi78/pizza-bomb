package com.barad.bomb.service;

import com.barad.bomb.service.dto.PriceHistoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.barad.bomb.domain.PriceHistoryEntity}.
 */
public interface PriceHistoryService {
    /**
     * Save a priceHistory.
     *
     * @param priceHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    PriceHistoryDTO save(PriceHistoryDTO priceHistoryDTO);

    /**
     * Partially updates a priceHistory.
     *
     * @param priceHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PriceHistoryDTO> partialUpdate(PriceHistoryDTO priceHistoryDTO);

    /**
     * Get all the priceHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PriceHistoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" priceHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PriceHistoryDTO> findOne(Long id);

    /**
     * Delete the "id" priceHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
