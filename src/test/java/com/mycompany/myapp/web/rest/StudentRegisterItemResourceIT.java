package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.StudentRegister;
import com.mycompany.myapp.repository.StudentRegisterRepository;
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
class StudentRegisterItemResourceIT {

    private static final Instant DEFAULT_DATE_OF_LESSON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_OF_LESSON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

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
            .andExpect(jsonPath("$.attended").value(DEFAULT_ATTENDED.booleanValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()));
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

        partialUpdatedStudentRegister.attended(UPDATED_ATTENDED).createdOn(UPDATED_CREATED_ON);

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
        assertThat(testStudentRegister.getAttended()).isEqualTo(UPDATED_ATTENDED);
        assertThat(testStudentRegister.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testStudentRegister.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
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
