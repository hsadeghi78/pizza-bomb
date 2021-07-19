package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.ConsumeMaterialEntity;
import com.barad.bomb.domain.FactorItemEntity;
import com.barad.bomb.domain.FoodEntity;
import com.barad.bomb.domain.FoodTypeEntity;
import com.barad.bomb.domain.MenuItemEntity;
import com.barad.bomb.domain.PartyEntity;
import com.barad.bomb.repository.FoodRepository;
import com.barad.bomb.service.criteria.FoodCriteria;
import com.barad.bomb.service.dto.FoodDTO;
import com.barad.bomb.service.mapper.FoodMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link FoodResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FoodResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_FOOD_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FOOD_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_SIZE_CLASS_ID = 1L;
    private static final Long UPDATED_SIZE_CLASS_ID = 2L;
    private static final Long SMALLER_SIZE_CLASS_ID = 1L - 1L;

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final Long DEFAULT_CATEGORY_CLASS_ID = 1L;
    private static final Long UPDATED_CATEGORY_CLASS_ID = 2L;
    private static final Long SMALLER_CATEGORY_CLASS_ID = 1L - 1L;

    private static final Double DEFAULT_LAST_PRICE = 1D;
    private static final Double UPDATED_LAST_PRICE = 2D;
    private static final Double SMALLER_LAST_PRICE = 1D - 1D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/foods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFoodMockMvc;

    private FoodEntity foodEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodEntity createEntity(EntityManager em) {
        FoodEntity foodEntity = new FoodEntity()
            .title(DEFAULT_TITLE)
            .foodCode(DEFAULT_FOOD_CODE)
            .sizeClassId(DEFAULT_SIZE_CLASS_ID)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .categoryClassId(DEFAULT_CATEGORY_CLASS_ID)
            .lastPrice(DEFAULT_LAST_PRICE)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        PartyEntity party;
        if (TestUtil.findAll(em, PartyEntity.class).isEmpty()) {
            party = PartyResourceIT.createEntity(em);
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, PartyEntity.class).get(0);
        }
        foodEntity.setProducerParty(party);
        // Add required entity
        FoodTypeEntity foodType;
        if (TestUtil.findAll(em, FoodTypeEntity.class).isEmpty()) {
            foodType = FoodTypeResourceIT.createEntity(em);
            em.persist(foodType);
            em.flush();
        } else {
            foodType = TestUtil.findAll(em, FoodTypeEntity.class).get(0);
        }
        foodEntity.setFoodType(foodType);
        return foodEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodEntity createUpdatedEntity(EntityManager em) {
        FoodEntity foodEntity = new FoodEntity()
            .title(UPDATED_TITLE)
            .foodCode(UPDATED_FOOD_CODE)
            .sizeClassId(UPDATED_SIZE_CLASS_ID)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .categoryClassId(UPDATED_CATEGORY_CLASS_ID)
            .lastPrice(UPDATED_LAST_PRICE)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        PartyEntity party;
        if (TestUtil.findAll(em, PartyEntity.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity(em);
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, PartyEntity.class).get(0);
        }
        foodEntity.setProducerParty(party);
        // Add required entity
        FoodTypeEntity foodType;
        if (TestUtil.findAll(em, FoodTypeEntity.class).isEmpty()) {
            foodType = FoodTypeResourceIT.createUpdatedEntity(em);
            em.persist(foodType);
            em.flush();
        } else {
            foodType = TestUtil.findAll(em, FoodTypeEntity.class).get(0);
        }
        foodEntity.setFoodType(foodType);
        return foodEntity;
    }

    @BeforeEach
    public void initTest() {
        foodEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createFood() throws Exception {
        int databaseSizeBeforeCreate = foodRepository.findAll().size();
        // Create the Food
        FoodDTO foodDTO = foodMapper.toDto(foodEntity);
        restFoodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodDTO)))
            .andExpect(status().isCreated());

        // Validate the Food in the database
        List<FoodEntity> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeCreate + 1);
        FoodEntity testFood = foodList.get(foodList.size() - 1);
        assertThat(testFood.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFood.getFoodCode()).isEqualTo(DEFAULT_FOOD_CODE);
        assertThat(testFood.getSizeClassId()).isEqualTo(DEFAULT_SIZE_CLASS_ID);
        assertThat(testFood.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testFood.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testFood.getCategoryClassId()).isEqualTo(DEFAULT_CATEGORY_CLASS_ID);
        assertThat(testFood.getLastPrice()).isEqualTo(DEFAULT_LAST_PRICE);
        assertThat(testFood.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createFoodWithExistingId() throws Exception {
        // Create the Food with an existing ID
        foodEntity.setId(1L);
        FoodDTO foodDTO = foodMapper.toDto(foodEntity);

        int databaseSizeBeforeCreate = foodRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFoodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Food in the database
        List<FoodEntity> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = foodRepository.findAll().size();
        // set the field null
        foodEntity.setTitle(null);

        // Create the Food, which fails.
        FoodDTO foodDTO = foodMapper.toDto(foodEntity);

        restFoodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodDTO)))
            .andExpect(status().isBadRequest());

        List<FoodEntity> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFoodCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = foodRepository.findAll().size();
        // set the field null
        foodEntity.setFoodCode(null);

        // Create the Food, which fails.
        FoodDTO foodDTO = foodMapper.toDto(foodEntity);

        restFoodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodDTO)))
            .andExpect(status().isBadRequest());

        List<FoodEntity> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = foodRepository.findAll().size();
        // set the field null
        foodEntity.setLastPrice(null);

        // Create the Food, which fails.
        FoodDTO foodDTO = foodMapper.toDto(foodEntity);

        restFoodMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodDTO)))
            .andExpect(status().isBadRequest());

        List<FoodEntity> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFoods() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList
        restFoodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(foodEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].foodCode").value(hasItem(DEFAULT_FOOD_CODE)))
            .andExpect(jsonPath("$.[*].sizeClassId").value(hasItem(DEFAULT_SIZE_CLASS_ID.intValue())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].categoryClassId").value(hasItem(DEFAULT_CATEGORY_CLASS_ID.intValue())))
            .andExpect(jsonPath("$.[*].lastPrice").value(hasItem(DEFAULT_LAST_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getFood() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get the food
        restFoodMockMvc
            .perform(get(ENTITY_API_URL_ID, foodEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(foodEntity.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.foodCode").value(DEFAULT_FOOD_CODE))
            .andExpect(jsonPath("$.sizeClassId").value(DEFAULT_SIZE_CLASS_ID.intValue()))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.categoryClassId").value(DEFAULT_CATEGORY_CLASS_ID.intValue()))
            .andExpect(jsonPath("$.lastPrice").value(DEFAULT_LAST_PRICE.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getFoodsByIdFiltering() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        Long id = foodEntity.getId();

        defaultFoodShouldBeFound("id.equals=" + id);
        defaultFoodShouldNotBeFound("id.notEquals=" + id);

        defaultFoodShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFoodShouldNotBeFound("id.greaterThan=" + id);

        defaultFoodShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFoodShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFoodsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where title equals to DEFAULT_TITLE
        defaultFoodShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the foodList where title equals to UPDATED_TITLE
        defaultFoodShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFoodsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where title not equals to DEFAULT_TITLE
        defaultFoodShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the foodList where title not equals to UPDATED_TITLE
        defaultFoodShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFoodsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultFoodShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the foodList where title equals to UPDATED_TITLE
        defaultFoodShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFoodsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where title is not null
        defaultFoodShouldBeFound("title.specified=true");

        // Get all the foodList where title is null
        defaultFoodShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllFoodsByTitleContainsSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where title contains DEFAULT_TITLE
        defaultFoodShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the foodList where title contains UPDATED_TITLE
        defaultFoodShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFoodsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where title does not contain DEFAULT_TITLE
        defaultFoodShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the foodList where title does not contain UPDATED_TITLE
        defaultFoodShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFoodsByFoodCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where foodCode equals to DEFAULT_FOOD_CODE
        defaultFoodShouldBeFound("foodCode.equals=" + DEFAULT_FOOD_CODE);

        // Get all the foodList where foodCode equals to UPDATED_FOOD_CODE
        defaultFoodShouldNotBeFound("foodCode.equals=" + UPDATED_FOOD_CODE);
    }

    @Test
    @Transactional
    void getAllFoodsByFoodCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where foodCode not equals to DEFAULT_FOOD_CODE
        defaultFoodShouldNotBeFound("foodCode.notEquals=" + DEFAULT_FOOD_CODE);

        // Get all the foodList where foodCode not equals to UPDATED_FOOD_CODE
        defaultFoodShouldBeFound("foodCode.notEquals=" + UPDATED_FOOD_CODE);
    }

    @Test
    @Transactional
    void getAllFoodsByFoodCodeIsInShouldWork() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where foodCode in DEFAULT_FOOD_CODE or UPDATED_FOOD_CODE
        defaultFoodShouldBeFound("foodCode.in=" + DEFAULT_FOOD_CODE + "," + UPDATED_FOOD_CODE);

        // Get all the foodList where foodCode equals to UPDATED_FOOD_CODE
        defaultFoodShouldNotBeFound("foodCode.in=" + UPDATED_FOOD_CODE);
    }

    @Test
    @Transactional
    void getAllFoodsByFoodCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where foodCode is not null
        defaultFoodShouldBeFound("foodCode.specified=true");

        // Get all the foodList where foodCode is null
        defaultFoodShouldNotBeFound("foodCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFoodsByFoodCodeContainsSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where foodCode contains DEFAULT_FOOD_CODE
        defaultFoodShouldBeFound("foodCode.contains=" + DEFAULT_FOOD_CODE);

        // Get all the foodList where foodCode contains UPDATED_FOOD_CODE
        defaultFoodShouldNotBeFound("foodCode.contains=" + UPDATED_FOOD_CODE);
    }

    @Test
    @Transactional
    void getAllFoodsByFoodCodeNotContainsSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where foodCode does not contain DEFAULT_FOOD_CODE
        defaultFoodShouldNotBeFound("foodCode.doesNotContain=" + DEFAULT_FOOD_CODE);

        // Get all the foodList where foodCode does not contain UPDATED_FOOD_CODE
        defaultFoodShouldBeFound("foodCode.doesNotContain=" + UPDATED_FOOD_CODE);
    }

    @Test
    @Transactional
    void getAllFoodsBySizeClassIdIsEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where sizeClassId equals to DEFAULT_SIZE_CLASS_ID
        defaultFoodShouldBeFound("sizeClassId.equals=" + DEFAULT_SIZE_CLASS_ID);

        // Get all the foodList where sizeClassId equals to UPDATED_SIZE_CLASS_ID
        defaultFoodShouldNotBeFound("sizeClassId.equals=" + UPDATED_SIZE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFoodsBySizeClassIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where sizeClassId not equals to DEFAULT_SIZE_CLASS_ID
        defaultFoodShouldNotBeFound("sizeClassId.notEquals=" + DEFAULT_SIZE_CLASS_ID);

        // Get all the foodList where sizeClassId not equals to UPDATED_SIZE_CLASS_ID
        defaultFoodShouldBeFound("sizeClassId.notEquals=" + UPDATED_SIZE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFoodsBySizeClassIdIsInShouldWork() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where sizeClassId in DEFAULT_SIZE_CLASS_ID or UPDATED_SIZE_CLASS_ID
        defaultFoodShouldBeFound("sizeClassId.in=" + DEFAULT_SIZE_CLASS_ID + "," + UPDATED_SIZE_CLASS_ID);

        // Get all the foodList where sizeClassId equals to UPDATED_SIZE_CLASS_ID
        defaultFoodShouldNotBeFound("sizeClassId.in=" + UPDATED_SIZE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFoodsBySizeClassIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where sizeClassId is not null
        defaultFoodShouldBeFound("sizeClassId.specified=true");

        // Get all the foodList where sizeClassId is null
        defaultFoodShouldNotBeFound("sizeClassId.specified=false");
    }

    @Test
    @Transactional
    void getAllFoodsBySizeClassIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where sizeClassId is greater than or equal to DEFAULT_SIZE_CLASS_ID
        defaultFoodShouldBeFound("sizeClassId.greaterThanOrEqual=" + DEFAULT_SIZE_CLASS_ID);

        // Get all the foodList where sizeClassId is greater than or equal to UPDATED_SIZE_CLASS_ID
        defaultFoodShouldNotBeFound("sizeClassId.greaterThanOrEqual=" + UPDATED_SIZE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFoodsBySizeClassIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where sizeClassId is less than or equal to DEFAULT_SIZE_CLASS_ID
        defaultFoodShouldBeFound("sizeClassId.lessThanOrEqual=" + DEFAULT_SIZE_CLASS_ID);

        // Get all the foodList where sizeClassId is less than or equal to SMALLER_SIZE_CLASS_ID
        defaultFoodShouldNotBeFound("sizeClassId.lessThanOrEqual=" + SMALLER_SIZE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFoodsBySizeClassIdIsLessThanSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where sizeClassId is less than DEFAULT_SIZE_CLASS_ID
        defaultFoodShouldNotBeFound("sizeClassId.lessThan=" + DEFAULT_SIZE_CLASS_ID);

        // Get all the foodList where sizeClassId is less than UPDATED_SIZE_CLASS_ID
        defaultFoodShouldBeFound("sizeClassId.lessThan=" + UPDATED_SIZE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFoodsBySizeClassIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where sizeClassId is greater than DEFAULT_SIZE_CLASS_ID
        defaultFoodShouldNotBeFound("sizeClassId.greaterThan=" + DEFAULT_SIZE_CLASS_ID);

        // Get all the foodList where sizeClassId is greater than SMALLER_SIZE_CLASS_ID
        defaultFoodShouldBeFound("sizeClassId.greaterThan=" + SMALLER_SIZE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFoodsByCategoryClassIdIsEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where categoryClassId equals to DEFAULT_CATEGORY_CLASS_ID
        defaultFoodShouldBeFound("categoryClassId.equals=" + DEFAULT_CATEGORY_CLASS_ID);

        // Get all the foodList where categoryClassId equals to UPDATED_CATEGORY_CLASS_ID
        defaultFoodShouldNotBeFound("categoryClassId.equals=" + UPDATED_CATEGORY_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFoodsByCategoryClassIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where categoryClassId not equals to DEFAULT_CATEGORY_CLASS_ID
        defaultFoodShouldNotBeFound("categoryClassId.notEquals=" + DEFAULT_CATEGORY_CLASS_ID);

        // Get all the foodList where categoryClassId not equals to UPDATED_CATEGORY_CLASS_ID
        defaultFoodShouldBeFound("categoryClassId.notEquals=" + UPDATED_CATEGORY_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFoodsByCategoryClassIdIsInShouldWork() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where categoryClassId in DEFAULT_CATEGORY_CLASS_ID or UPDATED_CATEGORY_CLASS_ID
        defaultFoodShouldBeFound("categoryClassId.in=" + DEFAULT_CATEGORY_CLASS_ID + "," + UPDATED_CATEGORY_CLASS_ID);

        // Get all the foodList where categoryClassId equals to UPDATED_CATEGORY_CLASS_ID
        defaultFoodShouldNotBeFound("categoryClassId.in=" + UPDATED_CATEGORY_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFoodsByCategoryClassIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where categoryClassId is not null
        defaultFoodShouldBeFound("categoryClassId.specified=true");

        // Get all the foodList where categoryClassId is null
        defaultFoodShouldNotBeFound("categoryClassId.specified=false");
    }

    @Test
    @Transactional
    void getAllFoodsByCategoryClassIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where categoryClassId is greater than or equal to DEFAULT_CATEGORY_CLASS_ID
        defaultFoodShouldBeFound("categoryClassId.greaterThanOrEqual=" + DEFAULT_CATEGORY_CLASS_ID);

        // Get all the foodList where categoryClassId is greater than or equal to UPDATED_CATEGORY_CLASS_ID
        defaultFoodShouldNotBeFound("categoryClassId.greaterThanOrEqual=" + UPDATED_CATEGORY_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFoodsByCategoryClassIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where categoryClassId is less than or equal to DEFAULT_CATEGORY_CLASS_ID
        defaultFoodShouldBeFound("categoryClassId.lessThanOrEqual=" + DEFAULT_CATEGORY_CLASS_ID);

        // Get all the foodList where categoryClassId is less than or equal to SMALLER_CATEGORY_CLASS_ID
        defaultFoodShouldNotBeFound("categoryClassId.lessThanOrEqual=" + SMALLER_CATEGORY_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFoodsByCategoryClassIdIsLessThanSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where categoryClassId is less than DEFAULT_CATEGORY_CLASS_ID
        defaultFoodShouldNotBeFound("categoryClassId.lessThan=" + DEFAULT_CATEGORY_CLASS_ID);

        // Get all the foodList where categoryClassId is less than UPDATED_CATEGORY_CLASS_ID
        defaultFoodShouldBeFound("categoryClassId.lessThan=" + UPDATED_CATEGORY_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFoodsByCategoryClassIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where categoryClassId is greater than DEFAULT_CATEGORY_CLASS_ID
        defaultFoodShouldNotBeFound("categoryClassId.greaterThan=" + DEFAULT_CATEGORY_CLASS_ID);

        // Get all the foodList where categoryClassId is greater than SMALLER_CATEGORY_CLASS_ID
        defaultFoodShouldBeFound("categoryClassId.greaterThan=" + SMALLER_CATEGORY_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFoodsByLastPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where lastPrice equals to DEFAULT_LAST_PRICE
        defaultFoodShouldBeFound("lastPrice.equals=" + DEFAULT_LAST_PRICE);

        // Get all the foodList where lastPrice equals to UPDATED_LAST_PRICE
        defaultFoodShouldNotBeFound("lastPrice.equals=" + UPDATED_LAST_PRICE);
    }

    @Test
    @Transactional
    void getAllFoodsByLastPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where lastPrice not equals to DEFAULT_LAST_PRICE
        defaultFoodShouldNotBeFound("lastPrice.notEquals=" + DEFAULT_LAST_PRICE);

        // Get all the foodList where lastPrice not equals to UPDATED_LAST_PRICE
        defaultFoodShouldBeFound("lastPrice.notEquals=" + UPDATED_LAST_PRICE);
    }

    @Test
    @Transactional
    void getAllFoodsByLastPriceIsInShouldWork() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where lastPrice in DEFAULT_LAST_PRICE or UPDATED_LAST_PRICE
        defaultFoodShouldBeFound("lastPrice.in=" + DEFAULT_LAST_PRICE + "," + UPDATED_LAST_PRICE);

        // Get all the foodList where lastPrice equals to UPDATED_LAST_PRICE
        defaultFoodShouldNotBeFound("lastPrice.in=" + UPDATED_LAST_PRICE);
    }

    @Test
    @Transactional
    void getAllFoodsByLastPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where lastPrice is not null
        defaultFoodShouldBeFound("lastPrice.specified=true");

        // Get all the foodList where lastPrice is null
        defaultFoodShouldNotBeFound("lastPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllFoodsByLastPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where lastPrice is greater than or equal to DEFAULT_LAST_PRICE
        defaultFoodShouldBeFound("lastPrice.greaterThanOrEqual=" + DEFAULT_LAST_PRICE);

        // Get all the foodList where lastPrice is greater than or equal to UPDATED_LAST_PRICE
        defaultFoodShouldNotBeFound("lastPrice.greaterThanOrEqual=" + UPDATED_LAST_PRICE);
    }

    @Test
    @Transactional
    void getAllFoodsByLastPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where lastPrice is less than or equal to DEFAULT_LAST_PRICE
        defaultFoodShouldBeFound("lastPrice.lessThanOrEqual=" + DEFAULT_LAST_PRICE);

        // Get all the foodList where lastPrice is less than or equal to SMALLER_LAST_PRICE
        defaultFoodShouldNotBeFound("lastPrice.lessThanOrEqual=" + SMALLER_LAST_PRICE);
    }

    @Test
    @Transactional
    void getAllFoodsByLastPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where lastPrice is less than DEFAULT_LAST_PRICE
        defaultFoodShouldNotBeFound("lastPrice.lessThan=" + DEFAULT_LAST_PRICE);

        // Get all the foodList where lastPrice is less than UPDATED_LAST_PRICE
        defaultFoodShouldBeFound("lastPrice.lessThan=" + UPDATED_LAST_PRICE);
    }

    @Test
    @Transactional
    void getAllFoodsByLastPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where lastPrice is greater than DEFAULT_LAST_PRICE
        defaultFoodShouldNotBeFound("lastPrice.greaterThan=" + DEFAULT_LAST_PRICE);

        // Get all the foodList where lastPrice is greater than SMALLER_LAST_PRICE
        defaultFoodShouldBeFound("lastPrice.greaterThan=" + SMALLER_LAST_PRICE);
    }

    @Test
    @Transactional
    void getAllFoodsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where description equals to DEFAULT_DESCRIPTION
        defaultFoodShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the foodList where description equals to UPDATED_DESCRIPTION
        defaultFoodShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFoodsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where description not equals to DEFAULT_DESCRIPTION
        defaultFoodShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the foodList where description not equals to UPDATED_DESCRIPTION
        defaultFoodShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFoodsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFoodShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the foodList where description equals to UPDATED_DESCRIPTION
        defaultFoodShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFoodsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where description is not null
        defaultFoodShouldBeFound("description.specified=true");

        // Get all the foodList where description is null
        defaultFoodShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllFoodsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where description contains DEFAULT_DESCRIPTION
        defaultFoodShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the foodList where description contains UPDATED_DESCRIPTION
        defaultFoodShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFoodsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        // Get all the foodList where description does not contain DEFAULT_DESCRIPTION
        defaultFoodShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the foodList where description does not contain UPDATED_DESCRIPTION
        defaultFoodShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFoodsByMenuItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);
        MenuItemEntity menuItems = MenuItemResourceIT.createEntity(em);
        em.persist(menuItems);
        em.flush();
        foodEntity.addMenuItems(menuItems);
        foodRepository.saveAndFlush(foodEntity);
        Long menuItemsId = menuItems.getId();

        // Get all the foodList where menuItems equals to menuItemsId
        defaultFoodShouldBeFound("menuItemsId.equals=" + menuItemsId);

        // Get all the foodList where menuItems equals to (menuItemsId + 1)
        defaultFoodShouldNotBeFound("menuItemsId.equals=" + (menuItemsId + 1));
    }

    @Test
    @Transactional
    void getAllFoodsByFactorItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);
        FactorItemEntity factorItems = FactorItemResourceIT.createEntity(em);
        em.persist(factorItems);
        em.flush();
        foodEntity.addFactorItems(factorItems);
        foodRepository.saveAndFlush(foodEntity);
        Long factorItemsId = factorItems.getId();

        // Get all the foodList where factorItems equals to factorItemsId
        defaultFoodShouldBeFound("factorItemsId.equals=" + factorItemsId);

        // Get all the foodList where factorItems equals to (factorItemsId + 1)
        defaultFoodShouldNotBeFound("factorItemsId.equals=" + (factorItemsId + 1));
    }

    @Test
    @Transactional
    void getAllFoodsByMaterialsIsEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);
        ConsumeMaterialEntity materials = ConsumeMaterialResourceIT.createEntity(em);
        em.persist(materials);
        em.flush();
        foodEntity.addMaterials(materials);
        foodRepository.saveAndFlush(foodEntity);
        Long materialsId = materials.getId();

        // Get all the foodList where materials equals to materialsId
        defaultFoodShouldBeFound("materialsId.equals=" + materialsId);

        // Get all the foodList where materials equals to (materialsId + 1)
        defaultFoodShouldNotBeFound("materialsId.equals=" + (materialsId + 1));
    }

    @Test
    @Transactional
    void getAllFoodsByProducerPartyIsEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);
        PartyEntity producerParty = PartyResourceIT.createEntity(em);
        em.persist(producerParty);
        em.flush();
        foodEntity.setProducerParty(producerParty);
        foodRepository.saveAndFlush(foodEntity);
        Long producerPartyId = producerParty.getId();

        // Get all the foodList where producerParty equals to producerPartyId
        defaultFoodShouldBeFound("producerPartyId.equals=" + producerPartyId);

        // Get all the foodList where producerParty equals to (producerPartyId + 1)
        defaultFoodShouldNotBeFound("producerPartyId.equals=" + (producerPartyId + 1));
    }

    @Test
    @Transactional
    void getAllFoodsByDesignerPartyIsEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);
        PartyEntity designerParty = PartyResourceIT.createEntity(em);
        em.persist(designerParty);
        em.flush();
        foodEntity.setDesignerParty(designerParty);
        foodRepository.saveAndFlush(foodEntity);
        Long designerPartyId = designerParty.getId();

        // Get all the foodList where designerParty equals to designerPartyId
        defaultFoodShouldBeFound("designerPartyId.equals=" + designerPartyId);

        // Get all the foodList where designerParty equals to (designerPartyId + 1)
        defaultFoodShouldNotBeFound("designerPartyId.equals=" + (designerPartyId + 1));
    }

    @Test
    @Transactional
    void getAllFoodsByFoodTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);
        FoodTypeEntity foodType = FoodTypeResourceIT.createEntity(em);
        em.persist(foodType);
        em.flush();
        foodEntity.setFoodType(foodType);
        foodRepository.saveAndFlush(foodEntity);
        Long foodTypeId = foodType.getId();

        // Get all the foodList where foodType equals to foodTypeId
        defaultFoodShouldBeFound("foodTypeId.equals=" + foodTypeId);

        // Get all the foodList where foodType equals to (foodTypeId + 1)
        defaultFoodShouldNotBeFound("foodTypeId.equals=" + (foodTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFoodShouldBeFound(String filter) throws Exception {
        restFoodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(foodEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].foodCode").value(hasItem(DEFAULT_FOOD_CODE)))
            .andExpect(jsonPath("$.[*].sizeClassId").value(hasItem(DEFAULT_SIZE_CLASS_ID.intValue())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].categoryClassId").value(hasItem(DEFAULT_CATEGORY_CLASS_ID.intValue())))
            .andExpect(jsonPath("$.[*].lastPrice").value(hasItem(DEFAULT_LAST_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restFoodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFoodShouldNotBeFound(String filter) throws Exception {
        restFoodMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFoodMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFood() throws Exception {
        // Get the food
        restFoodMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFood() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        int databaseSizeBeforeUpdate = foodRepository.findAll().size();

        // Update the food
        FoodEntity updatedFoodEntity = foodRepository.findById(foodEntity.getId()).get();
        // Disconnect from session so that the updates on updatedFoodEntity are not directly saved in db
        em.detach(updatedFoodEntity);
        updatedFoodEntity
            .title(UPDATED_TITLE)
            .foodCode(UPDATED_FOOD_CODE)
            .sizeClassId(UPDATED_SIZE_CLASS_ID)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .categoryClassId(UPDATED_CATEGORY_CLASS_ID)
            .lastPrice(UPDATED_LAST_PRICE)
            .description(UPDATED_DESCRIPTION);
        FoodDTO foodDTO = foodMapper.toDto(updatedFoodEntity);

        restFoodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, foodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foodDTO))
            )
            .andExpect(status().isOk());

        // Validate the Food in the database
        List<FoodEntity> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeUpdate);
        FoodEntity testFood = foodList.get(foodList.size() - 1);
        assertThat(testFood.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFood.getFoodCode()).isEqualTo(UPDATED_FOOD_CODE);
        assertThat(testFood.getSizeClassId()).isEqualTo(UPDATED_SIZE_CLASS_ID);
        assertThat(testFood.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testFood.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testFood.getCategoryClassId()).isEqualTo(UPDATED_CATEGORY_CLASS_ID);
        assertThat(testFood.getLastPrice()).isEqualTo(UPDATED_LAST_PRICE);
        assertThat(testFood.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingFood() throws Exception {
        int databaseSizeBeforeUpdate = foodRepository.findAll().size();
        foodEntity.setId(count.incrementAndGet());

        // Create the Food
        FoodDTO foodDTO = foodMapper.toDto(foodEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, foodDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Food in the database
        List<FoodEntity> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFood() throws Exception {
        int databaseSizeBeforeUpdate = foodRepository.findAll().size();
        foodEntity.setId(count.incrementAndGet());

        // Create the Food
        FoodDTO foodDTO = foodMapper.toDto(foodEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Food in the database
        List<FoodEntity> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFood() throws Exception {
        int databaseSizeBeforeUpdate = foodRepository.findAll().size();
        foodEntity.setId(count.incrementAndGet());

        // Create the Food
        FoodDTO foodDTO = foodMapper.toDto(foodEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Food in the database
        List<FoodEntity> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFoodWithPatch() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        int databaseSizeBeforeUpdate = foodRepository.findAll().size();

        // Update the food using partial update
        FoodEntity partialUpdatedFoodEntity = new FoodEntity();
        partialUpdatedFoodEntity.setId(foodEntity.getId());

        partialUpdatedFoodEntity
            .foodCode(UPDATED_FOOD_CODE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION);

        restFoodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoodEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFoodEntity))
            )
            .andExpect(status().isOk());

        // Validate the Food in the database
        List<FoodEntity> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeUpdate);
        FoodEntity testFood = foodList.get(foodList.size() - 1);
        assertThat(testFood.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFood.getFoodCode()).isEqualTo(UPDATED_FOOD_CODE);
        assertThat(testFood.getSizeClassId()).isEqualTo(DEFAULT_SIZE_CLASS_ID);
        assertThat(testFood.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testFood.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testFood.getCategoryClassId()).isEqualTo(DEFAULT_CATEGORY_CLASS_ID);
        assertThat(testFood.getLastPrice()).isEqualTo(DEFAULT_LAST_PRICE);
        assertThat(testFood.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateFoodWithPatch() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        int databaseSizeBeforeUpdate = foodRepository.findAll().size();

        // Update the food using partial update
        FoodEntity partialUpdatedFoodEntity = new FoodEntity();
        partialUpdatedFoodEntity.setId(foodEntity.getId());

        partialUpdatedFoodEntity
            .title(UPDATED_TITLE)
            .foodCode(UPDATED_FOOD_CODE)
            .sizeClassId(UPDATED_SIZE_CLASS_ID)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .categoryClassId(UPDATED_CATEGORY_CLASS_ID)
            .lastPrice(UPDATED_LAST_PRICE)
            .description(UPDATED_DESCRIPTION);

        restFoodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoodEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFoodEntity))
            )
            .andExpect(status().isOk());

        // Validate the Food in the database
        List<FoodEntity> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeUpdate);
        FoodEntity testFood = foodList.get(foodList.size() - 1);
        assertThat(testFood.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFood.getFoodCode()).isEqualTo(UPDATED_FOOD_CODE);
        assertThat(testFood.getSizeClassId()).isEqualTo(UPDATED_SIZE_CLASS_ID);
        assertThat(testFood.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testFood.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testFood.getCategoryClassId()).isEqualTo(UPDATED_CATEGORY_CLASS_ID);
        assertThat(testFood.getLastPrice()).isEqualTo(UPDATED_LAST_PRICE);
        assertThat(testFood.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingFood() throws Exception {
        int databaseSizeBeforeUpdate = foodRepository.findAll().size();
        foodEntity.setId(count.incrementAndGet());

        // Create the Food
        FoodDTO foodDTO = foodMapper.toDto(foodEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, foodDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(foodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Food in the database
        List<FoodEntity> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFood() throws Exception {
        int databaseSizeBeforeUpdate = foodRepository.findAll().size();
        foodEntity.setId(count.incrementAndGet());

        // Create the Food
        FoodDTO foodDTO = foodMapper.toDto(foodEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(foodDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Food in the database
        List<FoodEntity> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFood() throws Exception {
        int databaseSizeBeforeUpdate = foodRepository.findAll().size();
        foodEntity.setId(count.incrementAndGet());

        // Create the Food
        FoodDTO foodDTO = foodMapper.toDto(foodEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(foodDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Food in the database
        List<FoodEntity> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFood() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(foodEntity);

        int databaseSizeBeforeDelete = foodRepository.findAll().size();

        // Delete the food
        restFoodMockMvc
            .perform(delete(ENTITY_API_URL_ID, foodEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FoodEntity> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
