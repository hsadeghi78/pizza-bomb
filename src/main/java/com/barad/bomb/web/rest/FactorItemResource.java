package com.barad.bomb.web.rest;

import com.barad.bomb.repository.FactorItemRepository;
import com.barad.bomb.service.FactorItemQueryService;
import com.barad.bomb.service.FactorItemService;
import com.barad.bomb.service.criteria.FactorItemCriteria;
import com.barad.bomb.service.dto.FactorItemDTO;
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
 * REST controller for managing {@link com.barad.bomb.domain.FactorItemEntity}.
 */
@RestController
@RequestMapping("/api")
public class FactorItemResource {

    private final Logger log = LoggerFactory.getLogger(FactorItemResource.class);

    private static final String ENTITY_NAME = "factorItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactorItemService factorItemService;

    private final FactorItemRepository factorItemRepository;

    private final FactorItemQueryService factorItemQueryService;

    public FactorItemResource(
        FactorItemService factorItemService,
        FactorItemRepository factorItemRepository,
        FactorItemQueryService factorItemQueryService
    ) {
        this.factorItemService = factorItemService;
        this.factorItemRepository = factorItemRepository;
        this.factorItemQueryService = factorItemQueryService;
    }

    /**
     * {@code POST  /factor-items} : Create a new factorItem.
     *
     * @param factorItemDTO the factorItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factorItemDTO, or with status {@code 400 (Bad Request)} if the factorItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/factor-items")
    public ResponseEntity<FactorItemDTO> createFactorItem(@Valid @RequestBody FactorItemDTO factorItemDTO) throws URISyntaxException {
        log.debug("REST request to save FactorItem : {}", factorItemDTO);
        if (factorItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new factorItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FactorItemDTO result = factorItemService.save(factorItemDTO);
        return ResponseEntity
            .created(new URI("/api/factor-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /factor-items/:id} : Updates an existing factorItem.
     *
     * @param id the id of the factorItemDTO to save.
     * @param factorItemDTO the factorItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factorItemDTO,
     * or with status {@code 400 (Bad Request)} if the factorItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factorItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/factor-items/{id}")
    public ResponseEntity<FactorItemDTO> updateFactorItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FactorItemDTO factorItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FactorItem : {}, {}", id, factorItemDTO);
        if (factorItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factorItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factorItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FactorItemDTO result = factorItemService.save(factorItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, factorItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /factor-items/:id} : Partial updates given fields of an existing factorItem, field will ignore if it is null
     *
     * @param id the id of the factorItemDTO to save.
     * @param factorItemDTO the factorItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factorItemDTO,
     * or with status {@code 400 (Bad Request)} if the factorItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the factorItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the factorItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/factor-items/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FactorItemDTO> partialUpdateFactorItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FactorItemDTO factorItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FactorItem partially : {}, {}", id, factorItemDTO);
        if (factorItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factorItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factorItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FactorItemDTO> result = factorItemService.partialUpdate(factorItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, factorItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /factor-items} : get all the factorItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factorItems in body.
     */
    @GetMapping("/factor-items")
    public ResponseEntity<List<FactorItemDTO>> getAllFactorItems(FactorItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FactorItems by criteria: {}", criteria);
        Page<FactorItemDTO> page = factorItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /factor-items/count} : count all the factorItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/factor-items/count")
    public ResponseEntity<Long> countFactorItems(FactorItemCriteria criteria) {
        log.debug("REST request to count FactorItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(factorItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /factor-items/:id} : get the "id" factorItem.
     *
     * @param id the id of the factorItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factorItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/factor-items/{id}")
    public ResponseEntity<FactorItemDTO> getFactorItem(@PathVariable Long id) {
        log.debug("REST request to get FactorItem : {}", id);
        Optional<FactorItemDTO> factorItemDTO = factorItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(factorItemDTO);
    }

    /**
     * {@code DELETE  /factor-items/:id} : delete the "id" factorItem.
     *
     * @param id the id of the factorItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/factor-items/{id}")
    public ResponseEntity<Void> deleteFactorItem(@PathVariable Long id) {
        log.debug("REST request to delete FactorItem : {}", id);
        factorItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
