package com.barad.bomb.service;

import com.barad.bomb.service.dto.PartyInformationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.barad.bomb.domain.PartyInformationEntity}.
 */
public interface PartyInformationService {
    /**
     * Save a partyInformation.
     *
     * @param partyInformationDTO the entity to save.
     * @return the persisted entity.
     */
    PartyInformationDTO save(PartyInformationDTO partyInformationDTO);

    /**
     * Partially updates a partyInformation.
     *
     * @param partyInformationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PartyInformationDTO> partialUpdate(PartyInformationDTO partyInformationDTO);

    /**
     * Get all the partyInformations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PartyInformationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" partyInformation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PartyInformationDTO> findOne(Long id);

    /**
     * Delete the "id" partyInformation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
