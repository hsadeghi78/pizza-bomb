package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.CommentEntity;
import com.barad.bomb.domain.CommentEntity;
import com.barad.bomb.domain.PartyEntity;
import com.barad.bomb.repository.CommentRepository;
import com.barad.bomb.service.criteria.CommentCriteria;
import com.barad.bomb.service.dto.CommentDTO;
import com.barad.bomb.service.mapper.CommentMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CommentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommentResourceIT {

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;
    private static final Integer SMALLER_RATING = 1 - 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommentMockMvc;

    private CommentEntity commentEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommentEntity createEntity(EntityManager em) {
        CommentEntity commentEntity = new CommentEntity().rating(DEFAULT_RATING).description(DEFAULT_DESCRIPTION);
        // Add required entity
        PartyEntity party;
        if (TestUtil.findAll(em, PartyEntity.class).isEmpty()) {
            party = PartyResourceIT.createEntity(em);
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, PartyEntity.class).get(0);
        }
        commentEntity.setWriterParty(party);
        // Add required entity
        commentEntity.setAudienceParty(party);
        return commentEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommentEntity createUpdatedEntity(EntityManager em) {
        CommentEntity commentEntity = new CommentEntity().rating(UPDATED_RATING).description(UPDATED_DESCRIPTION);
        // Add required entity
        PartyEntity party;
        if (TestUtil.findAll(em, PartyEntity.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity(em);
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, PartyEntity.class).get(0);
        }
        commentEntity.setWriterParty(party);
        // Add required entity
        commentEntity.setAudienceParty(party);
        return commentEntity;
    }

    @BeforeEach
    public void initTest() {
        commentEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createComment() throws Exception {
        int databaseSizeBeforeCreate = commentRepository.findAll().size();
        // Create the Comment
        CommentDTO commentDTO = commentMapper.toDto(commentEntity);
        restCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentDTO)))
            .andExpect(status().isCreated());

        // Validate the Comment in the database
        List<CommentEntity> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeCreate + 1);
        CommentEntity testComment = commentList.get(commentList.size() - 1);
        assertThat(testComment.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testComment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createCommentWithExistingId() throws Exception {
        // Create the Comment with an existing ID
        commentEntity.setId(1L);
        CommentDTO commentDTO = commentMapper.toDto(commentEntity);

        int databaseSizeBeforeCreate = commentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Comment in the database
        List<CommentEntity> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllComments() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        // Get all the commentList
        restCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getComment() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        // Get the comment
        restCommentMockMvc
            .perform(get(ENTITY_API_URL_ID, commentEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commentEntity.getId().intValue()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCommentsByIdFiltering() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        Long id = commentEntity.getId();

        defaultCommentShouldBeFound("id.equals=" + id);
        defaultCommentShouldNotBeFound("id.notEquals=" + id);

        defaultCommentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommentShouldNotBeFound("id.greaterThan=" + id);

        defaultCommentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommentsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        // Get all the commentList where rating equals to DEFAULT_RATING
        defaultCommentShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the commentList where rating equals to UPDATED_RATING
        defaultCommentShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllCommentsByRatingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        // Get all the commentList where rating not equals to DEFAULT_RATING
        defaultCommentShouldNotBeFound("rating.notEquals=" + DEFAULT_RATING);

        // Get all the commentList where rating not equals to UPDATED_RATING
        defaultCommentShouldBeFound("rating.notEquals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllCommentsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        // Get all the commentList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultCommentShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the commentList where rating equals to UPDATED_RATING
        defaultCommentShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllCommentsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        // Get all the commentList where rating is not null
        defaultCommentShouldBeFound("rating.specified=true");

        // Get all the commentList where rating is null
        defaultCommentShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    void getAllCommentsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        // Get all the commentList where rating is greater than or equal to DEFAULT_RATING
        defaultCommentShouldBeFound("rating.greaterThanOrEqual=" + DEFAULT_RATING);

        // Get all the commentList where rating is greater than or equal to UPDATED_RATING
        defaultCommentShouldNotBeFound("rating.greaterThanOrEqual=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllCommentsByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        // Get all the commentList where rating is less than or equal to DEFAULT_RATING
        defaultCommentShouldBeFound("rating.lessThanOrEqual=" + DEFAULT_RATING);

        // Get all the commentList where rating is less than or equal to SMALLER_RATING
        defaultCommentShouldNotBeFound("rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllCommentsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        // Get all the commentList where rating is less than DEFAULT_RATING
        defaultCommentShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the commentList where rating is less than UPDATED_RATING
        defaultCommentShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllCommentsByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        // Get all the commentList where rating is greater than DEFAULT_RATING
        defaultCommentShouldNotBeFound("rating.greaterThan=" + DEFAULT_RATING);

        // Get all the commentList where rating is greater than SMALLER_RATING
        defaultCommentShouldBeFound("rating.greaterThan=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    void getAllCommentsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        // Get all the commentList where description equals to DEFAULT_DESCRIPTION
        defaultCommentShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the commentList where description equals to UPDATED_DESCRIPTION
        defaultCommentShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommentsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        // Get all the commentList where description not equals to DEFAULT_DESCRIPTION
        defaultCommentShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the commentList where description not equals to UPDATED_DESCRIPTION
        defaultCommentShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommentsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        // Get all the commentList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCommentShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the commentList where description equals to UPDATED_DESCRIPTION
        defaultCommentShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommentsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        // Get all the commentList where description is not null
        defaultCommentShouldBeFound("description.specified=true");

        // Get all the commentList where description is null
        defaultCommentShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCommentsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        // Get all the commentList where description contains DEFAULT_DESCRIPTION
        defaultCommentShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the commentList where description contains UPDATED_DESCRIPTION
        defaultCommentShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommentsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        // Get all the commentList where description does not contain DEFAULT_DESCRIPTION
        defaultCommentShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the commentList where description does not contain UPDATED_DESCRIPTION
        defaultCommentShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCommentsByChildrenIsEqualToSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);
        CommentEntity children = CommentResourceIT.createEntity(em);
        em.persist(children);
        em.flush();
        commentEntity.addChildren(children);
        commentRepository.saveAndFlush(commentEntity);
        Long childrenId = children.getId();

        // Get all the commentList where children equals to childrenId
        defaultCommentShouldBeFound("childrenId.equals=" + childrenId);

        // Get all the commentList where children equals to (childrenId + 1)
        defaultCommentShouldNotBeFound("childrenId.equals=" + (childrenId + 1));
    }

    @Test
    @Transactional
    void getAllCommentsByWriterPartyIsEqualToSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);
        PartyEntity writerParty = PartyResourceIT.createEntity(em);
        em.persist(writerParty);
        em.flush();
        commentEntity.setWriterParty(writerParty);
        commentRepository.saveAndFlush(commentEntity);
        Long writerPartyId = writerParty.getId();

        // Get all the commentList where writerParty equals to writerPartyId
        defaultCommentShouldBeFound("writerPartyId.equals=" + writerPartyId);

        // Get all the commentList where writerParty equals to (writerPartyId + 1)
        defaultCommentShouldNotBeFound("writerPartyId.equals=" + (writerPartyId + 1));
    }

    @Test
    @Transactional
    void getAllCommentsByAudiencePartyIsEqualToSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);
        PartyEntity audienceParty = PartyResourceIT.createEntity(em);
        em.persist(audienceParty);
        em.flush();
        commentEntity.setAudienceParty(audienceParty);
        commentRepository.saveAndFlush(commentEntity);
        Long audiencePartyId = audienceParty.getId();

        // Get all the commentList where audienceParty equals to audiencePartyId
        defaultCommentShouldBeFound("audiencePartyId.equals=" + audiencePartyId);

        // Get all the commentList where audienceParty equals to (audiencePartyId + 1)
        defaultCommentShouldNotBeFound("audiencePartyId.equals=" + (audiencePartyId + 1));
    }

    @Test
    @Transactional
    void getAllCommentsByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);
        CommentEntity parent = CommentResourceIT.createEntity(em);
        em.persist(parent);
        em.flush();
        commentEntity.setParent(parent);
        commentRepository.saveAndFlush(commentEntity);
        Long parentId = parent.getId();

        // Get all the commentList where parent equals to parentId
        defaultCommentShouldBeFound("parentId.equals=" + parentId);

        // Get all the commentList where parent equals to (parentId + 1)
        defaultCommentShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommentShouldBeFound(String filter) throws Exception {
        restCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCommentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommentShouldNotBeFound(String filter) throws Exception {
        restCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingComment() throws Exception {
        // Get the comment
        restCommentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewComment() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        int databaseSizeBeforeUpdate = commentRepository.findAll().size();

        // Update the comment
        CommentEntity updatedCommentEntity = commentRepository.findById(commentEntity.getId()).get();
        // Disconnect from session so that the updates on updatedCommentEntity are not directly saved in db
        em.detach(updatedCommentEntity);
        updatedCommentEntity.rating(UPDATED_RATING).description(UPDATED_DESCRIPTION);
        CommentDTO commentDTO = commentMapper.toDto(updatedCommentEntity);

        restCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Comment in the database
        List<CommentEntity> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
        CommentEntity testComment = commentList.get(commentList.size() - 1);
        assertThat(testComment.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testComment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingComment() throws Exception {
        int databaseSizeBeforeUpdate = commentRepository.findAll().size();
        commentEntity.setId(count.incrementAndGet());

        // Create the Comment
        CommentDTO commentDTO = commentMapper.toDto(commentEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comment in the database
        List<CommentEntity> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComment() throws Exception {
        int databaseSizeBeforeUpdate = commentRepository.findAll().size();
        commentEntity.setId(count.incrementAndGet());

        // Create the Comment
        CommentDTO commentDTO = commentMapper.toDto(commentEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comment in the database
        List<CommentEntity> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComment() throws Exception {
        int databaseSizeBeforeUpdate = commentRepository.findAll().size();
        commentEntity.setId(count.incrementAndGet());

        // Create the Comment
        CommentDTO commentDTO = commentMapper.toDto(commentEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Comment in the database
        List<CommentEntity> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommentWithPatch() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        int databaseSizeBeforeUpdate = commentRepository.findAll().size();

        // Update the comment using partial update
        CommentEntity partialUpdatedCommentEntity = new CommentEntity();
        partialUpdatedCommentEntity.setId(commentEntity.getId());

        partialUpdatedCommentEntity.rating(UPDATED_RATING).description(UPDATED_DESCRIPTION);

        restCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommentEntity))
            )
            .andExpect(status().isOk());

        // Validate the Comment in the database
        List<CommentEntity> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
        CommentEntity testComment = commentList.get(commentList.size() - 1);
        assertThat(testComment.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testComment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCommentWithPatch() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        int databaseSizeBeforeUpdate = commentRepository.findAll().size();

        // Update the comment using partial update
        CommentEntity partialUpdatedCommentEntity = new CommentEntity();
        partialUpdatedCommentEntity.setId(commentEntity.getId());

        partialUpdatedCommentEntity.rating(UPDATED_RATING).description(UPDATED_DESCRIPTION);

        restCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommentEntity))
            )
            .andExpect(status().isOk());

        // Validate the Comment in the database
        List<CommentEntity> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
        CommentEntity testComment = commentList.get(commentList.size() - 1);
        assertThat(testComment.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testComment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingComment() throws Exception {
        int databaseSizeBeforeUpdate = commentRepository.findAll().size();
        commentEntity.setId(count.incrementAndGet());

        // Create the Comment
        CommentDTO commentDTO = commentMapper.toDto(commentEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comment in the database
        List<CommentEntity> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComment() throws Exception {
        int databaseSizeBeforeUpdate = commentRepository.findAll().size();
        commentEntity.setId(count.incrementAndGet());

        // Create the Comment
        CommentDTO commentDTO = commentMapper.toDto(commentEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comment in the database
        List<CommentEntity> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComment() throws Exception {
        int databaseSizeBeforeUpdate = commentRepository.findAll().size();
        commentEntity.setId(count.incrementAndGet());

        // Create the Comment
        CommentDTO commentDTO = commentMapper.toDto(commentEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(commentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Comment in the database
        List<CommentEntity> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComment() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(commentEntity);

        int databaseSizeBeforeDelete = commentRepository.findAll().size();

        // Delete the comment
        restCommentMockMvc
            .perform(delete(ENTITY_API_URL_ID, commentEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommentEntity> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
