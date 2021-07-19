package com.barad.bomb.service;

import com.barad.bomb.domain.*; // for static metamodels
import com.barad.bomb.domain.CommentEntity;
import com.barad.bomb.repository.CommentRepository;
import com.barad.bomb.service.criteria.CommentCriteria;
import com.barad.bomb.service.dto.CommentDTO;
import com.barad.bomb.service.mapper.CommentMapper;
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
 * Service for executing complex queries for {@link CommentEntity} entities in the database.
 * The main input is a {@link CommentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CommentDTO} or a {@link Page} of {@link CommentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommentQueryService extends QueryService<CommentEntity> {

    private final Logger log = LoggerFactory.getLogger(CommentQueryService.class);

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    public CommentQueryService(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    /**
     * Return a {@link List} of {@link CommentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommentDTO> findByCriteria(CommentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CommentEntity> specification = createSpecification(criteria);
        return commentMapper.toDto(commentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CommentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CommentDTO> findByCriteria(CommentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CommentEntity> specification = createSpecification(criteria);
        return commentRepository.findAll(specification, page).map(commentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CommentEntity> specification = createSpecification(criteria);
        return commentRepository.count(specification);
    }

    /**
     * Function to convert {@link CommentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CommentEntity> createSpecification(CommentCriteria criteria) {
        Specification<CommentEntity> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CommentEntity_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), CommentEntity_.rating));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), CommentEntity_.description));
            }
            if (criteria.getChildrenId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getChildrenId(),
                            root -> root.join(CommentEntity_.children, JoinType.LEFT).get(CommentEntity_.id)
                        )
                    );
            }
            if (criteria.getWriterPartyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getWriterPartyId(),
                            root -> root.join(CommentEntity_.writerParty, JoinType.LEFT).get(PartyEntity_.id)
                        )
                    );
            }
            if (criteria.getAudiencePartyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAudiencePartyId(),
                            root -> root.join(CommentEntity_.audienceParty, JoinType.LEFT).get(PartyEntity_.id)
                        )
                    );
            }
            if (criteria.getParentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParentId(),
                            root -> root.join(CommentEntity_.parent, JoinType.LEFT).get(CommentEntity_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
