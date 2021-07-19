package com.barad.bomb.service;

import com.barad.bomb.service.dto.FileDocumentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.barad.bomb.domain.FileDocumentEntity}.
 */
public interface FileDocumentService {
    /**
     * Save a fileDocument.
     *
     * @param fileDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    FileDocumentDTO save(FileDocumentDTO fileDocumentDTO);

    /**
     * Partially updates a fileDocument.
     *
     * @param fileDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FileDocumentDTO> partialUpdate(FileDocumentDTO fileDocumentDTO);

    /**
     * Get all the fileDocuments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FileDocumentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" fileDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FileDocumentDTO> findOne(Long id);

    /**
     * Delete the "id" fileDocument.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
