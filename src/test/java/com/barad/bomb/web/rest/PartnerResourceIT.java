package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.PartnerEntity;
import com.barad.bomb.domain.PartyEntity;
import com.barad.bomb.repository.PartnerRepository;
import com.barad.bomb.service.criteria.PartnerCriteria;
import com.barad.bomb.service.dto.PartnerDTO;
import com.barad.bomb.service.mapper.PartnerMapper;
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
 * Integration tests for the {@link PartnerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PartnerResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PARTNER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PARTNER_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TRADE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TRADE_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ECONOMIC_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ECONOMIC_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ACTIVITY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTIVITY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ACTIVITY_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/partners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private PartnerMapper partnerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartnerMockMvc;

    private PartnerEntity partnerEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartnerEntity createEntity(EntityManager em) {
        PartnerEntity partnerEntity = new PartnerEntity()
            .title(DEFAULT_TITLE)
            .partnerCode(DEFAULT_PARTNER_CODE)
            .tradeTitle(DEFAULT_TRADE_TITLE)
            .economicCode(DEFAULT_ECONOMIC_CODE)
            .activityDate(DEFAULT_ACTIVITY_DATE);
        return partnerEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartnerEntity createUpdatedEntity(EntityManager em) {
        PartnerEntity partnerEntity = new PartnerEntity()
            .title(UPDATED_TITLE)
            .partnerCode(UPDATED_PARTNER_CODE)
            .tradeTitle(UPDATED_TRADE_TITLE)
            .economicCode(UPDATED_ECONOMIC_CODE)
            .activityDate(UPDATED_ACTIVITY_DATE);
        return partnerEntity;
    }

    @BeforeEach
    public void initTest() {
        partnerEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createPartner() throws Exception {
        int databaseSizeBeforeCreate = partnerRepository.findAll().size();
        // Create the Partner
        PartnerDTO partnerDTO = partnerMapper.toDto(partnerEntity);
        restPartnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partnerDTO)))
            .andExpect(status().isCreated());

        // Validate the Partner in the database
        List<PartnerEntity> partnerList = partnerRepository.findAll();
        assertThat(partnerList).hasSize(databaseSizeBeforeCreate + 1);
        PartnerEntity testPartner = partnerList.get(partnerList.size() - 1);
        assertThat(testPartner.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPartner.getPartnerCode()).isEqualTo(DEFAULT_PARTNER_CODE);
        assertThat(testPartner.getTradeTitle()).isEqualTo(DEFAULT_TRADE_TITLE);
        assertThat(testPartner.getEconomicCode()).isEqualTo(DEFAULT_ECONOMIC_CODE);
        assertThat(testPartner.getActivityDate()).isEqualTo(DEFAULT_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    void createPartnerWithExistingId() throws Exception {
        // Create the Partner with an existing ID
        partnerEntity.setId(1L);
        PartnerDTO partnerDTO = partnerMapper.toDto(partnerEntity);

        int databaseSizeBeforeCreate = partnerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partnerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Partner in the database
        List<PartnerEntity> partnerList = partnerRepository.findAll();
        assertThat(partnerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = partnerRepository.findAll().size();
        // set the field null
        partnerEntity.setTitle(null);

        // Create the Partner, which fails.
        PartnerDTO partnerDTO = partnerMapper.toDto(partnerEntity);

        restPartnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partnerDTO)))
            .andExpect(status().isBadRequest());

        List<PartnerEntity> partnerList = partnerRepository.findAll();
        assertThat(partnerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPartnerCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = partnerRepository.findAll().size();
        // set the field null
        partnerEntity.setPartnerCode(null);

        // Create the Partner, which fails.
        PartnerDTO partnerDTO = partnerMapper.toDto(partnerEntity);

        restPartnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partnerDTO)))
            .andExpect(status().isBadRequest());

        List<PartnerEntity> partnerList = partnerRepository.findAll();
        assertThat(partnerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTradeTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = partnerRepository.findAll().size();
        // set the field null
        partnerEntity.setTradeTitle(null);

        // Create the Partner, which fails.
        PartnerDTO partnerDTO = partnerMapper.toDto(partnerEntity);

        restPartnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partnerDTO)))
            .andExpect(status().isBadRequest());

        List<PartnerEntity> partnerList = partnerRepository.findAll();
        assertThat(partnerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPartners() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList
        restPartnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partnerEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].partnerCode").value(hasItem(DEFAULT_PARTNER_CODE)))
            .andExpect(jsonPath("$.[*].tradeTitle").value(hasItem(DEFAULT_TRADE_TITLE)))
            .andExpect(jsonPath("$.[*].economicCode").value(hasItem(DEFAULT_ECONOMIC_CODE)))
            .andExpect(jsonPath("$.[*].activityDate").value(hasItem(DEFAULT_ACTIVITY_DATE.toString())));
    }

    @Test
    @Transactional
    void getPartner() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get the partner
        restPartnerMockMvc
            .perform(get(ENTITY_API_URL_ID, partnerEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partnerEntity.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.partnerCode").value(DEFAULT_PARTNER_CODE))
            .andExpect(jsonPath("$.tradeTitle").value(DEFAULT_TRADE_TITLE))
            .andExpect(jsonPath("$.economicCode").value(DEFAULT_ECONOMIC_CODE))
            .andExpect(jsonPath("$.activityDate").value(DEFAULT_ACTIVITY_DATE.toString()));
    }

    @Test
    @Transactional
    void getPartnersByIdFiltering() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        Long id = partnerEntity.getId();

        defaultPartnerShouldBeFound("id.equals=" + id);
        defaultPartnerShouldNotBeFound("id.notEquals=" + id);

        defaultPartnerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPartnerShouldNotBeFound("id.greaterThan=" + id);

        defaultPartnerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPartnerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPartnersByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where title equals to DEFAULT_TITLE
        defaultPartnerShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the partnerList where title equals to UPDATED_TITLE
        defaultPartnerShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPartnersByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where title not equals to DEFAULT_TITLE
        defaultPartnerShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the partnerList where title not equals to UPDATED_TITLE
        defaultPartnerShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPartnersByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultPartnerShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the partnerList where title equals to UPDATED_TITLE
        defaultPartnerShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPartnersByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where title is not null
        defaultPartnerShouldBeFound("title.specified=true");

        // Get all the partnerList where title is null
        defaultPartnerShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllPartnersByTitleContainsSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where title contains DEFAULT_TITLE
        defaultPartnerShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the partnerList where title contains UPDATED_TITLE
        defaultPartnerShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPartnersByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where title does not contain DEFAULT_TITLE
        defaultPartnerShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the partnerList where title does not contain UPDATED_TITLE
        defaultPartnerShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPartnersByPartnerCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where partnerCode equals to DEFAULT_PARTNER_CODE
        defaultPartnerShouldBeFound("partnerCode.equals=" + DEFAULT_PARTNER_CODE);

        // Get all the partnerList where partnerCode equals to UPDATED_PARTNER_CODE
        defaultPartnerShouldNotBeFound("partnerCode.equals=" + UPDATED_PARTNER_CODE);
    }

    @Test
    @Transactional
    void getAllPartnersByPartnerCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where partnerCode not equals to DEFAULT_PARTNER_CODE
        defaultPartnerShouldNotBeFound("partnerCode.notEquals=" + DEFAULT_PARTNER_CODE);

        // Get all the partnerList where partnerCode not equals to UPDATED_PARTNER_CODE
        defaultPartnerShouldBeFound("partnerCode.notEquals=" + UPDATED_PARTNER_CODE);
    }

    @Test
    @Transactional
    void getAllPartnersByPartnerCodeIsInShouldWork() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where partnerCode in DEFAULT_PARTNER_CODE or UPDATED_PARTNER_CODE
        defaultPartnerShouldBeFound("partnerCode.in=" + DEFAULT_PARTNER_CODE + "," + UPDATED_PARTNER_CODE);

        // Get all the partnerList where partnerCode equals to UPDATED_PARTNER_CODE
        defaultPartnerShouldNotBeFound("partnerCode.in=" + UPDATED_PARTNER_CODE);
    }

    @Test
    @Transactional
    void getAllPartnersByPartnerCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where partnerCode is not null
        defaultPartnerShouldBeFound("partnerCode.specified=true");

        // Get all the partnerList where partnerCode is null
        defaultPartnerShouldNotBeFound("partnerCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPartnersByPartnerCodeContainsSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where partnerCode contains DEFAULT_PARTNER_CODE
        defaultPartnerShouldBeFound("partnerCode.contains=" + DEFAULT_PARTNER_CODE);

        // Get all the partnerList where partnerCode contains UPDATED_PARTNER_CODE
        defaultPartnerShouldNotBeFound("partnerCode.contains=" + UPDATED_PARTNER_CODE);
    }

    @Test
    @Transactional
    void getAllPartnersByPartnerCodeNotContainsSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where partnerCode does not contain DEFAULT_PARTNER_CODE
        defaultPartnerShouldNotBeFound("partnerCode.doesNotContain=" + DEFAULT_PARTNER_CODE);

        // Get all the partnerList where partnerCode does not contain UPDATED_PARTNER_CODE
        defaultPartnerShouldBeFound("partnerCode.doesNotContain=" + UPDATED_PARTNER_CODE);
    }

    @Test
    @Transactional
    void getAllPartnersByTradeTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where tradeTitle equals to DEFAULT_TRADE_TITLE
        defaultPartnerShouldBeFound("tradeTitle.equals=" + DEFAULT_TRADE_TITLE);

        // Get all the partnerList where tradeTitle equals to UPDATED_TRADE_TITLE
        defaultPartnerShouldNotBeFound("tradeTitle.equals=" + UPDATED_TRADE_TITLE);
    }

    @Test
    @Transactional
    void getAllPartnersByTradeTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where tradeTitle not equals to DEFAULT_TRADE_TITLE
        defaultPartnerShouldNotBeFound("tradeTitle.notEquals=" + DEFAULT_TRADE_TITLE);

        // Get all the partnerList where tradeTitle not equals to UPDATED_TRADE_TITLE
        defaultPartnerShouldBeFound("tradeTitle.notEquals=" + UPDATED_TRADE_TITLE);
    }

    @Test
    @Transactional
    void getAllPartnersByTradeTitleIsInShouldWork() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where tradeTitle in DEFAULT_TRADE_TITLE or UPDATED_TRADE_TITLE
        defaultPartnerShouldBeFound("tradeTitle.in=" + DEFAULT_TRADE_TITLE + "," + UPDATED_TRADE_TITLE);

        // Get all the partnerList where tradeTitle equals to UPDATED_TRADE_TITLE
        defaultPartnerShouldNotBeFound("tradeTitle.in=" + UPDATED_TRADE_TITLE);
    }

    @Test
    @Transactional
    void getAllPartnersByTradeTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where tradeTitle is not null
        defaultPartnerShouldBeFound("tradeTitle.specified=true");

        // Get all the partnerList where tradeTitle is null
        defaultPartnerShouldNotBeFound("tradeTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllPartnersByTradeTitleContainsSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where tradeTitle contains DEFAULT_TRADE_TITLE
        defaultPartnerShouldBeFound("tradeTitle.contains=" + DEFAULT_TRADE_TITLE);

        // Get all the partnerList where tradeTitle contains UPDATED_TRADE_TITLE
        defaultPartnerShouldNotBeFound("tradeTitle.contains=" + UPDATED_TRADE_TITLE);
    }

    @Test
    @Transactional
    void getAllPartnersByTradeTitleNotContainsSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where tradeTitle does not contain DEFAULT_TRADE_TITLE
        defaultPartnerShouldNotBeFound("tradeTitle.doesNotContain=" + DEFAULT_TRADE_TITLE);

        // Get all the partnerList where tradeTitle does not contain UPDATED_TRADE_TITLE
        defaultPartnerShouldBeFound("tradeTitle.doesNotContain=" + UPDATED_TRADE_TITLE);
    }

    @Test
    @Transactional
    void getAllPartnersByEconomicCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where economicCode equals to DEFAULT_ECONOMIC_CODE
        defaultPartnerShouldBeFound("economicCode.equals=" + DEFAULT_ECONOMIC_CODE);

        // Get all the partnerList where economicCode equals to UPDATED_ECONOMIC_CODE
        defaultPartnerShouldNotBeFound("economicCode.equals=" + UPDATED_ECONOMIC_CODE);
    }

    @Test
    @Transactional
    void getAllPartnersByEconomicCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where economicCode not equals to DEFAULT_ECONOMIC_CODE
        defaultPartnerShouldNotBeFound("economicCode.notEquals=" + DEFAULT_ECONOMIC_CODE);

        // Get all the partnerList where economicCode not equals to UPDATED_ECONOMIC_CODE
        defaultPartnerShouldBeFound("economicCode.notEquals=" + UPDATED_ECONOMIC_CODE);
    }

    @Test
    @Transactional
    void getAllPartnersByEconomicCodeIsInShouldWork() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where economicCode in DEFAULT_ECONOMIC_CODE or UPDATED_ECONOMIC_CODE
        defaultPartnerShouldBeFound("economicCode.in=" + DEFAULT_ECONOMIC_CODE + "," + UPDATED_ECONOMIC_CODE);

        // Get all the partnerList where economicCode equals to UPDATED_ECONOMIC_CODE
        defaultPartnerShouldNotBeFound("economicCode.in=" + UPDATED_ECONOMIC_CODE);
    }

    @Test
    @Transactional
    void getAllPartnersByEconomicCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where economicCode is not null
        defaultPartnerShouldBeFound("economicCode.specified=true");

        // Get all the partnerList where economicCode is null
        defaultPartnerShouldNotBeFound("economicCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPartnersByEconomicCodeContainsSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where economicCode contains DEFAULT_ECONOMIC_CODE
        defaultPartnerShouldBeFound("economicCode.contains=" + DEFAULT_ECONOMIC_CODE);

        // Get all the partnerList where economicCode contains UPDATED_ECONOMIC_CODE
        defaultPartnerShouldNotBeFound("economicCode.contains=" + UPDATED_ECONOMIC_CODE);
    }

    @Test
    @Transactional
    void getAllPartnersByEconomicCodeNotContainsSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where economicCode does not contain DEFAULT_ECONOMIC_CODE
        defaultPartnerShouldNotBeFound("economicCode.doesNotContain=" + DEFAULT_ECONOMIC_CODE);

        // Get all the partnerList where economicCode does not contain UPDATED_ECONOMIC_CODE
        defaultPartnerShouldBeFound("economicCode.doesNotContain=" + UPDATED_ECONOMIC_CODE);
    }

    @Test
    @Transactional
    void getAllPartnersByActivityDateIsEqualToSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where activityDate equals to DEFAULT_ACTIVITY_DATE
        defaultPartnerShouldBeFound("activityDate.equals=" + DEFAULT_ACTIVITY_DATE);

        // Get all the partnerList where activityDate equals to UPDATED_ACTIVITY_DATE
        defaultPartnerShouldNotBeFound("activityDate.equals=" + UPDATED_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    void getAllPartnersByActivityDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where activityDate not equals to DEFAULT_ACTIVITY_DATE
        defaultPartnerShouldNotBeFound("activityDate.notEquals=" + DEFAULT_ACTIVITY_DATE);

        // Get all the partnerList where activityDate not equals to UPDATED_ACTIVITY_DATE
        defaultPartnerShouldBeFound("activityDate.notEquals=" + UPDATED_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    void getAllPartnersByActivityDateIsInShouldWork() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where activityDate in DEFAULT_ACTIVITY_DATE or UPDATED_ACTIVITY_DATE
        defaultPartnerShouldBeFound("activityDate.in=" + DEFAULT_ACTIVITY_DATE + "," + UPDATED_ACTIVITY_DATE);

        // Get all the partnerList where activityDate equals to UPDATED_ACTIVITY_DATE
        defaultPartnerShouldNotBeFound("activityDate.in=" + UPDATED_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    void getAllPartnersByActivityDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where activityDate is not null
        defaultPartnerShouldBeFound("activityDate.specified=true");

        // Get all the partnerList where activityDate is null
        defaultPartnerShouldNotBeFound("activityDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPartnersByActivityDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where activityDate is greater than or equal to DEFAULT_ACTIVITY_DATE
        defaultPartnerShouldBeFound("activityDate.greaterThanOrEqual=" + DEFAULT_ACTIVITY_DATE);

        // Get all the partnerList where activityDate is greater than or equal to UPDATED_ACTIVITY_DATE
        defaultPartnerShouldNotBeFound("activityDate.greaterThanOrEqual=" + UPDATED_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    void getAllPartnersByActivityDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where activityDate is less than or equal to DEFAULT_ACTIVITY_DATE
        defaultPartnerShouldBeFound("activityDate.lessThanOrEqual=" + DEFAULT_ACTIVITY_DATE);

        // Get all the partnerList where activityDate is less than or equal to SMALLER_ACTIVITY_DATE
        defaultPartnerShouldNotBeFound("activityDate.lessThanOrEqual=" + SMALLER_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    void getAllPartnersByActivityDateIsLessThanSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where activityDate is less than DEFAULT_ACTIVITY_DATE
        defaultPartnerShouldNotBeFound("activityDate.lessThan=" + DEFAULT_ACTIVITY_DATE);

        // Get all the partnerList where activityDate is less than UPDATED_ACTIVITY_DATE
        defaultPartnerShouldBeFound("activityDate.lessThan=" + UPDATED_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    void getAllPartnersByActivityDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        // Get all the partnerList where activityDate is greater than DEFAULT_ACTIVITY_DATE
        defaultPartnerShouldNotBeFound("activityDate.greaterThan=" + DEFAULT_ACTIVITY_DATE);

        // Get all the partnerList where activityDate is greater than SMALLER_ACTIVITY_DATE
        defaultPartnerShouldBeFound("activityDate.greaterThan=" + SMALLER_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    void getAllPartnersByPartiesIsEqualToSomething() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);
        PartyEntity parties = PartyResourceIT.createEntity(em);
        em.persist(parties);
        em.flush();
        partnerEntity.addParties(parties);
        partnerRepository.saveAndFlush(partnerEntity);
        Long partiesId = parties.getId();

        // Get all the partnerList where parties equals to partiesId
        defaultPartnerShouldBeFound("partiesId.equals=" + partiesId);

        // Get all the partnerList where parties equals to (partiesId + 1)
        defaultPartnerShouldNotBeFound("partiesId.equals=" + (partiesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartnerShouldBeFound(String filter) throws Exception {
        restPartnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partnerEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].partnerCode").value(hasItem(DEFAULT_PARTNER_CODE)))
            .andExpect(jsonPath("$.[*].tradeTitle").value(hasItem(DEFAULT_TRADE_TITLE)))
            .andExpect(jsonPath("$.[*].economicCode").value(hasItem(DEFAULT_ECONOMIC_CODE)))
            .andExpect(jsonPath("$.[*].activityDate").value(hasItem(DEFAULT_ACTIVITY_DATE.toString())));

        // Check, that the count call also returns 1
        restPartnerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPartnerShouldNotBeFound(String filter) throws Exception {
        restPartnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartnerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPartner() throws Exception {
        // Get the partner
        restPartnerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPartner() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        int databaseSizeBeforeUpdate = partnerRepository.findAll().size();

        // Update the partner
        PartnerEntity updatedPartnerEntity = partnerRepository.findById(partnerEntity.getId()).get();
        // Disconnect from session so that the updates on updatedPartnerEntity are not directly saved in db
        em.detach(updatedPartnerEntity);
        updatedPartnerEntity
            .title(UPDATED_TITLE)
            .partnerCode(UPDATED_PARTNER_CODE)
            .tradeTitle(UPDATED_TRADE_TITLE)
            .economicCode(UPDATED_ECONOMIC_CODE)
            .activityDate(UPDATED_ACTIVITY_DATE);
        PartnerDTO partnerDTO = partnerMapper.toDto(updatedPartnerEntity);

        restPartnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partnerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partnerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Partner in the database
        List<PartnerEntity> partnerList = partnerRepository.findAll();
        assertThat(partnerList).hasSize(databaseSizeBeforeUpdate);
        PartnerEntity testPartner = partnerList.get(partnerList.size() - 1);
        assertThat(testPartner.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPartner.getPartnerCode()).isEqualTo(UPDATED_PARTNER_CODE);
        assertThat(testPartner.getTradeTitle()).isEqualTo(UPDATED_TRADE_TITLE);
        assertThat(testPartner.getEconomicCode()).isEqualTo(UPDATED_ECONOMIC_CODE);
        assertThat(testPartner.getActivityDate()).isEqualTo(UPDATED_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPartner() throws Exception {
        int databaseSizeBeforeUpdate = partnerRepository.findAll().size();
        partnerEntity.setId(count.incrementAndGet());

        // Create the Partner
        PartnerDTO partnerDTO = partnerMapper.toDto(partnerEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partnerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partnerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Partner in the database
        List<PartnerEntity> partnerList = partnerRepository.findAll();
        assertThat(partnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartner() throws Exception {
        int databaseSizeBeforeUpdate = partnerRepository.findAll().size();
        partnerEntity.setId(count.incrementAndGet());

        // Create the Partner
        PartnerDTO partnerDTO = partnerMapper.toDto(partnerEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partnerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Partner in the database
        List<PartnerEntity> partnerList = partnerRepository.findAll();
        assertThat(partnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartner() throws Exception {
        int databaseSizeBeforeUpdate = partnerRepository.findAll().size();
        partnerEntity.setId(count.incrementAndGet());

        // Create the Partner
        PartnerDTO partnerDTO = partnerMapper.toDto(partnerEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partnerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Partner in the database
        List<PartnerEntity> partnerList = partnerRepository.findAll();
        assertThat(partnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartnerWithPatch() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        int databaseSizeBeforeUpdate = partnerRepository.findAll().size();

        // Update the partner using partial update
        PartnerEntity partialUpdatedPartnerEntity = new PartnerEntity();
        partialUpdatedPartnerEntity.setId(partnerEntity.getId());

        partialUpdatedPartnerEntity.partnerCode(UPDATED_PARTNER_CODE).tradeTitle(UPDATED_TRADE_TITLE).activityDate(UPDATED_ACTIVITY_DATE);

        restPartnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartnerEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartnerEntity))
            )
            .andExpect(status().isOk());

        // Validate the Partner in the database
        List<PartnerEntity> partnerList = partnerRepository.findAll();
        assertThat(partnerList).hasSize(databaseSizeBeforeUpdate);
        PartnerEntity testPartner = partnerList.get(partnerList.size() - 1);
        assertThat(testPartner.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPartner.getPartnerCode()).isEqualTo(UPDATED_PARTNER_CODE);
        assertThat(testPartner.getTradeTitle()).isEqualTo(UPDATED_TRADE_TITLE);
        assertThat(testPartner.getEconomicCode()).isEqualTo(DEFAULT_ECONOMIC_CODE);
        assertThat(testPartner.getActivityDate()).isEqualTo(UPDATED_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePartnerWithPatch() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        int databaseSizeBeforeUpdate = partnerRepository.findAll().size();

        // Update the partner using partial update
        PartnerEntity partialUpdatedPartnerEntity = new PartnerEntity();
        partialUpdatedPartnerEntity.setId(partnerEntity.getId());

        partialUpdatedPartnerEntity
            .title(UPDATED_TITLE)
            .partnerCode(UPDATED_PARTNER_CODE)
            .tradeTitle(UPDATED_TRADE_TITLE)
            .economicCode(UPDATED_ECONOMIC_CODE)
            .activityDate(UPDATED_ACTIVITY_DATE);

        restPartnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartnerEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartnerEntity))
            )
            .andExpect(status().isOk());

        // Validate the Partner in the database
        List<PartnerEntity> partnerList = partnerRepository.findAll();
        assertThat(partnerList).hasSize(databaseSizeBeforeUpdate);
        PartnerEntity testPartner = partnerList.get(partnerList.size() - 1);
        assertThat(testPartner.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPartner.getPartnerCode()).isEqualTo(UPDATED_PARTNER_CODE);
        assertThat(testPartner.getTradeTitle()).isEqualTo(UPDATED_TRADE_TITLE);
        assertThat(testPartner.getEconomicCode()).isEqualTo(UPDATED_ECONOMIC_CODE);
        assertThat(testPartner.getActivityDate()).isEqualTo(UPDATED_ACTIVITY_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPartner() throws Exception {
        int databaseSizeBeforeUpdate = partnerRepository.findAll().size();
        partnerEntity.setId(count.incrementAndGet());

        // Create the Partner
        PartnerDTO partnerDTO = partnerMapper.toDto(partnerEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partnerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partnerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Partner in the database
        List<PartnerEntity> partnerList = partnerRepository.findAll();
        assertThat(partnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartner() throws Exception {
        int databaseSizeBeforeUpdate = partnerRepository.findAll().size();
        partnerEntity.setId(count.incrementAndGet());

        // Create the Partner
        PartnerDTO partnerDTO = partnerMapper.toDto(partnerEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partnerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Partner in the database
        List<PartnerEntity> partnerList = partnerRepository.findAll();
        assertThat(partnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartner() throws Exception {
        int databaseSizeBeforeUpdate = partnerRepository.findAll().size();
        partnerEntity.setId(count.incrementAndGet());

        // Create the Partner
        PartnerDTO partnerDTO = partnerMapper.toDto(partnerEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartnerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(partnerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Partner in the database
        List<PartnerEntity> partnerList = partnerRepository.findAll();
        assertThat(partnerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartner() throws Exception {
        // Initialize the database
        partnerRepository.saveAndFlush(partnerEntity);

        int databaseSizeBeforeDelete = partnerRepository.findAll().size();

        // Delete the partner
        restPartnerMockMvc
            .perform(delete(ENTITY_API_URL_ID, partnerEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PartnerEntity> partnerList = partnerRepository.findAll();
        assertThat(partnerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
