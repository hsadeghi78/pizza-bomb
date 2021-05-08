package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.BranchEntity;
import com.barad.bomb.domain.CriticismEntity;
import com.barad.bomb.domain.PartyEntity;
import com.barad.bomb.repository.PartyRepository;
import com.barad.bomb.service.criteria.PartyCriteria;
import com.barad.bomb.service.dto.PartyDTO;
import com.barad.bomb.service.mapper.PartyMapper;
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
 * Integration tests for the {@link PartyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PartyResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PARTY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PARTY_CODE = "BBBBBBBBBB";

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

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/parties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private PartyMapper partyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartyMockMvc;

    private PartyEntity partyEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyEntity createEntity(EntityManager em) {
        PartyEntity partyEntity = new PartyEntity()
            .title(DEFAULT_TITLE)
            .partyCode(DEFAULT_PARTY_CODE)
            .tradeTitle(DEFAULT_TRADE_TITLE)
            .activationDate(DEFAULT_ACTIVATION_DATE)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .activationStatus(DEFAULT_ACTIVATION_STATUS)
            .description(DEFAULT_DESCRIPTION);
        return partyEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyEntity createUpdatedEntity(EntityManager em) {
        PartyEntity partyEntity = new PartyEntity()
            .title(UPDATED_TITLE)
            .partyCode(UPDATED_PARTY_CODE)
            .tradeTitle(UPDATED_TRADE_TITLE)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .activationStatus(UPDATED_ACTIVATION_STATUS)
            .description(UPDATED_DESCRIPTION);
        return partyEntity;
    }

    @BeforeEach
    public void initTest() {
        partyEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createParty() throws Exception {
        int databaseSizeBeforeCreate = partyRepository.findAll().size();
        // Create the Party
        PartyDTO partyDTO = partyMapper.toDto(partyEntity);
        restPartyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partyDTO)))
            .andExpect(status().isCreated());

        // Validate the Party in the database
        List<PartyEntity> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeCreate + 1);
        PartyEntity testParty = partyList.get(partyList.size() - 1);
        assertThat(testParty.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testParty.getPartyCode()).isEqualTo(DEFAULT_PARTY_CODE);
        assertThat(testParty.getTradeTitle()).isEqualTo(DEFAULT_TRADE_TITLE);
        assertThat(testParty.getActivationDate()).isEqualTo(DEFAULT_ACTIVATION_DATE);
        assertThat(testParty.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testParty.getActivationStatus()).isEqualTo(DEFAULT_ACTIVATION_STATUS);
        assertThat(testParty.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createPartyWithExistingId() throws Exception {
        // Create the Party with an existing ID
        partyEntity.setId(1L);
        PartyDTO partyDTO = partyMapper.toDto(partyEntity);

        int databaseSizeBeforeCreate = partyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Party in the database
        List<PartyEntity> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().size();
        // set the field null
        partyEntity.setTitle(null);

        // Create the Party, which fails.
        PartyDTO partyDTO = partyMapper.toDto(partyEntity);

        restPartyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partyDTO)))
            .andExpect(status().isBadRequest());

        List<PartyEntity> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPartyCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().size();
        // set the field null
        partyEntity.setPartyCode(null);

        // Create the Party, which fails.
        PartyDTO partyDTO = partyMapper.toDto(partyEntity);

        restPartyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partyDTO)))
            .andExpect(status().isBadRequest());

        List<PartyEntity> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTradeTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().size();
        // set the field null
        partyEntity.setTradeTitle(null);

        // Create the Party, which fails.
        PartyDTO partyDTO = partyMapper.toDto(partyEntity);

        restPartyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partyDTO)))
            .andExpect(status().isBadRequest());

        List<PartyEntity> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().size();
        // set the field null
        partyEntity.setActivationDate(null);

        // Create the Party, which fails.
        PartyDTO partyDTO = partyMapper.toDto(partyEntity);

        restPartyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partyDTO)))
            .andExpect(status().isBadRequest());

        List<PartyEntity> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivationStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().size();
        // set the field null
        partyEntity.setActivationStatus(null);

        // Create the Party, which fails.
        PartyDTO partyDTO = partyMapper.toDto(partyEntity);

        restPartyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partyDTO)))
            .andExpect(status().isBadRequest());

        List<PartyEntity> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllParties() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList
        restPartyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].partyCode").value(hasItem(DEFAULT_PARTY_CODE)))
            .andExpect(jsonPath("$.[*].tradeTitle").value(hasItem(DEFAULT_TRADE_TITLE)))
            .andExpect(jsonPath("$.[*].activationDate").value(hasItem(DEFAULT_ACTIVATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].activationStatus").value(hasItem(DEFAULT_ACTIVATION_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getParty() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get the party
        restPartyMockMvc
            .perform(get(ENTITY_API_URL_ID, partyEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partyEntity.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.partyCode").value(DEFAULT_PARTY_CODE))
            .andExpect(jsonPath("$.tradeTitle").value(DEFAULT_TRADE_TITLE))
            .andExpect(jsonPath("$.activationDate").value(DEFAULT_ACTIVATION_DATE.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.activationStatus").value(DEFAULT_ACTIVATION_STATUS.booleanValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getPartiesByIdFiltering() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        Long id = partyEntity.getId();

        defaultPartyShouldBeFound("id.equals=" + id);
        defaultPartyShouldNotBeFound("id.notEquals=" + id);

        defaultPartyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPartyShouldNotBeFound("id.greaterThan=" + id);

        defaultPartyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPartyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPartiesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where title equals to DEFAULT_TITLE
        defaultPartyShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the partyList where title equals to UPDATED_TITLE
        defaultPartyShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPartiesByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where title not equals to DEFAULT_TITLE
        defaultPartyShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the partyList where title not equals to UPDATED_TITLE
        defaultPartyShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPartiesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultPartyShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the partyList where title equals to UPDATED_TITLE
        defaultPartyShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPartiesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where title is not null
        defaultPartyShouldBeFound("title.specified=true");

        // Get all the partyList where title is null
        defaultPartyShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByTitleContainsSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where title contains DEFAULT_TITLE
        defaultPartyShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the partyList where title contains UPDATED_TITLE
        defaultPartyShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPartiesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where title does not contain DEFAULT_TITLE
        defaultPartyShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the partyList where title does not contain UPDATED_TITLE
        defaultPartyShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllPartiesByPartyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where partyCode equals to DEFAULT_PARTY_CODE
        defaultPartyShouldBeFound("partyCode.equals=" + DEFAULT_PARTY_CODE);

        // Get all the partyList where partyCode equals to UPDATED_PARTY_CODE
        defaultPartyShouldNotBeFound("partyCode.equals=" + UPDATED_PARTY_CODE);
    }

    @Test
    @Transactional
    void getAllPartiesByPartyCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where partyCode not equals to DEFAULT_PARTY_CODE
        defaultPartyShouldNotBeFound("partyCode.notEquals=" + DEFAULT_PARTY_CODE);

        // Get all the partyList where partyCode not equals to UPDATED_PARTY_CODE
        defaultPartyShouldBeFound("partyCode.notEquals=" + UPDATED_PARTY_CODE);
    }

    @Test
    @Transactional
    void getAllPartiesByPartyCodeIsInShouldWork() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where partyCode in DEFAULT_PARTY_CODE or UPDATED_PARTY_CODE
        defaultPartyShouldBeFound("partyCode.in=" + DEFAULT_PARTY_CODE + "," + UPDATED_PARTY_CODE);

        // Get all the partyList where partyCode equals to UPDATED_PARTY_CODE
        defaultPartyShouldNotBeFound("partyCode.in=" + UPDATED_PARTY_CODE);
    }

    @Test
    @Transactional
    void getAllPartiesByPartyCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where partyCode is not null
        defaultPartyShouldBeFound("partyCode.specified=true");

        // Get all the partyList where partyCode is null
        defaultPartyShouldNotBeFound("partyCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByPartyCodeContainsSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where partyCode contains DEFAULT_PARTY_CODE
        defaultPartyShouldBeFound("partyCode.contains=" + DEFAULT_PARTY_CODE);

        // Get all the partyList where partyCode contains UPDATED_PARTY_CODE
        defaultPartyShouldNotBeFound("partyCode.contains=" + UPDATED_PARTY_CODE);
    }

    @Test
    @Transactional
    void getAllPartiesByPartyCodeNotContainsSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where partyCode does not contain DEFAULT_PARTY_CODE
        defaultPartyShouldNotBeFound("partyCode.doesNotContain=" + DEFAULT_PARTY_CODE);

        // Get all the partyList where partyCode does not contain UPDATED_PARTY_CODE
        defaultPartyShouldBeFound("partyCode.doesNotContain=" + UPDATED_PARTY_CODE);
    }

    @Test
    @Transactional
    void getAllPartiesByTradeTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where tradeTitle equals to DEFAULT_TRADE_TITLE
        defaultPartyShouldBeFound("tradeTitle.equals=" + DEFAULT_TRADE_TITLE);

        // Get all the partyList where tradeTitle equals to UPDATED_TRADE_TITLE
        defaultPartyShouldNotBeFound("tradeTitle.equals=" + UPDATED_TRADE_TITLE);
    }

    @Test
    @Transactional
    void getAllPartiesByTradeTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where tradeTitle not equals to DEFAULT_TRADE_TITLE
        defaultPartyShouldNotBeFound("tradeTitle.notEquals=" + DEFAULT_TRADE_TITLE);

        // Get all the partyList where tradeTitle not equals to UPDATED_TRADE_TITLE
        defaultPartyShouldBeFound("tradeTitle.notEquals=" + UPDATED_TRADE_TITLE);
    }

    @Test
    @Transactional
    void getAllPartiesByTradeTitleIsInShouldWork() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where tradeTitle in DEFAULT_TRADE_TITLE or UPDATED_TRADE_TITLE
        defaultPartyShouldBeFound("tradeTitle.in=" + DEFAULT_TRADE_TITLE + "," + UPDATED_TRADE_TITLE);

        // Get all the partyList where tradeTitle equals to UPDATED_TRADE_TITLE
        defaultPartyShouldNotBeFound("tradeTitle.in=" + UPDATED_TRADE_TITLE);
    }

    @Test
    @Transactional
    void getAllPartiesByTradeTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where tradeTitle is not null
        defaultPartyShouldBeFound("tradeTitle.specified=true");

        // Get all the partyList where tradeTitle is null
        defaultPartyShouldNotBeFound("tradeTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByTradeTitleContainsSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where tradeTitle contains DEFAULT_TRADE_TITLE
        defaultPartyShouldBeFound("tradeTitle.contains=" + DEFAULT_TRADE_TITLE);

        // Get all the partyList where tradeTitle contains UPDATED_TRADE_TITLE
        defaultPartyShouldNotBeFound("tradeTitle.contains=" + UPDATED_TRADE_TITLE);
    }

    @Test
    @Transactional
    void getAllPartiesByTradeTitleNotContainsSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where tradeTitle does not contain DEFAULT_TRADE_TITLE
        defaultPartyShouldNotBeFound("tradeTitle.doesNotContain=" + DEFAULT_TRADE_TITLE);

        // Get all the partyList where tradeTitle does not contain UPDATED_TRADE_TITLE
        defaultPartyShouldBeFound("tradeTitle.doesNotContain=" + UPDATED_TRADE_TITLE);
    }

    @Test
    @Transactional
    void getAllPartiesByActivationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where activationDate equals to DEFAULT_ACTIVATION_DATE
        defaultPartyShouldBeFound("activationDate.equals=" + DEFAULT_ACTIVATION_DATE);

        // Get all the partyList where activationDate equals to UPDATED_ACTIVATION_DATE
        defaultPartyShouldNotBeFound("activationDate.equals=" + UPDATED_ACTIVATION_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByActivationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where activationDate not equals to DEFAULT_ACTIVATION_DATE
        defaultPartyShouldNotBeFound("activationDate.notEquals=" + DEFAULT_ACTIVATION_DATE);

        // Get all the partyList where activationDate not equals to UPDATED_ACTIVATION_DATE
        defaultPartyShouldBeFound("activationDate.notEquals=" + UPDATED_ACTIVATION_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByActivationDateIsInShouldWork() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where activationDate in DEFAULT_ACTIVATION_DATE or UPDATED_ACTIVATION_DATE
        defaultPartyShouldBeFound("activationDate.in=" + DEFAULT_ACTIVATION_DATE + "," + UPDATED_ACTIVATION_DATE);

        // Get all the partyList where activationDate equals to UPDATED_ACTIVATION_DATE
        defaultPartyShouldNotBeFound("activationDate.in=" + UPDATED_ACTIVATION_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByActivationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where activationDate is not null
        defaultPartyShouldBeFound("activationDate.specified=true");

        // Get all the partyList where activationDate is null
        defaultPartyShouldNotBeFound("activationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByActivationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where activationDate is greater than or equal to DEFAULT_ACTIVATION_DATE
        defaultPartyShouldBeFound("activationDate.greaterThanOrEqual=" + DEFAULT_ACTIVATION_DATE);

        // Get all the partyList where activationDate is greater than or equal to UPDATED_ACTIVATION_DATE
        defaultPartyShouldNotBeFound("activationDate.greaterThanOrEqual=" + UPDATED_ACTIVATION_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByActivationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where activationDate is less than or equal to DEFAULT_ACTIVATION_DATE
        defaultPartyShouldBeFound("activationDate.lessThanOrEqual=" + DEFAULT_ACTIVATION_DATE);

        // Get all the partyList where activationDate is less than or equal to SMALLER_ACTIVATION_DATE
        defaultPartyShouldNotBeFound("activationDate.lessThanOrEqual=" + SMALLER_ACTIVATION_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByActivationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where activationDate is less than DEFAULT_ACTIVATION_DATE
        defaultPartyShouldNotBeFound("activationDate.lessThan=" + DEFAULT_ACTIVATION_DATE);

        // Get all the partyList where activationDate is less than UPDATED_ACTIVATION_DATE
        defaultPartyShouldBeFound("activationDate.lessThan=" + UPDATED_ACTIVATION_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByActivationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where activationDate is greater than DEFAULT_ACTIVATION_DATE
        defaultPartyShouldNotBeFound("activationDate.greaterThan=" + DEFAULT_ACTIVATION_DATE);

        // Get all the partyList where activationDate is greater than SMALLER_ACTIVATION_DATE
        defaultPartyShouldBeFound("activationDate.greaterThan=" + SMALLER_ACTIVATION_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByExpirationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where expirationDate equals to DEFAULT_EXPIRATION_DATE
        defaultPartyShouldBeFound("expirationDate.equals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the partyList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultPartyShouldNotBeFound("expirationDate.equals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByExpirationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where expirationDate not equals to DEFAULT_EXPIRATION_DATE
        defaultPartyShouldNotBeFound("expirationDate.notEquals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the partyList where expirationDate not equals to UPDATED_EXPIRATION_DATE
        defaultPartyShouldBeFound("expirationDate.notEquals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByExpirationDateIsInShouldWork() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where expirationDate in DEFAULT_EXPIRATION_DATE or UPDATED_EXPIRATION_DATE
        defaultPartyShouldBeFound("expirationDate.in=" + DEFAULT_EXPIRATION_DATE + "," + UPDATED_EXPIRATION_DATE);

        // Get all the partyList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultPartyShouldNotBeFound("expirationDate.in=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByExpirationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where expirationDate is not null
        defaultPartyShouldBeFound("expirationDate.specified=true");

        // Get all the partyList where expirationDate is null
        defaultPartyShouldNotBeFound("expirationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByExpirationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where expirationDate is greater than or equal to DEFAULT_EXPIRATION_DATE
        defaultPartyShouldBeFound("expirationDate.greaterThanOrEqual=" + DEFAULT_EXPIRATION_DATE);

        // Get all the partyList where expirationDate is greater than or equal to UPDATED_EXPIRATION_DATE
        defaultPartyShouldNotBeFound("expirationDate.greaterThanOrEqual=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByExpirationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where expirationDate is less than or equal to DEFAULT_EXPIRATION_DATE
        defaultPartyShouldBeFound("expirationDate.lessThanOrEqual=" + DEFAULT_EXPIRATION_DATE);

        // Get all the partyList where expirationDate is less than or equal to SMALLER_EXPIRATION_DATE
        defaultPartyShouldNotBeFound("expirationDate.lessThanOrEqual=" + SMALLER_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByExpirationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where expirationDate is less than DEFAULT_EXPIRATION_DATE
        defaultPartyShouldNotBeFound("expirationDate.lessThan=" + DEFAULT_EXPIRATION_DATE);

        // Get all the partyList where expirationDate is less than UPDATED_EXPIRATION_DATE
        defaultPartyShouldBeFound("expirationDate.lessThan=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByExpirationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where expirationDate is greater than DEFAULT_EXPIRATION_DATE
        defaultPartyShouldNotBeFound("expirationDate.greaterThan=" + DEFAULT_EXPIRATION_DATE);

        // Get all the partyList where expirationDate is greater than SMALLER_EXPIRATION_DATE
        defaultPartyShouldBeFound("expirationDate.greaterThan=" + SMALLER_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllPartiesByActivationStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where activationStatus equals to DEFAULT_ACTIVATION_STATUS
        defaultPartyShouldBeFound("activationStatus.equals=" + DEFAULT_ACTIVATION_STATUS);

        // Get all the partyList where activationStatus equals to UPDATED_ACTIVATION_STATUS
        defaultPartyShouldNotBeFound("activationStatus.equals=" + UPDATED_ACTIVATION_STATUS);
    }

    @Test
    @Transactional
    void getAllPartiesByActivationStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where activationStatus not equals to DEFAULT_ACTIVATION_STATUS
        defaultPartyShouldNotBeFound("activationStatus.notEquals=" + DEFAULT_ACTIVATION_STATUS);

        // Get all the partyList where activationStatus not equals to UPDATED_ACTIVATION_STATUS
        defaultPartyShouldBeFound("activationStatus.notEquals=" + UPDATED_ACTIVATION_STATUS);
    }

    @Test
    @Transactional
    void getAllPartiesByActivationStatusIsInShouldWork() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where activationStatus in DEFAULT_ACTIVATION_STATUS or UPDATED_ACTIVATION_STATUS
        defaultPartyShouldBeFound("activationStatus.in=" + DEFAULT_ACTIVATION_STATUS + "," + UPDATED_ACTIVATION_STATUS);

        // Get all the partyList where activationStatus equals to UPDATED_ACTIVATION_STATUS
        defaultPartyShouldNotBeFound("activationStatus.in=" + UPDATED_ACTIVATION_STATUS);
    }

    @Test
    @Transactional
    void getAllPartiesByActivationStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where activationStatus is not null
        defaultPartyShouldBeFound("activationStatus.specified=true");

        // Get all the partyList where activationStatus is null
        defaultPartyShouldNotBeFound("activationStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where description equals to DEFAULT_DESCRIPTION
        defaultPartyShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the partyList where description equals to UPDATED_DESCRIPTION
        defaultPartyShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPartiesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where description not equals to DEFAULT_DESCRIPTION
        defaultPartyShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the partyList where description not equals to UPDATED_DESCRIPTION
        defaultPartyShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPartiesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPartyShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the partyList where description equals to UPDATED_DESCRIPTION
        defaultPartyShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPartiesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where description is not null
        defaultPartyShouldBeFound("description.specified=true");

        // Get all the partyList where description is null
        defaultPartyShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where description contains DEFAULT_DESCRIPTION
        defaultPartyShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the partyList where description contains UPDATED_DESCRIPTION
        defaultPartyShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPartiesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where description does not contain DEFAULT_DESCRIPTION
        defaultPartyShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the partyList where description does not contain UPDATED_DESCRIPTION
        defaultPartyShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPartiesByBranchsIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);
        BranchEntity branchs = BranchResourceIT.createEntity(em);
        em.persist(branchs);
        em.flush();
        partyEntity.addBranchs(branchs);
        partyRepository.saveAndFlush(partyEntity);
        Long branchsId = branchs.getId();

        // Get all the partyList where branchs equals to branchsId
        defaultPartyShouldBeFound("branchsId.equals=" + branchsId);

        // Get all the partyList where branchs equals to (branchsId + 1)
        defaultPartyShouldNotBeFound("branchsId.equals=" + (branchsId + 1));
    }

    @Test
    @Transactional
    void getAllPartiesByCriticismsIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);
        CriticismEntity criticisms = CriticismResourceIT.createEntity(em);
        em.persist(criticisms);
        em.flush();
        partyEntity.addCriticisms(criticisms);
        partyRepository.saveAndFlush(partyEntity);
        Long criticismsId = criticisms.getId();

        // Get all the partyList where criticisms equals to criticismsId
        defaultPartyShouldBeFound("criticismsId.equals=" + criticismsId);

        // Get all the partyList where criticisms equals to (criticismsId + 1)
        defaultPartyShouldNotBeFound("criticismsId.equals=" + (criticismsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartyShouldBeFound(String filter) throws Exception {
        restPartyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].partyCode").value(hasItem(DEFAULT_PARTY_CODE)))
            .andExpect(jsonPath("$.[*].tradeTitle").value(hasItem(DEFAULT_TRADE_TITLE)))
            .andExpect(jsonPath("$.[*].activationDate").value(hasItem(DEFAULT_ACTIVATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].activationStatus").value(hasItem(DEFAULT_ACTIVATION_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restPartyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPartyShouldNotBeFound(String filter) throws Exception {
        restPartyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingParty() throws Exception {
        // Get the party
        restPartyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParty() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        int databaseSizeBeforeUpdate = partyRepository.findAll().size();

        // Update the party
        PartyEntity updatedPartyEntity = partyRepository.findById(partyEntity.getId()).get();
        // Disconnect from session so that the updates on updatedPartyEntity are not directly saved in db
        em.detach(updatedPartyEntity);
        updatedPartyEntity
            .title(UPDATED_TITLE)
            .partyCode(UPDATED_PARTY_CODE)
            .tradeTitle(UPDATED_TRADE_TITLE)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .activationStatus(UPDATED_ACTIVATION_STATUS)
            .description(UPDATED_DESCRIPTION);
        PartyDTO partyDTO = partyMapper.toDto(updatedPartyEntity);

        restPartyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Party in the database
        List<PartyEntity> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
        PartyEntity testParty = partyList.get(partyList.size() - 1);
        assertThat(testParty.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testParty.getPartyCode()).isEqualTo(UPDATED_PARTY_CODE);
        assertThat(testParty.getTradeTitle()).isEqualTo(UPDATED_TRADE_TITLE);
        assertThat(testParty.getActivationDate()).isEqualTo(UPDATED_ACTIVATION_DATE);
        assertThat(testParty.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testParty.getActivationStatus()).isEqualTo(UPDATED_ACTIVATION_STATUS);
        assertThat(testParty.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().size();
        partyEntity.setId(count.incrementAndGet());

        // Create the Party
        PartyDTO partyDTO = partyMapper.toDto(partyEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Party in the database
        List<PartyEntity> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().size();
        partyEntity.setId(count.incrementAndGet());

        // Create the Party
        PartyDTO partyDTO = partyMapper.toDto(partyEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Party in the database
        List<PartyEntity> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().size();
        partyEntity.setId(count.incrementAndGet());

        // Create the Party
        PartyDTO partyDTO = partyMapper.toDto(partyEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Party in the database
        List<PartyEntity> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartyWithPatch() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        int databaseSizeBeforeUpdate = partyRepository.findAll().size();

        // Update the party using partial update
        PartyEntity partialUpdatedPartyEntity = new PartyEntity();
        partialUpdatedPartyEntity.setId(partyEntity.getId());

        partialUpdatedPartyEntity
            .title(UPDATED_TITLE)
            .partyCode(UPDATED_PARTY_CODE)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .activationStatus(UPDATED_ACTIVATION_STATUS)
            .description(UPDATED_DESCRIPTION);

        restPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartyEntity))
            )
            .andExpect(status().isOk());

        // Validate the Party in the database
        List<PartyEntity> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
        PartyEntity testParty = partyList.get(partyList.size() - 1);
        assertThat(testParty.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testParty.getPartyCode()).isEqualTo(UPDATED_PARTY_CODE);
        assertThat(testParty.getTradeTitle()).isEqualTo(DEFAULT_TRADE_TITLE);
        assertThat(testParty.getActivationDate()).isEqualTo(UPDATED_ACTIVATION_DATE);
        assertThat(testParty.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testParty.getActivationStatus()).isEqualTo(UPDATED_ACTIVATION_STATUS);
        assertThat(testParty.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdatePartyWithPatch() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        int databaseSizeBeforeUpdate = partyRepository.findAll().size();

        // Update the party using partial update
        PartyEntity partialUpdatedPartyEntity = new PartyEntity();
        partialUpdatedPartyEntity.setId(partyEntity.getId());

        partialUpdatedPartyEntity
            .title(UPDATED_TITLE)
            .partyCode(UPDATED_PARTY_CODE)
            .tradeTitle(UPDATED_TRADE_TITLE)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .activationStatus(UPDATED_ACTIVATION_STATUS)
            .description(UPDATED_DESCRIPTION);

        restPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartyEntity))
            )
            .andExpect(status().isOk());

        // Validate the Party in the database
        List<PartyEntity> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
        PartyEntity testParty = partyList.get(partyList.size() - 1);
        assertThat(testParty.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testParty.getPartyCode()).isEqualTo(UPDATED_PARTY_CODE);
        assertThat(testParty.getTradeTitle()).isEqualTo(UPDATED_TRADE_TITLE);
        assertThat(testParty.getActivationDate()).isEqualTo(UPDATED_ACTIVATION_DATE);
        assertThat(testParty.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testParty.getActivationStatus()).isEqualTo(UPDATED_ACTIVATION_STATUS);
        assertThat(testParty.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().size();
        partyEntity.setId(count.incrementAndGet());

        // Create the Party
        PartyDTO partyDTO = partyMapper.toDto(partyEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Party in the database
        List<PartyEntity> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().size();
        partyEntity.setId(count.incrementAndGet());

        // Create the Party
        PartyDTO partyDTO = partyMapper.toDto(partyEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Party in the database
        List<PartyEntity> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParty() throws Exception {
        int databaseSizeBeforeUpdate = partyRepository.findAll().size();
        partyEntity.setId(count.incrementAndGet());

        // Create the Party
        PartyDTO partyDTO = partyMapper.toDto(partyEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(partyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Party in the database
        List<PartyEntity> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParty() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        int databaseSizeBeforeDelete = partyRepository.findAll().size();

        // Delete the party
        restPartyMockMvc
            .perform(delete(ENTITY_API_URL_ID, partyEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PartyEntity> partyList = partyRepository.findAll();
        assertThat(partyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
