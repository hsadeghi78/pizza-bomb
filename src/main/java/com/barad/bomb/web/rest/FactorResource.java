package com.barad.bomb.web.rest;

import com.barad.bomb.repository.FactorRepository;
import com.barad.bomb.service.FactorQueryService;
import com.barad.bomb.service.FactorService;
import com.barad.bomb.service.criteria.FactorCriteria;
import com.barad.bomb.service.dto.FactorDTO;
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
 * REST controller for managing {@link com.barad.bomb.domain.FactorEntity}.
 */
@RestController
@RequestMapping("/api")
public class FactorResource {

    private final Logger log = LoggerFactory.getLogger(FactorResource.class);

    private static final String ENTITY_NAME = "factor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactorService factorService;

    private final FactorRepository factorRepository;

    private final FactorQueryService factorQueryService;

    public FactorResource(FactorService factorService, FactorRepository factorRepository, FactorQueryService factorQueryService) {
        this.factorService = factorService;
        this.factorRepository = factorRepository;
        this.factorQueryService = factorQueryService;
    }

    /**
     * {@code POST  /factors} : Create a new factor.
     *
     * @param factorDTO the factorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factorDTO, or with status {@code 400 (Bad Request)} if the factor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/factors")
    public ResponseEntity<FactorDTO> createFactor(@Valid @RequestBody FactorDTO factorDTO) throws URISyntaxException {
        log.debug("REST request to save Factor : {}", factorDTO);
        if (factorDTO.getId() != null) {
            throw new BadRequestAlertException("A new factor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FactorDTO result = factorService.save(factorDTO);
        return ResponseEntity
            .created(new URI("/api/factors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /factors/:id} : Updates an existing factor.
     *
     * @param id the id of the factorDTO to save.
     * @param factorDTO the factorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factorDTO,
     * or with status {@code 400 (Bad Request)} if the factorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/factors/{id}")
    public ResponseEntity<FactorDTO> updateFactor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FactorDTO factorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Factor : {}, {}", id, factorDTO);
        if (factorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FactorDTO result = factorService.save(factorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, factorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /factors/:id} : Partial updates given fields of an existing factor, field will ignore if it is null
     *
     * @param id the id of the factorDTO to save.
     * @param factorDTO the factorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factorDTO,
     * or with status {@code 400 (Bad Request)} if the factorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the factorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the factorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/factors/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FactorDTO> partialUpdateFactor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FactorDTO factorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Factor partially : {}, {}", id, factorDTO);
        if (factorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FactorDTO> result = factorService.partialUpdate(factorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, factorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /factors} : get all the factors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factors in body.
     */
    @GetMapping("/factors")
    public ResponseEntity<List<FactorDTO>> getAllFactors(FactorCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Factors by criteria: {}", criteria);
        Page<FactorDTO> page = factorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /factors/count} : count all the factors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/factors/count")
    public ResponseEntity<Long> countFactors(FactorCriteria criteria) {
        log.debug("REST request to count Factors by criteria: {}", criteria);
        return ResponseEntity.ok().body(factorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /factors/:id} : get the "id" factor.
     *
     * @param id the id of the factorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/factors/{id}")
    public ResponseEntity<FactorDTO> getFactor(@PathVariable Long id) {
        log.debug("REST request to get Factor : {}", id);
        Optional<FactorDTO> factorDTO = factorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(factorDTO);
    }

    /**
     * {@code DELETE  /factors/:id} : delete the "id" factor.
     *
     * @param id the id of the factorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/factors/{id}")
    public ResponseEntity<Void> deleteFactor(@PathVariable Long id) {
        log.debug("REST request to delete Factor : {}", id);
        factorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
