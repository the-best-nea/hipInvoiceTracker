package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LessonInstance;
import com.mycompany.myapp.domain.LessonTimetable;
import com.mycompany.myapp.domain.Subject;
import com.mycompany.myapp.repository.SubjectRepository;
import com.mycompany.myapp.service.criteria.SubjectCriteria;
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
 * Integration tests for the {@link SubjectResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubjectResourceIT {

    private static final String DEFAULT_SUBJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/subjects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubjectMockMvc;

    private Subject subject;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subject createEntity(EntityManager em) {
        Subject subject = new Subject()
            .subjectName(DEFAULT_SUBJECT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .active(DEFAULT_ACTIVE);
        return subject;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subject createUpdatedEntity(EntityManager em) {
        Subject subject = new Subject()
            .subjectName(UPDATED_SUBJECT_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .active(UPDATED_ACTIVE);
        return subject;
    }

    @BeforeEach
    public void initTest() {
        subject = createEntity(em);
    }

    @Test
    @Transactional
    void createSubject() throws Exception {
        int databaseSizeBeforeCreate = subjectRepository.findAll().size();
        // Create the Subject
        restSubjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subject)))
            .andExpect(status().isCreated());

        // Validate the Subject in the database
        List<Subject> subjectList = subjectRepository.findAll();
        assertThat(subjectList).hasSize(databaseSizeBeforeCreate + 1);
        Subject testSubject = subjectList.get(subjectList.size() - 1);
        assertThat(testSubject.getSubjectName()).isEqualTo(DEFAULT_SUBJECT_NAME);
        assertThat(testSubject.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSubject.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSubject.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createSubjectWithExistingId() throws Exception {
        // Create the Subject with an existing ID
        subject.setId(1L);

        int databaseSizeBeforeCreate = subjectRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subject)))
            .andExpect(status().isBadRequest());

        // Validate the Subject in the database
        List<Subject> subjectList = subjectRepository.findAll();
        assertThat(subjectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSubjectNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = subjectRepository.findAll().size();
        // set the field null
        subject.setSubjectName(null);

        // Create the Subject, which fails.

        restSubjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subject)))
            .andExpect(status().isBadRequest());

        List<Subject> subjectList = subjectRepository.findAll();
        assertThat(subjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = subjectRepository.findAll().size();
        // set the field null
        subject.setCreatedAt(null);

        // Create the Subject, which fails.

        restSubjectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subject)))
            .andExpect(status().isBadRequest());

        List<Subject> subjectList = subjectRepository.findAll();
        assertThat(subjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubjects() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList
        restSubjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subject.getId().intValue())))
            .andExpect(jsonPath("$.[*].subjectName").value(hasItem(DEFAULT_SUBJECT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getSubject() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get the subject
        restSubjectMockMvc
            .perform(get(ENTITY_API_URL_ID, subject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subject.getId().intValue()))
            .andExpect(jsonPath("$.subjectName").value(DEFAULT_SUBJECT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getSubjectsByIdFiltering() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        Long id = subject.getId();

        defaultSubjectShouldBeFound("id.equals=" + id);
        defaultSubjectShouldNotBeFound("id.notEquals=" + id);

        defaultSubjectShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSubjectShouldNotBeFound("id.greaterThan=" + id);

        defaultSubjectShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSubjectShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSubjectsBySubjectNameIsEqualToSomething() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where subjectName equals to DEFAULT_SUBJECT_NAME
        defaultSubjectShouldBeFound("subjectName.equals=" + DEFAULT_SUBJECT_NAME);

        // Get all the subjectList where subjectName equals to UPDATED_SUBJECT_NAME
        defaultSubjectShouldNotBeFound("subjectName.equals=" + UPDATED_SUBJECT_NAME);
    }

    @Test
    @Transactional
    void getAllSubjectsBySubjectNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where subjectName not equals to DEFAULT_SUBJECT_NAME
        defaultSubjectShouldNotBeFound("subjectName.notEquals=" + DEFAULT_SUBJECT_NAME);

        // Get all the subjectList where subjectName not equals to UPDATED_SUBJECT_NAME
        defaultSubjectShouldBeFound("subjectName.notEquals=" + UPDATED_SUBJECT_NAME);
    }

    @Test
    @Transactional
    void getAllSubjectsBySubjectNameIsInShouldWork() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where subjectName in DEFAULT_SUBJECT_NAME or UPDATED_SUBJECT_NAME
        defaultSubjectShouldBeFound("subjectName.in=" + DEFAULT_SUBJECT_NAME + "," + UPDATED_SUBJECT_NAME);

        // Get all the subjectList where subjectName equals to UPDATED_SUBJECT_NAME
        defaultSubjectShouldNotBeFound("subjectName.in=" + UPDATED_SUBJECT_NAME);
    }

    @Test
    @Transactional
    void getAllSubjectsBySubjectNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where subjectName is not null
        defaultSubjectShouldBeFound("subjectName.specified=true");

        // Get all the subjectList where subjectName is null
        defaultSubjectShouldNotBeFound("subjectName.specified=false");
    }

    @Test
    @Transactional
    void getAllSubjectsBySubjectNameContainsSomething() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where subjectName contains DEFAULT_SUBJECT_NAME
        defaultSubjectShouldBeFound("subjectName.contains=" + DEFAULT_SUBJECT_NAME);

        // Get all the subjectList where subjectName contains UPDATED_SUBJECT_NAME
        defaultSubjectShouldNotBeFound("subjectName.contains=" + UPDATED_SUBJECT_NAME);
    }

    @Test
    @Transactional
    void getAllSubjectsBySubjectNameNotContainsSomething() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where subjectName does not contain DEFAULT_SUBJECT_NAME
        defaultSubjectShouldNotBeFound("subjectName.doesNotContain=" + DEFAULT_SUBJECT_NAME);

        // Get all the subjectList where subjectName does not contain UPDATED_SUBJECT_NAME
        defaultSubjectShouldBeFound("subjectName.doesNotContain=" + UPDATED_SUBJECT_NAME);
    }

    @Test
    @Transactional
    void getAllSubjectsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where description equals to DEFAULT_DESCRIPTION
        defaultSubjectShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the subjectList where description equals to UPDATED_DESCRIPTION
        defaultSubjectShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSubjectsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where description not equals to DEFAULT_DESCRIPTION
        defaultSubjectShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the subjectList where description not equals to UPDATED_DESCRIPTION
        defaultSubjectShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSubjectsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSubjectShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the subjectList where description equals to UPDATED_DESCRIPTION
        defaultSubjectShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSubjectsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where description is not null
        defaultSubjectShouldBeFound("description.specified=true");

        // Get all the subjectList where description is null
        defaultSubjectShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllSubjectsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where description contains DEFAULT_DESCRIPTION
        defaultSubjectShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the subjectList where description contains UPDATED_DESCRIPTION
        defaultSubjectShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSubjectsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where description does not contain DEFAULT_DESCRIPTION
        defaultSubjectShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the subjectList where description does not contain UPDATED_DESCRIPTION
        defaultSubjectShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSubjectsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where createdAt equals to DEFAULT_CREATED_AT
        defaultSubjectShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the subjectList where createdAt equals to UPDATED_CREATED_AT
        defaultSubjectShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSubjectsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where createdAt not equals to DEFAULT_CREATED_AT
        defaultSubjectShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the subjectList where createdAt not equals to UPDATED_CREATED_AT
        defaultSubjectShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSubjectsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultSubjectShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the subjectList where createdAt equals to UPDATED_CREATED_AT
        defaultSubjectShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllSubjectsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where createdAt is not null
        defaultSubjectShouldBeFound("createdAt.specified=true");

        // Get all the subjectList where createdAt is null
        defaultSubjectShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSubjectsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where active equals to DEFAULT_ACTIVE
        defaultSubjectShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the subjectList where active equals to UPDATED_ACTIVE
        defaultSubjectShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllSubjectsByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where active not equals to DEFAULT_ACTIVE
        defaultSubjectShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the subjectList where active not equals to UPDATED_ACTIVE
        defaultSubjectShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllSubjectsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultSubjectShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the subjectList where active equals to UPDATED_ACTIVE
        defaultSubjectShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllSubjectsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        // Get all the subjectList where active is not null
        defaultSubjectShouldBeFound("active.specified=true");

        // Get all the subjectList where active is null
        defaultSubjectShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    void getAllSubjectsByLessonTimetableIsEqualToSomething() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);
        LessonTimetable lessonTimetable = LessonTimetableResourceIT.createEntity(em);
        em.persist(lessonTimetable);
        em.flush();
        subject.addLessonTimetable(lessonTimetable);
        subjectRepository.saveAndFlush(subject);
        Long lessonTimetableId = lessonTimetable.getId();

        // Get all the subjectList where lessonTimetable equals to lessonTimetableId
        defaultSubjectShouldBeFound("lessonTimetableId.equals=" + lessonTimetableId);

        // Get all the subjectList where lessonTimetable equals to (lessonTimetableId + 1)
        defaultSubjectShouldNotBeFound("lessonTimetableId.equals=" + (lessonTimetableId + 1));
    }

    @Test
    @Transactional
    void getAllSubjectsByLessonInstanceIsEqualToSomething() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);
        LessonInstance lessonInstance = LessonInstanceResourceIT.createEntity(em);
        em.persist(lessonInstance);
        em.flush();
        subject.addLessonInstance(lessonInstance);
        subjectRepository.saveAndFlush(subject);
        Long lessonInstanceId = lessonInstance.getId();

        // Get all the subjectList where lessonInstance equals to lessonInstanceId
        defaultSubjectShouldBeFound("lessonInstanceId.equals=" + lessonInstanceId);

        // Get all the subjectList where lessonInstance equals to (lessonInstanceId + 1)
        defaultSubjectShouldNotBeFound("lessonInstanceId.equals=" + (lessonInstanceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSubjectShouldBeFound(String filter) throws Exception {
        restSubjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subject.getId().intValue())))
            .andExpect(jsonPath("$.[*].subjectName").value(hasItem(DEFAULT_SUBJECT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restSubjectMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSubjectShouldNotBeFound(String filter) throws Exception {
        restSubjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubjectMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSubject() throws Exception {
        // Get the subject
        restSubjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSubject() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        int databaseSizeBeforeUpdate = subjectRepository.findAll().size();

        // Update the subject
        Subject updatedSubject = subjectRepository.findById(subject.getId()).get();
        // Disconnect from session so that the updates on updatedSubject are not directly saved in db
        em.detach(updatedSubject);
        updatedSubject
            .subjectName(UPDATED_SUBJECT_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .active(UPDATED_ACTIVE);

        restSubjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSubject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSubject))
            )
            .andExpect(status().isOk());

        // Validate the Subject in the database
        List<Subject> subjectList = subjectRepository.findAll();
        assertThat(subjectList).hasSize(databaseSizeBeforeUpdate);
        Subject testSubject = subjectList.get(subjectList.size() - 1);
        assertThat(testSubject.getSubjectName()).isEqualTo(UPDATED_SUBJECT_NAME);
        assertThat(testSubject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSubject.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSubject.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingSubject() throws Exception {
        int databaseSizeBeforeUpdate = subjectRepository.findAll().size();
        subject.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subject))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subject in the database
        List<Subject> subjectList = subjectRepository.findAll();
        assertThat(subjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubject() throws Exception {
        int databaseSizeBeforeUpdate = subjectRepository.findAll().size();
        subject.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subject))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subject in the database
        List<Subject> subjectList = subjectRepository.findAll();
        assertThat(subjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubject() throws Exception {
        int databaseSizeBeforeUpdate = subjectRepository.findAll().size();
        subject.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subject)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Subject in the database
        List<Subject> subjectList = subjectRepository.findAll();
        assertThat(subjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubjectWithPatch() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        int databaseSizeBeforeUpdate = subjectRepository.findAll().size();

        // Update the subject using partial update
        Subject partialUpdatedSubject = new Subject();
        partialUpdatedSubject.setId(subject.getId());

        partialUpdatedSubject.subjectName(UPDATED_SUBJECT_NAME).active(UPDATED_ACTIVE);

        restSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubject))
            )
            .andExpect(status().isOk());

        // Validate the Subject in the database
        List<Subject> subjectList = subjectRepository.findAll();
        assertThat(subjectList).hasSize(databaseSizeBeforeUpdate);
        Subject testSubject = subjectList.get(subjectList.size() - 1);
        assertThat(testSubject.getSubjectName()).isEqualTo(UPDATED_SUBJECT_NAME);
        assertThat(testSubject.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSubject.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSubject.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateSubjectWithPatch() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        int databaseSizeBeforeUpdate = subjectRepository.findAll().size();

        // Update the subject using partial update
        Subject partialUpdatedSubject = new Subject();
        partialUpdatedSubject.setId(subject.getId());

        partialUpdatedSubject
            .subjectName(UPDATED_SUBJECT_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .active(UPDATED_ACTIVE);

        restSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubject))
            )
            .andExpect(status().isOk());

        // Validate the Subject in the database
        List<Subject> subjectList = subjectRepository.findAll();
        assertThat(subjectList).hasSize(databaseSizeBeforeUpdate);
        Subject testSubject = subjectList.get(subjectList.size() - 1);
        assertThat(testSubject.getSubjectName()).isEqualTo(UPDATED_SUBJECT_NAME);
        assertThat(testSubject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSubject.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSubject.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingSubject() throws Exception {
        int databaseSizeBeforeUpdate = subjectRepository.findAll().size();
        subject.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subject))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subject in the database
        List<Subject> subjectList = subjectRepository.findAll();
        assertThat(subjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubject() throws Exception {
        int databaseSizeBeforeUpdate = subjectRepository.findAll().size();
        subject.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subject))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subject in the database
        List<Subject> subjectList = subjectRepository.findAll();
        assertThat(subjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubject() throws Exception {
        int databaseSizeBeforeUpdate = subjectRepository.findAll().size();
        subject.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubjectMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(subject)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Subject in the database
        List<Subject> subjectList = subjectRepository.findAll();
        assertThat(subjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubject() throws Exception {
        // Initialize the database
        subjectRepository.saveAndFlush(subject);

        int databaseSizeBeforeDelete = subjectRepository.findAll().size();

        // Delete the subject
        restSubjectMockMvc
            .perform(delete(ENTITY_API_URL_ID, subject.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Subject> subjectList = subjectRepository.findAll();
        assertThat(subjectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
