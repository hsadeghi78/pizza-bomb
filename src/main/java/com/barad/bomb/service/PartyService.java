package com.barad.bomb.service;

import com.barad.bomb.service.dto.PartyDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.barad.bomb.domain.PartyEntity}.
 */
public interface PartyService {
    /**
     * Save a party.
     *
     * @param partyDTO the entity to save.
     * @return the persisted entity.
     */
    PartyDTO save(PartyDTO partyDTO);

    /**
     * Partially updates a party.
     *
     * @param partyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PartyDTO> partialUpdate(PartyDTO partyDTO);

    /**
     * Get all the parties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PartyDTO> findAll(Pageable pageable);

    /**
     * Get the "id" party.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PartyDTO> findOne(Long id);

    /**
     * Delete the "id" party.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
