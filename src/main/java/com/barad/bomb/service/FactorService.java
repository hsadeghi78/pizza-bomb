package com.barad.bomb.service;

import com.barad.bomb.service.dto.FactorDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.barad.bomb.domain.FactorEntity}.
 */
public interface FactorService {
    /**
     * Save a factor.
     *
     * @param factorDTO the entity to save.
     * @return the persisted entity.
     */
    FactorDTO save(FactorDTO factorDTO);

    /**
     * Partially updates a factor.
     *
     * @param factorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FactorDTO> partialUpdate(FactorDTO factorDTO);

    /**
     * Get all the factors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FactorDTO> findAll(Pageable pageable);

    /**
     * Get the "id" factor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FactorDTO> findOne(Long id);

    /**
     * Delete the "id" factor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
