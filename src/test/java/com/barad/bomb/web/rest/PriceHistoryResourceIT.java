package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.PriceHistoryEntity;
import com.barad.bomb.repository.PriceHistoryRepository;
import com.barad.bomb.service.criteria.PriceHistoryCriteria;
import com.barad.bomb.service.dto.PriceHistoryDTO;
import com.barad.bomb.service.mapper.PriceHistoryMapper;
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
 * Integration tests for the {@link PriceHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PriceHistoryResourceIT {

    private static final Long DEFAULT_FOOD_ID = 1L;
    private static final Long UPDATED_FOOD_ID = 2L;
    private static final Long SMALLER_FOOD_ID = 1L - 1L;

    private static final Long DEFAULT_MATERIAL_ID = 1L;
    private static final Long UPDATED_MATERIAL_ID = 2L;
    private static final Long SMALLER_MATERIAL_ID = 1L - 1L;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;
    private static final Double SMALLER_PRICE = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/price-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    @Autowired
    private PriceHistoryMapper priceHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPriceHistoryMockMvc;

    private PriceHistoryEntity priceHistoryEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriceHistoryEntity createEntity(EntityManager em) {
        PriceHistoryEntity priceHistoryEntity = new PriceHistoryEntity()
            .foodId(DEFAULT_FOOD_ID)
            .materialId(DEFAULT_MATERIAL_ID)
            .price(DEFAULT_PRICE);
        return priceHistoryEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriceHistoryEntity createUpdatedEntity(EntityManager em) {
        PriceHistoryEntity priceHistoryEntity = new PriceHistoryEntity()
            .foodId(UPDATED_FOOD_ID)
            .materialId(UPDATED_MATERIAL_ID)
            .price(UPDATED_PRICE);
        return priceHistoryEntity;
    }

    @BeforeEach
    public void initTest() {
        priceHistoryEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createPriceHistory() throws Exception {
        int databaseSizeBeforeCreate = priceHistoryRepository.findAll().size();
        // Create the PriceHistory
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(priceHistoryEntity);
        restPriceHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PriceHistory in the database
        List<PriceHistoryEntity> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        PriceHistoryEntity testPriceHistory = priceHistoryList.get(priceHistoryList.size() - 1);
        assertThat(testPriceHistory.getFoodId()).isEqualTo(DEFAULT_FOOD_ID);
        assertThat(testPriceHistory.getMaterialId()).isEqualTo(DEFAULT_MATERIAL_ID);
        assertThat(testPriceHistory.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createPriceHistoryWithExistingId() throws Exception {
        // Create the PriceHistory with an existing ID
        priceHistoryEntity.setId(1L);
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(priceHistoryEntity);

        int databaseSizeBeforeCreate = priceHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPriceHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceHistory in the database
        List<PriceHistoryEntity> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = priceHistoryRepository.findAll().size();
        // set the field null
        priceHistoryEntity.setPrice(null);

        // Create the PriceHistory, which fails.
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(priceHistoryEntity);

        restPriceHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<PriceHistoryEntity> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPriceHistories() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList
        restPriceHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priceHistoryEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].foodId").value(hasItem(DEFAULT_FOOD_ID.intValue())))
            .andExpect(jsonPath("$.[*].materialId").value(hasItem(DEFAULT_MATERIAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getPriceHistory() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get the priceHistory
        restPriceHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, priceHistoryEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(priceHistoryEntity.getId().intValue()))
            .andExpect(jsonPath("$.foodId").value(DEFAULT_FOOD_ID.intValue()))
            .andExpect(jsonPath("$.materialId").value(DEFAULT_MATERIAL_ID.intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getPriceHistoriesByIdFiltering() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        Long id = priceHistoryEntity.getId();

        defaultPriceHistoryShouldBeFound("id.equals=" + id);
        defaultPriceHistoryShouldNotBeFound("id.notEquals=" + id);

        defaultPriceHistoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPriceHistoryShouldNotBeFound("id.greaterThan=" + id);

        defaultPriceHistoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPriceHistoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByFoodIdIsEqualToSomething() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where foodId equals to DEFAULT_FOOD_ID
        defaultPriceHistoryShouldBeFound("foodId.equals=" + DEFAULT_FOOD_ID);

        // Get all the priceHistoryList where foodId equals to UPDATED_FOOD_ID
        defaultPriceHistoryShouldNotBeFound("foodId.equals=" + UPDATED_FOOD_ID);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByFoodIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where foodId not equals to DEFAULT_FOOD_ID
        defaultPriceHistoryShouldNotBeFound("foodId.notEquals=" + DEFAULT_FOOD_ID);

        // Get all the priceHistoryList where foodId not equals to UPDATED_FOOD_ID
        defaultPriceHistoryShouldBeFound("foodId.notEquals=" + UPDATED_FOOD_ID);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByFoodIdIsInShouldWork() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where foodId in DEFAULT_FOOD_ID or UPDATED_FOOD_ID
        defaultPriceHistoryShouldBeFound("foodId.in=" + DEFAULT_FOOD_ID + "," + UPDATED_FOOD_ID);

        // Get all the priceHistoryList where foodId equals to UPDATED_FOOD_ID
        defaultPriceHistoryShouldNotBeFound("foodId.in=" + UPDATED_FOOD_ID);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByFoodIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where foodId is not null
        defaultPriceHistoryShouldBeFound("foodId.specified=true");

        // Get all the priceHistoryList where foodId is null
        defaultPriceHistoryShouldNotBeFound("foodId.specified=false");
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByFoodIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where foodId is greater than or equal to DEFAULT_FOOD_ID
        defaultPriceHistoryShouldBeFound("foodId.greaterThanOrEqual=" + DEFAULT_FOOD_ID);

        // Get all the priceHistoryList where foodId is greater than or equal to UPDATED_FOOD_ID
        defaultPriceHistoryShouldNotBeFound("foodId.greaterThanOrEqual=" + UPDATED_FOOD_ID);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByFoodIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where foodId is less than or equal to DEFAULT_FOOD_ID
        defaultPriceHistoryShouldBeFound("foodId.lessThanOrEqual=" + DEFAULT_FOOD_ID);

        // Get all the priceHistoryList where foodId is less than or equal to SMALLER_FOOD_ID
        defaultPriceHistoryShouldNotBeFound("foodId.lessThanOrEqual=" + SMALLER_FOOD_ID);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByFoodIdIsLessThanSomething() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where foodId is less than DEFAULT_FOOD_ID
        defaultPriceHistoryShouldNotBeFound("foodId.lessThan=" + DEFAULT_FOOD_ID);

        // Get all the priceHistoryList where foodId is less than UPDATED_FOOD_ID
        defaultPriceHistoryShouldBeFound("foodId.lessThan=" + UPDATED_FOOD_ID);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByFoodIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where foodId is greater than DEFAULT_FOOD_ID
        defaultPriceHistoryShouldNotBeFound("foodId.greaterThan=" + DEFAULT_FOOD_ID);

        // Get all the priceHistoryList where foodId is greater than SMALLER_FOOD_ID
        defaultPriceHistoryShouldBeFound("foodId.greaterThan=" + SMALLER_FOOD_ID);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByMaterialIdIsEqualToSomething() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where materialId equals to DEFAULT_MATERIAL_ID
        defaultPriceHistoryShouldBeFound("materialId.equals=" + DEFAULT_MATERIAL_ID);

        // Get all the priceHistoryList where materialId equals to UPDATED_MATERIAL_ID
        defaultPriceHistoryShouldNotBeFound("materialId.equals=" + UPDATED_MATERIAL_ID);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByMaterialIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where materialId not equals to DEFAULT_MATERIAL_ID
        defaultPriceHistoryShouldNotBeFound("materialId.notEquals=" + DEFAULT_MATERIAL_ID);

        // Get all the priceHistoryList where materialId not equals to UPDATED_MATERIAL_ID
        defaultPriceHistoryShouldBeFound("materialId.notEquals=" + UPDATED_MATERIAL_ID);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByMaterialIdIsInShouldWork() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where materialId in DEFAULT_MATERIAL_ID or UPDATED_MATERIAL_ID
        defaultPriceHistoryShouldBeFound("materialId.in=" + DEFAULT_MATERIAL_ID + "," + UPDATED_MATERIAL_ID);

        // Get all the priceHistoryList where materialId equals to UPDATED_MATERIAL_ID
        defaultPriceHistoryShouldNotBeFound("materialId.in=" + UPDATED_MATERIAL_ID);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByMaterialIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where materialId is not null
        defaultPriceHistoryShouldBeFound("materialId.specified=true");

        // Get all the priceHistoryList where materialId is null
        defaultPriceHistoryShouldNotBeFound("materialId.specified=false");
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByMaterialIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where materialId is greater than or equal to DEFAULT_MATERIAL_ID
        defaultPriceHistoryShouldBeFound("materialId.greaterThanOrEqual=" + DEFAULT_MATERIAL_ID);

        // Get all the priceHistoryList where materialId is greater than or equal to UPDATED_MATERIAL_ID
        defaultPriceHistoryShouldNotBeFound("materialId.greaterThanOrEqual=" + UPDATED_MATERIAL_ID);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByMaterialIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where materialId is less than or equal to DEFAULT_MATERIAL_ID
        defaultPriceHistoryShouldBeFound("materialId.lessThanOrEqual=" + DEFAULT_MATERIAL_ID);

        // Get all the priceHistoryList where materialId is less than or equal to SMALLER_MATERIAL_ID
        defaultPriceHistoryShouldNotBeFound("materialId.lessThanOrEqual=" + SMALLER_MATERIAL_ID);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByMaterialIdIsLessThanSomething() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where materialId is less than DEFAULT_MATERIAL_ID
        defaultPriceHistoryShouldNotBeFound("materialId.lessThan=" + DEFAULT_MATERIAL_ID);

        // Get all the priceHistoryList where materialId is less than UPDATED_MATERIAL_ID
        defaultPriceHistoryShouldBeFound("materialId.lessThan=" + UPDATED_MATERIAL_ID);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByMaterialIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where materialId is greater than DEFAULT_MATERIAL_ID
        defaultPriceHistoryShouldNotBeFound("materialId.greaterThan=" + DEFAULT_MATERIAL_ID);

        // Get all the priceHistoryList where materialId is greater than SMALLER_MATERIAL_ID
        defaultPriceHistoryShouldBeFound("materialId.greaterThan=" + SMALLER_MATERIAL_ID);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where price equals to DEFAULT_PRICE
        defaultPriceHistoryShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the priceHistoryList where price equals to UPDATED_PRICE
        defaultPriceHistoryShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where price not equals to DEFAULT_PRICE
        defaultPriceHistoryShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the priceHistoryList where price not equals to UPDATED_PRICE
        defaultPriceHistoryShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultPriceHistoryShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the priceHistoryList where price equals to UPDATED_PRICE
        defaultPriceHistoryShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where price is not null
        defaultPriceHistoryShouldBeFound("price.specified=true");

        // Get all the priceHistoryList where price is null
        defaultPriceHistoryShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where price is greater than or equal to DEFAULT_PRICE
        defaultPriceHistoryShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the priceHistoryList where price is greater than or equal to UPDATED_PRICE
        defaultPriceHistoryShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where price is less than or equal to DEFAULT_PRICE
        defaultPriceHistoryShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the priceHistoryList where price is less than or equal to SMALLER_PRICE
        defaultPriceHistoryShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where price is less than DEFAULT_PRICE
        defaultPriceHistoryShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the priceHistoryList where price is less than UPDATED_PRICE
        defaultPriceHistoryShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllPriceHistoriesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        // Get all the priceHistoryList where price is greater than DEFAULT_PRICE
        defaultPriceHistoryShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the priceHistoryList where price is greater than SMALLER_PRICE
        defaultPriceHistoryShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPriceHistoryShouldBeFound(String filter) throws Exception {
        restPriceHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priceHistoryEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].foodId").value(hasItem(DEFAULT_FOOD_ID.intValue())))
            .andExpect(jsonPath("$.[*].materialId").value(hasItem(DEFAULT_MATERIAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));

        // Check, that the count call also returns 1
        restPriceHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPriceHistoryShouldNotBeFound(String filter) throws Exception {
        restPriceHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPriceHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPriceHistory() throws Exception {
        // Get the priceHistory
        restPriceHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPriceHistory() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();

        // Update the priceHistory
        PriceHistoryEntity updatedPriceHistoryEntity = priceHistoryRepository.findById(priceHistoryEntity.getId()).get();
        // Disconnect from session so that the updates on updatedPriceHistoryEntity are not directly saved in db
        em.detach(updatedPriceHistoryEntity);
        updatedPriceHistoryEntity.foodId(UPDATED_FOOD_ID).materialId(UPDATED_MATERIAL_ID).price(UPDATED_PRICE);
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(updatedPriceHistoryEntity);

        restPriceHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, priceHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the PriceHistory in the database
        List<PriceHistoryEntity> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
        PriceHistoryEntity testPriceHistory = priceHistoryList.get(priceHistoryList.size() - 1);
        assertThat(testPriceHistory.getFoodId()).isEqualTo(UPDATED_FOOD_ID);
        assertThat(testPriceHistory.getMaterialId()).isEqualTo(UPDATED_MATERIAL_ID);
        assertThat(testPriceHistory.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingPriceHistory() throws Exception {
        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();
        priceHistoryEntity.setId(count.incrementAndGet());

        // Create the PriceHistory
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(priceHistoryEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, priceHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceHistory in the database
        List<PriceHistoryEntity> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPriceHistory() throws Exception {
        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();
        priceHistoryEntity.setId(count.incrementAndGet());

        // Create the PriceHistory
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(priceHistoryEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceHistory in the database
        List<PriceHistoryEntity> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPriceHistory() throws Exception {
        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();
        priceHistoryEntity.setId(count.incrementAndGet());

        // Create the PriceHistory
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(priceHistoryEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceHistoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PriceHistory in the database
        List<PriceHistoryEntity> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePriceHistoryWithPatch() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();

        // Update the priceHistory using partial update
        PriceHistoryEntity partialUpdatedPriceHistoryEntity = new PriceHistoryEntity();
        partialUpdatedPriceHistoryEntity.setId(priceHistoryEntity.getId());

        partialUpdatedPriceHistoryEntity.materialId(UPDATED_MATERIAL_ID);

        restPriceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPriceHistoryEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPriceHistoryEntity))
            )
            .andExpect(status().isOk());

        // Validate the PriceHistory in the database
        List<PriceHistoryEntity> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
        PriceHistoryEntity testPriceHistory = priceHistoryList.get(priceHistoryList.size() - 1);
        assertThat(testPriceHistory.getFoodId()).isEqualTo(DEFAULT_FOOD_ID);
        assertThat(testPriceHistory.getMaterialId()).isEqualTo(UPDATED_MATERIAL_ID);
        assertThat(testPriceHistory.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void fullUpdatePriceHistoryWithPatch() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();

        // Update the priceHistory using partial update
        PriceHistoryEntity partialUpdatedPriceHistoryEntity = new PriceHistoryEntity();
        partialUpdatedPriceHistoryEntity.setId(priceHistoryEntity.getId());

        partialUpdatedPriceHistoryEntity.foodId(UPDATED_FOOD_ID).materialId(UPDATED_MATERIAL_ID).price(UPDATED_PRICE);

        restPriceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPriceHistoryEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPriceHistoryEntity))
            )
            .andExpect(status().isOk());

        // Validate the PriceHistory in the database
        List<PriceHistoryEntity> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
        PriceHistoryEntity testPriceHistory = priceHistoryList.get(priceHistoryList.size() - 1);
        assertThat(testPriceHistory.getFoodId()).isEqualTo(UPDATED_FOOD_ID);
        assertThat(testPriceHistory.getMaterialId()).isEqualTo(UPDATED_MATERIAL_ID);
        assertThat(testPriceHistory.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingPriceHistory() throws Exception {
        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();
        priceHistoryEntity.setId(count.incrementAndGet());

        // Create the PriceHistory
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(priceHistoryEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, priceHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceHistory in the database
        List<PriceHistoryEntity> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPriceHistory() throws Exception {
        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();
        priceHistoryEntity.setId(count.incrementAndGet());

        // Create the PriceHistory
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(priceHistoryEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceHistory in the database
        List<PriceHistoryEntity> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPriceHistory() throws Exception {
        int databaseSizeBeforeUpdate = priceHistoryRepository.findAll().size();
        priceHistoryEntity.setId(count.incrementAndGet());

        // Create the PriceHistory
        PriceHistoryDTO priceHistoryDTO = priceHistoryMapper.toDto(priceHistoryEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priceHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PriceHistory in the database
        List<PriceHistoryEntity> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePriceHistory() throws Exception {
        // Initialize the database
        priceHistoryRepository.saveAndFlush(priceHistoryEntity);

        int databaseSizeBeforeDelete = priceHistoryRepository.findAll().size();

        // Delete the priceHistory
        restPriceHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, priceHistoryEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PriceHistoryEntity> priceHistoryList = priceHistoryRepository.findAll();
        assertThat(priceHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
