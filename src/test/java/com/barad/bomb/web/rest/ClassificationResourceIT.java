package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.ClassTypeEntity;
import com.barad.bomb.domain.ClassificationEntity;
import com.barad.bomb.repository.ClassificationRepository;
import com.barad.bomb.service.criteria.ClassificationCriteria;
import com.barad.bomb.service.dto.ClassificationDTO;
import com.barad.bomb.service.mapper.ClassificationMapper;
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
 * Integration tests for the {@link ClassificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClassificationResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CLASS_CODE = 1;
    private static final Integer UPDATED_CLASS_CODE = 2;
    private static final Integer SMALLER_CLASS_CODE = 1 - 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/classifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClassificationRepository classificationRepository;

    @Autowired
    private ClassificationMapper classificationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassificationMockMvc;

    private ClassificationEntity classificationEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassificationEntity createEntity(EntityManager em) {
        ClassificationEntity classificationEntity = new ClassificationEntity()
            .title(DEFAULT_TITLE)
            .classCode(DEFAULT_CLASS_CODE)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        ClassTypeEntity classType;
        if (TestUtil.findAll(em, ClassTypeEntity.class).isEmpty()) {
            classType = ClassTypeResourceIT.createEntity(em);
            em.persist(classType);
            em.flush();
        } else {
            classType = TestUtil.findAll(em, ClassTypeEntity.class).get(0);
        }
        classificationEntity.setClassType(classType);
        return classificationEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassificationEntity createUpdatedEntity(EntityManager em) {
        ClassificationEntity classificationEntity = new ClassificationEntity()
            .title(UPDATED_TITLE)
            .classCode(UPDATED_CLASS_CODE)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        ClassTypeEntity classType;
        if (TestUtil.findAll(em, ClassTypeEntity.class).isEmpty()) {
            classType = ClassTypeResourceIT.createUpdatedEntity(em);
            em.persist(classType);
            em.flush();
        } else {
            classType = TestUtil.findAll(em, ClassTypeEntity.class).get(0);
        }
        classificationEntity.setClassType(classType);
        return classificationEntity;
    }

    @BeforeEach
    public void initTest() {
        classificationEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createClassification() throws Exception {
        int databaseSizeBeforeCreate = classificationRepository.findAll().size();
        // Create the Classification
        ClassificationDTO classificationDTO = classificationMapper.toDto(classificationEntity);
        restClassificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classificationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Classification in the database
        List<ClassificationEntity> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeCreate + 1);
        ClassificationEntity testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testClassification.getClassCode()).isEqualTo(DEFAULT_CLASS_CODE);
        assertThat(testClassification.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createClassificationWithExistingId() throws Exception {
        // Create the Classification with an existing ID
        classificationEntity.setId(1L);
        ClassificationDTO classificationDTO = classificationMapper.toDto(classificationEntity);

        int databaseSizeBeforeCreate = classificationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classification in the database
        List<ClassificationEntity> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = classificationRepository.findAll().size();
        // set the field null
        classificationEntity.setTitle(null);

        // Create the Classification, which fails.
        ClassificationDTO classificationDTO = classificationMapper.toDto(classificationEntity);

        restClassificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClassificationEntity> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClassCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = classificationRepository.findAll().size();
        // set the field null
        classificationEntity.setClassCode(null);

        // Create the Classification, which fails.
        ClassificationDTO classificationDTO = classificationMapper.toDto(classificationEntity);

        restClassificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<ClassificationEntity> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClassifications() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList
        restClassificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classificationEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].classCode").value(hasItem(DEFAULT_CLASS_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getClassification() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get the classification
        restClassificationMockMvc
            .perform(get(ENTITY_API_URL_ID, classificationEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classificationEntity.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.classCode").value(DEFAULT_CLASS_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getClassificationsByIdFiltering() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        Long id = classificationEntity.getId();

        defaultClassificationShouldBeFound("id.equals=" + id);
        defaultClassificationShouldNotBeFound("id.notEquals=" + id);

        defaultClassificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClassificationShouldNotBeFound("id.greaterThan=" + id);

        defaultClassificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClassificationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClassificationsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where title equals to DEFAULT_TITLE
        defaultClassificationShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the classificationList where title equals to UPDATED_TITLE
        defaultClassificationShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllClassificationsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where title not equals to DEFAULT_TITLE
        defaultClassificationShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the classificationList where title not equals to UPDATED_TITLE
        defaultClassificationShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllClassificationsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultClassificationShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the classificationList where title equals to UPDATED_TITLE
        defaultClassificationShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllClassificationsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where title is not null
        defaultClassificationShouldBeFound("title.specified=true");

        // Get all the classificationList where title is null
        defaultClassificationShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationsByTitleContainsSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where title contains DEFAULT_TITLE
        defaultClassificationShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the classificationList where title contains UPDATED_TITLE
        defaultClassificationShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllClassificationsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where title does not contain DEFAULT_TITLE
        defaultClassificationShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the classificationList where title does not contain UPDATED_TITLE
        defaultClassificationShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllClassificationsByClassCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where classCode equals to DEFAULT_CLASS_CODE
        defaultClassificationShouldBeFound("classCode.equals=" + DEFAULT_CLASS_CODE);

        // Get all the classificationList where classCode equals to UPDATED_CLASS_CODE
        defaultClassificationShouldNotBeFound("classCode.equals=" + UPDATED_CLASS_CODE);
    }

    @Test
    @Transactional
    void getAllClassificationsByClassCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where classCode not equals to DEFAULT_CLASS_CODE
        defaultClassificationShouldNotBeFound("classCode.notEquals=" + DEFAULT_CLASS_CODE);

        // Get all the classificationList where classCode not equals to UPDATED_CLASS_CODE
        defaultClassificationShouldBeFound("classCode.notEquals=" + UPDATED_CLASS_CODE);
    }

    @Test
    @Transactional
    void getAllClassificationsByClassCodeIsInShouldWork() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where classCode in DEFAULT_CLASS_CODE or UPDATED_CLASS_CODE
        defaultClassificationShouldBeFound("classCode.in=" + DEFAULT_CLASS_CODE + "," + UPDATED_CLASS_CODE);

        // Get all the classificationList where classCode equals to UPDATED_CLASS_CODE
        defaultClassificationShouldNotBeFound("classCode.in=" + UPDATED_CLASS_CODE);
    }

    @Test
    @Transactional
    void getAllClassificationsByClassCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where classCode is not null
        defaultClassificationShouldBeFound("classCode.specified=true");

        // Get all the classificationList where classCode is null
        defaultClassificationShouldNotBeFound("classCode.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationsByClassCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where classCode is greater than or equal to DEFAULT_CLASS_CODE
        defaultClassificationShouldBeFound("classCode.greaterThanOrEqual=" + DEFAULT_CLASS_CODE);

        // Get all the classificationList where classCode is greater than or equal to UPDATED_CLASS_CODE
        defaultClassificationShouldNotBeFound("classCode.greaterThanOrEqual=" + UPDATED_CLASS_CODE);
    }

    @Test
    @Transactional
    void getAllClassificationsByClassCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where classCode is less than or equal to DEFAULT_CLASS_CODE
        defaultClassificationShouldBeFound("classCode.lessThanOrEqual=" + DEFAULT_CLASS_CODE);

        // Get all the classificationList where classCode is less than or equal to SMALLER_CLASS_CODE
        defaultClassificationShouldNotBeFound("classCode.lessThanOrEqual=" + SMALLER_CLASS_CODE);
    }

    @Test
    @Transactional
    void getAllClassificationsByClassCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where classCode is less than DEFAULT_CLASS_CODE
        defaultClassificationShouldNotBeFound("classCode.lessThan=" + DEFAULT_CLASS_CODE);

        // Get all the classificationList where classCode is less than UPDATED_CLASS_CODE
        defaultClassificationShouldBeFound("classCode.lessThan=" + UPDATED_CLASS_CODE);
    }

    @Test
    @Transactional
    void getAllClassificationsByClassCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where classCode is greater than DEFAULT_CLASS_CODE
        defaultClassificationShouldNotBeFound("classCode.greaterThan=" + DEFAULT_CLASS_CODE);

        // Get all the classificationList where classCode is greater than SMALLER_CLASS_CODE
        defaultClassificationShouldBeFound("classCode.greaterThan=" + SMALLER_CLASS_CODE);
    }

    @Test
    @Transactional
    void getAllClassificationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where description equals to DEFAULT_DESCRIPTION
        defaultClassificationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the classificationList where description equals to UPDATED_DESCRIPTION
        defaultClassificationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllClassificationsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where description not equals to DEFAULT_DESCRIPTION
        defaultClassificationShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the classificationList where description not equals to UPDATED_DESCRIPTION
        defaultClassificationShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllClassificationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultClassificationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the classificationList where description equals to UPDATED_DESCRIPTION
        defaultClassificationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllClassificationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where description is not null
        defaultClassificationShouldBeFound("description.specified=true");

        // Get all the classificationList where description is null
        defaultClassificationShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where description contains DEFAULT_DESCRIPTION
        defaultClassificationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the classificationList where description contains UPDATED_DESCRIPTION
        defaultClassificationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllClassificationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        // Get all the classificationList where description does not contain DEFAULT_DESCRIPTION
        defaultClassificationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the classificationList where description does not contain UPDATED_DESCRIPTION
        defaultClassificationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllClassificationsByClassTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);
        ClassTypeEntity classType = ClassTypeResourceIT.createEntity(em);
        em.persist(classType);
        em.flush();
        classificationEntity.setClassType(classType);
        classificationRepository.saveAndFlush(classificationEntity);
        Long classTypeId = classType.getId();

        // Get all the classificationList where classType equals to classTypeId
        defaultClassificationShouldBeFound("classTypeId.equals=" + classTypeId);

        // Get all the classificationList where classType equals to (classTypeId + 1)
        defaultClassificationShouldNotBeFound("classTypeId.equals=" + (classTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassificationShouldBeFound(String filter) throws Exception {
        restClassificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classificationEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].classCode").value(hasItem(DEFAULT_CLASS_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restClassificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassificationShouldNotBeFound(String filter) throws Exception {
        restClassificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClassification() throws Exception {
        // Get the classification
        restClassificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClassification() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();

        // Update the classification
        ClassificationEntity updatedClassificationEntity = classificationRepository.findById(classificationEntity.getId()).get();
        // Disconnect from session so that the updates on updatedClassificationEntity are not directly saved in db
        em.detach(updatedClassificationEntity);
        updatedClassificationEntity.title(UPDATED_TITLE).classCode(UPDATED_CLASS_CODE).description(UPDATED_DESCRIPTION);
        ClassificationDTO classificationDTO = classificationMapper.toDto(updatedClassificationEntity);

        restClassificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Classification in the database
        List<ClassificationEntity> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
        ClassificationEntity testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testClassification.getClassCode()).isEqualTo(UPDATED_CLASS_CODE);
        assertThat(testClassification.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();
        classificationEntity.setId(count.incrementAndGet());

        // Create the Classification
        ClassificationDTO classificationDTO = classificationMapper.toDto(classificationEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classification in the database
        List<ClassificationEntity> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();
        classificationEntity.setId(count.incrementAndGet());

        // Create the Classification
        ClassificationDTO classificationDTO = classificationMapper.toDto(classificationEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classification in the database
        List<ClassificationEntity> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();
        classificationEntity.setId(count.incrementAndGet());

        // Create the Classification
        ClassificationDTO classificationDTO = classificationMapper.toDto(classificationEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassificationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classification in the database
        List<ClassificationEntity> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassificationWithPatch() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();

        // Update the classification using partial update
        ClassificationEntity partialUpdatedClassificationEntity = new ClassificationEntity();
        partialUpdatedClassificationEntity.setId(classificationEntity.getId());

        partialUpdatedClassificationEntity.title(UPDATED_TITLE).classCode(UPDATED_CLASS_CODE);

        restClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassificationEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassificationEntity))
            )
            .andExpect(status().isOk());

        // Validate the Classification in the database
        List<ClassificationEntity> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
        ClassificationEntity testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testClassification.getClassCode()).isEqualTo(UPDATED_CLASS_CODE);
        assertThat(testClassification.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateClassificationWithPatch() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();

        // Update the classification using partial update
        ClassificationEntity partialUpdatedClassificationEntity = new ClassificationEntity();
        partialUpdatedClassificationEntity.setId(classificationEntity.getId());

        partialUpdatedClassificationEntity.title(UPDATED_TITLE).classCode(UPDATED_CLASS_CODE).description(UPDATED_DESCRIPTION);

        restClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassificationEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassificationEntity))
            )
            .andExpect(status().isOk());

        // Validate the Classification in the database
        List<ClassificationEntity> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
        ClassificationEntity testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testClassification.getClassCode()).isEqualTo(UPDATED_CLASS_CODE);
        assertThat(testClassification.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();
        classificationEntity.setId(count.incrementAndGet());

        // Create the Classification
        ClassificationDTO classificationDTO = classificationMapper.toDto(classificationEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classificationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classification in the database
        List<ClassificationEntity> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();
        classificationEntity.setId(count.incrementAndGet());

        // Create the Classification
        ClassificationDTO classificationDTO = classificationMapper.toDto(classificationEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classification in the database
        List<ClassificationEntity> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();
        classificationEntity.setId(count.incrementAndGet());

        // Create the Classification
        ClassificationDTO classificationDTO = classificationMapper.toDto(classificationEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classification in the database
        List<ClassificationEntity> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassification() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classificationEntity);

        int databaseSizeBeforeDelete = classificationRepository.findAll().size();

        // Delete the classification
        restClassificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, classificationEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassificationEntity> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
