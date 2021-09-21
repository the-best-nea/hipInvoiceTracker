package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.LessonTimetable;
import com.mycompany.myapp.repository.LessonTimetableRepository;
import com.mycompany.myapp.service.criteria.LessonTimetableCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link LessonTimetable} entities in the database.
 * The main input is a {@link LessonTimetableCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LessonTimetable} or a {@link Page} of {@link LessonTimetable} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LessonTimetableQueryService extends QueryService<LessonTimetable> {

    private final Logger log = LoggerFactory.getLogger(LessonTimetableQueryService.class);

    private final LessonTimetableRepository lessonTimetableRepository;

    public LessonTimetableQueryService(LessonTimetableRepository lessonTimetableRepository) {
        this.lessonTimetableRepository = lessonTimetableRepository;
    }

    /**
     * Return a {@link List} of {@link LessonTimetable} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LessonTimetable> findByCriteria(LessonTimetableCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LessonTimetable> specification = createSpecification(criteria);
        return lessonTimetableRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link LessonTimetable} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LessonTimetable> findByCriteria(LessonTimetableCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LessonTimetable> specification = createSpecification(criteria);
        return lessonTimetableRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LessonTimetableCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LessonTimetable> specification = createSpecification(criteria);
        return lessonTimetableRepository.count(specification);
    }

    /**
     * Function to convert {@link LessonTimetableCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LessonTimetable> createSpecification(LessonTimetableCriteria criteria) {
        Specification<LessonTimetable> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LessonTimetable_.id));
            }
            if (criteria.getLessonName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLessonName(), LessonTimetable_.lessonName));
            }
            if (criteria.getStartAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartAt(), LessonTimetable_.startAt));
            }
            if (criteria.getEndAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndAt(), LessonTimetable_.endAt));
            }
            if (criteria.getDayOfWeek() != null) {
                specification = specification.and(buildSpecification(criteria.getDayOfWeek(), LessonTimetable_.dayOfWeek));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), LessonTimetable_.description));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), LessonTimetable_.createdAt));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), LessonTimetable_.active));
            }
            if (criteria.getRegisterTaken() != null) {
                specification = specification.and(buildSpecification(criteria.getRegisterTaken(), LessonTimetable_.registerTaken));
            }
            if (criteria.getLocationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLocationId(),
                            root -> root.join(LessonTimetable_.location, JoinType.LEFT).get(Location_.id)
                        )
                    );
            }
            if (criteria.getSubjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubjectId(),
                            root -> root.join(LessonTimetable_.subject, JoinType.LEFT).get(Subject_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
