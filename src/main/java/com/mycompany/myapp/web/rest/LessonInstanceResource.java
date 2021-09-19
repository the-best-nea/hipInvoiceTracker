package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.LessonInstance;
import com.mycompany.myapp.domain.LessonInstanceRequest;
import com.mycompany.myapp.repository.LessonInstanceRepository;
import com.mycompany.myapp.service.RegistrationService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.LessonInstance}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LessonInstanceResource {

    private final Logger log = LoggerFactory.getLogger(LessonInstanceResource.class);

    private static final String ENTITY_NAME = "lessonInstance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LessonInstanceRepository lessonInstanceRepository;
    private final RegistrationService registrationService;

    public LessonInstanceResource(LessonInstanceRepository lessonInstanceRepository, RegistrationService registrationService) {
        this.lessonInstanceRepository = lessonInstanceRepository;
        this.registrationService = registrationService;
    }

    /**
     * {@code POST  /lesson-instances} : Create a new lessonInstance.
     *
     * @param lessonInstance the lessonInstance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lessonInstance, or with status {@code 400 (Bad Request)} if the lessonInstance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lesson-instances")
    public ResponseEntity<LessonInstance> createLessonInstance(@Valid @RequestBody LessonInstance lessonInstance)
        throws URISyntaxException {
        log.debug("REST request to save LessonInstance : {}", lessonInstance);
        if (lessonInstance.getId() != null) {
            throw new BadRequestAlertException("A new lessonInstance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LessonInstance result = lessonInstanceRepository.save(lessonInstance);
        return ResponseEntity
            .created(new URI("/api/lesson-instances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lesson-instances/:id} : Updates an existing lessonInstance.
     *
     * @param id the id of the lessonInstance to save.
     * @param lessonInstance the lessonInstance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lessonInstance,
     * or with status {@code 400 (Bad Request)} if the lessonInstance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lessonInstance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lesson-instances/{id}")
    public ResponseEntity<LessonInstance> updateLessonInstance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LessonInstance lessonInstance
    ) throws URISyntaxException {
        log.debug("REST request to update LessonInstance : {}, {}", id, lessonInstance);
        if (lessonInstance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lessonInstance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lessonInstanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LessonInstance result = lessonInstanceRepository.save(lessonInstance);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lessonInstance.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lesson-instances/:id} : Partial updates given fields of an existing lessonInstance, field will ignore if it is null
     *
     * @param id the id of the lessonInstance to save.
     * @param lessonInstance the lessonInstance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lessonInstance,
     * or with status {@code 400 (Bad Request)} if the lessonInstance is not valid,
     * or with status {@code 404 (Not Found)} if the lessonInstance is not found,
     * or with status {@code 500 (Internal Server Error)} if the lessonInstance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lesson-instances/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LessonInstance> partialUpdateLessonInstance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LessonInstance lessonInstance
    ) throws URISyntaxException {
        log.debug("REST request to partial update LessonInstance partially : {}, {}", id, lessonInstance);
        if (lessonInstance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lessonInstance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lessonInstanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LessonInstance> result = lessonInstanceRepository
            .findById(lessonInstance.getId())
            .map(
                existingLessonInstance -> {
                    if (lessonInstance.getLessonName() != null) {
                        existingLessonInstance.setLessonName(lessonInstance.getLessonName());
                    }
                    if (lessonInstance.getStartAt() != null) {
                        existingLessonInstance.setStartAt(lessonInstance.getStartAt());
                    }
                    if (lessonInstance.getEndAt() != null) {
                        existingLessonInstance.setEndAt(lessonInstance.getEndAt());
                    }
                    if (lessonInstance.getDayOfWeek() != null) {
                        existingLessonInstance.setDayOfWeek(lessonInstance.getDayOfWeek());
                    }
                    if (lessonInstance.getDescription() != null) {
                        existingLessonInstance.setDescription(lessonInstance.getDescription());
                    }
                    if (lessonInstance.getCretedOn() != null) {
                        existingLessonInstance.setCretedOn(lessonInstance.getCretedOn());
                    }

                    return existingLessonInstance;
                }
            )
            .map(lessonInstanceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lessonInstance.getId().toString())
        );
    }

    /**
     * {@code GET  /lesson-instances} : get all the lessonInstances.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lessonInstances in body.
     */
    @GetMapping("/lesson-instances")
    public List<LessonInstance> getAllLessonInstances(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all LessonInstances");
        return lessonInstanceRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /lesson-instances/:id} : get the "id" lessonInstance.
     *
     * @param id the id of the lessonInstance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lessonInstance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lesson-instances/{id}")
    public ResponseEntity<LessonInstance> getLessonInstance(@PathVariable Long id) {
        log.debug("REST request to get LessonInstance : {}", id);
        Optional<LessonInstance> lessonInstance = lessonInstanceRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(lessonInstance);
    }

    /**
     * {@code DELETE  /lesson-instances/:id} : delete the "id" lessonInstance.
     *
     * @param id the id of the lessonInstance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lesson-instances/{id}")
    public ResponseEntity<Void> deleteLessonInstance(@PathVariable Long id) {
        log.debug("REST request to delete LessonInstance : {}", id);
        lessonInstanceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PutMapping("/lesson-instances/{id}/register")
    public ResponseEntity<LessonInstance> registerLessonInstance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LessonInstanceRequest lessonInstance
    ) throws URISyntaxException {
        log.debug("REST blah request to update LessonInstance through register : {}, {}", id, lessonInstance);
        if (lessonInstance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lessonInstance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lessonInstanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LessonInstance lessonInstance1 = new LessonInstance();
        lessonInstance1.setId(lessonInstance.getId());

        //Add service to add register to register table
        registrationService.takeRegister(lessonInstance);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lessonInstance.getId().toString()))
            .body(lessonInstance1);
    }
}
