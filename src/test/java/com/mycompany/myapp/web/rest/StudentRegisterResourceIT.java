package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LessonInstance;
import com.mycompany.myapp.domain.Student;
import com.mycompany.myapp.domain.StudentRegister;
import com.mycompany.myapp.repository.StudentRegisterRepository;
import com.mycompany.myapp.service.criteria.StudentRegisterCriteria;
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
 * Integration tests for the {@link StudentRegisterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentRegisterResourceIT {

    private static final Instant DEFAULT_DATE_OF_LESSON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_OF_LESSON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Float DEFAULT_PAY = 1F;
    private static final Float UPDATED_PAY = 2F;
    private static final Float SMALLER_PAY = 1F - 1F;

    private static final Boolean DEFAULT_ATTENDED = false;
    private static final Boolean UPDATED_ATTENDED = true;

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/student-registers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentRegisterRepository studentRegisterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentRegisterMockMvc;

    private StudentRegister studentRegister;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentRegister createEntity(EntityManager em) {
        StudentRegister studentRegister = new StudentRegister()
            .dateOfLesson(DEFAULT_DATE_OF_LESSON)
            .pay(DEFAULT_PAY)
            .attended(DEFAULT_ATTENDED)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedOn(DEFAULT_UPDATED_ON);
        return studentRegister;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentRegister createUpdatedEntity(EntityManager em) {
        StudentRegister studentRegister = new StudentRegister()
            .dateOfLesson(UPDATED_DATE_OF_LESSON)
            .pay(UPDATED_PAY)
            .attended(UPDATED_ATTENDED)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);
        return studentRegister;
    }

    @BeforeEach
    public void initTest() {
        studentRegister = createEntity(em);
    }

    @Test
    @Transactional
    void createStudentRegister() throws Exception {
        int databaseSizeBeforeCreate = studentRegisterRepository.findAll().size();
        // Create the StudentRegister
        restStudentRegisterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentRegister))
            )
            .andExpect(status().isCreated());

        // Validate the StudentRegister in the database
        List<StudentRegister> studentRegisterList = studentRegisterRepository.findAll();
        assertThat(studentRegisterList).hasSize(databaseSizeBeforeCreate + 1);
        StudentRegister testStudentRegister = studentRegisterList.get(studentRegisterList.size() - 1);
        assertThat(testStudentRegister.getDateOfLesson()).isEqualTo(DEFAULT_DATE_OF_LESSON);
        assertThat(testStudentRegister.getPay()).isEqualTo(DEFAULT_PAY);
        assertThat(testStudentRegister.getAttended()).isEqualTo(DEFAULT_ATTENDED);
        assertThat(testStudentRegister.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testStudentRegister.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    void createStudentRegisterWithExistingId() throws Exception {
        // Create the StudentRegister with an existing ID
        studentRegister.setId(1L);

        int databaseSizeBeforeCreate = studentRegisterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentRegisterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentRegister))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentRegister in the database
        List<StudentRegister> studentRegisterList = studentRegisterRepository.findAll();
        assertThat(studentRegisterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateOfLessonIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRegisterRepository.findAll().size();
        // set the field null
        studentRegister.setDateOfLesson(null);

        // Create the StudentRegister, which fails.

        restStudentRegisterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentRegister))
            )
            .andExpect(status().isBadRequest());

        List<StudentRegister> studentRegisterList = studentRegisterRepository.findAll();
        assertThat(studentRegisterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRegisterRepository.findAll().size();
        // set the field null
        studentRegister.setCreatedOn(null);

        // Create the StudentRegister, which fails.

        restStudentRegisterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentRegister))
            )
            .andExpect(status().isBadRequest());

        List<StudentRegister> studentRegisterList = studentRegisterRepository.findAll();
        assertThat(studentRegisterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRegisterRepository.findAll().size();
        // set the field null
        studentRegister.setUpdatedOn(null);

        // Create the StudentRegister, which fails.

        restStudentRegisterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentRegister))
            )
            .andExpect(status().isBadRequest());

        List<StudentRegister> studentRegisterList = studentRegisterRepository.findAll();
        assertThat(studentRegisterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStudentRegisters() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList
        restStudentRegisterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentRegister.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOfLesson").value(hasItem(DEFAULT_DATE_OF_LESSON.toString())))
            .andExpect(jsonPath("$.[*].pay").value(hasItem(DEFAULT_PAY.doubleValue())))
            .andExpect(jsonPath("$.[*].attended").value(hasItem(DEFAULT_ATTENDED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));
    }

    @Test
    @Transactional
    void getStudentRegister() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get the studentRegister
        restStudentRegisterMockMvc
            .perform(get(ENTITY_API_URL_ID, studentRegister.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentRegister.getId().intValue()))
            .andExpect(jsonPath("$.dateOfLesson").value(DEFAULT_DATE_OF_LESSON.toString()))
            .andExpect(jsonPath("$.pay").value(DEFAULT_PAY.doubleValue()))
            .andExpect(jsonPath("$.attended").value(DEFAULT_ATTENDED.booleanValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()));
    }

    @Test
    @Transactional
    void getStudentRegistersByIdFiltering() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        Long id = studentRegister.getId();

        defaultStudentRegisterShouldBeFound("id.equals=" + id);
        defaultStudentRegisterShouldNotBeFound("id.notEquals=" + id);

        defaultStudentRegisterShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStudentRegisterShouldNotBeFound("id.greaterThan=" + id);

        defaultStudentRegisterShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStudentRegisterShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByDateOfLessonIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where dateOfLesson equals to DEFAULT_DATE_OF_LESSON
        defaultStudentRegisterShouldBeFound("dateOfLesson.equals=" + DEFAULT_DATE_OF_LESSON);

        // Get all the studentRegisterList where dateOfLesson equals to UPDATED_DATE_OF_LESSON
        defaultStudentRegisterShouldNotBeFound("dateOfLesson.equals=" + UPDATED_DATE_OF_LESSON);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByDateOfLessonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where dateOfLesson not equals to DEFAULT_DATE_OF_LESSON
        defaultStudentRegisterShouldNotBeFound("dateOfLesson.notEquals=" + DEFAULT_DATE_OF_LESSON);

        // Get all the studentRegisterList where dateOfLesson not equals to UPDATED_DATE_OF_LESSON
        defaultStudentRegisterShouldBeFound("dateOfLesson.notEquals=" + UPDATED_DATE_OF_LESSON);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByDateOfLessonIsInShouldWork() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where dateOfLesson in DEFAULT_DATE_OF_LESSON or UPDATED_DATE_OF_LESSON
        defaultStudentRegisterShouldBeFound("dateOfLesson.in=" + DEFAULT_DATE_OF_LESSON + "," + UPDATED_DATE_OF_LESSON);

        // Get all the studentRegisterList where dateOfLesson equals to UPDATED_DATE_OF_LESSON
        defaultStudentRegisterShouldNotBeFound("dateOfLesson.in=" + UPDATED_DATE_OF_LESSON);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByDateOfLessonIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where dateOfLesson is not null
        defaultStudentRegisterShouldBeFound("dateOfLesson.specified=true");

        // Get all the studentRegisterList where dateOfLesson is null
        defaultStudentRegisterShouldNotBeFound("dateOfLesson.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentRegistersByPayIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where pay equals to DEFAULT_PAY
        defaultStudentRegisterShouldBeFound("pay.equals=" + DEFAULT_PAY);

        // Get all the studentRegisterList where pay equals to UPDATED_PAY
        defaultStudentRegisterShouldNotBeFound("pay.equals=" + UPDATED_PAY);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByPayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where pay not equals to DEFAULT_PAY
        defaultStudentRegisterShouldNotBeFound("pay.notEquals=" + DEFAULT_PAY);

        // Get all the studentRegisterList where pay not equals to UPDATED_PAY
        defaultStudentRegisterShouldBeFound("pay.notEquals=" + UPDATED_PAY);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByPayIsInShouldWork() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where pay in DEFAULT_PAY or UPDATED_PAY
        defaultStudentRegisterShouldBeFound("pay.in=" + DEFAULT_PAY + "," + UPDATED_PAY);

        // Get all the studentRegisterList where pay equals to UPDATED_PAY
        defaultStudentRegisterShouldNotBeFound("pay.in=" + UPDATED_PAY);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByPayIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where pay is not null
        defaultStudentRegisterShouldBeFound("pay.specified=true");

        // Get all the studentRegisterList where pay is null
        defaultStudentRegisterShouldNotBeFound("pay.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentRegistersByPayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where pay is greater than or equal to DEFAULT_PAY
        defaultStudentRegisterShouldBeFound("pay.greaterThanOrEqual=" + DEFAULT_PAY);

        // Get all the studentRegisterList where pay is greater than or equal to UPDATED_PAY
        defaultStudentRegisterShouldNotBeFound("pay.greaterThanOrEqual=" + UPDATED_PAY);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByPayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where pay is less than or equal to DEFAULT_PAY
        defaultStudentRegisterShouldBeFound("pay.lessThanOrEqual=" + DEFAULT_PAY);

        // Get all the studentRegisterList where pay is less than or equal to SMALLER_PAY
        defaultStudentRegisterShouldNotBeFound("pay.lessThanOrEqual=" + SMALLER_PAY);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByPayIsLessThanSomething() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where pay is less than DEFAULT_PAY
        defaultStudentRegisterShouldNotBeFound("pay.lessThan=" + DEFAULT_PAY);

        // Get all the studentRegisterList where pay is less than UPDATED_PAY
        defaultStudentRegisterShouldBeFound("pay.lessThan=" + UPDATED_PAY);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByPayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where pay is greater than DEFAULT_PAY
        defaultStudentRegisterShouldNotBeFound("pay.greaterThan=" + DEFAULT_PAY);

        // Get all the studentRegisterList where pay is greater than SMALLER_PAY
        defaultStudentRegisterShouldBeFound("pay.greaterThan=" + SMALLER_PAY);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByAttendedIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where attended equals to DEFAULT_ATTENDED
        defaultStudentRegisterShouldBeFound("attended.equals=" + DEFAULT_ATTENDED);

        // Get all the studentRegisterList where attended equals to UPDATED_ATTENDED
        defaultStudentRegisterShouldNotBeFound("attended.equals=" + UPDATED_ATTENDED);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByAttendedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where attended not equals to DEFAULT_ATTENDED
        defaultStudentRegisterShouldNotBeFound("attended.notEquals=" + DEFAULT_ATTENDED);

        // Get all the studentRegisterList where attended not equals to UPDATED_ATTENDED
        defaultStudentRegisterShouldBeFound("attended.notEquals=" + UPDATED_ATTENDED);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByAttendedIsInShouldWork() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where attended in DEFAULT_ATTENDED or UPDATED_ATTENDED
        defaultStudentRegisterShouldBeFound("attended.in=" + DEFAULT_ATTENDED + "," + UPDATED_ATTENDED);

        // Get all the studentRegisterList where attended equals to UPDATED_ATTENDED
        defaultStudentRegisterShouldNotBeFound("attended.in=" + UPDATED_ATTENDED);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByAttendedIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where attended is not null
        defaultStudentRegisterShouldBeFound("attended.specified=true");

        // Get all the studentRegisterList where attended is null
        defaultStudentRegisterShouldNotBeFound("attended.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentRegistersByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where createdOn equals to DEFAULT_CREATED_ON
        defaultStudentRegisterShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the studentRegisterList where createdOn equals to UPDATED_CREATED_ON
        defaultStudentRegisterShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByCreatedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where createdOn not equals to DEFAULT_CREATED_ON
        defaultStudentRegisterShouldNotBeFound("createdOn.notEquals=" + DEFAULT_CREATED_ON);

        // Get all the studentRegisterList where createdOn not equals to UPDATED_CREATED_ON
        defaultStudentRegisterShouldBeFound("createdOn.notEquals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultStudentRegisterShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the studentRegisterList where createdOn equals to UPDATED_CREATED_ON
        defaultStudentRegisterShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where createdOn is not null
        defaultStudentRegisterShouldBeFound("createdOn.specified=true");

        // Get all the studentRegisterList where createdOn is null
        defaultStudentRegisterShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentRegistersByUpdatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where updatedOn equals to DEFAULT_UPDATED_ON
        defaultStudentRegisterShouldBeFound("updatedOn.equals=" + DEFAULT_UPDATED_ON);

        // Get all the studentRegisterList where updatedOn equals to UPDATED_UPDATED_ON
        defaultStudentRegisterShouldNotBeFound("updatedOn.equals=" + UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByUpdatedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where updatedOn not equals to DEFAULT_UPDATED_ON
        defaultStudentRegisterShouldNotBeFound("updatedOn.notEquals=" + DEFAULT_UPDATED_ON);

        // Get all the studentRegisterList where updatedOn not equals to UPDATED_UPDATED_ON
        defaultStudentRegisterShouldBeFound("updatedOn.notEquals=" + UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByUpdatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where updatedOn in DEFAULT_UPDATED_ON or UPDATED_UPDATED_ON
        defaultStudentRegisterShouldBeFound("updatedOn.in=" + DEFAULT_UPDATED_ON + "," + UPDATED_UPDATED_ON);

        // Get all the studentRegisterList where updatedOn equals to UPDATED_UPDATED_ON
        defaultStudentRegisterShouldNotBeFound("updatedOn.in=" + UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void getAllStudentRegistersByUpdatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        // Get all the studentRegisterList where updatedOn is not null
        defaultStudentRegisterShouldBeFound("updatedOn.specified=true");

        // Get all the studentRegisterList where updatedOn is null
        defaultStudentRegisterShouldNotBeFound("updatedOn.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentRegistersByStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);
        Student student = StudentResourceIT.createEntity(em);
        em.persist(student);
        em.flush();
        studentRegister.setStudent(student);
        studentRegisterRepository.saveAndFlush(studentRegister);
        Long studentId = student.getId();

        // Get all the studentRegisterList where student equals to studentId
        defaultStudentRegisterShouldBeFound("studentId.equals=" + studentId);

        // Get all the studentRegisterList where student equals to (studentId + 1)
        defaultStudentRegisterShouldNotBeFound("studentId.equals=" + (studentId + 1));
    }

    @Test
    @Transactional
    void getAllStudentRegistersByLessonTimetableIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);
        LessonInstance lessonTimetable = LessonInstanceResourceIT.createEntity(em);
        em.persist(lessonTimetable);
        em.flush();
        studentRegister.setLessonTimetable(lessonTimetable);
        studentRegisterRepository.saveAndFlush(studentRegister);
        Long lessonTimetableId = lessonTimetable.getId();

        // Get all the studentRegisterList where lessonTimetable equals to lessonTimetableId
        defaultStudentRegisterShouldBeFound("lessonTimetableId.equals=" + lessonTimetableId);

        // Get all the studentRegisterList where lessonTimetable equals to (lessonTimetableId + 1)
        defaultStudentRegisterShouldNotBeFound("lessonTimetableId.equals=" + (lessonTimetableId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentRegisterShouldBeFound(String filter) throws Exception {
        restStudentRegisterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentRegister.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOfLesson").value(hasItem(DEFAULT_DATE_OF_LESSON.toString())))
            .andExpect(jsonPath("$.[*].pay").value(hasItem(DEFAULT_PAY.doubleValue())))
            .andExpect(jsonPath("$.[*].attended").value(hasItem(DEFAULT_ATTENDED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())));

        // Check, that the count call also returns 1
        restStudentRegisterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentRegisterShouldNotBeFound(String filter) throws Exception {
        restStudentRegisterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentRegisterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStudentRegister() throws Exception {
        // Get the studentRegister
        restStudentRegisterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStudentRegister() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        int databaseSizeBeforeUpdate = studentRegisterRepository.findAll().size();

        // Update the studentRegister
        StudentRegister updatedStudentRegister = studentRegisterRepository.findById(studentRegister.getId()).get();
        // Disconnect from session so that the updates on updatedStudentRegister are not directly saved in db
        em.detach(updatedStudentRegister);
        updatedStudentRegister
            .dateOfLesson(UPDATED_DATE_OF_LESSON)
            .pay(UPDATED_PAY)
            .attended(UPDATED_ATTENDED)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);

        restStudentRegisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStudentRegister.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStudentRegister))
            )
            .andExpect(status().isOk());

        // Validate the StudentRegister in the database
        List<StudentRegister> studentRegisterList = studentRegisterRepository.findAll();
        assertThat(studentRegisterList).hasSize(databaseSizeBeforeUpdate);
        StudentRegister testStudentRegister = studentRegisterList.get(studentRegisterList.size() - 1);
        assertThat(testStudentRegister.getDateOfLesson()).isEqualTo(UPDATED_DATE_OF_LESSON);
        assertThat(testStudentRegister.getPay()).isEqualTo(UPDATED_PAY);
        assertThat(testStudentRegister.getAttended()).isEqualTo(UPDATED_ATTENDED);
        assertThat(testStudentRegister.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testStudentRegister.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void putNonExistingStudentRegister() throws Exception {
        int databaseSizeBeforeUpdate = studentRegisterRepository.findAll().size();
        studentRegister.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentRegisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentRegister.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentRegister))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentRegister in the database
        List<StudentRegister> studentRegisterList = studentRegisterRepository.findAll();
        assertThat(studentRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentRegister() throws Exception {
        int databaseSizeBeforeUpdate = studentRegisterRepository.findAll().size();
        studentRegister.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentRegisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentRegister))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentRegister in the database
        List<StudentRegister> studentRegisterList = studentRegisterRepository.findAll();
        assertThat(studentRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentRegister() throws Exception {
        int databaseSizeBeforeUpdate = studentRegisterRepository.findAll().size();
        studentRegister.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentRegisterMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentRegister))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentRegister in the database
        List<StudentRegister> studentRegisterList = studentRegisterRepository.findAll();
        assertThat(studentRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentRegisterWithPatch() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        int databaseSizeBeforeUpdate = studentRegisterRepository.findAll().size();

        // Update the studentRegister using partial update
        StudentRegister partialUpdatedStudentRegister = new StudentRegister();
        partialUpdatedStudentRegister.setId(studentRegister.getId());

        partialUpdatedStudentRegister.pay(UPDATED_PAY).attended(UPDATED_ATTENDED).updatedOn(UPDATED_UPDATED_ON);

        restStudentRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentRegister.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentRegister))
            )
            .andExpect(status().isOk());

        // Validate the StudentRegister in the database
        List<StudentRegister> studentRegisterList = studentRegisterRepository.findAll();
        assertThat(studentRegisterList).hasSize(databaseSizeBeforeUpdate);
        StudentRegister testStudentRegister = studentRegisterList.get(studentRegisterList.size() - 1);
        assertThat(testStudentRegister.getDateOfLesson()).isEqualTo(DEFAULT_DATE_OF_LESSON);
        assertThat(testStudentRegister.getPay()).isEqualTo(UPDATED_PAY);
        assertThat(testStudentRegister.getAttended()).isEqualTo(UPDATED_ATTENDED);
        assertThat(testStudentRegister.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testStudentRegister.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void fullUpdateStudentRegisterWithPatch() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        int databaseSizeBeforeUpdate = studentRegisterRepository.findAll().size();

        // Update the studentRegister using partial update
        StudentRegister partialUpdatedStudentRegister = new StudentRegister();
        partialUpdatedStudentRegister.setId(studentRegister.getId());

        partialUpdatedStudentRegister
            .dateOfLesson(UPDATED_DATE_OF_LESSON)
            .pay(UPDATED_PAY)
            .attended(UPDATED_ATTENDED)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON);

        restStudentRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentRegister.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentRegister))
            )
            .andExpect(status().isOk());

        // Validate the StudentRegister in the database
        List<StudentRegister> studentRegisterList = studentRegisterRepository.findAll();
        assertThat(studentRegisterList).hasSize(databaseSizeBeforeUpdate);
        StudentRegister testStudentRegister = studentRegisterList.get(studentRegisterList.size() - 1);
        assertThat(testStudentRegister.getDateOfLesson()).isEqualTo(UPDATED_DATE_OF_LESSON);
        assertThat(testStudentRegister.getPay()).isEqualTo(UPDATED_PAY);
        assertThat(testStudentRegister.getAttended()).isEqualTo(UPDATED_ATTENDED);
        assertThat(testStudentRegister.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testStudentRegister.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingStudentRegister() throws Exception {
        int databaseSizeBeforeUpdate = studentRegisterRepository.findAll().size();
        studentRegister.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentRegister.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentRegister))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentRegister in the database
        List<StudentRegister> studentRegisterList = studentRegisterRepository.findAll();
        assertThat(studentRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudentRegister() throws Exception {
        int databaseSizeBeforeUpdate = studentRegisterRepository.findAll().size();
        studentRegister.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentRegister))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentRegister in the database
        List<StudentRegister> studentRegisterList = studentRegisterRepository.findAll();
        assertThat(studentRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudentRegister() throws Exception {
        int databaseSizeBeforeUpdate = studentRegisterRepository.findAll().size();
        studentRegister.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentRegister))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentRegister in the database
        List<StudentRegister> studentRegisterList = studentRegisterRepository.findAll();
        assertThat(studentRegisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudentRegister() throws Exception {
        // Initialize the database
        studentRegisterRepository.saveAndFlush(studentRegister);

        int databaseSizeBeforeDelete = studentRegisterRepository.findAll().size();

        // Delete the studentRegister
        restStudentRegisterMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentRegister.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentRegister> studentRegisterList = studentRegisterRepository.findAll();
        assertThat(studentRegisterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
