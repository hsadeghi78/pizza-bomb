package com.barad.bomb.web.rest;

import com.barad.bomb.repository.FactorStatusHistoryRepository;
import com.barad.bomb.service.FactorStatusHistoryQueryService;
import com.barad.bomb.service.FactorStatusHistoryService;
import com.barad.bomb.service.criteria.FactorStatusHistoryCriteria;
import com.barad.bomb.service.dto.FactorStatusHistoryDTO;
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
 * REST controller for managing {@link com.barad.bomb.domain.FactorStatusHistoryEntity}.
 */
@RestController
@RequestMapping("/api")
public class FactorStatusHistoryResource {

    private final Logger log = LoggerFactory.getLogger(FactorStatusHistoryResource.class);

    private static final String ENTITY_NAME = "factorStatusHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactorStatusHistoryService factorStatusHistoryService;

    private final FactorStatusHistoryRepository factorStatusHistoryRepository;

    private final FactorStatusHistoryQueryService factorStatusHistoryQueryService;

    public FactorStatusHistoryResource(
        FactorStatusHistoryService factorStatusHistoryService,
        FactorStatusHistoryRepository factorStatusHistoryRepository,
        FactorStatusHistoryQueryService factorStatusHistoryQueryService
    ) {
        this.factorStatusHistoryService = factorStatusHistoryService;
        this.factorStatusHistoryRepository = factorStatusHistoryRepository;
        this.factorStatusHistoryQueryService = factorStatusHistoryQueryService;
    }

    /**
     * {@code POST  /factor-status-histories} : Create a new factorStatusHistory.
     *
     * @param factorStatusHistoryDTO the factorStatusHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factorStatusHistoryDTO, or with status {@code 400 (Bad Request)} if the factorStatusHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/factor-status-histories")
    public ResponseEntity<FactorStatusHistoryDTO> createFactorStatusHistory(
        @Valid @RequestBody FactorStatusHistoryDTO factorStatusHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to save FactorStatusHistory : {}", factorStatusHistoryDTO);
        if (factorStatusHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new factorStatusHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FactorStatusHistoryDTO result = factorStatusHistoryService.save(factorStatusHistoryDTO);
        return ResponseEntity
            .created(new URI("/api/factor-status-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /factor-status-histories/:id} : Updates an existing factorStatusHistory.
     *
     * @param id the id of the factorStatusHistoryDTO to save.
     * @param factorStatusHistoryDTO the factorStatusHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factorStatusHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the factorStatusHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factorStatusHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/factor-status-histories/{id}")
    public ResponseEntity<FactorStatusHistoryDTO> updateFactorStatusHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FactorStatusHistoryDTO factorStatusHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FactorStatusHistory : {}, {}", id, factorStatusHistoryDTO);
        if (factorStatusHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factorStatusHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factorStatusHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FactorStatusHistoryDTO result = factorStatusHistoryService.save(factorStatusHistoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, factorStatusHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /factor-status-histories/:id} : Partial updates given fields of an existing factorStatusHistory, field will ignore if it is null
     *
     * @param id the id of the factorStatusHistoryDTO to save.
     * @param factorStatusHistoryDTO the factorStatusHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factorStatusHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the factorStatusHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the factorStatusHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the factorStatusHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/factor-status-histories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FactorStatusHistoryDTO> partialUpdateFactorStatusHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FactorStatusHistoryDTO factorStatusHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FactorStatusHistory partially : {}, {}", id, factorStatusHistoryDTO);
        if (factorStatusHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factorStatusHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factorStatusHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FactorStatusHistoryDTO> result = factorStatusHistoryService.partialUpdate(factorStatusHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, factorStatusHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /factor-status-histories} : get all the factorStatusHistories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factorStatusHistories in body.
     */
    @GetMapping("/factor-status-histories")
    public ResponseEntity<List<FactorStatusHistoryDTO>> getAllFactorStatusHistories(
        FactorStatusHistoryCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get FactorStatusHistories by criteria: {}", criteria);
        Page<FactorStatusHistoryDTO> page = factorStatusHistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /factor-status-histories/count} : count all the factorStatusHistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/factor-status-histories/count")
    public ResponseEntity<Long> countFactorStatusHistories(FactorStatusHistoryCriteria criteria) {
        log.debug("REST request to count FactorStatusHistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(factorStatusHistoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /factor-status-histories/:id} : get the "id" factorStatusHistory.
     *
     * @param id the id of the factorStatusHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factorStatusHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/factor-status-histories/{id}")
    public ResponseEntity<FactorStatusHistoryDTO> getFactorStatusHistory(@PathVariable Long id) {
        log.debug("REST request to get FactorStatusHistory : {}", id);
        Optional<FactorStatusHistoryDTO> factorStatusHistoryDTO = factorStatusHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(factorStatusHistoryDTO);
    }

    /**
     * {@code DELETE  /factor-status-histories/:id} : delete the "id" factorStatusHistory.
     *
     * @param id the id of the factorStatusHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/factor-status-histories/{id}")
    public ResponseEntity<Void> deleteFactorStatusHistory(@PathVariable Long id) {
        log.debug("REST request to delete FactorStatusHistory : {}", id);
        factorStatusHistoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
