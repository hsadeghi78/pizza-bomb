package com.barad.bomb.web.rest;

import com.barad.bomb.repository.PriceHistoryRepository;
import com.barad.bomb.service.PriceHistoryQueryService;
import com.barad.bomb.service.PriceHistoryService;
import com.barad.bomb.service.criteria.PriceHistoryCriteria;
import com.barad.bomb.service.dto.PriceHistoryDTO;
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
 * REST controller for managing {@link com.barad.bomb.domain.PriceHistoryEntity}.
 */
@RestController
@RequestMapping("/api")
public class PriceHistoryResource {

    private final Logger log = LoggerFactory.getLogger(PriceHistoryResource.class);

    private static final String ENTITY_NAME = "priceHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PriceHistoryService priceHistoryService;

    private final PriceHistoryRepository priceHistoryRepository;

    private final PriceHistoryQueryService priceHistoryQueryService;

    public PriceHistoryResource(
        PriceHistoryService priceHistoryService,
        PriceHistoryRepository priceHistoryRepository,
        PriceHistoryQueryService priceHistoryQueryService
    ) {
        this.priceHistoryService = priceHistoryService;
        this.priceHistoryRepository = priceHistoryRepository;
        this.priceHistoryQueryService = priceHistoryQueryService;
    }

    /**
     * {@code POST  /price-histories} : Create a new priceHistory.
     *
     * @param priceHistoryDTO the priceHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new priceHistoryDTO, or with status {@code 400 (Bad Request)} if the priceHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/price-histories")
    public ResponseEntity<PriceHistoryDTO> createPriceHistory(@Valid @RequestBody PriceHistoryDTO priceHistoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save PriceHistory : {}", priceHistoryDTO);
        if (priceHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new priceHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PriceHistoryDTO result = priceHistoryService.save(priceHistoryDTO);
        return ResponseEntity
            .created(new URI("/api/price-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /price-histories/:id} : Updates an existing priceHistory.
     *
     * @param id the id of the priceHistoryDTO to save.
     * @param priceHistoryDTO the priceHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated priceHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the priceHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the priceHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/price-histories/{id}")
    public ResponseEntity<PriceHistoryDTO> updatePriceHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PriceHistoryDTO priceHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PriceHistory : {}, {}", id, priceHistoryDTO);
        if (priceHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, priceHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!priceHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PriceHistoryDTO result = priceHistoryService.save(priceHistoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, priceHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /price-histories/:id} : Partial updates given fields of an existing priceHistory, field will ignore if it is null
     *
     * @param id the id of the priceHistoryDTO to save.
     * @param priceHistoryDTO the priceHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated priceHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the priceHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the priceHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the priceHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/price-histories/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PriceHistoryDTO> partialUpdatePriceHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PriceHistoryDTO priceHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PriceHistory partially : {}, {}", id, priceHistoryDTO);
        if (priceHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, priceHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!priceHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PriceHistoryDTO> result = priceHistoryService.partialUpdate(priceHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, priceHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /price-histories} : get all the priceHistories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of priceHistories in body.
     */
    @GetMapping("/price-histories")
    public ResponseEntity<List<PriceHistoryDTO>> getAllPriceHistories(PriceHistoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PriceHistories by criteria: {}", criteria);
        Page<PriceHistoryDTO> page = priceHistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /price-histories/count} : count all the priceHistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/price-histories/count")
    public ResponseEntity<Long> countPriceHistories(PriceHistoryCriteria criteria) {
        log.debug("REST request to count PriceHistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(priceHistoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /price-histories/:id} : get the "id" priceHistory.
     *
     * @param id the id of the priceHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the priceHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/price-histories/{id}")
    public ResponseEntity<PriceHistoryDTO> getPriceHistory(@PathVariable Long id) {
        log.debug("REST request to get PriceHistory : {}", id);
        Optional<PriceHistoryDTO> priceHistoryDTO = priceHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(priceHistoryDTO);
    }

    /**
     * {@code DELETE  /price-histories/:id} : delete the "id" priceHistory.
     *
     * @param id the id of the priceHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/price-histories/{id}")
    public ResponseEntity<Void> deletePriceHistory(@PathVariable Long id) {
        log.debug("REST request to delete PriceHistory : {}", id);
        priceHistoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
