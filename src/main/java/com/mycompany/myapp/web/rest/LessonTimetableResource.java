package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.LessonInstance;
import com.mycompany.myapp.domain.LessonTimetable;
import com.mycompany.myapp.repository.LessonTimetableRepository;
import com.mycompany.myapp.service.LessonService;
import com.mycompany.myapp.service.LessonTimetableForUserService;
import com.mycompany.myapp.service.LessonTimetableQueryService;
import com.mycompany.myapp.service.LessonTimetableService;
import com.mycompany.myapp.service.criteria.LessonTimetableCriteria;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.LessonTimetable}.
 */
@RestController
@RequestMapping("/api")
public class LessonTimetableResource {

    private final Logger log = LoggerFactory.getLogger(LessonTimetableResource.class);

    private static final String ENTITY_NAME = "lessonTimetable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LessonTimetableService lessonTimetableService;

    private final LessonTimetableRepository lessonTimetableRepository;

    private final LessonTimetableQueryService lessonTimetableQueryService;
    private final LessonService lessonService;
    private final LessonTimetableForUserService lessonTimetableForUserService;

    public LessonTimetableResource(
        LessonTimetableService lessonTimetableService,
        LessonTimetableRepository lessonTimetableRepository,
        LessonTimetableQueryService lessonTimetableQueryService,
        LessonService lessonService, LessonTimetableForUserService lessonTimetableForUserService) {
        this.lessonTimetableService = lessonTimetableService;
        this.lessonTimetableRepository = lessonTimetableRepository;
        this.lessonTimetableQueryService = lessonTimetableQueryService;
        this.lessonService = lessonService;
        this.lessonTimetableForUserService = lessonTimetableForUserService;
    }

    /**
     * {@code POST  /lesson-timetables} : Create a new lessonTimetable.
     *
     * @param lessonTimetable the lessonTimetable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lessonTimetable, or with status {@code 400 (Bad Request)} if the lessonTimetable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lesson-timetables")
    public ResponseEntity<LessonTimetable> createLessonTimetable(@Valid @RequestBody LessonTimetable lessonTimetable)
        throws URISyntaxException {
        log.debug("REST request to save LessonTimetable : {}", lessonTimetable);
        if (lessonTimetable.getId() != null) {
            throw new BadRequestAlertException("A new lessonTimetable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LessonTimetable result = lessonTimetableService.save(lessonTimetable);
        return ResponseEntity
            .created(new URI("/api/lesson-timetables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lesson-timetables/:id} : Updates an existing lessonTimetable.
     *
     * @param id the id of the lessonTimetable to save.
     * @param lessonTimetable the lessonTimetable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lessonTimetable,
     * or with status {@code 400 (Bad Request)} if the lessonTimetable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lessonTimetable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lesson-timetables/{id}")
    public ResponseEntity<LessonTimetable> updateLessonTimetable(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LessonTimetable lessonTimetable
    ) throws URISyntaxException {
        log.debug("REST request to update LessonTimetable : {}, {}", id, lessonTimetable);
        if (lessonTimetable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lessonTimetable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lessonTimetableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LessonTimetable result = lessonTimetableService.save(lessonTimetable);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lessonTimetable.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lesson-timetables/:id} : Partial updates given fields of an existing lessonTimetable, field will ignore if it is null
     *
     * @param id the id of the lessonTimetable to save.
     * @param lessonTimetable the lessonTimetable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lessonTimetable,
     * or with status {@code 400 (Bad Request)} if the lessonTimetable is not valid,
     * or with status {@code 404 (Not Found)} if the lessonTimetable is not found,
     * or with status {@code 500 (Internal Server Error)} if the lessonTimetable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lesson-timetables/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LessonTimetable> partialUpdateLessonTimetable(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LessonTimetable lessonTimetable
    ) throws URISyntaxException {
        log.debug("REST request to partial update LessonTimetable partially : {}, {}", id, lessonTimetable);
        if (lessonTimetable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lessonTimetable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lessonTimetableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LessonTimetable> result = lessonTimetableService.partialUpdate(lessonTimetable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lessonTimetable.getId().toString())
        );
    }

    /**
     * {@code GET  /lesson-timetables} : get all the lessonTimetables.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lessonTimetables in body.
     */
    @GetMapping("/lesson-timetables")
    public ResponseEntity<List<LessonTimetable>> getAllLessonTimetables(LessonTimetableCriteria criteria) {
        log.debug("REST request to get LessonTimetables by criteria: {}", criteria);
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            List<LessonTimetable> entityList = lessonTimetableQueryService.findByCriteria(criteria);
            return ResponseEntity.ok().body(entityList);
        } else {
            List<LessonTimetable> entityList = lessonTimetableForUserService.getAllLessonTimetableByUser();
            return ResponseEntity.ok().body(entityList);
        }
    }

    /**
     * {@code GET  /lesson-timetables/count} : count all the lessonTimetables.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lesson-timetables/count")
    public ResponseEntity<Long> countLessonTimetables(LessonTimetableCriteria criteria) {
        log.debug("REST request to count LessonTimetables by criteria: {}", criteria);
        return ResponseEntity.ok().body(lessonTimetableQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lesson-timetables/:id} : get the "id" lessonTimetable.
     *
     * @param id the id of the lessonTimetable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lessonTimetable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lesson-timetables/{id}")
    public ResponseEntity<LessonTimetable> getLessonTimetable(@PathVariable Long id) {
        log.debug("REST request to get LessonTimetable : {}", id);
        Optional<LessonTimetable> lessonTimetable = lessonTimetableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lessonTimetable);
    }

    /**
     * {@code DELETE  /lesson-timetables/:id} : delete the "id" lessonTimetable.
     *
     * @param id the id of the lessonTimetable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lesson-timetables/{id}")
    public ResponseEntity<Void> deleteLessonTimetable(@PathVariable Long id) {
        log.debug("REST request to delete LessonTimetable : {}", id);
        lessonTimetableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/lesson-timetables/create-instance/{id}")
    public ResponseEntity<LessonInstance> createInstanceLessonTimetable(@PathVariable Long id) {
        log.info(">> REST request to create instance of LessonTimetable : {}", id);

        LessonInstance result = lessonService.createInstance(id);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

}
