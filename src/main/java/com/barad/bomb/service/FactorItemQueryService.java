package com.barad.bomb.service;

import com.barad.bomb.domain.*; // for static metamodels
import com.barad.bomb.domain.FactorItemEntity;
import com.barad.bomb.repository.FactorItemRepository;
import com.barad.bomb.service.criteria.FactorItemCriteria;
import com.barad.bomb.service.dto.FactorItemDTO;
import com.barad.bomb.service.mapper.FactorItemMapper;
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
 * Service for executing complex queries for {@link FactorItemEntity} entities in the database.
 * The main input is a {@link FactorItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FactorItemDTO} or a {@link Page} of {@link FactorItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FactorItemQueryService extends QueryService<FactorItemEntity> {

    private final Logger log = LoggerFactory.getLogger(FactorItemQueryService.class);

    private final FactorItemRepository factorItemRepository;

    private final FactorItemMapper factorItemMapper;

    public FactorItemQueryService(FactorItemRepository factorItemRepository, FactorItemMapper factorItemMapper) {
        this.factorItemRepository = factorItemRepository;
        this.factorItemMapper = factorItemMapper;
    }

    /**
     * Return a {@link List} of {@link FactorItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FactorItemDTO> findByCriteria(FactorItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FactorItemEntity> specification = createSpecification(criteria);
        return factorItemMapper.toDto(factorItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FactorItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FactorItemDTO> findByCriteria(FactorItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FactorItemEntity> specification = createSpecification(criteria);
        return factorItemRepository.findAll(specification, page).map(factorItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FactorItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FactorItemEntity> specification = createSpecification(criteria);
        return factorItemRepository.count(specification);
    }

    /**
     * Function to convert {@link FactorItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FactorItemEntity> createSpecification(FactorItemCriteria criteria) {
        Specification<FactorItemEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FactorItemEntity_.id));
            }
            if (criteria.getRowNum() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRowNum(), FactorItemEntity_.rowNum));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), FactorItemEntity_.title));
            }
            if (criteria.getCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCount(), FactorItemEntity_.count));
            }
            if (criteria.getDiscount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscount(), FactorItemEntity_.discount));
            }
            if (criteria.getTax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTax(), FactorItemEntity_.tax));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), FactorItemEntity_.description));
            }
            if (criteria.getFoodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFoodId(),
                            root -> root.join(FactorItemEntity_.food, JoinType.LEFT).get(FoodEntity_.id)
                        )
                    );
            }
            if (criteria.getFactorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFactorId(),
                            root -> root.join(FactorItemEntity_.factor, JoinType.LEFT).get(FactorEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
