package com.barad.bomb.service;

import com.barad.bomb.domain.*; // for static metamodels
import com.barad.bomb.domain.CriticismEntity;
import com.barad.bomb.repository.CriticismRepository;
import com.barad.bomb.service.criteria.CriticismCriteria;
import com.barad.bomb.service.dto.CriticismDTO;
import com.barad.bomb.service.mapper.CriticismMapper;
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
 * Service for executing complex queries for {@link CriticismEntity} entities in the database.
 * The main input is a {@link CriticismCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CriticismDTO} or a {@link Page} of {@link CriticismDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CriticismQueryService extends QueryService<CriticismEntity> {

    private final Logger log = LoggerFactory.getLogger(CriticismQueryService.class);

    private final CriticismRepository criticismRepository;

    private final CriticismMapper criticismMapper;

    public CriticismQueryService(CriticismRepository criticismRepository, CriticismMapper criticismMapper) {
        this.criticismRepository = criticismRepository;
        this.criticismMapper = criticismMapper;
    }

    /**
     * Return a {@link List} of {@link CriticismDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CriticismDTO> findByCriteria(CriticismCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CriticismEntity> specification = createSpecification(criteria);
        return criticismMapper.toDto(criticismRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CriticismDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CriticismDTO> findByCriteria(CriticismCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CriticismEntity> specification = createSpecification(criteria);
        return criticismRepository.findAll(specification, page).map(criticismMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CriticismCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CriticismEntity> specification = createSpecification(criteria);
        return criticismRepository.count(specification);
    }

    /**
     * Function to convert {@link CriticismCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CriticismEntity> createSpecification(CriticismCriteria criteria) {
        Specification<CriticismEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CriticismEntity_.id));
            }
            if (criteria.getFullName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullName(), CriticismEntity_.fullName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), CriticismEntity_.email));
            }
            if (criteria.getContactNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContactNumber(), CriticismEntity_.contactNumber));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CriticismEntity_.description));
            }
            if (criteria.getPartyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPartyId(),
                            root -> root.join(CriticismEntity_.party, JoinType.LEFT).get(PartyEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
