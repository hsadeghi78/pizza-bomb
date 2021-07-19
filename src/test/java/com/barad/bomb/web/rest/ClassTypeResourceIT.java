package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.ClassTypeEntity;
import com.barad.bomb.domain.ClassificationEntity;
import com.barad.bomb.repository.ClassTypeRepository;
import com.barad.bomb.service.criteria.ClassTypeCriteria;
import com.barad.bomb.service.dto.ClassTypeDTO;
import com.barad.bomb.service.mapper.ClassTypeMapper;
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
 * Integration tests for the {@link ClassTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClassTypeResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_TYPE_CODE = 1;
    private static final Integer UPDATED_TYPE_CODE = 2;
    private static final Integer SMALLER_TYPE_CODE = 1 - 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/class-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClassTypeRepository classTypeRepository;

    @Autowired
    private ClassTypeMapper classTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassTypeMockMvc;

    private ClassTypeEntity classTypeEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassTypeEntity createEntity(EntityManager em) {
        ClassTypeEntity classTypeEntity = new ClassTypeEntity()
            .title(DEFAULT_TITLE)
            .typeCode(DEFAULT_TYPE_CODE)
            .description(DEFAULT_DESCRIPTION);
        return classTypeEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassTypeEntity createUpdatedEntity(EntityManager em) {
        ClassTypeEntity classTypeEntity = new ClassTypeEntity()
            .title(UPDATED_TITLE)
            .typeCode(UPDATED_TYPE_CODE)
            .description(UPDATED_DESCRIPTION);
        return classTypeEntity;
    }

    @BeforeEach
    public void initTest() {
        classTypeEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createClassType() throws Exception {
        int databaseSizeBeforeCreate = classTypeRepository.findAll().size();
        // Create the ClassType
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classTypeEntity);
        restClassTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the ClassType in the database
        List<ClassTypeEntity> classTypeList = classTypeRepository.findAll();
        assertThat(classTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ClassTypeEntity testClassType = classTypeList.get(classTypeList.size() - 1);
        assertThat(testClassType.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testClassType.getTypeCode()).isEqualTo(DEFAULT_TYPE_CODE);
        assertThat(testClassType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createClassTypeWithExistingId() throws Exception {
        // Create the ClassType with an existing ID
        classTypeEntity.setId(1L);
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classTypeEntity);

        int databaseSizeBeforeCreate = classTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClassType in the database
        List<ClassTypeEntity> classTypeList = classTypeRepository.findAll();
        assertThat(classTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = classTypeRepository.findAll().size();
        // set the field null
        classTypeEntity.setTitle(null);

        // Create the ClassType, which fails.
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classTypeEntity);

        restClassTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classTypeDTO)))
            .andExpect(status().isBadRequest());

        List<ClassTypeEntity> classTypeList = classTypeRepository.findAll();
        assertThat(classTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = classTypeRepository.findAll().size();
        // set the field null
        classTypeEntity.setTypeCode(null);

        // Create the ClassType, which fails.
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classTypeEntity);

        restClassTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classTypeDTO)))
            .andExpect(status().isBadRequest());

        List<ClassTypeEntity> classTypeList = classTypeRepository.findAll();
        assertThat(classTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClassTypes() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList
        restClassTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classTypeEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].typeCode").value(hasItem(DEFAULT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getClassType() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get the classType
        restClassTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, classTypeEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classTypeEntity.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.typeCode").value(DEFAULT_TYPE_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getClassTypesByIdFiltering() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        Long id = classTypeEntity.getId();

        defaultClassTypeShouldBeFound("id.equals=" + id);
        defaultClassTypeShouldNotBeFound("id.notEquals=" + id);

        defaultClassTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClassTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultClassTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClassTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClassTypesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where title equals to DEFAULT_TITLE
        defaultClassTypeShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the classTypeList where title equals to UPDATED_TITLE
        defaultClassTypeShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllClassTypesByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where title not equals to DEFAULT_TITLE
        defaultClassTypeShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the classTypeList where title not equals to UPDATED_TITLE
        defaultClassTypeShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllClassTypesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultClassTypeShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the classTypeList where title equals to UPDATED_TITLE
        defaultClassTypeShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllClassTypesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where title is not null
        defaultClassTypeShouldBeFound("title.specified=true");

        // Get all the classTypeList where title is null
        defaultClassTypeShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllClassTypesByTitleContainsSomething() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where title contains DEFAULT_TITLE
        defaultClassTypeShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the classTypeList where title contains UPDATED_TITLE
        defaultClassTypeShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllClassTypesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where title does not contain DEFAULT_TITLE
        defaultClassTypeShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the classTypeList where title does not contain UPDATED_TITLE
        defaultClassTypeShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllClassTypesByTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where typeCode equals to DEFAULT_TYPE_CODE
        defaultClassTypeShouldBeFound("typeCode.equals=" + DEFAULT_TYPE_CODE);

        // Get all the classTypeList where typeCode equals to UPDATED_TYPE_CODE
        defaultClassTypeShouldNotBeFound("typeCode.equals=" + UPDATED_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllClassTypesByTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where typeCode not equals to DEFAULT_TYPE_CODE
        defaultClassTypeShouldNotBeFound("typeCode.notEquals=" + DEFAULT_TYPE_CODE);

        // Get all the classTypeList where typeCode not equals to UPDATED_TYPE_CODE
        defaultClassTypeShouldBeFound("typeCode.notEquals=" + UPDATED_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllClassTypesByTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where typeCode in DEFAULT_TYPE_CODE or UPDATED_TYPE_CODE
        defaultClassTypeShouldBeFound("typeCode.in=" + DEFAULT_TYPE_CODE + "," + UPDATED_TYPE_CODE);

        // Get all the classTypeList where typeCode equals to UPDATED_TYPE_CODE
        defaultClassTypeShouldNotBeFound("typeCode.in=" + UPDATED_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllClassTypesByTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where typeCode is not null
        defaultClassTypeShouldBeFound("typeCode.specified=true");

        // Get all the classTypeList where typeCode is null
        defaultClassTypeShouldNotBeFound("typeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllClassTypesByTypeCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where typeCode is greater than or equal to DEFAULT_TYPE_CODE
        defaultClassTypeShouldBeFound("typeCode.greaterThanOrEqual=" + DEFAULT_TYPE_CODE);

        // Get all the classTypeList where typeCode is greater than or equal to UPDATED_TYPE_CODE
        defaultClassTypeShouldNotBeFound("typeCode.greaterThanOrEqual=" + UPDATED_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllClassTypesByTypeCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where typeCode is less than or equal to DEFAULT_TYPE_CODE
        defaultClassTypeShouldBeFound("typeCode.lessThanOrEqual=" + DEFAULT_TYPE_CODE);

        // Get all the classTypeList where typeCode is less than or equal to SMALLER_TYPE_CODE
        defaultClassTypeShouldNotBeFound("typeCode.lessThanOrEqual=" + SMALLER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllClassTypesByTypeCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where typeCode is less than DEFAULT_TYPE_CODE
        defaultClassTypeShouldNotBeFound("typeCode.lessThan=" + DEFAULT_TYPE_CODE);

        // Get all the classTypeList where typeCode is less than UPDATED_TYPE_CODE
        defaultClassTypeShouldBeFound("typeCode.lessThan=" + UPDATED_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllClassTypesByTypeCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where typeCode is greater than DEFAULT_TYPE_CODE
        defaultClassTypeShouldNotBeFound("typeCode.greaterThan=" + DEFAULT_TYPE_CODE);

        // Get all the classTypeList where typeCode is greater than SMALLER_TYPE_CODE
        defaultClassTypeShouldBeFound("typeCode.greaterThan=" + SMALLER_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllClassTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where description equals to DEFAULT_DESCRIPTION
        defaultClassTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the classTypeList where description equals to UPDATED_DESCRIPTION
        defaultClassTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllClassTypesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where description not equals to DEFAULT_DESCRIPTION
        defaultClassTypeShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the classTypeList where description not equals to UPDATED_DESCRIPTION
        defaultClassTypeShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllClassTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultClassTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the classTypeList where description equals to UPDATED_DESCRIPTION
        defaultClassTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllClassTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where description is not null
        defaultClassTypeShouldBeFound("description.specified=true");

        // Get all the classTypeList where description is null
        defaultClassTypeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllClassTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where description contains DEFAULT_DESCRIPTION
        defaultClassTypeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the classTypeList where description contains UPDATED_DESCRIPTION
        defaultClassTypeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllClassTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        // Get all the classTypeList where description does not contain DEFAULT_DESCRIPTION
        defaultClassTypeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the classTypeList where description does not contain UPDATED_DESCRIPTION
        defaultClassTypeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllClassTypesByClassificationsIsEqualToSomething() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);
        ClassificationEntity classifications = ClassificationResourceIT.createEntity(em);
        em.persist(classifications);
        em.flush();
        classTypeEntity.addClassifications(classifications);
        classTypeRepository.saveAndFlush(classTypeEntity);
        Long classificationsId = classifications.getId();

        // Get all the classTypeList where classifications equals to classificationsId
        defaultClassTypeShouldBeFound("classificationsId.equals=" + classificationsId);

        // Get all the classTypeList where classifications equals to (classificationsId + 1)
        defaultClassTypeShouldNotBeFound("classificationsId.equals=" + (classificationsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassTypeShouldBeFound(String filter) throws Exception {
        restClassTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classTypeEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].typeCode").value(hasItem(DEFAULT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restClassTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassTypeShouldNotBeFound(String filter) throws Exception {
        restClassTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClassType() throws Exception {
        // Get the classType
        restClassTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClassType() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        int databaseSizeBeforeUpdate = classTypeRepository.findAll().size();

        // Update the classType
        ClassTypeEntity updatedClassTypeEntity = classTypeRepository.findById(classTypeEntity.getId()).get();
        // Disconnect from session so that the updates on updatedClassTypeEntity are not directly saved in db
        em.detach(updatedClassTypeEntity);
        updatedClassTypeEntity.title(UPDATED_TITLE).typeCode(UPDATED_TYPE_CODE).description(UPDATED_DESCRIPTION);
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(updatedClassTypeEntity);

        restClassTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClassType in the database
        List<ClassTypeEntity> classTypeList = classTypeRepository.findAll();
        assertThat(classTypeList).hasSize(databaseSizeBeforeUpdate);
        ClassTypeEntity testClassType = classTypeList.get(classTypeList.size() - 1);
        assertThat(testClassType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testClassType.getTypeCode()).isEqualTo(UPDATED_TYPE_CODE);
        assertThat(testClassType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingClassType() throws Exception {
        int databaseSizeBeforeUpdate = classTypeRepository.findAll().size();
        classTypeEntity.setId(count.incrementAndGet());

        // Create the ClassType
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classTypeEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassType in the database
        List<ClassTypeEntity> classTypeList = classTypeRepository.findAll();
        assertThat(classTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassType() throws Exception {
        int databaseSizeBeforeUpdate = classTypeRepository.findAll().size();
        classTypeEntity.setId(count.incrementAndGet());

        // Create the ClassType
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classTypeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassType in the database
        List<ClassTypeEntity> classTypeList = classTypeRepository.findAll();
        assertThat(classTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassType() throws Exception {
        int databaseSizeBeforeUpdate = classTypeRepository.findAll().size();
        classTypeEntity.setId(count.incrementAndGet());

        // Create the ClassType
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classTypeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassType in the database
        List<ClassTypeEntity> classTypeList = classTypeRepository.findAll();
        assertThat(classTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassTypeWithPatch() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        int databaseSizeBeforeUpdate = classTypeRepository.findAll().size();

        // Update the classType using partial update
        ClassTypeEntity partialUpdatedClassTypeEntity = new ClassTypeEntity();
        partialUpdatedClassTypeEntity.setId(classTypeEntity.getId());

        partialUpdatedClassTypeEntity.description(UPDATED_DESCRIPTION);

        restClassTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassTypeEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassTypeEntity))
            )
            .andExpect(status().isOk());

        // Validate the ClassType in the database
        List<ClassTypeEntity> classTypeList = classTypeRepository.findAll();
        assertThat(classTypeList).hasSize(databaseSizeBeforeUpdate);
        ClassTypeEntity testClassType = classTypeList.get(classTypeList.size() - 1);
        assertThat(testClassType.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testClassType.getTypeCode()).isEqualTo(DEFAULT_TYPE_CODE);
        assertThat(testClassType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateClassTypeWithPatch() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        int databaseSizeBeforeUpdate = classTypeRepository.findAll().size();

        // Update the classType using partial update
        ClassTypeEntity partialUpdatedClassTypeEntity = new ClassTypeEntity();
        partialUpdatedClassTypeEntity.setId(classTypeEntity.getId());

        partialUpdatedClassTypeEntity.title(UPDATED_TITLE).typeCode(UPDATED_TYPE_CODE).description(UPDATED_DESCRIPTION);

        restClassTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassTypeEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassTypeEntity))
            )
            .andExpect(status().isOk());

        // Validate the ClassType in the database
        List<ClassTypeEntity> classTypeList = classTypeRepository.findAll();
        assertThat(classTypeList).hasSize(databaseSizeBeforeUpdate);
        ClassTypeEntity testClassType = classTypeList.get(classTypeList.size() - 1);
        assertThat(testClassType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testClassType.getTypeCode()).isEqualTo(UPDATED_TYPE_CODE);
        assertThat(testClassType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingClassType() throws Exception {
        int databaseSizeBeforeUpdate = classTypeRepository.findAll().size();
        classTypeEntity.setId(count.incrementAndGet());

        // Create the ClassType
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classTypeEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassType in the database
        List<ClassTypeEntity> classTypeList = classTypeRepository.findAll();
        assertThat(classTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassType() throws Exception {
        int databaseSizeBeforeUpdate = classTypeRepository.findAll().size();
        classTypeEntity.setId(count.incrementAndGet());

        // Create the ClassType
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classTypeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassType in the database
        List<ClassTypeEntity> classTypeList = classTypeRepository.findAll();
        assertThat(classTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassType() throws Exception {
        int databaseSizeBeforeUpdate = classTypeRepository.findAll().size();
        classTypeEntity.setId(count.incrementAndGet());

        // Create the ClassType
        ClassTypeDTO classTypeDTO = classTypeMapper.toDto(classTypeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(classTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassType in the database
        List<ClassTypeEntity> classTypeList = classTypeRepository.findAll();
        assertThat(classTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassType() throws Exception {
        // Initialize the database
        classTypeRepository.saveAndFlush(classTypeEntity);

        int databaseSizeBeforeDelete = classTypeRepository.findAll().size();

        // Delete the classType
        restClassTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, classTypeEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassTypeEntity> classTypeList = classTypeRepository.findAll();
        assertThat(classTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
