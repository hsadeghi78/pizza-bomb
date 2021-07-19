package com.barad.bomb.service;

import com.barad.bomb.domain.*; // for static metamodels
import com.barad.bomb.domain.PartnerEntity;
import com.barad.bomb.repository.PartnerRepository;
import com.barad.bomb.service.criteria.PartnerCriteria;
import com.barad.bomb.service.dto.PartnerDTO;
import com.barad.bomb.service.mapper.PartnerMapper;
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
 * Service for executing complex queries for {@link PartnerEntity} entities in the database.
 * The main input is a {@link PartnerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PartnerDTO} or a {@link Page} of {@link PartnerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartnerQueryService extends QueryService<PartnerEntity> {

    private final Logger log = LoggerFactory.getLogger(PartnerQueryService.class);

    private final PartnerRepository partnerRepository;

    private final PartnerMapper partnerMapper;

    public PartnerQueryService(PartnerRepository partnerRepository, PartnerMapper partnerMapper) {
        this.partnerRepository = partnerRepository;
        this.partnerMapper = partnerMapper;
    }

    /**
     * Return a {@link List} of {@link PartnerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PartnerDTO> findByCriteria(PartnerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PartnerEntity> specification = createSpecification(criteria);
        return partnerMapper.toDto(partnerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PartnerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PartnerDTO> findByCriteria(PartnerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PartnerEntity> specification = createSpecification(criteria);
        return partnerRepository.findAll(specification, page).map(partnerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartnerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PartnerEntity> specification = createSpecification(criteria);
        return partnerRepository.count(specification);
    }

    /**
     * Function to convert {@link PartnerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PartnerEntity> createSpecification(PartnerCriteria criteria) {
        Specification<PartnerEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PartnerEntity_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), PartnerEntity_.title));
            }
            if (criteria.getPartnerCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPartnerCode(), PartnerEntity_.partnerCode));
            }
            if (criteria.getTradeTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTradeTitle(), PartnerEntity_.tradeTitle));
            }
            if (criteria.getEconomicCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEconomicCode(), PartnerEntity_.economicCode));
            }
            if (criteria.getActivityDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActivityDate(), PartnerEntity_.activityDate));
            }
            if (criteria.getPartiesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPartiesId(),
                            root -> root.join(PartnerEntity_.parties, JoinType.LEFT).get(PartyEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
