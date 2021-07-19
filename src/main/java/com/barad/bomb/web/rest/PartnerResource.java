package com.barad.bomb.web.rest;

import com.barad.bomb.repository.PartnerRepository;
import com.barad.bomb.service.PartnerQueryService;
import com.barad.bomb.service.PartnerService;
import com.barad.bomb.service.criteria.PartnerCriteria;
import com.barad.bomb.service.dto.PartnerDTO;
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
 * REST controller for managing {@link com.barad.bomb.domain.PartnerEntity}.
 */
@RestController
@RequestMapping("/api")
public class PartnerResource {

    private final Logger log = LoggerFactory.getLogger(PartnerResource.class);

    private static final String ENTITY_NAME = "partner";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartnerService partnerService;

    private final PartnerRepository partnerRepository;

    private final PartnerQueryService partnerQueryService;

    public PartnerResource(PartnerService partnerService, PartnerRepository partnerRepository, PartnerQueryService partnerQueryService) {
        this.partnerService = partnerService;
        this.partnerRepository = partnerRepository;
        this.partnerQueryService = partnerQueryService;
    }

    /**
     * {@code POST  /partners} : Create a new partner.
     *
     * @param partnerDTO the partnerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partnerDTO, or with status {@code 400 (Bad Request)} if the partner has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/partners")
    public ResponseEntity<PartnerDTO> createPartner(@Valid @RequestBody PartnerDTO partnerDTO) throws URISyntaxException {
        log.debug("REST request to save Partner : {}", partnerDTO);
        if (partnerDTO.getId() != null) {
            throw new BadRequestAlertException("A new partner cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PartnerDTO result = partnerService.save(partnerDTO);
        return ResponseEntity
            .created(new URI("/api/partners/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /partners/:id} : Updates an existing partner.
     *
     * @param id the id of the partnerDTO to save.
     * @param partnerDTO the partnerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partnerDTO,
     * or with status {@code 400 (Bad Request)} if the partnerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partnerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/partners/{id}")
    public ResponseEntity<PartnerDTO> updatePartner(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PartnerDTO partnerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Partner : {}, {}", id, partnerDTO);
        if (partnerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partnerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partnerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PartnerDTO result = partnerService.save(partnerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partnerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /partners/:id} : Partial updates given fields of an existing partner, field will ignore if it is null
     *
     * @param id the id of the partnerDTO to save.
     * @param partnerDTO the partnerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partnerDTO,
     * or with status {@code 400 (Bad Request)} if the partnerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the partnerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the partnerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/partners/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PartnerDTO> partialUpdatePartner(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PartnerDTO partnerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Partner partially : {}, {}", id, partnerDTO);
        if (partnerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partnerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partnerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartnerDTO> result = partnerService.partialUpdate(partnerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partnerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /partners} : get all the partners.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of partners in body.
     */
    @GetMapping("/partners")
    public ResponseEntity<List<PartnerDTO>> getAllPartners(PartnerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Partners by criteria: {}", criteria);
        Page<PartnerDTO> page = partnerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /partners/count} : count all the partners.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/partners/count")
    public ResponseEntity<Long> countPartners(PartnerCriteria criteria) {
        log.debug("REST request to count Partners by criteria: {}", criteria);
        return ResponseEntity.ok().body(partnerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /partners/:id} : get the "id" partner.
     *
     * @param id the id of the partnerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partnerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/partners/{id}")
    public ResponseEntity<PartnerDTO> getPartner(@PathVariable Long id) {
        log.debug("REST request to get Partner : {}", id);
        Optional<PartnerDTO> partnerDTO = partnerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partnerDTO);
    }

    /**
     * {@code DELETE  /partners/:id} : delete the "id" partner.
     *
     * @param id the id of the partnerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/partners/{id}")
    public ResponseEntity<Void> deletePartner(@PathVariable Long id) {
        log.debug("REST request to delete Partner : {}", id);
        partnerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
