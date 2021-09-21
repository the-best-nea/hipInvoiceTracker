package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.LessonTimetableTeacher;
import com.mycompany.myapp.repository.LessonTimetableTeacherRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LessonTimetableTeacher}.
 */
@Service
@Transactional
public class LessonTimetableTeacherService {

    private final Logger log = LoggerFactory.getLogger(LessonTimetableTeacherService.class);

    private final LessonTimetableTeacherRepository lessonTimetableTeacherRepository;

    public LessonTimetableTeacherService(LessonTimetableTeacherRepository lessonTimetableTeacherRepository) {
        this.lessonTimetableTeacherRepository = lessonTimetableTeacherRepository;
    }

    /**
     * Save a lessonTimetableTeacher.
     *
     * @param lessonTimetableTeacher the entity to save.
     * @return the persisted entity.
     */
    public LessonTimetableTeacher save(LessonTimetableTeacher lessonTimetableTeacher) {
        log.debug("Request to save LessonTimetableTeacher : {}", lessonTimetableTeacher);
        return lessonTimetableTeacherRepository.save(lessonTimetableTeacher);
    }

    /**
     * Partially update a lessonTimetableTeacher.
     *
     * @param lessonTimetableTeacher the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LessonTimetableTeacher> partialUpdate(LessonTimetableTeacher lessonTimetableTeacher) {
        log.debug("Request to partially update LessonTimetableTeacher : {}", lessonTimetableTeacher);

        return lessonTimetableTeacherRepository
            .findById(lessonTimetableTeacher.getId())
            .map(
                existingLessonTimetableTeacher -> {
                    if (lessonTimetableTeacher.getPay() != null) {
                        existingLessonTimetableTeacher.setPay(lessonTimetableTeacher.getPay());
                    }

                    return existingLessonTimetableTeacher;
                }
            )
            .map(lessonTimetableTeacherRepository::save);
    }

    /**
     * Get all the lessonTimetableTeachers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LessonTimetableTeacher> findAll() {
        log.debug("Request to get all LessonTimetableTeachers");
        return lessonTimetableTeacherRepository.findAll();
    }

    /**
     * Get one lessonTimetableTeacher by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LessonTimetableTeacher> findOne(Long id) {
        log.debug("Request to get LessonTimetableTeacher : {}", id);
        return lessonTimetableTeacherRepository.findById(id);
    }

    /**
     * Delete the lessonTimetableTeacher by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LessonTimetableTeacher : {}", id);
        lessonTimetableTeacherRepository.deleteById(id);
    }
}
