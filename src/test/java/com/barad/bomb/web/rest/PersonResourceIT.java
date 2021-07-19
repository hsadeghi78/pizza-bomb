package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.PartyEntity;
import com.barad.bomb.domain.PersonEntity;
import com.barad.bomb.repository.PersonRepository;
import com.barad.bomb.service.criteria.PersonCriteria;
import com.barad.bomb.service.dto.PersonDTO;
import com.barad.bomb.service.mapper.PersonMapper;
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
 * Integration tests for the {@link PersonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonResourceIT {

    private static final String DEFAULT_FISRT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FISRT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BIRTH_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_NATIONAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_NATIONAL_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/people";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonMockMvc;

    private PersonEntity personEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonEntity createEntity(EntityManager em) {
        PersonEntity personEntity = new PersonEntity()
            .fisrtName(DEFAULT_FISRT_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .birthDate(DEFAULT_BIRTH_DATE)
            .nationalCode(DEFAULT_NATIONAL_CODE);
        return personEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonEntity createUpdatedEntity(EntityManager em) {
        PersonEntity personEntity = new PersonEntity()
            .fisrtName(UPDATED_FISRT_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .nationalCode(UPDATED_NATIONAL_CODE);
        return personEntity;
    }

    @BeforeEach
    public void initTest() {
        personEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createPerson() throws Exception {
        int databaseSizeBeforeCreate = personRepository.findAll().size();
        // Create the Person
        PersonDTO personDTO = personMapper.toDto(personEntity);
        restPersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isCreated());

        // Validate the Person in the database
        List<PersonEntity> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate + 1);
        PersonEntity testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getFisrtName()).isEqualTo(DEFAULT_FISRT_NAME);
        assertThat(testPerson.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPerson.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testPerson.getNationalCode()).isEqualTo(DEFAULT_NATIONAL_CODE);
    }

    @Test
    @Transactional
    void createPersonWithExistingId() throws Exception {
        // Create the Person with an existing ID
        personEntity.setId(1L);
        PersonDTO personDTO = personMapper.toDto(personEntity);

        int databaseSizeBeforeCreate = personRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<PersonEntity> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFisrtNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = personRepository.findAll().size();
        // set the field null
        personEntity.setFisrtName(null);

        // Create the Person, which fails.
        PersonDTO personDTO = personMapper.toDto(personEntity);

        restPersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isBadRequest());

        List<PersonEntity> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = personRepository.findAll().size();
        // set the field null
        personEntity.setLastName(null);

        // Create the Person, which fails.
        PersonDTO personDTO = personMapper.toDto(personEntity);

        restPersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isBadRequest());

        List<PersonEntity> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNationalCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = personRepository.findAll().size();
        // set the field null
        personEntity.setNationalCode(null);

        // Create the Person, which fails.
        PersonDTO personDTO = personMapper.toDto(personEntity);

        restPersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isBadRequest());

        List<PersonEntity> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPeople() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].fisrtName").value(hasItem(DEFAULT_FISRT_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].nationalCode").value(hasItem(DEFAULT_NATIONAL_CODE)));
    }

    @Test
    @Transactional
    void getPerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get the person
        restPersonMockMvc
            .perform(get(ENTITY_API_URL_ID, personEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personEntity.getId().intValue()))
            .andExpect(jsonPath("$.fisrtName").value(DEFAULT_FISRT_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.nationalCode").value(DEFAULT_NATIONAL_CODE));
    }

    @Test
    @Transactional
    void getPeopleByIdFiltering() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        Long id = personEntity.getId();

        defaultPersonShouldBeFound("id.equals=" + id);
        defaultPersonShouldNotBeFound("id.notEquals=" + id);

        defaultPersonShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPersonShouldNotBeFound("id.greaterThan=" + id);

        defaultPersonShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPersonShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPeopleByFisrtNameIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where fisrtName equals to DEFAULT_FISRT_NAME
        defaultPersonShouldBeFound("fisrtName.equals=" + DEFAULT_FISRT_NAME);

        // Get all the personList where fisrtName equals to UPDATED_FISRT_NAME
        defaultPersonShouldNotBeFound("fisrtName.equals=" + UPDATED_FISRT_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByFisrtNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where fisrtName not equals to DEFAULT_FISRT_NAME
        defaultPersonShouldNotBeFound("fisrtName.notEquals=" + DEFAULT_FISRT_NAME);

        // Get all the personList where fisrtName not equals to UPDATED_FISRT_NAME
        defaultPersonShouldBeFound("fisrtName.notEquals=" + UPDATED_FISRT_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByFisrtNameIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where fisrtName in DEFAULT_FISRT_NAME or UPDATED_FISRT_NAME
        defaultPersonShouldBeFound("fisrtName.in=" + DEFAULT_FISRT_NAME + "," + UPDATED_FISRT_NAME);

        // Get all the personList where fisrtName equals to UPDATED_FISRT_NAME
        defaultPersonShouldNotBeFound("fisrtName.in=" + UPDATED_FISRT_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByFisrtNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where fisrtName is not null
        defaultPersonShouldBeFound("fisrtName.specified=true");

        // Get all the personList where fisrtName is null
        defaultPersonShouldNotBeFound("fisrtName.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByFisrtNameContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where fisrtName contains DEFAULT_FISRT_NAME
        defaultPersonShouldBeFound("fisrtName.contains=" + DEFAULT_FISRT_NAME);

        // Get all the personList where fisrtName contains UPDATED_FISRT_NAME
        defaultPersonShouldNotBeFound("fisrtName.contains=" + UPDATED_FISRT_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByFisrtNameNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where fisrtName does not contain DEFAULT_FISRT_NAME
        defaultPersonShouldNotBeFound("fisrtName.doesNotContain=" + DEFAULT_FISRT_NAME);

        // Get all the personList where fisrtName does not contain UPDATED_FISRT_NAME
        defaultPersonShouldBeFound("fisrtName.doesNotContain=" + UPDATED_FISRT_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where lastName equals to DEFAULT_LAST_NAME
        defaultPersonShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the personList where lastName equals to UPDATED_LAST_NAME
        defaultPersonShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where lastName not equals to DEFAULT_LAST_NAME
        defaultPersonShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the personList where lastName not equals to UPDATED_LAST_NAME
        defaultPersonShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultPersonShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the personList where lastName equals to UPDATED_LAST_NAME
        defaultPersonShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where lastName is not null
        defaultPersonShouldBeFound("lastName.specified=true");

        // Get all the personList where lastName is null
        defaultPersonShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByLastNameContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where lastName contains DEFAULT_LAST_NAME
        defaultPersonShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the personList where lastName contains UPDATED_LAST_NAME
        defaultPersonShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where lastName does not contain DEFAULT_LAST_NAME
        defaultPersonShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the personList where lastName does not contain UPDATED_LAST_NAME
        defaultPersonShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where birthDate equals to DEFAULT_BIRTH_DATE
        defaultPersonShouldBeFound("birthDate.equals=" + DEFAULT_BIRTH_DATE);

        // Get all the personList where birthDate equals to UPDATED_BIRTH_DATE
        defaultPersonShouldNotBeFound("birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByBirthDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where birthDate not equals to DEFAULT_BIRTH_DATE
        defaultPersonShouldNotBeFound("birthDate.notEquals=" + DEFAULT_BIRTH_DATE);

        // Get all the personList where birthDate not equals to UPDATED_BIRTH_DATE
        defaultPersonShouldBeFound("birthDate.notEquals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where birthDate in DEFAULT_BIRTH_DATE or UPDATED_BIRTH_DATE
        defaultPersonShouldBeFound("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE);

        // Get all the personList where birthDate equals to UPDATED_BIRTH_DATE
        defaultPersonShouldNotBeFound("birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where birthDate is not null
        defaultPersonShouldBeFound("birthDate.specified=true");

        // Get all the personList where birthDate is null
        defaultPersonShouldNotBeFound("birthDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where birthDate is greater than or equal to DEFAULT_BIRTH_DATE
        defaultPersonShouldBeFound("birthDate.greaterThanOrEqual=" + DEFAULT_BIRTH_DATE);

        // Get all the personList where birthDate is greater than or equal to UPDATED_BIRTH_DATE
        defaultPersonShouldNotBeFound("birthDate.greaterThanOrEqual=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByBirthDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where birthDate is less than or equal to DEFAULT_BIRTH_DATE
        defaultPersonShouldBeFound("birthDate.lessThanOrEqual=" + DEFAULT_BIRTH_DATE);

        // Get all the personList where birthDate is less than or equal to SMALLER_BIRTH_DATE
        defaultPersonShouldNotBeFound("birthDate.lessThanOrEqual=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where birthDate is less than DEFAULT_BIRTH_DATE
        defaultPersonShouldNotBeFound("birthDate.lessThan=" + DEFAULT_BIRTH_DATE);

        // Get all the personList where birthDate is less than UPDATED_BIRTH_DATE
        defaultPersonShouldBeFound("birthDate.lessThan=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByBirthDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where birthDate is greater than DEFAULT_BIRTH_DATE
        defaultPersonShouldNotBeFound("birthDate.greaterThan=" + DEFAULT_BIRTH_DATE);

        // Get all the personList where birthDate is greater than SMALLER_BIRTH_DATE
        defaultPersonShouldBeFound("birthDate.greaterThan=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByNationalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where nationalCode equals to DEFAULT_NATIONAL_CODE
        defaultPersonShouldBeFound("nationalCode.equals=" + DEFAULT_NATIONAL_CODE);

        // Get all the personList where nationalCode equals to UPDATED_NATIONAL_CODE
        defaultPersonShouldNotBeFound("nationalCode.equals=" + UPDATED_NATIONAL_CODE);
    }

    @Test
    @Transactional
    void getAllPeopleByNationalCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where nationalCode not equals to DEFAULT_NATIONAL_CODE
        defaultPersonShouldNotBeFound("nationalCode.notEquals=" + DEFAULT_NATIONAL_CODE);

        // Get all the personList where nationalCode not equals to UPDATED_NATIONAL_CODE
        defaultPersonShouldBeFound("nationalCode.notEquals=" + UPDATED_NATIONAL_CODE);
    }

    @Test
    @Transactional
    void getAllPeopleByNationalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where nationalCode in DEFAULT_NATIONAL_CODE or UPDATED_NATIONAL_CODE
        defaultPersonShouldBeFound("nationalCode.in=" + DEFAULT_NATIONAL_CODE + "," + UPDATED_NATIONAL_CODE);

        // Get all the personList where nationalCode equals to UPDATED_NATIONAL_CODE
        defaultPersonShouldNotBeFound("nationalCode.in=" + UPDATED_NATIONAL_CODE);
    }

    @Test
    @Transactional
    void getAllPeopleByNationalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where nationalCode is not null
        defaultPersonShouldBeFound("nationalCode.specified=true");

        // Get all the personList where nationalCode is null
        defaultPersonShouldNotBeFound("nationalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByNationalCodeContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where nationalCode contains DEFAULT_NATIONAL_CODE
        defaultPersonShouldBeFound("nationalCode.contains=" + DEFAULT_NATIONAL_CODE);

        // Get all the personList where nationalCode contains UPDATED_NATIONAL_CODE
        defaultPersonShouldNotBeFound("nationalCode.contains=" + UPDATED_NATIONAL_CODE);
    }

    @Test
    @Transactional
    void getAllPeopleByNationalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        // Get all the personList where nationalCode does not contain DEFAULT_NATIONAL_CODE
        defaultPersonShouldNotBeFound("nationalCode.doesNotContain=" + DEFAULT_NATIONAL_CODE);

        // Get all the personList where nationalCode does not contain UPDATED_NATIONAL_CODE
        defaultPersonShouldBeFound("nationalCode.doesNotContain=" + UPDATED_NATIONAL_CODE);
    }

    @Test
    @Transactional
    void getAllPeopleByPartiesIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);
        PartyEntity parties = PartyResourceIT.createEntity(em);
        em.persist(parties);
        em.flush();
        personEntity.addParties(parties);
        personRepository.saveAndFlush(personEntity);
        Long partiesId = parties.getId();

        // Get all the personList where parties equals to partiesId
        defaultPersonShouldBeFound("partiesId.equals=" + partiesId);

        // Get all the personList where parties equals to (partiesId + 1)
        defaultPersonShouldNotBeFound("partiesId.equals=" + (partiesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonShouldBeFound(String filter) throws Exception {
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].fisrtName").value(hasItem(DEFAULT_FISRT_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].nationalCode").value(hasItem(DEFAULT_NATIONAL_CODE)));

        // Check, that the count call also returns 1
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonShouldNotBeFound(String filter) throws Exception {
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPerson() throws Exception {
        // Get the person
        restPersonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person
        PersonEntity updatedPersonEntity = personRepository.findById(personEntity.getId()).get();
        // Disconnect from session so that the updates on updatedPersonEntity are not directly saved in db
        em.detach(updatedPersonEntity);
        updatedPersonEntity
            .fisrtName(UPDATED_FISRT_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .nationalCode(UPDATED_NATIONAL_CODE);
        PersonDTO personDTO = personMapper.toDto(updatedPersonEntity);

        restPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personDTO))
            )
            .andExpect(status().isOk());

        // Validate the Person in the database
        List<PersonEntity> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
        PersonEntity testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getFisrtName()).isEqualTo(UPDATED_FISRT_NAME);
        assertThat(testPerson.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPerson.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testPerson.getNationalCode()).isEqualTo(UPDATED_NATIONAL_CODE);
    }

    @Test
    @Transactional
    void putNonExistingPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        personEntity.setId(count.incrementAndGet());

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(personEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<PersonEntity> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        personEntity.setId(count.incrementAndGet());

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(personEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<PersonEntity> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        personEntity.setId(count.incrementAndGet());

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(personEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Person in the database
        List<PersonEntity> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonWithPatch() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person using partial update
        PersonEntity partialUpdatedPersonEntity = new PersonEntity();
        partialUpdatedPersonEntity.setId(personEntity.getId());

        partialUpdatedPersonEntity.lastName(UPDATED_LAST_NAME).nationalCode(UPDATED_NATIONAL_CODE);

        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonEntity))
            )
            .andExpect(status().isOk());

        // Validate the Person in the database
        List<PersonEntity> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
        PersonEntity testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getFisrtName()).isEqualTo(DEFAULT_FISRT_NAME);
        assertThat(testPerson.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPerson.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testPerson.getNationalCode()).isEqualTo(UPDATED_NATIONAL_CODE);
    }

    @Test
    @Transactional
    void fullUpdatePersonWithPatch() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person using partial update
        PersonEntity partialUpdatedPersonEntity = new PersonEntity();
        partialUpdatedPersonEntity.setId(personEntity.getId());

        partialUpdatedPersonEntity
            .fisrtName(UPDATED_FISRT_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .nationalCode(UPDATED_NATIONAL_CODE);

        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonEntity))
            )
            .andExpect(status().isOk());

        // Validate the Person in the database
        List<PersonEntity> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
        PersonEntity testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getFisrtName()).isEqualTo(UPDATED_FISRT_NAME);
        assertThat(testPerson.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPerson.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testPerson.getNationalCode()).isEqualTo(UPDATED_NATIONAL_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        personEntity.setId(count.incrementAndGet());

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(personEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<PersonEntity> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        personEntity.setId(count.incrementAndGet());

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(personEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<PersonEntity> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        personEntity.setId(count.incrementAndGet());

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(personEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Person in the database
        List<PersonEntity> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(personEntity);

        int databaseSizeBeforeDelete = personRepository.findAll().size();

        // Delete the person
        restPersonMockMvc
            .perform(delete(ENTITY_API_URL_ID, personEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonEntity> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
