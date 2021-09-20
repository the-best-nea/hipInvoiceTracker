package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.StudentRegister;
import com.mycompany.myapp.repository.StudentRegisterRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.StudentRegister}.
 */
@RestController
@RequestMapping("/api")
@Transactional
@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
public class StudentRegisterResource {

    private final Logger log = LoggerFactory.getLogger(StudentRegisterResource.class);

    private static final String ENTITY_NAME = "studentRegister";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentRegisterRepository studentRegisterRepository;

    public StudentRegisterResource(StudentRegisterRepository studentRegisterRepository) {
        this.studentRegisterRepository = studentRegisterRepository;
    }

    /**
     * {@code POST  /student-registers} : Create a new studentRegister.
     *
     * @param studentRegister the studentRegister to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentRegister, or with status {@code 400 (Bad Request)} if the studentRegister has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/student-registers")
    public ResponseEntity<StudentRegister> createStudentRegister(@Valid @RequestBody StudentRegister studentRegister)
        throws URISyntaxException {
        log.debug("REST request to save StudentRegister : {}", studentRegister);
        if (studentRegister.getId() != null) {
            throw new BadRequestAlertException("A new studentRegister cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudentRegister result = studentRegisterRepository.save(studentRegister);
        return ResponseEntity
            .created(new URI("/api/student-registers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /student-registers/:id} : Updates an existing studentRegister.
     *
     * @param id the id of the studentRegister to save.
     * @param studentRegister the studentRegister to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentRegister,
     * or with status {@code 400 (Bad Request)} if the studentRegister is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentRegister couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/student-registers/{id}")
    public ResponseEntity<StudentRegister> updateStudentRegister(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StudentRegister studentRegister
    ) throws URISyntaxException {
        log.debug("REST request to update StudentRegister : {}, {}", id, studentRegister);
        if (studentRegister.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentRegister.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentRegisterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StudentRegister result = studentRegisterRepository.save(studentRegister);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studentRegister.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /student-registers/:id} : Partial updates given fields of an existing studentRegister, field will ignore if it is null
     *
     * @param id the id of the studentRegister to save.
     * @param studentRegister the studentRegister to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentRegister,
     * or with status {@code 400 (Bad Request)} if the studentRegister is not valid,
     * or with status {@code 404 (Not Found)} if the studentRegister is not found,
     * or with status {@code 500 (Internal Server Error)} if the studentRegister couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/student-registers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<StudentRegister> partialUpdateStudentRegister(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StudentRegister studentRegister
    ) throws URISyntaxException {
        log.debug("REST request to partial update StudentRegister partially : {}, {}", id, studentRegister);
        if (studentRegister.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentRegister.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentRegisterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudentRegister> result = studentRegisterRepository
            .findById(studentRegister.getId())
            .map(
                existingStudentRegister -> {
                    if (studentRegister.getDateOfLesson() != null) {
                        existingStudentRegister.setDateOfLesson(studentRegister.getDateOfLesson());
                    }
                    if (studentRegister.getPay() != null) {
                        existingStudentRegister.setPay(studentRegister.getPay());
                    }
                    if (studentRegister.getAttended() != null) {
                        existingStudentRegister.setAttended(studentRegister.getAttended());
                    }
                    if (studentRegister.getCreatedOn() != null) {
                        existingStudentRegister.setCreatedOn(studentRegister.getCreatedOn());
                    }
                    if (studentRegister.getUpdatedOn() != null) {
                        existingStudentRegister.setUpdatedOn(studentRegister.getUpdatedOn());
                    }

                    return existingStudentRegister;
                }
            )
            .map(studentRegisterRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, studentRegister.getId().toString())
        );
    }

    /**
     * {@code GET  /student-registers} : get all the studentRegisters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentRegisters in body.
     */
    @GetMapping("/student-registers")
    public List<StudentRegister> getAllStudentRegisters() {
        log.debug("REST request to get all StudentRegisters");
        return studentRegisterRepository.findAll();
    }

    /**
     * {@code GET  /student-registers/:id} : get the "id" studentRegister.
     *
     * @param id the id of the studentRegister to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentRegister, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-registers/{id}")
    public ResponseEntity<StudentRegister> getStudentRegister(@PathVariable Long id) {
        log.debug("REST request to get StudentRegister : {}", id);
        Optional<StudentRegister> studentRegister = studentRegisterRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(studentRegister);
    }

    /**
     * {@code DELETE  /student-registers/:id} : delete the "id" studentRegister.
     *
     * @param id the id of the studentRegister to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/student-registers/{id}")
    public ResponseEntity<Void> deleteStudentRegister(@PathVariable Long id) {
        log.debug("REST request to delete StudentRegister : {}", id);
        studentRegisterRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
