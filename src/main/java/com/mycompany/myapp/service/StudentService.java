package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Student;
import com.mycompany.myapp.domain.StudentRegister;
import com.mycompany.myapp.repository.StudentRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Student}.
 */
@Service
@Transactional
public class StudentService {

    private final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final RegistrationService registrationService;

    public StudentService(StudentRepository studentRepository, RegistrationService registrationService) {
        this.studentRepository = studentRepository;
        this.registrationService = registrationService;
    }

    /**
     * Save a student.
     *
     * @param student the entity to save.
     * @return the persisted entity.
     */
    public Student save(Student student) {
        log.debug("Request to save Student : {}", student);
        return studentRepository.save(student);
    }

    /**
     * Partially update a student.
     *
     * @param student the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Student> partialUpdate(Student student) {
        log.debug("Request to partially update Student : {}", student);

        return studentRepository
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
    }

    /**
     * Get all the students.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Student> findAll() {
        log.debug("Request to get all Students");
        return studentRepository.findAll();
    }

    /**
     * Get one student by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Student> findOne(Long id) {
        log.debug("Request to get Student : {}", id);
        return studentRepository.findById(id);
    }

    /**
     * Delete the student by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Student : {}", id);
        studentRepository.deleteById(id);
    }


    public List<StudentRegister> getAllStudentsByLesson(Long id) {
        return registrationService.getStudents(id);
    }
}
