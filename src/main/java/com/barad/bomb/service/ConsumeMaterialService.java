package com.barad.bomb.service;

import com.barad.bomb.service.dto.ConsumeMaterialDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.barad.bomb.domain.ConsumeMaterialEntity}.
 */
public interface ConsumeMaterialService {
    /**
     * Save a consumeMaterial.
     *
     * @param consumeMaterialDTO the entity to save.
     * @return the persisted entity.
     */
    ConsumeMaterialDTO save(ConsumeMaterialDTO consumeMaterialDTO);

    /**
     * Partially updates a consumeMaterial.
     *
     * @param consumeMaterialDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ConsumeMaterialDTO> partialUpdate(ConsumeMaterialDTO consumeMaterialDTO);

    /**
     * Get all the consumeMaterials.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConsumeMaterialDTO> findAll(Pageable pageable);

    /**
     * Get the "id" consumeMaterial.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConsumeMaterialDTO> findOne(Long id);

    /**
     * Delete the "id" consumeMaterial.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
