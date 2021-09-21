package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LessonTimetable;
import com.mycompany.myapp.domain.LessonTimetableTeacher;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.LessonTimetableTeacherRepository;
import com.mycompany.myapp.service.criteria.LessonTimetableTeacherCriteria;
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
 * Integration tests for the {@link LessonTimetableTeacherResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LessonTimetableTeacherResourceIT {

    private static final Float DEFAULT_PAY = 1F;
    private static final Float UPDATED_PAY = 2F;
    private static final Float SMALLER_PAY = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/lesson-timetable-teachers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LessonTimetableTeacherRepository lessonTimetableTeacherRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLessonTimetableTeacherMockMvc;

    private LessonTimetableTeacher lessonTimetableTeacher;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LessonTimetableTeacher createEntity(EntityManager em) {
        LessonTimetableTeacher lessonTimetableTeacher = new LessonTimetableTeacher().pay(DEFAULT_PAY);
        return lessonTimetableTeacher;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LessonTimetableTeacher createUpdatedEntity(EntityManager em) {
        LessonTimetableTeacher lessonTimetableTeacher = new LessonTimetableTeacher().pay(UPDATED_PAY);
        return lessonTimetableTeacher;
    }

    @BeforeEach
    public void initTest() {
        lessonTimetableTeacher = createEntity(em);
    }

    @Test
    @Transactional
    void createLessonTimetableTeacher() throws Exception {
        int databaseSizeBeforeCreate = lessonTimetableTeacherRepository.findAll().size();
        // Create the LessonTimetableTeacher
        restLessonTimetableTeacherMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetableTeacher))
            )
            .andExpect(status().isCreated());

        // Validate the LessonTimetableTeacher in the database
        List<LessonTimetableTeacher> lessonTimetableTeacherList = lessonTimetableTeacherRepository.findAll();
        assertThat(lessonTimetableTeacherList).hasSize(databaseSizeBeforeCreate + 1);
        LessonTimetableTeacher testLessonTimetableTeacher = lessonTimetableTeacherList.get(lessonTimetableTeacherList.size() - 1);
        assertThat(testLessonTimetableTeacher.getPay()).isEqualTo(DEFAULT_PAY);
    }

    @Test
    @Transactional
    void createLessonTimetableTeacherWithExistingId() throws Exception {
        // Create the LessonTimetableTeacher with an existing ID
        lessonTimetableTeacher.setId(1L);

        int databaseSizeBeforeCreate = lessonTimetableTeacherRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLessonTimetableTeacherMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetableTeacher))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTimetableTeacher in the database
        List<LessonTimetableTeacher> lessonTimetableTeacherList = lessonTimetableTeacherRepository.findAll();
        assertThat(lessonTimetableTeacherList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPayIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonTimetableTeacherRepository.findAll().size();
        // set the field null
        lessonTimetableTeacher.setPay(null);

        // Create the LessonTimetableTeacher, which fails.

        restLessonTimetableTeacherMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetableTeacher))
            )
            .andExpect(status().isBadRequest());

        List<LessonTimetableTeacher> lessonTimetableTeacherList = lessonTimetableTeacherRepository.findAll();
        assertThat(lessonTimetableTeacherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLessonTimetableTeachers() throws Exception {
        // Initialize the database
        lessonTimetableTeacherRepository.saveAndFlush(lessonTimetableTeacher);

        // Get all the lessonTimetableTeacherList
        restLessonTimetableTeacherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lessonTimetableTeacher.getId().intValue())))
            .andExpect(jsonPath("$.[*].pay").value(hasItem(DEFAULT_PAY.doubleValue())));
    }

    @Test
    @Transactional
    void getLessonTimetableTeacher() throws Exception {
        // Initialize the database
        lessonTimetableTeacherRepository.saveAndFlush(lessonTimetableTeacher);

        // Get the lessonTimetableTeacher
        restLessonTimetableTeacherMockMvc
            .perform(get(ENTITY_API_URL_ID, lessonTimetableTeacher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lessonTimetableTeacher.getId().intValue()))
            .andExpect(jsonPath("$.pay").value(DEFAULT_PAY.doubleValue()));
    }

    @Test
    @Transactional
    void getLessonTimetableTeachersByIdFiltering() throws Exception {
        // Initialize the database
        lessonTimetableTeacherRepository.saveAndFlush(lessonTimetableTeacher);

        Long id = lessonTimetableTeacher.getId();

        defaultLessonTimetableTeacherShouldBeFound("id.equals=" + id);
        defaultLessonTimetableTeacherShouldNotBeFound("id.notEquals=" + id);

        defaultLessonTimetableTeacherShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLessonTimetableTeacherShouldNotBeFound("id.greaterThan=" + id);

        defaultLessonTimetableTeacherShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLessonTimetableTeacherShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLessonTimetableTeachersByPayIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableTeacherRepository.saveAndFlush(lessonTimetableTeacher);

        // Get all the lessonTimetableTeacherList where pay equals to DEFAULT_PAY
        defaultLessonTimetableTeacherShouldBeFound("pay.equals=" + DEFAULT_PAY);

        // Get all the lessonTimetableTeacherList where pay equals to UPDATED_PAY
        defaultLessonTimetableTeacherShouldNotBeFound("pay.equals=" + UPDATED_PAY);
    }

    @Test
    @Transactional
    void getAllLessonTimetableTeachersByPayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableTeacherRepository.saveAndFlush(lessonTimetableTeacher);

        // Get all the lessonTimetableTeacherList where pay not equals to DEFAULT_PAY
        defaultLessonTimetableTeacherShouldNotBeFound("pay.notEquals=" + DEFAULT_PAY);

        // Get all the lessonTimetableTeacherList where pay not equals to UPDATED_PAY
        defaultLessonTimetableTeacherShouldBeFound("pay.notEquals=" + UPDATED_PAY);
    }

    @Test
    @Transactional
    void getAllLessonTimetableTeachersByPayIsInShouldWork() throws Exception {
        // Initialize the database
        lessonTimetableTeacherRepository.saveAndFlush(lessonTimetableTeacher);

        // Get all the lessonTimetableTeacherList where pay in DEFAULT_PAY or UPDATED_PAY
        defaultLessonTimetableTeacherShouldBeFound("pay.in=" + DEFAULT_PAY + "," + UPDATED_PAY);

        // Get all the lessonTimetableTeacherList where pay equals to UPDATED_PAY
        defaultLessonTimetableTeacherShouldNotBeFound("pay.in=" + UPDATED_PAY);
    }

    @Test
    @Transactional
    void getAllLessonTimetableTeachersByPayIsNullOrNotNull() throws Exception {
        // Initialize the database
        lessonTimetableTeacherRepository.saveAndFlush(lessonTimetableTeacher);

        // Get all the lessonTimetableTeacherList where pay is not null
        defaultLessonTimetableTeacherShouldBeFound("pay.specified=true");

        // Get all the lessonTimetableTeacherList where pay is null
        defaultLessonTimetableTeacherShouldNotBeFound("pay.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonTimetableTeachersByPayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableTeacherRepository.saveAndFlush(lessonTimetableTeacher);

        // Get all the lessonTimetableTeacherList where pay is greater than or equal to DEFAULT_PAY
        defaultLessonTimetableTeacherShouldBeFound("pay.greaterThanOrEqual=" + DEFAULT_PAY);

        // Get all the lessonTimetableTeacherList where pay is greater than or equal to UPDATED_PAY
        defaultLessonTimetableTeacherShouldNotBeFound("pay.greaterThanOrEqual=" + UPDATED_PAY);
    }

    @Test
    @Transactional
    void getAllLessonTimetableTeachersByPayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableTeacherRepository.saveAndFlush(lessonTimetableTeacher);

        // Get all the lessonTimetableTeacherList where pay is less than or equal to DEFAULT_PAY
        defaultLessonTimetableTeacherShouldBeFound("pay.lessThanOrEqual=" + DEFAULT_PAY);

        // Get all the lessonTimetableTeacherList where pay is less than or equal to SMALLER_PAY
        defaultLessonTimetableTeacherShouldNotBeFound("pay.lessThanOrEqual=" + SMALLER_PAY);
    }

    @Test
    @Transactional
    void getAllLessonTimetableTeachersByPayIsLessThanSomething() throws Exception {
        // Initialize the database
        lessonTimetableTeacherRepository.saveAndFlush(lessonTimetableTeacher);

        // Get all the lessonTimetableTeacherList where pay is less than DEFAULT_PAY
        defaultLessonTimetableTeacherShouldNotBeFound("pay.lessThan=" + DEFAULT_PAY);

        // Get all the lessonTimetableTeacherList where pay is less than UPDATED_PAY
        defaultLessonTimetableTeacherShouldBeFound("pay.lessThan=" + UPDATED_PAY);
    }

    @Test
    @Transactional
    void getAllLessonTimetableTeachersByPayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        lessonTimetableTeacherRepository.saveAndFlush(lessonTimetableTeacher);

        // Get all the lessonTimetableTeacherList where pay is greater than DEFAULT_PAY
        defaultLessonTimetableTeacherShouldNotBeFound("pay.greaterThan=" + DEFAULT_PAY);

        // Get all the lessonTimetableTeacherList where pay is greater than SMALLER_PAY
        defaultLessonTimetableTeacherShouldBeFound("pay.greaterThan=" + SMALLER_PAY);
    }

    @Test
    @Transactional
    void getAllLessonTimetableTeachersByLessonTimetableIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableTeacherRepository.saveAndFlush(lessonTimetableTeacher);
        LessonTimetable lessonTimetable = LessonTimetableResourceIT.createEntity(em);
        em.persist(lessonTimetable);
        em.flush();
        lessonTimetableTeacher.setLessonTimetable(lessonTimetable);
        lessonTimetableTeacherRepository.saveAndFlush(lessonTimetableTeacher);
        Long lessonTimetableId = lessonTimetable.getId();

        // Get all the lessonTimetableTeacherList where lessonTimetable equals to lessonTimetableId
        defaultLessonTimetableTeacherShouldBeFound("lessonTimetableId.equals=" + lessonTimetableId);

        // Get all the lessonTimetableTeacherList where lessonTimetable equals to (lessonTimetableId + 1)
        defaultLessonTimetableTeacherShouldNotBeFound("lessonTimetableId.equals=" + (lessonTimetableId + 1));
    }

    @Test
    @Transactional
    void getAllLessonTimetableTeachersByInternalUserIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableTeacherRepository.saveAndFlush(lessonTimetableTeacher);
        User internalUser = UserResourceIT.createEntity(em);
        em.persist(internalUser);
        em.flush();
        lessonTimetableTeacher.setInternalUser(internalUser);
        lessonTimetableTeacherRepository.saveAndFlush(lessonTimetableTeacher);
        Long internalUserId = internalUser.getId();

        // Get all the lessonTimetableTeacherList where internalUser equals to internalUserId
        defaultLessonTimetableTeacherShouldBeFound("internalUserId.equals=" + internalUserId);

        // Get all the lessonTimetableTeacherList where internalUser equals to (internalUserId + 1)
        defaultLessonTimetableTeacherShouldNotBeFound("internalUserId.equals=" + (internalUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLessonTimetableTeacherShouldBeFound(String filter) throws Exception {
        restLessonTimetableTeacherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lessonTimetableTeacher.getId().intValue())))
            .andExpect(jsonPath("$.[*].pay").value(hasItem(DEFAULT_PAY.doubleValue())));

        // Check, that the count call also returns 1
        restLessonTimetableTeacherMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLessonTimetableTeacherShouldNotBeFound(String filter) throws Exception {
        restLessonTimetableTeacherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLessonTimetableTeacherMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLessonTimetableTeacher() throws Exception {
        // Get the lessonTimetableTeacher
        restLessonTimetableTeacherMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLessonTimetableTeacher() throws Exception {
        // Initialize the database
        lessonTimetableTeacherRepository.saveAndFlush(lessonTimetableTeacher);

        int databaseSizeBeforeUpdate = lessonTimetableTeacherRepository.findAll().size();

        // Update the lessonTimetableTeacher
        LessonTimetableTeacher updatedLessonTimetableTeacher = lessonTimetableTeacherRepository
            .findById(lessonTimetableTeacher.getId())
            .get();
        // Disconnect from session so that the updates on updatedLessonTimetableTeacher are not directly saved in db
        em.detach(updatedLessonTimetableTeacher);
        updatedLessonTimetableTeacher.pay(UPDATED_PAY);

        restLessonTimetableTeacherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLessonTimetableTeacher.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLessonTimetableTeacher))
            )
            .andExpect(status().isOk());

        // Validate the LessonTimetableTeacher in the database
        List<LessonTimetableTeacher> lessonTimetableTeacherList = lessonTimetableTeacherRepository.findAll();
        assertThat(lessonTimetableTeacherList).hasSize(databaseSizeBeforeUpdate);
        LessonTimetableTeacher testLessonTimetableTeacher = lessonTimetableTeacherList.get(lessonTimetableTeacherList.size() - 1);
        assertThat(testLessonTimetableTeacher.getPay()).isEqualTo(UPDATED_PAY);
    }

    @Test
    @Transactional
    void putNonExistingLessonTimetableTeacher() throws Exception {
        int databaseSizeBeforeUpdate = lessonTimetableTeacherRepository.findAll().size();
        lessonTimetableTeacher.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonTimetableTeacherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lessonTimetableTeacher.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetableTeacher))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTimetableTeacher in the database
        List<LessonTimetableTeacher> lessonTimetableTeacherList = lessonTimetableTeacherRepository.findAll();
        assertThat(lessonTimetableTeacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLessonTimetableTeacher() throws Exception {
        int databaseSizeBeforeUpdate = lessonTimetableTeacherRepository.findAll().size();
        lessonTimetableTeacher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonTimetableTeacherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetableTeacher))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTimetableTeacher in the database
        List<LessonTimetableTeacher> lessonTimetableTeacherList = lessonTimetableTeacherRepository.findAll();
        assertThat(lessonTimetableTeacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLessonTimetableTeacher() throws Exception {
        int databaseSizeBeforeUpdate = lessonTimetableTeacherRepository.findAll().size();
        lessonTimetableTeacher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonTimetableTeacherMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetableTeacher))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LessonTimetableTeacher in the database
        List<LessonTimetableTeacher> lessonTimetableTeacherList = lessonTimetableTeacherRepository.findAll();
        assertThat(lessonTimetableTeacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLessonTimetableTeacherWithPatch() throws Exception {
        // Initialize the database
        lessonTimetableTeacherRepository.saveAndFlush(lessonTimetableTeacher);

        int databaseSizeBeforeUpdate = lessonTimetableTeacherRepository.findAll().size();

        // Update the lessonTimetableTeacher using partial update
        LessonTimetableTeacher partialUpdatedLessonTimetableTeacher = new LessonTimetableTeacher();
        partialUpdatedLessonTimetableTeacher.setId(lessonTimetableTeacher.getId());

        partialUpdatedLessonTimetableTeacher.pay(UPDATED_PAY);

        restLessonTimetableTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLessonTimetableTeacher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLessonTimetableTeacher))
            )
            .andExpect(status().isOk());

        // Validate the LessonTimetableTeacher in the database
        List<LessonTimetableTeacher> lessonTimetableTeacherList = lessonTimetableTeacherRepository.findAll();
        assertThat(lessonTimetableTeacherList).hasSize(databaseSizeBeforeUpdate);
        LessonTimetableTeacher testLessonTimetableTeacher = lessonTimetableTeacherList.get(lessonTimetableTeacherList.size() - 1);
        assertThat(testLessonTimetableTeacher.getPay()).isEqualTo(UPDATED_PAY);
    }

    @Test
    @Transactional
    void fullUpdateLessonTimetableTeacherWithPatch() throws Exception {
        // Initialize the database
        lessonTimetableTeacherRepository.saveAndFlush(lessonTimetableTeacher);

        int databaseSizeBeforeUpdate = lessonTimetableTeacherRepository.findAll().size();

        // Update the lessonTimetableTeacher using partial update
        LessonTimetableTeacher partialUpdatedLessonTimetableTeacher = new LessonTimetableTeacher();
        partialUpdatedLessonTimetableTeacher.setId(lessonTimetableTeacher.getId());

        partialUpdatedLessonTimetableTeacher.pay(UPDATED_PAY);

        restLessonTimetableTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLessonTimetableTeacher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLessonTimetableTeacher))
            )
            .andExpect(status().isOk());

        // Validate the LessonTimetableTeacher in the database
        List<LessonTimetableTeacher> lessonTimetableTeacherList = lessonTimetableTeacherRepository.findAll();
        assertThat(lessonTimetableTeacherList).hasSize(databaseSizeBeforeUpdate);
        LessonTimetableTeacher testLessonTimetableTeacher = lessonTimetableTeacherList.get(lessonTimetableTeacherList.size() - 1);
        assertThat(testLessonTimetableTeacher.getPay()).isEqualTo(UPDATED_PAY);
    }

    @Test
    @Transactional
    void patchNonExistingLessonTimetableTeacher() throws Exception {
        int databaseSizeBeforeUpdate = lessonTimetableTeacherRepository.findAll().size();
        lessonTimetableTeacher.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonTimetableTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lessonTimetableTeacher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetableTeacher))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTimetableTeacher in the database
        List<LessonTimetableTeacher> lessonTimetableTeacherList = lessonTimetableTeacherRepository.findAll();
        assertThat(lessonTimetableTeacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLessonTimetableTeacher() throws Exception {
        int databaseSizeBeforeUpdate = lessonTimetableTeacherRepository.findAll().size();
        lessonTimetableTeacher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonTimetableTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetableTeacher))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTimetableTeacher in the database
        List<LessonTimetableTeacher> lessonTimetableTeacherList = lessonTimetableTeacherRepository.findAll();
        assertThat(lessonTimetableTeacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLessonTimetableTeacher() throws Exception {
        int databaseSizeBeforeUpdate = lessonTimetableTeacherRepository.findAll().size();
        lessonTimetableTeacher.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonTimetableTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetableTeacher))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LessonTimetableTeacher in the database
        List<LessonTimetableTeacher> lessonTimetableTeacherList = lessonTimetableTeacherRepository.findAll();
        assertThat(lessonTimetableTeacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLessonTimetableTeacher() throws Exception {
        // Initialize the database
        lessonTimetableTeacherRepository.saveAndFlush(lessonTimetableTeacher);

        int databaseSizeBeforeDelete = lessonTimetableTeacherRepository.findAll().size();

        // Delete the lessonTimetableTeacher
        restLessonTimetableTeacherMockMvc
            .perform(delete(ENTITY_API_URL_ID, lessonTimetableTeacher.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LessonTimetableTeacher> lessonTimetableTeacherList = lessonTimetableTeacherRepository.findAll();
        assertThat(lessonTimetableTeacherList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
