package com.barad.bomb.service;

import com.barad.bomb.service.dto.ClassificationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.barad.bomb.domain.ClassificationEntity}.
 */
public interface ClassificationService {
    /**
     * Save a classification.
     *
     * @param classificationDTO the entity to save.
     * @return the persisted entity.
     */
    ClassificationDTO save(ClassificationDTO classificationDTO);

    /**
     * Partially updates a classification.
     *
     * @param classificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClassificationDTO> partialUpdate(ClassificationDTO classificationDTO);

    /**
     * Get all the classifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClassificationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" classification.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClassificationDTO> findOne(Long id);

    /**
     * Delete the "id" classification.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
