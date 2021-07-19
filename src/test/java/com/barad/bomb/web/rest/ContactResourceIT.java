package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.ContactEntity;
import com.barad.bomb.domain.PartyEntity;
import com.barad.bomb.domain.enumeration.ContactType;
import com.barad.bomb.repository.ContactRepository;
import com.barad.bomb.service.criteria.ContactCriteria;
import com.barad.bomb.service.dto.ContactDTO;
import com.barad.bomb.service.mapper.ContactMapper;
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
 * Integration tests for the {@link ContactResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final ContactType DEFAULT_CONTACT_TYPE = ContactType.TELEPHONE;
    private static final ContactType UPDATED_CONTACT_TYPE = ContactType.MOBILE;

    private static final String DEFAULT_CONTACT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contacts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactMapper contactMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactMockMvc;

    private ContactEntity contactEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactEntity createEntity(EntityManager em) {
        ContactEntity contactEntity = new ContactEntity()
            .title(DEFAULT_TITLE)
            .contactType(DEFAULT_CONTACT_TYPE)
            .contactValue(DEFAULT_CONTACT_VALUE);
        // Add required entity
        PartyEntity party;
        if (TestUtil.findAll(em, PartyEntity.class).isEmpty()) {
            party = PartyResourceIT.createEntity(em);
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, PartyEntity.class).get(0);
        }
        contactEntity.setParty(party);
        return contactEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactEntity createUpdatedEntity(EntityManager em) {
        ContactEntity contactEntity = new ContactEntity()
            .title(UPDATED_TITLE)
            .contactType(UPDATED_CONTACT_TYPE)
            .contactValue(UPDATED_CONTACT_VALUE);
        // Add required entity
        PartyEntity party;
        if (TestUtil.findAll(em, PartyEntity.class).isEmpty()) {
            party = PartyResourceIT.createUpdatedEntity(em);
            em.persist(party);
            em.flush();
        } else {
            party = TestUtil.findAll(em, PartyEntity.class).get(0);
        }
        contactEntity.setParty(party);
        return contactEntity;
    }

    @BeforeEach
    public void initTest() {
        contactEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createContact() throws Exception {
        int databaseSizeBeforeCreate = contactRepository.findAll().size();
        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contactEntity);
        restContactMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isCreated());

        // Validate the Contact in the database
        List<ContactEntity> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate + 1);
        ContactEntity testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testContact.getContactType()).isEqualTo(DEFAULT_CONTACT_TYPE);
        assertThat(testContact.getContactValue()).isEqualTo(DEFAULT_CONTACT_VALUE);
    }

    @Test
    @Transactional
    void createContactWithExistingId() throws Exception {
        // Create the Contact with an existing ID
        contactEntity.setId(1L);
        ContactDTO contactDTO = contactMapper.toDto(contactEntity);

        int databaseSizeBeforeCreate = contactRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<ContactEntity> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactRepository.findAll().size();
        // set the field null
        contactEntity.setTitle(null);

        // Create the Contact, which fails.
        ContactDTO contactDTO = contactMapper.toDto(contactEntity);

        restContactMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isBadRequest());

        List<ContactEntity> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContactTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactRepository.findAll().size();
        // set the field null
        contactEntity.setContactType(null);

        // Create the Contact, which fails.
        ContactDTO contactDTO = contactMapper.toDto(contactEntity);

        restContactMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isBadRequest());

        List<ContactEntity> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContactValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactRepository.findAll().size();
        // set the field null
        contactEntity.setContactValue(null);

        // Create the Contact, which fails.
        ContactDTO contactDTO = contactMapper.toDto(contactEntity);

        restContactMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isBadRequest());

        List<ContactEntity> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContacts() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        // Get all the contactList
        restContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].contactType").value(hasItem(DEFAULT_CONTACT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].contactValue").value(hasItem(DEFAULT_CONTACT_VALUE)));
    }

    @Test
    @Transactional
    void getContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        // Get the contact
        restContactMockMvc
            .perform(get(ENTITY_API_URL_ID, contactEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactEntity.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.contactType").value(DEFAULT_CONTACT_TYPE.toString()))
            .andExpect(jsonPath("$.contactValue").value(DEFAULT_CONTACT_VALUE));
    }

    @Test
    @Transactional
    void getContactsByIdFiltering() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        Long id = contactEntity.getId();

        defaultContactShouldBeFound("id.equals=" + id);
        defaultContactShouldNotBeFound("id.notEquals=" + id);

        defaultContactShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContactShouldNotBeFound("id.greaterThan=" + id);

        defaultContactShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContactShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContactsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        // Get all the contactList where title equals to DEFAULT_TITLE
        defaultContactShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the contactList where title equals to UPDATED_TITLE
        defaultContactShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllContactsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        // Get all the contactList where title not equals to DEFAULT_TITLE
        defaultContactShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the contactList where title not equals to UPDATED_TITLE
        defaultContactShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllContactsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        // Get all the contactList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultContactShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the contactList where title equals to UPDATED_TITLE
        defaultContactShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllContactsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        // Get all the contactList where title is not null
        defaultContactShouldBeFound("title.specified=true");

        // Get all the contactList where title is null
        defaultContactShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByTitleContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        // Get all the contactList where title contains DEFAULT_TITLE
        defaultContactShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the contactList where title contains UPDATED_TITLE
        defaultContactShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllContactsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        // Get all the contactList where title does not contain DEFAULT_TITLE
        defaultContactShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the contactList where title does not contain UPDATED_TITLE
        defaultContactShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllContactsByContactTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        // Get all the contactList where contactType equals to DEFAULT_CONTACT_TYPE
        defaultContactShouldBeFound("contactType.equals=" + DEFAULT_CONTACT_TYPE);

        // Get all the contactList where contactType equals to UPDATED_CONTACT_TYPE
        defaultContactShouldNotBeFound("contactType.equals=" + UPDATED_CONTACT_TYPE);
    }

    @Test
    @Transactional
    void getAllContactsByContactTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        // Get all the contactList where contactType not equals to DEFAULT_CONTACT_TYPE
        defaultContactShouldNotBeFound("contactType.notEquals=" + DEFAULT_CONTACT_TYPE);

        // Get all the contactList where contactType not equals to UPDATED_CONTACT_TYPE
        defaultContactShouldBeFound("contactType.notEquals=" + UPDATED_CONTACT_TYPE);
    }

    @Test
    @Transactional
    void getAllContactsByContactTypeIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        // Get all the contactList where contactType in DEFAULT_CONTACT_TYPE or UPDATED_CONTACT_TYPE
        defaultContactShouldBeFound("contactType.in=" + DEFAULT_CONTACT_TYPE + "," + UPDATED_CONTACT_TYPE);

        // Get all the contactList where contactType equals to UPDATED_CONTACT_TYPE
        defaultContactShouldNotBeFound("contactType.in=" + UPDATED_CONTACT_TYPE);
    }

    @Test
    @Transactional
    void getAllContactsByContactTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        // Get all the contactList where contactType is not null
        defaultContactShouldBeFound("contactType.specified=true");

        // Get all the contactList where contactType is null
        defaultContactShouldNotBeFound("contactType.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByContactValueIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        // Get all the contactList where contactValue equals to DEFAULT_CONTACT_VALUE
        defaultContactShouldBeFound("contactValue.equals=" + DEFAULT_CONTACT_VALUE);

        // Get all the contactList where contactValue equals to UPDATED_CONTACT_VALUE
        defaultContactShouldNotBeFound("contactValue.equals=" + UPDATED_CONTACT_VALUE);
    }

    @Test
    @Transactional
    void getAllContactsByContactValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        // Get all the contactList where contactValue not equals to DEFAULT_CONTACT_VALUE
        defaultContactShouldNotBeFound("contactValue.notEquals=" + DEFAULT_CONTACT_VALUE);

        // Get all the contactList where contactValue not equals to UPDATED_CONTACT_VALUE
        defaultContactShouldBeFound("contactValue.notEquals=" + UPDATED_CONTACT_VALUE);
    }

    @Test
    @Transactional
    void getAllContactsByContactValueIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        // Get all the contactList where contactValue in DEFAULT_CONTACT_VALUE or UPDATED_CONTACT_VALUE
        defaultContactShouldBeFound("contactValue.in=" + DEFAULT_CONTACT_VALUE + "," + UPDATED_CONTACT_VALUE);

        // Get all the contactList where contactValue equals to UPDATED_CONTACT_VALUE
        defaultContactShouldNotBeFound("contactValue.in=" + UPDATED_CONTACT_VALUE);
    }

    @Test
    @Transactional
    void getAllContactsByContactValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        // Get all the contactList where contactValue is not null
        defaultContactShouldBeFound("contactValue.specified=true");

        // Get all the contactList where contactValue is null
        defaultContactShouldNotBeFound("contactValue.specified=false");
    }

    @Test
    @Transactional
    void getAllContactsByContactValueContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        // Get all the contactList where contactValue contains DEFAULT_CONTACT_VALUE
        defaultContactShouldBeFound("contactValue.contains=" + DEFAULT_CONTACT_VALUE);

        // Get all the contactList where contactValue contains UPDATED_CONTACT_VALUE
        defaultContactShouldNotBeFound("contactValue.contains=" + UPDATED_CONTACT_VALUE);
    }

    @Test
    @Transactional
    void getAllContactsByContactValueNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        // Get all the contactList where contactValue does not contain DEFAULT_CONTACT_VALUE
        defaultContactShouldNotBeFound("contactValue.doesNotContain=" + DEFAULT_CONTACT_VALUE);

        // Get all the contactList where contactValue does not contain UPDATED_CONTACT_VALUE
        defaultContactShouldBeFound("contactValue.doesNotContain=" + UPDATED_CONTACT_VALUE);
    }

    @Test
    @Transactional
    void getAllContactsByPartyIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);
        PartyEntity party = PartyResourceIT.createEntity(em);
        em.persist(party);
        em.flush();
        contactEntity.setParty(party);
        contactRepository.saveAndFlush(contactEntity);
        Long partyId = party.getId();

        // Get all the contactList where party equals to partyId
        defaultContactShouldBeFound("partyId.equals=" + partyId);

        // Get all the contactList where party equals to (partyId + 1)
        defaultContactShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContactShouldBeFound(String filter) throws Exception {
        restContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].contactType").value(hasItem(DEFAULT_CONTACT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].contactValue").value(hasItem(DEFAULT_CONTACT_VALUE)));

        // Check, that the count call also returns 1
        restContactMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContactShouldNotBeFound(String filter) throws Exception {
        restContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContactMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContact() throws Exception {
        // Get the contact
        restContactMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        int databaseSizeBeforeUpdate = contactRepository.findAll().size();

        // Update the contact
        ContactEntity updatedContactEntity = contactRepository.findById(contactEntity.getId()).get();
        // Disconnect from session so that the updates on updatedContactEntity are not directly saved in db
        em.detach(updatedContactEntity);
        updatedContactEntity.title(UPDATED_TITLE).contactType(UPDATED_CONTACT_TYPE).contactValue(UPDATED_CONTACT_VALUE);
        ContactDTO contactDTO = contactMapper.toDto(updatedContactEntity);

        restContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactDTO))
            )
            .andExpect(status().isOk());

        // Validate the Contact in the database
        List<ContactEntity> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
        ContactEntity testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContact.getContactType()).isEqualTo(UPDATED_CONTACT_TYPE);
        assertThat(testContact.getContactValue()).isEqualTo(UPDATED_CONTACT_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();
        contactEntity.setId(count.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contactEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<ContactEntity> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();
        contactEntity.setId(count.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contactEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<ContactEntity> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();
        contactEntity.setId(count.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contactEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contact in the database
        List<ContactEntity> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactWithPatch() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        int databaseSizeBeforeUpdate = contactRepository.findAll().size();

        // Update the contact using partial update
        ContactEntity partialUpdatedContactEntity = new ContactEntity();
        partialUpdatedContactEntity.setId(contactEntity.getId());

        partialUpdatedContactEntity.contactType(UPDATED_CONTACT_TYPE);

        restContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactEntity))
            )
            .andExpect(status().isOk());

        // Validate the Contact in the database
        List<ContactEntity> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
        ContactEntity testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testContact.getContactType()).isEqualTo(UPDATED_CONTACT_TYPE);
        assertThat(testContact.getContactValue()).isEqualTo(DEFAULT_CONTACT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateContactWithPatch() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        int databaseSizeBeforeUpdate = contactRepository.findAll().size();

        // Update the contact using partial update
        ContactEntity partialUpdatedContactEntity = new ContactEntity();
        partialUpdatedContactEntity.setId(contactEntity.getId());

        partialUpdatedContactEntity.title(UPDATED_TITLE).contactType(UPDATED_CONTACT_TYPE).contactValue(UPDATED_CONTACT_VALUE);

        restContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactEntity))
            )
            .andExpect(status().isOk());

        // Validate the Contact in the database
        List<ContactEntity> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
        ContactEntity testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContact.getContactType()).isEqualTo(UPDATED_CONTACT_TYPE);
        assertThat(testContact.getContactValue()).isEqualTo(UPDATED_CONTACT_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();
        contactEntity.setId(count.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contactEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<ContactEntity> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();
        contactEntity.setId(count.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contactEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<ContactEntity> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();
        contactEntity.setId(count.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contactEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contactDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contact in the database
        List<ContactEntity> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contactEntity);

        int databaseSizeBeforeDelete = contactRepository.findAll().size();

        // Delete the contact
        restContactMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactEntity> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
