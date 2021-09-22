package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.LessonInstance;
import com.mycompany.myapp.domain.LessonInstanceRequest;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.LessonInstanceRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.LessonInstanceQueryService;
import com.mycompany.myapp.service.LessonInstanceService;
import com.mycompany.myapp.service.RegistrationService;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.service.criteria.LessonInstanceCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.LessonInstance}.
 */
@RestController
@RequestMapping("/api")
public class LessonInstanceResource {

    private final Logger log = LoggerFactory.getLogger(LessonInstanceResource.class);

    private static final String ENTITY_NAME = "lessonInstance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LessonInstanceService lessonInstanceService;

    private final LessonInstanceRepository lessonInstanceRepository;

    private final LessonInstanceQueryService lessonInstanceQueryService;
    private final RegistrationService registrationService;
    private final UserService userService;

    public LessonInstanceResource(
        LessonInstanceService lessonInstanceService,
        LessonInstanceRepository lessonInstanceRepository,
        LessonInstanceQueryService lessonInstanceQueryService,
        RegistrationService registrationService, UserService userService) {
        this.lessonInstanceService = lessonInstanceService;
        this.lessonInstanceRepository = lessonInstanceRepository;
        this.lessonInstanceQueryService = lessonInstanceQueryService;
        this.registrationService = registrationService;
        this.userService = userService;
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
        LessonInstance result = lessonInstanceService.save(lessonInstance);
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

        LessonInstance result = lessonInstanceService.save(lessonInstance);
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

        Optional<LessonInstance> result = lessonInstanceService.partialUpdate(lessonInstance);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lessonInstance.getId().toString())
        );
    }

    /**
     * {@code GET  /lesson-instances} : get all the lessonInstances.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lessonInstances in body.
     */
    @GetMapping("/lesson-instances")
    public ResponseEntity<List<LessonInstance>> getAllLessonInstances(LessonInstanceCriteria criteria) {
        log.debug("REST request to get LessonInstances by criteria: {}", criteria);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserWithAuthoritiesByLogin(userDetails.getUsername()).orElseThrow(RuntimeException::new);
        if (user.getAuthorities().contains(AuthoritiesConstants.ADMIN)){
            log.debug("User is an admin");
        } else  {
            log.debug("User is not an admin");
            LongFilter longFilter = new LongFilter();
            longFilter.setEquals(user.getId());
            criteria.setInternalUserId(longFilter);
        }

        List<LessonInstance> entityList = lessonInstanceQueryService.findByCriteria(criteria);

        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /lesson-instances/count} : count all the lessonInstances.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lesson-instances/count")
    public ResponseEntity<Long> countLessonInstances(LessonInstanceCriteria criteria) {
        log.debug("REST request to count LessonInstances by criteria: {}", criteria);
        return ResponseEntity.ok().body(lessonInstanceQueryService.countByCriteria(criteria));
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
        Optional<LessonInstance> lessonInstance = lessonInstanceService.findOne(id);
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
        lessonInstanceService.delete(id);
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

        //service to add register to register table
        registrationService.takeRegister(lessonInstance);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lessonInstance.getId().toString()))
            .body(lessonInstance1);
    }
}
