package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.FactorEntity;
import com.barad.bomb.domain.FactorItemEntity;
import com.barad.bomb.domain.FoodEntity;
import com.barad.bomb.repository.FactorItemRepository;
import com.barad.bomb.service.criteria.FactorItemCriteria;
import com.barad.bomb.service.dto.FactorItemDTO;
import com.barad.bomb.service.mapper.FactorItemMapper;
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
 * Integration tests for the {@link FactorItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FactorItemResourceIT {

    private static final Integer DEFAULT_ROW_NUM = 1;
    private static final Integer UPDATED_ROW_NUM = 2;
    private static final Integer SMALLER_ROW_NUM = 1 - 1;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_COUNT = 1;
    private static final Integer UPDATED_COUNT = 2;
    private static final Integer SMALLER_COUNT = 1 - 1;

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;
    private static final Double SMALLER_DISCOUNT = 1D - 1D;

    private static final Double DEFAULT_TAX = 1D;
    private static final Double UPDATED_TAX = 2D;
    private static final Double SMALLER_TAX = 1D - 1D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/factor-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FactorItemRepository factorItemRepository;

    @Autowired
    private FactorItemMapper factorItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFactorItemMockMvc;

    private FactorItemEntity factorItemEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactorItemEntity createEntity(EntityManager em) {
        FactorItemEntity factorItemEntity = new FactorItemEntity()
            .rowNum(DEFAULT_ROW_NUM)
            .title(DEFAULT_TITLE)
            .count(DEFAULT_COUNT)
            .discount(DEFAULT_DISCOUNT)
            .tax(DEFAULT_TAX)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        FoodEntity food;
        if (TestUtil.findAll(em, FoodEntity.class).isEmpty()) {
            food = FoodResourceIT.createEntity(em);
            em.persist(food);
            em.flush();
        } else {
            food = TestUtil.findAll(em, FoodEntity.class).get(0);
        }
        factorItemEntity.setFood(food);
        // Add required entity
        FactorEntity factor;
        if (TestUtil.findAll(em, FactorEntity.class).isEmpty()) {
            factor = FactorResourceIT.createEntity(em);
            em.persist(factor);
            em.flush();
        } else {
            factor = TestUtil.findAll(em, FactorEntity.class).get(0);
        }
        factorItemEntity.setFactor(factor);
        return factorItemEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactorItemEntity createUpdatedEntity(EntityManager em) {
        FactorItemEntity factorItemEntity = new FactorItemEntity()
            .rowNum(UPDATED_ROW_NUM)
            .title(UPDATED_TITLE)
            .count(UPDATED_COUNT)
            .discount(UPDATED_DISCOUNT)
            .tax(UPDATED_TAX)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        FoodEntity food;
        if (TestUtil.findAll(em, FoodEntity.class).isEmpty()) {
            food = FoodResourceIT.createUpdatedEntity(em);
            em.persist(food);
            em.flush();
        } else {
            food = TestUtil.findAll(em, FoodEntity.class).get(0);
        }
        factorItemEntity.setFood(food);
        // Add required entity
        FactorEntity factor;
        if (TestUtil.findAll(em, FactorEntity.class).isEmpty()) {
            factor = FactorResourceIT.createUpdatedEntity(em);
            em.persist(factor);
            em.flush();
        } else {
            factor = TestUtil.findAll(em, FactorEntity.class).get(0);
        }
        factorItemEntity.setFactor(factor);
        return factorItemEntity;
    }

    @BeforeEach
    public void initTest() {
        factorItemEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createFactorItem() throws Exception {
        int databaseSizeBeforeCreate = factorItemRepository.findAll().size();
        // Create the FactorItem
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItemEntity);
        restFactorItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factorItemDTO)))
            .andExpect(status().isCreated());

        // Validate the FactorItem in the database
        List<FactorItemEntity> factorItemList = factorItemRepository.findAll();
        assertThat(factorItemList).hasSize(databaseSizeBeforeCreate + 1);
        FactorItemEntity testFactorItem = factorItemList.get(factorItemList.size() - 1);
        assertThat(testFactorItem.getRowNum()).isEqualTo(DEFAULT_ROW_NUM);
        assertThat(testFactorItem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFactorItem.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testFactorItem.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testFactorItem.getTax()).isEqualTo(DEFAULT_TAX);
        assertThat(testFactorItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createFactorItemWithExistingId() throws Exception {
        // Create the FactorItem with an existing ID
        factorItemEntity.setId(1L);
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItemEntity);

        int databaseSizeBeforeCreate = factorItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactorItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factorItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FactorItem in the database
        List<FactorItemEntity> factorItemList = factorItemRepository.findAll();
        assertThat(factorItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRowNumIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorItemRepository.findAll().size();
        // set the field null
        factorItemEntity.setRowNum(null);

        // Create the FactorItem, which fails.
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItemEntity);

        restFactorItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factorItemDTO)))
            .andExpect(status().isBadRequest());

        List<FactorItemEntity> factorItemList = factorItemRepository.findAll();
        assertThat(factorItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorItemRepository.findAll().size();
        // set the field null
        factorItemEntity.setTitle(null);

        // Create the FactorItem, which fails.
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItemEntity);

        restFactorItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factorItemDTO)))
            .andExpect(status().isBadRequest());

        List<FactorItemEntity> factorItemList = factorItemRepository.findAll();
        assertThat(factorItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorItemRepository.findAll().size();
        // set the field null
        factorItemEntity.setCount(null);

        // Create the FactorItem, which fails.
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItemEntity);

        restFactorItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factorItemDTO)))
            .andExpect(status().isBadRequest());

        List<FactorItemEntity> factorItemList = factorItemRepository.findAll();
        assertThat(factorItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFactorItems() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList
        restFactorItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factorItemEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].rowNum").value(hasItem(DEFAULT_ROW_NUM)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getFactorItem() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get the factorItem
        restFactorItemMockMvc
            .perform(get(ENTITY_API_URL_ID, factorItemEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factorItemEntity.getId().intValue()))
            .andExpect(jsonPath("$.rowNum").value(DEFAULT_ROW_NUM))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.tax").value(DEFAULT_TAX.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getFactorItemsByIdFiltering() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        Long id = factorItemEntity.getId();

        defaultFactorItemShouldBeFound("id.equals=" + id);
        defaultFactorItemShouldNotBeFound("id.notEquals=" + id);

        defaultFactorItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFactorItemShouldNotBeFound("id.greaterThan=" + id);

        defaultFactorItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFactorItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFactorItemsByRowNumIsEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where rowNum equals to DEFAULT_ROW_NUM
        defaultFactorItemShouldBeFound("rowNum.equals=" + DEFAULT_ROW_NUM);

        // Get all the factorItemList where rowNum equals to UPDATED_ROW_NUM
        defaultFactorItemShouldNotBeFound("rowNum.equals=" + UPDATED_ROW_NUM);
    }

    @Test
    @Transactional
    void getAllFactorItemsByRowNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where rowNum not equals to DEFAULT_ROW_NUM
        defaultFactorItemShouldNotBeFound("rowNum.notEquals=" + DEFAULT_ROW_NUM);

        // Get all the factorItemList where rowNum not equals to UPDATED_ROW_NUM
        defaultFactorItemShouldBeFound("rowNum.notEquals=" + UPDATED_ROW_NUM);
    }

    @Test
    @Transactional
    void getAllFactorItemsByRowNumIsInShouldWork() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where rowNum in DEFAULT_ROW_NUM or UPDATED_ROW_NUM
        defaultFactorItemShouldBeFound("rowNum.in=" + DEFAULT_ROW_NUM + "," + UPDATED_ROW_NUM);

        // Get all the factorItemList where rowNum equals to UPDATED_ROW_NUM
        defaultFactorItemShouldNotBeFound("rowNum.in=" + UPDATED_ROW_NUM);
    }

    @Test
    @Transactional
    void getAllFactorItemsByRowNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where rowNum is not null
        defaultFactorItemShouldBeFound("rowNum.specified=true");

        // Get all the factorItemList where rowNum is null
        defaultFactorItemShouldNotBeFound("rowNum.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorItemsByRowNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where rowNum is greater than or equal to DEFAULT_ROW_NUM
        defaultFactorItemShouldBeFound("rowNum.greaterThanOrEqual=" + DEFAULT_ROW_NUM);

        // Get all the factorItemList where rowNum is greater than or equal to UPDATED_ROW_NUM
        defaultFactorItemShouldNotBeFound("rowNum.greaterThanOrEqual=" + UPDATED_ROW_NUM);
    }

    @Test
    @Transactional
    void getAllFactorItemsByRowNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where rowNum is less than or equal to DEFAULT_ROW_NUM
        defaultFactorItemShouldBeFound("rowNum.lessThanOrEqual=" + DEFAULT_ROW_NUM);

        // Get all the factorItemList where rowNum is less than or equal to SMALLER_ROW_NUM
        defaultFactorItemShouldNotBeFound("rowNum.lessThanOrEqual=" + SMALLER_ROW_NUM);
    }

    @Test
    @Transactional
    void getAllFactorItemsByRowNumIsLessThanSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where rowNum is less than DEFAULT_ROW_NUM
        defaultFactorItemShouldNotBeFound("rowNum.lessThan=" + DEFAULT_ROW_NUM);

        // Get all the factorItemList where rowNum is less than UPDATED_ROW_NUM
        defaultFactorItemShouldBeFound("rowNum.lessThan=" + UPDATED_ROW_NUM);
    }

    @Test
    @Transactional
    void getAllFactorItemsByRowNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where rowNum is greater than DEFAULT_ROW_NUM
        defaultFactorItemShouldNotBeFound("rowNum.greaterThan=" + DEFAULT_ROW_NUM);

        // Get all the factorItemList where rowNum is greater than SMALLER_ROW_NUM
        defaultFactorItemShouldBeFound("rowNum.greaterThan=" + SMALLER_ROW_NUM);
    }

    @Test
    @Transactional
    void getAllFactorItemsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where title equals to DEFAULT_TITLE
        defaultFactorItemShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the factorItemList where title equals to UPDATED_TITLE
        defaultFactorItemShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFactorItemsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where title not equals to DEFAULT_TITLE
        defaultFactorItemShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the factorItemList where title not equals to UPDATED_TITLE
        defaultFactorItemShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFactorItemsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultFactorItemShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the factorItemList where title equals to UPDATED_TITLE
        defaultFactorItemShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFactorItemsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where title is not null
        defaultFactorItemShouldBeFound("title.specified=true");

        // Get all the factorItemList where title is null
        defaultFactorItemShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorItemsByTitleContainsSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where title contains DEFAULT_TITLE
        defaultFactorItemShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the factorItemList where title contains UPDATED_TITLE
        defaultFactorItemShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFactorItemsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where title does not contain DEFAULT_TITLE
        defaultFactorItemShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the factorItemList where title does not contain UPDATED_TITLE
        defaultFactorItemShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFactorItemsByCountIsEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where count equals to DEFAULT_COUNT
        defaultFactorItemShouldBeFound("count.equals=" + DEFAULT_COUNT);

        // Get all the factorItemList where count equals to UPDATED_COUNT
        defaultFactorItemShouldNotBeFound("count.equals=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    void getAllFactorItemsByCountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where count not equals to DEFAULT_COUNT
        defaultFactorItemShouldNotBeFound("count.notEquals=" + DEFAULT_COUNT);

        // Get all the factorItemList where count not equals to UPDATED_COUNT
        defaultFactorItemShouldBeFound("count.notEquals=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    void getAllFactorItemsByCountIsInShouldWork() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where count in DEFAULT_COUNT or UPDATED_COUNT
        defaultFactorItemShouldBeFound("count.in=" + DEFAULT_COUNT + "," + UPDATED_COUNT);

        // Get all the factorItemList where count equals to UPDATED_COUNT
        defaultFactorItemShouldNotBeFound("count.in=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    void getAllFactorItemsByCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where count is not null
        defaultFactorItemShouldBeFound("count.specified=true");

        // Get all the factorItemList where count is null
        defaultFactorItemShouldNotBeFound("count.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorItemsByCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where count is greater than or equal to DEFAULT_COUNT
        defaultFactorItemShouldBeFound("count.greaterThanOrEqual=" + DEFAULT_COUNT);

        // Get all the factorItemList where count is greater than or equal to UPDATED_COUNT
        defaultFactorItemShouldNotBeFound("count.greaterThanOrEqual=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    void getAllFactorItemsByCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where count is less than or equal to DEFAULT_COUNT
        defaultFactorItemShouldBeFound("count.lessThanOrEqual=" + DEFAULT_COUNT);

        // Get all the factorItemList where count is less than or equal to SMALLER_COUNT
        defaultFactorItemShouldNotBeFound("count.lessThanOrEqual=" + SMALLER_COUNT);
    }

    @Test
    @Transactional
    void getAllFactorItemsByCountIsLessThanSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where count is less than DEFAULT_COUNT
        defaultFactorItemShouldNotBeFound("count.lessThan=" + DEFAULT_COUNT);

        // Get all the factorItemList where count is less than UPDATED_COUNT
        defaultFactorItemShouldBeFound("count.lessThan=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    void getAllFactorItemsByCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where count is greater than DEFAULT_COUNT
        defaultFactorItemShouldNotBeFound("count.greaterThan=" + DEFAULT_COUNT);

        // Get all the factorItemList where count is greater than SMALLER_COUNT
        defaultFactorItemShouldBeFound("count.greaterThan=" + SMALLER_COUNT);
    }

    @Test
    @Transactional
    void getAllFactorItemsByDiscountIsEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where discount equals to DEFAULT_DISCOUNT
        defaultFactorItemShouldBeFound("discount.equals=" + DEFAULT_DISCOUNT);

        // Get all the factorItemList where discount equals to UPDATED_DISCOUNT
        defaultFactorItemShouldNotBeFound("discount.equals=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllFactorItemsByDiscountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where discount not equals to DEFAULT_DISCOUNT
        defaultFactorItemShouldNotBeFound("discount.notEquals=" + DEFAULT_DISCOUNT);

        // Get all the factorItemList where discount not equals to UPDATED_DISCOUNT
        defaultFactorItemShouldBeFound("discount.notEquals=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllFactorItemsByDiscountIsInShouldWork() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where discount in DEFAULT_DISCOUNT or UPDATED_DISCOUNT
        defaultFactorItemShouldBeFound("discount.in=" + DEFAULT_DISCOUNT + "," + UPDATED_DISCOUNT);

        // Get all the factorItemList where discount equals to UPDATED_DISCOUNT
        defaultFactorItemShouldNotBeFound("discount.in=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllFactorItemsByDiscountIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where discount is not null
        defaultFactorItemShouldBeFound("discount.specified=true");

        // Get all the factorItemList where discount is null
        defaultFactorItemShouldNotBeFound("discount.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorItemsByDiscountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where discount is greater than or equal to DEFAULT_DISCOUNT
        defaultFactorItemShouldBeFound("discount.greaterThanOrEqual=" + DEFAULT_DISCOUNT);

        // Get all the factorItemList where discount is greater than or equal to UPDATED_DISCOUNT
        defaultFactorItemShouldNotBeFound("discount.greaterThanOrEqual=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllFactorItemsByDiscountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where discount is less than or equal to DEFAULT_DISCOUNT
        defaultFactorItemShouldBeFound("discount.lessThanOrEqual=" + DEFAULT_DISCOUNT);

        // Get all the factorItemList where discount is less than or equal to SMALLER_DISCOUNT
        defaultFactorItemShouldNotBeFound("discount.lessThanOrEqual=" + SMALLER_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllFactorItemsByDiscountIsLessThanSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where discount is less than DEFAULT_DISCOUNT
        defaultFactorItemShouldNotBeFound("discount.lessThan=" + DEFAULT_DISCOUNT);

        // Get all the factorItemList where discount is less than UPDATED_DISCOUNT
        defaultFactorItemShouldBeFound("discount.lessThan=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllFactorItemsByDiscountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where discount is greater than DEFAULT_DISCOUNT
        defaultFactorItemShouldNotBeFound("discount.greaterThan=" + DEFAULT_DISCOUNT);

        // Get all the factorItemList where discount is greater than SMALLER_DISCOUNT
        defaultFactorItemShouldBeFound("discount.greaterThan=" + SMALLER_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllFactorItemsByTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where tax equals to DEFAULT_TAX
        defaultFactorItemShouldBeFound("tax.equals=" + DEFAULT_TAX);

        // Get all the factorItemList where tax equals to UPDATED_TAX
        defaultFactorItemShouldNotBeFound("tax.equals=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllFactorItemsByTaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where tax not equals to DEFAULT_TAX
        defaultFactorItemShouldNotBeFound("tax.notEquals=" + DEFAULT_TAX);

        // Get all the factorItemList where tax not equals to UPDATED_TAX
        defaultFactorItemShouldBeFound("tax.notEquals=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllFactorItemsByTaxIsInShouldWork() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where tax in DEFAULT_TAX or UPDATED_TAX
        defaultFactorItemShouldBeFound("tax.in=" + DEFAULT_TAX + "," + UPDATED_TAX);

        // Get all the factorItemList where tax equals to UPDATED_TAX
        defaultFactorItemShouldNotBeFound("tax.in=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllFactorItemsByTaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where tax is not null
        defaultFactorItemShouldBeFound("tax.specified=true");

        // Get all the factorItemList where tax is null
        defaultFactorItemShouldNotBeFound("tax.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorItemsByTaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where tax is greater than or equal to DEFAULT_TAX
        defaultFactorItemShouldBeFound("tax.greaterThanOrEqual=" + DEFAULT_TAX);

        // Get all the factorItemList where tax is greater than or equal to UPDATED_TAX
        defaultFactorItemShouldNotBeFound("tax.greaterThanOrEqual=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllFactorItemsByTaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where tax is less than or equal to DEFAULT_TAX
        defaultFactorItemShouldBeFound("tax.lessThanOrEqual=" + DEFAULT_TAX);

        // Get all the factorItemList where tax is less than or equal to SMALLER_TAX
        defaultFactorItemShouldNotBeFound("tax.lessThanOrEqual=" + SMALLER_TAX);
    }

    @Test
    @Transactional
    void getAllFactorItemsByTaxIsLessThanSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where tax is less than DEFAULT_TAX
        defaultFactorItemShouldNotBeFound("tax.lessThan=" + DEFAULT_TAX);

        // Get all the factorItemList where tax is less than UPDATED_TAX
        defaultFactorItemShouldBeFound("tax.lessThan=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllFactorItemsByTaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where tax is greater than DEFAULT_TAX
        defaultFactorItemShouldNotBeFound("tax.greaterThan=" + DEFAULT_TAX);

        // Get all the factorItemList where tax is greater than SMALLER_TAX
        defaultFactorItemShouldBeFound("tax.greaterThan=" + SMALLER_TAX);
    }

    @Test
    @Transactional
    void getAllFactorItemsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where description equals to DEFAULT_DESCRIPTION
        defaultFactorItemShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the factorItemList where description equals to UPDATED_DESCRIPTION
        defaultFactorItemShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFactorItemsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where description not equals to DEFAULT_DESCRIPTION
        defaultFactorItemShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the factorItemList where description not equals to UPDATED_DESCRIPTION
        defaultFactorItemShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFactorItemsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFactorItemShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the factorItemList where description equals to UPDATED_DESCRIPTION
        defaultFactorItemShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFactorItemsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where description is not null
        defaultFactorItemShouldBeFound("description.specified=true");

        // Get all the factorItemList where description is null
        defaultFactorItemShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorItemsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where description contains DEFAULT_DESCRIPTION
        defaultFactorItemShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the factorItemList where description contains UPDATED_DESCRIPTION
        defaultFactorItemShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFactorItemsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        // Get all the factorItemList where description does not contain DEFAULT_DESCRIPTION
        defaultFactorItemShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the factorItemList where description does not contain UPDATED_DESCRIPTION
        defaultFactorItemShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFactorItemsByFoodIsEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);
        FoodEntity food = FoodResourceIT.createEntity(em);
        em.persist(food);
        em.flush();
        factorItemEntity.setFood(food);
        factorItemRepository.saveAndFlush(factorItemEntity);
        Long foodId = food.getId();

        // Get all the factorItemList where food equals to foodId
        defaultFactorItemShouldBeFound("foodId.equals=" + foodId);

        // Get all the factorItemList where food equals to (foodId + 1)
        defaultFactorItemShouldNotBeFound("foodId.equals=" + (foodId + 1));
    }

    @Test
    @Transactional
    void getAllFactorItemsByFactorIsEqualToSomething() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);
        FactorEntity factor = FactorResourceIT.createEntity(em);
        em.persist(factor);
        em.flush();
        factorItemEntity.setFactor(factor);
        factorItemRepository.saveAndFlush(factorItemEntity);
        Long factorId = factor.getId();

        // Get all the factorItemList where factor equals to factorId
        defaultFactorItemShouldBeFound("factorId.equals=" + factorId);

        // Get all the factorItemList where factor equals to (factorId + 1)
        defaultFactorItemShouldNotBeFound("factorId.equals=" + (factorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFactorItemShouldBeFound(String filter) throws Exception {
        restFactorItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factorItemEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].rowNum").value(hasItem(DEFAULT_ROW_NUM)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restFactorItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFactorItemShouldNotBeFound(String filter) throws Exception {
        restFactorItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFactorItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFactorItem() throws Exception {
        // Get the factorItem
        restFactorItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFactorItem() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        int databaseSizeBeforeUpdate = factorItemRepository.findAll().size();

        // Update the factorItem
        FactorItemEntity updatedFactorItemEntity = factorItemRepository.findById(factorItemEntity.getId()).get();
        // Disconnect from session so that the updates on updatedFactorItemEntity are not directly saved in db
        em.detach(updatedFactorItemEntity);
        updatedFactorItemEntity
            .rowNum(UPDATED_ROW_NUM)
            .title(UPDATED_TITLE)
            .count(UPDATED_COUNT)
            .discount(UPDATED_DISCOUNT)
            .tax(UPDATED_TAX)
            .description(UPDATED_DESCRIPTION);
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(updatedFactorItemEntity);

        restFactorItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factorItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factorItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the FactorItem in the database
        List<FactorItemEntity> factorItemList = factorItemRepository.findAll();
        assertThat(factorItemList).hasSize(databaseSizeBeforeUpdate);
        FactorItemEntity testFactorItem = factorItemList.get(factorItemList.size() - 1);
        assertThat(testFactorItem.getRowNum()).isEqualTo(UPDATED_ROW_NUM);
        assertThat(testFactorItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFactorItem.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testFactorItem.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testFactorItem.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testFactorItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingFactorItem() throws Exception {
        int databaseSizeBeforeUpdate = factorItemRepository.findAll().size();
        factorItemEntity.setId(count.incrementAndGet());

        // Create the FactorItem
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItemEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactorItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factorItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factorItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactorItem in the database
        List<FactorItemEntity> factorItemList = factorItemRepository.findAll();
        assertThat(factorItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFactorItem() throws Exception {
        int databaseSizeBeforeUpdate = factorItemRepository.findAll().size();
        factorItemEntity.setId(count.incrementAndGet());

        // Create the FactorItem
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItemEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactorItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factorItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactorItem in the database
        List<FactorItemEntity> factorItemList = factorItemRepository.findAll();
        assertThat(factorItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFactorItem() throws Exception {
        int databaseSizeBeforeUpdate = factorItemRepository.findAll().size();
        factorItemEntity.setId(count.incrementAndGet());

        // Create the FactorItem
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItemEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactorItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factorItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactorItem in the database
        List<FactorItemEntity> factorItemList = factorItemRepository.findAll();
        assertThat(factorItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFactorItemWithPatch() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        int databaseSizeBeforeUpdate = factorItemRepository.findAll().size();

        // Update the factorItem using partial update
        FactorItemEntity partialUpdatedFactorItemEntity = new FactorItemEntity();
        partialUpdatedFactorItemEntity.setId(factorItemEntity.getId());

        partialUpdatedFactorItemEntity.rowNum(UPDATED_ROW_NUM).count(UPDATED_COUNT).tax(UPDATED_TAX);

        restFactorItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactorItemEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactorItemEntity))
            )
            .andExpect(status().isOk());

        // Validate the FactorItem in the database
        List<FactorItemEntity> factorItemList = factorItemRepository.findAll();
        assertThat(factorItemList).hasSize(databaseSizeBeforeUpdate);
        FactorItemEntity testFactorItem = factorItemList.get(factorItemList.size() - 1);
        assertThat(testFactorItem.getRowNum()).isEqualTo(UPDATED_ROW_NUM);
        assertThat(testFactorItem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFactorItem.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testFactorItem.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testFactorItem.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testFactorItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateFactorItemWithPatch() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        int databaseSizeBeforeUpdate = factorItemRepository.findAll().size();

        // Update the factorItem using partial update
        FactorItemEntity partialUpdatedFactorItemEntity = new FactorItemEntity();
        partialUpdatedFactorItemEntity.setId(factorItemEntity.getId());

        partialUpdatedFactorItemEntity
            .rowNum(UPDATED_ROW_NUM)
            .title(UPDATED_TITLE)
            .count(UPDATED_COUNT)
            .discount(UPDATED_DISCOUNT)
            .tax(UPDATED_TAX)
            .description(UPDATED_DESCRIPTION);

        restFactorItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactorItemEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactorItemEntity))
            )
            .andExpect(status().isOk());

        // Validate the FactorItem in the database
        List<FactorItemEntity> factorItemList = factorItemRepository.findAll();
        assertThat(factorItemList).hasSize(databaseSizeBeforeUpdate);
        FactorItemEntity testFactorItem = factorItemList.get(factorItemList.size() - 1);
        assertThat(testFactorItem.getRowNum()).isEqualTo(UPDATED_ROW_NUM);
        assertThat(testFactorItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFactorItem.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testFactorItem.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testFactorItem.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testFactorItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingFactorItem() throws Exception {
        int databaseSizeBeforeUpdate = factorItemRepository.findAll().size();
        factorItemEntity.setId(count.incrementAndGet());

        // Create the FactorItem
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItemEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactorItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factorItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factorItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactorItem in the database
        List<FactorItemEntity> factorItemList = factorItemRepository.findAll();
        assertThat(factorItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFactorItem() throws Exception {
        int databaseSizeBeforeUpdate = factorItemRepository.findAll().size();
        factorItemEntity.setId(count.incrementAndGet());

        // Create the FactorItem
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItemEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactorItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factorItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactorItem in the database
        List<FactorItemEntity> factorItemList = factorItemRepository.findAll();
        assertThat(factorItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFactorItem() throws Exception {
        int databaseSizeBeforeUpdate = factorItemRepository.findAll().size();
        factorItemEntity.setId(count.incrementAndGet());

        // Create the FactorItem
        FactorItemDTO factorItemDTO = factorItemMapper.toDto(factorItemEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactorItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(factorItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactorItem in the database
        List<FactorItemEntity> factorItemList = factorItemRepository.findAll();
        assertThat(factorItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFactorItem() throws Exception {
        // Initialize the database
        factorItemRepository.saveAndFlush(factorItemEntity);

        int databaseSizeBeforeDelete = factorItemRepository.findAll().size();

        // Delete the factorItem
        restFactorItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, factorItemEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FactorItemEntity> factorItemList = factorItemRepository.findAll();
        assertThat(factorItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
