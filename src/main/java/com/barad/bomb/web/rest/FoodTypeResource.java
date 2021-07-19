package com.barad.bomb.web.rest;

import com.barad.bomb.repository.FoodTypeRepository;
import com.barad.bomb.service.FoodTypeQueryService;
import com.barad.bomb.service.FoodTypeService;
import com.barad.bomb.service.criteria.FoodTypeCriteria;
import com.barad.bomb.service.dto.FoodTypeDTO;
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
 * REST controller for managing {@link com.barad.bomb.domain.FoodTypeEntity}.
 */
@RestController
@RequestMapping("/api")
public class FoodTypeResource {

    private final Logger log = LoggerFactory.getLogger(FoodTypeResource.class);

    private static final String ENTITY_NAME = "foodType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FoodTypeService foodTypeService;

    private final FoodTypeRepository foodTypeRepository;

    private final FoodTypeQueryService foodTypeQueryService;

    public FoodTypeResource(
        FoodTypeService foodTypeService,
        FoodTypeRepository foodTypeRepository,
        FoodTypeQueryService foodTypeQueryService
    ) {
        this.foodTypeService = foodTypeService;
        this.foodTypeRepository = foodTypeRepository;
        this.foodTypeQueryService = foodTypeQueryService;
    }

    /**
     * {@code POST  /food-types} : Create a new foodType.
     *
     * @param foodTypeDTO the foodTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new foodTypeDTO, or with status {@code 400 (Bad Request)} if the foodType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/food-types")
    public ResponseEntity<FoodTypeDTO> createFoodType(@Valid @RequestBody FoodTypeDTO foodTypeDTO) throws URISyntaxException {
        log.debug("REST request to save FoodType : {}", foodTypeDTO);
        if (foodTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new foodType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FoodTypeDTO result = foodTypeService.save(foodTypeDTO);
        return ResponseEntity
            .created(new URI("/api/food-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /food-types/:id} : Updates an existing foodType.
     *
     * @param id the id of the foodTypeDTO to save.
     * @param foodTypeDTO the foodTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated foodTypeDTO,
     * or with status {@code 400 (Bad Request)} if the foodTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the foodTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/food-types/{id}")
    public ResponseEntity<FoodTypeDTO> updateFoodType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FoodTypeDTO foodTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FoodType : {}, {}", id, foodTypeDTO);
        if (foodTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foodTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!foodTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FoodTypeDTO result = foodTypeService.save(foodTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, foodTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /food-types/:id} : Partial updates given fields of an existing foodType, field will ignore if it is null
     *
     * @param id the id of the foodTypeDTO to save.
     * @param foodTypeDTO the foodTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated foodTypeDTO,
     * or with status {@code 400 (Bad Request)} if the foodTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the foodTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the foodTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/food-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FoodTypeDTO> partialUpdateFoodType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FoodTypeDTO foodTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FoodType partially : {}, {}", id, foodTypeDTO);
        if (foodTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, foodTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!foodTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FoodTypeDTO> result = foodTypeService.partialUpdate(foodTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, foodTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /food-types} : get all the foodTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of foodTypes in body.
     */
    @GetMapping("/food-types")
    public ResponseEntity<List<FoodTypeDTO>> getAllFoodTypes(FoodTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FoodTypes by criteria: {}", criteria);
        Page<FoodTypeDTO> page = foodTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /food-types/count} : count all the foodTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/food-types/count")
    public ResponseEntity<Long> countFoodTypes(FoodTypeCriteria criteria) {
        log.debug("REST request to count FoodTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(foodTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /food-types/:id} : get the "id" foodType.
     *
     * @param id the id of the foodTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the foodTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/food-types/{id}")
    public ResponseEntity<FoodTypeDTO> getFoodType(@PathVariable Long id) {
        log.debug("REST request to get FoodType : {}", id);
        Optional<FoodTypeDTO> foodTypeDTO = foodTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(foodTypeDTO);
    }

    /**
     * {@code DELETE  /food-types/:id} : delete the "id" foodType.
     *
     * @param id the id of the foodTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/food-types/{id}")
    public ResponseEntity<Void> deleteFoodType(@PathVariable Long id) {
        log.debug("REST request to delete FoodType : {}", id);
        foodTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
