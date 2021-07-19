package com.barad.bomb.service;

import com.barad.bomb.domain.*; // for static metamodels
import com.barad.bomb.domain.MenuItemEntity;
import com.barad.bomb.repository.MenuItemRepository;
import com.barad.bomb.service.criteria.MenuItemCriteria;
import com.barad.bomb.service.dto.MenuItemDTO;
import com.barad.bomb.service.mapper.MenuItemMapper;
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
 * Service for executing complex queries for {@link MenuItemEntity} entities in the database.
 * The main input is a {@link MenuItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MenuItemDTO} or a {@link Page} of {@link MenuItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MenuItemQueryService extends QueryService<MenuItemEntity> {

    private final Logger log = LoggerFactory.getLogger(MenuItemQueryService.class);

    private final MenuItemRepository menuItemRepository;

    private final MenuItemMapper menuItemMapper;

    public MenuItemQueryService(MenuItemRepository menuItemRepository, MenuItemMapper menuItemMapper) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemMapper = menuItemMapper;
    }

    /**
     * Return a {@link List} of {@link MenuItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MenuItemDTO> findByCriteria(MenuItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MenuItemEntity> specification = createSpecification(criteria);
        return menuItemMapper.toDto(menuItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MenuItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MenuItemDTO> findByCriteria(MenuItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MenuItemEntity> specification = createSpecification(criteria);
        return menuItemRepository.findAll(specification, page).map(menuItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MenuItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MenuItemEntity> specification = createSpecification(criteria);
        return menuItemRepository.count(specification);
    }

    /**
     * Function to convert {@link MenuItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MenuItemEntity> createSpecification(MenuItemCriteria criteria) {
        Specification<MenuItemEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MenuItemEntity_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), MenuItemEntity_.title));
            }
            if (criteria.getExpirationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpirationDate(), MenuItemEntity_.expirationDate));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), MenuItemEntity_.description));
            }
            if (criteria.getPartyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPartyId(),
                            root -> root.join(MenuItemEntity_.party, JoinType.LEFT).get(PartyEntity_.id)
                        )
                    );
            }
            if (criteria.getFoodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFoodId(), root -> root.join(MenuItemEntity_.food, JoinType.LEFT).get(FoodEntity_.id))
                    );
            }
        }
        return specification;
    }
}
