package com.barad.bomb.service;

import com.barad.bomb.service.dto.CriticismDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.barad.bomb.domain.CriticismEntity}.
 */
public interface CriticismService {
    /**
     * Save a criticism.
     *
     * @param criticismDTO the entity to save.
     * @return the persisted entity.
     */
    CriticismDTO save(CriticismDTO criticismDTO);

    /**
     * Partially updates a criticism.
     *
     * @param criticismDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CriticismDTO> partialUpdate(CriticismDTO criticismDTO);

    /**
     * Get all the criticisms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CriticismDTO> findAll(Pageable pageable);

    /**
     * Get the "id" criticism.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CriticismDTO> findOne(Long id);

    /**
     * Delete the "id" criticism.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
