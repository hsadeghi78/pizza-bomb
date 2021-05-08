package com.barad.bomb.service;

import com.barad.bomb.domain.*; // for static metamodels
import com.barad.bomb.domain.PartyEntity;
import com.barad.bomb.repository.PartyRepository;
import com.barad.bomb.service.criteria.PartyCriteria;
import com.barad.bomb.service.dto.PartyDTO;
import com.barad.bomb.service.mapper.PartyMapper;
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
 * Service for executing complex queries for {@link PartyEntity} entities in the database.
 * The main input is a {@link PartyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PartyDTO} or a {@link Page} of {@link PartyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartyQueryService extends QueryService<PartyEntity> {

    private final Logger log = LoggerFactory.getLogger(PartyQueryService.class);

    private final PartyRepository partyRepository;

    private final PartyMapper partyMapper;

    public PartyQueryService(PartyRepository partyRepository, PartyMapper partyMapper) {
        this.partyRepository = partyRepository;
        this.partyMapper = partyMapper;
    }

    /**
     * Return a {@link List} of {@link PartyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PartyDTO> findByCriteria(PartyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PartyEntity> specification = createSpecification(criteria);
        return partyMapper.toDto(partyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PartyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PartyDTO> findByCriteria(PartyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PartyEntity> specification = createSpecification(criteria);
        return partyRepository.findAll(specification, page).map(partyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PartyEntity> specification = createSpecification(criteria);
        return partyRepository.count(specification);
    }

    /**
     * Function to convert {@link PartyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PartyEntity> createSpecification(PartyCriteria criteria) {
        Specification<PartyEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PartyEntity_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), PartyEntity_.title));
            }
            if (criteria.getPartyCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPartyCode(), PartyEntity_.partyCode));
            }
            if (criteria.getTradeTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTradeTitle(), PartyEntity_.tradeTitle));
            }
            if (criteria.getActivationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActivationDate(), PartyEntity_.activationDate));
            }
            if (criteria.getExpirationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpirationDate(), PartyEntity_.expirationDate));
            }
            if (criteria.getActivationStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getActivationStatus(), PartyEntity_.activationStatus));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PartyEntity_.description));
            }
            if (criteria.getBranchsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBranchsId(),
                            root -> root.join(PartyEntity_.branchs, JoinType.LEFT).get(BranchEntity_.id)
                        )
                    );
            }
            if (criteria.getCriticismsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCriticismsId(),
                            root -> root.join(PartyEntity_.criticisms, JoinType.LEFT).get(CriticismEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
