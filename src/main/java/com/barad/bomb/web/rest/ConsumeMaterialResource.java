package com.barad.bomb.web.rest;

import com.barad.bomb.repository.ConsumeMaterialRepository;
import com.barad.bomb.service.ConsumeMaterialQueryService;
import com.barad.bomb.service.ConsumeMaterialService;
import com.barad.bomb.service.criteria.ConsumeMaterialCriteria;
import com.barad.bomb.service.dto.ConsumeMaterialDTO;
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
 * REST controller for managing {@link com.barad.bomb.domain.ConsumeMaterialEntity}.
 */
@RestController
@RequestMapping("/api")
public class ConsumeMaterialResource {

    private final Logger log = LoggerFactory.getLogger(ConsumeMaterialResource.class);

    private static final String ENTITY_NAME = "consumeMaterial";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConsumeMaterialService consumeMaterialService;

    private final ConsumeMaterialRepository consumeMaterialRepository;

    private final ConsumeMaterialQueryService consumeMaterialQueryService;

    public ConsumeMaterialResource(
        ConsumeMaterialService consumeMaterialService,
        ConsumeMaterialRepository consumeMaterialRepository,
        ConsumeMaterialQueryService consumeMaterialQueryService
    ) {
        this.consumeMaterialService = consumeMaterialService;
        this.consumeMaterialRepository = consumeMaterialRepository;
        this.consumeMaterialQueryService = consumeMaterialQueryService;
    }

    /**
     * {@code POST  /consume-materials} : Create a new consumeMaterial.
     *
     * @param consumeMaterialDTO the consumeMaterialDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new consumeMaterialDTO, or with status {@code 400 (Bad Request)} if the consumeMaterial has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/consume-materials")
    public ResponseEntity<ConsumeMaterialDTO> createConsumeMaterial(@Valid @RequestBody ConsumeMaterialDTO consumeMaterialDTO)
        throws URISyntaxException {
        log.debug("REST request to save ConsumeMaterial : {}", consumeMaterialDTO);
        if (consumeMaterialDTO.getId() != null) {
            throw new BadRequestAlertException("A new consumeMaterial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConsumeMaterialDTO result = consumeMaterialService.save(consumeMaterialDTO);
        return ResponseEntity
            .created(new URI("/api/consume-materials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /consume-materials/:id} : Updates an existing consumeMaterial.
     *
     * @param id the id of the consumeMaterialDTO to save.
     * @param consumeMaterialDTO the consumeMaterialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consumeMaterialDTO,
     * or with status {@code 400 (Bad Request)} if the consumeMaterialDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the consumeMaterialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/consume-materials/{id}")
    public ResponseEntity<ConsumeMaterialDTO> updateConsumeMaterial(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ConsumeMaterialDTO consumeMaterialDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ConsumeMaterial : {}, {}", id, consumeMaterialDTO);
        if (consumeMaterialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consumeMaterialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consumeMaterialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConsumeMaterialDTO result = consumeMaterialService.save(consumeMaterialDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consumeMaterialDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /consume-materials/:id} : Partial updates given fields of an existing consumeMaterial, field will ignore if it is null
     *
     * @param id the id of the consumeMaterialDTO to save.
     * @param consumeMaterialDTO the consumeMaterialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consumeMaterialDTO,
     * or with status {@code 400 (Bad Request)} if the consumeMaterialDTO is not valid,
     * or with status {@code 404 (Not Found)} if the consumeMaterialDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the consumeMaterialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/consume-materials/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ConsumeMaterialDTO> partialUpdateConsumeMaterial(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ConsumeMaterialDTO consumeMaterialDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ConsumeMaterial partially : {}, {}", id, consumeMaterialDTO);
        if (consumeMaterialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consumeMaterialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consumeMaterialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConsumeMaterialDTO> result = consumeMaterialService.partialUpdate(consumeMaterialDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consumeMaterialDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /consume-materials} : get all the consumeMaterials.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of consumeMaterials in body.
     */
    @GetMapping("/consume-materials")
    public ResponseEntity<List<ConsumeMaterialDTO>> getAllConsumeMaterials(ConsumeMaterialCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ConsumeMaterials by criteria: {}", criteria);
        Page<ConsumeMaterialDTO> page = consumeMaterialQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /consume-materials/count} : count all the consumeMaterials.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/consume-materials/count")
    public ResponseEntity<Long> countConsumeMaterials(ConsumeMaterialCriteria criteria) {
        log.debug("REST request to count ConsumeMaterials by criteria: {}", criteria);
        return ResponseEntity.ok().body(consumeMaterialQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /consume-materials/:id} : get the "id" consumeMaterial.
     *
     * @param id the id of the consumeMaterialDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the consumeMaterialDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/consume-materials/{id}")
    public ResponseEntity<ConsumeMaterialDTO> getConsumeMaterial(@PathVariable Long id) {
        log.debug("REST request to get ConsumeMaterial : {}", id);
        Optional<ConsumeMaterialDTO> consumeMaterialDTO = consumeMaterialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(consumeMaterialDTO);
    }

    /**
     * {@code DELETE  /consume-materials/:id} : delete the "id" consumeMaterial.
     *
     * @param id the id of the consumeMaterialDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/consume-materials/{id}")
    public ResponseEntity<Void> deleteConsumeMaterial(@PathVariable Long id) {
        log.debug("REST request to delete ConsumeMaterial : {}", id);
        consumeMaterialService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
