package com.barad.bomb.service;

import com.barad.bomb.service.dto.ClassTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.barad.bomb.domain.ClassTypeEntity}.
 */
public interface ClassTypeService {
    /**
     * Save a classType.
     *
     * @param classTypeDTO the entity to save.
     * @return the persisted entity.
     */
    ClassTypeDTO save(ClassTypeDTO classTypeDTO);

    /**
     * Partially updates a classType.
     *
     * @param classTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClassTypeDTO> partialUpdate(ClassTypeDTO classTypeDTO);

    /**
     * Get all the classTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClassTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" classType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClassTypeDTO> findOne(Long id);

    /**
     * Delete the "id" classType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
