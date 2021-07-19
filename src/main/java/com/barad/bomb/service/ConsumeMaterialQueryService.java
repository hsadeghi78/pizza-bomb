package com.barad.bomb.service;

import com.barad.bomb.domain.*; // for static metamodels
import com.barad.bomb.domain.ConsumeMaterialEntity;
import com.barad.bomb.repository.ConsumeMaterialRepository;
import com.barad.bomb.service.criteria.ConsumeMaterialCriteria;
import com.barad.bomb.service.dto.ConsumeMaterialDTO;
import com.barad.bomb.service.mapper.ConsumeMaterialMapper;
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
 * Service for executing complex queries for {@link ConsumeMaterialEntity} entities in the database.
 * The main input is a {@link ConsumeMaterialCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ConsumeMaterialDTO} or a {@link Page} of {@link ConsumeMaterialDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConsumeMaterialQueryService extends QueryService<ConsumeMaterialEntity> {

    private final Logger log = LoggerFactory.getLogger(ConsumeMaterialQueryService.class);

    private final ConsumeMaterialRepository consumeMaterialRepository;

    private final ConsumeMaterialMapper consumeMaterialMapper;

    public ConsumeMaterialQueryService(ConsumeMaterialRepository consumeMaterialRepository, ConsumeMaterialMapper consumeMaterialMapper) {
        this.consumeMaterialRepository = consumeMaterialRepository;
        this.consumeMaterialMapper = consumeMaterialMapper;
    }

    /**
     * Return a {@link List} of {@link ConsumeMaterialDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ConsumeMaterialDTO> findByCriteria(ConsumeMaterialCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ConsumeMaterialEntity> specification = createSpecification(criteria);
        return consumeMaterialMapper.toDto(consumeMaterialRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ConsumeMaterialDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConsumeMaterialDTO> findByCriteria(ConsumeMaterialCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ConsumeMaterialEntity> specification = createSpecification(criteria);
        return consumeMaterialRepository.findAll(specification, page).map(consumeMaterialMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConsumeMaterialCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ConsumeMaterialEntity> specification = createSpecification(criteria);
        return consumeMaterialRepository.count(specification);
    }

    /**
     * Function to convert {@link ConsumeMaterialCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ConsumeMaterialEntity> createSpecification(ConsumeMaterialCriteria criteria) {
        Specification<ConsumeMaterialEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ConsumeMaterialEntity_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), ConsumeMaterialEntity_.title));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), ConsumeMaterialEntity_.type));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), ConsumeMaterialEntity_.amount));
            }
            if (criteria.getAmountUnitClassId() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAmountUnitClassId(), ConsumeMaterialEntity_.amountUnitClassId));
            }
            if (criteria.getFoodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFoodId(),
                            root -> root.join(ConsumeMaterialEntity_.food, JoinType.LEFT).get(FoodEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
