package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.LessonTimetable;
import com.mycompany.myapp.repository.LessonTimetableRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LessonTimetable}.
 */
@Service
@Transactional
public class LessonTimetableService {

    private final Logger log = LoggerFactory.getLogger(LessonTimetableService.class);

    private final LessonTimetableRepository lessonTimetableRepository;

    public LessonTimetableService(LessonTimetableRepository lessonTimetableRepository) {
        this.lessonTimetableRepository = lessonTimetableRepository;
    }

    /**
     * Save a lessonTimetable.
     *
     * @param lessonTimetable the entity to save.
     * @return the persisted entity.
     */
    public LessonTimetable save(LessonTimetable lessonTimetable) {
        log.debug("Request to save LessonTimetable : {}", lessonTimetable);
        return lessonTimetableRepository.save(lessonTimetable);
    }

    /**
     * Partially update a lessonTimetable.
     *
     * @param lessonTimetable the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LessonTimetable> partialUpdate(LessonTimetable lessonTimetable) {
        log.debug("Request to partially update LessonTimetable : {}", lessonTimetable);

        return lessonTimetableRepository
            .findById(lessonTimetable.getId())
            .map(
                existingLessonTimetable -> {
                    if (lessonTimetable.getLessonName() != null) {
                        existingLessonTimetable.setLessonName(lessonTimetable.getLessonName());
                    }
                    if (lessonTimetable.getStartAt() != null) {
                        existingLessonTimetable.setStartAt(lessonTimetable.getStartAt());
                    }
                    if (lessonTimetable.getEndAt() != null) {
                        existingLessonTimetable.setEndAt(lessonTimetable.getEndAt());
                    }
                    if (lessonTimetable.getDayOfWeek() != null) {
                        existingLessonTimetable.setDayOfWeek(lessonTimetable.getDayOfWeek());
                    }
                    if (lessonTimetable.getDescription() != null) {
                        existingLessonTimetable.setDescription(lessonTimetable.getDescription());
                    }
                    if (lessonTimetable.getCreatedAt() != null) {
                        existingLessonTimetable.setCreatedAt(lessonTimetable.getCreatedAt());
                    }
                    if (lessonTimetable.getActive() != null) {
                        existingLessonTimetable.setActive(lessonTimetable.getActive());
                    }
                    if (lessonTimetable.getRegisterTaken() != null) {
                        existingLessonTimetable.setRegisterTaken(lessonTimetable.getRegisterTaken());
                    }

                    return existingLessonTimetable;
                }
            )
            .map(lessonTimetableRepository::save);
    }

    /**
     * Get all the lessonTimetables.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LessonTimetable> findAll() {
        log.debug("Request to get all LessonTimetables");
        return lessonTimetableRepository.findAll();
    }

    /**
     * Get one lessonTimetable by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LessonTimetable> findOne(Long id) {
        log.debug("Request to get LessonTimetable : {}", id);
        return lessonTimetableRepository.findById(id);
    }

    /**
     * Delete the lessonTimetable by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LessonTimetable : {}", id);
        lessonTimetableRepository.deleteById(id);
    }
}
