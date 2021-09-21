package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LessonTimetable;
import com.mycompany.myapp.domain.LessonTimetableStudent;
import com.mycompany.myapp.domain.Student;
import com.mycompany.myapp.repository.LessonTimetableStudentRepository;
import com.mycompany.myapp.service.criteria.LessonTimetableStudentCriteria;
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
    private static final Float SMALLER_PAY = 1F - 1F;

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
    void getLessonTimetableStudentsByIdFiltering() throws Exception {
        // Initialize the database
        lessonTimetableStudentRepository.saveAndFlush(lessonTimetableStudent);

        Long id = lessonTimetableStudent.getId();

        defaultLessonTimetableStudentShouldBeFound("id.equals=" + id);
        defaultLessonTimetableStudentShouldNotBeFound("id.notEquals=" + id);

        defaultLessonTimetableStudentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLessonTimetableStudentShouldNotBeFound("id.greaterThan=" + id);

        defaultLessonTimetableStudentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLessonTimetableStudentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLessonTimetableStudentsByPayIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableStudentRepository.saveAndFlush(lessonTimetableStudent);

        // Get all the lessonTimetableStudentList where pay equals to DEFAULT_PAY
        defaultLessonTimetableStudentShouldBeFound("pay.equals=" + DEFAULT_PAY);

        // Get all the lessonTimetableStudentList where pay equals to UPDATED_PAY
        defaultLessonTimetableStudentShouldNotBeFound("pay.equals=" + UPDATED_PAY);
    }

    @Test
    @Transactional
    void getAllLessonTimetableStudentsByPayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableStudentRepository.saveAndFlush(lessonTimetableStudent);

        // Get all the lessonTimetableStudentList where pay not equals to DEFAULT_PAY
        defaultLessonTimetableStudentShouldNotBeFound("pay.notEquals=" + DEFAULT_PAY);

        // Get all the lessonTimetableStudentList where pay not equals to UPDATED_PAY
        defaultLessonTimetableStudentShouldBeFound("pay.notEquals=" + UPDATED_PAY);
    }

    @Test
    @Transactional
    void getAllLessonTimetableStudentsByPayIsInShouldWork() throws Exception {
        // Initialize the database
        lessonTimetableStudentRepository.saveAndFlush(lessonTimetableStudent);

        // Get all the lessonTimetableStudentList where pay in DEFAULT_PAY or UPDATED_PAY
        defaultLessonTimetableStudentShouldBeFound("pay.in=" + DEFAULT_PAY + "," + UPDATED_PAY);

        // Get all the lessonTimetableStudentList where pay equals to UPDATED_PAY
        defaultLessonTimetableStudentShouldNotBeFound("pay.in=" + UPDATED_PAY);
    }

    @Test
    @Transactional
    void getAllLessonTimetableStudentsByPayIsNullOrNotNull() throws Exception {
        // Initialize the database
        lessonTimetableStudentRepository.saveAndFlush(lessonTimetableStudent);

        // Get all the lessonTimetableStudentList where pay is not null
        defaultLessonTimetableStudentShouldBeFound("pay.specified=true");

        // Get all the lessonTimetableStudentList where pay is null
        defaultLessonTimetableStudentShouldNotBeFound("pay.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonTimetableStudentsByPayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableStudentRepository.saveAndFlush(lessonTimetableStudent);

        // Get all the lessonTimetableStudentList where pay is greater than or equal to DEFAULT_PAY
        defaultLessonTimetableStudentShouldBeFound("pay.greaterThanOrEqual=" + DEFAULT_PAY);

        // Get all the lessonTimetableStudentList where pay is greater than or equal to UPDATED_PAY
        defaultLessonTimetableStudentShouldNotBeFound("pay.greaterThanOrEqual=" + UPDATED_PAY);
    }

    @Test
    @Transactional
    void getAllLessonTimetableStudentsByPayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableStudentRepository.saveAndFlush(lessonTimetableStudent);

        // Get all the lessonTimetableStudentList where pay is less than or equal to DEFAULT_PAY
        defaultLessonTimetableStudentShouldBeFound("pay.lessThanOrEqual=" + DEFAULT_PAY);

        // Get all the lessonTimetableStudentList where pay is less than or equal to SMALLER_PAY
        defaultLessonTimetableStudentShouldNotBeFound("pay.lessThanOrEqual=" + SMALLER_PAY);
    }

    @Test
    @Transactional
    void getAllLessonTimetableStudentsByPayIsLessThanSomething() throws Exception {
        // Initialize the database
        lessonTimetableStudentRepository.saveAndFlush(lessonTimetableStudent);

        // Get all the lessonTimetableStudentList where pay is less than DEFAULT_PAY
        defaultLessonTimetableStudentShouldNotBeFound("pay.lessThan=" + DEFAULT_PAY);

        // Get all the lessonTimetableStudentList where pay is less than UPDATED_PAY
        defaultLessonTimetableStudentShouldBeFound("pay.lessThan=" + UPDATED_PAY);
    }

    @Test
    @Transactional
    void getAllLessonTimetableStudentsByPayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        lessonTimetableStudentRepository.saveAndFlush(lessonTimetableStudent);

        // Get all the lessonTimetableStudentList where pay is greater than DEFAULT_PAY
        defaultLessonTimetableStudentShouldNotBeFound("pay.greaterThan=" + DEFAULT_PAY);

        // Get all the lessonTimetableStudentList where pay is greater than SMALLER_PAY
        defaultLessonTimetableStudentShouldBeFound("pay.greaterThan=" + SMALLER_PAY);
    }

    @Test
    @Transactional
    void getAllLessonTimetableStudentsByStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableStudentRepository.saveAndFlush(lessonTimetableStudent);
        Student student = StudentResourceIT.createEntity(em);
        em.persist(student);
        em.flush();
        lessonTimetableStudent.setStudent(student);
        lessonTimetableStudentRepository.saveAndFlush(lessonTimetableStudent);
        Long studentId = student.getId();

        // Get all the lessonTimetableStudentList where student equals to studentId
        defaultLessonTimetableStudentShouldBeFound("studentId.equals=" + studentId);

        // Get all the lessonTimetableStudentList where student equals to (studentId + 1)
        defaultLessonTimetableStudentShouldNotBeFound("studentId.equals=" + (studentId + 1));
    }

    @Test
    @Transactional
    void getAllLessonTimetableStudentsByLessonTimetableIsEqualToSomething() throws Exception {
        // Initialize the database
        lessonTimetableStudentRepository.saveAndFlush(lessonTimetableStudent);
        LessonTimetable lessonTimetable = LessonTimetableResourceIT.createEntity(em);
        em.persist(lessonTimetable);
        em.flush();
        lessonTimetableStudent.setLessonTimetable(lessonTimetable);
        lessonTimetableStudentRepository.saveAndFlush(lessonTimetableStudent);
        Long lessonTimetableId = lessonTimetable.getId();

        // Get all the lessonTimetableStudentList where lessonTimetable equals to lessonTimetableId
        defaultLessonTimetableStudentShouldBeFound("lessonTimetableId.equals=" + lessonTimetableId);

        // Get all the lessonTimetableStudentList where lessonTimetable equals to (lessonTimetableId + 1)
        defaultLessonTimetableStudentShouldNotBeFound("lessonTimetableId.equals=" + (lessonTimetableId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLessonTimetableStudentShouldBeFound(String filter) throws Exception {
        restLessonTimetableStudentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lessonTimetableStudent.getId().intValue())))
            .andExpect(jsonPath("$.[*].pay").value(hasItem(DEFAULT_PAY.doubleValue())));

        // Check, that the count call also returns 1
        restLessonTimetableStudentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLessonTimetableStudentShouldNotBeFound(String filter) throws Exception {
        restLessonTimetableStudentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLessonTimetableStudentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
