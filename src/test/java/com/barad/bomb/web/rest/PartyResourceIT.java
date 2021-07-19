package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.AddressEntity;
import com.barad.bomb.domain.CommentEntity;
import com.barad.bomb.domain.ContactEntity;
import com.barad.bomb.domain.CriticismEntity;
import com.barad.bomb.domain.FactorEntity;
import com.barad.bomb.domain.FileDocumentEntity;
import com.barad.bomb.domain.FoodEntity;
import com.barad.bomb.domain.FoodTypeEntity;
import com.barad.bomb.domain.MenuItemEntity;
import com.barad.bomb.domain.PartnerEntity;
import com.barad.bomb.domain.PartyEntity;
import com.barad.bomb.domain.PartyEntity;
import com.barad.bomb.domain.PartyInformationEntity;
import com.barad.bomb.domain.PersonEntity;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link PartyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PartyResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

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

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;
    private static final Double SMALLER_LAT = 1D - 1D;

    private static final Double DEFAULT_LON = 1D;
    private static final Double UPDATED_LON = 2D;
    private static final Double SMALLER_LON = 1D - 1D;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final Long DEFAULT_PARTY_TYPE_CLASS_ID = 1L;
    private static final Long UPDATED_PARTY_TYPE_CLASS_ID = 2L;
    private static final Long SMALLER_PARTY_TYPE_CLASS_ID = 1L - 1L;

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
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .partyCode(DEFAULT_PARTY_CODE)
            .tradeTitle(DEFAULT_TRADE_TITLE)
            .activationDate(DEFAULT_ACTIVATION_DATE)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .activationStatus(DEFAULT_ACTIVATION_STATUS)
            .lat(DEFAULT_LAT)
            .lon(DEFAULT_LON)
            .address(DEFAULT_ADDRESS)
            .postalCode(DEFAULT_POSTAL_CODE)
            .mobile(DEFAULT_MOBILE)
            .partyTypeClassId(DEFAULT_PARTY_TYPE_CLASS_ID)
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
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .partyCode(UPDATED_PARTY_CODE)
            .tradeTitle(UPDATED_TRADE_TITLE)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .activationStatus(UPDATED_ACTIVATION_STATUS)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .mobile(UPDATED_MOBILE)
            .partyTypeClassId(UPDATED_PARTY_TYPE_CLASS_ID)
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
        assertThat(testParty.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testParty.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testParty.getPartyCode()).isEqualTo(DEFAULT_PARTY_CODE);
        assertThat(testParty.getTradeTitle()).isEqualTo(DEFAULT_TRADE_TITLE);
        assertThat(testParty.getActivationDate()).isEqualTo(DEFAULT_ACTIVATION_DATE);
        assertThat(testParty.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testParty.getActivationStatus()).isEqualTo(DEFAULT_ACTIVATION_STATUS);
        assertThat(testParty.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testParty.getLon()).isEqualTo(DEFAULT_LON);
        assertThat(testParty.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testParty.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testParty.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testParty.getPartyTypeClassId()).isEqualTo(DEFAULT_PARTY_TYPE_CLASS_ID);
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
    void checkLatIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().size();
        // set the field null
        partyEntity.setLat(null);

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
    void checkLonIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().size();
        // set the field null
        partyEntity.setLon(null);

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
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().size();
        // set the field null
        partyEntity.setAddress(null);

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
    void checkPostalCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().size();
        // set the field null
        partyEntity.setPostalCode(null);

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
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().size();
        // set the field null
        partyEntity.setMobile(null);

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
    void checkPartyTypeClassIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRepository.findAll().size();
        // set the field null
        partyEntity.setPartyTypeClassId(null);

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
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].partyCode").value(hasItem(DEFAULT_PARTY_CODE)))
            .andExpect(jsonPath("$.[*].tradeTitle").value(hasItem(DEFAULT_TRADE_TITLE)))
            .andExpect(jsonPath("$.[*].activationDate").value(hasItem(DEFAULT_ACTIVATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].activationStatus").value(hasItem(DEFAULT_ACTIVATION_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON.doubleValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].partyTypeClassId").value(hasItem(DEFAULT_PARTY_TYPE_CLASS_ID.intValue())))
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
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.partyCode").value(DEFAULT_PARTY_CODE))
            .andExpect(jsonPath("$.tradeTitle").value(DEFAULT_TRADE_TITLE))
            .andExpect(jsonPath("$.activationDate").value(DEFAULT_ACTIVATION_DATE.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.activationStatus").value(DEFAULT_ACTIVATION_STATUS.booleanValue()))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.lon").value(DEFAULT_LON.doubleValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.partyTypeClassId").value(DEFAULT_PARTY_TYPE_CLASS_ID.intValue()))
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
    void getAllPartiesByLatIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where lat equals to DEFAULT_LAT
        defaultPartyShouldBeFound("lat.equals=" + DEFAULT_LAT);

        // Get all the partyList where lat equals to UPDATED_LAT
        defaultPartyShouldNotBeFound("lat.equals=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllPartiesByLatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where lat not equals to DEFAULT_LAT
        defaultPartyShouldNotBeFound("lat.notEquals=" + DEFAULT_LAT);

        // Get all the partyList where lat not equals to UPDATED_LAT
        defaultPartyShouldBeFound("lat.notEquals=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllPartiesByLatIsInShouldWork() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where lat in DEFAULT_LAT or UPDATED_LAT
        defaultPartyShouldBeFound("lat.in=" + DEFAULT_LAT + "," + UPDATED_LAT);

        // Get all the partyList where lat equals to UPDATED_LAT
        defaultPartyShouldNotBeFound("lat.in=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllPartiesByLatIsNullOrNotNull() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where lat is not null
        defaultPartyShouldBeFound("lat.specified=true");

        // Get all the partyList where lat is null
        defaultPartyShouldNotBeFound("lat.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByLatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where lat is greater than or equal to DEFAULT_LAT
        defaultPartyShouldBeFound("lat.greaterThanOrEqual=" + DEFAULT_LAT);

        // Get all the partyList where lat is greater than or equal to UPDATED_LAT
        defaultPartyShouldNotBeFound("lat.greaterThanOrEqual=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllPartiesByLatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where lat is less than or equal to DEFAULT_LAT
        defaultPartyShouldBeFound("lat.lessThanOrEqual=" + DEFAULT_LAT);

        // Get all the partyList where lat is less than or equal to SMALLER_LAT
        defaultPartyShouldNotBeFound("lat.lessThanOrEqual=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    void getAllPartiesByLatIsLessThanSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where lat is less than DEFAULT_LAT
        defaultPartyShouldNotBeFound("lat.lessThan=" + DEFAULT_LAT);

        // Get all the partyList where lat is less than UPDATED_LAT
        defaultPartyShouldBeFound("lat.lessThan=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllPartiesByLatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where lat is greater than DEFAULT_LAT
        defaultPartyShouldNotBeFound("lat.greaterThan=" + DEFAULT_LAT);

        // Get all the partyList where lat is greater than SMALLER_LAT
        defaultPartyShouldBeFound("lat.greaterThan=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    void getAllPartiesByLonIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where lon equals to DEFAULT_LON
        defaultPartyShouldBeFound("lon.equals=" + DEFAULT_LON);

        // Get all the partyList where lon equals to UPDATED_LON
        defaultPartyShouldNotBeFound("lon.equals=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllPartiesByLonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where lon not equals to DEFAULT_LON
        defaultPartyShouldNotBeFound("lon.notEquals=" + DEFAULT_LON);

        // Get all the partyList where lon not equals to UPDATED_LON
        defaultPartyShouldBeFound("lon.notEquals=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllPartiesByLonIsInShouldWork() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where lon in DEFAULT_LON or UPDATED_LON
        defaultPartyShouldBeFound("lon.in=" + DEFAULT_LON + "," + UPDATED_LON);

        // Get all the partyList where lon equals to UPDATED_LON
        defaultPartyShouldNotBeFound("lon.in=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllPartiesByLonIsNullOrNotNull() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where lon is not null
        defaultPartyShouldBeFound("lon.specified=true");

        // Get all the partyList where lon is null
        defaultPartyShouldNotBeFound("lon.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByLonIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where lon is greater than or equal to DEFAULT_LON
        defaultPartyShouldBeFound("lon.greaterThanOrEqual=" + DEFAULT_LON);

        // Get all the partyList where lon is greater than or equal to UPDATED_LON
        defaultPartyShouldNotBeFound("lon.greaterThanOrEqual=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllPartiesByLonIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where lon is less than or equal to DEFAULT_LON
        defaultPartyShouldBeFound("lon.lessThanOrEqual=" + DEFAULT_LON);

        // Get all the partyList where lon is less than or equal to SMALLER_LON
        defaultPartyShouldNotBeFound("lon.lessThanOrEqual=" + SMALLER_LON);
    }

    @Test
    @Transactional
    void getAllPartiesByLonIsLessThanSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where lon is less than DEFAULT_LON
        defaultPartyShouldNotBeFound("lon.lessThan=" + DEFAULT_LON);

        // Get all the partyList where lon is less than UPDATED_LON
        defaultPartyShouldBeFound("lon.lessThan=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllPartiesByLonIsGreaterThanSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where lon is greater than DEFAULT_LON
        defaultPartyShouldNotBeFound("lon.greaterThan=" + DEFAULT_LON);

        // Get all the partyList where lon is greater than SMALLER_LON
        defaultPartyShouldBeFound("lon.greaterThan=" + SMALLER_LON);
    }

    @Test
    @Transactional
    void getAllPartiesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where address equals to DEFAULT_ADDRESS
        defaultPartyShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the partyList where address equals to UPDATED_ADDRESS
        defaultPartyShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllPartiesByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where address not equals to DEFAULT_ADDRESS
        defaultPartyShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the partyList where address not equals to UPDATED_ADDRESS
        defaultPartyShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllPartiesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultPartyShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the partyList where address equals to UPDATED_ADDRESS
        defaultPartyShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllPartiesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where address is not null
        defaultPartyShouldBeFound("address.specified=true");

        // Get all the partyList where address is null
        defaultPartyShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByAddressContainsSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where address contains DEFAULT_ADDRESS
        defaultPartyShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the partyList where address contains UPDATED_ADDRESS
        defaultPartyShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllPartiesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where address does not contain DEFAULT_ADDRESS
        defaultPartyShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the partyList where address does not contain UPDATED_ADDRESS
        defaultPartyShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllPartiesByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultPartyShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the partyList where postalCode equals to UPDATED_POSTAL_CODE
        defaultPartyShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllPartiesByPostalCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where postalCode not equals to DEFAULT_POSTAL_CODE
        defaultPartyShouldNotBeFound("postalCode.notEquals=" + DEFAULT_POSTAL_CODE);

        // Get all the partyList where postalCode not equals to UPDATED_POSTAL_CODE
        defaultPartyShouldBeFound("postalCode.notEquals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllPartiesByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultPartyShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the partyList where postalCode equals to UPDATED_POSTAL_CODE
        defaultPartyShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllPartiesByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where postalCode is not null
        defaultPartyShouldBeFound("postalCode.specified=true");

        // Get all the partyList where postalCode is null
        defaultPartyShouldNotBeFound("postalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where postalCode contains DEFAULT_POSTAL_CODE
        defaultPartyShouldBeFound("postalCode.contains=" + DEFAULT_POSTAL_CODE);

        // Get all the partyList where postalCode contains UPDATED_POSTAL_CODE
        defaultPartyShouldNotBeFound("postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllPartiesByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where postalCode does not contain DEFAULT_POSTAL_CODE
        defaultPartyShouldNotBeFound("postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE);

        // Get all the partyList where postalCode does not contain UPDATED_POSTAL_CODE
        defaultPartyShouldBeFound("postalCode.doesNotContain=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllPartiesByMobileIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where mobile equals to DEFAULT_MOBILE
        defaultPartyShouldBeFound("mobile.equals=" + DEFAULT_MOBILE);

        // Get all the partyList where mobile equals to UPDATED_MOBILE
        defaultPartyShouldNotBeFound("mobile.equals=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    void getAllPartiesByMobileIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where mobile not equals to DEFAULT_MOBILE
        defaultPartyShouldNotBeFound("mobile.notEquals=" + DEFAULT_MOBILE);

        // Get all the partyList where mobile not equals to UPDATED_MOBILE
        defaultPartyShouldBeFound("mobile.notEquals=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    void getAllPartiesByMobileIsInShouldWork() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where mobile in DEFAULT_MOBILE or UPDATED_MOBILE
        defaultPartyShouldBeFound("mobile.in=" + DEFAULT_MOBILE + "," + UPDATED_MOBILE);

        // Get all the partyList where mobile equals to UPDATED_MOBILE
        defaultPartyShouldNotBeFound("mobile.in=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    void getAllPartiesByMobileIsNullOrNotNull() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where mobile is not null
        defaultPartyShouldBeFound("mobile.specified=true");

        // Get all the partyList where mobile is null
        defaultPartyShouldNotBeFound("mobile.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByMobileContainsSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where mobile contains DEFAULT_MOBILE
        defaultPartyShouldBeFound("mobile.contains=" + DEFAULT_MOBILE);

        // Get all the partyList where mobile contains UPDATED_MOBILE
        defaultPartyShouldNotBeFound("mobile.contains=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    void getAllPartiesByMobileNotContainsSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where mobile does not contain DEFAULT_MOBILE
        defaultPartyShouldNotBeFound("mobile.doesNotContain=" + DEFAULT_MOBILE);

        // Get all the partyList where mobile does not contain UPDATED_MOBILE
        defaultPartyShouldBeFound("mobile.doesNotContain=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    void getAllPartiesByPartyTypeClassIdIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where partyTypeClassId equals to DEFAULT_PARTY_TYPE_CLASS_ID
        defaultPartyShouldBeFound("partyTypeClassId.equals=" + DEFAULT_PARTY_TYPE_CLASS_ID);

        // Get all the partyList where partyTypeClassId equals to UPDATED_PARTY_TYPE_CLASS_ID
        defaultPartyShouldNotBeFound("partyTypeClassId.equals=" + UPDATED_PARTY_TYPE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllPartiesByPartyTypeClassIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where partyTypeClassId not equals to DEFAULT_PARTY_TYPE_CLASS_ID
        defaultPartyShouldNotBeFound("partyTypeClassId.notEquals=" + DEFAULT_PARTY_TYPE_CLASS_ID);

        // Get all the partyList where partyTypeClassId not equals to UPDATED_PARTY_TYPE_CLASS_ID
        defaultPartyShouldBeFound("partyTypeClassId.notEquals=" + UPDATED_PARTY_TYPE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllPartiesByPartyTypeClassIdIsInShouldWork() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where partyTypeClassId in DEFAULT_PARTY_TYPE_CLASS_ID or UPDATED_PARTY_TYPE_CLASS_ID
        defaultPartyShouldBeFound("partyTypeClassId.in=" + DEFAULT_PARTY_TYPE_CLASS_ID + "," + UPDATED_PARTY_TYPE_CLASS_ID);

        // Get all the partyList where partyTypeClassId equals to UPDATED_PARTY_TYPE_CLASS_ID
        defaultPartyShouldNotBeFound("partyTypeClassId.in=" + UPDATED_PARTY_TYPE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllPartiesByPartyTypeClassIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where partyTypeClassId is not null
        defaultPartyShouldBeFound("partyTypeClassId.specified=true");

        // Get all the partyList where partyTypeClassId is null
        defaultPartyShouldNotBeFound("partyTypeClassId.specified=false");
    }

    @Test
    @Transactional
    void getAllPartiesByPartyTypeClassIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where partyTypeClassId is greater than or equal to DEFAULT_PARTY_TYPE_CLASS_ID
        defaultPartyShouldBeFound("partyTypeClassId.greaterThanOrEqual=" + DEFAULT_PARTY_TYPE_CLASS_ID);

        // Get all the partyList where partyTypeClassId is greater than or equal to UPDATED_PARTY_TYPE_CLASS_ID
        defaultPartyShouldNotBeFound("partyTypeClassId.greaterThanOrEqual=" + UPDATED_PARTY_TYPE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllPartiesByPartyTypeClassIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where partyTypeClassId is less than or equal to DEFAULT_PARTY_TYPE_CLASS_ID
        defaultPartyShouldBeFound("partyTypeClassId.lessThanOrEqual=" + DEFAULT_PARTY_TYPE_CLASS_ID);

        // Get all the partyList where partyTypeClassId is less than or equal to SMALLER_PARTY_TYPE_CLASS_ID
        defaultPartyShouldNotBeFound("partyTypeClassId.lessThanOrEqual=" + SMALLER_PARTY_TYPE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllPartiesByPartyTypeClassIdIsLessThanSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where partyTypeClassId is less than DEFAULT_PARTY_TYPE_CLASS_ID
        defaultPartyShouldNotBeFound("partyTypeClassId.lessThan=" + DEFAULT_PARTY_TYPE_CLASS_ID);

        // Get all the partyList where partyTypeClassId is less than UPDATED_PARTY_TYPE_CLASS_ID
        defaultPartyShouldBeFound("partyTypeClassId.lessThan=" + UPDATED_PARTY_TYPE_CLASS_ID);
    }

    @Test
    @Transactional
    void getAllPartiesByPartyTypeClassIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);

        // Get all the partyList where partyTypeClassId is greater than DEFAULT_PARTY_TYPE_CLASS_ID
        defaultPartyShouldNotBeFound("partyTypeClassId.greaterThan=" + DEFAULT_PARTY_TYPE_CLASS_ID);

        // Get all the partyList where partyTypeClassId is greater than SMALLER_PARTY_TYPE_CLASS_ID
        defaultPartyShouldBeFound("partyTypeClassId.greaterThan=" + SMALLER_PARTY_TYPE_CLASS_ID);
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

    @Test
    @Transactional
    void getAllPartiesByFilesIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);
        FileDocumentEntity files = FileDocumentResourceIT.createEntity(em);
        em.persist(files);
        em.flush();
        partyEntity.addFiles(files);
        partyRepository.saveAndFlush(partyEntity);
        Long filesId = files.getId();

        // Get all the partyList where files equals to filesId
        defaultPartyShouldBeFound("filesId.equals=" + filesId);

        // Get all the partyList where files equals to (filesId + 1)
        defaultPartyShouldNotBeFound("filesId.equals=" + (filesId + 1));
    }

    @Test
    @Transactional
    void getAllPartiesByMoreInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);
        PartyInformationEntity moreInfo = PartyInformationResourceIT.createEntity(em);
        em.persist(moreInfo);
        em.flush();
        partyEntity.addMoreInfo(moreInfo);
        partyRepository.saveAndFlush(partyEntity);
        Long moreInfoId = moreInfo.getId();

        // Get all the partyList where moreInfo equals to moreInfoId
        defaultPartyShouldBeFound("moreInfoId.equals=" + moreInfoId);

        // Get all the partyList where moreInfo equals to (moreInfoId + 1)
        defaultPartyShouldNotBeFound("moreInfoId.equals=" + (moreInfoId + 1));
    }

    @Test
    @Transactional
    void getAllPartiesByWritedCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);
        CommentEntity writedComments = CommentResourceIT.createEntity(em);
        em.persist(writedComments);
        em.flush();
        partyEntity.addWritedComments(writedComments);
        partyRepository.saveAndFlush(partyEntity);
        Long writedCommentsId = writedComments.getId();

        // Get all the partyList where writedComments equals to writedCommentsId
        defaultPartyShouldBeFound("writedCommentsId.equals=" + writedCommentsId);

        // Get all the partyList where writedComments equals to (writedCommentsId + 1)
        defaultPartyShouldNotBeFound("writedCommentsId.equals=" + (writedCommentsId + 1));
    }

    @Test
    @Transactional
    void getAllPartiesByAudienceCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);
        CommentEntity audienceComments = CommentResourceIT.createEntity(em);
        em.persist(audienceComments);
        em.flush();
        partyEntity.addAudienceComments(audienceComments);
        partyRepository.saveAndFlush(partyEntity);
        Long audienceCommentsId = audienceComments.getId();

        // Get all the partyList where audienceComments equals to audienceCommentsId
        defaultPartyShouldBeFound("audienceCommentsId.equals=" + audienceCommentsId);

        // Get all the partyList where audienceComments equals to (audienceCommentsId + 1)
        defaultPartyShouldNotBeFound("audienceCommentsId.equals=" + (audienceCommentsId + 1));
    }

    @Test
    @Transactional
    void getAllPartiesByFoodTypesIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);
        FoodTypeEntity foodTypes = FoodTypeResourceIT.createEntity(em);
        em.persist(foodTypes);
        em.flush();
        partyEntity.addFoodTypes(foodTypes);
        partyRepository.saveAndFlush(partyEntity);
        Long foodTypesId = foodTypes.getId();

        // Get all the partyList where foodTypes equals to foodTypesId
        defaultPartyShouldBeFound("foodTypesId.equals=" + foodTypesId);

        // Get all the partyList where foodTypes equals to (foodTypesId + 1)
        defaultPartyShouldNotBeFound("foodTypesId.equals=" + (foodTypesId + 1));
    }

    @Test
    @Transactional
    void getAllPartiesByChildrenIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);
        PartyEntity children = PartyResourceIT.createEntity(em);
        em.persist(children);
        em.flush();
        partyEntity.addChildren(children);
        partyRepository.saveAndFlush(partyEntity);
        Long childrenId = children.getId();

        // Get all the partyList where children equals to childrenId
        defaultPartyShouldBeFound("childrenId.equals=" + childrenId);

        // Get all the partyList where children equals to (childrenId + 1)
        defaultPartyShouldNotBeFound("childrenId.equals=" + (childrenId + 1));
    }

    @Test
    @Transactional
    void getAllPartiesByContactsIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);
        ContactEntity contacts = ContactResourceIT.createEntity(em);
        em.persist(contacts);
        em.flush();
        partyEntity.addContacts(contacts);
        partyRepository.saveAndFlush(partyEntity);
        Long contactsId = contacts.getId();

        // Get all the partyList where contacts equals to contactsId
        defaultPartyShouldBeFound("contactsId.equals=" + contactsId);

        // Get all the partyList where contacts equals to (contactsId + 1)
        defaultPartyShouldNotBeFound("contactsId.equals=" + (contactsId + 1));
    }

    @Test
    @Transactional
    void getAllPartiesByAddressesIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);
        AddressEntity addresses = AddressResourceIT.createEntity(em);
        em.persist(addresses);
        em.flush();
        partyEntity.addAddresses(addresses);
        partyRepository.saveAndFlush(partyEntity);
        Long addressesId = addresses.getId();

        // Get all the partyList where addresses equals to addressesId
        defaultPartyShouldBeFound("addressesId.equals=" + addressesId);

        // Get all the partyList where addresses equals to (addressesId + 1)
        defaultPartyShouldNotBeFound("addressesId.equals=" + (addressesId + 1));
    }

    @Test
    @Transactional
    void getAllPartiesByMenuItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);
        MenuItemEntity menuItems = MenuItemResourceIT.createEntity(em);
        em.persist(menuItems);
        em.flush();
        partyEntity.addMenuItems(menuItems);
        partyRepository.saveAndFlush(partyEntity);
        Long menuItemsId = menuItems.getId();

        // Get all the partyList where menuItems equals to menuItemsId
        defaultPartyShouldBeFound("menuItemsId.equals=" + menuItemsId);

        // Get all the partyList where menuItems equals to (menuItemsId + 1)
        defaultPartyShouldNotBeFound("menuItemsId.equals=" + (menuItemsId + 1));
    }

    @Test
    @Transactional
    void getAllPartiesByProduceFoodsIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);
        FoodEntity produceFoods = FoodResourceIT.createEntity(em);
        em.persist(produceFoods);
        em.flush();
        partyEntity.addProduceFoods(produceFoods);
        partyRepository.saveAndFlush(partyEntity);
        Long produceFoodsId = produceFoods.getId();

        // Get all the partyList where produceFoods equals to produceFoodsId
        defaultPartyShouldBeFound("produceFoodsId.equals=" + produceFoodsId);

        // Get all the partyList where produceFoods equals to (produceFoodsId + 1)
        defaultPartyShouldNotBeFound("produceFoodsId.equals=" + (produceFoodsId + 1));
    }

    @Test
    @Transactional
    void getAllPartiesByDesignedFoodsIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);
        FoodEntity designedFoods = FoodResourceIT.createEntity(em);
        em.persist(designedFoods);
        em.flush();
        partyEntity.addDesignedFoods(designedFoods);
        partyRepository.saveAndFlush(partyEntity);
        Long designedFoodsId = designedFoods.getId();

        // Get all the partyList where designedFoods equals to designedFoodsId
        defaultPartyShouldBeFound("designedFoodsId.equals=" + designedFoodsId);

        // Get all the partyList where designedFoods equals to (designedFoodsId + 1)
        defaultPartyShouldNotBeFound("designedFoodsId.equals=" + (designedFoodsId + 1));
    }

    @Test
    @Transactional
    void getAllPartiesByBuyerFactorsIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);
        FactorEntity buyerFactors = FactorResourceIT.createEntity(em);
        em.persist(buyerFactors);
        em.flush();
        partyEntity.addBuyerFactors(buyerFactors);
        partyRepository.saveAndFlush(partyEntity);
        Long buyerFactorsId = buyerFactors.getId();

        // Get all the partyList where buyerFactors equals to buyerFactorsId
        defaultPartyShouldBeFound("buyerFactorsId.equals=" + buyerFactorsId);

        // Get all the partyList where buyerFactors equals to (buyerFactorsId + 1)
        defaultPartyShouldNotBeFound("buyerFactorsId.equals=" + (buyerFactorsId + 1));
    }

    @Test
    @Transactional
    void getAllPartiesBySellerFactorsIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);
        FactorEntity sellerFactors = FactorResourceIT.createEntity(em);
        em.persist(sellerFactors);
        em.flush();
        partyEntity.addSellerFactors(sellerFactors);
        partyRepository.saveAndFlush(partyEntity);
        Long sellerFactorsId = sellerFactors.getId();

        // Get all the partyList where sellerFactors equals to sellerFactorsId
        defaultPartyShouldBeFound("sellerFactorsId.equals=" + sellerFactorsId);

        // Get all the partyList where sellerFactors equals to (sellerFactorsId + 1)
        defaultPartyShouldNotBeFound("sellerFactorsId.equals=" + (sellerFactorsId + 1));
    }

    @Test
    @Transactional
    void getAllPartiesByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);
        PartyEntity parent = PartyResourceIT.createEntity(em);
        em.persist(parent);
        em.flush();
        partyEntity.setParent(parent);
        partyRepository.saveAndFlush(partyEntity);
        Long parentId = parent.getId();

        // Get all the partyList where parent equals to parentId
        defaultPartyShouldBeFound("parentId.equals=" + parentId);

        // Get all the partyList where parent equals to (parentId + 1)
        defaultPartyShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    @Test
    @Transactional
    void getAllPartiesByPartnerIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);
        PartnerEntity partner = PartnerResourceIT.createEntity(em);
        em.persist(partner);
        em.flush();
        partyEntity.setPartner(partner);
        partyRepository.saveAndFlush(partyEntity);
        Long partnerId = partner.getId();

        // Get all the partyList where partner equals to partnerId
        defaultPartyShouldBeFound("partnerId.equals=" + partnerId);

        // Get all the partyList where partner equals to (partnerId + 1)
        defaultPartyShouldNotBeFound("partnerId.equals=" + (partnerId + 1));
    }

    @Test
    @Transactional
    void getAllPartiesByPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRepository.saveAndFlush(partyEntity);
        PersonEntity person = PersonResourceIT.createEntity(em);
        em.persist(person);
        em.flush();
        partyEntity.setPerson(person);
        partyRepository.saveAndFlush(partyEntity);
        Long personId = person.getId();

        // Get all the partyList where person equals to personId
        defaultPartyShouldBeFound("personId.equals=" + personId);

        // Get all the partyList where person equals to (personId + 1)
        defaultPartyShouldNotBeFound("personId.equals=" + (personId + 1));
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
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].partyCode").value(hasItem(DEFAULT_PARTY_CODE)))
            .andExpect(jsonPath("$.[*].tradeTitle").value(hasItem(DEFAULT_TRADE_TITLE)))
            .andExpect(jsonPath("$.[*].activationDate").value(hasItem(DEFAULT_ACTIVATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].activationStatus").value(hasItem(DEFAULT_ACTIVATION_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON.doubleValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].partyTypeClassId").value(hasItem(DEFAULT_PARTY_TYPE_CLASS_ID.intValue())))
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
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .partyCode(UPDATED_PARTY_CODE)
            .tradeTitle(UPDATED_TRADE_TITLE)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .activationStatus(UPDATED_ACTIVATION_STATUS)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .mobile(UPDATED_MOBILE)
            .partyTypeClassId(UPDATED_PARTY_TYPE_CLASS_ID)
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
        assertThat(testParty.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testParty.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testParty.getPartyCode()).isEqualTo(UPDATED_PARTY_CODE);
        assertThat(testParty.getTradeTitle()).isEqualTo(UPDATED_TRADE_TITLE);
        assertThat(testParty.getActivationDate()).isEqualTo(UPDATED_ACTIVATION_DATE);
        assertThat(testParty.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testParty.getActivationStatus()).isEqualTo(UPDATED_ACTIVATION_STATUS);
        assertThat(testParty.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testParty.getLon()).isEqualTo(UPDATED_LON);
        assertThat(testParty.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testParty.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testParty.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testParty.getPartyTypeClassId()).isEqualTo(UPDATED_PARTY_TYPE_CLASS_ID);
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
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .tradeTitle(UPDATED_TRADE_TITLE)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .activationStatus(UPDATED_ACTIVATION_STATUS)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .address(UPDATED_ADDRESS)
            .mobile(UPDATED_MOBILE)
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
        assertThat(testParty.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testParty.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testParty.getPartyCode()).isEqualTo(DEFAULT_PARTY_CODE);
        assertThat(testParty.getTradeTitle()).isEqualTo(UPDATED_TRADE_TITLE);
        assertThat(testParty.getActivationDate()).isEqualTo(UPDATED_ACTIVATION_DATE);
        assertThat(testParty.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testParty.getActivationStatus()).isEqualTo(UPDATED_ACTIVATION_STATUS);
        assertThat(testParty.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testParty.getLon()).isEqualTo(UPDATED_LON);
        assertThat(testParty.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testParty.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testParty.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testParty.getPartyTypeClassId()).isEqualTo(DEFAULT_PARTY_TYPE_CLASS_ID);
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
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .partyCode(UPDATED_PARTY_CODE)
            .tradeTitle(UPDATED_TRADE_TITLE)
            .activationDate(UPDATED_ACTIVATION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .activationStatus(UPDATED_ACTIVATION_STATUS)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .mobile(UPDATED_MOBILE)
            .partyTypeClassId(UPDATED_PARTY_TYPE_CLASS_ID)
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
        assertThat(testParty.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testParty.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testParty.getPartyCode()).isEqualTo(UPDATED_PARTY_CODE);
        assertThat(testParty.getTradeTitle()).isEqualTo(UPDATED_TRADE_TITLE);
        assertThat(testParty.getActivationDate()).isEqualTo(UPDATED_ACTIVATION_DATE);
        assertThat(testParty.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testParty.getActivationStatus()).isEqualTo(UPDATED_ACTIVATION_STATUS);
        assertThat(testParty.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testParty.getLon()).isEqualTo(UPDATED_LON);
        assertThat(testParty.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testParty.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testParty.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testParty.getPartyTypeClassId()).isEqualTo(UPDATED_PARTY_TYPE_CLASS_ID);
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
