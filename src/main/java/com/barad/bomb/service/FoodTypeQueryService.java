package com.barad.bomb.service;

import com.barad.bomb.domain.*; // for static metamodels
import com.barad.bomb.domain.FoodTypeEntity;
import com.barad.bomb.repository.FoodTypeRepository;
import com.barad.bomb.service.criteria.FoodTypeCriteria;
import com.barad.bomb.service.dto.FoodTypeDTO;
import com.barad.bomb.service.mapper.FoodTypeMapper;
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
 * Service for executing complex queries for {@link FoodTypeEntity} entities in the database.
 * The main input is a {@link FoodTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FoodTypeDTO} or a {@link Page} of {@link FoodTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FoodTypeQueryService extends QueryService<FoodTypeEntity> {

    private final Logger log = LoggerFactory.getLogger(FoodTypeQueryService.class);

    private final FoodTypeRepository foodTypeRepository;

    private final FoodTypeMapper foodTypeMapper;

    public FoodTypeQueryService(FoodTypeRepository foodTypeRepository, FoodTypeMapper foodTypeMapper) {
        this.foodTypeRepository = foodTypeRepository;
        this.foodTypeMapper = foodTypeMapper;
    }

    /**
     * Return a {@link List} of {@link FoodTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FoodTypeDTO> findByCriteria(FoodTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FoodTypeEntity> specification = createSpecification(criteria);
        return foodTypeMapper.toDto(foodTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FoodTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FoodTypeDTO> findByCriteria(FoodTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FoodTypeEntity> specification = createSpecification(criteria);
        return foodTypeRepository.findAll(specification, page).map(foodTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FoodTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FoodTypeEntity> specification = createSpecification(criteria);
        return foodTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link FoodTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FoodTypeEntity> createSpecification(FoodTypeCriteria criteria) {
        Specification<FoodTypeEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FoodTypeEntity_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), FoodTypeEntity_.title));
            }
            if (criteria.getTypeCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTypeCode(), FoodTypeEntity_.typeCode));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), FoodTypeEntity_.description));
            }
            if (criteria.getFoodsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFoodsId(),
                            root -> root.join(FoodTypeEntity_.foods, JoinType.LEFT).get(FoodEntity_.id)
                        )
                    );
            }
            if (criteria.getPartyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPartyId(),
                            root -> root.join(FoodTypeEntity_.party, JoinType.LEFT).get(PartyEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
