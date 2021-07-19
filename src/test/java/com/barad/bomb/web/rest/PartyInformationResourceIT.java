package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.PartyEntity;
import com.barad.bomb.domain.PartyInformationEntity;
import com.barad.bomb.domain.enumeration.PartyInfoType;
import com.barad.bomb.repository.PartyInformationRepository;
import com.barad.bomb.service.criteria.PartyInformationCriteria;
import com.barad.bomb.service.dto.PartyInformationDTO;
import com.barad.bomb.service.mapper.PartyInformationMapper;
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
 * Integration tests for the {@link PartyInformationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PartyInformationResourceIT {

    private static final PartyInfoType DEFAULT_INFO_TYPE = PartyInfoType.WORK_TIME;
    private static final PartyInfoType UPDATED_INFO_TYPE = PartyInfoType.PRIVATE;

    private static final String DEFAULT_INFO_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_INFO_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_INFO_DESC = "AAAAAAAAAA";
    private static final String UPDATED_INFO_DESC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/party-informations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PartyInformationRepository partyInformationRepository;

    @Autowired
    private PartyInformationMapper partyInformationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartyInformationMockMvc;

    private PartyInformationEntity partyInformationEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyInformationEntity createEntity(EntityManager em) {
        PartyInformationEntity partyInformationEntity = new PartyInformationEntity()
            .infoType(DEFAULT_INFO_TYPE)
            .infoTitle(DEFAULT_INFO_TITLE)
            .infoDesc(DEFAULT_INFO_DESC);
        // Add required entity
        PartyEntity party;
        if (TestUtil.findAll(em, PartyEntity.class).isEmpty()) {
            party = PartyResourceIT.createEntity(em);
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, PartyEntity.class).get(0);
        }
        partyInformationEntity.setParty(party);
        return partyInformationEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyInformationEntity createUpdatedEntity(EntityManager em) {
        PartyInformationEntity partyInformationEntity = new PartyInformationEntity()
            .infoType(UPDATED_INFO_TYPE)
            .infoTitle(UPDATED_INFO_TITLE)
            .infoDesc(UPDATED_INFO_DESC);
        // Add required entity
        PartyEntity party;
        if (TestUtil.findAll(em, PartyEntity.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity(em);
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, PartyEntity.class).get(0);
        }
        partyInformationEntity.setParty(party);
        return partyInformationEntity;
    }

    @BeforeEach
    public void initTest() {
        partyInformationEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createPartyInformation() throws Exception {
        int databaseSizeBeforeCreate = partyInformationRepository.findAll().size();
        // Create the PartyInformation
        PartyInformationDTO partyInformationDTO = partyInformationMapper.toDto(partyInformationEntity);
        restPartyInformationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partyInformationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PartyInformation in the database
        List<PartyInformationEntity> partyInformationList = partyInformationRepository.findAll();
        assertThat(partyInformationList).hasSize(databaseSizeBeforeCreate + 1);
        PartyInformationEntity testPartyInformation = partyInformationList.get(partyInformationList.size() - 1);
        assertThat(testPartyInformation.getInfoType()).isEqualTo(DEFAULT_INFO_TYPE);
        assertThat(testPartyInformation.getInfoTitle()).isEqualTo(DEFAULT_INFO_TITLE);
        assertThat(testPartyInformation.getInfoDesc()).isEqualTo(DEFAULT_INFO_DESC);
    }

    @Test
    @Transactional
    void createPartyInformationWithExistingId() throws Exception {
        // Create the PartyInformation with an existing ID
        partyInformationEntity.setId(1L);
        PartyInformationDTO partyInformationDTO = partyInformationMapper.toDto(partyInformationEntity);

        int databaseSizeBeforeCreate = partyInformationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartyInformationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partyInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyInformation in the database
        List<PartyInformationEntity> partyInformationList = partyInformationRepository.findAll();
        assertThat(partyInformationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInfoTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyInformationRepository.findAll().size();
        // set the field null
        partyInformationEntity.setInfoType(null);

        // Create the PartyInformation, which fails.
        PartyInformationDTO partyInformationDTO = partyInformationMapper.toDto(partyInformationEntity);

        restPartyInformationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partyInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<PartyInformationEntity> partyInformationList = partyInformationRepository.findAll();
        assertThat(partyInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInfoTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyInformationRepository.findAll().size();
        // set the field null
        partyInformationEntity.setInfoTitle(null);

        // Create the PartyInformation, which fails.
        PartyInformationDTO partyInformationDTO = partyInformationMapper.toDto(partyInformationEntity);

        restPartyInformationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partyInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<PartyInformationEntity> partyInformationList = partyInformationRepository.findAll();
        assertThat(partyInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPartyInformations() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        // Get all the partyInformationList
        restPartyInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyInformationEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].infoType").value(hasItem(DEFAULT_INFO_TYPE.toString())))
            .andExpect(jsonPath("$.[*].infoTitle").value(hasItem(DEFAULT_INFO_TITLE)))
            .andExpect(jsonPath("$.[*].infoDesc").value(hasItem(DEFAULT_INFO_DESC)));
    }

    @Test
    @Transactional
    void getPartyInformation() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        // Get the partyInformation
        restPartyInformationMockMvc
            .perform(get(ENTITY_API_URL_ID, partyInformationEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partyInformationEntity.getId().intValue()))
            .andExpect(jsonPath("$.infoType").value(DEFAULT_INFO_TYPE.toString()))
            .andExpect(jsonPath("$.infoTitle").value(DEFAULT_INFO_TITLE))
            .andExpect(jsonPath("$.infoDesc").value(DEFAULT_INFO_DESC));
    }

    @Test
    @Transactional
    void getPartyInformationsByIdFiltering() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        Long id = partyInformationEntity.getId();

        defaultPartyInformationShouldBeFound("id.equals=" + id);
        defaultPartyInformationShouldNotBeFound("id.notEquals=" + id);

        defaultPartyInformationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPartyInformationShouldNotBeFound("id.greaterThan=" + id);

        defaultPartyInformationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPartyInformationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPartyInformationsByInfoTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        // Get all the partyInformationList where infoType equals to DEFAULT_INFO_TYPE
        defaultPartyInformationShouldBeFound("infoType.equals=" + DEFAULT_INFO_TYPE);

        // Get all the partyInformationList where infoType equals to UPDATED_INFO_TYPE
        defaultPartyInformationShouldNotBeFound("infoType.equals=" + UPDATED_INFO_TYPE);
    }

    @Test
    @Transactional
    void getAllPartyInformationsByInfoTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        // Get all the partyInformationList where infoType not equals to DEFAULT_INFO_TYPE
        defaultPartyInformationShouldNotBeFound("infoType.notEquals=" + DEFAULT_INFO_TYPE);

        // Get all the partyInformationList where infoType not equals to UPDATED_INFO_TYPE
        defaultPartyInformationShouldBeFound("infoType.notEquals=" + UPDATED_INFO_TYPE);
    }

    @Test
    @Transactional
    void getAllPartyInformationsByInfoTypeIsInShouldWork() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        // Get all the partyInformationList where infoType in DEFAULT_INFO_TYPE or UPDATED_INFO_TYPE
        defaultPartyInformationShouldBeFound("infoType.in=" + DEFAULT_INFO_TYPE + "," + UPDATED_INFO_TYPE);

        // Get all the partyInformationList where infoType equals to UPDATED_INFO_TYPE
        defaultPartyInformationShouldNotBeFound("infoType.in=" + UPDATED_INFO_TYPE);
    }

    @Test
    @Transactional
    void getAllPartyInformationsByInfoTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        // Get all the partyInformationList where infoType is not null
        defaultPartyInformationShouldBeFound("infoType.specified=true");

        // Get all the partyInformationList where infoType is null
        defaultPartyInformationShouldNotBeFound("infoType.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyInformationsByInfoTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        // Get all the partyInformationList where infoTitle equals to DEFAULT_INFO_TITLE
        defaultPartyInformationShouldBeFound("infoTitle.equals=" + DEFAULT_INFO_TITLE);

        // Get all the partyInformationList where infoTitle equals to UPDATED_INFO_TITLE
        defaultPartyInformationShouldNotBeFound("infoTitle.equals=" + UPDATED_INFO_TITLE);
    }

    @Test
    @Transactional
    void getAllPartyInformationsByInfoTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        // Get all the partyInformationList where infoTitle not equals to DEFAULT_INFO_TITLE
        defaultPartyInformationShouldNotBeFound("infoTitle.notEquals=" + DEFAULT_INFO_TITLE);

        // Get all the partyInformationList where infoTitle not equals to UPDATED_INFO_TITLE
        defaultPartyInformationShouldBeFound("infoTitle.notEquals=" + UPDATED_INFO_TITLE);
    }

    @Test
    @Transactional
    void getAllPartyInformationsByInfoTitleIsInShouldWork() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        // Get all the partyInformationList where infoTitle in DEFAULT_INFO_TITLE or UPDATED_INFO_TITLE
        defaultPartyInformationShouldBeFound("infoTitle.in=" + DEFAULT_INFO_TITLE + "," + UPDATED_INFO_TITLE);

        // Get all the partyInformationList where infoTitle equals to UPDATED_INFO_TITLE
        defaultPartyInformationShouldNotBeFound("infoTitle.in=" + UPDATED_INFO_TITLE);
    }

    @Test
    @Transactional
    void getAllPartyInformationsByInfoTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        // Get all the partyInformationList where infoTitle is not null
        defaultPartyInformationShouldBeFound("infoTitle.specified=true");

        // Get all the partyInformationList where infoTitle is null
        defaultPartyInformationShouldNotBeFound("infoTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyInformationsByInfoTitleContainsSomething() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        // Get all the partyInformationList where infoTitle contains DEFAULT_INFO_TITLE
        defaultPartyInformationShouldBeFound("infoTitle.contains=" + DEFAULT_INFO_TITLE);

        // Get all the partyInformationList where infoTitle contains UPDATED_INFO_TITLE
        defaultPartyInformationShouldNotBeFound("infoTitle.contains=" + UPDATED_INFO_TITLE);
    }

    @Test
    @Transactional
    void getAllPartyInformationsByInfoTitleNotContainsSomething() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        // Get all the partyInformationList where infoTitle does not contain DEFAULT_INFO_TITLE
        defaultPartyInformationShouldNotBeFound("infoTitle.doesNotContain=" + DEFAULT_INFO_TITLE);

        // Get all the partyInformationList where infoTitle does not contain UPDATED_INFO_TITLE
        defaultPartyInformationShouldBeFound("infoTitle.doesNotContain=" + UPDATED_INFO_TITLE);
    }

    @Test
    @Transactional
    void getAllPartyInformationsByInfoDescIsEqualToSomething() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        // Get all the partyInformationList where infoDesc equals to DEFAULT_INFO_DESC
        defaultPartyInformationShouldBeFound("infoDesc.equals=" + DEFAULT_INFO_DESC);

        // Get all the partyInformationList where infoDesc equals to UPDATED_INFO_DESC
        defaultPartyInformationShouldNotBeFound("infoDesc.equals=" + UPDATED_INFO_DESC);
    }

    @Test
    @Transactional
    void getAllPartyInformationsByInfoDescIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        // Get all the partyInformationList where infoDesc not equals to DEFAULT_INFO_DESC
        defaultPartyInformationShouldNotBeFound("infoDesc.notEquals=" + DEFAULT_INFO_DESC);

        // Get all the partyInformationList where infoDesc not equals to UPDATED_INFO_DESC
        defaultPartyInformationShouldBeFound("infoDesc.notEquals=" + UPDATED_INFO_DESC);
    }

    @Test
    @Transactional
    void getAllPartyInformationsByInfoDescIsInShouldWork() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        // Get all the partyInformationList where infoDesc in DEFAULT_INFO_DESC or UPDATED_INFO_DESC
        defaultPartyInformationShouldBeFound("infoDesc.in=" + DEFAULT_INFO_DESC + "," + UPDATED_INFO_DESC);

        // Get all the partyInformationList where infoDesc equals to UPDATED_INFO_DESC
        defaultPartyInformationShouldNotBeFound("infoDesc.in=" + UPDATED_INFO_DESC);
    }

    @Test
    @Transactional
    void getAllPartyInformationsByInfoDescIsNullOrNotNull() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        // Get all the partyInformationList where infoDesc is not null
        defaultPartyInformationShouldBeFound("infoDesc.specified=true");

        // Get all the partyInformationList where infoDesc is null
        defaultPartyInformationShouldNotBeFound("infoDesc.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyInformationsByInfoDescContainsSomething() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        // Get all the partyInformationList where infoDesc contains DEFAULT_INFO_DESC
        defaultPartyInformationShouldBeFound("infoDesc.contains=" + DEFAULT_INFO_DESC);

        // Get all the partyInformationList where infoDesc contains UPDATED_INFO_DESC
        defaultPartyInformationShouldNotBeFound("infoDesc.contains=" + UPDATED_INFO_DESC);
    }

    @Test
    @Transactional
    void getAllPartyInformationsByInfoDescNotContainsSomething() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        // Get all the partyInformationList where infoDesc does not contain DEFAULT_INFO_DESC
        defaultPartyInformationShouldNotBeFound("infoDesc.doesNotContain=" + DEFAULT_INFO_DESC);

        // Get all the partyInformationList where infoDesc does not contain UPDATED_INFO_DESC
        defaultPartyInformationShouldBeFound("infoDesc.doesNotContain=" + UPDATED_INFO_DESC);
    }

    @Test
    @Transactional
    void getAllPartyInformationsByPartyIsEqualToSomething() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);
        PartyEntity party = PartyResourceIT.createEntity(em);
        em.persist(party);
        em.flush();
        partyInformationEntity.setParty(party);
        partyInformationRepository.saveAndFlush(partyInformationEntity);
        Long partyId = party.getId();

        // Get all the partyInformationList where party equals to partyId
        defaultPartyInformationShouldBeFound("partyId.equals=" + partyId);

        // Get all the partyInformationList where party equals to (partyId + 1)
        defaultPartyInformationShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartyInformationShouldBeFound(String filter) throws Exception {
        restPartyInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyInformationEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].infoType").value(hasItem(DEFAULT_INFO_TYPE.toString())))
            .andExpect(jsonPath("$.[*].infoTitle").value(hasItem(DEFAULT_INFO_TITLE)))
            .andExpect(jsonPath("$.[*].infoDesc").value(hasItem(DEFAULT_INFO_DESC)));

        // Check, that the count call also returns 1
        restPartyInformationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPartyInformationShouldNotBeFound(String filter) throws Exception {
        restPartyInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartyInformationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPartyInformation() throws Exception {
        // Get the partyInformation
        restPartyInformationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPartyInformation() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        int databaseSizeBeforeUpdate = partyInformationRepository.findAll().size();

        // Update the partyInformation
        PartyInformationEntity updatedPartyInformationEntity = partyInformationRepository.findById(partyInformationEntity.getId()).get();
        // Disconnect from session so that the updates on updatedPartyInformationEntity are not directly saved in db
        em.detach(updatedPartyInformationEntity);
        updatedPartyInformationEntity.infoType(UPDATED_INFO_TYPE).infoTitle(UPDATED_INFO_TITLE).infoDesc(UPDATED_INFO_DESC);
        PartyInformationDTO partyInformationDTO = partyInformationMapper.toDto(updatedPartyInformationEntity);

        restPartyInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partyInformationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partyInformationDTO))
            )
            .andExpect(status().isOk());

        // Validate the PartyInformation in the database
        List<PartyInformationEntity> partyInformationList = partyInformationRepository.findAll();
        assertThat(partyInformationList).hasSize(databaseSizeBeforeUpdate);
        PartyInformationEntity testPartyInformation = partyInformationList.get(partyInformationList.size() - 1);
        assertThat(testPartyInformation.getInfoType()).isEqualTo(UPDATED_INFO_TYPE);
        assertThat(testPartyInformation.getInfoTitle()).isEqualTo(UPDATED_INFO_TITLE);
        assertThat(testPartyInformation.getInfoDesc()).isEqualTo(UPDATED_INFO_DESC);
    }

    @Test
    @Transactional
    void putNonExistingPartyInformation() throws Exception {
        int databaseSizeBeforeUpdate = partyInformationRepository.findAll().size();
        partyInformationEntity.setId(count.incrementAndGet());

        // Create the PartyInformation
        PartyInformationDTO partyInformationDTO = partyInformationMapper.toDto(partyInformationEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partyInformationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partyInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyInformation in the database
        List<PartyInformationEntity> partyInformationList = partyInformationRepository.findAll();
        assertThat(partyInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartyInformation() throws Exception {
        int databaseSizeBeforeUpdate = partyInformationRepository.findAll().size();
        partyInformationEntity.setId(count.incrementAndGet());

        // Create the PartyInformation
        PartyInformationDTO partyInformationDTO = partyInformationMapper.toDto(partyInformationEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partyInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyInformation in the database
        List<PartyInformationEntity> partyInformationList = partyInformationRepository.findAll();
        assertThat(partyInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartyInformation() throws Exception {
        int databaseSizeBeforeUpdate = partyInformationRepository.findAll().size();
        partyInformationEntity.setId(count.incrementAndGet());

        // Create the PartyInformation
        PartyInformationDTO partyInformationDTO = partyInformationMapper.toDto(partyInformationEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyInformationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partyInformationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyInformation in the database
        List<PartyInformationEntity> partyInformationList = partyInformationRepository.findAll();
        assertThat(partyInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartyInformationWithPatch() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        int databaseSizeBeforeUpdate = partyInformationRepository.findAll().size();

        // Update the partyInformation using partial update
        PartyInformationEntity partialUpdatedPartyInformationEntity = new PartyInformationEntity();
        partialUpdatedPartyInformationEntity.setId(partyInformationEntity.getId());

        partialUpdatedPartyInformationEntity.infoTitle(UPDATED_INFO_TITLE).infoDesc(UPDATED_INFO_DESC);

        restPartyInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyInformationEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartyInformationEntity))
            )
            .andExpect(status().isOk());

        // Validate the PartyInformation in the database
        List<PartyInformationEntity> partyInformationList = partyInformationRepository.findAll();
        assertThat(partyInformationList).hasSize(databaseSizeBeforeUpdate);
        PartyInformationEntity testPartyInformation = partyInformationList.get(partyInformationList.size() - 1);
        assertThat(testPartyInformation.getInfoType()).isEqualTo(DEFAULT_INFO_TYPE);
        assertThat(testPartyInformation.getInfoTitle()).isEqualTo(UPDATED_INFO_TITLE);
        assertThat(testPartyInformation.getInfoDesc()).isEqualTo(UPDATED_INFO_DESC);
    }

    @Test
    @Transactional
    void fullUpdatePartyInformationWithPatch() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        int databaseSizeBeforeUpdate = partyInformationRepository.findAll().size();

        // Update the partyInformation using partial update
        PartyInformationEntity partialUpdatedPartyInformationEntity = new PartyInformationEntity();
        partialUpdatedPartyInformationEntity.setId(partyInformationEntity.getId());

        partialUpdatedPartyInformationEntity.infoType(UPDATED_INFO_TYPE).infoTitle(UPDATED_INFO_TITLE).infoDesc(UPDATED_INFO_DESC);

        restPartyInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyInformationEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartyInformationEntity))
            )
            .andExpect(status().isOk());

        // Validate the PartyInformation in the database
        List<PartyInformationEntity> partyInformationList = partyInformationRepository.findAll();
        assertThat(partyInformationList).hasSize(databaseSizeBeforeUpdate);
        PartyInformationEntity testPartyInformation = partyInformationList.get(partyInformationList.size() - 1);
        assertThat(testPartyInformation.getInfoType()).isEqualTo(UPDATED_INFO_TYPE);
        assertThat(testPartyInformation.getInfoTitle()).isEqualTo(UPDATED_INFO_TITLE);
        assertThat(testPartyInformation.getInfoDesc()).isEqualTo(UPDATED_INFO_DESC);
    }

    @Test
    @Transactional
    void patchNonExistingPartyInformation() throws Exception {
        int databaseSizeBeforeUpdate = partyInformationRepository.findAll().size();
        partyInformationEntity.setId(count.incrementAndGet());

        // Create the PartyInformation
        PartyInformationDTO partyInformationDTO = partyInformationMapper.toDto(partyInformationEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partyInformationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partyInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyInformation in the database
        List<PartyInformationEntity> partyInformationList = partyInformationRepository.findAll();
        assertThat(partyInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartyInformation() throws Exception {
        int databaseSizeBeforeUpdate = partyInformationRepository.findAll().size();
        partyInformationEntity.setId(count.incrementAndGet());

        // Create the PartyInformation
        PartyInformationDTO partyInformationDTO = partyInformationMapper.toDto(partyInformationEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partyInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyInformation in the database
        List<PartyInformationEntity> partyInformationList = partyInformationRepository.findAll();
        assertThat(partyInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartyInformation() throws Exception {
        int databaseSizeBeforeUpdate = partyInformationRepository.findAll().size();
        partyInformationEntity.setId(count.incrementAndGet());

        // Create the PartyInformation
        PartyInformationDTO partyInformationDTO = partyInformationMapper.toDto(partyInformationEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyInformationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partyInformationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyInformation in the database
        List<PartyInformationEntity> partyInformationList = partyInformationRepository.findAll();
        assertThat(partyInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartyInformation() throws Exception {
        // Initialize the database
        partyInformationRepository.saveAndFlush(partyInformationEntity);

        int databaseSizeBeforeDelete = partyInformationRepository.findAll().size();

        // Delete the partyInformation
        restPartyInformationMockMvc
            .perform(delete(ENTITY_API_URL_ID, partyInformationEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PartyInformationEntity> partyInformationList = partyInformationRepository.findAll();
        assertThat(partyInformationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
