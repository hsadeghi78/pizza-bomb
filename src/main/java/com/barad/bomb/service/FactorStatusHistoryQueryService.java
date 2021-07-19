package com.barad.bomb.service;

import com.barad.bomb.domain.*; // for static metamodels
import com.barad.bomb.domain.FactorStatusHistoryEntity;
import com.barad.bomb.repository.FactorStatusHistoryRepository;
import com.barad.bomb.service.criteria.FactorStatusHistoryCriteria;
import com.barad.bomb.service.dto.FactorStatusHistoryDTO;
import com.barad.bomb.service.mapper.FactorStatusHistoryMapper;
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
 * Service for executing complex queries for {@link FactorStatusHistoryEntity} entities in the database.
 * The main input is a {@link FactorStatusHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FactorStatusHistoryDTO} or a {@link Page} of {@link FactorStatusHistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FactorStatusHistoryQueryService extends QueryService<FactorStatusHistoryEntity> {

    private final Logger log = LoggerFactory.getLogger(FactorStatusHistoryQueryService.class);

    private final FactorStatusHistoryRepository factorStatusHistoryRepository;

    private final FactorStatusHistoryMapper factorStatusHistoryMapper;

    public FactorStatusHistoryQueryService(
        FactorStatusHistoryRepository factorStatusHistoryRepository,
        FactorStatusHistoryMapper factorStatusHistoryMapper
    ) {
        this.factorStatusHistoryRepository = factorStatusHistoryRepository;
        this.factorStatusHistoryMapper = factorStatusHistoryMapper;
    }

    /**
     * Return a {@link List} of {@link FactorStatusHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FactorStatusHistoryDTO> findByCriteria(FactorStatusHistoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FactorStatusHistoryEntity> specification = createSpecification(criteria);
        return factorStatusHistoryMapper.toDto(factorStatusHistoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FactorStatusHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FactorStatusHistoryDTO> findByCriteria(FactorStatusHistoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FactorStatusHistoryEntity> specification = createSpecification(criteria);
        return factorStatusHistoryRepository.findAll(specification, page).map(factorStatusHistoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FactorStatusHistoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FactorStatusHistoryEntity> specification = createSpecification(criteria);
        return factorStatusHistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link FactorStatusHistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FactorStatusHistoryEntity> createSpecification(FactorStatusHistoryCriteria criteria) {
        Specification<FactorStatusHistoryEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FactorStatusHistoryEntity_.id));
            }
            if (criteria.getFactorId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFactorId(), FactorStatusHistoryEntity_.factorId));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), FactorStatusHistoryEntity_.status));
            }
        }
        return specification;
    }
}
