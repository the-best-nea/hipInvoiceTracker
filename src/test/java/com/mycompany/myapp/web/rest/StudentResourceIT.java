package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LessonInstance;
import com.mycompany.myapp.domain.Student;
import com.mycompany.myapp.domain.StudentRegister;
import com.mycompany.myapp.repository.StudentRepository;
import com.mycompany.myapp.service.criteria.StudentCriteria;
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
 * Integration tests for the {@link StudentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR_GROUP = 1;
    private static final Integer UPDATED_YEAR_GROUP = 2;
    private static final Integer SMALLER_YEAR_GROUP = 1 - 1;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Float DEFAULT_BALANCE = 1F;
    private static final Float UPDATED_BALANCE = 2F;
    private static final Float SMALLER_BALANCE = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/students";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentMockMvc;

    private Student student;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createEntity(EntityManager em) {
        Student student = new Student()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .yearGroup(DEFAULT_YEAR_GROUP)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .active(DEFAULT_ACTIVE)
            .createdAt(DEFAULT_CREATED_AT)
            .balance(DEFAULT_BALANCE);
        return student;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createUpdatedEntity(EntityManager em) {
        Student student = new Student()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .yearGroup(UPDATED_YEAR_GROUP)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .active(UPDATED_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .balance(UPDATED_BALANCE);
        return student;
    }

    @BeforeEach
    public void initTest() {
        student = createEntity(em);
    }

    @Test
    @Transactional
    void createStudent() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();
        // Create the Student
        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isCreated());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate + 1);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testStudent.getYearGroup()).isEqualTo(DEFAULT_YEAR_GROUP);
        assertThat(testStudent.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testStudent.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testStudent.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testStudent.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testStudent.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testStudent.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testStudent.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    void createStudentWithExistingId() throws Exception {
        // Create the Student with an existing ID
        student.setId(1L);

        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setFirstName(null);

        // Create the Student, which fails.

        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setLastName(null);

        // Create the Student, which fails.

        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkYearGroupIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setYearGroup(null);

        // Create the Student, which fails.

        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setEmail(null);

        // Create the Student, which fails.

        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setStartDate(null);

        // Create the Student, which fails.

        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setCreatedAt(null);

        // Create the Student, which fails.

        restStudentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStudents() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].yearGroup").value(hasItem(DEFAULT_YEAR_GROUP)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));
    }

    @Test
    @Transactional
    void getStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get the student
        restStudentMockMvc
            .perform(get(ENTITY_API_URL_ID, student.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(student.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.yearGroup").value(DEFAULT_YEAR_GROUP))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    void getStudentsByIdFiltering() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        Long id = student.getId();

        defaultStudentShouldBeFound("id.equals=" + id);
        defaultStudentShouldNotBeFound("id.notEquals=" + id);

        defaultStudentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStudentShouldNotBeFound("id.greaterThan=" + id);

        defaultStudentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStudentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStudentsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName equals to DEFAULT_FIRST_NAME
        defaultStudentShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName equals to UPDATED_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName not equals to DEFAULT_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName not equals to UPDATED_FIRST_NAME
        defaultStudentShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultStudentShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the studentList where firstName equals to UPDATED_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName is not null
        defaultStudentShouldBeFound("firstName.specified=true");

        // Get all the studentList where firstName is null
        defaultStudentShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName contains DEFAULT_FIRST_NAME
        defaultStudentShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName contains UPDATED_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName does not contain DEFAULT_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName does not contain UPDATED_FIRST_NAME
        defaultStudentShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName equals to DEFAULT_LAST_NAME
        defaultStudentShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName equals to UPDATED_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName not equals to DEFAULT_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName not equals to UPDATED_LAST_NAME
        defaultStudentShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultStudentShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the studentList where lastName equals to UPDATED_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName is not null
        defaultStudentShouldBeFound("lastName.specified=true");

        // Get all the studentList where lastName is null
        defaultStudentShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName contains DEFAULT_LAST_NAME
        defaultStudentShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName contains UPDATED_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName does not contain DEFAULT_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName does not contain UPDATED_LAST_NAME
        defaultStudentShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllStudentsByYearGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where yearGroup equals to DEFAULT_YEAR_GROUP
        defaultStudentShouldBeFound("yearGroup.equals=" + DEFAULT_YEAR_GROUP);

        // Get all the studentList where yearGroup equals to UPDATED_YEAR_GROUP
        defaultStudentShouldNotBeFound("yearGroup.equals=" + UPDATED_YEAR_GROUP);
    }

    @Test
    @Transactional
    void getAllStudentsByYearGroupIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where yearGroup not equals to DEFAULT_YEAR_GROUP
        defaultStudentShouldNotBeFound("yearGroup.notEquals=" + DEFAULT_YEAR_GROUP);

        // Get all the studentList where yearGroup not equals to UPDATED_YEAR_GROUP
        defaultStudentShouldBeFound("yearGroup.notEquals=" + UPDATED_YEAR_GROUP);
    }

    @Test
    @Transactional
    void getAllStudentsByYearGroupIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where yearGroup in DEFAULT_YEAR_GROUP or UPDATED_YEAR_GROUP
        defaultStudentShouldBeFound("yearGroup.in=" + DEFAULT_YEAR_GROUP + "," + UPDATED_YEAR_GROUP);

        // Get all the studentList where yearGroup equals to UPDATED_YEAR_GROUP
        defaultStudentShouldNotBeFound("yearGroup.in=" + UPDATED_YEAR_GROUP);
    }

    @Test
    @Transactional
    void getAllStudentsByYearGroupIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where yearGroup is not null
        defaultStudentShouldBeFound("yearGroup.specified=true");

        // Get all the studentList where yearGroup is null
        defaultStudentShouldNotBeFound("yearGroup.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByYearGroupIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where yearGroup is greater than or equal to DEFAULT_YEAR_GROUP
        defaultStudentShouldBeFound("yearGroup.greaterThanOrEqual=" + DEFAULT_YEAR_GROUP);

        // Get all the studentList where yearGroup is greater than or equal to UPDATED_YEAR_GROUP
        defaultStudentShouldNotBeFound("yearGroup.greaterThanOrEqual=" + UPDATED_YEAR_GROUP);
    }

    @Test
    @Transactional
    void getAllStudentsByYearGroupIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where yearGroup is less than or equal to DEFAULT_YEAR_GROUP
        defaultStudentShouldBeFound("yearGroup.lessThanOrEqual=" + DEFAULT_YEAR_GROUP);

        // Get all the studentList where yearGroup is less than or equal to SMALLER_YEAR_GROUP
        defaultStudentShouldNotBeFound("yearGroup.lessThanOrEqual=" + SMALLER_YEAR_GROUP);
    }

    @Test
    @Transactional
    void getAllStudentsByYearGroupIsLessThanSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where yearGroup is less than DEFAULT_YEAR_GROUP
        defaultStudentShouldNotBeFound("yearGroup.lessThan=" + DEFAULT_YEAR_GROUP);

        // Get all the studentList where yearGroup is less than UPDATED_YEAR_GROUP
        defaultStudentShouldBeFound("yearGroup.lessThan=" + UPDATED_YEAR_GROUP);
    }

    @Test
    @Transactional
    void getAllStudentsByYearGroupIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where yearGroup is greater than DEFAULT_YEAR_GROUP
        defaultStudentShouldNotBeFound("yearGroup.greaterThan=" + DEFAULT_YEAR_GROUP);

        // Get all the studentList where yearGroup is greater than SMALLER_YEAR_GROUP
        defaultStudentShouldBeFound("yearGroup.greaterThan=" + SMALLER_YEAR_GROUP);
    }

    @Test
    @Transactional
    void getAllStudentsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email equals to DEFAULT_EMAIL
        defaultStudentShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the studentList where email equals to UPDATED_EMAIL
        defaultStudentShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllStudentsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email not equals to DEFAULT_EMAIL
        defaultStudentShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the studentList where email not equals to UPDATED_EMAIL
        defaultStudentShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllStudentsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultStudentShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the studentList where email equals to UPDATED_EMAIL
        defaultStudentShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllStudentsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email is not null
        defaultStudentShouldBeFound("email.specified=true");

        // Get all the studentList where email is null
        defaultStudentShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByEmailContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email contains DEFAULT_EMAIL
        defaultStudentShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the studentList where email contains UPDATED_EMAIL
        defaultStudentShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllStudentsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email does not contain DEFAULT_EMAIL
        defaultStudentShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the studentList where email does not contain UPDATED_EMAIL
        defaultStudentShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllStudentsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultStudentShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the studentList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultStudentShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllStudentsByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultStudentShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the studentList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultStudentShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllStudentsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultStudentShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the studentList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultStudentShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllStudentsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phoneNumber is not null
        defaultStudentShouldBeFound("phoneNumber.specified=true");

        // Get all the studentList where phoneNumber is null
        defaultStudentShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultStudentShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the studentList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultStudentShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllStudentsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultStudentShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the studentList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultStudentShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllStudentsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where startDate equals to DEFAULT_START_DATE
        defaultStudentShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the studentList where startDate equals to UPDATED_START_DATE
        defaultStudentShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllStudentsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where startDate not equals to DEFAULT_START_DATE
        defaultStudentShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the studentList where startDate not equals to UPDATED_START_DATE
        defaultStudentShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllStudentsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultStudentShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the studentList where startDate equals to UPDATED_START_DATE
        defaultStudentShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllStudentsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where startDate is not null
        defaultStudentShouldBeFound("startDate.specified=true");

        // Get all the studentList where startDate is null
        defaultStudentShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where endDate equals to DEFAULT_END_DATE
        defaultStudentShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the studentList where endDate equals to UPDATED_END_DATE
        defaultStudentShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllStudentsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where endDate not equals to DEFAULT_END_DATE
        defaultStudentShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the studentList where endDate not equals to UPDATED_END_DATE
        defaultStudentShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllStudentsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultStudentShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the studentList where endDate equals to UPDATED_END_DATE
        defaultStudentShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllStudentsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where endDate is not null
        defaultStudentShouldBeFound("endDate.specified=true");

        // Get all the studentList where endDate is null
        defaultStudentShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where active equals to DEFAULT_ACTIVE
        defaultStudentShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the studentList where active equals to UPDATED_ACTIVE
        defaultStudentShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllStudentsByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where active not equals to DEFAULT_ACTIVE
        defaultStudentShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the studentList where active not equals to UPDATED_ACTIVE
        defaultStudentShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllStudentsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultStudentShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the studentList where active equals to UPDATED_ACTIVE
        defaultStudentShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllStudentsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where active is not null
        defaultStudentShouldBeFound("active.specified=true");

        // Get all the studentList where active is null
        defaultStudentShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where createdAt equals to DEFAULT_CREATED_AT
        defaultStudentShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the studentList where createdAt equals to UPDATED_CREATED_AT
        defaultStudentShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllStudentsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where createdAt not equals to DEFAULT_CREATED_AT
        defaultStudentShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the studentList where createdAt not equals to UPDATED_CREATED_AT
        defaultStudentShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllStudentsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultStudentShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the studentList where createdAt equals to UPDATED_CREATED_AT
        defaultStudentShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllStudentsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where createdAt is not null
        defaultStudentShouldBeFound("createdAt.specified=true");

        // Get all the studentList where createdAt is null
        defaultStudentShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where balance equals to DEFAULT_BALANCE
        defaultStudentShouldBeFound("balance.equals=" + DEFAULT_BALANCE);

        // Get all the studentList where balance equals to UPDATED_BALANCE
        defaultStudentShouldNotBeFound("balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllStudentsByBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where balance not equals to DEFAULT_BALANCE
        defaultStudentShouldNotBeFound("balance.notEquals=" + DEFAULT_BALANCE);

        // Get all the studentList where balance not equals to UPDATED_BALANCE
        defaultStudentShouldBeFound("balance.notEquals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllStudentsByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where balance in DEFAULT_BALANCE or UPDATED_BALANCE
        defaultStudentShouldBeFound("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE);

        // Get all the studentList where balance equals to UPDATED_BALANCE
        defaultStudentShouldNotBeFound("balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllStudentsByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where balance is not null
        defaultStudentShouldBeFound("balance.specified=true");

        // Get all the studentList where balance is null
        defaultStudentShouldNotBeFound("balance.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentsByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where balance is greater than or equal to DEFAULT_BALANCE
        defaultStudentShouldBeFound("balance.greaterThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the studentList where balance is greater than or equal to UPDATED_BALANCE
        defaultStudentShouldNotBeFound("balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllStudentsByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where balance is less than or equal to DEFAULT_BALANCE
        defaultStudentShouldBeFound("balance.lessThanOrEqual=" + DEFAULT_BALANCE);

        // Get all the studentList where balance is less than or equal to SMALLER_BALANCE
        defaultStudentShouldNotBeFound("balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    void getAllStudentsByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where balance is less than DEFAULT_BALANCE
        defaultStudentShouldNotBeFound("balance.lessThan=" + DEFAULT_BALANCE);

        // Get all the studentList where balance is less than UPDATED_BALANCE
        defaultStudentShouldBeFound("balance.lessThan=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllStudentsByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where balance is greater than DEFAULT_BALANCE
        defaultStudentShouldNotBeFound("balance.greaterThan=" + DEFAULT_BALANCE);

        // Get all the studentList where balance is greater than SMALLER_BALANCE
        defaultStudentShouldBeFound("balance.greaterThan=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    void getAllStudentsByStudentRegisterIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        StudentRegister studentRegister = StudentRegisterResourceIT.createEntity(em);
        em.persist(studentRegister);
        em.flush();
        student.addStudentRegister(studentRegister);
        studentRepository.saveAndFlush(student);
        Long studentRegisterId = studentRegister.getId();

        // Get all the studentList where studentRegister equals to studentRegisterId
        defaultStudentShouldBeFound("studentRegisterId.equals=" + studentRegisterId);

        // Get all the studentList where studentRegister equals to (studentRegisterId + 1)
        defaultStudentShouldNotBeFound("studentRegisterId.equals=" + (studentRegisterId + 1));
    }

    @Test
    @Transactional
    void getAllStudentsByLessonInstanceIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        LessonInstance lessonInstance = LessonInstanceResourceIT.createEntity(em);
        em.persist(lessonInstance);
        em.flush();
        student.addLessonInstance(lessonInstance);
        studentRepository.saveAndFlush(student);
        Long lessonInstanceId = lessonInstance.getId();

        // Get all the studentList where lessonInstance equals to lessonInstanceId
        defaultStudentShouldBeFound("lessonInstanceId.equals=" + lessonInstanceId);

        // Get all the studentList where lessonInstance equals to (lessonInstanceId + 1)
        defaultStudentShouldNotBeFound("lessonInstanceId.equals=" + (lessonInstanceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentShouldBeFound(String filter) throws Exception {
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].yearGroup").value(hasItem(DEFAULT_YEAR_GROUP)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));

        // Check, that the count call also returns 1
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentShouldNotBeFound(String filter) throws Exception {
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStudent() throws Exception {
        // Get the student
        restStudentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student
        Student updatedStudent = studentRepository.findById(student.getId()).get();
        // Disconnect from session so that the updates on updatedStudent are not directly saved in db
        em.detach(updatedStudent);
        updatedStudent
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .yearGroup(UPDATED_YEAR_GROUP)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .active(UPDATED_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .balance(UPDATED_BALANCE);

        restStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStudent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStudent))
            )
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testStudent.getYearGroup()).isEqualTo(UPDATED_YEAR_GROUP);
        assertThat(testStudent.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStudent.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testStudent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testStudent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testStudent.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testStudent.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testStudent.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void putNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, student.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(student))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(student))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentWithPatch() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student using partial update
        Student partialUpdatedStudent = new Student();
        partialUpdatedStudent.setId(student.getId());

        partialUpdatedStudent
            .yearGroup(UPDATED_YEAR_GROUP)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createdAt(UPDATED_CREATED_AT);

        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudent))
            )
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testStudent.getYearGroup()).isEqualTo(UPDATED_YEAR_GROUP);
        assertThat(testStudent.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStudent.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testStudent.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testStudent.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testStudent.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testStudent.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testStudent.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    void fullUpdateStudentWithPatch() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student using partial update
        Student partialUpdatedStudent = new Student();
        partialUpdatedStudent.setId(student.getId());

        partialUpdatedStudent
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .yearGroup(UPDATED_YEAR_GROUP)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .active(UPDATED_ACTIVE)
            .createdAt(UPDATED_CREATED_AT)
            .balance(UPDATED_BALANCE);

        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudent))
            )
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testStudent.getYearGroup()).isEqualTo(UPDATED_YEAR_GROUP);
        assertThat(testStudent.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStudent.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testStudent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testStudent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testStudent.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testStudent.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testStudent.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void patchNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, student.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(student))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(student))
            )
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();
        student.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeDelete = studentRepository.findAll().size();

        // Delete the student
        restStudentMockMvc
            .perform(delete(ENTITY_API_URL_ID, student.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
