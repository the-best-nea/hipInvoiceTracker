package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.StudentRegister;
import com.mycompany.myapp.repository.StudentRegisterRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StudentRegister}.
 */
@Service
@Transactional
public class StudentRegisterService {

    private final Logger log = LoggerFactory.getLogger(StudentRegisterService.class);

    private final StudentRegisterRepository studentRegisterRepository;

    public StudentRegisterService(StudentRegisterRepository studentRegisterRepository) {
        this.studentRegisterRepository = studentRegisterRepository;
    }

    /**
     * Save a studentRegister.
     *
     * @param studentRegister the entity to save.
     * @return the persisted entity.
     */
    public StudentRegister save(StudentRegister studentRegister) {
        log.debug("Request to save StudentRegister : {}", studentRegister);
        return studentRegisterRepository.save(studentRegister);
    }

    /**
     * Partially update a studentRegister.
     *
     * @param studentRegister the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StudentRegister> partialUpdate(StudentRegister studentRegister) {
        log.debug("Request to partially update StudentRegister : {}", studentRegister);

        return studentRegisterRepository
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
    }

    /**
     * Get all the studentRegisters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StudentRegister> findAll() {
        log.debug("Request to get all StudentRegisters");
        return studentRegisterRepository.findAll();
    }

    /**
     * Get one studentRegister by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StudentRegister> findOne(Long id) {
        log.debug("Request to get StudentRegister : {}", id);
        return studentRegisterRepository.findById(id);
    }

    /**
     * Delete the studentRegister by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StudentRegister : {}", id);
        studentRegisterRepository.deleteById(id);
    }
}
