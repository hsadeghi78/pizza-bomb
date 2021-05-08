package com.barad.bomb.web.rest;

import com.barad.bomb.repository.PartyRepository;
import com.barad.bomb.service.PartyQueryService;
import com.barad.bomb.service.PartyService;
import com.barad.bomb.service.criteria.PartyCriteria;
import com.barad.bomb.service.dto.PartyDTO;
import com.barad.bomb.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.barad.bomb.domain.PartyEntity}.
 */
@RestController
@RequestMapping("/api")
public class PartyResource {

    private final Logger log = LoggerFactory.getLogger(PartyResource.class);

    private static final String ENTITY_NAME = "party";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartyService partyService;

    private final PartyRepository partyRepository;

    private final PartyQueryService partyQueryService;

    public PartyResource(PartyService partyService, PartyRepository partyRepository, PartyQueryService partyQueryService) {
        this.partyService = partyService;
        this.partyRepository = partyRepository;
        this.partyQueryService = partyQueryService;
    }

    /**
     * {@code POST  /parties} : Create a new party.
     *
     * @param partyDTO the partyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partyDTO, or with status {@code 400 (Bad Request)} if the party has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parties")
    public ResponseEntity<PartyDTO> createParty(@Valid @RequestBody PartyDTO partyDTO) throws URISyntaxException {
        log.debug("REST request to save Party : {}", partyDTO);
        if (partyDTO.getId() != null) {
            throw new BadRequestAlertException("A new party cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PartyDTO result = partyService.save(partyDTO);
        return ResponseEntity
            .created(new URI("/api/parties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parties/:id} : Updates an existing party.
     *
     * @param id the id of the partyDTO to save.
     * @param partyDTO the partyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyDTO,
     * or with status {@code 400 (Bad Request)} if the partyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parties/{id}")
    public ResponseEntity<PartyDTO> updateParty(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PartyDTO partyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Party : {}, {}", id, partyDTO);
        if (partyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PartyDTO result = partyService.save(partyDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parties/:id} : Partial updates given fields of an existing party, field will ignore if it is null
     *
     * @param id the id of the partyDTO to save.
     * @param partyDTO the partyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyDTO,
     * or with status {@code 400 (Bad Request)} if the partyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the partyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the partyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parties/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PartyDTO> partialUpdateParty(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PartyDTO partyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Party partially : {}, {}", id, partyDTO);
        if (partyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartyDTO> result = partyService.partialUpdate(partyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /parties} : get all the parties.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parties in body.
     */
    @GetMapping("/parties")
    public ResponseEntity<List<PartyDTO>> getAllParties(PartyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Parties by criteria: {}", criteria);
        Page<PartyDTO> page = partyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /parties/count} : count all the parties.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/parties/count")
    public ResponseEntity<Long> countParties(PartyCriteria criteria) {
        log.debug("REST request to count Parties by criteria: {}", criteria);
        return ResponseEntity.ok().body(partyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /parties/:id} : get the "id" party.
     *
     * @param id the id of the partyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parties/{id}")
    public ResponseEntity<PartyDTO> getParty(@PathVariable Long id) {
        log.debug("REST request to get Party : {}", id);
        Optional<PartyDTO> partyDTO = partyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partyDTO);
    }

    /**
     * {@code DELETE  /parties/:id} : delete the "id" party.
     *
     * @param id the id of the partyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parties/{id}")
    public ResponseEntity<Void> deleteParty(@PathVariable Long id) {
        log.debug("REST request to delete Party : {}", id);
        partyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
