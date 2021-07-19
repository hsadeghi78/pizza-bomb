package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.FoodEntity;
import com.barad.bomb.domain.FoodTypeEntity;
import com.barad.bomb.domain.PartyEntity;
import com.barad.bomb.repository.FoodTypeRepository;
import com.barad.bomb.service.criteria.FoodTypeCriteria;
import com.barad.bomb.service.dto.FoodTypeDTO;
import com.barad.bomb.service.mapper.FoodTypeMapper;
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
 * Integration tests for the {@link FoodTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FoodTypeResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/food-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FoodTypeRepository foodTypeRepository;

    @Autowired
    private FoodTypeMapper foodTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFoodTypeMockMvc;

    private FoodTypeEntity foodTypeEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodTypeEntity createEntity(EntityManager em) {
        FoodTypeEntity foodTypeEntity = new FoodTypeEntity()
            .title(DEFAULT_TITLE)
            .typeCode(DEFAULT_TYPE_CODE)
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
        foodTypeEntity.setParty(party);
        return foodTypeEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FoodTypeEntity createUpdatedEntity(EntityManager em) {
        FoodTypeEntity foodTypeEntity = new FoodTypeEntity()
            .title(UPDATED_TITLE)
            .typeCode(UPDATED_TYPE_CODE)
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
        foodTypeEntity.setParty(party);
        return foodTypeEntity;
    }

    @BeforeEach
    public void initTest() {
        foodTypeEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createFoodType() throws Exception {
        int databaseSizeBeforeCreate = foodTypeRepository.findAll().size();
        // Create the FoodType
        FoodTypeDTO foodTypeDTO = foodTypeMapper.toDto(foodTypeEntity);
        restFoodTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the FoodType in the database
        List<FoodTypeEntity> foodTypeList = foodTypeRepository.findAll();
        assertThat(foodTypeList).hasSize(databaseSizeBeforeCreate + 1);
        FoodTypeEntity testFoodType = foodTypeList.get(foodTypeList.size() - 1);
        assertThat(testFoodType.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFoodType.getTypeCode()).isEqualTo(DEFAULT_TYPE_CODE);
        assertThat(testFoodType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createFoodTypeWithExistingId() throws Exception {
        // Create the FoodType with an existing ID
        foodTypeEntity.setId(1L);
        FoodTypeDTO foodTypeDTO = foodTypeMapper.toDto(foodTypeEntity);

        int databaseSizeBeforeCreate = foodTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFoodTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FoodType in the database
        List<FoodTypeEntity> foodTypeList = foodTypeRepository.findAll();
        assertThat(foodTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = foodTypeRepository.findAll().size();
        // set the field null
        foodTypeEntity.setTitle(null);

        // Create the FoodType, which fails.
        FoodTypeDTO foodTypeDTO = foodTypeMapper.toDto(foodTypeEntity);

        restFoodTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FoodTypeEntity> foodTypeList = foodTypeRepository.findAll();
        assertThat(foodTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = foodTypeRepository.findAll().size();
        // set the field null
        foodTypeEntity.setTypeCode(null);

        // Create the FoodType, which fails.
        FoodTypeDTO foodTypeDTO = foodTypeMapper.toDto(foodTypeEntity);

        restFoodTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FoodTypeEntity> foodTypeList = foodTypeRepository.findAll();
        assertThat(foodTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFoodTypes() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get all the foodTypeList
        restFoodTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(foodTypeEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].typeCode").value(hasItem(DEFAULT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getFoodType() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get the foodType
        restFoodTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, foodTypeEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(foodTypeEntity.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.typeCode").value(DEFAULT_TYPE_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getFoodTypesByIdFiltering() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        Long id = foodTypeEntity.getId();

        defaultFoodTypeShouldBeFound("id.equals=" + id);
        defaultFoodTypeShouldNotBeFound("id.notEquals=" + id);

        defaultFoodTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFoodTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultFoodTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFoodTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFoodTypesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get all the foodTypeList where title equals to DEFAULT_TITLE
        defaultFoodTypeShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the foodTypeList where title equals to UPDATED_TITLE
        defaultFoodTypeShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFoodTypesByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get all the foodTypeList where title not equals to DEFAULT_TITLE
        defaultFoodTypeShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the foodTypeList where title not equals to UPDATED_TITLE
        defaultFoodTypeShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFoodTypesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get all the foodTypeList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultFoodTypeShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the foodTypeList where title equals to UPDATED_TITLE
        defaultFoodTypeShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFoodTypesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get all the foodTypeList where title is not null
        defaultFoodTypeShouldBeFound("title.specified=true");

        // Get all the foodTypeList where title is null
        defaultFoodTypeShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllFoodTypesByTitleContainsSomething() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get all the foodTypeList where title contains DEFAULT_TITLE
        defaultFoodTypeShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the foodTypeList where title contains UPDATED_TITLE
        defaultFoodTypeShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFoodTypesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get all the foodTypeList where title does not contain DEFAULT_TITLE
        defaultFoodTypeShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the foodTypeList where title does not contain UPDATED_TITLE
        defaultFoodTypeShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFoodTypesByTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get all the foodTypeList where typeCode equals to DEFAULT_TYPE_CODE
        defaultFoodTypeShouldBeFound("typeCode.equals=" + DEFAULT_TYPE_CODE);

        // Get all the foodTypeList where typeCode equals to UPDATED_TYPE_CODE
        defaultFoodTypeShouldNotBeFound("typeCode.equals=" + UPDATED_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFoodTypesByTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get all the foodTypeList where typeCode not equals to DEFAULT_TYPE_CODE
        defaultFoodTypeShouldNotBeFound("typeCode.notEquals=" + DEFAULT_TYPE_CODE);

        // Get all the foodTypeList where typeCode not equals to UPDATED_TYPE_CODE
        defaultFoodTypeShouldBeFound("typeCode.notEquals=" + UPDATED_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFoodTypesByTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get all the foodTypeList where typeCode in DEFAULT_TYPE_CODE or UPDATED_TYPE_CODE
        defaultFoodTypeShouldBeFound("typeCode.in=" + DEFAULT_TYPE_CODE + "," + UPDATED_TYPE_CODE);

        // Get all the foodTypeList where typeCode equals to UPDATED_TYPE_CODE
        defaultFoodTypeShouldNotBeFound("typeCode.in=" + UPDATED_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFoodTypesByTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get all the foodTypeList where typeCode is not null
        defaultFoodTypeShouldBeFound("typeCode.specified=true");

        // Get all the foodTypeList where typeCode is null
        defaultFoodTypeShouldNotBeFound("typeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllFoodTypesByTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get all the foodTypeList where typeCode contains DEFAULT_TYPE_CODE
        defaultFoodTypeShouldBeFound("typeCode.contains=" + DEFAULT_TYPE_CODE);

        // Get all the foodTypeList where typeCode contains UPDATED_TYPE_CODE
        defaultFoodTypeShouldNotBeFound("typeCode.contains=" + UPDATED_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFoodTypesByTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get all the foodTypeList where typeCode does not contain DEFAULT_TYPE_CODE
        defaultFoodTypeShouldNotBeFound("typeCode.doesNotContain=" + DEFAULT_TYPE_CODE);

        // Get all the foodTypeList where typeCode does not contain UPDATED_TYPE_CODE
        defaultFoodTypeShouldBeFound("typeCode.doesNotContain=" + UPDATED_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllFoodTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get all the foodTypeList where description equals to DEFAULT_DESCRIPTION
        defaultFoodTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the foodTypeList where description equals to UPDATED_DESCRIPTION
        defaultFoodTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFoodTypesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get all the foodTypeList where description not equals to DEFAULT_DESCRIPTION
        defaultFoodTypeShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the foodTypeList where description not equals to UPDATED_DESCRIPTION
        defaultFoodTypeShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFoodTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get all the foodTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFoodTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the foodTypeList where description equals to UPDATED_DESCRIPTION
        defaultFoodTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFoodTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get all the foodTypeList where description is not null
        defaultFoodTypeShouldBeFound("description.specified=true");

        // Get all the foodTypeList where description is null
        defaultFoodTypeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllFoodTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get all the foodTypeList where description contains DEFAULT_DESCRIPTION
        defaultFoodTypeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the foodTypeList where description contains UPDATED_DESCRIPTION
        defaultFoodTypeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFoodTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        // Get all the foodTypeList where description does not contain DEFAULT_DESCRIPTION
        defaultFoodTypeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the foodTypeList where description does not contain UPDATED_DESCRIPTION
        defaultFoodTypeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFoodTypesByFoodsIsEqualToSomething() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);
        FoodEntity foods = FoodResourceIT.createEntity(em);
        em.persist(foods);
        em.flush();
        foodTypeEntity.addFoods(foods);
        foodTypeRepository.saveAndFlush(foodTypeEntity);
        Long foodsId = foods.getId();

        // Get all the foodTypeList where foods equals to foodsId
        defaultFoodTypeShouldBeFound("foodsId.equals=" + foodsId);

        // Get all the foodTypeList where foods equals to (foodsId + 1)
        defaultFoodTypeShouldNotBeFound("foodsId.equals=" + (foodsId + 1));
    }

    @Test
    @Transactional
    void getAllFoodTypesByPartyIsEqualToSomething() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);
        PartyEntity party = PartyResourceIT.createEntity(em);
        em.persist(party);
        em.flush();
        foodTypeEntity.setParty(party);
        foodTypeRepository.saveAndFlush(foodTypeEntity);
        Long partyId = party.getId();

        // Get all the foodTypeList where party equals to partyId
        defaultFoodTypeShouldBeFound("partyId.equals=" + partyId);

        // Get all the foodTypeList where party equals to (partyId + 1)
        defaultFoodTypeShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFoodTypeShouldBeFound(String filter) throws Exception {
        restFoodTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(foodTypeEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].typeCode").value(hasItem(DEFAULT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restFoodTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFoodTypeShouldNotBeFound(String filter) throws Exception {
        restFoodTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFoodTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFoodType() throws Exception {
        // Get the foodType
        restFoodTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFoodType() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        int databaseSizeBeforeUpdate = foodTypeRepository.findAll().size();

        // Update the foodType
        FoodTypeEntity updatedFoodTypeEntity = foodTypeRepository.findById(foodTypeEntity.getId()).get();
        // Disconnect from session so that the updates on updatedFoodTypeEntity are not directly saved in db
        em.detach(updatedFoodTypeEntity);
        updatedFoodTypeEntity.title(UPDATED_TITLE).typeCode(UPDATED_TYPE_CODE).description(UPDATED_DESCRIPTION);
        FoodTypeDTO foodTypeDTO = foodTypeMapper.toDto(updatedFoodTypeEntity);

        restFoodTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, foodTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foodTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the FoodType in the database
        List<FoodTypeEntity> foodTypeList = foodTypeRepository.findAll();
        assertThat(foodTypeList).hasSize(databaseSizeBeforeUpdate);
        FoodTypeEntity testFoodType = foodTypeList.get(foodTypeList.size() - 1);
        assertThat(testFoodType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFoodType.getTypeCode()).isEqualTo(UPDATED_TYPE_CODE);
        assertThat(testFoodType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingFoodType() throws Exception {
        int databaseSizeBeforeUpdate = foodTypeRepository.findAll().size();
        foodTypeEntity.setId(count.incrementAndGet());

        // Create the FoodType
        FoodTypeDTO foodTypeDTO = foodTypeMapper.toDto(foodTypeEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, foodTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foodTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodType in the database
        List<FoodTypeEntity> foodTypeList = foodTypeRepository.findAll();
        assertThat(foodTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFoodType() throws Exception {
        int databaseSizeBeforeUpdate = foodTypeRepository.findAll().size();
        foodTypeEntity.setId(count.incrementAndGet());

        // Create the FoodType
        FoodTypeDTO foodTypeDTO = foodTypeMapper.toDto(foodTypeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(foodTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodType in the database
        List<FoodTypeEntity> foodTypeList = foodTypeRepository.findAll();
        assertThat(foodTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFoodType() throws Exception {
        int databaseSizeBeforeUpdate = foodTypeRepository.findAll().size();
        foodTypeEntity.setId(count.incrementAndGet());

        // Create the FoodType
        FoodTypeDTO foodTypeDTO = foodTypeMapper.toDto(foodTypeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(foodTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FoodType in the database
        List<FoodTypeEntity> foodTypeList = foodTypeRepository.findAll();
        assertThat(foodTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFoodTypeWithPatch() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        int databaseSizeBeforeUpdate = foodTypeRepository.findAll().size();

        // Update the foodType using partial update
        FoodTypeEntity partialUpdatedFoodTypeEntity = new FoodTypeEntity();
        partialUpdatedFoodTypeEntity.setId(foodTypeEntity.getId());

        partialUpdatedFoodTypeEntity.title(UPDATED_TITLE).typeCode(UPDATED_TYPE_CODE);

        restFoodTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoodTypeEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFoodTypeEntity))
            )
            .andExpect(status().isOk());

        // Validate the FoodType in the database
        List<FoodTypeEntity> foodTypeList = foodTypeRepository.findAll();
        assertThat(foodTypeList).hasSize(databaseSizeBeforeUpdate);
        FoodTypeEntity testFoodType = foodTypeList.get(foodTypeList.size() - 1);
        assertThat(testFoodType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFoodType.getTypeCode()).isEqualTo(UPDATED_TYPE_CODE);
        assertThat(testFoodType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateFoodTypeWithPatch() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        int databaseSizeBeforeUpdate = foodTypeRepository.findAll().size();

        // Update the foodType using partial update
        FoodTypeEntity partialUpdatedFoodTypeEntity = new FoodTypeEntity();
        partialUpdatedFoodTypeEntity.setId(foodTypeEntity.getId());

        partialUpdatedFoodTypeEntity.title(UPDATED_TITLE).typeCode(UPDATED_TYPE_CODE).description(UPDATED_DESCRIPTION);

        restFoodTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFoodTypeEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFoodTypeEntity))
            )
            .andExpect(status().isOk());

        // Validate the FoodType in the database
        List<FoodTypeEntity> foodTypeList = foodTypeRepository.findAll();
        assertThat(foodTypeList).hasSize(databaseSizeBeforeUpdate);
        FoodTypeEntity testFoodType = foodTypeList.get(foodTypeList.size() - 1);
        assertThat(testFoodType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFoodType.getTypeCode()).isEqualTo(UPDATED_TYPE_CODE);
        assertThat(testFoodType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingFoodType() throws Exception {
        int databaseSizeBeforeUpdate = foodTypeRepository.findAll().size();
        foodTypeEntity.setId(count.incrementAndGet());

        // Create the FoodType
        FoodTypeDTO foodTypeDTO = foodTypeMapper.toDto(foodTypeEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, foodTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(foodTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodType in the database
        List<FoodTypeEntity> foodTypeList = foodTypeRepository.findAll();
        assertThat(foodTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFoodType() throws Exception {
        int databaseSizeBeforeUpdate = foodTypeRepository.findAll().size();
        foodTypeEntity.setId(count.incrementAndGet());

        // Create the FoodType
        FoodTypeDTO foodTypeDTO = foodTypeMapper.toDto(foodTypeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(foodTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FoodType in the database
        List<FoodTypeEntity> foodTypeList = foodTypeRepository.findAll();
        assertThat(foodTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFoodType() throws Exception {
        int databaseSizeBeforeUpdate = foodTypeRepository.findAll().size();
        foodTypeEntity.setId(count.incrementAndGet());

        // Create the FoodType
        FoodTypeDTO foodTypeDTO = foodTypeMapper.toDto(foodTypeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFoodTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(foodTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FoodType in the database
        List<FoodTypeEntity> foodTypeList = foodTypeRepository.findAll();
        assertThat(foodTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFoodType() throws Exception {
        // Initialize the database
        foodTypeRepository.saveAndFlush(foodTypeEntity);

        int databaseSizeBeforeDelete = foodTypeRepository.findAll().size();

        // Delete the foodType
        restFoodTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, foodTypeEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FoodTypeEntity> foodTypeList = foodTypeRepository.findAll();
        assertThat(foodTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
