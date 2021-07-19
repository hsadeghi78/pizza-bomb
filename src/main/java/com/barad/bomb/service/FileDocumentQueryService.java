package com.barad.bomb.service;

import com.barad.bomb.domain.*; // for static metamodels
import com.barad.bomb.domain.FileDocumentEntity;
import com.barad.bomb.repository.FileDocumentRepository;
import com.barad.bomb.service.criteria.FileDocumentCriteria;
import com.barad.bomb.service.dto.FileDocumentDTO;
import com.barad.bomb.service.mapper.FileDocumentMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FileDocumentEntity} entities in the database.
 * The main input is a {@link FileDocumentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FileDocumentDTO} or a {@link Page} of {@link FileDocumentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FileDocumentQueryService extends QueryService<FileDocumentEntity> {

    private final Logger log = LoggerFactory.getLogger(FileDocumentQueryService.class);

    private final FileDocumentRepository fileDocumentRepository;

    private final FileDocumentMapper fileDocumentMapper;

    public FileDocumentQueryService(FileDocumentRepository fileDocumentRepository, FileDocumentMapper fileDocumentMapper) {
        this.fileDocumentRepository = fileDocumentRepository;
        this.fileDocumentMapper = fileDocumentMapper;
    }

    /**
     * Return a {@link List} of {@link FileDocumentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FileDocumentDTO> findByCriteria(FileDocumentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FileDocumentEntity> specification = createSpecification(criteria);
        return fileDocumentMapper.toDto(fileDocumentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FileDocumentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FileDocumentDTO> findByCriteria(FileDocumentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FileDocumentEntity> specification = createSpecification(criteria);
        return fileDocumentRepository.findAll(specification, page).map(fileDocumentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FileDocumentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FileDocumentEntity> specification = createSpecification(criteria);
        return fileDocumentRepository.count(specification);
    }

    /**
     * Function to convert {@link FileDocumentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FileDocumentEntity> createSpecification(FileDocumentCriteria criteria) {
        Specification<FileDocumentEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FileDocumentEntity_.id));
            }
            if (criteria.getFileName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileName(), FileDocumentEntity_.fileName));
            }
            if (criteria.getFilePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFilePath(), FileDocumentEntity_.filePath));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), FileDocumentEntity_.description));
            }
            if (criteria.getPartyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPartyId(),
                            root -> root.join(FileDocumentEntity_.party, JoinType.LEFT).get(PartyEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
