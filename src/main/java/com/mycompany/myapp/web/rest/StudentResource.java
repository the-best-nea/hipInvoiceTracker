package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.RegisterItem;
import com.mycompany.myapp.domain.Student;
import com.mycompany.myapp.domain.StudentRegister;
import com.mycompany.myapp.repository.StudentRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.StudentService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Student}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class StudentResource {

    private final Logger log = LoggerFactory.getLogger(StudentResource.class);

    private static final String ENTITY_NAME = "student";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentRepository studentRepository;
    private final StudentService studentService;

    public StudentResource(StudentRepository studentRepository, StudentService studentService) {
        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }

    /**
     * {@code POST  /students} : Create a new student.
     *
     * @param student the student to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new student, or with status {@code 400 (Bad Request)} if the student has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/students")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) throws URISyntaxException {
        log.debug("REST request to save Student : {}", student);
        if (student.getId() != null) {
            throw new BadRequestAlertException("A new student cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Student result = studentRepository.save(student);
        return ResponseEntity
            .created(new URI("/api/students/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /students/:id} : Updates an existing student.
     *
     * @param id      the id of the student to save.
     * @param student the student to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated student,
     * or with status {@code 400 (Bad Request)} if the student is not valid,
     * or with status {@code 500 (Internal Server Error)} if the student couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/students/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Student> updateStudent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Student student
    ) throws URISyntaxException {
        log.debug("REST request to update Student : {}, {}", id, student);
        if (student.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, student.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Student result = studentRepository.save(student);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, student.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /students/:id} : Partial updates given fields of an existing student, field will ignore if it is null
     *
     * @param id      the id of the student to save.
     * @param student the student to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated student,
     * or with status {@code 400 (Bad Request)} if the student is not valid,
     * or with status {@code 404 (Not Found)} if the student is not found,
     * or with status {@code 500 (Internal Server Error)} if the student couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/students/{id}", consumes = "application/merge-patch+json")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Student> partialUpdateStudent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Student student
    ) throws URISyntaxException {
        log.debug("REST request to partial update Student partially : {}, {}", id, student);
        if (student.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, student.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Student> result = studentRepository
            .findById(student.getId())
            .map(
                existingStudent -> {
                    if (student.getFirstName() != null) {
                        existingStudent.setFirstName(student.getFirstName());
                    }
                    if (student.getLastName() != null) {
                        existingStudent.setLastName(student.getLastName());
                    }
                    if (student.getYearGroup() != null) {
                        existingStudent.setYearGroup(student.getYearGroup());
                    }
                    if (student.getEmail() != null) {
                        existingStudent.setEmail(student.getEmail());
                    }
                    if (student.getPhoneNumber() != null) {
                        existingStudent.setPhoneNumber(student.getPhoneNumber());
                    }
                    if (student.getStartDate() != null) {
                        existingStudent.setStartDate(student.getStartDate());
                    }
                    if (student.getEndDate() != null) {
                        existingStudent.setEndDate(student.getEndDate());
                    }
                    if (student.getActive() != null) {
                        existingStudent.setActive(student.getActive());
                    }
                    if (student.getCreatedAt() != null) {
                        existingStudent.setCreatedAt(student.getCreatedAt());
                    }
                    if (student.getBalance() != null) {
                        existingStudent.setBalance(student.getBalance());
                    }


                    return existingStudent;
                }
            )
            .map(studentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, student.getId().toString())
        );
    }

    /**
     * {@code GET  /students} : get all the students.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of students in body.
     */
    @GetMapping("/students")
    public List<Student> getAllStudents() {
        log.debug("REST request to get all Students");
        return studentRepository.findAll();
    }

    /**
     * {@code GET  /students/:id} : get the "id" student.
     *
     * @param id the id of the student to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the student, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/students/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        log.debug("REST request to get Student : {}", id);
        Optional<Student> student = studentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(student);
    }

    /**
     * {@code DELETE  /students/:id} : delete the "id" student.
     *
     * @param id the id of the student to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/students/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        log.debug("REST request to delete Student : {}", id);
        studentRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/students/byLessonId/{id}")
    public List<RegisterItem> getAllStudentByLessonId(@PathVariable Long id) {
        log.debug("REST request to get Students By Lesson Id : {}", id);

        List<StudentRegister> allStudentsByLesson = studentService.getAllStudentsByLesson(id);

        List<RegisterItem> registerItems = allStudentsByLesson
            .stream()
            .map(
                r ->
                    new RegisterItem()
                        .setId(r.getId())
                        .setStudentId(r.getStudent().getId())
                        .setFirstName(r.getStudent().getFirstName())
                        .setLastName(r.getStudent().getLastName())
                        .setAttended(r.getAttended())
                        //.setPay(r.getPay())
            )
            .collect(Collectors.toList());

        registerItems.forEach(System.out::println);

        return registerItems;
        //        RegisterResponse registerResponse = new RegisterResponse().setRegister(registerItems);
        //
        //
        //        return ResponseUtil.wrapOrNotFound(Optional.of(registerResponse));
    }
}
