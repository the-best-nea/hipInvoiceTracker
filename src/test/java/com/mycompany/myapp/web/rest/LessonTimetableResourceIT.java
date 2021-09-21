package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LessonTimetable;
import com.mycompany.myapp.domain.Location;
import com.mycompany.myapp.domain.Subject;
import com.mycompany.myapp.domain.enumeration.DayOfWeek;
import com.mycompany.myapp.repository.LessonTimetableRepository;
import com.mycompany.myapp.service.criteria.LessonTimetableCriteria;
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
 * Integration tests for the {@link LessonTimetableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LessonTimetableResourceIT {

    private static final String DEFAULT_LESSON_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LESSON_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final DayOfWeek DEFAULT_DAY_OF_WEEK = DayOfWeek.MONDAY;
    private static final DayOfWeek UPDATED_DAY_OF_WEEK = DayOfWeek.TUESDAY;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_REGISTER_TAKEN = false;
    private static final Boolean UPDATED_REGISTER_TAKEN = true;

    private static final String ENTITY_API_URL = "/api/lesson-timetables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LessonTimetableRepository lessonTimetableRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLessonTimetableMockMvc;

    private LessonTimetable lessonTimetable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LessonTimetable createEntity(EntityManager em) {
        LessonTimetable lessonTimetable = new LessonTimetable()
            .lessonName(DEFAULT_LESSON_NAME)
            .startAt(DEFAULT_START_AT)
            .endAt(DEFAULT_END_AT)
            .dayOfWeek(DEFAULT_DAY_OF_WEEK)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .active(DEFAULT_ACTIVE)
            .registerTaken(DEFAULT_REGISTER_TAKEN);
        return lessonTimetable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LessonTimetable createUpdatedEntity(EntityManager em) {
        LessonTimetable lessonTimetable = new LessonTimetable()
            .lessonName(UPDATED_LESSON_NAME)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .active(UPDATED_ACTIVE)
            .registerTaken(UPDATED_REGISTER_TAKEN);
        return lessonTimetable;
    }

    @BeforeEach
    public void initTest() {
        lessonTimetable = createEntity(em);
    }

    @Test
    @Transactional
    void createLessonTimetable() throws Exception {
        int databaseSizeBeforeCreate = lessonTimetableRepository.findAll().size();
        // Create the LessonTimetable
        restLessonTimetableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lessonTimetable))
            )
            .andExpect(status().isCreated());

        // Validate the LessonTimetable in the database
        List<LessonTimetable> lessonTimetableList = lessonTimetableRepository.findAll();
        assertThat(lessonTimetableList).hasSize(databaseSizeBeforeCreate + 1);
        LessonTimetable testLessonTimetable = lessonTimetableList.get(lessonTimetableList.size() - 1);
        assertThat(testLessonTimetable.getLessonName()).isEqualTo(DEFAULT_LESSON_NAME);
        assertThat(testLessonTimetable.getStartAt()).isEqualTo(DEFAULT_START_AT);
        assertThat(testLessonTimetable.getEndAt()).isEqualTo(DEFAULT_END_AT);
        assertThat(testLessonTimetable.getDayOfWeek()).isEqualTo(DEFAULT_DAY_OF_WEEK);
        assertThat(testLessonTimetable.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLessonTimetable.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testLessonTimetable.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testLessonTimetable.getRegisterTaken()).isEqualTo(DEFAULT_REGISTER_TAKEN);
    }

    @Test
    @Transactional
    void createLessonTimetableWithExistingId() throws Exception {
        // Create the LessonTimetable with an existing ID
        lessonTimetable.setId(1L);

        int databaseSizeBeforeCreate = lessonTimetableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLessonTimetableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lessonTimetable))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTimetable in the database
        List<LessonTimetable> lessonTimetableList = lessonTimetableRepository.findAll();
        assertThat(lessonTimetableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLessonNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonTimetableRepository.findAll().size();
        // set the field null
        lessonTimetable.setLessonName(null);

        // Create the LessonTimetable, which fails.

        restLessonTimetableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lessonTimetable))
            )
            .andExpect(status().isBadRequest());

        List<LessonTimetable> lessonTimetableList = lessonTimetableRepository.findAll();
        assertThat(lessonTimetableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonTimetableRepository.findAll().size();
        // set the field null
        lessonTimetable.setStartAt(null);

        // Create the LessonTimetable, which fails.

        restLessonTimetableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lessonTimetable))
            )
            .andExpect(status().isBadRequest());

        List<LessonTimetable> lessonTimetableList = lessonTimetableRepository.findAll();
        assertThat(lessonTimetableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonTimetableRepository.findAll().size();
        // set the field null
        lessonTimetable.setEndAt(null);

        // Create the LessonTimetable, which fails.

        restLessonTimetableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lessonTimetable))
            )
            .andExpect(status().isBadRequest());

        List<LessonTimetable> lessonTimetableList = lessonTimetableRepository.findAll();
        assertThat(lessonTimetableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDayOfWeekIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonTimetableRepository.findAll().size();
        // set the field null
        lessonTimetable.setDayOfWeek(null);

        // Create the LessonTimetable, which fails.

        restLessonTimetableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lessonTimetable))
            )
            .andExpect(status().isBadRequest());

        List<LessonTimetable> lessonTimetableList = lessonTimetableRepository.findAll();
        assertThat(lessonTimetableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonTimetableRepository.findAll().size();
        // set the field null
        lessonTimetable.setCreatedAt(null);

        // Create the LessonTimetable, which fails.

        restLessonTimetableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lessonTimetable))
            )
            .andExpect(status().isBadRequest());

        List<LessonTimetable> lessonTimetableList = lessonTimetableRepository.findAll();
        assertThat(lessonTimetableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLessonTimetables() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList
        restLessonTimetableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lessonTimetable.getId().intValue())))
            .andExpect(jsonPath("$.[*].lessonName").value(hasItem(DEFAULT_LESSON_NAME)))
            .andExpect(jsonPath("$.[*].startAt").value(hasItem(DEFAULT_START_AT.toString())))
            .andExpect(jsonPath("$.[*].endAt").value(hasItem(DEFAULT_END_AT.toString())))
            .andExpect(jsonPath("$.[*].dayOfWeek").value(hasItem(DEFAULT_DAY_OF_WEEK.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].registerTaken").value(hasItem(DEFAULT_REGISTER_TAKEN.booleanValue())));
    }

    @Test
    @Transactional
    void getLessonTimetable() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get the lessonTimetable
        restLessonTimetableMockMvc
            .perform(get(ENTITY_API_URL_ID, lessonTimetable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lessonTimetable.getId().intValue()))
            .andExpect(jsonPath("$.lessonName").value(DEFAULT_LESSON_NAME))
            .andExpect(jsonPath("$.startAt").value(DEFAULT_START_AT.toString()))
            .andExpect(jsonPath("$.endAt").value(DEFAULT_END_AT.toString()))
            .andExpect(jsonPath("$.dayOfWeek").value(DEFAULT_DAY_OF_WEEK.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.registerTaken").value(DEFAULT_REGISTER_TAKEN.booleanValue()));
    }

    @Test
    @Transactional
    void getLessonTimetablesByIdFiltering() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        Long id = lessonTimetable.getId();

        defaultLessonTimetableShouldBeFound("id.equals=" + id);
        defaultLessonTimetableShouldNotBeFound("id.notEquals=" + id);

        defaultLessonTimetableShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLessonTimetableShouldNotBeFound("id.greaterThan=" + id);

        defaultLessonTimetableShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLessonTimetableShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByLessonNameIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where lessonName equals to DEFAULT_LESSON_NAME
        defaultLessonTimetableShouldBeFound("lessonName.equals=" + DEFAULT_LESSON_NAME);

        // Get all the lessonTimetableList where lessonName equals to UPDATED_LESSON_NAME
        defaultLessonTimetableShouldNotBeFound("lessonName.equals=" + UPDATED_LESSON_NAME);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByLessonNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where lessonName not equals to DEFAULT_LESSON_NAME
        defaultLessonTimetableShouldNotBeFound("lessonName.notEquals=" + DEFAULT_LESSON_NAME);

        // Get all the lessonTimetableList where lessonName not equals to UPDATED_LESSON_NAME
        defaultLessonTimetableShouldBeFound("lessonName.notEquals=" + UPDATED_LESSON_NAME);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByLessonNameIsInShouldWork() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where lessonName in DEFAULT_LESSON_NAME or UPDATED_LESSON_NAME
        defaultLessonTimetableShouldBeFound("lessonName.in=" + DEFAULT_LESSON_NAME + "," + UPDATED_LESSON_NAME);

        // Get all the lessonTimetableList where lessonName equals to UPDATED_LESSON_NAME
        defaultLessonTimetableShouldNotBeFound("lessonName.in=" + UPDATED_LESSON_NAME);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByLessonNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where lessonName is not null
        defaultLessonTimetableShouldBeFound("lessonName.specified=true");

        // Get all the lessonTimetableList where lessonName is null
        defaultLessonTimetableShouldNotBeFound("lessonName.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByLessonNameContainsSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where lessonName contains DEFAULT_LESSON_NAME
        defaultLessonTimetableShouldBeFound("lessonName.contains=" + DEFAULT_LESSON_NAME);

        // Get all the lessonTimetableList where lessonName contains UPDATED_LESSON_NAME
        defaultLessonTimetableShouldNotBeFound("lessonName.contains=" + UPDATED_LESSON_NAME);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByLessonNameNotContainsSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where lessonName does not contain DEFAULT_LESSON_NAME
        defaultLessonTimetableShouldNotBeFound("lessonName.doesNotContain=" + DEFAULT_LESSON_NAME);

        // Get all the lessonTimetableList where lessonName does not contain UPDATED_LESSON_NAME
        defaultLessonTimetableShouldBeFound("lessonName.doesNotContain=" + UPDATED_LESSON_NAME);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByStartAtIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where startAt equals to DEFAULT_START_AT
        defaultLessonTimetableShouldBeFound("startAt.equals=" + DEFAULT_START_AT);

        // Get all the lessonTimetableList where startAt equals to UPDATED_START_AT
        defaultLessonTimetableShouldNotBeFound("startAt.equals=" + UPDATED_START_AT);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByStartAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where startAt not equals to DEFAULT_START_AT
        defaultLessonTimetableShouldNotBeFound("startAt.notEquals=" + DEFAULT_START_AT);

        // Get all the lessonTimetableList where startAt not equals to UPDATED_START_AT
        defaultLessonTimetableShouldBeFound("startAt.notEquals=" + UPDATED_START_AT);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByStartAtIsInShouldWork() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where startAt in DEFAULT_START_AT or UPDATED_START_AT
        defaultLessonTimetableShouldBeFound("startAt.in=" + DEFAULT_START_AT + "," + UPDATED_START_AT);

        // Get all the lessonTimetableList where startAt equals to UPDATED_START_AT
        defaultLessonTimetableShouldNotBeFound("startAt.in=" + UPDATED_START_AT);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByStartAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where startAt is not null
        defaultLessonTimetableShouldBeFound("startAt.specified=true");

        // Get all the lessonTimetableList where startAt is null
        defaultLessonTimetableShouldNotBeFound("startAt.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByEndAtIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where endAt equals to DEFAULT_END_AT
        defaultLessonTimetableShouldBeFound("endAt.equals=" + DEFAULT_END_AT);

        // Get all the lessonTimetableList where endAt equals to UPDATED_END_AT
        defaultLessonTimetableShouldNotBeFound("endAt.equals=" + UPDATED_END_AT);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByEndAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where endAt not equals to DEFAULT_END_AT
        defaultLessonTimetableShouldNotBeFound("endAt.notEquals=" + DEFAULT_END_AT);

        // Get all the lessonTimetableList where endAt not equals to UPDATED_END_AT
        defaultLessonTimetableShouldBeFound("endAt.notEquals=" + UPDATED_END_AT);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByEndAtIsInShouldWork() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where endAt in DEFAULT_END_AT or UPDATED_END_AT
        defaultLessonTimetableShouldBeFound("endAt.in=" + DEFAULT_END_AT + "," + UPDATED_END_AT);

        // Get all the lessonTimetableList where endAt equals to UPDATED_END_AT
        defaultLessonTimetableShouldNotBeFound("endAt.in=" + UPDATED_END_AT);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByEndAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where endAt is not null
        defaultLessonTimetableShouldBeFound("endAt.specified=true");

        // Get all the lessonTimetableList where endAt is null
        defaultLessonTimetableShouldNotBeFound("endAt.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByDayOfWeekIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where dayOfWeek equals to DEFAULT_DAY_OF_WEEK
        defaultLessonTimetableShouldBeFound("dayOfWeek.equals=" + DEFAULT_DAY_OF_WEEK);

        // Get all the lessonTimetableList where dayOfWeek equals to UPDATED_DAY_OF_WEEK
        defaultLessonTimetableShouldNotBeFound("dayOfWeek.equals=" + UPDATED_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByDayOfWeekIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where dayOfWeek not equals to DEFAULT_DAY_OF_WEEK
        defaultLessonTimetableShouldNotBeFound("dayOfWeek.notEquals=" + DEFAULT_DAY_OF_WEEK);

        // Get all the lessonTimetableList where dayOfWeek not equals to UPDATED_DAY_OF_WEEK
        defaultLessonTimetableShouldBeFound("dayOfWeek.notEquals=" + UPDATED_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByDayOfWeekIsInShouldWork() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where dayOfWeek in DEFAULT_DAY_OF_WEEK or UPDATED_DAY_OF_WEEK
        defaultLessonTimetableShouldBeFound("dayOfWeek.in=" + DEFAULT_DAY_OF_WEEK + "," + UPDATED_DAY_OF_WEEK);

        // Get all the lessonTimetableList where dayOfWeek equals to UPDATED_DAY_OF_WEEK
        defaultLessonTimetableShouldNotBeFound("dayOfWeek.in=" + UPDATED_DAY_OF_WEEK);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByDayOfWeekIsNullOrNotNull() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where dayOfWeek is not null
        defaultLessonTimetableShouldBeFound("dayOfWeek.specified=true");

        // Get all the lessonTimetableList where dayOfWeek is null
        defaultLessonTimetableShouldNotBeFound("dayOfWeek.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where description equals to DEFAULT_DESCRIPTION
        defaultLessonTimetableShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the lessonTimetableList where description equals to UPDATED_DESCRIPTION
        defaultLessonTimetableShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where description not equals to DEFAULT_DESCRIPTION
        defaultLessonTimetableShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the lessonTimetableList where description not equals to UPDATED_DESCRIPTION
        defaultLessonTimetableShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultLessonTimetableShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the lessonTimetableList where description equals to UPDATED_DESCRIPTION
        defaultLessonTimetableShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where description is not null
        defaultLessonTimetableShouldBeFound("description.specified=true");

        // Get all the lessonTimetableList where description is null
        defaultLessonTimetableShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where description contains DEFAULT_DESCRIPTION
        defaultLessonTimetableShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the lessonTimetableList where description contains UPDATED_DESCRIPTION
        defaultLessonTimetableShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where description does not contain DEFAULT_DESCRIPTION
        defaultLessonTimetableShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the lessonTimetableList where description does not contain UPDATED_DESCRIPTION
        defaultLessonTimetableShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where createdAt equals to DEFAULT_CREATED_AT
        defaultLessonTimetableShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the lessonTimetableList where createdAt equals to UPDATED_CREATED_AT
        defaultLessonTimetableShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where createdAt not equals to DEFAULT_CREATED_AT
        defaultLessonTimetableShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the lessonTimetableList where createdAt not equals to UPDATED_CREATED_AT
        defaultLessonTimetableShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultLessonTimetableShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the lessonTimetableList where createdAt equals to UPDATED_CREATED_AT
        defaultLessonTimetableShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where createdAt is not null
        defaultLessonTimetableShouldBeFound("createdAt.specified=true");

        // Get all the lessonTimetableList where createdAt is null
        defaultLessonTimetableShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where active equals to DEFAULT_ACTIVE
        defaultLessonTimetableShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the lessonTimetableList where active equals to UPDATED_ACTIVE
        defaultLessonTimetableShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where active not equals to DEFAULT_ACTIVE
        defaultLessonTimetableShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the lessonTimetableList where active not equals to UPDATED_ACTIVE
        defaultLessonTimetableShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultLessonTimetableShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the lessonTimetableList where active equals to UPDATED_ACTIVE
        defaultLessonTimetableShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where active is not null
        defaultLessonTimetableShouldBeFound("active.specified=true");

        // Get all the lessonTimetableList where active is null
        defaultLessonTimetableShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByRegisterTakenIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where registerTaken equals to DEFAULT_REGISTER_TAKEN
        defaultLessonTimetableShouldBeFound("registerTaken.equals=" + DEFAULT_REGISTER_TAKEN);

        // Get all the lessonTimetableList where registerTaken equals to UPDATED_REGISTER_TAKEN
        defaultLessonTimetableShouldNotBeFound("registerTaken.equals=" + UPDATED_REGISTER_TAKEN);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByRegisterTakenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where registerTaken not equals to DEFAULT_REGISTER_TAKEN
        defaultLessonTimetableShouldNotBeFound("registerTaken.notEquals=" + DEFAULT_REGISTER_TAKEN);

        // Get all the lessonTimetableList where registerTaken not equals to UPDATED_REGISTER_TAKEN
        defaultLessonTimetableShouldBeFound("registerTaken.notEquals=" + UPDATED_REGISTER_TAKEN);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByRegisterTakenIsInShouldWork() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where registerTaken in DEFAULT_REGISTER_TAKEN or UPDATED_REGISTER_TAKEN
        defaultLessonTimetableShouldBeFound("registerTaken.in=" + DEFAULT_REGISTER_TAKEN + "," + UPDATED_REGISTER_TAKEN);

        // Get all the lessonTimetableList where registerTaken equals to UPDATED_REGISTER_TAKEN
        defaultLessonTimetableShouldNotBeFound("registerTaken.in=" + UPDATED_REGISTER_TAKEN);
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByRegisterTakenIsNullOrNotNull() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        // Get all the lessonTimetableList where registerTaken is not null
        defaultLessonTimetableShouldBeFound("registerTaken.specified=true");

        // Get all the lessonTimetableList where registerTaken is null
        defaultLessonTimetableShouldNotBeFound("registerTaken.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonTimetablesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);
        Location location = LocationResourceIT.createEntity(em);
        em.persist(location);
        em.flush();
        lessonTimetable.setLocation(location);
        lessonTimetableRepository.saveAndFlush(lessonTimetable);
        Long locationId = location.getId();

        // Get all the lessonTimetableList where location equals to locationId
        defaultLessonTimetableShouldBeFound("locationId.equals=" + locationId);

        // Get all the lessonTimetableList where location equals to (locationId + 1)
        defaultLessonTimetableShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    @Test
    @Transactional
    void getAllLessonTimetablesBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);
        Subject subject = SubjectResourceIT.createEntity(em);
        em.persist(subject);
        em.flush();
        lessonTimetable.setSubject(subject);
        lessonTimetableRepository.saveAndFlush(lessonTimetable);
        Long subjectId = subject.getId();

        // Get all the lessonTimetableList where subject equals to subjectId
        defaultLessonTimetableShouldBeFound("subjectId.equals=" + subjectId);

        // Get all the lessonTimetableList where subject equals to (subjectId + 1)
        defaultLessonTimetableShouldNotBeFound("subjectId.equals=" + (subjectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLessonTimetableShouldBeFound(String filter) throws Exception {
        restLessonTimetableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lessonTimetable.getId().intValue())))
            .andExpect(jsonPath("$.[*].lessonName").value(hasItem(DEFAULT_LESSON_NAME)))
            .andExpect(jsonPath("$.[*].startAt").value(hasItem(DEFAULT_START_AT.toString())))
            .andExpect(jsonPath("$.[*].endAt").value(hasItem(DEFAULT_END_AT.toString())))
            .andExpect(jsonPath("$.[*].dayOfWeek").value(hasItem(DEFAULT_DAY_OF_WEEK.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].registerTaken").value(hasItem(DEFAULT_REGISTER_TAKEN.booleanValue())));

        // Check, that the count call also returns 1
        restLessonTimetableMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLessonTimetableShouldNotBeFound(String filter) throws Exception {
        restLessonTimetableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLessonTimetableMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLessonTimetable() throws Exception {
        // Get the lessonTimetable
        restLessonTimetableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLessonTimetable() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        int databaseSizeBeforeUpdate = lessonTimetableRepository.findAll().size();

        // Update the lessonTimetable
        LessonTimetable updatedLessonTimetable = lessonTimetableRepository.findById(lessonTimetable.getId()).get();
        // Disconnect from session so that the updates on updatedLessonTimetable are not directly saved in db
        em.detach(updatedLessonTimetable);
        updatedLessonTimetable
            .lessonName(UPDATED_LESSON_NAME)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .active(UPDATED_ACTIVE)
            .registerTaken(UPDATED_REGISTER_TAKEN);

        restLessonTimetableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLessonTimetable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLessonTimetable))
            )
            .andExpect(status().isOk());

        // Validate the LessonTimetable in the database
        List<LessonTimetable> lessonTimetableList = lessonTimetableRepository.findAll();
        assertThat(lessonTimetableList).hasSize(databaseSizeBeforeUpdate);
        LessonTimetable testLessonTimetable = lessonTimetableList.get(lessonTimetableList.size() - 1);
        assertThat(testLessonTimetable.getLessonName()).isEqualTo(UPDATED_LESSON_NAME);
        assertThat(testLessonTimetable.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testLessonTimetable.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testLessonTimetable.getDayOfWeek()).isEqualTo(UPDATED_DAY_OF_WEEK);
        assertThat(testLessonTimetable.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLessonTimetable.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testLessonTimetable.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testLessonTimetable.getRegisterTaken()).isEqualTo(UPDATED_REGISTER_TAKEN);
    }

    @Test
    @Transactional
    void putNonExistingLessonTimetable() throws Exception {
        int databaseSizeBeforeUpdate = lessonTimetableRepository.findAll().size();
        lessonTimetable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonTimetableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lessonTimetable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetable))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTimetable in the database
        List<LessonTimetable> lessonTimetableList = lessonTimetableRepository.findAll();
        assertThat(lessonTimetableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLessonTimetable() throws Exception {
        int databaseSizeBeforeUpdate = lessonTimetableRepository.findAll().size();
        lessonTimetable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonTimetableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetable))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTimetable in the database
        List<LessonTimetable> lessonTimetableList = lessonTimetableRepository.findAll();
        assertThat(lessonTimetableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLessonTimetable() throws Exception {
        int databaseSizeBeforeUpdate = lessonTimetableRepository.findAll().size();
        lessonTimetable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonTimetableMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lessonTimetable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LessonTimetable in the database
        List<LessonTimetable> lessonTimetableList = lessonTimetableRepository.findAll();
        assertThat(lessonTimetableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLessonTimetableWithPatch() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        int databaseSizeBeforeUpdate = lessonTimetableRepository.findAll().size();

        // Update the lessonTimetable using partial update
        LessonTimetable partialUpdatedLessonTimetable = new LessonTimetable();
        partialUpdatedLessonTimetable.setId(lessonTimetable.getId());

        partialUpdatedLessonTimetable
            .lessonName(UPDATED_LESSON_NAME)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE)
            .registerTaken(UPDATED_REGISTER_TAKEN);

        restLessonTimetableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLessonTimetable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLessonTimetable))
            )
            .andExpect(status().isOk());

        // Validate the LessonTimetable in the database
        List<LessonTimetable> lessonTimetableList = lessonTimetableRepository.findAll();
        assertThat(lessonTimetableList).hasSize(databaseSizeBeforeUpdate);
        LessonTimetable testLessonTimetable = lessonTimetableList.get(lessonTimetableList.size() - 1);
        assertThat(testLessonTimetable.getLessonName()).isEqualTo(UPDATED_LESSON_NAME);
        assertThat(testLessonTimetable.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testLessonTimetable.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testLessonTimetable.getDayOfWeek()).isEqualTo(UPDATED_DAY_OF_WEEK);
        assertThat(testLessonTimetable.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLessonTimetable.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testLessonTimetable.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testLessonTimetable.getRegisterTaken()).isEqualTo(UPDATED_REGISTER_TAKEN);
    }

    @Test
    @Transactional
    void fullUpdateLessonTimetableWithPatch() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        int databaseSizeBeforeUpdate = lessonTimetableRepository.findAll().size();

        // Update the lessonTimetable using partial update
        LessonTimetable partialUpdatedLessonTimetable = new LessonTimetable();
        partialUpdatedLessonTimetable.setId(lessonTimetable.getId());

        partialUpdatedLessonTimetable
            .lessonName(UPDATED_LESSON_NAME)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .active(UPDATED_ACTIVE)
            .registerTaken(UPDATED_REGISTER_TAKEN);

        restLessonTimetableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLessonTimetable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLessonTimetable))
            )
            .andExpect(status().isOk());

        // Validate the LessonTimetable in the database
        List<LessonTimetable> lessonTimetableList = lessonTimetableRepository.findAll();
        assertThat(lessonTimetableList).hasSize(databaseSizeBeforeUpdate);
        LessonTimetable testLessonTimetable = lessonTimetableList.get(lessonTimetableList.size() - 1);
        assertThat(testLessonTimetable.getLessonName()).isEqualTo(UPDATED_LESSON_NAME);
        assertThat(testLessonTimetable.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testLessonTimetable.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testLessonTimetable.getDayOfWeek()).isEqualTo(UPDATED_DAY_OF_WEEK);
        assertThat(testLessonTimetable.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLessonTimetable.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testLessonTimetable.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testLessonTimetable.getRegisterTaken()).isEqualTo(UPDATED_REGISTER_TAKEN);
    }

    @Test
    @Transactional
    void patchNonExistingLessonTimetable() throws Exception {
        int databaseSizeBeforeUpdate = lessonTimetableRepository.findAll().size();
        lessonTimetable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonTimetableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lessonTimetable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetable))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTimetable in the database
        List<LessonTimetable> lessonTimetableList = lessonTimetableRepository.findAll();
        assertThat(lessonTimetableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLessonTimetable() throws Exception {
        int databaseSizeBeforeUpdate = lessonTimetableRepository.findAll().size();
        lessonTimetable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonTimetableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetable))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTimetable in the database
        List<LessonTimetable> lessonTimetableList = lessonTimetableRepository.findAll();
        assertThat(lessonTimetableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLessonTimetable() throws Exception {
        int databaseSizeBeforeUpdate = lessonTimetableRepository.findAll().size();
        lessonTimetable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonTimetableMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LessonTimetable in the database
        List<LessonTimetable> lessonTimetableList = lessonTimetableRepository.findAll();
        assertThat(lessonTimetableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLessonTimetable() throws Exception {
        // Initialize the database
        lessonTimetableRepository.saveAndFlush(lessonTimetable);

        int databaseSizeBeforeDelete = lessonTimetableRepository.findAll().size();

        // Delete the lessonTimetable
        restLessonTimetableMockMvc
            .perform(delete(ENTITY_API_URL_ID, lessonTimetable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LessonTimetable> lessonTimetableList = lessonTimetableRepository.findAll();
        assertThat(lessonTimetableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
