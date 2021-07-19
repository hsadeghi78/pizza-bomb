package com.barad.bomb.service;

import com.barad.bomb.domain.*; // for static metamodels
import com.barad.bomb.domain.FactorEntity;
import com.barad.bomb.repository.FactorRepository;
import com.barad.bomb.service.criteria.FactorCriteria;
import com.barad.bomb.service.dto.FactorDTO;
import com.barad.bomb.service.mapper.FactorMapper;
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
 * Service for executing complex queries for {@link FactorEntity} entities in the database.
 * The main input is a {@link FactorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FactorDTO} or a {@link Page} of {@link FactorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FactorQueryService extends QueryService<FactorEntity> {

    private final Logger log = LoggerFactory.getLogger(FactorQueryService.class);

    private final FactorRepository factorRepository;

    private final FactorMapper factorMapper;

    public FactorQueryService(FactorRepository factorRepository, FactorMapper factorMapper) {
        this.factorRepository = factorRepository;
        this.factorMapper = factorMapper;
    }

    /**
     * Return a {@link List} of {@link FactorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FactorDTO> findByCriteria(FactorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FactorEntity> specification = createSpecification(criteria);
        return factorMapper.toDto(factorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FactorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FactorDTO> findByCriteria(FactorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FactorEntity> specification = createSpecification(criteria);
        return factorRepository.findAll(specification, page).map(factorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FactorCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FactorEntity> specification = createSpecification(criteria);
        return factorRepository.count(specification);
    }

    /**
     * Function to convert {@link FactorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FactorEntity> createSpecification(FactorCriteria criteria) {
        Specification<FactorEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FactorEntity_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), FactorEntity_.title));
            }
            if (criteria.getFactorCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFactorCode(), FactorEntity_.factorCode));
            }
            if (criteria.getLastStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getLastStatus(), FactorEntity_.lastStatus));
            }
            if (criteria.getOrderWay() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderWay(), FactorEntity_.orderWay));
            }
            if (criteria.getServing() != null) {
                specification = specification.and(buildSpecification(criteria.getServing(), FactorEntity_.serving));
            }
            if (criteria.getPaymentStateClassId() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPaymentStateClassId(), FactorEntity_.paymentStateClassId));
            }
            if (criteria.getCategoryClassId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCategoryClassId(), FactorEntity_.categoryClassId));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), FactorEntity_.totalPrice));
            }
            if (criteria.getDiscount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscount(), FactorEntity_.discount));
            }
            if (criteria.getTax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTax(), FactorEntity_.tax));
            }
            if (criteria.getNetprice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNetprice(), FactorEntity_.netprice));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), FactorEntity_.description));
            }
            if (criteria.getFactorItemsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFactorItemsId(),
                            root -> root.join(FactorEntity_.factorItems, JoinType.LEFT).get(FactorItemEntity_.id)
                        )
                    );
            }
            if (criteria.getBuyerPartyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBuyerPartyId(),
                            root -> root.join(FactorEntity_.buyerParty, JoinType.LEFT).get(PartyEntity_.id)
                        )
                    );
            }
            if (criteria.getSellerPartyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSellerPartyId(),
                            root -> root.join(FactorEntity_.sellerParty, JoinType.LEFT).get(PartyEntity_.id)
                        )
                    );
            }
            if (criteria.getDeliveryAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDeliveryAddressId(),
                            root -> root.join(FactorEntity_.deliveryAddress, JoinType.LEFT).get(AddressEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
