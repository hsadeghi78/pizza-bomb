package com.barad.bomb.service;

import com.barad.bomb.domain.*; // for static metamodels
import com.barad.bomb.domain.ClassificationEntity;
import com.barad.bomb.repository.ClassificationRepository;
import com.barad.bomb.service.criteria.ClassificationCriteria;
import com.barad.bomb.service.dto.ClassificationDTO;
import com.barad.bomb.service.mapper.ClassificationMapper;
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
 * Service for executing complex queries for {@link ClassificationEntity} entities in the database.
 * The main input is a {@link ClassificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClassificationDTO} or a {@link Page} of {@link ClassificationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassificationQueryService extends QueryService<ClassificationEntity> {

    private final Logger log = LoggerFactory.getLogger(ClassificationQueryService.class);

    private final ClassificationRepository classificationRepository;

    private final ClassificationMapper classificationMapper;

    public ClassificationQueryService(ClassificationRepository classificationRepository, ClassificationMapper classificationMapper) {
        this.classificationRepository = classificationRepository;
        this.classificationMapper = classificationMapper;
    }

    /**
     * Return a {@link List} of {@link ClassificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClassificationDTO> findByCriteria(ClassificationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ClassificationEntity> specification = createSpecification(criteria);
        return classificationMapper.toDto(classificationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ClassificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassificationDTO> findByCriteria(ClassificationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ClassificationEntity> specification = createSpecification(criteria);
        return classificationRepository.findAll(specification, page).map(classificationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClassificationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ClassificationEntity> specification = createSpecification(criteria);
        return classificationRepository.count(specification);
    }

    /**
     * Function to convert {@link ClassificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ClassificationEntity> createSpecification(ClassificationCriteria criteria) {
        Specification<ClassificationEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ClassificationEntity_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), ClassificationEntity_.title));
            }
            if (criteria.getClassCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getClassCode(), ClassificationEntity_.classCode));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ClassificationEntity_.description));
            }
            if (criteria.getClassTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getClassTypeId(),
                            root -> root.join(ClassificationEntity_.classType, JoinType.LEFT).get(ClassTypeEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
