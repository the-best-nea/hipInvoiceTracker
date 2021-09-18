package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LessonTimetable;
import com.mycompany.myapp.domain.enumeration.DayOfWeek;
import com.mycompany.myapp.repository.LessonTimetableRepository;
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
            .active(DEFAULT_ACTIVE);
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
            .active(UPDATED_ACTIVE);
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
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
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
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
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
            .active(UPDATED_ACTIVE);

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
            .active(UPDATED_ACTIVE);

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
            .active(UPDATED_ACTIVE);

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
