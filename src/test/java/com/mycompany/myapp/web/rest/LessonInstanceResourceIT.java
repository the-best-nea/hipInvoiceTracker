package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LessonInstance;
import com.mycompany.myapp.domain.enumeration.DayOfWeek;
import com.mycompany.myapp.repository.LessonInstanceRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LessonInstanceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LessonInstanceResourceIT {

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

    private static final Instant DEFAULT_CRETED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CRETED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/lesson-instances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LessonInstanceRepository lessonInstanceRepository;

    @Mock
    private LessonInstanceRepository lessonInstanceRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLessonInstanceMockMvc;

    private LessonInstance lessonInstance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LessonInstance createEntity(EntityManager em) {
        LessonInstance lessonInstance = new LessonInstance()
            .lessonName(DEFAULT_LESSON_NAME)
            .startAt(DEFAULT_START_AT)
            .endAt(DEFAULT_END_AT)
            .dayOfWeek(DEFAULT_DAY_OF_WEEK)
            .description(DEFAULT_DESCRIPTION)
            .cretedOn(DEFAULT_CRETED_ON);
        return lessonInstance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LessonInstance createUpdatedEntity(EntityManager em) {
        LessonInstance lessonInstance = new LessonInstance()
            .lessonName(UPDATED_LESSON_NAME)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .description(UPDATED_DESCRIPTION)
            .cretedOn(UPDATED_CRETED_ON);
        return lessonInstance;
    }

    @BeforeEach
    public void initTest() {
        lessonInstance = createEntity(em);
    }

    @Test
    @Transactional
    void createLessonInstance() throws Exception {
        int databaseSizeBeforeCreate = lessonInstanceRepository.findAll().size();
        // Create the LessonInstance
        restLessonInstanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lessonInstance))
            )
            .andExpect(status().isCreated());

        // Validate the LessonInstance in the database
        List<LessonInstance> lessonInstanceList = lessonInstanceRepository.findAll();
        assertThat(lessonInstanceList).hasSize(databaseSizeBeforeCreate + 1);
        LessonInstance testLessonInstance = lessonInstanceList.get(lessonInstanceList.size() - 1);
        assertThat(testLessonInstance.getLessonName()).isEqualTo(DEFAULT_LESSON_NAME);
        assertThat(testLessonInstance.getStartAt()).isEqualTo(DEFAULT_START_AT);
        assertThat(testLessonInstance.getEndAt()).isEqualTo(DEFAULT_END_AT);
        assertThat(testLessonInstance.getDayOfWeek()).isEqualTo(DEFAULT_DAY_OF_WEEK);
        assertThat(testLessonInstance.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLessonInstance.getCretedOn()).isEqualTo(DEFAULT_CRETED_ON);
    }

    @Test
    @Transactional
    void createLessonInstanceWithExistingId() throws Exception {
        // Create the LessonInstance with an existing ID
        lessonInstance.setId(1L);

        int databaseSizeBeforeCreate = lessonInstanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLessonInstanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lessonInstance))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonInstance in the database
        List<LessonInstance> lessonInstanceList = lessonInstanceRepository.findAll();
        assertThat(lessonInstanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLessonNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonInstanceRepository.findAll().size();
        // set the field null
        lessonInstance.setLessonName(null);

        // Create the LessonInstance, which fails.

        restLessonInstanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lessonInstance))
            )
            .andExpect(status().isBadRequest());

        List<LessonInstance> lessonInstanceList = lessonInstanceRepository.findAll();
        assertThat(lessonInstanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonInstanceRepository.findAll().size();
        // set the field null
        lessonInstance.setStartAt(null);

        // Create the LessonInstance, which fails.

        restLessonInstanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lessonInstance))
            )
            .andExpect(status().isBadRequest());

        List<LessonInstance> lessonInstanceList = lessonInstanceRepository.findAll();
        assertThat(lessonInstanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonInstanceRepository.findAll().size();
        // set the field null
        lessonInstance.setEndAt(null);

        // Create the LessonInstance, which fails.

        restLessonInstanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lessonInstance))
            )
            .andExpect(status().isBadRequest());

        List<LessonInstance> lessonInstanceList = lessonInstanceRepository.findAll();
        assertThat(lessonInstanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDayOfWeekIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonInstanceRepository.findAll().size();
        // set the field null
        lessonInstance.setDayOfWeek(null);

        // Create the LessonInstance, which fails.

        restLessonInstanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lessonInstance))
            )
            .andExpect(status().isBadRequest());

        List<LessonInstance> lessonInstanceList = lessonInstanceRepository.findAll();
        assertThat(lessonInstanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCretedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = lessonInstanceRepository.findAll().size();
        // set the field null
        lessonInstance.setCretedOn(null);

        // Create the LessonInstance, which fails.

        restLessonInstanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lessonInstance))
            )
            .andExpect(status().isBadRequest());

        List<LessonInstance> lessonInstanceList = lessonInstanceRepository.findAll();
        assertThat(lessonInstanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLessonInstances() throws Exception {
        // Initialize the database
        lessonInstanceRepository.saveAndFlush(lessonInstance);

        // Get all the lessonInstanceList
        restLessonInstanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lessonInstance.getId().intValue())))
            .andExpect(jsonPath("$.[*].lessonName").value(hasItem(DEFAULT_LESSON_NAME)))
            .andExpect(jsonPath("$.[*].startAt").value(hasItem(DEFAULT_START_AT.toString())))
            .andExpect(jsonPath("$.[*].endAt").value(hasItem(DEFAULT_END_AT.toString())))
            .andExpect(jsonPath("$.[*].dayOfWeek").value(hasItem(DEFAULT_DAY_OF_WEEK.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cretedOn").value(hasItem(DEFAULT_CRETED_ON.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLessonInstancesWithEagerRelationshipsIsEnabled() throws Exception {
        when(lessonInstanceRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLessonInstanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(lessonInstanceRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLessonInstancesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(lessonInstanceRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLessonInstanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(lessonInstanceRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getLessonInstance() throws Exception {
        // Initialize the database
        lessonInstanceRepository.saveAndFlush(lessonInstance);

        // Get the lessonInstance
        restLessonInstanceMockMvc
            .perform(get(ENTITY_API_URL_ID, lessonInstance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lessonInstance.getId().intValue()))
            .andExpect(jsonPath("$.lessonName").value(DEFAULT_LESSON_NAME))
            .andExpect(jsonPath("$.startAt").value(DEFAULT_START_AT.toString()))
            .andExpect(jsonPath("$.endAt").value(DEFAULT_END_AT.toString()))
            .andExpect(jsonPath("$.dayOfWeek").value(DEFAULT_DAY_OF_WEEK.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.cretedOn").value(DEFAULT_CRETED_ON.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLessonInstance() throws Exception {
        // Get the lessonInstance
        restLessonInstanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLessonInstance() throws Exception {
        // Initialize the database
        lessonInstanceRepository.saveAndFlush(lessonInstance);

        int databaseSizeBeforeUpdate = lessonInstanceRepository.findAll().size();

        // Update the lessonInstance
        LessonInstance updatedLessonInstance = lessonInstanceRepository.findById(lessonInstance.getId()).get();
        // Disconnect from session so that the updates on updatedLessonInstance are not directly saved in db
        em.detach(updatedLessonInstance);
        updatedLessonInstance
            .lessonName(UPDATED_LESSON_NAME)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .description(UPDATED_DESCRIPTION)
            .cretedOn(UPDATED_CRETED_ON);

        restLessonInstanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLessonInstance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLessonInstance))
            )
            .andExpect(status().isOk());

        // Validate the LessonInstance in the database
        List<LessonInstance> lessonInstanceList = lessonInstanceRepository.findAll();
        assertThat(lessonInstanceList).hasSize(databaseSizeBeforeUpdate);
        LessonInstance testLessonInstance = lessonInstanceList.get(lessonInstanceList.size() - 1);
        assertThat(testLessonInstance.getLessonName()).isEqualTo(UPDATED_LESSON_NAME);
        assertThat(testLessonInstance.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testLessonInstance.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testLessonInstance.getDayOfWeek()).isEqualTo(UPDATED_DAY_OF_WEEK);
        assertThat(testLessonInstance.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLessonInstance.getCretedOn()).isEqualTo(UPDATED_CRETED_ON);
    }

    @Test
    @Transactional
    void putNonExistingLessonInstance() throws Exception {
        int databaseSizeBeforeUpdate = lessonInstanceRepository.findAll().size();
        lessonInstance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonInstanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lessonInstance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonInstance))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonInstance in the database
        List<LessonInstance> lessonInstanceList = lessonInstanceRepository.findAll();
        assertThat(lessonInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLessonInstance() throws Exception {
        int databaseSizeBeforeUpdate = lessonInstanceRepository.findAll().size();
        lessonInstance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonInstanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonInstance))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonInstance in the database
        List<LessonInstance> lessonInstanceList = lessonInstanceRepository.findAll();
        assertThat(lessonInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLessonInstance() throws Exception {
        int databaseSizeBeforeUpdate = lessonInstanceRepository.findAll().size();
        lessonInstance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonInstanceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lessonInstance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LessonInstance in the database
        List<LessonInstance> lessonInstanceList = lessonInstanceRepository.findAll();
        assertThat(lessonInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLessonInstanceWithPatch() throws Exception {
        // Initialize the database
        lessonInstanceRepository.saveAndFlush(lessonInstance);

        int databaseSizeBeforeUpdate = lessonInstanceRepository.findAll().size();

        // Update the lessonInstance using partial update
        LessonInstance partialUpdatedLessonInstance = new LessonInstance();
        partialUpdatedLessonInstance.setId(lessonInstance.getId());

        partialUpdatedLessonInstance.endAt(UPDATED_END_AT).dayOfWeek(UPDATED_DAY_OF_WEEK).description(UPDATED_DESCRIPTION);

        restLessonInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLessonInstance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLessonInstance))
            )
            .andExpect(status().isOk());

        // Validate the LessonInstance in the database
        List<LessonInstance> lessonInstanceList = lessonInstanceRepository.findAll();
        assertThat(lessonInstanceList).hasSize(databaseSizeBeforeUpdate);
        LessonInstance testLessonInstance = lessonInstanceList.get(lessonInstanceList.size() - 1);
        assertThat(testLessonInstance.getLessonName()).isEqualTo(DEFAULT_LESSON_NAME);
        assertThat(testLessonInstance.getStartAt()).isEqualTo(DEFAULT_START_AT);
        assertThat(testLessonInstance.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testLessonInstance.getDayOfWeek()).isEqualTo(UPDATED_DAY_OF_WEEK);
        assertThat(testLessonInstance.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLessonInstance.getCretedOn()).isEqualTo(DEFAULT_CRETED_ON);
    }

    @Test
    @Transactional
    void fullUpdateLessonInstanceWithPatch() throws Exception {
        // Initialize the database
        lessonInstanceRepository.saveAndFlush(lessonInstance);

        int databaseSizeBeforeUpdate = lessonInstanceRepository.findAll().size();

        // Update the lessonInstance using partial update
        LessonInstance partialUpdatedLessonInstance = new LessonInstance();
        partialUpdatedLessonInstance.setId(lessonInstance.getId());

        partialUpdatedLessonInstance
            .lessonName(UPDATED_LESSON_NAME)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .description(UPDATED_DESCRIPTION)
            .cretedOn(UPDATED_CRETED_ON);

        restLessonInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLessonInstance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLessonInstance))
            )
            .andExpect(status().isOk());

        // Validate the LessonInstance in the database
        List<LessonInstance> lessonInstanceList = lessonInstanceRepository.findAll();
        assertThat(lessonInstanceList).hasSize(databaseSizeBeforeUpdate);
        LessonInstance testLessonInstance = lessonInstanceList.get(lessonInstanceList.size() - 1);
        assertThat(testLessonInstance.getLessonName()).isEqualTo(UPDATED_LESSON_NAME);
        assertThat(testLessonInstance.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testLessonInstance.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testLessonInstance.getDayOfWeek()).isEqualTo(UPDATED_DAY_OF_WEEK);
        assertThat(testLessonInstance.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLessonInstance.getCretedOn()).isEqualTo(UPDATED_CRETED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingLessonInstance() throws Exception {
        int databaseSizeBeforeUpdate = lessonInstanceRepository.findAll().size();
        lessonInstance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lessonInstance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lessonInstance))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonInstance in the database
        List<LessonInstance> lessonInstanceList = lessonInstanceRepository.findAll();
        assertThat(lessonInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLessonInstance() throws Exception {
        int databaseSizeBeforeUpdate = lessonInstanceRepository.findAll().size();
        lessonInstance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lessonInstance))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonInstance in the database
        List<LessonInstance> lessonInstanceList = lessonInstanceRepository.findAll();
        assertThat(lessonInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLessonInstance() throws Exception {
        int databaseSizeBeforeUpdate = lessonInstanceRepository.findAll().size();
        lessonInstance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lessonInstance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LessonInstance in the database
        List<LessonInstance> lessonInstanceList = lessonInstanceRepository.findAll();
        assertThat(lessonInstanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLessonInstance() throws Exception {
        // Initialize the database
        lessonInstanceRepository.saveAndFlush(lessonInstance);

        int databaseSizeBeforeDelete = lessonInstanceRepository.findAll().size();

        // Delete the lessonInstance
        restLessonInstanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, lessonInstance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LessonInstance> lessonInstanceList = lessonInstanceRepository.findAll();
        assertThat(lessonInstanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
