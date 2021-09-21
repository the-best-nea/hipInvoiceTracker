package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.LessonTimetableTeacher;
import com.mycompany.myapp.repository.LessonTimetableTeacherRepository;
import com.mycompany.myapp.service.LessonTimetableTeacherQueryService;
import com.mycompany.myapp.service.LessonTimetableTeacherService;
import com.mycompany.myapp.service.criteria.LessonTimetableTeacherCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.LessonTimetableTeacher}.
 */
@RestController
@RequestMapping("/api")
public class LessonTimetableTeacherResource {

    private final Logger log = LoggerFactory.getLogger(LessonTimetableTeacherResource.class);

    private static final String ENTITY_NAME = "lessonTimetableTeacher";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LessonTimetableTeacherService lessonTimetableTeacherService;

    private final LessonTimetableTeacherRepository lessonTimetableTeacherRepository;

    private final LessonTimetableTeacherQueryService lessonTimetableTeacherQueryService;

    public LessonTimetableTeacherResource(
        LessonTimetableTeacherService lessonTimetableTeacherService,
        LessonTimetableTeacherRepository lessonTimetableTeacherRepository,
        LessonTimetableTeacherQueryService lessonTimetableTeacherQueryService
    ) {
        this.lessonTimetableTeacherService = lessonTimetableTeacherService;
        this.lessonTimetableTeacherRepository = lessonTimetableTeacherRepository;
        this.lessonTimetableTeacherQueryService = lessonTimetableTeacherQueryService;
    }

    /**
     * {@code POST  /lesson-timetable-teachers} : Create a new lessonTimetableTeacher.
     *
     * @param lessonTimetableTeacher the lessonTimetableTeacher to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lessonTimetableTeacher, or with status {@code 400 (Bad Request)} if the lessonTimetableTeacher has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lesson-timetable-teachers")
    public ResponseEntity<LessonTimetableTeacher> createLessonTimetableTeacher(
        @Valid @RequestBody LessonTimetableTeacher lessonTimetableTeacher
    ) throws URISyntaxException {
        log.debug("REST request to save LessonTimetableTeacher : {}", lessonTimetableTeacher);
        if (lessonTimetableTeacher.getId() != null) {
            throw new BadRequestAlertException("A new lessonTimetableTeacher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LessonTimetableTeacher result = lessonTimetableTeacherService.save(lessonTimetableTeacher);
        return ResponseEntity
            .created(new URI("/api/lesson-timetable-teachers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lesson-timetable-teachers/:id} : Updates an existing lessonTimetableTeacher.
     *
     * @param id the id of the lessonTimetableTeacher to save.
     * @param lessonTimetableTeacher the lessonTimetableTeacher to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lessonTimetableTeacher,
     * or with status {@code 400 (Bad Request)} if the lessonTimetableTeacher is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lessonTimetableTeacher couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lesson-timetable-teachers/{id}")
    public ResponseEntity<LessonTimetableTeacher> updateLessonTimetableTeacher(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LessonTimetableTeacher lessonTimetableTeacher
    ) throws URISyntaxException {
        log.debug("REST request to update LessonTimetableTeacher : {}, {}", id, lessonTimetableTeacher);
        if (lessonTimetableTeacher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lessonTimetableTeacher.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lessonTimetableTeacherRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LessonTimetableTeacher result = lessonTimetableTeacherService.save(lessonTimetableTeacher);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lessonTimetableTeacher.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lesson-timetable-teachers/:id} : Partial updates given fields of an existing lessonTimetableTeacher, field will ignore if it is null
     *
     * @param id the id of the lessonTimetableTeacher to save.
     * @param lessonTimetableTeacher the lessonTimetableTeacher to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lessonTimetableTeacher,
     * or with status {@code 400 (Bad Request)} if the lessonTimetableTeacher is not valid,
     * or with status {@code 404 (Not Found)} if the lessonTimetableTeacher is not found,
     * or with status {@code 500 (Internal Server Error)} if the lessonTimetableTeacher couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lesson-timetable-teachers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LessonTimetableTeacher> partialUpdateLessonTimetableTeacher(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LessonTimetableTeacher lessonTimetableTeacher
    ) throws URISyntaxException {
        log.debug("REST request to partial update LessonTimetableTeacher partially : {}, {}", id, lessonTimetableTeacher);
        if (lessonTimetableTeacher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lessonTimetableTeacher.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lessonTimetableTeacherRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LessonTimetableTeacher> result = lessonTimetableTeacherService.partialUpdate(lessonTimetableTeacher);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lessonTimetableTeacher.getId().toString())
        );
    }

    /**
     * {@code GET  /lesson-timetable-teachers} : get all the lessonTimetableTeachers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lessonTimetableTeachers in body.
     */
    @GetMapping("/lesson-timetable-teachers")
    public ResponseEntity<List<LessonTimetableTeacher>> getAllLessonTimetableTeachers(LessonTimetableTeacherCriteria criteria) {
        log.debug("REST request to get LessonTimetableTeachers by criteria: {}", criteria);
        List<LessonTimetableTeacher> entityList = lessonTimetableTeacherQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /lesson-timetable-teachers/count} : count all the lessonTimetableTeachers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lesson-timetable-teachers/count")
    public ResponseEntity<Long> countLessonTimetableTeachers(LessonTimetableTeacherCriteria criteria) {
        log.debug("REST request to count LessonTimetableTeachers by criteria: {}", criteria);
        return ResponseEntity.ok().body(lessonTimetableTeacherQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lesson-timetable-teachers/:id} : get the "id" lessonTimetableTeacher.
     *
     * @param id the id of the lessonTimetableTeacher to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lessonTimetableTeacher, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lesson-timetable-teachers/{id}")
    public ResponseEntity<LessonTimetableTeacher> getLessonTimetableTeacher(@PathVariable Long id) {
        log.debug("REST request to get LessonTimetableTeacher : {}", id);
        Optional<LessonTimetableTeacher> lessonTimetableTeacher = lessonTimetableTeacherService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lessonTimetableTeacher);
    }

    /**
     * {@code DELETE  /lesson-timetable-teachers/:id} : delete the "id" lessonTimetableTeacher.
     *
     * @param id the id of the lessonTimetableTeacher to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lesson-timetable-teachers/{id}")
    public ResponseEntity<Void> deleteLessonTimetableTeacher(@PathVariable Long id) {
        log.debug("REST request to delete LessonTimetableTeacher : {}", id);
        lessonTimetableTeacherService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
