package com.barad.bomb.web.rest;

import com.barad.bomb.repository.CriticismRepository;
import com.barad.bomb.service.CriticismQueryService;
import com.barad.bomb.service.CriticismService;
import com.barad.bomb.service.criteria.CriticismCriteria;
import com.barad.bomb.service.dto.CriticismDTO;
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
 * REST controller for managing {@link com.barad.bomb.domain.CriticismEntity}.
 */
@RestController
@RequestMapping("/api")
public class CriticismResource {

    private final Logger log = LoggerFactory.getLogger(CriticismResource.class);

    private static final String ENTITY_NAME = "criticism";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CriticismService criticismService;

    private final CriticismRepository criticismRepository;

    private final CriticismQueryService criticismQueryService;

    public CriticismResource(
        CriticismService criticismService,
        CriticismRepository criticismRepository,
        CriticismQueryService criticismQueryService
    ) {
        this.criticismService = criticismService;
        this.criticismRepository = criticismRepository;
        this.criticismQueryService = criticismQueryService;
    }

    /**
     * {@code POST  /criticisms} : Create a new criticism.
     *
     * @param criticismDTO the criticismDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new criticismDTO, or with status {@code 400 (Bad Request)} if the criticism has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/criticisms")
    public ResponseEntity<CriticismDTO> createCriticism(@Valid @RequestBody CriticismDTO criticismDTO) throws URISyntaxException {
        log.debug("REST request to save Criticism : {}", criticismDTO);
        if (criticismDTO.getId() != null) {
            throw new BadRequestAlertException("A new criticism cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CriticismDTO result = criticismService.save(criticismDTO);
        return ResponseEntity
            .created(new URI("/api/criticisms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /criticisms/:id} : Updates an existing criticism.
     *
     * @param id the id of the criticismDTO to save.
     * @param criticismDTO the criticismDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated criticismDTO,
     * or with status {@code 400 (Bad Request)} if the criticismDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the criticismDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/criticisms/{id}")
    public ResponseEntity<CriticismDTO> updateCriticism(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CriticismDTO criticismDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Criticism : {}, {}", id, criticismDTO);
        if (criticismDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, criticismDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!criticismRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CriticismDTO result = criticismService.save(criticismDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, criticismDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /criticisms/:id} : Partial updates given fields of an existing criticism, field will ignore if it is null
     *
     * @param id the id of the criticismDTO to save.
     * @param criticismDTO the criticismDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated criticismDTO,
     * or with status {@code 400 (Bad Request)} if the criticismDTO is not valid,
     * or with status {@code 404 (Not Found)} if the criticismDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the criticismDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/criticisms/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CriticismDTO> partialUpdateCriticism(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CriticismDTO criticismDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Criticism partially : {}, {}", id, criticismDTO);
        if (criticismDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, criticismDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!criticismRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CriticismDTO> result = criticismService.partialUpdate(criticismDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, criticismDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /criticisms} : get all the criticisms.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of criticisms in body.
     */
    @GetMapping("/criticisms")
    public ResponseEntity<List<CriticismDTO>> getAllCriticisms(CriticismCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Criticisms by criteria: {}", criteria);
        Page<CriticismDTO> page = criticismQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /criticisms/count} : count all the criticisms.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/criticisms/count")
    public ResponseEntity<Long> countCriticisms(CriticismCriteria criteria) {
        log.debug("REST request to count Criticisms by criteria: {}", criteria);
        return ResponseEntity.ok().body(criticismQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /criticisms/:id} : get the "id" criticism.
     *
     * @param id the id of the criticismDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the criticismDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/criticisms/{id}")
    public ResponseEntity<CriticismDTO> getCriticism(@PathVariable Long id) {
        log.debug("REST request to get Criticism : {}", id);
        Optional<CriticismDTO> criticismDTO = criticismService.findOne(id);
        return ResponseUtil.wrapOrNotFound(criticismDTO);
    }

    /**
     * {@code DELETE  /criticisms/:id} : delete the "id" criticism.
     *
     * @param id the id of the criticismDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/criticisms/{id}")
    public ResponseEntity<Void> deleteCriticism(@PathVariable Long id) {
        log.debug("REST request to delete Criticism : {}", id);
        criticismService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
