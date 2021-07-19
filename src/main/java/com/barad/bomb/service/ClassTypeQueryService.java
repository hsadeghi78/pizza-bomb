package com.barad.bomb.service;

import com.barad.bomb.domain.*; // for static metamodels
import com.barad.bomb.domain.ClassTypeEntity;
import com.barad.bomb.repository.ClassTypeRepository;
import com.barad.bomb.service.criteria.ClassTypeCriteria;
import com.barad.bomb.service.dto.ClassTypeDTO;
import com.barad.bomb.service.mapper.ClassTypeMapper;
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
 * Service for executing complex queries for {@link ClassTypeEntity} entities in the database.
 * The main input is a {@link ClassTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClassTypeDTO} or a {@link Page} of {@link ClassTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassTypeQueryService extends QueryService<ClassTypeEntity> {

    private final Logger log = LoggerFactory.getLogger(ClassTypeQueryService.class);

    private final ClassTypeRepository classTypeRepository;

    private final ClassTypeMapper classTypeMapper;

    public ClassTypeQueryService(ClassTypeRepository classTypeRepository, ClassTypeMapper classTypeMapper) {
        this.classTypeRepository = classTypeRepository;
        this.classTypeMapper = classTypeMapper;
    }

    /**
     * Return a {@link List} of {@link ClassTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClassTypeDTO> findByCriteria(ClassTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ClassTypeEntity> specification = createSpecification(criteria);
        return classTypeMapper.toDto(classTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ClassTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassTypeDTO> findByCriteria(ClassTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ClassTypeEntity> specification = createSpecification(criteria);
        return classTypeRepository.findAll(specification, page).map(classTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClassTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ClassTypeEntity> specification = createSpecification(criteria);
        return classTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ClassTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ClassTypeEntity> createSpecification(ClassTypeCriteria criteria) {
        Specification<ClassTypeEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ClassTypeEntity_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), ClassTypeEntity_.title));
            }
            if (criteria.getTypeCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTypeCode(), ClassTypeEntity_.typeCode));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ClassTypeEntity_.description));
            }
            if (criteria.getClassificationsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getClassificationsId(),
                            root -> root.join(ClassTypeEntity_.classifications, JoinType.LEFT).get(ClassificationEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
