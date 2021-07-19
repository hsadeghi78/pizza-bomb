package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.FileDocumentEntity;
import com.barad.bomb.domain.PartyEntity;
import com.barad.bomb.repository.FileDocumentRepository;
import com.barad.bomb.service.criteria.FileDocumentCriteria;
import com.barad.bomb.service.dto.FileDocumentDTO;
import com.barad.bomb.service.mapper.FileDocumentMapper;
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
 * Integration tests for the {@link FileDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileDocumentResourceIT {

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_CONTENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/file-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FileDocumentRepository fileDocumentRepository;

    @Autowired
    private FileDocumentMapper fileDocumentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFileDocumentMockMvc;

    private FileDocumentEntity fileDocumentEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileDocumentEntity createEntity(EntityManager em) {
        FileDocumentEntity fileDocumentEntity = new FileDocumentEntity()
            .fileName(DEFAULT_FILE_NAME)
            .fileContent(DEFAULT_FILE_CONTENT)
            .fileContentContentType(DEFAULT_FILE_CONTENT_CONTENT_TYPE)
            .filePath(DEFAULT_FILE_PATH)
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
        fileDocumentEntity.setParty(party);
        return fileDocumentEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileDocumentEntity createUpdatedEntity(EntityManager em) {
        FileDocumentEntity fileDocumentEntity = new FileDocumentEntity()
            .fileName(UPDATED_FILE_NAME)
            .fileContent(UPDATED_FILE_CONTENT)
            .fileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE)
            .filePath(UPDATED_FILE_PATH)
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
        fileDocumentEntity.setParty(party);
        return fileDocumentEntity;
    }

    @BeforeEach
    public void initTest() {
        fileDocumentEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createFileDocument() throws Exception {
        int databaseSizeBeforeCreate = fileDocumentRepository.findAll().size();
        // Create the FileDocument
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocumentEntity);
        restFileDocumentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FileDocument in the database
        List<FileDocumentEntity> fileDocumentList = fileDocumentRepository.findAll();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        FileDocumentEntity testFileDocument = fileDocumentList.get(fileDocumentList.size() - 1);
        assertThat(testFileDocument.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testFileDocument.getFileContent()).isEqualTo(DEFAULT_FILE_CONTENT);
        assertThat(testFileDocument.getFileContentContentType()).isEqualTo(DEFAULT_FILE_CONTENT_CONTENT_TYPE);
        assertThat(testFileDocument.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testFileDocument.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createFileDocumentWithExistingId() throws Exception {
        // Create the FileDocument with an existing ID
        fileDocumentEntity.setId(1L);
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocumentEntity);

        int databaseSizeBeforeCreate = fileDocumentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileDocumentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileDocument in the database
        List<FileDocumentEntity> fileDocumentList = fileDocumentRepository.findAll();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFileNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileDocumentRepository.findAll().size();
        // set the field null
        fileDocumentEntity.setFileName(null);

        // Create the FileDocument, which fails.
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocumentEntity);

        restFileDocumentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        List<FileDocumentEntity> fileDocumentList = fileDocumentRepository.findAll();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileDocumentRepository.findAll().size();
        // set the field null
        fileDocumentEntity.setDescription(null);

        // Create the FileDocument, which fails.
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocumentEntity);

        restFileDocumentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        List<FileDocumentEntity> fileDocumentList = fileDocumentRepository.findAll();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFileDocuments() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get all the fileDocumentList
        restFileDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileDocumentEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileContentContentType").value(hasItem(DEFAULT_FILE_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileContent").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_CONTENT))))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getFileDocument() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get the fileDocument
        restFileDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, fileDocumentEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fileDocumentEntity.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.fileContentContentType").value(DEFAULT_FILE_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileContent").value(Base64Utils.encodeToString(DEFAULT_FILE_CONTENT)))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getFileDocumentsByIdFiltering() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        Long id = fileDocumentEntity.getId();

        defaultFileDocumentShouldBeFound("id.equals=" + id);
        defaultFileDocumentShouldNotBeFound("id.notEquals=" + id);

        defaultFileDocumentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFileDocumentShouldNotBeFound("id.greaterThan=" + id);

        defaultFileDocumentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFileDocumentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFileDocumentsByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get all the fileDocumentList where fileName equals to DEFAULT_FILE_NAME
        defaultFileDocumentShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the fileDocumentList where fileName equals to UPDATED_FILE_NAME
        defaultFileDocumentShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllFileDocumentsByFileNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get all the fileDocumentList where fileName not equals to DEFAULT_FILE_NAME
        defaultFileDocumentShouldNotBeFound("fileName.notEquals=" + DEFAULT_FILE_NAME);

        // Get all the fileDocumentList where fileName not equals to UPDATED_FILE_NAME
        defaultFileDocumentShouldBeFound("fileName.notEquals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllFileDocumentsByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get all the fileDocumentList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultFileDocumentShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the fileDocumentList where fileName equals to UPDATED_FILE_NAME
        defaultFileDocumentShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllFileDocumentsByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get all the fileDocumentList where fileName is not null
        defaultFileDocumentShouldBeFound("fileName.specified=true");

        // Get all the fileDocumentList where fileName is null
        defaultFileDocumentShouldNotBeFound("fileName.specified=false");
    }

    @Test
    @Transactional
    void getAllFileDocumentsByFileNameContainsSomething() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get all the fileDocumentList where fileName contains DEFAULT_FILE_NAME
        defaultFileDocumentShouldBeFound("fileName.contains=" + DEFAULT_FILE_NAME);

        // Get all the fileDocumentList where fileName contains UPDATED_FILE_NAME
        defaultFileDocumentShouldNotBeFound("fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllFileDocumentsByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get all the fileDocumentList where fileName does not contain DEFAULT_FILE_NAME
        defaultFileDocumentShouldNotBeFound("fileName.doesNotContain=" + DEFAULT_FILE_NAME);

        // Get all the fileDocumentList where fileName does not contain UPDATED_FILE_NAME
        defaultFileDocumentShouldBeFound("fileName.doesNotContain=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllFileDocumentsByFilePathIsEqualToSomething() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get all the fileDocumentList where filePath equals to DEFAULT_FILE_PATH
        defaultFileDocumentShouldBeFound("filePath.equals=" + DEFAULT_FILE_PATH);

        // Get all the fileDocumentList where filePath equals to UPDATED_FILE_PATH
        defaultFileDocumentShouldNotBeFound("filePath.equals=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllFileDocumentsByFilePathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get all the fileDocumentList where filePath not equals to DEFAULT_FILE_PATH
        defaultFileDocumentShouldNotBeFound("filePath.notEquals=" + DEFAULT_FILE_PATH);

        // Get all the fileDocumentList where filePath not equals to UPDATED_FILE_PATH
        defaultFileDocumentShouldBeFound("filePath.notEquals=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllFileDocumentsByFilePathIsInShouldWork() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get all the fileDocumentList where filePath in DEFAULT_FILE_PATH or UPDATED_FILE_PATH
        defaultFileDocumentShouldBeFound("filePath.in=" + DEFAULT_FILE_PATH + "," + UPDATED_FILE_PATH);

        // Get all the fileDocumentList where filePath equals to UPDATED_FILE_PATH
        defaultFileDocumentShouldNotBeFound("filePath.in=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllFileDocumentsByFilePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get all the fileDocumentList where filePath is not null
        defaultFileDocumentShouldBeFound("filePath.specified=true");

        // Get all the fileDocumentList where filePath is null
        defaultFileDocumentShouldNotBeFound("filePath.specified=false");
    }

    @Test
    @Transactional
    void getAllFileDocumentsByFilePathContainsSomething() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get all the fileDocumentList where filePath contains DEFAULT_FILE_PATH
        defaultFileDocumentShouldBeFound("filePath.contains=" + DEFAULT_FILE_PATH);

        // Get all the fileDocumentList where filePath contains UPDATED_FILE_PATH
        defaultFileDocumentShouldNotBeFound("filePath.contains=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllFileDocumentsByFilePathNotContainsSomething() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get all the fileDocumentList where filePath does not contain DEFAULT_FILE_PATH
        defaultFileDocumentShouldNotBeFound("filePath.doesNotContain=" + DEFAULT_FILE_PATH);

        // Get all the fileDocumentList where filePath does not contain UPDATED_FILE_PATH
        defaultFileDocumentShouldBeFound("filePath.doesNotContain=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllFileDocumentsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get all the fileDocumentList where description equals to DEFAULT_DESCRIPTION
        defaultFileDocumentShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the fileDocumentList where description equals to UPDATED_DESCRIPTION
        defaultFileDocumentShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFileDocumentsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get all the fileDocumentList where description not equals to DEFAULT_DESCRIPTION
        defaultFileDocumentShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the fileDocumentList where description not equals to UPDATED_DESCRIPTION
        defaultFileDocumentShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFileDocumentsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get all the fileDocumentList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFileDocumentShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the fileDocumentList where description equals to UPDATED_DESCRIPTION
        defaultFileDocumentShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFileDocumentsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get all the fileDocumentList where description is not null
        defaultFileDocumentShouldBeFound("description.specified=true");

        // Get all the fileDocumentList where description is null
        defaultFileDocumentShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllFileDocumentsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get all the fileDocumentList where description contains DEFAULT_DESCRIPTION
        defaultFileDocumentShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the fileDocumentList where description contains UPDATED_DESCRIPTION
        defaultFileDocumentShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFileDocumentsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        // Get all the fileDocumentList where description does not contain DEFAULT_DESCRIPTION
        defaultFileDocumentShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the fileDocumentList where description does not contain UPDATED_DESCRIPTION
        defaultFileDocumentShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFileDocumentsByPartyIsEqualToSomething() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);
        PartyEntity party = PartyResourceIT.createEntity(em);
        em.persist(party);
        em.flush();
        fileDocumentEntity.setParty(party);
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);
        Long partyId = party.getId();

        // Get all the fileDocumentList where party equals to partyId
        defaultFileDocumentShouldBeFound("partyId.equals=" + partyId);

        // Get all the fileDocumentList where party equals to (partyId + 1)
        defaultFileDocumentShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFileDocumentShouldBeFound(String filter) throws Exception {
        restFileDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileDocumentEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileContentContentType").value(hasItem(DEFAULT_FILE_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileContent").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_CONTENT))))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restFileDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFileDocumentShouldNotBeFound(String filter) throws Exception {
        restFileDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFileDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFileDocument() throws Exception {
        // Get the fileDocument
        restFileDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFileDocument() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        int databaseSizeBeforeUpdate = fileDocumentRepository.findAll().size();

        // Update the fileDocument
        FileDocumentEntity updatedFileDocumentEntity = fileDocumentRepository.findById(fileDocumentEntity.getId()).get();
        // Disconnect from session so that the updates on updatedFileDocumentEntity are not directly saved in db
        em.detach(updatedFileDocumentEntity);
        updatedFileDocumentEntity
            .fileName(UPDATED_FILE_NAME)
            .fileContent(UPDATED_FILE_CONTENT)
            .fileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE)
            .filePath(UPDATED_FILE_PATH)
            .description(UPDATED_DESCRIPTION);
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(updatedFileDocumentEntity);

        restFileDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            )
            .andExpect(status().isOk());

        // Validate the FileDocument in the database
        List<FileDocumentEntity> fileDocumentList = fileDocumentRepository.findAll();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeUpdate);
        FileDocumentEntity testFileDocument = fileDocumentList.get(fileDocumentList.size() - 1);
        assertThat(testFileDocument.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testFileDocument.getFileContent()).isEqualTo(UPDATED_FILE_CONTENT);
        assertThat(testFileDocument.getFileContentContentType()).isEqualTo(UPDATED_FILE_CONTENT_CONTENT_TYPE);
        assertThat(testFileDocument.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testFileDocument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingFileDocument() throws Exception {
        int databaseSizeBeforeUpdate = fileDocumentRepository.findAll().size();
        fileDocumentEntity.setId(count.incrementAndGet());

        // Create the FileDocument
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocumentEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileDocument in the database
        List<FileDocumentEntity> fileDocumentList = fileDocumentRepository.findAll();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFileDocument() throws Exception {
        int databaseSizeBeforeUpdate = fileDocumentRepository.findAll().size();
        fileDocumentEntity.setId(count.incrementAndGet());

        // Create the FileDocument
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocumentEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileDocument in the database
        List<FileDocumentEntity> fileDocumentList = fileDocumentRepository.findAll();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFileDocument() throws Exception {
        int databaseSizeBeforeUpdate = fileDocumentRepository.findAll().size();
        fileDocumentEntity.setId(count.incrementAndGet());

        // Create the FileDocument
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocumentEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileDocumentMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileDocument in the database
        List<FileDocumentEntity> fileDocumentList = fileDocumentRepository.findAll();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFileDocumentWithPatch() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        int databaseSizeBeforeUpdate = fileDocumentRepository.findAll().size();

        // Update the fileDocument using partial update
        FileDocumentEntity partialUpdatedFileDocumentEntity = new FileDocumentEntity();
        partialUpdatedFileDocumentEntity.setId(fileDocumentEntity.getId());

        partialUpdatedFileDocumentEntity.fileName(UPDATED_FILE_NAME).description(UPDATED_DESCRIPTION);

        restFileDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileDocumentEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileDocumentEntity))
            )
            .andExpect(status().isOk());

        // Validate the FileDocument in the database
        List<FileDocumentEntity> fileDocumentList = fileDocumentRepository.findAll();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeUpdate);
        FileDocumentEntity testFileDocument = fileDocumentList.get(fileDocumentList.size() - 1);
        assertThat(testFileDocument.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testFileDocument.getFileContent()).isEqualTo(DEFAULT_FILE_CONTENT);
        assertThat(testFileDocument.getFileContentContentType()).isEqualTo(DEFAULT_FILE_CONTENT_CONTENT_TYPE);
        assertThat(testFileDocument.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testFileDocument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateFileDocumentWithPatch() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        int databaseSizeBeforeUpdate = fileDocumentRepository.findAll().size();

        // Update the fileDocument using partial update
        FileDocumentEntity partialUpdatedFileDocumentEntity = new FileDocumentEntity();
        partialUpdatedFileDocumentEntity.setId(fileDocumentEntity.getId());

        partialUpdatedFileDocumentEntity
            .fileName(UPDATED_FILE_NAME)
            .fileContent(UPDATED_FILE_CONTENT)
            .fileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE)
            .filePath(UPDATED_FILE_PATH)
            .description(UPDATED_DESCRIPTION);

        restFileDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileDocumentEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileDocumentEntity))
            )
            .andExpect(status().isOk());

        // Validate the FileDocument in the database
        List<FileDocumentEntity> fileDocumentList = fileDocumentRepository.findAll();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeUpdate);
        FileDocumentEntity testFileDocument = fileDocumentList.get(fileDocumentList.size() - 1);
        assertThat(testFileDocument.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testFileDocument.getFileContent()).isEqualTo(UPDATED_FILE_CONTENT);
        assertThat(testFileDocument.getFileContentContentType()).isEqualTo(UPDATED_FILE_CONTENT_CONTENT_TYPE);
        assertThat(testFileDocument.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testFileDocument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingFileDocument() throws Exception {
        int databaseSizeBeforeUpdate = fileDocumentRepository.findAll().size();
        fileDocumentEntity.setId(count.incrementAndGet());

        // Create the FileDocument
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocumentEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fileDocumentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileDocument in the database
        List<FileDocumentEntity> fileDocumentList = fileDocumentRepository.findAll();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFileDocument() throws Exception {
        int databaseSizeBeforeUpdate = fileDocumentRepository.findAll().size();
        fileDocumentEntity.setId(count.incrementAndGet());

        // Create the FileDocument
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocumentEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileDocument in the database
        List<FileDocumentEntity> fileDocumentList = fileDocumentRepository.findAll();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFileDocument() throws Exception {
        int databaseSizeBeforeUpdate = fileDocumentRepository.findAll().size();
        fileDocumentEntity.setId(count.incrementAndGet());

        // Create the FileDocument
        FileDocumentDTO fileDocumentDTO = fileDocumentMapper.toDto(fileDocumentEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileDocumentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileDocument in the database
        List<FileDocumentEntity> fileDocumentList = fileDocumentRepository.findAll();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFileDocument() throws Exception {
        // Initialize the database
        fileDocumentRepository.saveAndFlush(fileDocumentEntity);

        int databaseSizeBeforeDelete = fileDocumentRepository.findAll().size();

        // Delete the fileDocument
        restFileDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, fileDocumentEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FileDocumentEntity> fileDocumentList = fileDocumentRepository.findAll();
        assertThat(fileDocumentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
