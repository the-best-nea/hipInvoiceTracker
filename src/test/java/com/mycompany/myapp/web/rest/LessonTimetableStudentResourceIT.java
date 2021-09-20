package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LessonTimetableStudent;
import com.mycompany.myapp.repository.LessonTimetableStudentRepository;
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
 * Integration tests for the {@link LessonTimetableStudentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LessonTimetableStudentResourceIT {

    private static final Float DEFAULT_PAY = 1F;
    private static final Float UPDATED_PAY = 2F;

    private static final String ENTITY_API_URL = "/api/lesson-timetable-students";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LessonTimetableStudentRepository lessonTimetableStudentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLessonTimetableStudentMockMvc;

    private LessonTimetableStudent lessonTimetableStudent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LessonTimetableStudent createEntity(EntityManager em) {
        LessonTimetableStudent lessonTimetableStudent = new LessonTimetableStudent().pay(DEFAULT_PAY);
        return lessonTimetableStudent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LessonTimetableStudent createUpdatedEntity(EntityManager em) {
        LessonTimetableStudent lessonTimetableStudent = new LessonTimetableStudent().pay(UPDATED_PAY);
        return lessonTimetableStudent;
    }

    @BeforeEach
    public void initTest() {
        lessonTimetableStudent = createEntity(em);
    }

    @Test
    @Transactional
    void createLessonTimetableStudent() throws Exception {
        int databaseSizeBeforeCreate = lessonTimetableStudentRepository.findAll().size();
        // Create the LessonTimetableStudent
        restLessonTimetableStudentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetableStudent))
            )
            .andExpect(status().isCreated());

        // Validate the LessonTimetableStudent in the database
        List<LessonTimetableStudent> lessonTimetableStudentList = lessonTimetableStudentRepository.findAll();
        assertThat(lessonTimetableStudentList).hasSize(databaseSizeBeforeCreate + 1);
        LessonTimetableStudent testLessonTimetableStudent = lessonTimetableStudentList.get(lessonTimetableStudentList.size() - 1);
        assertThat(testLessonTimetableStudent.getPay()).isEqualTo(DEFAULT_PAY);
    }

    @Test
    @Transactional
    void createLessonTimetableStudentWithExistingId() throws Exception {
        // Create the LessonTimetableStudent with an existing ID
        lessonTimetableStudent.setId(1L);

        int databaseSizeBeforeCreate = lessonTimetableStudentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLessonTimetableStudentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetableStudent))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTimetableStudent in the database
        List<LessonTimetableStudent> lessonTimetableStudentList = lessonTimetableStudentRepository.findAll();
        assertThat(lessonTimetableStudentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLessonTimetableStudents() throws Exception {
        // Initialize the database
        lessonTimetableStudentRepository.saveAndFlush(lessonTimetableStudent);

        // Get all the lessonTimetableStudentList
        restLessonTimetableStudentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lessonTimetableStudent.getId().intValue())))
            .andExpect(jsonPath("$.[*].pay").value(hasItem(DEFAULT_PAY.doubleValue())));
    }

    @Test
    @Transactional
    void getLessonTimetableStudent() throws Exception {
        // Initialize the database
        lessonTimetableStudentRepository.saveAndFlush(lessonTimetableStudent);

        // Get the lessonTimetableStudent
        restLessonTimetableStudentMockMvc
            .perform(get(ENTITY_API_URL_ID, lessonTimetableStudent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lessonTimetableStudent.getId().intValue()))
            .andExpect(jsonPath("$.pay").value(DEFAULT_PAY.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingLessonTimetableStudent() throws Exception {
        // Get the lessonTimetableStudent
        restLessonTimetableStudentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLessonTimetableStudent() throws Exception {
        // Initialize the database
        lessonTimetableStudentRepository.saveAndFlush(lessonTimetableStudent);

        int databaseSizeBeforeUpdate = lessonTimetableStudentRepository.findAll().size();

        // Update the lessonTimetableStudent
        LessonTimetableStudent updatedLessonTimetableStudent = lessonTimetableStudentRepository
            .findById(lessonTimetableStudent.getId())
            .get();
        // Disconnect from session so that the updates on updatedLessonTimetableStudent are not directly saved in db
        em.detach(updatedLessonTimetableStudent);
        updatedLessonTimetableStudent.pay(UPDATED_PAY);

        restLessonTimetableStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLessonTimetableStudent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLessonTimetableStudent))
            )
            .andExpect(status().isOk());

        // Validate the LessonTimetableStudent in the database
        List<LessonTimetableStudent> lessonTimetableStudentList = lessonTimetableStudentRepository.findAll();
        assertThat(lessonTimetableStudentList).hasSize(databaseSizeBeforeUpdate);
        LessonTimetableStudent testLessonTimetableStudent = lessonTimetableStudentList.get(lessonTimetableStudentList.size() - 1);
        assertThat(testLessonTimetableStudent.getPay()).isEqualTo(UPDATED_PAY);
    }

    @Test
    @Transactional
    void putNonExistingLessonTimetableStudent() throws Exception {
        int databaseSizeBeforeUpdate = lessonTimetableStudentRepository.findAll().size();
        lessonTimetableStudent.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonTimetableStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lessonTimetableStudent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetableStudent))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTimetableStudent in the database
        List<LessonTimetableStudent> lessonTimetableStudentList = lessonTimetableStudentRepository.findAll();
        assertThat(lessonTimetableStudentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLessonTimetableStudent() throws Exception {
        int databaseSizeBeforeUpdate = lessonTimetableStudentRepository.findAll().size();
        lessonTimetableStudent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonTimetableStudentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetableStudent))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTimetableStudent in the database
        List<LessonTimetableStudent> lessonTimetableStudentList = lessonTimetableStudentRepository.findAll();
        assertThat(lessonTimetableStudentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLessonTimetableStudent() throws Exception {
        int databaseSizeBeforeUpdate = lessonTimetableStudentRepository.findAll().size();
        lessonTimetableStudent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonTimetableStudentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetableStudent))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LessonTimetableStudent in the database
        List<LessonTimetableStudent> lessonTimetableStudentList = lessonTimetableStudentRepository.findAll();
        assertThat(lessonTimetableStudentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLessonTimetableStudentWithPatch() throws Exception {
        // Initialize the database
        lessonTimetableStudentRepository.saveAndFlush(lessonTimetableStudent);

        int databaseSizeBeforeUpdate = lessonTimetableStudentRepository.findAll().size();

        // Update the lessonTimetableStudent using partial update
        LessonTimetableStudent partialUpdatedLessonTimetableStudent = new LessonTimetableStudent();
        partialUpdatedLessonTimetableStudent.setId(lessonTimetableStudent.getId());

        restLessonTimetableStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLessonTimetableStudent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLessonTimetableStudent))
            )
            .andExpect(status().isOk());

        // Validate the LessonTimetableStudent in the database
        List<LessonTimetableStudent> lessonTimetableStudentList = lessonTimetableStudentRepository.findAll();
        assertThat(lessonTimetableStudentList).hasSize(databaseSizeBeforeUpdate);
        LessonTimetableStudent testLessonTimetableStudent = lessonTimetableStudentList.get(lessonTimetableStudentList.size() - 1);
        assertThat(testLessonTimetableStudent.getPay()).isEqualTo(DEFAULT_PAY);
    }

    @Test
    @Transactional
    void fullUpdateLessonTimetableStudentWithPatch() throws Exception {
        // Initialize the database
        lessonTimetableStudentRepository.saveAndFlush(lessonTimetableStudent);

        int databaseSizeBeforeUpdate = lessonTimetableStudentRepository.findAll().size();

        // Update the lessonTimetableStudent using partial update
        LessonTimetableStudent partialUpdatedLessonTimetableStudent = new LessonTimetableStudent();
        partialUpdatedLessonTimetableStudent.setId(lessonTimetableStudent.getId());

        partialUpdatedLessonTimetableStudent.pay(UPDATED_PAY);

        restLessonTimetableStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLessonTimetableStudent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLessonTimetableStudent))
            )
            .andExpect(status().isOk());

        // Validate the LessonTimetableStudent in the database
        List<LessonTimetableStudent> lessonTimetableStudentList = lessonTimetableStudentRepository.findAll();
        assertThat(lessonTimetableStudentList).hasSize(databaseSizeBeforeUpdate);
        LessonTimetableStudent testLessonTimetableStudent = lessonTimetableStudentList.get(lessonTimetableStudentList.size() - 1);
        assertThat(testLessonTimetableStudent.getPay()).isEqualTo(UPDATED_PAY);
    }

    @Test
    @Transactional
    void patchNonExistingLessonTimetableStudent() throws Exception {
        int databaseSizeBeforeUpdate = lessonTimetableStudentRepository.findAll().size();
        lessonTimetableStudent.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonTimetableStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lessonTimetableStudent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetableStudent))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTimetableStudent in the database
        List<LessonTimetableStudent> lessonTimetableStudentList = lessonTimetableStudentRepository.findAll();
        assertThat(lessonTimetableStudentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLessonTimetableStudent() throws Exception {
        int databaseSizeBeforeUpdate = lessonTimetableStudentRepository.findAll().size();
        lessonTimetableStudent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonTimetableStudentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetableStudent))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonTimetableStudent in the database
        List<LessonTimetableStudent> lessonTimetableStudentList = lessonTimetableStudentRepository.findAll();
        assertThat(lessonTimetableStudentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLessonTimetableStudent() throws Exception {
        int databaseSizeBeforeUpdate = lessonTimetableStudentRepository.findAll().size();
        lessonTimetableStudent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonTimetableStudentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lessonTimetableStudent))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LessonTimetableStudent in the database
        List<LessonTimetableStudent> lessonTimetableStudentList = lessonTimetableStudentRepository.findAll();
        assertThat(lessonTimetableStudentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLessonTimetableStudent() throws Exception {
        // Initialize the database
        lessonTimetableStudentRepository.saveAndFlush(lessonTimetableStudent);

        int databaseSizeBeforeDelete = lessonTimetableStudentRepository.findAll().size();

        // Delete the lessonTimetableStudent
        restLessonTimetableStudentMockMvc
            .perform(delete(ENTITY_API_URL_ID, lessonTimetableStudent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LessonTimetableStudent> lessonTimetableStudentList = lessonTimetableStudentRepository.findAll();
        assertThat(lessonTimetableStudentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
