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
            if (criteria.getLat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLat(), PartyEntity_.lat));
            }
            if (criteria.getLon() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLon(), PartyEntity_.lon));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), PartyEntity_.address));
            }
            if (criteria.getPostalCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostalCode(), PartyEntity_.postalCode));
            }
            if (criteria.getMobile() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMobile(), PartyEntity_.mobile));
            }
            if (criteria.getPartyTypeClassId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPartyTypeClassId(), PartyEntity_.partyTypeClassId));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PartyEntity_.description));
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
            if (criteria.getFilesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFilesId(),
                            root -> root.join(PartyEntity_.files, JoinType.LEFT).get(FileDocumentEntity_.id)
                        )
                    );
            }
            if (criteria.getMoreInfoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMoreInfoId(),
                            root -> root.join(PartyEntity_.moreInfos, JoinType.LEFT).get(PartyInformationEntity_.id)
                        )
                    );
            }
            if (criteria.getWrittenCommentsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getWrittenCommentsId(),
                            root -> root.join(PartyEntity_.writtenComments, JoinType.LEFT).get(CommentEntity_.id)
                        )
                    );
            }
            if (criteria.getAudienceCommentsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAudienceCommentsId(),
                            root -> root.join(PartyEntity_.audienceComments, JoinType.LEFT).get(CommentEntity_.id)
                        )
                    );
            }
            if (criteria.getFoodTypesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFoodTypesId(),
                            root -> root.join(PartyEntity_.foodTypes, JoinType.LEFT).get(FoodTypeEntity_.id)
                        )
                    );
            }
            if (criteria.getChildrenId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getChildrenId(),
                            root -> root.join(PartyEntity_.children, JoinType.LEFT).get(PartyEntity_.id)
                        )
                    );
            }
            if (criteria.getContactsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContactsId(),
                            root -> root.join(PartyEntity_.contacts, JoinType.LEFT).get(ContactEntity_.id)
                        )
                    );
            }
            if (criteria.getAddressesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAddressesId(),
                            root -> root.join(PartyEntity_.addresses, JoinType.LEFT).get(AddressEntity_.id)
                        )
                    );
            }
            if (criteria.getMenuItemsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMenuItemsId(),
                            root -> root.join(PartyEntity_.menuItems, JoinType.LEFT).get(MenuItemEntity_.id)
                        )
                    );
            }
            if (criteria.getProduceFoodsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProduceFoodsId(),
                            root -> root.join(PartyEntity_.produceFoods, JoinType.LEFT).get(FoodEntity_.id)
                        )
                    );
            }
            if (criteria.getDesignedFoodsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDesignedFoodsId(),
                            root -> root.join(PartyEntity_.designedFoods, JoinType.LEFT).get(FoodEntity_.id)
                        )
                    );
            }
            if (criteria.getBuyerFactorsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBuyerFactorsId(),
                            root -> root.join(PartyEntity_.buyerFactors, JoinType.LEFT).get(FactorEntity_.id)
                        )
                    );
            }
            if (criteria.getSellerFactorsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSellerFactorsId(),
                            root -> root.join(PartyEntity_.sellerFactors, JoinType.LEFT).get(FactorEntity_.id)
                        )
                    );
            }
            if (criteria.getParentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParentId(),
                            root -> root.join(PartyEntity_.parent, JoinType.LEFT).get(PartyEntity_.id)
                        )
                    );
            }
            if (criteria.getPartnerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPartnerId(),
                            root -> root.join(PartyEntity_.partner, JoinType.LEFT).get(PartnerEntity_.id)
                        )
                    );
            }
            if (criteria.getPersonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPersonId(),
                            root -> root.join(PartyEntity_.person, JoinType.LEFT).get(PersonEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
