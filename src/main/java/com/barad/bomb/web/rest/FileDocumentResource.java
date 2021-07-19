package com.barad.bomb.web.rest;

import com.barad.bomb.repository.FileDocumentRepository;
import com.barad.bomb.service.FileDocumentQueryService;
import com.barad.bomb.service.FileDocumentService;
import com.barad.bomb.service.criteria.FileDocumentCriteria;
import com.barad.bomb.service.dto.FileDocumentDTO;
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
 * REST controller for managing {@link com.barad.bomb.domain.FileDocumentEntity}.
 */
@RestController
@RequestMapping("/api")
public class FileDocumentResource {

    private final Logger log = LoggerFactory.getLogger(FileDocumentResource.class);

    private static final String ENTITY_NAME = "fileDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileDocumentService fileDocumentService;

    private final FileDocumentRepository fileDocumentRepository;

    private final FileDocumentQueryService fileDocumentQueryService;

    public FileDocumentResource(
        FileDocumentService fileDocumentService,
        FileDocumentRepository fileDocumentRepository,
        FileDocumentQueryService fileDocumentQueryService
    ) {
        this.fileDocumentService = fileDocumentService;
        this.fileDocumentRepository = fileDocumentRepository;
        this.fileDocumentQueryService = fileDocumentQueryService;
    }

    /**
     * {@code POST  /file-documents} : Create a new fileDocument.
     *
     * @param fileDocumentDTO the fileDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileDocumentDTO, or with status {@code 400 (Bad Request)} if the fileDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/file-documents")
    public ResponseEntity<FileDocumentDTO> createFileDocument(@Valid @RequestBody FileDocumentDTO fileDocumentDTO)
        throws URISyntaxException {
        log.debug("REST request to save FileDocument : {}", fileDocumentDTO);
        if (fileDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new fileDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileDocumentDTO result = fileDocumentService.save(fileDocumentDTO);
        return ResponseEntity
            .created(new URI("/api/file-documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /file-documents/:id} : Updates an existing fileDocument.
     *
     * @param id the id of the fileDocumentDTO to save.
     * @param fileDocumentDTO the fileDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the fileDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/file-documents/{id}")
    public ResponseEntity<FileDocumentDTO> updateFileDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FileDocumentDTO fileDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FileDocument : {}, {}", id, fileDocumentDTO);
        if (fileDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FileDocumentDTO result = fileDocumentService.save(fileDocumentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileDocumentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /file-documents/:id} : Partial updates given fields of an existing fileDocument, field will ignore if it is null
     *
     * @param id the id of the fileDocumentDTO to save.
     * @param fileDocumentDTO the fileDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the fileDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fileDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fileDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/file-documents/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FileDocumentDTO> partialUpdateFileDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FileDocumentDTO fileDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FileDocument partially : {}, {}", id, fileDocumentDTO);
        if (fileDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FileDocumentDTO> result = fileDocumentService.partialUpdate(fileDocumentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileDocumentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /file-documents} : get all the fileDocuments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileDocuments in body.
     */
    @GetMapping("/file-documents")
    public ResponseEntity<List<FileDocumentDTO>> getAllFileDocuments(FileDocumentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FileDocuments by criteria: {}", criteria);
        Page<FileDocumentDTO> page = fileDocumentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /file-documents/count} : count all the fileDocuments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/file-documents/count")
    public ResponseEntity<Long> countFileDocuments(FileDocumentCriteria criteria) {
        log.debug("REST request to count FileDocuments by criteria: {}", criteria);
        return ResponseEntity.ok().body(fileDocumentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /file-documents/:id} : get the "id" fileDocument.
     *
     * @param id the id of the fileDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/file-documents/{id}")
    public ResponseEntity<FileDocumentDTO> getFileDocument(@PathVariable Long id) {
        log.debug("REST request to get FileDocument : {}", id);
        Optional<FileDocumentDTO> fileDocumentDTO = fileDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fileDocumentDTO);
    }

    /**
     * {@code DELETE  /file-documents/:id} : delete the "id" fileDocument.
     *
     * @param id the id of the fileDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/file-documents/{id}")
    public ResponseEntity<Void> deleteFileDocument(@PathVariable Long id) {
        log.debug("REST request to delete FileDocument : {}", id);
        fileDocumentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
