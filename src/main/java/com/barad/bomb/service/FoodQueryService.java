package com.barad.bomb.service;

import com.barad.bomb.domain.*; // for static metamodels
import com.barad.bomb.domain.FoodEntity;
import com.barad.bomb.repository.FoodRepository;
import com.barad.bomb.service.criteria.FoodCriteria;
import com.barad.bomb.service.dto.FoodDTO;
import com.barad.bomb.service.mapper.FoodMapper;
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
 * Service for executing complex queries for {@link FoodEntity} entities in the database.
 * The main input is a {@link FoodCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FoodDTO} or a {@link Page} of {@link FoodDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FoodQueryService extends QueryService<FoodEntity> {

    private final Logger log = LoggerFactory.getLogger(FoodQueryService.class);

    private final FoodRepository foodRepository;

    private final FoodMapper foodMapper;

    public FoodQueryService(FoodRepository foodRepository, FoodMapper foodMapper) {
        this.foodRepository = foodRepository;
        this.foodMapper = foodMapper;
    }

    /**
     * Return a {@link List} of {@link FoodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FoodDTO> findByCriteria(FoodCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FoodEntity> specification = createSpecification(criteria);
        return foodMapper.toDto(foodRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FoodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FoodDTO> findByCriteria(FoodCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FoodEntity> specification = createSpecification(criteria);
        return foodRepository.findAll(specification, page).map(foodMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FoodCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FoodEntity> specification = createSpecification(criteria);
        return foodRepository.count(specification);
    }

    /**
     * Function to convert {@link FoodCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FoodEntity> createSpecification(FoodCriteria criteria) {
        Specification<FoodEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FoodEntity_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), FoodEntity_.title));
            }
            if (criteria.getFoodCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFoodCode(), FoodEntity_.foodCode));
            }
            if (criteria.getSizeClassId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSizeClassId(), FoodEntity_.sizeClassId));
            }
            if (criteria.getCategoryClassId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCategoryClassId(), FoodEntity_.categoryClassId));
            }
            if (criteria.getLastPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastPrice(), FoodEntity_.lastPrice));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), FoodEntity_.description));
            }
            if (criteria.getMenuItemsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMenuItemsId(),
                            root -> root.join(FoodEntity_.menuItems, JoinType.LEFT).get(MenuItemEntity_.id)
                        )
                    );
            }
            if (criteria.getFactorItemsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFactorItemsId(),
                            root -> root.join(FoodEntity_.factorItems, JoinType.LEFT).get(FactorItemEntity_.id)
                        )
                    );
            }
            if (criteria.getMaterialsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMaterialsId(),
                            root -> root.join(FoodEntity_.materials, JoinType.LEFT).get(ConsumeMaterialEntity_.id)
                        )
                    );
            }
            if (criteria.getProducerPartyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProducerPartyId(),
                            root -> root.join(FoodEntity_.producerParty, JoinType.LEFT).get(PartyEntity_.id)
                        )
                    );
            }
            if (criteria.getDesignerPartyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDesignerPartyId(),
                            root -> root.join(FoodEntity_.designerParty, JoinType.LEFT).get(PartyEntity_.id)
                        )
                    );
            }
            if (criteria.getFoodTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFoodTypeId(),
                            root -> root.join(FoodEntity_.foodType, JoinType.LEFT).get(FoodTypeEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
