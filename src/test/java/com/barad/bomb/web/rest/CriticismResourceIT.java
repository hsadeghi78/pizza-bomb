package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.CriticismEntity;
import com.barad.bomb.domain.PartyEntity;
import com.barad.bomb.repository.CriticismRepository;
import com.barad.bomb.service.criteria.CriticismCriteria;
import com.barad.bomb.service.dto.CriticismDTO;
import com.barad.bomb.service.mapper.CriticismMapper;
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
 * Integration tests for the {@link CriticismResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CriticismResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/criticisms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CriticismRepository criticismRepository;

    @Autowired
    private CriticismMapper criticismMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCriticismMockMvc;

    private CriticismEntity criticismEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CriticismEntity createEntity(EntityManager em) {
        CriticismEntity criticismEntity = new CriticismEntity()
            .fullName(DEFAULT_FULL_NAME)
            .email(DEFAULT_EMAIL)
            .contactNumber(DEFAULT_CONTACT_NUMBER)
            .description(DEFAULT_DESCRIPTION);
        return criticismEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CriticismEntity createUpdatedEntity(EntityManager em) {
        CriticismEntity criticismEntity = new CriticismEntity()
            .fullName(UPDATED_FULL_NAME)
            .email(UPDATED_EMAIL)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .description(UPDATED_DESCRIPTION);
        return criticismEntity;
    }

    @BeforeEach
    public void initTest() {
        criticismEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createCriticism() throws Exception {
        int databaseSizeBeforeCreate = criticismRepository.findAll().size();
        // Create the Criticism
        CriticismDTO criticismDTO = criticismMapper.toDto(criticismEntity);
        restCriticismMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(criticismDTO)))
            .andExpect(status().isCreated());

        // Validate the Criticism in the database
        List<CriticismEntity> criticismList = criticismRepository.findAll();
        assertThat(criticismList).hasSize(databaseSizeBeforeCreate + 1);
        CriticismEntity testCriticism = criticismList.get(criticismList.size() - 1);
        assertThat(testCriticism.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testCriticism.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCriticism.getContactNumber()).isEqualTo(DEFAULT_CONTACT_NUMBER);
        assertThat(testCriticism.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createCriticismWithExistingId() throws Exception {
        // Create the Criticism with an existing ID
        criticismEntity.setId(1L);
        CriticismDTO criticismDTO = criticismMapper.toDto(criticismEntity);

        int databaseSizeBeforeCreate = criticismRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCriticismMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(criticismDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Criticism in the database
        List<CriticismEntity> criticismList = criticismRepository.findAll();
        assertThat(criticismList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = criticismRepository.findAll().size();
        // set the field null
        criticismEntity.setFullName(null);

        // Create the Criticism, which fails.
        CriticismDTO criticismDTO = criticismMapper.toDto(criticismEntity);

        restCriticismMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(criticismDTO)))
            .andExpect(status().isBadRequest());

        List<CriticismEntity> criticismList = criticismRepository.findAll();
        assertThat(criticismList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = criticismRepository.findAll().size();
        // set the field null
        criticismEntity.setDescription(null);

        // Create the Criticism, which fails.
        CriticismDTO criticismDTO = criticismMapper.toDto(criticismEntity);

        restCriticismMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(criticismDTO)))
            .andExpect(status().isBadRequest());

        List<CriticismEntity> criticismList = criticismRepository.findAll();
        assertThat(criticismList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCriticisms() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList
        restCriticismMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(criticismEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCriticism() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get the criticism
        restCriticismMockMvc
            .perform(get(ENTITY_API_URL_ID, criticismEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(criticismEntity.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.contactNumber").value(DEFAULT_CONTACT_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCriticismsByIdFiltering() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        Long id = criticismEntity.getId();

        defaultCriticismShouldBeFound("id.equals=" + id);
        defaultCriticismShouldNotBeFound("id.notEquals=" + id);

        defaultCriticismShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCriticismShouldNotBeFound("id.greaterThan=" + id);

        defaultCriticismShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCriticismShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCriticismsByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where fullName equals to DEFAULT_FULL_NAME
        defaultCriticismShouldBeFound("fullName.equals=" + DEFAULT_FULL_NAME);

        // Get all the criticismList where fullName equals to UPDATED_FULL_NAME
        defaultCriticismShouldNotBeFound("fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllCriticismsByFullNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where fullName not equals to DEFAULT_FULL_NAME
        defaultCriticismShouldNotBeFound("fullName.notEquals=" + DEFAULT_FULL_NAME);

        // Get all the criticismList where fullName not equals to UPDATED_FULL_NAME
        defaultCriticismShouldBeFound("fullName.notEquals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllCriticismsByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where fullName in DEFAULT_FULL_NAME or UPDATED_FULL_NAME
        defaultCriticismShouldBeFound("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME);

        // Get all the criticismList where fullName equals to UPDATED_FULL_NAME
        defaultCriticismShouldNotBeFound("fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllCriticismsByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where fullName is not null
        defaultCriticismShouldBeFound("fullName.specified=true");

        // Get all the criticismList where fullName is null
        defaultCriticismShouldNotBeFound("fullName.specified=false");
    }

    @Test
    @Transactional
    void getAllCriticismsByFullNameContainsSomething() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where fullName contains DEFAULT_FULL_NAME
        defaultCriticismShouldBeFound("fullName.contains=" + DEFAULT_FULL_NAME);

        // Get all the criticismList where fullName contains UPDATED_FULL_NAME
        defaultCriticismShouldNotBeFound("fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllCriticismsByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where fullName does not contain DEFAULT_FULL_NAME
        defaultCriticismShouldNotBeFound("fullName.doesNotContain=" + DEFAULT_FULL_NAME);

        // Get all the criticismList where fullName does not contain UPDATED_FULL_NAME
        defaultCriticismShouldBeFound("fullName.doesNotContain=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllCriticismsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where email equals to DEFAULT_EMAIL
        defaultCriticismShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the criticismList where email equals to UPDATED_EMAIL
        defaultCriticismShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCriticismsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where email not equals to DEFAULT_EMAIL
        defaultCriticismShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the criticismList where email not equals to UPDATED_EMAIL
        defaultCriticismShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCriticismsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultCriticismShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the criticismList where email equals to UPDATED_EMAIL
        defaultCriticismShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCriticismsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where email is not null
        defaultCriticismShouldBeFound("email.specified=true");

        // Get all the criticismList where email is null
        defaultCriticismShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllCriticismsByEmailContainsSomething() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where email contains DEFAULT_EMAIL
        defaultCriticismShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the criticismList where email contains UPDATED_EMAIL
        defaultCriticismShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCriticismsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where email does not contain DEFAULT_EMAIL
        defaultCriticismShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the criticismList where email does not contain UPDATED_EMAIL
        defaultCriticismShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCriticismsByContactNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where contactNumber equals to DEFAULT_CONTACT_NUMBER
        defaultCriticismShouldBeFound("contactNumber.equals=" + DEFAULT_CONTACT_NUMBER);

        // Get all the criticismList where contactNumber equals to UPDATED_CONTACT_NUMBER
        defaultCriticismShouldNotBeFound("contactNumber.equals=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllCriticismsByContactNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where contactNumber not equals to DEFAULT_CONTACT_NUMBER
        defaultCriticismShouldNotBeFound("contactNumber.notEquals=" + DEFAULT_CONTACT_NUMBER);

        // Get all the criticismList where contactNumber not equals to UPDATED_CONTACT_NUMBER
        defaultCriticismShouldBeFound("contactNumber.notEquals=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllCriticismsByContactNumberIsInShouldWork() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where contactNumber in DEFAULT_CONTACT_NUMBER or UPDATED_CONTACT_NUMBER
        defaultCriticismShouldBeFound("contactNumber.in=" + DEFAULT_CONTACT_NUMBER + "," + UPDATED_CONTACT_NUMBER);

        // Get all the criticismList where contactNumber equals to UPDATED_CONTACT_NUMBER
        defaultCriticismShouldNotBeFound("contactNumber.in=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllCriticismsByContactNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where contactNumber is not null
        defaultCriticismShouldBeFound("contactNumber.specified=true");

        // Get all the criticismList where contactNumber is null
        defaultCriticismShouldNotBeFound("contactNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCriticismsByContactNumberContainsSomething() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where contactNumber contains DEFAULT_CONTACT_NUMBER
        defaultCriticismShouldBeFound("contactNumber.contains=" + DEFAULT_CONTACT_NUMBER);

        // Get all the criticismList where contactNumber contains UPDATED_CONTACT_NUMBER
        defaultCriticismShouldNotBeFound("contactNumber.contains=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllCriticismsByContactNumberNotContainsSomething() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where contactNumber does not contain DEFAULT_CONTACT_NUMBER
        defaultCriticismShouldNotBeFound("contactNumber.doesNotContain=" + DEFAULT_CONTACT_NUMBER);

        // Get all the criticismList where contactNumber does not contain UPDATED_CONTACT_NUMBER
        defaultCriticismShouldBeFound("contactNumber.doesNotContain=" + UPDATED_CONTACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllCriticismsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where description equals to DEFAULT_DESCRIPTION
        defaultCriticismShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the criticismList where description equals to UPDATED_DESCRIPTION
        defaultCriticismShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCriticismsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where description not equals to DEFAULT_DESCRIPTION
        defaultCriticismShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the criticismList where description not equals to UPDATED_DESCRIPTION
        defaultCriticismShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCriticismsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCriticismShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the criticismList where description equals to UPDATED_DESCRIPTION
        defaultCriticismShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCriticismsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where description is not null
        defaultCriticismShouldBeFound("description.specified=true");

        // Get all the criticismList where description is null
        defaultCriticismShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCriticismsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where description contains DEFAULT_DESCRIPTION
        defaultCriticismShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the criticismList where description contains UPDATED_DESCRIPTION
        defaultCriticismShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCriticismsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        // Get all the criticismList where description does not contain DEFAULT_DESCRIPTION
        defaultCriticismShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the criticismList where description does not contain UPDATED_DESCRIPTION
        defaultCriticismShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCriticismsByPartyIsEqualToSomething() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);
        PartyEntity party = PartyResourceIT.createEntity(em);
        em.persist(party);
        em.flush();
        criticismEntity.setParty(party);
        criticismRepository.saveAndFlush(criticismEntity);
        Long partyId = party.getId();

        // Get all the criticismList where party equals to partyId
        defaultCriticismShouldBeFound("partyId.equals=" + partyId);

        // Get all the criticismList where party equals to (partyId + 1)
        defaultCriticismShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCriticismShouldBeFound(String filter) throws Exception {
        restCriticismMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(criticismEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCriticismMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCriticismShouldNotBeFound(String filter) throws Exception {
        restCriticismMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCriticismMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCriticism() throws Exception {
        // Get the criticism
        restCriticismMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCriticism() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        int databaseSizeBeforeUpdate = criticismRepository.findAll().size();

        // Update the criticism
        CriticismEntity updatedCriticismEntity = criticismRepository.findById(criticismEntity.getId()).get();
        // Disconnect from session so that the updates on updatedCriticismEntity are not directly saved in db
        em.detach(updatedCriticismEntity);
        updatedCriticismEntity
            .fullName(UPDATED_FULL_NAME)
            .email(UPDATED_EMAIL)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .description(UPDATED_DESCRIPTION);
        CriticismDTO criticismDTO = criticismMapper.toDto(updatedCriticismEntity);

        restCriticismMockMvc
            .perform(
                put(ENTITY_API_URL_ID, criticismDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(criticismDTO))
            )
            .andExpect(status().isOk());

        // Validate the Criticism in the database
        List<CriticismEntity> criticismList = criticismRepository.findAll();
        assertThat(criticismList).hasSize(databaseSizeBeforeUpdate);
        CriticismEntity testCriticism = criticismList.get(criticismList.size() - 1);
        assertThat(testCriticism.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testCriticism.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCriticism.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
        assertThat(testCriticism.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingCriticism() throws Exception {
        int databaseSizeBeforeUpdate = criticismRepository.findAll().size();
        criticismEntity.setId(count.incrementAndGet());

        // Create the Criticism
        CriticismDTO criticismDTO = criticismMapper.toDto(criticismEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCriticismMockMvc
            .perform(
                put(ENTITY_API_URL_ID, criticismDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(criticismDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Criticism in the database
        List<CriticismEntity> criticismList = criticismRepository.findAll();
        assertThat(criticismList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCriticism() throws Exception {
        int databaseSizeBeforeUpdate = criticismRepository.findAll().size();
        criticismEntity.setId(count.incrementAndGet());

        // Create the Criticism
        CriticismDTO criticismDTO = criticismMapper.toDto(criticismEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCriticismMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(criticismDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Criticism in the database
        List<CriticismEntity> criticismList = criticismRepository.findAll();
        assertThat(criticismList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCriticism() throws Exception {
        int databaseSizeBeforeUpdate = criticismRepository.findAll().size();
        criticismEntity.setId(count.incrementAndGet());

        // Create the Criticism
        CriticismDTO criticismDTO = criticismMapper.toDto(criticismEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCriticismMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(criticismDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Criticism in the database
        List<CriticismEntity> criticismList = criticismRepository.findAll();
        assertThat(criticismList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCriticismWithPatch() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        int databaseSizeBeforeUpdate = criticismRepository.findAll().size();

        // Update the criticism using partial update
        CriticismEntity partialUpdatedCriticismEntity = new CriticismEntity();
        partialUpdatedCriticismEntity.setId(criticismEntity.getId());

        restCriticismMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCriticismEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCriticismEntity))
            )
            .andExpect(status().isOk());

        // Validate the Criticism in the database
        List<CriticismEntity> criticismList = criticismRepository.findAll();
        assertThat(criticismList).hasSize(databaseSizeBeforeUpdate);
        CriticismEntity testCriticism = criticismList.get(criticismList.size() - 1);
        assertThat(testCriticism.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testCriticism.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCriticism.getContactNumber()).isEqualTo(DEFAULT_CONTACT_NUMBER);
        assertThat(testCriticism.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCriticismWithPatch() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        int databaseSizeBeforeUpdate = criticismRepository.findAll().size();

        // Update the criticism using partial update
        CriticismEntity partialUpdatedCriticismEntity = new CriticismEntity();
        partialUpdatedCriticismEntity.setId(criticismEntity.getId());

        partialUpdatedCriticismEntity
            .fullName(UPDATED_FULL_NAME)
            .email(UPDATED_EMAIL)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .description(UPDATED_DESCRIPTION);

        restCriticismMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCriticismEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCriticismEntity))
            )
            .andExpect(status().isOk());

        // Validate the Criticism in the database
        List<CriticismEntity> criticismList = criticismRepository.findAll();
        assertThat(criticismList).hasSize(databaseSizeBeforeUpdate);
        CriticismEntity testCriticism = criticismList.get(criticismList.size() - 1);
        assertThat(testCriticism.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testCriticism.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCriticism.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
        assertThat(testCriticism.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCriticism() throws Exception {
        int databaseSizeBeforeUpdate = criticismRepository.findAll().size();
        criticismEntity.setId(count.incrementAndGet());

        // Create the Criticism
        CriticismDTO criticismDTO = criticismMapper.toDto(criticismEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCriticismMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, criticismDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(criticismDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Criticism in the database
        List<CriticismEntity> criticismList = criticismRepository.findAll();
        assertThat(criticismList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCriticism() throws Exception {
        int databaseSizeBeforeUpdate = criticismRepository.findAll().size();
        criticismEntity.setId(count.incrementAndGet());

        // Create the Criticism
        CriticismDTO criticismDTO = criticismMapper.toDto(criticismEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCriticismMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(criticismDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Criticism in the database
        List<CriticismEntity> criticismList = criticismRepository.findAll();
        assertThat(criticismList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCriticism() throws Exception {
        int databaseSizeBeforeUpdate = criticismRepository.findAll().size();
        criticismEntity.setId(count.incrementAndGet());

        // Create the Criticism
        CriticismDTO criticismDTO = criticismMapper.toDto(criticismEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCriticismMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(criticismDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Criticism in the database
        List<CriticismEntity> criticismList = criticismRepository.findAll();
        assertThat(criticismList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCriticism() throws Exception {
        // Initialize the database
        criticismRepository.saveAndFlush(criticismEntity);

        int databaseSizeBeforeDelete = criticismRepository.findAll().size();

        // Delete the criticism
        restCriticismMockMvc
            .perform(delete(ENTITY_API_URL_ID, criticismEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CriticismEntity> criticismList = criticismRepository.findAll();
        assertThat(criticismList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
