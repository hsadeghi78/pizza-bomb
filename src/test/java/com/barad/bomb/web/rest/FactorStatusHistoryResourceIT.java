package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.FactorStatusHistoryEntity;
import com.barad.bomb.domain.enumeration.FactorStatus;
import com.barad.bomb.repository.FactorStatusHistoryRepository;
import com.barad.bomb.service.criteria.FactorStatusHistoryCriteria;
import com.barad.bomb.service.dto.FactorStatusHistoryDTO;
import com.barad.bomb.service.mapper.FactorStatusHistoryMapper;
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
 * Integration tests for the {@link FactorStatusHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FactorStatusHistoryResourceIT {

    private static final Long DEFAULT_FACTOR_ID = 1L;
    private static final Long UPDATED_FACTOR_ID = 2L;
    private static final Long SMALLER_FACTOR_ID = 1L - 1L;

    private static final FactorStatus DEFAULT_STATUS = FactorStatus.INITIATE;
    private static final FactorStatus UPDATED_STATUS = FactorStatus.PRINTED;

    private static final String ENTITY_API_URL = "/api/factor-status-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FactorStatusHistoryRepository factorStatusHistoryRepository;

    @Autowired
    private FactorStatusHistoryMapper factorStatusHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFactorStatusHistoryMockMvc;

    private FactorStatusHistoryEntity factorStatusHistoryEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactorStatusHistoryEntity createEntity(EntityManager em) {
        FactorStatusHistoryEntity factorStatusHistoryEntity = new FactorStatusHistoryEntity()
            .factorId(DEFAULT_FACTOR_ID)
            .status(DEFAULT_STATUS);
        return factorStatusHistoryEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactorStatusHistoryEntity createUpdatedEntity(EntityManager em) {
        FactorStatusHistoryEntity factorStatusHistoryEntity = new FactorStatusHistoryEntity()
            .factorId(UPDATED_FACTOR_ID)
            .status(UPDATED_STATUS);
        return factorStatusHistoryEntity;
    }

    @BeforeEach
    public void initTest() {
        factorStatusHistoryEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createFactorStatusHistory() throws Exception {
        int databaseSizeBeforeCreate = factorStatusHistoryRepository.findAll().size();
        // Create the FactorStatusHistory
        FactorStatusHistoryDTO factorStatusHistoryDTO = factorStatusHistoryMapper.toDto(factorStatusHistoryEntity);
        restFactorStatusHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factorStatusHistoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FactorStatusHistory in the database
        List<FactorStatusHistoryEntity> factorStatusHistoryList = factorStatusHistoryRepository.findAll();
        assertThat(factorStatusHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        FactorStatusHistoryEntity testFactorStatusHistory = factorStatusHistoryList.get(factorStatusHistoryList.size() - 1);
        assertThat(testFactorStatusHistory.getFactorId()).isEqualTo(DEFAULT_FACTOR_ID);
        assertThat(testFactorStatusHistory.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createFactorStatusHistoryWithExistingId() throws Exception {
        // Create the FactorStatusHistory with an existing ID
        factorStatusHistoryEntity.setId(1L);
        FactorStatusHistoryDTO factorStatusHistoryDTO = factorStatusHistoryMapper.toDto(factorStatusHistoryEntity);

        int databaseSizeBeforeCreate = factorStatusHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactorStatusHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factorStatusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactorStatusHistory in the database
        List<FactorStatusHistoryEntity> factorStatusHistoryList = factorStatusHistoryRepository.findAll();
        assertThat(factorStatusHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFactorIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorStatusHistoryRepository.findAll().size();
        // set the field null
        factorStatusHistoryEntity.setFactorId(null);

        // Create the FactorStatusHistory, which fails.
        FactorStatusHistoryDTO factorStatusHistoryDTO = factorStatusHistoryMapper.toDto(factorStatusHistoryEntity);

        restFactorStatusHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factorStatusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<FactorStatusHistoryEntity> factorStatusHistoryList = factorStatusHistoryRepository.findAll();
        assertThat(factorStatusHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorStatusHistoryRepository.findAll().size();
        // set the field null
        factorStatusHistoryEntity.setStatus(null);

        // Create the FactorStatusHistory, which fails.
        FactorStatusHistoryDTO factorStatusHistoryDTO = factorStatusHistoryMapper.toDto(factorStatusHistoryEntity);

        restFactorStatusHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factorStatusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<FactorStatusHistoryEntity> factorStatusHistoryList = factorStatusHistoryRepository.findAll();
        assertThat(factorStatusHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFactorStatusHistories() throws Exception {
        // Initialize the database
        factorStatusHistoryRepository.saveAndFlush(factorStatusHistoryEntity);

        // Get all the factorStatusHistoryList
        restFactorStatusHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factorStatusHistoryEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].factorId").value(hasItem(DEFAULT_FACTOR_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getFactorStatusHistory() throws Exception {
        // Initialize the database
        factorStatusHistoryRepository.saveAndFlush(factorStatusHistoryEntity);

        // Get the factorStatusHistory
        restFactorStatusHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, factorStatusHistoryEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factorStatusHistoryEntity.getId().intValue()))
            .andExpect(jsonPath("$.factorId").value(DEFAULT_FACTOR_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getFactorStatusHistoriesByIdFiltering() throws Exception {
        // Initialize the database
        factorStatusHistoryRepository.saveAndFlush(factorStatusHistoryEntity);

        Long id = factorStatusHistoryEntity.getId();

        defaultFactorStatusHistoryShouldBeFound("id.equals=" + id);
        defaultFactorStatusHistoryShouldNotBeFound("id.notEquals=" + id);

        defaultFactorStatusHistoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFactorStatusHistoryShouldNotBeFound("id.greaterThan=" + id);

        defaultFactorStatusHistoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFactorStatusHistoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFactorStatusHistoriesByFactorIdIsEqualToSomething() throws Exception {
        // Initialize the database
        factorStatusHistoryRepository.saveAndFlush(factorStatusHistoryEntity);

        // Get all the factorStatusHistoryList where factorId equals to DEFAULT_FACTOR_ID
        defaultFactorStatusHistoryShouldBeFound("factorId.equals=" + DEFAULT_FACTOR_ID);

        // Get all the factorStatusHistoryList where factorId equals to UPDATED_FACTOR_ID
        defaultFactorStatusHistoryShouldNotBeFound("factorId.equals=" + UPDATED_FACTOR_ID);
    }

    @Test
    @Transactional
    void getAllFactorStatusHistoriesByFactorIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorStatusHistoryRepository.saveAndFlush(factorStatusHistoryEntity);

        // Get all the factorStatusHistoryList where factorId not equals to DEFAULT_FACTOR_ID
        defaultFactorStatusHistoryShouldNotBeFound("factorId.notEquals=" + DEFAULT_FACTOR_ID);

        // Get all the factorStatusHistoryList where factorId not equals to UPDATED_FACTOR_ID
        defaultFactorStatusHistoryShouldBeFound("factorId.notEquals=" + UPDATED_FACTOR_ID);
    }

    @Test
    @Transactional
    void getAllFactorStatusHistoriesByFactorIdIsInShouldWork() throws Exception {
        // Initialize the database
        factorStatusHistoryRepository.saveAndFlush(factorStatusHistoryEntity);

        // Get all the factorStatusHistoryList where factorId in DEFAULT_FACTOR_ID or UPDATED_FACTOR_ID
        defaultFactorStatusHistoryShouldBeFound("factorId.in=" + DEFAULT_FACTOR_ID + "," + UPDATED_FACTOR_ID);

        // Get all the factorStatusHistoryList where factorId equals to UPDATED_FACTOR_ID
        defaultFactorStatusHistoryShouldNotBeFound("factorId.in=" + UPDATED_FACTOR_ID);
    }

    @Test
    @Transactional
    void getAllFactorStatusHistoriesByFactorIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorStatusHistoryRepository.saveAndFlush(factorStatusHistoryEntity);

        // Get all the factorStatusHistoryList where factorId is not null
        defaultFactorStatusHistoryShouldBeFound("factorId.specified=true");

        // Get all the factorStatusHistoryList where factorId is null
        defaultFactorStatusHistoryShouldNotBeFound("factorId.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorStatusHistoriesByFactorIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorStatusHistoryRepository.saveAndFlush(factorStatusHistoryEntity);

        // Get all the factorStatusHistoryList where factorId is greater than or equal to DEFAULT_FACTOR_ID
        defaultFactorStatusHistoryShouldBeFound("factorId.greaterThanOrEqual=" + DEFAULT_FACTOR_ID);

        // Get all the factorStatusHistoryList where factorId is greater than or equal to UPDATED_FACTOR_ID
        defaultFactorStatusHistoryShouldNotBeFound("factorId.greaterThanOrEqual=" + UPDATED_FACTOR_ID);
    }

    @Test
    @Transactional
    void getAllFactorStatusHistoriesByFactorIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorStatusHistoryRepository.saveAndFlush(factorStatusHistoryEntity);

        // Get all the factorStatusHistoryList where factorId is less than or equal to DEFAULT_FACTOR_ID
        defaultFactorStatusHistoryShouldBeFound("factorId.lessThanOrEqual=" + DEFAULT_FACTOR_ID);

        // Get all the factorStatusHistoryList where factorId is less than or equal to SMALLER_FACTOR_ID
        defaultFactorStatusHistoryShouldNotBeFound("factorId.lessThanOrEqual=" + SMALLER_FACTOR_ID);
    }

    @Test
    @Transactional
    void getAllFactorStatusHistoriesByFactorIdIsLessThanSomething() throws Exception {
        // Initialize the database
        factorStatusHistoryRepository.saveAndFlush(factorStatusHistoryEntity);

        // Get all the factorStatusHistoryList where factorId is less than DEFAULT_FACTOR_ID
        defaultFactorStatusHistoryShouldNotBeFound("factorId.lessThan=" + DEFAULT_FACTOR_ID);

        // Get all the factorStatusHistoryList where factorId is less than UPDATED_FACTOR_ID
        defaultFactorStatusHistoryShouldBeFound("factorId.lessThan=" + UPDATED_FACTOR_ID);
    }

    @Test
    @Transactional
    void getAllFactorStatusHistoriesByFactorIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        factorStatusHistoryRepository.saveAndFlush(factorStatusHistoryEntity);

        // Get all the factorStatusHistoryList where factorId is greater than DEFAULT_FACTOR_ID
        defaultFactorStatusHistoryShouldNotBeFound("factorId.greaterThan=" + DEFAULT_FACTOR_ID);

        // Get all the factorStatusHistoryList where factorId is greater than SMALLER_FACTOR_ID
        defaultFactorStatusHistoryShouldBeFound("factorId.greaterThan=" + SMALLER_FACTOR_ID);
    }

    @Test
    @Transactional
    void getAllFactorStatusHistoriesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        factorStatusHistoryRepository.saveAndFlush(factorStatusHistoryEntity);

        // Get all the factorStatusHistoryList where status equals to DEFAULT_STATUS
        defaultFactorStatusHistoryShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the factorStatusHistoryList where status equals to UPDATED_STATUS
        defaultFactorStatusHistoryShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllFactorStatusHistoriesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorStatusHistoryRepository.saveAndFlush(factorStatusHistoryEntity);

        // Get all the factorStatusHistoryList where status not equals to DEFAULT_STATUS
        defaultFactorStatusHistoryShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the factorStatusHistoryList where status not equals to UPDATED_STATUS
        defaultFactorStatusHistoryShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllFactorStatusHistoriesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        factorStatusHistoryRepository.saveAndFlush(factorStatusHistoryEntity);

        // Get all the factorStatusHistoryList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultFactorStatusHistoryShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the factorStatusHistoryList where status equals to UPDATED_STATUS
        defaultFactorStatusHistoryShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllFactorStatusHistoriesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorStatusHistoryRepository.saveAndFlush(factorStatusHistoryEntity);

        // Get all the factorStatusHistoryList where status is not null
        defaultFactorStatusHistoryShouldBeFound("status.specified=true");

        // Get all the factorStatusHistoryList where status is null
        defaultFactorStatusHistoryShouldNotBeFound("status.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFactorStatusHistoryShouldBeFound(String filter) throws Exception {
        restFactorStatusHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factorStatusHistoryEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].factorId").value(hasItem(DEFAULT_FACTOR_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restFactorStatusHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFactorStatusHistoryShouldNotBeFound(String filter) throws Exception {
        restFactorStatusHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFactorStatusHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFactorStatusHistory() throws Exception {
        // Get the factorStatusHistory
        restFactorStatusHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFactorStatusHistory() throws Exception {
        // Initialize the database
        factorStatusHistoryRepository.saveAndFlush(factorStatusHistoryEntity);

        int databaseSizeBeforeUpdate = factorStatusHistoryRepository.findAll().size();

        // Update the factorStatusHistory
        FactorStatusHistoryEntity updatedFactorStatusHistoryEntity = factorStatusHistoryRepository
            .findById(factorStatusHistoryEntity.getId())
            .get();
        // Disconnect from session so that the updates on updatedFactorStatusHistoryEntity are not directly saved in db
        em.detach(updatedFactorStatusHistoryEntity);
        updatedFactorStatusHistoryEntity.factorId(UPDATED_FACTOR_ID).status(UPDATED_STATUS);
        FactorStatusHistoryDTO factorStatusHistoryDTO = factorStatusHistoryMapper.toDto(updatedFactorStatusHistoryEntity);

        restFactorStatusHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factorStatusHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factorStatusHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the FactorStatusHistory in the database
        List<FactorStatusHistoryEntity> factorStatusHistoryList = factorStatusHistoryRepository.findAll();
        assertThat(factorStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
        FactorStatusHistoryEntity testFactorStatusHistory = factorStatusHistoryList.get(factorStatusHistoryList.size() - 1);
        assertThat(testFactorStatusHistory.getFactorId()).isEqualTo(UPDATED_FACTOR_ID);
        assertThat(testFactorStatusHistory.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingFactorStatusHistory() throws Exception {
        int databaseSizeBeforeUpdate = factorStatusHistoryRepository.findAll().size();
        factorStatusHistoryEntity.setId(count.incrementAndGet());

        // Create the FactorStatusHistory
        FactorStatusHistoryDTO factorStatusHistoryDTO = factorStatusHistoryMapper.toDto(factorStatusHistoryEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactorStatusHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factorStatusHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factorStatusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactorStatusHistory in the database
        List<FactorStatusHistoryEntity> factorStatusHistoryList = factorStatusHistoryRepository.findAll();
        assertThat(factorStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFactorStatusHistory() throws Exception {
        int databaseSizeBeforeUpdate = factorStatusHistoryRepository.findAll().size();
        factorStatusHistoryEntity.setId(count.incrementAndGet());

        // Create the FactorStatusHistory
        FactorStatusHistoryDTO factorStatusHistoryDTO = factorStatusHistoryMapper.toDto(factorStatusHistoryEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactorStatusHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factorStatusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactorStatusHistory in the database
        List<FactorStatusHistoryEntity> factorStatusHistoryList = factorStatusHistoryRepository.findAll();
        assertThat(factorStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFactorStatusHistory() throws Exception {
        int databaseSizeBeforeUpdate = factorStatusHistoryRepository.findAll().size();
        factorStatusHistoryEntity.setId(count.incrementAndGet());

        // Create the FactorStatusHistory
        FactorStatusHistoryDTO factorStatusHistoryDTO = factorStatusHistoryMapper.toDto(factorStatusHistoryEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactorStatusHistoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factorStatusHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactorStatusHistory in the database
        List<FactorStatusHistoryEntity> factorStatusHistoryList = factorStatusHistoryRepository.findAll();
        assertThat(factorStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFactorStatusHistoryWithPatch() throws Exception {
        // Initialize the database
        factorStatusHistoryRepository.saveAndFlush(factorStatusHistoryEntity);

        int databaseSizeBeforeUpdate = factorStatusHistoryRepository.findAll().size();

        // Update the factorStatusHistory using partial update
        FactorStatusHistoryEntity partialUpdatedFactorStatusHistoryEntity = new FactorStatusHistoryEntity();
        partialUpdatedFactorStatusHistoryEntity.setId(factorStatusHistoryEntity.getId());

        partialUpdatedFactorStatusHistoryEntity.factorId(UPDATED_FACTOR_ID);

        restFactorStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactorStatusHistoryEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactorStatusHistoryEntity))
            )
            .andExpect(status().isOk());

        // Validate the FactorStatusHistory in the database
        List<FactorStatusHistoryEntity> factorStatusHistoryList = factorStatusHistoryRepository.findAll();
        assertThat(factorStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
        FactorStatusHistoryEntity testFactorStatusHistory = factorStatusHistoryList.get(factorStatusHistoryList.size() - 1);
        assertThat(testFactorStatusHistory.getFactorId()).isEqualTo(UPDATED_FACTOR_ID);
        assertThat(testFactorStatusHistory.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateFactorStatusHistoryWithPatch() throws Exception {
        // Initialize the database
        factorStatusHistoryRepository.saveAndFlush(factorStatusHistoryEntity);

        int databaseSizeBeforeUpdate = factorStatusHistoryRepository.findAll().size();

        // Update the factorStatusHistory using partial update
        FactorStatusHistoryEntity partialUpdatedFactorStatusHistoryEntity = new FactorStatusHistoryEntity();
        partialUpdatedFactorStatusHistoryEntity.setId(factorStatusHistoryEntity.getId());

        partialUpdatedFactorStatusHistoryEntity.factorId(UPDATED_FACTOR_ID).status(UPDATED_STATUS);

        restFactorStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactorStatusHistoryEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactorStatusHistoryEntity))
            )
            .andExpect(status().isOk());

        // Validate the FactorStatusHistory in the database
        List<FactorStatusHistoryEntity> factorStatusHistoryList = factorStatusHistoryRepository.findAll();
        assertThat(factorStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
        FactorStatusHistoryEntity testFactorStatusHistory = factorStatusHistoryList.get(factorStatusHistoryList.size() - 1);
        assertThat(testFactorStatusHistory.getFactorId()).isEqualTo(UPDATED_FACTOR_ID);
        assertThat(testFactorStatusHistory.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingFactorStatusHistory() throws Exception {
        int databaseSizeBeforeUpdate = factorStatusHistoryRepository.findAll().size();
        factorStatusHistoryEntity.setId(count.incrementAndGet());

        // Create the FactorStatusHistory
        FactorStatusHistoryDTO factorStatusHistoryDTO = factorStatusHistoryMapper.toDto(factorStatusHistoryEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactorStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factorStatusHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factorStatusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactorStatusHistory in the database
        List<FactorStatusHistoryEntity> factorStatusHistoryList = factorStatusHistoryRepository.findAll();
        assertThat(factorStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFactorStatusHistory() throws Exception {
        int databaseSizeBeforeUpdate = factorStatusHistoryRepository.findAll().size();
        factorStatusHistoryEntity.setId(count.incrementAndGet());

        // Create the FactorStatusHistory
        FactorStatusHistoryDTO factorStatusHistoryDTO = factorStatusHistoryMapper.toDto(factorStatusHistoryEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactorStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factorStatusHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactorStatusHistory in the database
        List<FactorStatusHistoryEntity> factorStatusHistoryList = factorStatusHistoryRepository.findAll();
        assertThat(factorStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFactorStatusHistory() throws Exception {
        int databaseSizeBeforeUpdate = factorStatusHistoryRepository.findAll().size();
        factorStatusHistoryEntity.setId(count.incrementAndGet());

        // Create the FactorStatusHistory
        FactorStatusHistoryDTO factorStatusHistoryDTO = factorStatusHistoryMapper.toDto(factorStatusHistoryEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactorStatusHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factorStatusHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactorStatusHistory in the database
        List<FactorStatusHistoryEntity> factorStatusHistoryList = factorStatusHistoryRepository.findAll();
        assertThat(factorStatusHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFactorStatusHistory() throws Exception {
        // Initialize the database
        factorStatusHistoryRepository.saveAndFlush(factorStatusHistoryEntity);

        int databaseSizeBeforeDelete = factorStatusHistoryRepository.findAll().size();

        // Delete the factorStatusHistory
        restFactorStatusHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, factorStatusHistoryEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FactorStatusHistoryEntity> factorStatusHistoryList = factorStatusHistoryRepository.findAll();
        assertThat(factorStatusHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
