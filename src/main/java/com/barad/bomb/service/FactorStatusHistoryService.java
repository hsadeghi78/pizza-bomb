package com.barad.bomb.service;

import com.barad.bomb.service.dto.FactorStatusHistoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.barad.bomb.domain.FactorStatusHistoryEntity}.
 */
public interface FactorStatusHistoryService {
    /**
     * Save a factorStatusHistory.
     *
     * @param factorStatusHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    FactorStatusHistoryDTO save(FactorStatusHistoryDTO factorStatusHistoryDTO);

    /**
     * Partially updates a factorStatusHistory.
     *
     * @param factorStatusHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FactorStatusHistoryDTO> partialUpdate(FactorStatusHistoryDTO factorStatusHistoryDTO);

    /**
     * Get all the factorStatusHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FactorStatusHistoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" factorStatusHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FactorStatusHistoryDTO> findOne(Long id);

    /**
     * Delete the "id" factorStatusHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
