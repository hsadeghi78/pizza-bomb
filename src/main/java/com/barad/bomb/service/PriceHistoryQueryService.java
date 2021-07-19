package com.barad.bomb.service;

import com.barad.bomb.domain.*; // for static metamodels
import com.barad.bomb.domain.PriceHistoryEntity;
import com.barad.bomb.repository.PriceHistoryRepository;
import com.barad.bomb.service.criteria.PriceHistoryCriteria;
import com.barad.bomb.service.dto.PriceHistoryDTO;
import com.barad.bomb.service.mapper.PriceHistoryMapper;
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
 * Service for executing complex queries for {@link PriceHistoryEntity} entities in the database.
 * The main input is a {@link PriceHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PriceHistoryDTO} or a {@link Page} of {@link PriceHistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PriceHistoryQueryService extends QueryService<PriceHistoryEntity> {

    private final Logger log = LoggerFactory.getLogger(PriceHistoryQueryService.class);

    private final PriceHistoryRepository priceHistoryRepository;

    private final PriceHistoryMapper priceHistoryMapper;

    public PriceHistoryQueryService(PriceHistoryRepository priceHistoryRepository, PriceHistoryMapper priceHistoryMapper) {
        this.priceHistoryRepository = priceHistoryRepository;
        this.priceHistoryMapper = priceHistoryMapper;
    }

    /**
     * Return a {@link List} of {@link PriceHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PriceHistoryDTO> findByCriteria(PriceHistoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PriceHistoryEntity> specification = createSpecification(criteria);
        return priceHistoryMapper.toDto(priceHistoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PriceHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PriceHistoryDTO> findByCriteria(PriceHistoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PriceHistoryEntity> specification = createSpecification(criteria);
        return priceHistoryRepository.findAll(specification, page).map(priceHistoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PriceHistoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PriceHistoryEntity> specification = createSpecification(criteria);
        return priceHistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link PriceHistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PriceHistoryEntity> createSpecification(PriceHistoryCriteria criteria) {
        Specification<PriceHistoryEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PriceHistoryEntity_.id));
            }
            if (criteria.getFoodId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFoodId(), PriceHistoryEntity_.foodId));
            }
            if (criteria.getMaterialId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaterialId(), PriceHistoryEntity_.materialId));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), PriceHistoryEntity_.price));
            }
        }
        return specification;
    }
}
