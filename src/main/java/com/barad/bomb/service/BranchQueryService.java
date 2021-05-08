package com.barad.bomb.service;

import com.barad.bomb.domain.*; // for static metamodels
import com.barad.bomb.domain.BranchEntity;
import com.barad.bomb.repository.BranchRepository;
import com.barad.bomb.service.criteria.BranchCriteria;
import com.barad.bomb.service.dto.BranchDTO;
import com.barad.bomb.service.mapper.BranchMapper;
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
 * Service for executing complex queries for {@link BranchEntity} entities in the database.
 * The main input is a {@link BranchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BranchDTO} or a {@link Page} of {@link BranchDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BranchQueryService extends QueryService<BranchEntity> {

    private final Logger log = LoggerFactory.getLogger(BranchQueryService.class);

    private final BranchRepository branchRepository;

    private final BranchMapper branchMapper;

    public BranchQueryService(BranchRepository branchRepository, BranchMapper branchMapper) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
    }

    /**
     * Return a {@link List} of {@link BranchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BranchDTO> findByCriteria(BranchCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BranchEntity> specification = createSpecification(criteria);
        return branchMapper.toDto(branchRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BranchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BranchDTO> findByCriteria(BranchCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BranchEntity> specification = createSpecification(criteria);
        return branchRepository.findAll(specification, page).map(branchMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BranchCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BranchEntity> specification = createSpecification(criteria);
        return branchRepository.count(specification);
    }

    /**
     * Function to convert {@link BranchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BranchEntity> createSpecification(BranchCriteria criteria) {
        Specification<BranchEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BranchEntity_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), BranchEntity_.title));
            }
            if (criteria.getBranchCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBranchCode(), BranchEntity_.branchCode));
            }
            if (criteria.getTradeTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTradeTitle(), BranchEntity_.tradeTitle));
            }
            if (criteria.getActivationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActivationDate(), BranchEntity_.activationDate));
            }
            if (criteria.getExpirationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpirationDate(), BranchEntity_.expirationDate));
            }
            if (criteria.getActivationStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getActivationStatus(), BranchEntity_.activationStatus));
            }
            if (criteria.getLat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLat(), BranchEntity_.lat));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), BranchEntity_.address));
            }
            if (criteria.getPostalCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostalCode(), BranchEntity_.postalCode));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), BranchEntity_.description));
            }
            if (criteria.getPartyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPartyId(),
                            root -> root.join(BranchEntity_.party, JoinType.LEFT).get(PartyEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
