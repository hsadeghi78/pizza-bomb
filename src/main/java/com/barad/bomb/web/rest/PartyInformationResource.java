package com.barad.bomb.web.rest;

import com.barad.bomb.repository.PartyInformationRepository;
import com.barad.bomb.service.PartyInformationQueryService;
import com.barad.bomb.service.PartyInformationService;
import com.barad.bomb.service.criteria.PartyInformationCriteria;
import com.barad.bomb.service.dto.PartyInformationDTO;
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
 * REST controller for managing {@link com.barad.bomb.domain.PartyInformationEntity}.
 */
@RestController
@RequestMapping("/api")
public class PartyInformationResource {

    private final Logger log = LoggerFactory.getLogger(PartyInformationResource.class);

    private static final String ENTITY_NAME = "partyInformation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartyInformationService partyInformationService;

    private final PartyInformationRepository partyInformationRepository;

    private final PartyInformationQueryService partyInformationQueryService;

    public PartyInformationResource(
        PartyInformationService partyInformationService,
        PartyInformationRepository partyInformationRepository,
        PartyInformationQueryService partyInformationQueryService
    ) {
        this.partyInformationService = partyInformationService;
        this.partyInformationRepository = partyInformationRepository;
        this.partyInformationQueryService = partyInformationQueryService;
    }

    /**
     * {@code POST  /party-informations} : Create a new partyInformation.
     *
     * @param partyInformationDTO the partyInformationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partyInformationDTO, or with status {@code 400 (Bad Request)} if the partyInformation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/party-informations")
    public ResponseEntity<PartyInformationDTO> createPartyInformation(@Valid @RequestBody PartyInformationDTO partyInformationDTO)
        throws URISyntaxException {
        log.debug("REST request to save PartyInformation : {}", partyInformationDTO);
        if (partyInformationDTO.getId() != null) {
            throw new BadRequestAlertException("A new partyInformation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PartyInformationDTO result = partyInformationService.save(partyInformationDTO);
        return ResponseEntity
            .created(new URI("/api/party-informations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /party-informations/:id} : Updates an existing partyInformation.
     *
     * @param id the id of the partyInformationDTO to save.
     * @param partyInformationDTO the partyInformationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyInformationDTO,
     * or with status {@code 400 (Bad Request)} if the partyInformationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partyInformationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/party-informations/{id}")
    public ResponseEntity<PartyInformationDTO> updatePartyInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PartyInformationDTO partyInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PartyInformation : {}, {}", id, partyInformationDTO);
        if (partyInformationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyInformationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PartyInformationDTO result = partyInformationService.save(partyInformationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyInformationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /party-informations/:id} : Partial updates given fields of an existing partyInformation, field will ignore if it is null
     *
     * @param id the id of the partyInformationDTO to save.
     * @param partyInformationDTO the partyInformationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyInformationDTO,
     * or with status {@code 400 (Bad Request)} if the partyInformationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the partyInformationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the partyInformationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/party-informations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PartyInformationDTO> partialUpdatePartyInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PartyInformationDTO partyInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PartyInformation partially : {}, {}", id, partyInformationDTO);
        if (partyInformationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyInformationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartyInformationDTO> result = partyInformationService.partialUpdate(partyInformationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyInformationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /party-informations} : get all the partyInformations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of partyInformations in body.
     */
    @GetMapping("/party-informations")
    public ResponseEntity<List<PartyInformationDTO>> getAllPartyInformations(PartyInformationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PartyInformations by criteria: {}", criteria);
        Page<PartyInformationDTO> page = partyInformationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /party-informations/count} : count all the partyInformations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/party-informations/count")
    public ResponseEntity<Long> countPartyInformations(PartyInformationCriteria criteria) {
        log.debug("REST request to count PartyInformations by criteria: {}", criteria);
        return ResponseEntity.ok().body(partyInformationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /party-informations/:id} : get the "id" partyInformation.
     *
     * @param id the id of the partyInformationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partyInformationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/party-informations/{id}")
    public ResponseEntity<PartyInformationDTO> getPartyInformation(@PathVariable Long id) {
        log.debug("REST request to get PartyInformation : {}", id);
        Optional<PartyInformationDTO> partyInformationDTO = partyInformationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partyInformationDTO);
    }

    /**
     * {@code DELETE  /party-informations/:id} : delete the "id" partyInformation.
     *
     * @param id the id of the partyInformationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/party-informations/{id}")
    public ResponseEntity<Void> deletePartyInformation(@PathVariable Long id) {
        log.debug("REST request to delete PartyInformation : {}", id);
        partyInformationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
