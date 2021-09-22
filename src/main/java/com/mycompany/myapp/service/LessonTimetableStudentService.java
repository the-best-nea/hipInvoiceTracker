package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.LessonTimetableStudent;
import com.mycompany.myapp.domain.Student;
import com.mycompany.myapp.repository.LessonTimetableStudentRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LessonTimetableStudent}.
 */
@Service
@Transactional
public class LessonTimetableStudentService {

    private final Logger log = LoggerFactory.getLogger(LessonTimetableStudentService.class);

    private final LessonTimetableStudentRepository lessonTimetableStudentRepository;

    public LessonTimetableStudentService(LessonTimetableStudentRepository lessonTimetableStudentRepository) {
        this.lessonTimetableStudentRepository = lessonTimetableStudentRepository;
    }

    /**
     * Save a lessonTimetableStudent.
     *
     * @param lessonTimetableStudent the entity to save.
     * @return the persisted entity.
     */
    public LessonTimetableStudent save(LessonTimetableStudent lessonTimetableStudent) {
        log.debug("Request to save LessonTimetableStudent : {}", lessonTimetableStudent);
        return lessonTimetableStudentRepository.save(lessonTimetableStudent);
    }

    /**
     * Partially update a lessonTimetableStudent.
     *
     * @param lessonTimetableStudent the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LessonTimetableStudent> partialUpdate(LessonTimetableStudent lessonTimetableStudent) {
        log.debug("Request to partially update LessonTimetableStudent : {}", lessonTimetableStudent);

        return lessonTimetableStudentRepository
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
    }

    /**
     * Get all the lessonTimetableStudents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LessonTimetableStudent> findAll() {
        log.debug("Request to get all LessonTimetableStudents");
        return lessonTimetableStudentRepository.findAll();
    }

    /**
     * Get one lessonTimetableStudent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LessonTimetableStudent> findOne(Long id) {
        log.debug("Request to get LessonTimetableStudent : {}", id);
        return lessonTimetableStudentRepository.findById(id);
    }

    /**
     * Delete the lessonTimetableStudent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LessonTimetableStudent : {}", id);
        lessonTimetableStudentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<LessonTimetableStudent> findAllByStudent(Student student) {
        log.debug("Request to get LessonTimetableStudent by student ID : {}", student.getId());
        return lessonTimetableStudentRepository.findAllByStudent(student);
    }
}
