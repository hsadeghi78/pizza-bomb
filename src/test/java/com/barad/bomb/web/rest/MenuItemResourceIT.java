package com.barad.bomb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.barad.bomb.IntegrationTest;
import com.barad.bomb.domain.FoodEntity;
import com.barad.bomb.domain.MenuItemEntity;
import com.barad.bomb.domain.PartyEntity;
import com.barad.bomb.repository.MenuItemRepository;
import com.barad.bomb.service.criteria.MenuItemCriteria;
import com.barad.bomb.service.dto.MenuItemDTO;
import com.barad.bomb.service.mapper.MenuItemMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link MenuItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MenuItemResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXPIRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/menu-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private MenuItemMapper menuItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMenuItemMockMvc;

    private MenuItemEntity menuItemEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuItemEntity createEntity(EntityManager em) {
        MenuItemEntity menuItemEntity = new MenuItemEntity()
            .title(DEFAULT_TITLE)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
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
        menuItemEntity.setParty(party);
        // Add required entity
        FoodEntity food;
        if (TestUtil.findAll(em, FoodEntity.class).isEmpty()) {
            food = FoodResourceIT.createEntity(em);
            em.persist(food);
            em.flush();
        } else {
            food = TestUtil.findAll(em, FoodEntity.class).get(0);
        }
        menuItemEntity.setFood(food);
        return menuItemEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuItemEntity createUpdatedEntity(EntityManager em) {
        MenuItemEntity menuItemEntity = new MenuItemEntity()
            .title(UPDATED_TITLE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
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
        menuItemEntity.setParty(party);
        // Add required entity
        FoodEntity food;
        if (TestUtil.findAll(em, FoodEntity.class).isEmpty()) {
            food = FoodResourceIT.createUpdatedEntity(em);
            em.persist(food);
            em.flush();
        } else {
            food = TestUtil.findAll(em, FoodEntity.class).get(0);
        }
        menuItemEntity.setFood(food);
        return menuItemEntity;
    }

    @BeforeEach
    public void initTest() {
        menuItemEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createMenuItem() throws Exception {
        int databaseSizeBeforeCreate = menuItemRepository.findAll().size();
        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItemEntity);
        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuItemDTO)))
            .andExpect(status().isCreated());

        // Validate the MenuItem in the database
        List<MenuItemEntity> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeCreate + 1);
        MenuItemEntity testMenuItem = menuItemList.get(menuItemList.size() - 1);
        assertThat(testMenuItem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMenuItem.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testMenuItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createMenuItemWithExistingId() throws Exception {
        // Create the MenuItem with an existing ID
        menuItemEntity.setId(1L);
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItemEntity);

        int databaseSizeBeforeCreate = menuItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        List<MenuItemEntity> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuItemRepository.findAll().size();
        // set the field null
        menuItemEntity.setTitle(null);

        // Create the MenuItem, which fails.
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItemEntity);

        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuItemDTO)))
            .andExpect(status().isBadRequest());

        List<MenuItemEntity> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpirationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = menuItemRepository.findAll().size();
        // set the field null
        menuItemEntity.setExpirationDate(null);

        // Create the MenuItem, which fails.
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItemEntity);

        restMenuItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuItemDTO)))
            .andExpect(status().isBadRequest());

        List<MenuItemEntity> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMenuItems() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        // Get all the menuItemList
        restMenuItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuItemEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        // Get the menuItem
        restMenuItemMockMvc
            .perform(get(ENTITY_API_URL_ID, menuItemEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(menuItemEntity.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getMenuItemsByIdFiltering() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        Long id = menuItemEntity.getId();

        defaultMenuItemShouldBeFound("id.equals=" + id);
        defaultMenuItemShouldNotBeFound("id.notEquals=" + id);

        defaultMenuItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMenuItemShouldNotBeFound("id.greaterThan=" + id);

        defaultMenuItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMenuItemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMenuItemsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        // Get all the menuItemList where title equals to DEFAULT_TITLE
        defaultMenuItemShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the menuItemList where title equals to UPDATED_TITLE
        defaultMenuItemShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMenuItemsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        // Get all the menuItemList where title not equals to DEFAULT_TITLE
        defaultMenuItemShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the menuItemList where title not equals to UPDATED_TITLE
        defaultMenuItemShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMenuItemsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        // Get all the menuItemList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultMenuItemShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the menuItemList where title equals to UPDATED_TITLE
        defaultMenuItemShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMenuItemsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        // Get all the menuItemList where title is not null
        defaultMenuItemShouldBeFound("title.specified=true");

        // Get all the menuItemList where title is null
        defaultMenuItemShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuItemsByTitleContainsSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        // Get all the menuItemList where title contains DEFAULT_TITLE
        defaultMenuItemShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the menuItemList where title contains UPDATED_TITLE
        defaultMenuItemShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMenuItemsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        // Get all the menuItemList where title does not contain DEFAULT_TITLE
        defaultMenuItemShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the menuItemList where title does not contain UPDATED_TITLE
        defaultMenuItemShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMenuItemsByExpirationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        // Get all the menuItemList where expirationDate equals to DEFAULT_EXPIRATION_DATE
        defaultMenuItemShouldBeFound("expirationDate.equals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the menuItemList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultMenuItemShouldNotBeFound("expirationDate.equals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllMenuItemsByExpirationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        // Get all the menuItemList where expirationDate not equals to DEFAULT_EXPIRATION_DATE
        defaultMenuItemShouldNotBeFound("expirationDate.notEquals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the menuItemList where expirationDate not equals to UPDATED_EXPIRATION_DATE
        defaultMenuItemShouldBeFound("expirationDate.notEquals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllMenuItemsByExpirationDateIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        // Get all the menuItemList where expirationDate in DEFAULT_EXPIRATION_DATE or UPDATED_EXPIRATION_DATE
        defaultMenuItemShouldBeFound("expirationDate.in=" + DEFAULT_EXPIRATION_DATE + "," + UPDATED_EXPIRATION_DATE);

        // Get all the menuItemList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultMenuItemShouldNotBeFound("expirationDate.in=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllMenuItemsByExpirationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        // Get all the menuItemList where expirationDate is not null
        defaultMenuItemShouldBeFound("expirationDate.specified=true");

        // Get all the menuItemList where expirationDate is null
        defaultMenuItemShouldNotBeFound("expirationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuItemsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        // Get all the menuItemList where description equals to DEFAULT_DESCRIPTION
        defaultMenuItemShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the menuItemList where description equals to UPDATED_DESCRIPTION
        defaultMenuItemShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMenuItemsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        // Get all the menuItemList where description not equals to DEFAULT_DESCRIPTION
        defaultMenuItemShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the menuItemList where description not equals to UPDATED_DESCRIPTION
        defaultMenuItemShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMenuItemsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        // Get all the menuItemList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMenuItemShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the menuItemList where description equals to UPDATED_DESCRIPTION
        defaultMenuItemShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMenuItemsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        // Get all the menuItemList where description is not null
        defaultMenuItemShouldBeFound("description.specified=true");

        // Get all the menuItemList where description is null
        defaultMenuItemShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllMenuItemsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        // Get all the menuItemList where description contains DEFAULT_DESCRIPTION
        defaultMenuItemShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the menuItemList where description contains UPDATED_DESCRIPTION
        defaultMenuItemShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMenuItemsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        // Get all the menuItemList where description does not contain DEFAULT_DESCRIPTION
        defaultMenuItemShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the menuItemList where description does not contain UPDATED_DESCRIPTION
        defaultMenuItemShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMenuItemsByPartyIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);
        PartyEntity party = PartyResourceIT.createEntity(em);
        em.persist(party);
        em.flush();
        menuItemEntity.setParty(party);
        menuItemRepository.saveAndFlush(menuItemEntity);
        Long partyId = party.getId();

        // Get all the menuItemList where party equals to partyId
        defaultMenuItemShouldBeFound("partyId.equals=" + partyId);

        // Get all the menuItemList where party equals to (partyId + 1)
        defaultMenuItemShouldNotBeFound("partyId.equals=" + (partyId + 1));
    }

    @Test
    @Transactional
    void getAllMenuItemsByFoodIsEqualToSomething() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);
        FoodEntity food = FoodResourceIT.createEntity(em);
        em.persist(food);
        em.flush();
        menuItemEntity.setFood(food);
        menuItemRepository.saveAndFlush(menuItemEntity);
        Long foodId = food.getId();

        // Get all the menuItemList where food equals to foodId
        defaultMenuItemShouldBeFound("foodId.equals=" + foodId);

        // Get all the menuItemList where food equals to (foodId + 1)
        defaultMenuItemShouldNotBeFound("foodId.equals=" + (foodId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMenuItemShouldBeFound(String filter) throws Exception {
        restMenuItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuItemEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restMenuItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMenuItemShouldNotBeFound(String filter) throws Exception {
        restMenuItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMenuItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMenuItem() throws Exception {
        // Get the menuItem
        restMenuItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();

        // Update the menuItem
        MenuItemEntity updatedMenuItemEntity = menuItemRepository.findById(menuItemEntity.getId()).get();
        // Disconnect from session so that the updates on updatedMenuItemEntity are not directly saved in db
        em.detach(updatedMenuItemEntity);
        updatedMenuItemEntity.title(UPDATED_TITLE).expirationDate(UPDATED_EXPIRATION_DATE).description(UPDATED_DESCRIPTION);
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(updatedMenuItemEntity);

        restMenuItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menuItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menuItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the MenuItem in the database
        List<MenuItemEntity> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
        MenuItemEntity testMenuItem = menuItemList.get(menuItemList.size() - 1);
        assertThat(testMenuItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMenuItem.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testMenuItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();
        menuItemEntity.setId(count.incrementAndGet());

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItemEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menuItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menuItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        List<MenuItemEntity> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();
        menuItemEntity.setId(count.incrementAndGet());

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItemEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(menuItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        List<MenuItemEntity> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();
        menuItemEntity.setId(count.incrementAndGet());

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItemEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(menuItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuItem in the database
        List<MenuItemEntity> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMenuItemWithPatch() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();

        // Update the menuItem using partial update
        MenuItemEntity partialUpdatedMenuItemEntity = new MenuItemEntity();
        partialUpdatedMenuItemEntity.setId(menuItemEntity.getId());

        partialUpdatedMenuItemEntity.expirationDate(UPDATED_EXPIRATION_DATE).description(UPDATED_DESCRIPTION);

        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuItemEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenuItemEntity))
            )
            .andExpect(status().isOk());

        // Validate the MenuItem in the database
        List<MenuItemEntity> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
        MenuItemEntity testMenuItem = menuItemList.get(menuItemList.size() - 1);
        assertThat(testMenuItem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMenuItem.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testMenuItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateMenuItemWithPatch() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();

        // Update the menuItem using partial update
        MenuItemEntity partialUpdatedMenuItemEntity = new MenuItemEntity();
        partialUpdatedMenuItemEntity.setId(menuItemEntity.getId());

        partialUpdatedMenuItemEntity.title(UPDATED_TITLE).expirationDate(UPDATED_EXPIRATION_DATE).description(UPDATED_DESCRIPTION);

        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuItemEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMenuItemEntity))
            )
            .andExpect(status().isOk());

        // Validate the MenuItem in the database
        List<MenuItemEntity> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
        MenuItemEntity testMenuItem = menuItemList.get(menuItemList.size() - 1);
        assertThat(testMenuItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMenuItem.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testMenuItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();
        menuItemEntity.setId(count.incrementAndGet());

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItemEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, menuItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menuItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        List<MenuItemEntity> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();
        menuItemEntity.setId(count.incrementAndGet());

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItemEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(menuItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuItem in the database
        List<MenuItemEntity> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMenuItem() throws Exception {
        int databaseSizeBeforeUpdate = menuItemRepository.findAll().size();
        menuItemEntity.setId(count.incrementAndGet());

        // Create the MenuItem
        MenuItemDTO menuItemDTO = menuItemMapper.toDto(menuItemEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(menuItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuItem in the database
        List<MenuItemEntity> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMenuItem() throws Exception {
        // Initialize the database
        menuItemRepository.saveAndFlush(menuItemEntity);

        int databaseSizeBeforeDelete = menuItemRepository.findAll().size();

        // Delete the menuItem
        restMenuItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, menuItemEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MenuItemEntity> menuItemList = menuItemRepository.findAll();
        assertThat(menuItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
