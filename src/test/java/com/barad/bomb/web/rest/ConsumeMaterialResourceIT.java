package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.ConsumeMaterialEntity;
import com.barad.bomb.domain.FoodEntity;
import com.barad.bomb.repository.ConsumeMaterialRepository;
import com.barad.bomb.service.criteria.ConsumeMaterialCriteria;
import com.barad.bomb.service.dto.ConsumeMaterialDTO;
import com.barad.bomb.service.mapper.ConsumeMaterialMapper;
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
 * Integration tests for the {@link ConsumeMaterialResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConsumeMaterialResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;
    private static final Double SMALLER_AMOUNT = 1D - 1D;

    private static final Long DEFAULT_AMOUNT_UNIT_CLASS_ID = 1L;
    private static final Long UPDATED_AMOUNT_UNIT_CLASS_ID = 2L;
    private static final Long SMALLER_AMOUNT_UNIT_CLASS_ID = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/consume-materials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConsumeMaterialRepository consumeMaterialRepository;

    @Autowired
    private ConsumeMaterialMapper consumeMaterialMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsumeMaterialMockMvc;

    private ConsumeMaterialEntity consumeMaterialEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConsumeMaterialEntity createEntity(EntityManager em) {
        ConsumeMaterialEntity consumeMaterialEntity = new ConsumeMaterialEntity()
            .title(DEFAULT_TITLE)
            .type(DEFAULT_TYPE)
            .amount(DEFAULT_AMOUNT)
            .amountUnitClassId(DEFAULT_AMOUNT_UNIT_CLASS_ID);
        // Add required entity
        FoodEntity food;
        if (TestUtil.findAll(em, FoodEntity.class).isEmpty()) {
            food = FoodResourceIT.createEntity(em);
            em.persist(food);
            em.flush();
        } else {
            food = TestUtil.findAll(em, FoodEntity.class).get(0);
        }
        consumeMaterialEntity.setFood(food);
        return consumeMaterialEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConsumeMaterialEntity createUpdatedEntity(EntityManager em) {
        ConsumeMaterialEntity consumeMaterialEntity = new ConsumeMaterialEntity()
            .title(UPDATED_TITLE)
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .amountUnitClassId(UPDATED_AMOUNT_UNIT_CLASS_ID);
        // Add required entity
        FoodEntity food;
        if (TestUtil.findAll(em, FoodEntity.class).isEmpty()) {
            food = FoodResourceIT.createUpdatedEntity(em);
            em.persist(food);
            em.flush();
        } else {
            food = TestUtil.findAll(em, FoodEntity.class).get(0);
        }
        consumeMaterialEntity.setFood(food);
        return consumeMaterialEntity;
    }

    @BeforeEach
    public void initTest() {
        consumeMaterialEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createConsumeMaterial() throws Exception {
        int databaseSizeBeforeCreate = consumeMaterialRepository.findAll().size();
        // Create the ConsumeMaterial
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterialEntity);
        restConsumeMaterialMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterialEntity> consumeMaterialList = consumeMaterialRepository.findAll();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeCreate + 1);
        ConsumeMaterialEntity testConsumeMaterial = consumeMaterialList.get(consumeMaterialList.size() - 1);
        assertThat(testConsumeMaterial.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testConsumeMaterial.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testConsumeMaterial.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testConsumeMaterial.getAmountUnitClassId()).isEqualTo(DEFAULT_AMOUNT_UNIT_CLASS_ID);
    }

    @Test
    @Transactional
    void createConsumeMaterialWithExistingId() throws Exception {
        // Create the ConsumeMaterial with an existing ID
        consumeMaterialEntity.setId(1L);
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterialEntity);

        int databaseSizeBeforeCreate = consumeMaterialRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsumeMaterialMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterialEntity> consumeMaterialList = consumeMaterialRepository.findAll();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = consumeMaterialRepository.findAll().size();
        // set the field null
        consumeMaterialEntity.setTitle(null);

        // Create the ConsumeMaterial, which fails.
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterialEntity);

        restConsumeMaterialMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            )
            .andExpect(status().isBadRequest());

        List<ConsumeMaterialEntity> consumeMaterialList = consumeMaterialRepository.findAll();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = consumeMaterialRepository.findAll().size();
        // set the field null
        consumeMaterialEntity.setAmount(null);

        // Create the ConsumeMaterial, which fails.
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterialEntity);

        restConsumeMaterialMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            )
            .andExpect(status().isBadRequest());

        List<ConsumeMaterialEntity> consumeMaterialList = consumeMaterialRepository.findAll();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountUnitClassIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = consumeMaterialRepository.findAll().size();
        // set the field null
        consumeMaterialEntity.setAmountUnitClassId(null);

        // Create the ConsumeMaterial, which fails.
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterialEntity);

        restConsumeMaterialMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            )
            .andExpect(status().isBadRequest());

        List<ConsumeMaterialEntity> consumeMaterialList = consumeMaterialRepository.findAll();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConsumeMaterials() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList
        restConsumeMaterialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consumeMaterialEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].amountUnitClassId").value(hasItem(DEFAULT_AMOUNT_UNIT_CLASS_ID.intValue())));
    }

    @Test
    @Transactional
    void getConsumeMaterial() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get the consumeMaterial
        restConsumeMaterialMockMvc
            .perform(get(ENTITY_API_URL_ID, consumeMaterialEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consumeMaterialEntity.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.amountUnitClassId").value(DEFAULT_AMOUNT_UNIT_CLASS_ID.intValue()));
    }

    @Test
    @Transactional
    void getConsumeMaterialsByIdFiltering() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        Long id = consumeMaterialEntity.getId();

        defaultConsumeMaterialShouldBeFound("id.equals=" + id);
        defaultConsumeMaterialShouldNotBeFound("id.notEquals=" + id);

        defaultConsumeMaterialShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultConsumeMaterialShouldNotBeFound("id.greaterThan=" + id);

        defaultConsumeMaterialShouldBeFound("id.lessThanOrEqual=" + id);
        defaultConsumeMaterialShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where title equals to DEFAULT_TITLE
        defaultConsumeMaterialShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the consumeMaterialList where title equals to UPDATED_TITLE
        defaultConsumeMaterialShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where title not equals to DEFAULT_TITLE
        defaultConsumeMaterialShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the consumeMaterialList where title not equals to UPDATED_TITLE
        defaultConsumeMaterialShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultConsumeMaterialShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the consumeMaterialList where title equals to UPDATED_TITLE
        defaultConsumeMaterialShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where title is not null
        defaultConsumeMaterialShouldBeFound("title.specified=true");

        // Get all the consumeMaterialList where title is null
        defaultConsumeMaterialShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByTitleContainsSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where title contains DEFAULT_TITLE
        defaultConsumeMaterialShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the consumeMaterialList where title contains UPDATED_TITLE
        defaultConsumeMaterialShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where title does not contain DEFAULT_TITLE
        defaultConsumeMaterialShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the consumeMaterialList where title does not contain UPDATED_TITLE
        defaultConsumeMaterialShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where type equals to DEFAULT_TYPE
        defaultConsumeMaterialShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the consumeMaterialList where type equals to UPDATED_TYPE
        defaultConsumeMaterialShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where type not equals to DEFAULT_TYPE
        defaultConsumeMaterialShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the consumeMaterialList where type not equals to UPDATED_TYPE
        defaultConsumeMaterialShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultConsumeMaterialShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the consumeMaterialList where type equals to UPDATED_TYPE
        defaultConsumeMaterialShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where type is not null
        defaultConsumeMaterialShouldBeFound("type.specified=true");

        // Get all the consumeMaterialList where type is null
        defaultConsumeMaterialShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByTypeContainsSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where type contains DEFAULT_TYPE
        defaultConsumeMaterialShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the consumeMaterialList where type contains UPDATED_TYPE
        defaultConsumeMaterialShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where type does not contain DEFAULT_TYPE
        defaultConsumeMaterialShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the consumeMaterialList where type does not contain UPDATED_TYPE
        defaultConsumeMaterialShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where amount equals to DEFAULT_AMOUNT
        defaultConsumeMaterialShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the consumeMaterialList where amount equals to UPDATED_AMOUNT
        defaultConsumeMaterialShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where amount not equals to DEFAULT_AMOUNT
        defaultConsumeMaterialShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the consumeMaterialList where amount not equals to UPDATED_AMOUNT
        defaultConsumeMaterialShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultConsumeMaterialShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the consumeMaterialList where amount equals to UPDATED_AMOUNT
        defaultConsumeMaterialShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where amount is not null
        defaultConsumeMaterialShouldBeFound("amount.specified=true");

        // Get all the consumeMaterialList where amount is null
        defaultConsumeMaterialShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultConsumeMaterialShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the consumeMaterialList where amount is greater than or equal to UPDATED_AMOUNT
        defaultConsumeMaterialShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where amount is less than or equal to DEFAULT_AMOUNT
        defaultConsumeMaterialShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the consumeMaterialList where amount is less than or equal to SMALLER_AMOUNT
        defaultConsumeMaterialShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where amount is less than DEFAULT_AMOUNT
        defaultConsumeMaterialShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the consumeMaterialList where amount is less than UPDATED_AMOUNT
        defaultConsumeMaterialShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where amount is greater than DEFAULT_AMOUNT
        defaultConsumeMaterialShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the consumeMaterialList where amount is greater than SMALLER_AMOUNT
        defaultConsumeMaterialShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByAmountUnitClassIdIsEqualToSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where amountUnitClassId equals to DEFAULT_AMOUNT_UNIT_CLASS_ID
        defaultConsumeMaterialShouldBeFound("amountUnitClassId.equals=" + DEFAULT_AMOUNT_UNIT_CLASS_ID);

        // Get all the consumeMaterialList where amountUnitClassId equals to UPDATED_AMOUNT_UNIT_CLASS_ID
        defaultConsumeMaterialShouldNotBeFound("amountUnitClassId.equals=" + UPDATED_AMOUNT_UNIT_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByAmountUnitClassIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where amountUnitClassId not equals to DEFAULT_AMOUNT_UNIT_CLASS_ID
        defaultConsumeMaterialShouldNotBeFound("amountUnitClassId.notEquals=" + DEFAULT_AMOUNT_UNIT_CLASS_ID);

        // Get all the consumeMaterialList where amountUnitClassId not equals to UPDATED_AMOUNT_UNIT_CLASS_ID
        defaultConsumeMaterialShouldBeFound("amountUnitClassId.notEquals=" + UPDATED_AMOUNT_UNIT_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByAmountUnitClassIdIsInShouldWork() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where amountUnitClassId in DEFAULT_AMOUNT_UNIT_CLASS_ID or UPDATED_AMOUNT_UNIT_CLASS_ID
        defaultConsumeMaterialShouldBeFound("amountUnitClassId.in=" + DEFAULT_AMOUNT_UNIT_CLASS_ID + "," + UPDATED_AMOUNT_UNIT_CLASS_ID);

        // Get all the consumeMaterialList where amountUnitClassId equals to UPDATED_AMOUNT_UNIT_CLASS_ID
        defaultConsumeMaterialShouldNotBeFound("amountUnitClassId.in=" + UPDATED_AMOUNT_UNIT_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByAmountUnitClassIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where amountUnitClassId is not null
        defaultConsumeMaterialShouldBeFound("amountUnitClassId.specified=true");

        // Get all the consumeMaterialList where amountUnitClassId is null
        defaultConsumeMaterialShouldNotBeFound("amountUnitClassId.specified=false");
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByAmountUnitClassIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where amountUnitClassId is greater than or equal to DEFAULT_AMOUNT_UNIT_CLASS_ID
        defaultConsumeMaterialShouldBeFound("amountUnitClassId.greaterThanOrEqual=" + DEFAULT_AMOUNT_UNIT_CLASS_ID);

        // Get all the consumeMaterialList where amountUnitClassId is greater than or equal to UPDATED_AMOUNT_UNIT_CLASS_ID
        defaultConsumeMaterialShouldNotBeFound("amountUnitClassId.greaterThanOrEqual=" + UPDATED_AMOUNT_UNIT_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByAmountUnitClassIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where amountUnitClassId is less than or equal to DEFAULT_AMOUNT_UNIT_CLASS_ID
        defaultConsumeMaterialShouldBeFound("amountUnitClassId.lessThanOrEqual=" + DEFAULT_AMOUNT_UNIT_CLASS_ID);

        // Get all the consumeMaterialList where amountUnitClassId is less than or equal to SMALLER_AMOUNT_UNIT_CLASS_ID
        defaultConsumeMaterialShouldNotBeFound("amountUnitClassId.lessThanOrEqual=" + SMALLER_AMOUNT_UNIT_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByAmountUnitClassIdIsLessThanSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where amountUnitClassId is less than DEFAULT_AMOUNT_UNIT_CLASS_ID
        defaultConsumeMaterialShouldNotBeFound("amountUnitClassId.lessThan=" + DEFAULT_AMOUNT_UNIT_CLASS_ID);

        // Get all the consumeMaterialList where amountUnitClassId is less than UPDATED_AMOUNT_UNIT_CLASS_ID
        defaultConsumeMaterialShouldBeFound("amountUnitClassId.lessThan=" + UPDATED_AMOUNT_UNIT_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByAmountUnitClassIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        // Get all the consumeMaterialList where amountUnitClassId is greater than DEFAULT_AMOUNT_UNIT_CLASS_ID
        defaultConsumeMaterialShouldNotBeFound("amountUnitClassId.greaterThan=" + DEFAULT_AMOUNT_UNIT_CLASS_ID);

        // Get all the consumeMaterialList where amountUnitClassId is greater than SMALLER_AMOUNT_UNIT_CLASS_ID
        defaultConsumeMaterialShouldBeFound("amountUnitClassId.greaterThan=" + SMALLER_AMOUNT_UNIT_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllConsumeMaterialsByFoodIsEqualToSomething() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);
        FoodEntity food = FoodResourceIT.createEntity(em);
        em.persist(food);
        em.flush();
        consumeMaterialEntity.setFood(food);
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);
        Long foodId = food.getId();

        // Get all the consumeMaterialList where food equals to foodId
        defaultConsumeMaterialShouldBeFound("foodId.equals=" + foodId);

        // Get all the consumeMaterialList where food equals to (foodId + 1)
        defaultConsumeMaterialShouldNotBeFound("foodId.equals=" + (foodId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConsumeMaterialShouldBeFound(String filter) throws Exception {
        restConsumeMaterialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consumeMaterialEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].amountUnitClassId").value(hasItem(DEFAULT_AMOUNT_UNIT_CLASS_ID.intValue())));

        // Check, that the count call also returns 1
        restConsumeMaterialMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConsumeMaterialShouldNotBeFound(String filter) throws Exception {
        restConsumeMaterialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConsumeMaterialMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConsumeMaterial() throws Exception {
        // Get the consumeMaterial
        restConsumeMaterialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConsumeMaterial() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        int databaseSizeBeforeUpdate = consumeMaterialRepository.findAll().size();

        // Update the consumeMaterial
        ConsumeMaterialEntity updatedConsumeMaterialEntity = consumeMaterialRepository.findById(consumeMaterialEntity.getId()).get();
        // Disconnect from session so that the updates on updatedConsumeMaterialEntity are not directly saved in db
        em.detach(updatedConsumeMaterialEntity);
        updatedConsumeMaterialEntity
            .title(UPDATED_TITLE)
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .amountUnitClassId(UPDATED_AMOUNT_UNIT_CLASS_ID);
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(updatedConsumeMaterialEntity);

        restConsumeMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consumeMaterialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            )
            .andExpect(status().isOk());

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterialEntity> consumeMaterialList = consumeMaterialRepository.findAll();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeUpdate);
        ConsumeMaterialEntity testConsumeMaterial = consumeMaterialList.get(consumeMaterialList.size() - 1);
        assertThat(testConsumeMaterial.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testConsumeMaterial.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testConsumeMaterial.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testConsumeMaterial.getAmountUnitClassId()).isEqualTo(UPDATED_AMOUNT_UNIT_CLASS_ID);
    }

    @Test
    @Transactional
    void putNonExistingConsumeMaterial() throws Exception {
        int databaseSizeBeforeUpdate = consumeMaterialRepository.findAll().size();
        consumeMaterialEntity.setId(count.incrementAndGet());

        // Create the ConsumeMaterial
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterialEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsumeMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consumeMaterialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterialEntity> consumeMaterialList = consumeMaterialRepository.findAll();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsumeMaterial() throws Exception {
        int databaseSizeBeforeUpdate = consumeMaterialRepository.findAll().size();
        consumeMaterialEntity.setId(count.incrementAndGet());

        // Create the ConsumeMaterial
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterialEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumeMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterialEntity> consumeMaterialList = consumeMaterialRepository.findAll();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsumeMaterial() throws Exception {
        int databaseSizeBeforeUpdate = consumeMaterialRepository.findAll().size();
        consumeMaterialEntity.setId(count.incrementAndGet());

        // Create the ConsumeMaterial
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterialEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumeMaterialMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterialEntity> consumeMaterialList = consumeMaterialRepository.findAll();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsumeMaterialWithPatch() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        int databaseSizeBeforeUpdate = consumeMaterialRepository.findAll().size();

        // Update the consumeMaterial using partial update
        ConsumeMaterialEntity partialUpdatedConsumeMaterialEntity = new ConsumeMaterialEntity();
        partialUpdatedConsumeMaterialEntity.setId(consumeMaterialEntity.getId());

        partialUpdatedConsumeMaterialEntity.type(UPDATED_TYPE).amount(UPDATED_AMOUNT);

        restConsumeMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsumeMaterialEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsumeMaterialEntity))
            )
            .andExpect(status().isOk());

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterialEntity> consumeMaterialList = consumeMaterialRepository.findAll();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeUpdate);
        ConsumeMaterialEntity testConsumeMaterial = consumeMaterialList.get(consumeMaterialList.size() - 1);
        assertThat(testConsumeMaterial.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testConsumeMaterial.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testConsumeMaterial.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testConsumeMaterial.getAmountUnitClassId()).isEqualTo(DEFAULT_AMOUNT_UNIT_CLASS_ID);
    }

    @Test
    @Transactional
    void fullUpdateConsumeMaterialWithPatch() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        int databaseSizeBeforeUpdate = consumeMaterialRepository.findAll().size();

        // Update the consumeMaterial using partial update
        ConsumeMaterialEntity partialUpdatedConsumeMaterialEntity = new ConsumeMaterialEntity();
        partialUpdatedConsumeMaterialEntity.setId(consumeMaterialEntity.getId());

        partialUpdatedConsumeMaterialEntity
            .title(UPDATED_TITLE)
            .type(UPDATED_TYPE)
            .amount(UPDATED_AMOUNT)
            .amountUnitClassId(UPDATED_AMOUNT_UNIT_CLASS_ID);

        restConsumeMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsumeMaterialEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsumeMaterialEntity))
            )
            .andExpect(status().isOk());

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterialEntity> consumeMaterialList = consumeMaterialRepository.findAll();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeUpdate);
        ConsumeMaterialEntity testConsumeMaterial = consumeMaterialList.get(consumeMaterialList.size() - 1);
        assertThat(testConsumeMaterial.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testConsumeMaterial.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testConsumeMaterial.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testConsumeMaterial.getAmountUnitClassId()).isEqualTo(UPDATED_AMOUNT_UNIT_CLASS_ID);
    }

    @Test
    @Transactional
    void patchNonExistingConsumeMaterial() throws Exception {
        int databaseSizeBeforeUpdate = consumeMaterialRepository.findAll().size();
        consumeMaterialEntity.setId(count.incrementAndGet());

        // Create the ConsumeMaterial
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterialEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsumeMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consumeMaterialDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterialEntity> consumeMaterialList = consumeMaterialRepository.findAll();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsumeMaterial() throws Exception {
        int databaseSizeBeforeUpdate = consumeMaterialRepository.findAll().size();
        consumeMaterialEntity.setId(count.incrementAndGet());

        // Create the ConsumeMaterial
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterialEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumeMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterialEntity> consumeMaterialList = consumeMaterialRepository.findAll();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsumeMaterial() throws Exception {
        int databaseSizeBeforeUpdate = consumeMaterialRepository.findAll().size();
        consumeMaterialEntity.setId(count.incrementAndGet());

        // Create the ConsumeMaterial
        ConsumeMaterialDTO consumeMaterialDTO = consumeMaterialMapper.toDto(consumeMaterialEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumeMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consumeMaterialDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConsumeMaterial in the database
        List<ConsumeMaterialEntity> consumeMaterialList = consumeMaterialRepository.findAll();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsumeMaterial() throws Exception {
        // Initialize the database
        consumeMaterialRepository.saveAndFlush(consumeMaterialEntity);

        int databaseSizeBeforeDelete = consumeMaterialRepository.findAll().size();

        // Delete the consumeMaterial
        restConsumeMaterialMockMvc
            .perform(delete(ENTITY_API_URL_ID, consumeMaterialEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConsumeMaterialEntity> consumeMaterialList = consumeMaterialRepository.findAll();
        assertThat(consumeMaterialList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
