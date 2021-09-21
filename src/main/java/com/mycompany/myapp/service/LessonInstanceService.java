package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.LessonInstance;
import com.mycompany.myapp.repository.LessonInstanceRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LessonInstance}.
 */
@Service
@Transactional
public class LessonInstanceService {

    private final Logger log = LoggerFactory.getLogger(LessonInstanceService.class);

    private final LessonInstanceRepository lessonInstanceRepository;

    public LessonInstanceService(LessonInstanceRepository lessonInstanceRepository) {
        this.lessonInstanceRepository = lessonInstanceRepository;
    }

    /**
     * Save a lessonInstance.
     *
     * @param lessonInstance the entity to save.
     * @return the persisted entity.
     */
    public LessonInstance save(LessonInstance lessonInstance) {
        log.debug("Request to save LessonInstance : {}", lessonInstance);
        return lessonInstanceRepository.save(lessonInstance);
    }

    /**
     * Partially update a lessonInstance.
     *
     * @param lessonInstance the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LessonInstance> partialUpdate(LessonInstance lessonInstance) {
        log.debug("Request to partially update LessonInstance : {}", lessonInstance);

        return lessonInstanceRepository
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
    }

    /**
     * Get all the lessonInstances.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LessonInstance> findAll() {
        log.debug("Request to get all LessonInstances");
        return lessonInstanceRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the lessonInstances with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<LessonInstance> findAllWithEagerRelationships(Pageable pageable) {
        return lessonInstanceRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one lessonInstance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LessonInstance> findOne(Long id) {
        log.debug("Request to get LessonInstance : {}", id);
        return lessonInstanceRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the lessonInstance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LessonInstance : {}", id);
        lessonInstanceRepository.deleteById(id);
    }
}
