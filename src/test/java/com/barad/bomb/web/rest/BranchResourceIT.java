package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.BranchEntity;
import com.barad.bomb.domain.PartyEntity;
import com.barad.bomb.repository.BranchRepository;
import com.barad.bomb.service.criteria.BranchCriteria;
import com.barad.bomb.service.dto.BranchDTO;
import com.barad.bomb.service.mapper.BranchMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link BranchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BranchResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TRADE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TRADE_TITLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ACTIVATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTIVATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ACTIVATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EXPIRATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ACTIVATION_STATUS = false;
    private static final Boolean UPDATED_ACTIVATION_STATUS = true;

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;
    private static final Double SMALLER_LAT = 1D - 1D;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/branches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBranchMockMvc;

    private BranchEntity branchEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BranchEntity createEntity(EntityManager em) {
        BranchEntity branchEntity = new BranchEntity()
            .title(DEFAULT_TITLE)
            .branchCode(DEFAULT_BRANCH_CODE)
            .tradeTitle(DEFAULT_TRADE_TITLE)
            .activationDate(DEFAULT_ACTIVATION_DATE)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .activationStatus(DEFAULT_ACTIVATION_STATUS)
            .lat(DEFAULT_LAT)
            .address(DEFAULT_ADDRESS)
            .postalCode(DEFAULT_POSTAL_CODE)
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
        branchEntity.setParty(party);
        return branchEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BranchEntity createUpdatedEntity(EntityManager em) {
        BranchEntity branchEntity = new BranchEntity()
            .title(UPDATED_TITLE)
            .branchCode(UPDATED_BRANCH_CODE)
            .tradeTitle(UPDATED_TRADE_TITLE)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .activationStatus(UPDATED_ACTIVATION_STATUS)
            .lat(UPDATED_LAT)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
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
        branchEntity.setParty(party);
        return branchEntity;
    }

    @BeforeEach
    public void initTest() {
        branchEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createBranch() throws Exception {
        int databaseSizeBeforeCreate = branchRepository.findAll().size();
        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branchEntity);
        restBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isCreated());

        // Validate the Branch in the database
        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeCreate + 1);
        BranchEntity testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBranch.getBranchCode()).isEqualTo(DEFAULT_BRANCH_CODE);
        assertThat(testBranch.getTradeTitle()).isEqualTo(DEFAULT_TRADE_TITLE);
        assertThat(testBranch.getActivationDate()).isEqualTo(DEFAULT_ACTIVATION_DATE);
        assertThat(testBranch.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testBranch.getActivationStatus()).isEqualTo(DEFAULT_ACTIVATION_STATUS);
        assertThat(testBranch.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testBranch.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testBranch.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testBranch.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createBranchWithExistingId() throws Exception {
        // Create the Branch with an existing ID
        branchEntity.setId(1L);
        BranchDTO branchDTO = branchMapper.toDto(branchEntity);

        int databaseSizeBeforeCreate = branchRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = branchRepository.findAll().size();
        // set the field null
        branchEntity.setTitle(null);

        // Create the Branch, which fails.
        BranchDTO branchDTO = branchMapper.toDto(branchEntity);

        restBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isBadRequest());

        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBranchCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = branchRepository.findAll().size();
        // set the field null
        branchEntity.setBranchCode(null);

        // Create the Branch, which fails.
        BranchDTO branchDTO = branchMapper.toDto(branchEntity);

        restBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isBadRequest());

        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTradeTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = branchRepository.findAll().size();
        // set the field null
        branchEntity.setTradeTitle(null);

        // Create the Branch, which fails.
        BranchDTO branchDTO = branchMapper.toDto(branchEntity);

        restBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isBadRequest());

        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = branchRepository.findAll().size();
        // set the field null
        branchEntity.setActivationDate(null);

        // Create the Branch, which fails.
        BranchDTO branchDTO = branchMapper.toDto(branchEntity);

        restBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isBadRequest());

        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivationStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = branchRepository.findAll().size();
        // set the field null
        branchEntity.setActivationStatus(null);

        // Create the Branch, which fails.
        BranchDTO branchDTO = branchMapper.toDto(branchEntity);

        restBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isBadRequest());

        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLatIsRequired() throws Exception {
        int databaseSizeBeforeTest = branchRepository.findAll().size();
        // set the field null
        branchEntity.setLat(null);

        // Create the Branch, which fails.
        BranchDTO branchDTO = branchMapper.toDto(branchEntity);

        restBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isBadRequest());

        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = branchRepository.findAll().size();
        // set the field null
        branchEntity.setAddress(null);

        // Create the Branch, which fails.
        BranchDTO branchDTO = branchMapper.toDto(branchEntity);

        restBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isBadRequest());

        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPostalCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = branchRepository.findAll().size();
        // set the field null
        branchEntity.setPostalCode(null);

        // Create the Branch, which fails.
        BranchDTO branchDTO = branchMapper.toDto(branchEntity);

        restBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isBadRequest());

        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBranches() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branchEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].tradeTitle").value(hasItem(DEFAULT_TRADE_TITLE)))
            .andExpect(jsonPath("$.[*].activationDate").value(hasItem(DEFAULT_ACTIVATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].activationStatus").value(hasItem(DEFAULT_ACTIVATION_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get the branch
        restBranchMockMvc
            .perform(get(ENTITY_API_URL_ID, branchEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(branchEntity.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.tradeTitle").value(DEFAULT_TRADE_TITLE))
            .andExpect(jsonPath("$.activationDate").value(DEFAULT_ACTIVATION_DATE.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.activationStatus").value(DEFAULT_ACTIVATION_STATUS.booleanValue()))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getBranchesByIdFiltering() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        Long id = branchEntity.getId();

        defaultBranchShouldBeFound("id.equals=" + id);
        defaultBranchShouldNotBeFound("id.notEquals=" + id);

        defaultBranchShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBranchShouldNotBeFound("id.greaterThan=" + id);

        defaultBranchShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBranchShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBranchesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where title equals to DEFAULT_TITLE
        defaultBranchShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the branchList where title equals to UPDATED_TITLE
        defaultBranchShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBranchesByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where title not equals to DEFAULT_TITLE
        defaultBranchShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the branchList where title not equals to UPDATED_TITLE
        defaultBranchShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBranchesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultBranchShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the branchList where title equals to UPDATED_TITLE
        defaultBranchShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBranchesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where title is not null
        defaultBranchShouldBeFound("title.specified=true");

        // Get all the branchList where title is null
        defaultBranchShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByTitleContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where title contains DEFAULT_TITLE
        defaultBranchShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the branchList where title contains UPDATED_TITLE
        defaultBranchShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBranchesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where title does not contain DEFAULT_TITLE
        defaultBranchShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the branchList where title does not contain UPDATED_TITLE
        defaultBranchShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllBranchesByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where branchCode equals to DEFAULT_BRANCH_CODE
        defaultBranchShouldBeFound("branchCode.equals=" + DEFAULT_BRANCH_CODE);

        // Get all the branchList where branchCode equals to UPDATED_BRANCH_CODE
        defaultBranchShouldNotBeFound("branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBranchesByBranchCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where branchCode not equals to DEFAULT_BRANCH_CODE
        defaultBranchShouldNotBeFound("branchCode.notEquals=" + DEFAULT_BRANCH_CODE);

        // Get all the branchList where branchCode not equals to UPDATED_BRANCH_CODE
        defaultBranchShouldBeFound("branchCode.notEquals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBranchesByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where branchCode in DEFAULT_BRANCH_CODE or UPDATED_BRANCH_CODE
        defaultBranchShouldBeFound("branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE);

        // Get all the branchList where branchCode equals to UPDATED_BRANCH_CODE
        defaultBranchShouldNotBeFound("branchCode.in=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBranchesByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where branchCode is not null
        defaultBranchShouldBeFound("branchCode.specified=true");

        // Get all the branchList where branchCode is null
        defaultBranchShouldNotBeFound("branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where branchCode contains DEFAULT_BRANCH_CODE
        defaultBranchShouldBeFound("branchCode.contains=" + DEFAULT_BRANCH_CODE);

        // Get all the branchList where branchCode contains UPDATED_BRANCH_CODE
        defaultBranchShouldNotBeFound("branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBranchesByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where branchCode does not contain DEFAULT_BRANCH_CODE
        defaultBranchShouldNotBeFound("branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE);

        // Get all the branchList where branchCode does not contain UPDATED_BRANCH_CODE
        defaultBranchShouldBeFound("branchCode.doesNotContain=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBranchesByTradeTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where tradeTitle equals to DEFAULT_TRADE_TITLE
        defaultBranchShouldBeFound("tradeTitle.equals=" + DEFAULT_TRADE_TITLE);

        // Get all the branchList where tradeTitle equals to UPDATED_TRADE_TITLE
        defaultBranchShouldNotBeFound("tradeTitle.equals=" + UPDATED_TRADE_TITLE);
    }

    @Test
    @Transactional
    void getAllBranchesByTradeTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where tradeTitle not equals to DEFAULT_TRADE_TITLE
        defaultBranchShouldNotBeFound("tradeTitle.notEquals=" + DEFAULT_TRADE_TITLE);

        // Get all the branchList where tradeTitle not equals to UPDATED_TRADE_TITLE
        defaultBranchShouldBeFound("tradeTitle.notEquals=" + UPDATED_TRADE_TITLE);
    }

    @Test
    @Transactional
    void getAllBranchesByTradeTitleIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where tradeTitle in DEFAULT_TRADE_TITLE or UPDATED_TRADE_TITLE
        defaultBranchShouldBeFound("tradeTitle.in=" + DEFAULT_TRADE_TITLE + "," + UPDATED_TRADE_TITLE);

        // Get all the branchList where tradeTitle equals to UPDATED_TRADE_TITLE
        defaultBranchShouldNotBeFound("tradeTitle.in=" + UPDATED_TRADE_TITLE);
    }

    @Test
    @Transactional
    void getAllBranchesByTradeTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where tradeTitle is not null
        defaultBranchShouldBeFound("tradeTitle.specified=true");

        // Get all the branchList where tradeTitle is null
        defaultBranchShouldNotBeFound("tradeTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByTradeTitleContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where tradeTitle contains DEFAULT_TRADE_TITLE
        defaultBranchShouldBeFound("tradeTitle.contains=" + DEFAULT_TRADE_TITLE);

        // Get all the branchList where tradeTitle contains UPDATED_TRADE_TITLE
        defaultBranchShouldNotBeFound("tradeTitle.contains=" + UPDATED_TRADE_TITLE);
    }

    @Test
    @Transactional
    void getAllBranchesByTradeTitleNotContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where tradeTitle does not contain DEFAULT_TRADE_TITLE
        defaultBranchShouldNotBeFound("tradeTitle.doesNotContain=" + DEFAULT_TRADE_TITLE);

        // Get all the branchList where tradeTitle does not contain UPDATED_TRADE_TITLE
        defaultBranchShouldBeFound("tradeTitle.doesNotContain=" + UPDATED_TRADE_TITLE);
    }

    @Test
    @Transactional
    void getAllBranchesByActivationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where activationDate equals to DEFAULT_ACTIVATION_DATE
        defaultBranchShouldBeFound("activationDate.equals=" + DEFAULT_ACTIVATION_DATE);

        // Get all the branchList where activationDate equals to UPDATED_ACTIVATION_DATE
        defaultBranchShouldNotBeFound("activationDate.equals=" + UPDATED_ACTIVATION_DATE);
    }

    @Test
    @Transactional
    void getAllBranchesByActivationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where activationDate not equals to DEFAULT_ACTIVATION_DATE
        defaultBranchShouldNotBeFound("activationDate.notEquals=" + DEFAULT_ACTIVATION_DATE);

        // Get all the branchList where activationDate not equals to UPDATED_ACTIVATION_DATE
        defaultBranchShouldBeFound("activationDate.notEquals=" + UPDATED_ACTIVATION_DATE);
    }

    @Test
    @Transactional
    void getAllBranchesByActivationDateIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where activationDate in DEFAULT_ACTIVATION_DATE or UPDATED_ACTIVATION_DATE
        defaultBranchShouldBeFound("activationDate.in=" + DEFAULT_ACTIVATION_DATE + "," + UPDATED_ACTIVATION_DATE);

        // Get all the branchList where activationDate equals to UPDATED_ACTIVATION_DATE
        defaultBranchShouldNotBeFound("activationDate.in=" + UPDATED_ACTIVATION_DATE);
    }

    @Test
    @Transactional
    void getAllBranchesByActivationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where activationDate is not null
        defaultBranchShouldBeFound("activationDate.specified=true");

        // Get all the branchList where activationDate is null
        defaultBranchShouldNotBeFound("activationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByActivationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where activationDate is greater than or equal to DEFAULT_ACTIVATION_DATE
        defaultBranchShouldBeFound("activationDate.greaterThanOrEqual=" + DEFAULT_ACTIVATION_DATE);

        // Get all the branchList where activationDate is greater than or equal to UPDATED_ACTIVATION_DATE
        defaultBranchShouldNotBeFound("activationDate.greaterThanOrEqual=" + UPDATED_ACTIVATION_DATE);
    }

    @Test
    @Transactional
    void getAllBranchesByActivationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where activationDate is less than or equal to DEFAULT_ACTIVATION_DATE
        defaultBranchShouldBeFound("activationDate.lessThanOrEqual=" + DEFAULT_ACTIVATION_DATE);

        // Get all the branchList where activationDate is less than or equal to SMALLER_ACTIVATION_DATE
        defaultBranchShouldNotBeFound("activationDate.lessThanOrEqual=" + SMALLER_ACTIVATION_DATE);
    }

    @Test
    @Transactional
    void getAllBranchesByActivationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where activationDate is less than DEFAULT_ACTIVATION_DATE
        defaultBranchShouldNotBeFound("activationDate.lessThan=" + DEFAULT_ACTIVATION_DATE);

        // Get all the branchList where activationDate is less than UPDATED_ACTIVATION_DATE
        defaultBranchShouldBeFound("activationDate.lessThan=" + UPDATED_ACTIVATION_DATE);
    }

    @Test
    @Transactional
    void getAllBranchesByActivationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where activationDate is greater than DEFAULT_ACTIVATION_DATE
        defaultBranchShouldNotBeFound("activationDate.greaterThan=" + DEFAULT_ACTIVATION_DATE);

        // Get all the branchList where activationDate is greater than SMALLER_ACTIVATION_DATE
        defaultBranchShouldBeFound("activationDate.greaterThan=" + SMALLER_ACTIVATION_DATE);
    }

    @Test
    @Transactional
    void getAllBranchesByExpirationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where expirationDate equals to DEFAULT_EXPIRATION_DATE
        defaultBranchShouldBeFound("expirationDate.equals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the branchList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultBranchShouldNotBeFound("expirationDate.equals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllBranchesByExpirationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where expirationDate not equals to DEFAULT_EXPIRATION_DATE
        defaultBranchShouldNotBeFound("expirationDate.notEquals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the branchList where expirationDate not equals to UPDATED_EXPIRATION_DATE
        defaultBranchShouldBeFound("expirationDate.notEquals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllBranchesByExpirationDateIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where expirationDate in DEFAULT_EXPIRATION_DATE or UPDATED_EXPIRATION_DATE
        defaultBranchShouldBeFound("expirationDate.in=" + DEFAULT_EXPIRATION_DATE + "," + UPDATED_EXPIRATION_DATE);

        // Get all the branchList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultBranchShouldNotBeFound("expirationDate.in=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllBranchesByExpirationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where expirationDate is not null
        defaultBranchShouldBeFound("expirationDate.specified=true");

        // Get all the branchList where expirationDate is null
        defaultBranchShouldNotBeFound("expirationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByExpirationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where expirationDate is greater than or equal to DEFAULT_EXPIRATION_DATE
        defaultBranchShouldBeFound("expirationDate.greaterThanOrEqual=" + DEFAULT_EXPIRATION_DATE);

        // Get all the branchList where expirationDate is greater than or equal to UPDATED_EXPIRATION_DATE
        defaultBranchShouldNotBeFound("expirationDate.greaterThanOrEqual=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllBranchesByExpirationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where expirationDate is less than or equal to DEFAULT_EXPIRATION_DATE
        defaultBranchShouldBeFound("expirationDate.lessThanOrEqual=" + DEFAULT_EXPIRATION_DATE);

        // Get all the branchList where expirationDate is less than or equal to SMALLER_EXPIRATION_DATE
        defaultBranchShouldNotBeFound("expirationDate.lessThanOrEqual=" + SMALLER_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllBranchesByExpirationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where expirationDate is less than DEFAULT_EXPIRATION_DATE
        defaultBranchShouldNotBeFound("expirationDate.lessThan=" + DEFAULT_EXPIRATION_DATE);

        // Get all the branchList where expirationDate is less than UPDATED_EXPIRATION_DATE
        defaultBranchShouldBeFound("expirationDate.lessThan=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllBranchesByExpirationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where expirationDate is greater than DEFAULT_EXPIRATION_DATE
        defaultBranchShouldNotBeFound("expirationDate.greaterThan=" + DEFAULT_EXPIRATION_DATE);

        // Get all the branchList where expirationDate is greater than SMALLER_EXPIRATION_DATE
        defaultBranchShouldBeFound("expirationDate.greaterThan=" + SMALLER_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllBranchesByActivationStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where activationStatus equals to DEFAULT_ACTIVATION_STATUS
        defaultBranchShouldBeFound("activationStatus.equals=" + DEFAULT_ACTIVATION_STATUS);

        // Get all the branchList where activationStatus equals to UPDATED_ACTIVATION_STATUS
        defaultBranchShouldNotBeFound("activationStatus.equals=" + UPDATED_ACTIVATION_STATUS);
    }

    @Test
    @Transactional
    void getAllBranchesByActivationStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where activationStatus not equals to DEFAULT_ACTIVATION_STATUS
        defaultBranchShouldNotBeFound("activationStatus.notEquals=" + DEFAULT_ACTIVATION_STATUS);

        // Get all the branchList where activationStatus not equals to UPDATED_ACTIVATION_STATUS
        defaultBranchShouldBeFound("activationStatus.notEquals=" + UPDATED_ACTIVATION_STATUS);
    }

    @Test
    @Transactional
    void getAllBranchesByActivationStatusIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where activationStatus in DEFAULT_ACTIVATION_STATUS or UPDATED_ACTIVATION_STATUS
        defaultBranchShouldBeFound("activationStatus.in=" + DEFAULT_ACTIVATION_STATUS + "," + UPDATED_ACTIVATION_STATUS);

        // Get all the branchList where activationStatus equals to UPDATED_ACTIVATION_STATUS
        defaultBranchShouldNotBeFound("activationStatus.in=" + UPDATED_ACTIVATION_STATUS);
    }

    @Test
    @Transactional
    void getAllBranchesByActivationStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where activationStatus is not null
        defaultBranchShouldBeFound("activationStatus.specified=true");

        // Get all the branchList where activationStatus is null
        defaultBranchShouldNotBeFound("activationStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByLatIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where lat equals to DEFAULT_LAT
        defaultBranchShouldBeFound("lat.equals=" + DEFAULT_LAT);

        // Get all the branchList where lat equals to UPDATED_LAT
        defaultBranchShouldNotBeFound("lat.equals=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllBranchesByLatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where lat not equals to DEFAULT_LAT
        defaultBranchShouldNotBeFound("lat.notEquals=" + DEFAULT_LAT);

        // Get all the branchList where lat not equals to UPDATED_LAT
        defaultBranchShouldBeFound("lat.notEquals=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllBranchesByLatIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where lat in DEFAULT_LAT or UPDATED_LAT
        defaultBranchShouldBeFound("lat.in=" + DEFAULT_LAT + "," + UPDATED_LAT);

        // Get all the branchList where lat equals to UPDATED_LAT
        defaultBranchShouldNotBeFound("lat.in=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllBranchesByLatIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where lat is not null
        defaultBranchShouldBeFound("lat.specified=true");

        // Get all the branchList where lat is null
        defaultBranchShouldNotBeFound("lat.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByLatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where lat is greater than or equal to DEFAULT_LAT
        defaultBranchShouldBeFound("lat.greaterThanOrEqual=" + DEFAULT_LAT);

        // Get all the branchList where lat is greater than or equal to UPDATED_LAT
        defaultBranchShouldNotBeFound("lat.greaterThanOrEqual=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllBranchesByLatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where lat is less than or equal to DEFAULT_LAT
        defaultBranchShouldBeFound("lat.lessThanOrEqual=" + DEFAULT_LAT);

        // Get all the branchList where lat is less than or equal to SMALLER_LAT
        defaultBranchShouldNotBeFound("lat.lessThanOrEqual=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    void getAllBranchesByLatIsLessThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where lat is less than DEFAULT_LAT
        defaultBranchShouldNotBeFound("lat.lessThan=" + DEFAULT_LAT);

        // Get all the branchList where lat is less than UPDATED_LAT
        defaultBranchShouldBeFound("lat.lessThan=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllBranchesByLatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where lat is greater than DEFAULT_LAT
        defaultBranchShouldNotBeFound("lat.greaterThan=" + DEFAULT_LAT);

        // Get all the branchList where lat is greater than SMALLER_LAT
        defaultBranchShouldBeFound("lat.greaterThan=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    void getAllBranchesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where address equals to DEFAULT_ADDRESS
        defaultBranchShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the branchList where address equals to UPDATED_ADDRESS
        defaultBranchShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllBranchesByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where address not equals to DEFAULT_ADDRESS
        defaultBranchShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the branchList where address not equals to UPDATED_ADDRESS
        defaultBranchShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllBranchesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultBranchShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the branchList where address equals to UPDATED_ADDRESS
        defaultBranchShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllBranchesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where address is not null
        defaultBranchShouldBeFound("address.specified=true");

        // Get all the branchList where address is null
        defaultBranchShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByAddressContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where address contains DEFAULT_ADDRESS
        defaultBranchShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the branchList where address contains UPDATED_ADDRESS
        defaultBranchShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllBranchesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where address does not contain DEFAULT_ADDRESS
        defaultBranchShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the branchList where address does not contain UPDATED_ADDRESS
        defaultBranchShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllBranchesByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultBranchShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the branchList where postalCode equals to UPDATED_POSTAL_CODE
        defaultBranchShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllBranchesByPostalCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where postalCode not equals to DEFAULT_POSTAL_CODE
        defaultBranchShouldNotBeFound("postalCode.notEquals=" + DEFAULT_POSTAL_CODE);

        // Get all the branchList where postalCode not equals to UPDATED_POSTAL_CODE
        defaultBranchShouldBeFound("postalCode.notEquals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllBranchesByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultBranchShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the branchList where postalCode equals to UPDATED_POSTAL_CODE
        defaultBranchShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllBranchesByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where postalCode is not null
        defaultBranchShouldBeFound("postalCode.specified=true");

        // Get all the branchList where postalCode is null
        defaultBranchShouldNotBeFound("postalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where postalCode contains DEFAULT_POSTAL_CODE
        defaultBranchShouldBeFound("postalCode.contains=" + DEFAULT_POSTAL_CODE);

        // Get all the branchList where postalCode contains UPDATED_POSTAL_CODE
        defaultBranchShouldNotBeFound("postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllBranchesByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where postalCode does not contain DEFAULT_POSTAL_CODE
        defaultBranchShouldNotBeFound("postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE);

        // Get all the branchList where postalCode does not contain UPDATED_POSTAL_CODE
        defaultBranchShouldBeFound("postalCode.doesNotContain=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllBranchesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where description equals to DEFAULT_DESCRIPTION
        defaultBranchShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the branchList where description equals to UPDATED_DESCRIPTION
        defaultBranchShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBranchesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where description not equals to DEFAULT_DESCRIPTION
        defaultBranchShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the branchList where description not equals to UPDATED_DESCRIPTION
        defaultBranchShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBranchesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultBranchShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the branchList where description equals to UPDATED_DESCRIPTION
        defaultBranchShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBranchesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where description is not null
        defaultBranchShouldBeFound("description.specified=true");

        // Get all the branchList where description is null
        defaultBranchShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where description contains DEFAULT_DESCRIPTION
        defaultBranchShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the branchList where description contains UPDATED_DESCRIPTION
        defaultBranchShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBranchesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        // Get all the branchList where description does not contain DEFAULT_DESCRIPTION
        defaultBranchShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the branchList where description does not contain UPDATED_DESCRIPTION
        defaultBranchShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBranchesByPartyIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);
        PartyEntity party = PartyResourceIT.createEntity(em);
        em.persist(party);
        em.flush();
        branchEntity.setParty(party);
        branchRepository.saveAndFlush(branchEntity);
        Long partyId = party.getId();

        // Get all the branchList where party equals to partyId
        defaultBranchShouldBeFound("partyId.equals=" + partyId);

        // Get all the branchList where party equals to (partyId + 1)
        defaultBranchShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBranchShouldBeFound(String filter) throws Exception {
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branchEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].tradeTitle").value(hasItem(DEFAULT_TRADE_TITLE)))
            .andExpect(jsonPath("$.[*].activationDate").value(hasItem(DEFAULT_ACTIVATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].activationStatus").value(hasItem(DEFAULT_ACTIVATION_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBranchShouldNotBeFound(String filter) throws Exception {
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBranch() throws Exception {
        // Get the branch
        restBranchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Update the branch
        BranchEntity updatedBranchEntity = branchRepository.findById(branchEntity.getId()).get();
        // Disconnect from session so that the updates on updatedBranchEntity are not directly saved in db
        em.detach(updatedBranchEntity);
        updatedBranchEntity
            .title(UPDATED_TITLE)
            .branchCode(UPDATED_BRANCH_CODE)
            .tradeTitle(UPDATED_TRADE_TITLE)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .activationStatus(UPDATED_ACTIVATION_STATUS)
            .lat(UPDATED_LAT)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .description(UPDATED_DESCRIPTION);
        BranchDTO branchDTO = branchMapper.toDto(updatedBranchEntity);

        restBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, branchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isOk());

        // Validate the Branch in the database
        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
        BranchEntity testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBranch.getBranchCode()).isEqualTo(UPDATED_BRANCH_CODE);
        assertThat(testBranch.getTradeTitle()).isEqualTo(UPDATED_TRADE_TITLE);
        assertThat(testBranch.getActivationDate()).isEqualTo(UPDATED_ACTIVATION_DATE);
        assertThat(testBranch.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testBranch.getActivationStatus()).isEqualTo(UPDATED_ACTIVATION_STATUS);
        assertThat(testBranch.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testBranch.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testBranch.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testBranch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branchEntity.setId(count.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branchEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, branchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branchEntity.setId(count.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branchEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branchEntity.setId(count.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branchEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Branch in the database
        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBranchWithPatch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Update the branch using partial update
        BranchEntity partialUpdatedBranchEntity = new BranchEntity();
        partialUpdatedBranchEntity.setId(branchEntity.getId());

        partialUpdatedBranchEntity
            .title(UPDATED_TITLE)
            .tradeTitle(UPDATED_TRADE_TITLE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .lat(UPDATED_LAT)
            .postalCode(UPDATED_POSTAL_CODE);

        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranchEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBranchEntity))
            )
            .andExpect(status().isOk());

        // Validate the Branch in the database
        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
        BranchEntity testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBranch.getBranchCode()).isEqualTo(DEFAULT_BRANCH_CODE);
        assertThat(testBranch.getTradeTitle()).isEqualTo(UPDATED_TRADE_TITLE);
        assertThat(testBranch.getActivationDate()).isEqualTo(DEFAULT_ACTIVATION_DATE);
        assertThat(testBranch.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testBranch.getActivationStatus()).isEqualTo(DEFAULT_ACTIVATION_STATUS);
        assertThat(testBranch.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testBranch.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testBranch.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testBranch.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateBranchWithPatch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Update the branch using partial update
        BranchEntity partialUpdatedBranchEntity = new BranchEntity();
        partialUpdatedBranchEntity.setId(branchEntity.getId());

        partialUpdatedBranchEntity
            .title(UPDATED_TITLE)
            .branchCode(UPDATED_BRANCH_CODE)
            .tradeTitle(UPDATED_TRADE_TITLE)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .activationStatus(UPDATED_ACTIVATION_STATUS)
            .lat(UPDATED_LAT)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .description(UPDATED_DESCRIPTION);

        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranchEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBranchEntity))
            )
            .andExpect(status().isOk());

        // Validate the Branch in the database
        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
        BranchEntity testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBranch.getBranchCode()).isEqualTo(UPDATED_BRANCH_CODE);
        assertThat(testBranch.getTradeTitle()).isEqualTo(UPDATED_TRADE_TITLE);
        assertThat(testBranch.getActivationDate()).isEqualTo(UPDATED_ACTIVATION_DATE);
        assertThat(testBranch.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testBranch.getActivationStatus()).isEqualTo(UPDATED_ACTIVATION_STATUS);
        assertThat(testBranch.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testBranch.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testBranch.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testBranch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branchEntity.setId(count.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branchEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, branchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branchEntity.setId(count.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branchEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branchEntity.setId(count.incrementAndGet());

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branchEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(branchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Branch in the database
        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branchEntity);

        int databaseSizeBeforeDelete = branchRepository.findAll().size();

        // Delete the branch
        restBranchMockMvc
            .perform(delete(ENTITY_API_URL_ID, branchEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BranchEntity> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
