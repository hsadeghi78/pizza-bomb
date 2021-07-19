package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.AddressEntity;
import com.barad.bomb.domain.FactorEntity;
import com.barad.bomb.domain.PartyEntity;
import com.barad.bomb.repository.AddressRepository;
import com.barad.bomb.service.criteria.AddressCriteria;
import com.barad.bomb.service.dto.AddressDTO;
import com.barad.bomb.service.mapper.AddressMapper;
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
 * Integration tests for the {@link AddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AddressResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;
    private static final Double SMALLER_LAT = 1D - 1D;

    private static final Double DEFAULT_LON = 1D;
    private static final Double UPDATED_LON = 2D;
    private static final Double SMALLER_LON = 1D - 1D;

    private static final String DEFAULT_STREET_1 = "AAAAAAAAAA";
    private static final String UPDATED_STREET_1 = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_2 = "AAAAAAAAAA";
    private static final String UPDATED_STREET_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAddressMockMvc;

    private AddressEntity addressEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AddressEntity createEntity(EntityManager em) {
        AddressEntity addressEntity = new AddressEntity()
            .title(DEFAULT_TITLE)
            .lat(DEFAULT_LAT)
            .lon(DEFAULT_LON)
            .street1(DEFAULT_STREET_1)
            .street2(DEFAULT_STREET_2)
            .address(DEFAULT_ADDRESS)
            .postalCode(DEFAULT_POSTAL_CODE);
        // Add required entity
        PartyEntity party;
        if (TestUtil.findAll(em, PartyEntity.class).isEmpty()) {
            party = PartyResourceIT.createEntity(em);
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, PartyEntity.class).get(0);
        }
        addressEntity.setParty(party);
        return addressEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AddressEntity createUpdatedEntity(EntityManager em) {
        AddressEntity addressEntity = new AddressEntity()
            .title(UPDATED_TITLE)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .street1(UPDATED_STREET_1)
            .street2(UPDATED_STREET_2)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE);
        // Add required entity
        PartyEntity party;
        if (TestUtil.findAll(em, PartyEntity.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity(em);
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, PartyEntity.class).get(0);
        }
        addressEntity.setParty(party);
        return addressEntity;
    }

    @BeforeEach
    public void initTest() {
        addressEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createAddress() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();
        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(addressEntity);
        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isCreated());

        // Validate the Address in the database
        List<AddressEntity> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate + 1);
        AddressEntity testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAddress.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testAddress.getLon()).isEqualTo(DEFAULT_LON);
        assertThat(testAddress.getStreet1()).isEqualTo(DEFAULT_STREET_1);
        assertThat(testAddress.getStreet2()).isEqualTo(DEFAULT_STREET_2);
        assertThat(testAddress.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testAddress.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
    }

    @Test
    @Transactional
    void createAddressWithExistingId() throws Exception {
        // Create the Address with an existing ID
        addressEntity.setId(1L);
        AddressDTO addressDTO = addressMapper.toDto(addressEntity);

        int databaseSizeBeforeCreate = addressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<AddressEntity> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        addressEntity.setTitle(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(addressEntity);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<AddressEntity> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLatIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        addressEntity.setLat(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(addressEntity);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<AddressEntity> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLonIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        addressEntity.setLon(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(addressEntity);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<AddressEntity> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        addressEntity.setAddress(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(addressEntity);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<AddressEntity> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPostalCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        addressEntity.setPostalCode(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(addressEntity);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<AddressEntity> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAddresses() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(addressEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON.doubleValue())))
            .andExpect(jsonPath("$.[*].street1").value(hasItem(DEFAULT_STREET_1)))
            .andExpect(jsonPath("$.[*].street2").value(hasItem(DEFAULT_STREET_2)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)));
    }

    @Test
    @Transactional
    void getAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get the address
        restAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, addressEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(addressEntity.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.lon").value(DEFAULT_LON.doubleValue()))
            .andExpect(jsonPath("$.street1").value(DEFAULT_STREET_1))
            .andExpect(jsonPath("$.street2").value(DEFAULT_STREET_2))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE));
    }

    @Test
    @Transactional
    void getAddressesByIdFiltering() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        Long id = addressEntity.getId();

        defaultAddressShouldBeFound("id.equals=" + id);
        defaultAddressShouldNotBeFound("id.notEquals=" + id);

        defaultAddressShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAddressShouldNotBeFound("id.greaterThan=" + id);

        defaultAddressShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAddressShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAddressesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where title equals to DEFAULT_TITLE
        defaultAddressShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the addressList where title equals to UPDATED_TITLE
        defaultAddressShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAddressesByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where title not equals to DEFAULT_TITLE
        defaultAddressShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the addressList where title not equals to UPDATED_TITLE
        defaultAddressShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAddressesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultAddressShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the addressList where title equals to UPDATED_TITLE
        defaultAddressShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAddressesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where title is not null
        defaultAddressShouldBeFound("title.specified=true");

        // Get all the addressList where title is null
        defaultAddressShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByTitleContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where title contains DEFAULT_TITLE
        defaultAddressShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the addressList where title contains UPDATED_TITLE
        defaultAddressShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAddressesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where title does not contain DEFAULT_TITLE
        defaultAddressShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the addressList where title does not contain UPDATED_TITLE
        defaultAddressShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAddressesByLatIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where lat equals to DEFAULT_LAT
        defaultAddressShouldBeFound("lat.equals=" + DEFAULT_LAT);

        // Get all the addressList where lat equals to UPDATED_LAT
        defaultAddressShouldNotBeFound("lat.equals=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllAddressesByLatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where lat not equals to DEFAULT_LAT
        defaultAddressShouldNotBeFound("lat.notEquals=" + DEFAULT_LAT);

        // Get all the addressList where lat not equals to UPDATED_LAT
        defaultAddressShouldBeFound("lat.notEquals=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllAddressesByLatIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where lat in DEFAULT_LAT or UPDATED_LAT
        defaultAddressShouldBeFound("lat.in=" + DEFAULT_LAT + "," + UPDATED_LAT);

        // Get all the addressList where lat equals to UPDATED_LAT
        defaultAddressShouldNotBeFound("lat.in=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllAddressesByLatIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where lat is not null
        defaultAddressShouldBeFound("lat.specified=true");

        // Get all the addressList where lat is null
        defaultAddressShouldNotBeFound("lat.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByLatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where lat is greater than or equal to DEFAULT_LAT
        defaultAddressShouldBeFound("lat.greaterThanOrEqual=" + DEFAULT_LAT);

        // Get all the addressList where lat is greater than or equal to UPDATED_LAT
        defaultAddressShouldNotBeFound("lat.greaterThanOrEqual=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllAddressesByLatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where lat is less than or equal to DEFAULT_LAT
        defaultAddressShouldBeFound("lat.lessThanOrEqual=" + DEFAULT_LAT);

        // Get all the addressList where lat is less than or equal to SMALLER_LAT
        defaultAddressShouldNotBeFound("lat.lessThanOrEqual=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    void getAllAddressesByLatIsLessThanSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where lat is less than DEFAULT_LAT
        defaultAddressShouldNotBeFound("lat.lessThan=" + DEFAULT_LAT);

        // Get all the addressList where lat is less than UPDATED_LAT
        defaultAddressShouldBeFound("lat.lessThan=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllAddressesByLatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where lat is greater than DEFAULT_LAT
        defaultAddressShouldNotBeFound("lat.greaterThan=" + DEFAULT_LAT);

        // Get all the addressList where lat is greater than SMALLER_LAT
        defaultAddressShouldBeFound("lat.greaterThan=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    void getAllAddressesByLonIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where lon equals to DEFAULT_LON
        defaultAddressShouldBeFound("lon.equals=" + DEFAULT_LON);

        // Get all the addressList where lon equals to UPDATED_LON
        defaultAddressShouldNotBeFound("lon.equals=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllAddressesByLonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where lon not equals to DEFAULT_LON
        defaultAddressShouldNotBeFound("lon.notEquals=" + DEFAULT_LON);

        // Get all the addressList where lon not equals to UPDATED_LON
        defaultAddressShouldBeFound("lon.notEquals=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllAddressesByLonIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where lon in DEFAULT_LON or UPDATED_LON
        defaultAddressShouldBeFound("lon.in=" + DEFAULT_LON + "," + UPDATED_LON);

        // Get all the addressList where lon equals to UPDATED_LON
        defaultAddressShouldNotBeFound("lon.in=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllAddressesByLonIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where lon is not null
        defaultAddressShouldBeFound("lon.specified=true");

        // Get all the addressList where lon is null
        defaultAddressShouldNotBeFound("lon.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByLonIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where lon is greater than or equal to DEFAULT_LON
        defaultAddressShouldBeFound("lon.greaterThanOrEqual=" + DEFAULT_LON);

        // Get all the addressList where lon is greater than or equal to UPDATED_LON
        defaultAddressShouldNotBeFound("lon.greaterThanOrEqual=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllAddressesByLonIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where lon is less than or equal to DEFAULT_LON
        defaultAddressShouldBeFound("lon.lessThanOrEqual=" + DEFAULT_LON);

        // Get all the addressList where lon is less than or equal to SMALLER_LON
        defaultAddressShouldNotBeFound("lon.lessThanOrEqual=" + SMALLER_LON);
    }

    @Test
    @Transactional
    void getAllAddressesByLonIsLessThanSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where lon is less than DEFAULT_LON
        defaultAddressShouldNotBeFound("lon.lessThan=" + DEFAULT_LON);

        // Get all the addressList where lon is less than UPDATED_LON
        defaultAddressShouldBeFound("lon.lessThan=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllAddressesByLonIsGreaterThanSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where lon is greater than DEFAULT_LON
        defaultAddressShouldNotBeFound("lon.greaterThan=" + DEFAULT_LON);

        // Get all the addressList where lon is greater than SMALLER_LON
        defaultAddressShouldBeFound("lon.greaterThan=" + SMALLER_LON);
    }

    @Test
    @Transactional
    void getAllAddressesByStreet1IsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where street1 equals to DEFAULT_STREET_1
        defaultAddressShouldBeFound("street1.equals=" + DEFAULT_STREET_1);

        // Get all the addressList where street1 equals to UPDATED_STREET_1
        defaultAddressShouldNotBeFound("street1.equals=" + UPDATED_STREET_1);
    }

    @Test
    @Transactional
    void getAllAddressesByStreet1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where street1 not equals to DEFAULT_STREET_1
        defaultAddressShouldNotBeFound("street1.notEquals=" + DEFAULT_STREET_1);

        // Get all the addressList where street1 not equals to UPDATED_STREET_1
        defaultAddressShouldBeFound("street1.notEquals=" + UPDATED_STREET_1);
    }

    @Test
    @Transactional
    void getAllAddressesByStreet1IsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where street1 in DEFAULT_STREET_1 or UPDATED_STREET_1
        defaultAddressShouldBeFound("street1.in=" + DEFAULT_STREET_1 + "," + UPDATED_STREET_1);

        // Get all the addressList where street1 equals to UPDATED_STREET_1
        defaultAddressShouldNotBeFound("street1.in=" + UPDATED_STREET_1);
    }

    @Test
    @Transactional
    void getAllAddressesByStreet1IsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where street1 is not null
        defaultAddressShouldBeFound("street1.specified=true");

        // Get all the addressList where street1 is null
        defaultAddressShouldNotBeFound("street1.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByStreet1ContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where street1 contains DEFAULT_STREET_1
        defaultAddressShouldBeFound("street1.contains=" + DEFAULT_STREET_1);

        // Get all the addressList where street1 contains UPDATED_STREET_1
        defaultAddressShouldNotBeFound("street1.contains=" + UPDATED_STREET_1);
    }

    @Test
    @Transactional
    void getAllAddressesByStreet1NotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where street1 does not contain DEFAULT_STREET_1
        defaultAddressShouldNotBeFound("street1.doesNotContain=" + DEFAULT_STREET_1);

        // Get all the addressList where street1 does not contain UPDATED_STREET_1
        defaultAddressShouldBeFound("street1.doesNotContain=" + UPDATED_STREET_1);
    }

    @Test
    @Transactional
    void getAllAddressesByStreet2IsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where street2 equals to DEFAULT_STREET_2
        defaultAddressShouldBeFound("street2.equals=" + DEFAULT_STREET_2);

        // Get all the addressList where street2 equals to UPDATED_STREET_2
        defaultAddressShouldNotBeFound("street2.equals=" + UPDATED_STREET_2);
    }

    @Test
    @Transactional
    void getAllAddressesByStreet2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where street2 not equals to DEFAULT_STREET_2
        defaultAddressShouldNotBeFound("street2.notEquals=" + DEFAULT_STREET_2);

        // Get all the addressList where street2 not equals to UPDATED_STREET_2
        defaultAddressShouldBeFound("street2.notEquals=" + UPDATED_STREET_2);
    }

    @Test
    @Transactional
    void getAllAddressesByStreet2IsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where street2 in DEFAULT_STREET_2 or UPDATED_STREET_2
        defaultAddressShouldBeFound("street2.in=" + DEFAULT_STREET_2 + "," + UPDATED_STREET_2);

        // Get all the addressList where street2 equals to UPDATED_STREET_2
        defaultAddressShouldNotBeFound("street2.in=" + UPDATED_STREET_2);
    }

    @Test
    @Transactional
    void getAllAddressesByStreet2IsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where street2 is not null
        defaultAddressShouldBeFound("street2.specified=true");

        // Get all the addressList where street2 is null
        defaultAddressShouldNotBeFound("street2.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByStreet2ContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where street2 contains DEFAULT_STREET_2
        defaultAddressShouldBeFound("street2.contains=" + DEFAULT_STREET_2);

        // Get all the addressList where street2 contains UPDATED_STREET_2
        defaultAddressShouldNotBeFound("street2.contains=" + UPDATED_STREET_2);
    }

    @Test
    @Transactional
    void getAllAddressesByStreet2NotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where street2 does not contain DEFAULT_STREET_2
        defaultAddressShouldNotBeFound("street2.doesNotContain=" + DEFAULT_STREET_2);

        // Get all the addressList where street2 does not contain UPDATED_STREET_2
        defaultAddressShouldBeFound("street2.doesNotContain=" + UPDATED_STREET_2);
    }

    @Test
    @Transactional
    void getAllAddressesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where address equals to DEFAULT_ADDRESS
        defaultAddressShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the addressList where address equals to UPDATED_ADDRESS
        defaultAddressShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAddressesByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where address not equals to DEFAULT_ADDRESS
        defaultAddressShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the addressList where address not equals to UPDATED_ADDRESS
        defaultAddressShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAddressesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultAddressShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the addressList where address equals to UPDATED_ADDRESS
        defaultAddressShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAddressesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where address is not null
        defaultAddressShouldBeFound("address.specified=true");

        // Get all the addressList where address is null
        defaultAddressShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByAddressContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where address contains DEFAULT_ADDRESS
        defaultAddressShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the addressList where address contains UPDATED_ADDRESS
        defaultAddressShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAddressesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where address does not contain DEFAULT_ADDRESS
        defaultAddressShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the addressList where address does not contain UPDATED_ADDRESS
        defaultAddressShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAddressesByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the addressList where postalCode equals to UPDATED_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllAddressesByPostalCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where postalCode not equals to DEFAULT_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.notEquals=" + DEFAULT_POSTAL_CODE);

        // Get all the addressList where postalCode not equals to UPDATED_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.notEquals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllAddressesByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the addressList where postalCode equals to UPDATED_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllAddressesByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where postalCode is not null
        defaultAddressShouldBeFound("postalCode.specified=true");

        // Get all the addressList where postalCode is null
        defaultAddressShouldNotBeFound("postalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where postalCode contains DEFAULT_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.contains=" + DEFAULT_POSTAL_CODE);

        // Get all the addressList where postalCode contains UPDATED_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllAddressesByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        // Get all the addressList where postalCode does not contain DEFAULT_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE);

        // Get all the addressList where postalCode does not contain UPDATED_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.doesNotContain=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllAddressesByFactorsIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);
        FactorEntity factors = FactorResourceIT.createEntity(em);
        em.persist(factors);
        em.flush();
        addressEntity.addFactors(factors);
        addressRepository.saveAndFlush(addressEntity);
        Long factorsId = factors.getId();

        // Get all the addressList where factors equals to factorsId
        defaultAddressShouldBeFound("factorsId.equals=" + factorsId);

        // Get all the addressList where factors equals to (factorsId + 1)
        defaultAddressShouldNotBeFound("factorsId.equals=" + (factorsId + 1));
    }

    @Test
    @Transactional
    void getAllAddressesByPartyIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);
        PartyEntity party = PartyResourceIT.createEntity(em);
        em.persist(party);
        em.flush();
        addressEntity.setParty(party);
        addressRepository.saveAndFlush(addressEntity);
        Long partyId = party.getId();

        // Get all the addressList where party equals to partyId
        defaultAddressShouldBeFound("partyId.equals=" + partyId);

        // Get all the addressList where party equals to (partyId + 1)
        defaultAddressShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAddressShouldBeFound(String filter) throws Exception {
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(addressEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON.doubleValue())))
            .andExpect(jsonPath("$.[*].street1").value(hasItem(DEFAULT_STREET_1)))
            .andExpect(jsonPath("$.[*].street2").value(hasItem(DEFAULT_STREET_2)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)));

        // Check, that the count call also returns 1
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAddressShouldNotBeFound(String filter) throws Exception {
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAddress() throws Exception {
        // Get the address
        restAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address
        AddressEntity updatedAddressEntity = addressRepository.findById(addressEntity.getId()).get();
        // Disconnect from session so that the updates on updatedAddressEntity are not directly saved in db
        em.detach(updatedAddressEntity);
        updatedAddressEntity
            .title(UPDATED_TITLE)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .street1(UPDATED_STREET_1)
            .street2(UPDATED_STREET_2)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE);
        AddressDTO addressDTO = addressMapper.toDto(updatedAddressEntity);

        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<AddressEntity> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        AddressEntity testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAddress.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testAddress.getLon()).isEqualTo(UPDATED_LON);
        assertThat(testAddress.getStreet1()).isEqualTo(UPDATED_STREET_1);
        assertThat(testAddress.getStreet2()).isEqualTo(UPDATED_STREET_2);
        assertThat(testAddress.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testAddress.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void putNonExistingAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        addressEntity.setId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(addressEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<AddressEntity> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        addressEntity.setId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(addressEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<AddressEntity> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        addressEntity.setId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(addressEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Address in the database
        List<AddressEntity> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAddressWithPatch() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address using partial update
        AddressEntity partialUpdatedAddressEntity = new AddressEntity();
        partialUpdatedAddressEntity.setId(addressEntity.getId());

        partialUpdatedAddressEntity
            .title(UPDATED_TITLE)
            .lon(UPDATED_LON)
            .street1(UPDATED_STREET_1)
            .street2(UPDATED_STREET_2)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE);

        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddressEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddressEntity))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<AddressEntity> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        AddressEntity testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAddress.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testAddress.getLon()).isEqualTo(UPDATED_LON);
        assertThat(testAddress.getStreet1()).isEqualTo(UPDATED_STREET_1);
        assertThat(testAddress.getStreet2()).isEqualTo(UPDATED_STREET_2);
        assertThat(testAddress.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testAddress.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void fullUpdateAddressWithPatch() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address using partial update
        AddressEntity partialUpdatedAddressEntity = new AddressEntity();
        partialUpdatedAddressEntity.setId(addressEntity.getId());

        partialUpdatedAddressEntity
            .title(UPDATED_TITLE)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .street1(UPDATED_STREET_1)
            .street2(UPDATED_STREET_2)
            .address(UPDATED_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE);

        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddressEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddressEntity))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<AddressEntity> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        AddressEntity testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAddress.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testAddress.getLon()).isEqualTo(UPDATED_LON);
        assertThat(testAddress.getStreet1()).isEqualTo(UPDATED_STREET_1);
        assertThat(testAddress.getStreet2()).isEqualTo(UPDATED_STREET_2);
        assertThat(testAddress.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testAddress.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        addressEntity.setId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(addressEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, addressDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<AddressEntity> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        addressEntity.setId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(addressEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<AddressEntity> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        addressEntity.setId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(addressEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Address in the database
        List<AddressEntity> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(addressEntity);

        int databaseSizeBeforeDelete = addressRepository.findAll().size();

        // Delete the address
        restAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, addressEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AddressEntity> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
