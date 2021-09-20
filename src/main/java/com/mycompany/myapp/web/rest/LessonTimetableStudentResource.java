package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.LessonTimetableStudent;
import com.mycompany.myapp.repository.LessonTimetableStudentRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.LessonTimetableStudent}.
 */
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
@Transactional
public class LessonTimetableStudentResource {

    private final Logger log = LoggerFactory.getLogger(LessonTimetableStudentResource.class);

    private static final String ENTITY_NAME = "lessonTimetableStudent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LessonTimetableStudentRepository lessonTimetableStudentRepository;

    public LessonTimetableStudentResource(LessonTimetableStudentRepository lessonTimetableStudentRepository) {
        this.lessonTimetableStudentRepository = lessonTimetableStudentRepository;
    }

    /**
     * {@code POST  /lesson-timetable-students} : Create a new lessonTimetableStudent.
     *
     * @param lessonTimetableStudent the lessonTimetableStudent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lessonTimetableStudent, or with status {@code 400 (Bad Request)} if the lessonTimetableStudent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lesson-timetable-students")
    public ResponseEntity<LessonTimetableStudent> createLessonTimetableStudent(@RequestBody LessonTimetableStudent lessonTimetableStudent)
        throws URISyntaxException {
        log.debug("REST request to save LessonTimetableStudent : {}", lessonTimetableStudent);
        if (lessonTimetableStudent.getId() != null) {
            throw new BadRequestAlertException("A new lessonTimetableStudent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LessonTimetableStudent result = lessonTimetableStudentRepository.save(lessonTimetableStudent);
        return ResponseEntity
            .created(new URI("/api/lesson-timetable-students/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lesson-timetable-students/:id} : Updates an existing lessonTimetableStudent.
     *
     * @param id the id of the lessonTimetableStudent to save.
     * @param lessonTimetableStudent the lessonTimetableStudent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lessonTimetableStudent,
     * or with status {@code 400 (Bad Request)} if the lessonTimetableStudent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lessonTimetableStudent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lesson-timetable-students/{id}")
    public ResponseEntity<LessonTimetableStudent> updateLessonTimetableStudent(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LessonTimetableStudent lessonTimetableStudent
    ) throws URISyntaxException {
        log.debug("REST request to update LessonTimetableStudent : {}, {}", id, lessonTimetableStudent);
        if (lessonTimetableStudent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lessonTimetableStudent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lessonTimetableStudentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LessonTimetableStudent result = lessonTimetableStudentRepository.save(lessonTimetableStudent);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lessonTimetableStudent.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lesson-timetable-students/:id} : Partial updates given fields of an existing lessonTimetableStudent, field will ignore if it is null
     *
     * @param id the id of the lessonTimetableStudent to save.
     * @param lessonTimetableStudent the lessonTimetableStudent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lessonTimetableStudent,
     * or with status {@code 400 (Bad Request)} if the lessonTimetableStudent is not valid,
     * or with status {@code 404 (Not Found)} if the lessonTimetableStudent is not found,
     * or with status {@code 500 (Internal Server Error)} if the lessonTimetableStudent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lesson-timetable-students/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LessonTimetableStudent> partialUpdateLessonTimetableStudent(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LessonTimetableStudent lessonTimetableStudent
    ) throws URISyntaxException {
        log.debug("REST request to partial update LessonTimetableStudent partially : {}, {}", id, lessonTimetableStudent);
        if (lessonTimetableStudent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lessonTimetableStudent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lessonTimetableStudentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LessonTimetableStudent> result = lessonTimetableStudentRepository
            .findById(lessonTimetableStudent.getId())
            .map(
                existingLessonTimetableStudent -> {
                    if (lessonTimetableStudent.getPay() != null) {
                        existingLessonTimetableStudent.setPay(lessonTimetableStudent.getPay());
                    }

                    return existingLessonTimetableStudent;
                }
            )
            .map(lessonTimetableStudentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lessonTimetableStudent.getId().toString())
        );
    }

    /**
     * {@code GET  /lesson-timetable-students} : get all the lessonTimetableStudents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lessonTimetableStudents in body.
     */
    @GetMapping("/lesson-timetable-students")
    public List<LessonTimetableStudent> getAllLessonTimetableStudents() {
        log.debug("REST request to get all LessonTimetableStudents");
        return lessonTimetableStudentRepository.findAll();
    }

    /**
     * {@code GET  /lesson-timetable-students/:id} : get the "id" lessonTimetableStudent.
     *
     * @param id the id of the lessonTimetableStudent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lessonTimetableStudent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lesson-timetable-students/{id}")
    public ResponseEntity<LessonTimetableStudent> getLessonTimetableStudent(@PathVariable Long id) {
        log.debug("REST request to get LessonTimetableStudent : {}", id);
        Optional<LessonTimetableStudent> lessonTimetableStudent = lessonTimetableStudentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lessonTimetableStudent);
    }

    /**
     * {@code DELETE  /lesson-timetable-students/:id} : delete the "id" lessonTimetableStudent.
     *
     * @param id the id of the lessonTimetableStudent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lesson-timetable-students/{id}")
    public ResponseEntity<Void> deleteLessonTimetableStudent(@PathVariable Long id) {
        log.debug("REST request to delete LessonTimetableStudent : {}", id);
        lessonTimetableStudentRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
