package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.AddressEntity;
import com.barad.bomb.domain.FactorEntity;
import com.barad.bomb.domain.FactorItemEntity;
import com.barad.bomb.domain.PartyEntity;
import com.barad.bomb.domain.enumeration.FactorOrderWay;
import com.barad.bomb.domain.enumeration.FactorServing;
import com.barad.bomb.domain.enumeration.FactorStatus;
import com.barad.bomb.repository.FactorRepository;
import com.barad.bomb.service.criteria.FactorCriteria;
import com.barad.bomb.service.dto.FactorDTO;
import com.barad.bomb.service.mapper.FactorMapper;
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
 * Integration tests for the {@link FactorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FactorResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_FACTOR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FACTOR_CODE = "BBBBBBBBBB";

    private static final FactorStatus DEFAULT_LAST_STATUS = FactorStatus.INITIATE;
    private static final FactorStatus UPDATED_LAST_STATUS = FactorStatus.PRINTED;

    private static final FactorOrderWay DEFAULT_ORDER_WAY = FactorOrderWay.PHONE_CALL;
    private static final FactorOrderWay UPDATED_ORDER_WAY = FactorOrderWay.IN_PERSON;

    private static final FactorServing DEFAULT_SERVING = FactorServing.INSIDE;
    private static final FactorServing UPDATED_SERVING = FactorServing.OUTSIDE;

    private static final Long DEFAULT_PAYMENT_STATE_CLASS_ID = 1L;
    private static final Long UPDATED_PAYMENT_STATE_CLASS_ID = 2L;
    private static final Long SMALLER_PAYMENT_STATE_CLASS_ID = 1L - 1L;

    private static final Long DEFAULT_CATEGORY_CLASS_ID = 1L;
    private static final Long UPDATED_CATEGORY_CLASS_ID = 2L;
    private static final Long SMALLER_CATEGORY_CLASS_ID = 1L - 1L;

    private static final Double DEFAULT_TOTAL_PRICE = 1D;
    private static final Double UPDATED_TOTAL_PRICE = 2D;
    private static final Double SMALLER_TOTAL_PRICE = 1D - 1D;

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;
    private static final Double SMALLER_DISCOUNT = 1D - 1D;

    private static final Double DEFAULT_TAX = 1D;
    private static final Double UPDATED_TAX = 2D;
    private static final Double SMALLER_TAX = 1D - 1D;

    private static final Double DEFAULT_NETPRICE = 1D;
    private static final Double UPDATED_NETPRICE = 2D;
    private static final Double SMALLER_NETPRICE = 1D - 1D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/factors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FactorRepository factorRepository;

    @Autowired
    private FactorMapper factorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFactorMockMvc;

    private FactorEntity factorEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactorEntity createEntity(EntityManager em) {
        FactorEntity factorEntity = new FactorEntity()
            .title(DEFAULT_TITLE)
            .factorCode(DEFAULT_FACTOR_CODE)
            .lastStatus(DEFAULT_LAST_STATUS)
            .orderWay(DEFAULT_ORDER_WAY)
            .serving(DEFAULT_SERVING)
            .paymentStateClassId(DEFAULT_PAYMENT_STATE_CLASS_ID)
            .categoryClassId(DEFAULT_CATEGORY_CLASS_ID)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .discount(DEFAULT_DISCOUNT)
            .tax(DEFAULT_TAX)
            .netprice(DEFAULT_NETPRICE)
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
        factorEntity.setBuyerParty(party);
        // Add required entity
        factorEntity.setSellerParty(party);
        return factorEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactorEntity createUpdatedEntity(EntityManager em) {
        FactorEntity factorEntity = new FactorEntity()
            .title(UPDATED_TITLE)
            .factorCode(UPDATED_FACTOR_CODE)
            .lastStatus(UPDATED_LAST_STATUS)
            .orderWay(UPDATED_ORDER_WAY)
            .serving(UPDATED_SERVING)
            .paymentStateClassId(UPDATED_PAYMENT_STATE_CLASS_ID)
            .categoryClassId(UPDATED_CATEGORY_CLASS_ID)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .discount(UPDATED_DISCOUNT)
            .tax(UPDATED_TAX)
            .netprice(UPDATED_NETPRICE)
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
        factorEntity.setBuyerParty(party);
        // Add required entity
        factorEntity.setSellerParty(party);
        return factorEntity;
    }

    @BeforeEach
    public void initTest() {
        factorEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createFactor() throws Exception {
        int databaseSizeBeforeCreate = factorRepository.findAll().size();
        // Create the Factor
        FactorDTO factorDTO = factorMapper.toDto(factorEntity);
        restFactorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factorDTO)))
            .andExpect(status().isCreated());

        // Validate the Factor in the database
        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeCreate + 1);
        FactorEntity testFactor = factorList.get(factorList.size() - 1);
        assertThat(testFactor.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFactor.getFactorCode()).isEqualTo(DEFAULT_FACTOR_CODE);
        assertThat(testFactor.getLastStatus()).isEqualTo(DEFAULT_LAST_STATUS);
        assertThat(testFactor.getOrderWay()).isEqualTo(DEFAULT_ORDER_WAY);
        assertThat(testFactor.getServing()).isEqualTo(DEFAULT_SERVING);
        assertThat(testFactor.getPaymentStateClassId()).isEqualTo(DEFAULT_PAYMENT_STATE_CLASS_ID);
        assertThat(testFactor.getCategoryClassId()).isEqualTo(DEFAULT_CATEGORY_CLASS_ID);
        assertThat(testFactor.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testFactor.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testFactor.getTax()).isEqualTo(DEFAULT_TAX);
        assertThat(testFactor.getNetprice()).isEqualTo(DEFAULT_NETPRICE);
        assertThat(testFactor.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createFactorWithExistingId() throws Exception {
        // Create the Factor with an existing ID
        factorEntity.setId(1L);
        FactorDTO factorDTO = factorMapper.toDto(factorEntity);

        int databaseSizeBeforeCreate = factorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Factor in the database
        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorRepository.findAll().size();
        // set the field null
        factorEntity.setTitle(null);

        // Create the Factor, which fails.
        FactorDTO factorDTO = factorMapper.toDto(factorEntity);

        restFactorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factorDTO)))
            .andExpect(status().isBadRequest());

        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFactorCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorRepository.findAll().size();
        // set the field null
        factorEntity.setFactorCode(null);

        // Create the Factor, which fails.
        FactorDTO factorDTO = factorMapper.toDto(factorEntity);

        restFactorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factorDTO)))
            .andExpect(status().isBadRequest());

        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorRepository.findAll().size();
        // set the field null
        factorEntity.setLastStatus(null);

        // Create the Factor, which fails.
        FactorDTO factorDTO = factorMapper.toDto(factorEntity);

        restFactorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factorDTO)))
            .andExpect(status().isBadRequest());

        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrderWayIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorRepository.findAll().size();
        // set the field null
        factorEntity.setOrderWay(null);

        // Create the Factor, which fails.
        FactorDTO factorDTO = factorMapper.toDto(factorEntity);

        restFactorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factorDTO)))
            .andExpect(status().isBadRequest());

        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkServingIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorRepository.findAll().size();
        // set the field null
        factorEntity.setServing(null);

        // Create the Factor, which fails.
        FactorDTO factorDTO = factorMapper.toDto(factorEntity);

        restFactorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factorDTO)))
            .andExpect(status().isBadRequest());

        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentStateClassIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorRepository.findAll().size();
        // set the field null
        factorEntity.setPaymentStateClassId(null);

        // Create the Factor, which fails.
        FactorDTO factorDTO = factorMapper.toDto(factorEntity);

        restFactorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factorDTO)))
            .andExpect(status().isBadRequest());

        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorRepository.findAll().size();
        // set the field null
        factorEntity.setTotalPrice(null);

        // Create the Factor, which fails.
        FactorDTO factorDTO = factorMapper.toDto(factorEntity);

        restFactorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factorDTO)))
            .andExpect(status().isBadRequest());

        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNetpriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = factorRepository.findAll().size();
        // set the field null
        factorEntity.setNetprice(null);

        // Create the Factor, which fails.
        FactorDTO factorDTO = factorMapper.toDto(factorEntity);

        restFactorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factorDTO)))
            .andExpect(status().isBadRequest());

        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFactors() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList
        restFactorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factorEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].factorCode").value(hasItem(DEFAULT_FACTOR_CODE)))
            .andExpect(jsonPath("$.[*].lastStatus").value(hasItem(DEFAULT_LAST_STATUS.toString())))
            .andExpect(jsonPath("$.[*].orderWay").value(hasItem(DEFAULT_ORDER_WAY.toString())))
            .andExpect(jsonPath("$.[*].serving").value(hasItem(DEFAULT_SERVING.toString())))
            .andExpect(jsonPath("$.[*].paymentStateClassId").value(hasItem(DEFAULT_PAYMENT_STATE_CLASS_ID.intValue())))
            .andExpect(jsonPath("$.[*].categoryClassId").value(hasItem(DEFAULT_CATEGORY_CLASS_ID.intValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].netprice").value(hasItem(DEFAULT_NETPRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getFactor() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get the factor
        restFactorMockMvc
            .perform(get(ENTITY_API_URL_ID, factorEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factorEntity.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.factorCode").value(DEFAULT_FACTOR_CODE))
            .andExpect(jsonPath("$.lastStatus").value(DEFAULT_LAST_STATUS.toString()))
            .andExpect(jsonPath("$.orderWay").value(DEFAULT_ORDER_WAY.toString()))
            .andExpect(jsonPath("$.serving").value(DEFAULT_SERVING.toString()))
            .andExpect(jsonPath("$.paymentStateClassId").value(DEFAULT_PAYMENT_STATE_CLASS_ID.intValue()))
            .andExpect(jsonPath("$.categoryClassId").value(DEFAULT_CATEGORY_CLASS_ID.intValue()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.tax").value(DEFAULT_TAX.doubleValue()))
            .andExpect(jsonPath("$.netprice").value(DEFAULT_NETPRICE.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getFactorsByIdFiltering() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        Long id = factorEntity.getId();

        defaultFactorShouldBeFound("id.equals=" + id);
        defaultFactorShouldNotBeFound("id.notEquals=" + id);

        defaultFactorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFactorShouldNotBeFound("id.greaterThan=" + id);

        defaultFactorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFactorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFactorsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where title equals to DEFAULT_TITLE
        defaultFactorShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the factorList where title equals to UPDATED_TITLE
        defaultFactorShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFactorsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where title not equals to DEFAULT_TITLE
        defaultFactorShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the factorList where title not equals to UPDATED_TITLE
        defaultFactorShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFactorsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultFactorShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the factorList where title equals to UPDATED_TITLE
        defaultFactorShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFactorsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where title is not null
        defaultFactorShouldBeFound("title.specified=true");

        // Get all the factorList where title is null
        defaultFactorShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorsByTitleContainsSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where title contains DEFAULT_TITLE
        defaultFactorShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the factorList where title contains UPDATED_TITLE
        defaultFactorShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFactorsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where title does not contain DEFAULT_TITLE
        defaultFactorShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the factorList where title does not contain UPDATED_TITLE
        defaultFactorShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFactorsByFactorCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where factorCode equals to DEFAULT_FACTOR_CODE
        defaultFactorShouldBeFound("factorCode.equals=" + DEFAULT_FACTOR_CODE);

        // Get all the factorList where factorCode equals to UPDATED_FACTOR_CODE
        defaultFactorShouldNotBeFound("factorCode.equals=" + UPDATED_FACTOR_CODE);
    }

    @Test
    @Transactional
    void getAllFactorsByFactorCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where factorCode not equals to DEFAULT_FACTOR_CODE
        defaultFactorShouldNotBeFound("factorCode.notEquals=" + DEFAULT_FACTOR_CODE);

        // Get all the factorList where factorCode not equals to UPDATED_FACTOR_CODE
        defaultFactorShouldBeFound("factorCode.notEquals=" + UPDATED_FACTOR_CODE);
    }

    @Test
    @Transactional
    void getAllFactorsByFactorCodeIsInShouldWork() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where factorCode in DEFAULT_FACTOR_CODE or UPDATED_FACTOR_CODE
        defaultFactorShouldBeFound("factorCode.in=" + DEFAULT_FACTOR_CODE + "," + UPDATED_FACTOR_CODE);

        // Get all the factorList where factorCode equals to UPDATED_FACTOR_CODE
        defaultFactorShouldNotBeFound("factorCode.in=" + UPDATED_FACTOR_CODE);
    }

    @Test
    @Transactional
    void getAllFactorsByFactorCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where factorCode is not null
        defaultFactorShouldBeFound("factorCode.specified=true");

        // Get all the factorList where factorCode is null
        defaultFactorShouldNotBeFound("factorCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorsByFactorCodeContainsSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where factorCode contains DEFAULT_FACTOR_CODE
        defaultFactorShouldBeFound("factorCode.contains=" + DEFAULT_FACTOR_CODE);

        // Get all the factorList where factorCode contains UPDATED_FACTOR_CODE
        defaultFactorShouldNotBeFound("factorCode.contains=" + UPDATED_FACTOR_CODE);
    }

    @Test
    @Transactional
    void getAllFactorsByFactorCodeNotContainsSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where factorCode does not contain DEFAULT_FACTOR_CODE
        defaultFactorShouldNotBeFound("factorCode.doesNotContain=" + DEFAULT_FACTOR_CODE);

        // Get all the factorList where factorCode does not contain UPDATED_FACTOR_CODE
        defaultFactorShouldBeFound("factorCode.doesNotContain=" + UPDATED_FACTOR_CODE);
    }

    @Test
    @Transactional
    void getAllFactorsByLastStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where lastStatus equals to DEFAULT_LAST_STATUS
        defaultFactorShouldBeFound("lastStatus.equals=" + DEFAULT_LAST_STATUS);

        // Get all the factorList where lastStatus equals to UPDATED_LAST_STATUS
        defaultFactorShouldNotBeFound("lastStatus.equals=" + UPDATED_LAST_STATUS);
    }

    @Test
    @Transactional
    void getAllFactorsByLastStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where lastStatus not equals to DEFAULT_LAST_STATUS
        defaultFactorShouldNotBeFound("lastStatus.notEquals=" + DEFAULT_LAST_STATUS);

        // Get all the factorList where lastStatus not equals to UPDATED_LAST_STATUS
        defaultFactorShouldBeFound("lastStatus.notEquals=" + UPDATED_LAST_STATUS);
    }

    @Test
    @Transactional
    void getAllFactorsByLastStatusIsInShouldWork() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where lastStatus in DEFAULT_LAST_STATUS or UPDATED_LAST_STATUS
        defaultFactorShouldBeFound("lastStatus.in=" + DEFAULT_LAST_STATUS + "," + UPDATED_LAST_STATUS);

        // Get all the factorList where lastStatus equals to UPDATED_LAST_STATUS
        defaultFactorShouldNotBeFound("lastStatus.in=" + UPDATED_LAST_STATUS);
    }

    @Test
    @Transactional
    void getAllFactorsByLastStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where lastStatus is not null
        defaultFactorShouldBeFound("lastStatus.specified=true");

        // Get all the factorList where lastStatus is null
        defaultFactorShouldNotBeFound("lastStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorsByOrderWayIsEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where orderWay equals to DEFAULT_ORDER_WAY
        defaultFactorShouldBeFound("orderWay.equals=" + DEFAULT_ORDER_WAY);

        // Get all the factorList where orderWay equals to UPDATED_ORDER_WAY
        defaultFactorShouldNotBeFound("orderWay.equals=" + UPDATED_ORDER_WAY);
    }

    @Test
    @Transactional
    void getAllFactorsByOrderWayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where orderWay not equals to DEFAULT_ORDER_WAY
        defaultFactorShouldNotBeFound("orderWay.notEquals=" + DEFAULT_ORDER_WAY);

        // Get all the factorList where orderWay not equals to UPDATED_ORDER_WAY
        defaultFactorShouldBeFound("orderWay.notEquals=" + UPDATED_ORDER_WAY);
    }

    @Test
    @Transactional
    void getAllFactorsByOrderWayIsInShouldWork() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where orderWay in DEFAULT_ORDER_WAY or UPDATED_ORDER_WAY
        defaultFactorShouldBeFound("orderWay.in=" + DEFAULT_ORDER_WAY + "," + UPDATED_ORDER_WAY);

        // Get all the factorList where orderWay equals to UPDATED_ORDER_WAY
        defaultFactorShouldNotBeFound("orderWay.in=" + UPDATED_ORDER_WAY);
    }

    @Test
    @Transactional
    void getAllFactorsByOrderWayIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where orderWay is not null
        defaultFactorShouldBeFound("orderWay.specified=true");

        // Get all the factorList where orderWay is null
        defaultFactorShouldNotBeFound("orderWay.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorsByServingIsEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where serving equals to DEFAULT_SERVING
        defaultFactorShouldBeFound("serving.equals=" + DEFAULT_SERVING);

        // Get all the factorList where serving equals to UPDATED_SERVING
        defaultFactorShouldNotBeFound("serving.equals=" + UPDATED_SERVING);
    }

    @Test
    @Transactional
    void getAllFactorsByServingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where serving not equals to DEFAULT_SERVING
        defaultFactorShouldNotBeFound("serving.notEquals=" + DEFAULT_SERVING);

        // Get all the factorList where serving not equals to UPDATED_SERVING
        defaultFactorShouldBeFound("serving.notEquals=" + UPDATED_SERVING);
    }

    @Test
    @Transactional
    void getAllFactorsByServingIsInShouldWork() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where serving in DEFAULT_SERVING or UPDATED_SERVING
        defaultFactorShouldBeFound("serving.in=" + DEFAULT_SERVING + "," + UPDATED_SERVING);

        // Get all the factorList where serving equals to UPDATED_SERVING
        defaultFactorShouldNotBeFound("serving.in=" + UPDATED_SERVING);
    }

    @Test
    @Transactional
    void getAllFactorsByServingIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where serving is not null
        defaultFactorShouldBeFound("serving.specified=true");

        // Get all the factorList where serving is null
        defaultFactorShouldNotBeFound("serving.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorsByPaymentStateClassIdIsEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where paymentStateClassId equals to DEFAULT_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldBeFound("paymentStateClassId.equals=" + DEFAULT_PAYMENT_STATE_CLASS_ID);

        // Get all the factorList where paymentStateClassId equals to UPDATED_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldNotBeFound("paymentStateClassId.equals=" + UPDATED_PAYMENT_STATE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFactorsByPaymentStateClassIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where paymentStateClassId not equals to DEFAULT_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldNotBeFound("paymentStateClassId.notEquals=" + DEFAULT_PAYMENT_STATE_CLASS_ID);

        // Get all the factorList where paymentStateClassId not equals to UPDATED_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldBeFound("paymentStateClassId.notEquals=" + UPDATED_PAYMENT_STATE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFactorsByPaymentStateClassIdIsInShouldWork() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where paymentStateClassId in DEFAULT_PAYMENT_STATE_CLASS_ID or UPDATED_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldBeFound("paymentStateClassId.in=" + DEFAULT_PAYMENT_STATE_CLASS_ID + "," + UPDATED_PAYMENT_STATE_CLASS_ID);

        // Get all the factorList where paymentStateClassId equals to UPDATED_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldNotBeFound("paymentStateClassId.in=" + UPDATED_PAYMENT_STATE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFactorsByPaymentStateClassIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where paymentStateClassId is not null
        defaultFactorShouldBeFound("paymentStateClassId.specified=true");

        // Get all the factorList where paymentStateClassId is null
        defaultFactorShouldNotBeFound("paymentStateClassId.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorsByPaymentStateClassIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where paymentStateClassId is greater than or equal to DEFAULT_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldBeFound("paymentStateClassId.greaterThanOrEqual=" + DEFAULT_PAYMENT_STATE_CLASS_ID);

        // Get all the factorList where paymentStateClassId is greater than or equal to UPDATED_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldNotBeFound("paymentStateClassId.greaterThanOrEqual=" + UPDATED_PAYMENT_STATE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFactorsByPaymentStateClassIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where paymentStateClassId is less than or equal to DEFAULT_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldBeFound("paymentStateClassId.lessThanOrEqual=" + DEFAULT_PAYMENT_STATE_CLASS_ID);

        // Get all the factorList where paymentStateClassId is less than or equal to SMALLER_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldNotBeFound("paymentStateClassId.lessThanOrEqual=" + SMALLER_PAYMENT_STATE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFactorsByPaymentStateClassIdIsLessThanSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where paymentStateClassId is less than DEFAULT_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldNotBeFound("paymentStateClassId.lessThan=" + DEFAULT_PAYMENT_STATE_CLASS_ID);

        // Get all the factorList where paymentStateClassId is less than UPDATED_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldBeFound("paymentStateClassId.lessThan=" + UPDATED_PAYMENT_STATE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFactorsByPaymentStateClassIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where paymentStateClassId is greater than DEFAULT_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldNotBeFound("paymentStateClassId.greaterThan=" + DEFAULT_PAYMENT_STATE_CLASS_ID);

        // Get all the factorList where paymentStateClassId is greater than SMALLER_PAYMENT_STATE_CLASS_ID
        defaultFactorShouldBeFound("paymentStateClassId.greaterThan=" + SMALLER_PAYMENT_STATE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFactorsByCategoryClassIdIsEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where categoryClassId equals to DEFAULT_CATEGORY_CLASS_ID
        defaultFactorShouldBeFound("categoryClassId.equals=" + DEFAULT_CATEGORY_CLASS_ID);

        // Get all the factorList where categoryClassId equals to UPDATED_CATEGORY_CLASS_ID
        defaultFactorShouldNotBeFound("categoryClassId.equals=" + UPDATED_CATEGORY_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFactorsByCategoryClassIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where categoryClassId not equals to DEFAULT_CATEGORY_CLASS_ID
        defaultFactorShouldNotBeFound("categoryClassId.notEquals=" + DEFAULT_CATEGORY_CLASS_ID);

        // Get all the factorList where categoryClassId not equals to UPDATED_CATEGORY_CLASS_ID
        defaultFactorShouldBeFound("categoryClassId.notEquals=" + UPDATED_CATEGORY_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFactorsByCategoryClassIdIsInShouldWork() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where categoryClassId in DEFAULT_CATEGORY_CLASS_ID or UPDATED_CATEGORY_CLASS_ID
        defaultFactorShouldBeFound("categoryClassId.in=" + DEFAULT_CATEGORY_CLASS_ID + "," + UPDATED_CATEGORY_CLASS_ID);

        // Get all the factorList where categoryClassId equals to UPDATED_CATEGORY_CLASS_ID
        defaultFactorShouldNotBeFound("categoryClassId.in=" + UPDATED_CATEGORY_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFactorsByCategoryClassIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where categoryClassId is not null
        defaultFactorShouldBeFound("categoryClassId.specified=true");

        // Get all the factorList where categoryClassId is null
        defaultFactorShouldNotBeFound("categoryClassId.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorsByCategoryClassIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where categoryClassId is greater than or equal to DEFAULT_CATEGORY_CLASS_ID
        defaultFactorShouldBeFound("categoryClassId.greaterThanOrEqual=" + DEFAULT_CATEGORY_CLASS_ID);

        // Get all the factorList where categoryClassId is greater than or equal to UPDATED_CATEGORY_CLASS_ID
        defaultFactorShouldNotBeFound("categoryClassId.greaterThanOrEqual=" + UPDATED_CATEGORY_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFactorsByCategoryClassIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where categoryClassId is less than or equal to DEFAULT_CATEGORY_CLASS_ID
        defaultFactorShouldBeFound("categoryClassId.lessThanOrEqual=" + DEFAULT_CATEGORY_CLASS_ID);

        // Get all the factorList where categoryClassId is less than or equal to SMALLER_CATEGORY_CLASS_ID
        defaultFactorShouldNotBeFound("categoryClassId.lessThanOrEqual=" + SMALLER_CATEGORY_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFactorsByCategoryClassIdIsLessThanSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where categoryClassId is less than DEFAULT_CATEGORY_CLASS_ID
        defaultFactorShouldNotBeFound("categoryClassId.lessThan=" + DEFAULT_CATEGORY_CLASS_ID);

        // Get all the factorList where categoryClassId is less than UPDATED_CATEGORY_CLASS_ID
        defaultFactorShouldBeFound("categoryClassId.lessThan=" + UPDATED_CATEGORY_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFactorsByCategoryClassIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where categoryClassId is greater than DEFAULT_CATEGORY_CLASS_ID
        defaultFactorShouldNotBeFound("categoryClassId.greaterThan=" + DEFAULT_CATEGORY_CLASS_ID);

        // Get all the factorList where categoryClassId is greater than SMALLER_CATEGORY_CLASS_ID
        defaultFactorShouldBeFound("categoryClassId.greaterThan=" + SMALLER_CATEGORY_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllFactorsByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where totalPrice equals to DEFAULT_TOTAL_PRICE
        defaultFactorShouldBeFound("totalPrice.equals=" + DEFAULT_TOTAL_PRICE);

        // Get all the factorList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultFactorShouldNotBeFound("totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllFactorsByTotalPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where totalPrice not equals to DEFAULT_TOTAL_PRICE
        defaultFactorShouldNotBeFound("totalPrice.notEquals=" + DEFAULT_TOTAL_PRICE);

        // Get all the factorList where totalPrice not equals to UPDATED_TOTAL_PRICE
        defaultFactorShouldBeFound("totalPrice.notEquals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllFactorsByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where totalPrice in DEFAULT_TOTAL_PRICE or UPDATED_TOTAL_PRICE
        defaultFactorShouldBeFound("totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE);

        // Get all the factorList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultFactorShouldNotBeFound("totalPrice.in=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllFactorsByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where totalPrice is not null
        defaultFactorShouldBeFound("totalPrice.specified=true");

        // Get all the factorList where totalPrice is null
        defaultFactorShouldNotBeFound("totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorsByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where totalPrice is greater than or equal to DEFAULT_TOTAL_PRICE
        defaultFactorShouldBeFound("totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the factorList where totalPrice is greater than or equal to UPDATED_TOTAL_PRICE
        defaultFactorShouldNotBeFound("totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllFactorsByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where totalPrice is less than or equal to DEFAULT_TOTAL_PRICE
        defaultFactorShouldBeFound("totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE);

        // Get all the factorList where totalPrice is less than or equal to SMALLER_TOTAL_PRICE
        defaultFactorShouldNotBeFound("totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllFactorsByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where totalPrice is less than DEFAULT_TOTAL_PRICE
        defaultFactorShouldNotBeFound("totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the factorList where totalPrice is less than UPDATED_TOTAL_PRICE
        defaultFactorShouldBeFound("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllFactorsByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where totalPrice is greater than DEFAULT_TOTAL_PRICE
        defaultFactorShouldNotBeFound("totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);

        // Get all the factorList where totalPrice is greater than SMALLER_TOTAL_PRICE
        defaultFactorShouldBeFound("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllFactorsByDiscountIsEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where discount equals to DEFAULT_DISCOUNT
        defaultFactorShouldBeFound("discount.equals=" + DEFAULT_DISCOUNT);

        // Get all the factorList where discount equals to UPDATED_DISCOUNT
        defaultFactorShouldNotBeFound("discount.equals=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllFactorsByDiscountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where discount not equals to DEFAULT_DISCOUNT
        defaultFactorShouldNotBeFound("discount.notEquals=" + DEFAULT_DISCOUNT);

        // Get all the factorList where discount not equals to UPDATED_DISCOUNT
        defaultFactorShouldBeFound("discount.notEquals=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllFactorsByDiscountIsInShouldWork() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where discount in DEFAULT_DISCOUNT or UPDATED_DISCOUNT
        defaultFactorShouldBeFound("discount.in=" + DEFAULT_DISCOUNT + "," + UPDATED_DISCOUNT);

        // Get all the factorList where discount equals to UPDATED_DISCOUNT
        defaultFactorShouldNotBeFound("discount.in=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllFactorsByDiscountIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where discount is not null
        defaultFactorShouldBeFound("discount.specified=true");

        // Get all the factorList where discount is null
        defaultFactorShouldNotBeFound("discount.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorsByDiscountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where discount is greater than or equal to DEFAULT_DISCOUNT
        defaultFactorShouldBeFound("discount.greaterThanOrEqual=" + DEFAULT_DISCOUNT);

        // Get all the factorList where discount is greater than or equal to UPDATED_DISCOUNT
        defaultFactorShouldNotBeFound("discount.greaterThanOrEqual=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllFactorsByDiscountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where discount is less than or equal to DEFAULT_DISCOUNT
        defaultFactorShouldBeFound("discount.lessThanOrEqual=" + DEFAULT_DISCOUNT);

        // Get all the factorList where discount is less than or equal to SMALLER_DISCOUNT
        defaultFactorShouldNotBeFound("discount.lessThanOrEqual=" + SMALLER_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllFactorsByDiscountIsLessThanSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where discount is less than DEFAULT_DISCOUNT
        defaultFactorShouldNotBeFound("discount.lessThan=" + DEFAULT_DISCOUNT);

        // Get all the factorList where discount is less than UPDATED_DISCOUNT
        defaultFactorShouldBeFound("discount.lessThan=" + UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllFactorsByDiscountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where discount is greater than DEFAULT_DISCOUNT
        defaultFactorShouldNotBeFound("discount.greaterThan=" + DEFAULT_DISCOUNT);

        // Get all the factorList where discount is greater than SMALLER_DISCOUNT
        defaultFactorShouldBeFound("discount.greaterThan=" + SMALLER_DISCOUNT);
    }

    @Test
    @Transactional
    void getAllFactorsByTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where tax equals to DEFAULT_TAX
        defaultFactorShouldBeFound("tax.equals=" + DEFAULT_TAX);

        // Get all the factorList where tax equals to UPDATED_TAX
        defaultFactorShouldNotBeFound("tax.equals=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllFactorsByTaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where tax not equals to DEFAULT_TAX
        defaultFactorShouldNotBeFound("tax.notEquals=" + DEFAULT_TAX);

        // Get all the factorList where tax not equals to UPDATED_TAX
        defaultFactorShouldBeFound("tax.notEquals=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllFactorsByTaxIsInShouldWork() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where tax in DEFAULT_TAX or UPDATED_TAX
        defaultFactorShouldBeFound("tax.in=" + DEFAULT_TAX + "," + UPDATED_TAX);

        // Get all the factorList where tax equals to UPDATED_TAX
        defaultFactorShouldNotBeFound("tax.in=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllFactorsByTaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where tax is not null
        defaultFactorShouldBeFound("tax.specified=true");

        // Get all the factorList where tax is null
        defaultFactorShouldNotBeFound("tax.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorsByTaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where tax is greater than or equal to DEFAULT_TAX
        defaultFactorShouldBeFound("tax.greaterThanOrEqual=" + DEFAULT_TAX);

        // Get all the factorList where tax is greater than or equal to UPDATED_TAX
        defaultFactorShouldNotBeFound("tax.greaterThanOrEqual=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllFactorsByTaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where tax is less than or equal to DEFAULT_TAX
        defaultFactorShouldBeFound("tax.lessThanOrEqual=" + DEFAULT_TAX);

        // Get all the factorList where tax is less than or equal to SMALLER_TAX
        defaultFactorShouldNotBeFound("tax.lessThanOrEqual=" + SMALLER_TAX);
    }

    @Test
    @Transactional
    void getAllFactorsByTaxIsLessThanSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where tax is less than DEFAULT_TAX
        defaultFactorShouldNotBeFound("tax.lessThan=" + DEFAULT_TAX);

        // Get all the factorList where tax is less than UPDATED_TAX
        defaultFactorShouldBeFound("tax.lessThan=" + UPDATED_TAX);
    }

    @Test
    @Transactional
    void getAllFactorsByTaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where tax is greater than DEFAULT_TAX
        defaultFactorShouldNotBeFound("tax.greaterThan=" + DEFAULT_TAX);

        // Get all the factorList where tax is greater than SMALLER_TAX
        defaultFactorShouldBeFound("tax.greaterThan=" + SMALLER_TAX);
    }

    @Test
    @Transactional
    void getAllFactorsByNetpriceIsEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where netprice equals to DEFAULT_NETPRICE
        defaultFactorShouldBeFound("netprice.equals=" + DEFAULT_NETPRICE);

        // Get all the factorList where netprice equals to UPDATED_NETPRICE
        defaultFactorShouldNotBeFound("netprice.equals=" + UPDATED_NETPRICE);
    }

    @Test
    @Transactional
    void getAllFactorsByNetpriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where netprice not equals to DEFAULT_NETPRICE
        defaultFactorShouldNotBeFound("netprice.notEquals=" + DEFAULT_NETPRICE);

        // Get all the factorList where netprice not equals to UPDATED_NETPRICE
        defaultFactorShouldBeFound("netprice.notEquals=" + UPDATED_NETPRICE);
    }

    @Test
    @Transactional
    void getAllFactorsByNetpriceIsInShouldWork() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where netprice in DEFAULT_NETPRICE or UPDATED_NETPRICE
        defaultFactorShouldBeFound("netprice.in=" + DEFAULT_NETPRICE + "," + UPDATED_NETPRICE);

        // Get all the factorList where netprice equals to UPDATED_NETPRICE
        defaultFactorShouldNotBeFound("netprice.in=" + UPDATED_NETPRICE);
    }

    @Test
    @Transactional
    void getAllFactorsByNetpriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where netprice is not null
        defaultFactorShouldBeFound("netprice.specified=true");

        // Get all the factorList where netprice is null
        defaultFactorShouldNotBeFound("netprice.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorsByNetpriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where netprice is greater than or equal to DEFAULT_NETPRICE
        defaultFactorShouldBeFound("netprice.greaterThanOrEqual=" + DEFAULT_NETPRICE);

        // Get all the factorList where netprice is greater than or equal to UPDATED_NETPRICE
        defaultFactorShouldNotBeFound("netprice.greaterThanOrEqual=" + UPDATED_NETPRICE);
    }

    @Test
    @Transactional
    void getAllFactorsByNetpriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where netprice is less than or equal to DEFAULT_NETPRICE
        defaultFactorShouldBeFound("netprice.lessThanOrEqual=" + DEFAULT_NETPRICE);

        // Get all the factorList where netprice is less than or equal to SMALLER_NETPRICE
        defaultFactorShouldNotBeFound("netprice.lessThanOrEqual=" + SMALLER_NETPRICE);
    }

    @Test
    @Transactional
    void getAllFactorsByNetpriceIsLessThanSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where netprice is less than DEFAULT_NETPRICE
        defaultFactorShouldNotBeFound("netprice.lessThan=" + DEFAULT_NETPRICE);

        // Get all the factorList where netprice is less than UPDATED_NETPRICE
        defaultFactorShouldBeFound("netprice.lessThan=" + UPDATED_NETPRICE);
    }

    @Test
    @Transactional
    void getAllFactorsByNetpriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where netprice is greater than DEFAULT_NETPRICE
        defaultFactorShouldNotBeFound("netprice.greaterThan=" + DEFAULT_NETPRICE);

        // Get all the factorList where netprice is greater than SMALLER_NETPRICE
        defaultFactorShouldBeFound("netprice.greaterThan=" + SMALLER_NETPRICE);
    }

    @Test
    @Transactional
    void getAllFactorsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where description equals to DEFAULT_DESCRIPTION
        defaultFactorShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the factorList where description equals to UPDATED_DESCRIPTION
        defaultFactorShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFactorsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where description not equals to DEFAULT_DESCRIPTION
        defaultFactorShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the factorList where description not equals to UPDATED_DESCRIPTION
        defaultFactorShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFactorsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFactorShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the factorList where description equals to UPDATED_DESCRIPTION
        defaultFactorShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFactorsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where description is not null
        defaultFactorShouldBeFound("description.specified=true");

        // Get all the factorList where description is null
        defaultFactorShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllFactorsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where description contains DEFAULT_DESCRIPTION
        defaultFactorShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the factorList where description contains UPDATED_DESCRIPTION
        defaultFactorShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFactorsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        // Get all the factorList where description does not contain DEFAULT_DESCRIPTION
        defaultFactorShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the factorList where description does not contain UPDATED_DESCRIPTION
        defaultFactorShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFactorsByFactorItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);
        FactorItemEntity factorItems = FactorItemResourceIT.createEntity(em);
        em.persist(factorItems);
        em.flush();
        factorEntity.addFactorItems(factorItems);
        factorRepository.saveAndFlush(factorEntity);
        Long factorItemsId = factorItems.getId();

        // Get all the factorList where factorItems equals to factorItemsId
        defaultFactorShouldBeFound("factorItemsId.equals=" + factorItemsId);

        // Get all the factorList where factorItems equals to (factorItemsId + 1)
        defaultFactorShouldNotBeFound("factorItemsId.equals=" + (factorItemsId + 1));
    }

    @Test
    @Transactional
    void getAllFactorsByBuyerPartyIsEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);
        PartyEntity buyerParty = PartyResourceIT.createEntity(em);
        em.persist(buyerParty);
        em.flush();
        factorEntity.setBuyerParty(buyerParty);
        factorRepository.saveAndFlush(factorEntity);
        Long buyerPartyId = buyerParty.getId();

        // Get all the factorList where buyerParty equals to buyerPartyId
        defaultFactorShouldBeFound("buyerPartyId.equals=" + buyerPartyId);

        // Get all the factorList where buyerParty equals to (buyerPartyId + 1)
        defaultFactorShouldNotBeFound("buyerPartyId.equals=" + (buyerPartyId + 1));
    }

    @Test
    @Transactional
    void getAllFactorsBySellerPartyIsEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);
        PartyEntity sellerParty = PartyResourceIT.createEntity(em);
        em.persist(sellerParty);
        em.flush();
        factorEntity.setSellerParty(sellerParty);
        factorRepository.saveAndFlush(factorEntity);
        Long sellerPartyId = sellerParty.getId();

        // Get all the factorList where sellerParty equals to sellerPartyId
        defaultFactorShouldBeFound("sellerPartyId.equals=" + sellerPartyId);

        // Get all the factorList where sellerParty equals to (sellerPartyId + 1)
        defaultFactorShouldNotBeFound("sellerPartyId.equals=" + (sellerPartyId + 1));
    }

    @Test
    @Transactional
    void getAllFactorsByDeliveryAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);
        AddressEntity deliveryAddress = AddressResourceIT.createEntity(em);
        em.persist(deliveryAddress);
        em.flush();
        factorEntity.setDeliveryAddress(deliveryAddress);
        factorRepository.saveAndFlush(factorEntity);
        Long deliveryAddressId = deliveryAddress.getId();

        // Get all the factorList where deliveryAddress equals to deliveryAddressId
        defaultFactorShouldBeFound("deliveryAddressId.equals=" + deliveryAddressId);

        // Get all the factorList where deliveryAddress equals to (deliveryAddressId + 1)
        defaultFactorShouldNotBeFound("deliveryAddressId.equals=" + (deliveryAddressId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFactorShouldBeFound(String filter) throws Exception {
        restFactorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factorEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].factorCode").value(hasItem(DEFAULT_FACTOR_CODE)))
            .andExpect(jsonPath("$.[*].lastStatus").value(hasItem(DEFAULT_LAST_STATUS.toString())))
            .andExpect(jsonPath("$.[*].orderWay").value(hasItem(DEFAULT_ORDER_WAY.toString())))
            .andExpect(jsonPath("$.[*].serving").value(hasItem(DEFAULT_SERVING.toString())))
            .andExpect(jsonPath("$.[*].paymentStateClassId").value(hasItem(DEFAULT_PAYMENT_STATE_CLASS_ID.intValue())))
            .andExpect(jsonPath("$.[*].categoryClassId").value(hasItem(DEFAULT_CATEGORY_CLASS_ID.intValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].netprice").value(hasItem(DEFAULT_NETPRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restFactorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFactorShouldNotBeFound(String filter) throws Exception {
        restFactorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFactorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFactor() throws Exception {
        // Get the factor
        restFactorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFactor() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        int databaseSizeBeforeUpdate = factorRepository.findAll().size();

        // Update the factor
        FactorEntity updatedFactorEntity = factorRepository.findById(factorEntity.getId()).get();
        // Disconnect from session so that the updates on updatedFactorEntity are not directly saved in db
        em.detach(updatedFactorEntity);
        updatedFactorEntity
            .title(UPDATED_TITLE)
            .factorCode(UPDATED_FACTOR_CODE)
            .lastStatus(UPDATED_LAST_STATUS)
            .orderWay(UPDATED_ORDER_WAY)
            .serving(UPDATED_SERVING)
            .paymentStateClassId(UPDATED_PAYMENT_STATE_CLASS_ID)
            .categoryClassId(UPDATED_CATEGORY_CLASS_ID)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .discount(UPDATED_DISCOUNT)
            .tax(UPDATED_TAX)
            .netprice(UPDATED_NETPRICE)
            .description(UPDATED_DESCRIPTION);
        FactorDTO factorDTO = factorMapper.toDto(updatedFactorEntity);

        restFactorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Factor in the database
        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate);
        FactorEntity testFactor = factorList.get(factorList.size() - 1);
        assertThat(testFactor.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFactor.getFactorCode()).isEqualTo(UPDATED_FACTOR_CODE);
        assertThat(testFactor.getLastStatus()).isEqualTo(UPDATED_LAST_STATUS);
        assertThat(testFactor.getOrderWay()).isEqualTo(UPDATED_ORDER_WAY);
        assertThat(testFactor.getServing()).isEqualTo(UPDATED_SERVING);
        assertThat(testFactor.getPaymentStateClassId()).isEqualTo(UPDATED_PAYMENT_STATE_CLASS_ID);
        assertThat(testFactor.getCategoryClassId()).isEqualTo(UPDATED_CATEGORY_CLASS_ID);
        assertThat(testFactor.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testFactor.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testFactor.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testFactor.getNetprice()).isEqualTo(UPDATED_NETPRICE);
        assertThat(testFactor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingFactor() throws Exception {
        int databaseSizeBeforeUpdate = factorRepository.findAll().size();
        factorEntity.setId(count.incrementAndGet());

        // Create the Factor
        FactorDTO factorDTO = factorMapper.toDto(factorEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factor in the database
        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFactor() throws Exception {
        int databaseSizeBeforeUpdate = factorRepository.findAll().size();
        factorEntity.setId(count.incrementAndGet());

        // Create the Factor
        FactorDTO factorDTO = factorMapper.toDto(factorEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(factorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factor in the database
        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFactor() throws Exception {
        int databaseSizeBeforeUpdate = factorRepository.findAll().size();
        factorEntity.setId(count.incrementAndGet());

        // Create the Factor
        FactorDTO factorDTO = factorMapper.toDto(factorEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(factorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factor in the database
        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFactorWithPatch() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        int databaseSizeBeforeUpdate = factorRepository.findAll().size();

        // Update the factor using partial update
        FactorEntity partialUpdatedFactorEntity = new FactorEntity();
        partialUpdatedFactorEntity.setId(factorEntity.getId());

        partialUpdatedFactorEntity
            .title(UPDATED_TITLE)
            .serving(UPDATED_SERVING)
            .paymentStateClassId(UPDATED_PAYMENT_STATE_CLASS_ID)
            .categoryClassId(UPDATED_CATEGORY_CLASS_ID)
            .discount(UPDATED_DISCOUNT)
            .tax(UPDATED_TAX)
            .description(UPDATED_DESCRIPTION);

        restFactorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactorEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactorEntity))
            )
            .andExpect(status().isOk());

        // Validate the Factor in the database
        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate);
        FactorEntity testFactor = factorList.get(factorList.size() - 1);
        assertThat(testFactor.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFactor.getFactorCode()).isEqualTo(DEFAULT_FACTOR_CODE);
        assertThat(testFactor.getLastStatus()).isEqualTo(DEFAULT_LAST_STATUS);
        assertThat(testFactor.getOrderWay()).isEqualTo(DEFAULT_ORDER_WAY);
        assertThat(testFactor.getServing()).isEqualTo(UPDATED_SERVING);
        assertThat(testFactor.getPaymentStateClassId()).isEqualTo(UPDATED_PAYMENT_STATE_CLASS_ID);
        assertThat(testFactor.getCategoryClassId()).isEqualTo(UPDATED_CATEGORY_CLASS_ID);
        assertThat(testFactor.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testFactor.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testFactor.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testFactor.getNetprice()).isEqualTo(DEFAULT_NETPRICE);
        assertThat(testFactor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateFactorWithPatch() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        int databaseSizeBeforeUpdate = factorRepository.findAll().size();

        // Update the factor using partial update
        FactorEntity partialUpdatedFactorEntity = new FactorEntity();
        partialUpdatedFactorEntity.setId(factorEntity.getId());

        partialUpdatedFactorEntity
            .title(UPDATED_TITLE)
            .factorCode(UPDATED_FACTOR_CODE)
            .lastStatus(UPDATED_LAST_STATUS)
            .orderWay(UPDATED_ORDER_WAY)
            .serving(UPDATED_SERVING)
            .paymentStateClassId(UPDATED_PAYMENT_STATE_CLASS_ID)
            .categoryClassId(UPDATED_CATEGORY_CLASS_ID)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .discount(UPDATED_DISCOUNT)
            .tax(UPDATED_TAX)
            .netprice(UPDATED_NETPRICE)
            .description(UPDATED_DESCRIPTION);

        restFactorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactorEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactorEntity))
            )
            .andExpect(status().isOk());

        // Validate the Factor in the database
        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate);
        FactorEntity testFactor = factorList.get(factorList.size() - 1);
        assertThat(testFactor.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFactor.getFactorCode()).isEqualTo(UPDATED_FACTOR_CODE);
        assertThat(testFactor.getLastStatus()).isEqualTo(UPDATED_LAST_STATUS);
        assertThat(testFactor.getOrderWay()).isEqualTo(UPDATED_ORDER_WAY);
        assertThat(testFactor.getServing()).isEqualTo(UPDATED_SERVING);
        assertThat(testFactor.getPaymentStateClassId()).isEqualTo(UPDATED_PAYMENT_STATE_CLASS_ID);
        assertThat(testFactor.getCategoryClassId()).isEqualTo(UPDATED_CATEGORY_CLASS_ID);
        assertThat(testFactor.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testFactor.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testFactor.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testFactor.getNetprice()).isEqualTo(UPDATED_NETPRICE);
        assertThat(testFactor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingFactor() throws Exception {
        int databaseSizeBeforeUpdate = factorRepository.findAll().size();
        factorEntity.setId(count.incrementAndGet());

        // Create the Factor
        FactorDTO factorDTO = factorMapper.toDto(factorEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factor in the database
        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFactor() throws Exception {
        int databaseSizeBeforeUpdate = factorRepository.findAll().size();
        factorEntity.setId(count.incrementAndGet());

        // Create the Factor
        FactorDTO factorDTO = factorMapper.toDto(factorEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(factorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factor in the database
        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFactor() throws Exception {
        int databaseSizeBeforeUpdate = factorRepository.findAll().size();
        factorEntity.setId(count.incrementAndGet());

        // Create the Factor
        FactorDTO factorDTO = factorMapper.toDto(factorEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(factorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factor in the database
        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFactor() throws Exception {
        // Initialize the database
        factorRepository.saveAndFlush(factorEntity);

        int databaseSizeBeforeDelete = factorRepository.findAll().size();

        // Delete the factor
        restFactorMockMvc
            .perform(delete(ENTITY_API_URL_ID, factorEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FactorEntity> factorList = factorRepository.findAll();
        assertThat(factorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
