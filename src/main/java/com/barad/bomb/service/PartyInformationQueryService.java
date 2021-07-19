package com.barad.bomb.service;

import com.barad.bomb.domain.*; // for static metamodels
import com.barad.bomb.domain.PartyInformationEntity;
import com.barad.bomb.repository.PartyInformationRepository;
import com.barad.bomb.service.criteria.PartyInformationCriteria;
import com.barad.bomb.service.dto.PartyInformationDTO;
import com.barad.bomb.service.mapper.PartyInformationMapper;
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
 * Service for executing complex queries for {@link PartyInformationEntity} entities in the database.
 * The main input is a {@link PartyInformationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PartyInformationDTO} or a {@link Page} of {@link PartyInformationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartyInformationQueryService extends QueryService<PartyInformationEntity> {

    private final Logger log = LoggerFactory.getLogger(PartyInformationQueryService.class);

    private final PartyInformationRepository partyInformationRepository;

    private final PartyInformationMapper partyInformationMapper;

    public PartyInformationQueryService(
        PartyInformationRepository partyInformationRepository,
        PartyInformationMapper partyInformationMapper
    ) {
        this.partyInformationRepository = partyInformationRepository;
        this.partyInformationMapper = partyInformationMapper;
    }

    /**
     * Return a {@link List} of {@link PartyInformationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PartyInformationDTO> findByCriteria(PartyInformationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PartyInformationEntity> specification = createSpecification(criteria);
        return partyInformationMapper.toDto(partyInformationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PartyInformationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PartyInformationDTO> findByCriteria(PartyInformationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PartyInformationEntity> specification = createSpecification(criteria);
        return partyInformationRepository.findAll(specification, page).map(partyInformationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartyInformationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PartyInformationEntity> specification = createSpecification(criteria);
        return partyInformationRepository.count(specification);
    }

    /**
     * Function to convert {@link PartyInformationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PartyInformationEntity> createSpecification(PartyInformationCriteria criteria) {
        Specification<PartyInformationEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PartyInformationEntity_.id));
            }
            if (criteria.getInfoType() != null) {
                specification = specification.and(buildSpecification(criteria.getInfoType(), PartyInformationEntity_.infoType));
            }
            if (criteria.getInfoTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInfoTitle(), PartyInformationEntity_.infoTitle));
            }
            if (criteria.getInfoDesc() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInfoDesc(), PartyInformationEntity_.infoDesc));
            }
            if (criteria.getPartyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPartyId(),
                            root -> root.join(PartyInformationEntity_.party, JoinType.LEFT).get(PartyEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
