package com.barad.bomb.web.rest;

import com.barad.bomb.repository.ClassTypeRepository;
import com.barad.bomb.service.ClassTypeQueryService;
import com.barad.bomb.service.ClassTypeService;
import com.barad.bomb.service.criteria.ClassTypeCriteria;
import com.barad.bomb.service.dto.ClassTypeDTO;
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
 * REST controller for managing {@link com.barad.bomb.domain.ClassTypeEntity}.
 */
@RestController
@RequestMapping("/api")
public class ClassTypeResource {

    private final Logger log = LoggerFactory.getLogger(ClassTypeResource.class);

    private static final String ENTITY_NAME = "classType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassTypeService classTypeService;

    private final ClassTypeRepository classTypeRepository;

    private final ClassTypeQueryService classTypeQueryService;

    public ClassTypeResource(
        ClassTypeService classTypeService,
        ClassTypeRepository classTypeRepository,
        ClassTypeQueryService classTypeQueryService
    ) {
        this.classTypeService = classTypeService;
        this.classTypeRepository = classTypeRepository;
        this.classTypeQueryService = classTypeQueryService;
    }

    /**
     * {@code POST  /class-types} : Create a new classType.
     *
     * @param classTypeDTO the classTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classTypeDTO, or with status {@code 400 (Bad Request)} if the classType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/class-types")
    public ResponseEntity<ClassTypeDTO> createClassType(@Valid @RequestBody ClassTypeDTO classTypeDTO) throws URISyntaxException {
        log.debug("REST request to save ClassType : {}", classTypeDTO);
        if (classTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new classType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClassTypeDTO result = classTypeService.save(classTypeDTO);
        return ResponseEntity
            .created(new URI("/api/class-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /class-types/:id} : Updates an existing classType.
     *
     * @param id the id of the classTypeDTO to save.
     * @param classTypeDTO the classTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classTypeDTO,
     * or with status {@code 400 (Bad Request)} if the classTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/class-types/{id}")
    public ResponseEntity<ClassTypeDTO> updateClassType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClassTypeDTO classTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ClassType : {}, {}", id, classTypeDTO);
        if (classTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ClassTypeDTO result = classTypeService.save(classTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /class-types/:id} : Partial updates given fields of an existing classType, field will ignore if it is null
     *
     * @param id the id of the classTypeDTO to save.
     * @param classTypeDTO the classTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classTypeDTO,
     * or with status {@code 400 (Bad Request)} if the classTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the classTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the classTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/class-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ClassTypeDTO> partialUpdateClassType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClassTypeDTO classTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ClassType partially : {}, {}", id, classTypeDTO);
        if (classTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClassTypeDTO> result = classTypeService.partialUpdate(classTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /class-types} : get all the classTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classTypes in body.
     */
    @GetMapping("/class-types")
    public ResponseEntity<List<ClassTypeDTO>> getAllClassTypes(ClassTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ClassTypes by criteria: {}", criteria);
        Page<ClassTypeDTO> page = classTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /class-types/count} : count all the classTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/class-types/count")
    public ResponseEntity<Long> countClassTypes(ClassTypeCriteria criteria) {
        log.debug("REST request to count ClassTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(classTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /class-types/:id} : get the "id" classType.
     *
     * @param id the id of the classTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/class-types/{id}")
    public ResponseEntity<ClassTypeDTO> getClassType(@PathVariable Long id) {
        log.debug("REST request to get ClassType : {}", id);
        Optional<ClassTypeDTO> classTypeDTO = classTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classTypeDTO);
    }

    /**
     * {@code DELETE  /class-types/:id} : delete the "id" classType.
     *
     * @param id the id of the classTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/class-types/{id}")
    public ResponseEntity<Void> deleteClassType(@PathVariable Long id) {
        log.debug("REST request to delete ClassType : {}", id);
        classTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
