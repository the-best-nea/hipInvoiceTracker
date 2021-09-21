package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.LessonTimetableTeacher;
import com.mycompany.myapp.repository.LessonTimetableTeacherRepository;
import com.mycompany.myapp.service.criteria.LessonTimetableTeacherCriteria;
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
 * Service for executing complex queries for {@link LessonTimetableTeacher} entities in the database.
 * The main input is a {@link LessonTimetableTeacherCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LessonTimetableTeacher} or a {@link Page} of {@link LessonTimetableTeacher} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LessonTimetableTeacherQueryService extends QueryService<LessonTimetableTeacher> {

    private final Logger log = LoggerFactory.getLogger(LessonTimetableTeacherQueryService.class);

    private final LessonTimetableTeacherRepository lessonTimetableTeacherRepository;

    public LessonTimetableTeacherQueryService(LessonTimetableTeacherRepository lessonTimetableTeacherRepository) {
        this.lessonTimetableTeacherRepository = lessonTimetableTeacherRepository;
    }

    /**
     * Return a {@link List} of {@link LessonTimetableTeacher} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LessonTimetableTeacher> findByCriteria(LessonTimetableTeacherCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LessonTimetableTeacher> specification = createSpecification(criteria);
        return lessonTimetableTeacherRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link LessonTimetableTeacher} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LessonTimetableTeacher> findByCriteria(LessonTimetableTeacherCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LessonTimetableTeacher> specification = createSpecification(criteria);
        return lessonTimetableTeacherRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LessonTimetableTeacherCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LessonTimetableTeacher> specification = createSpecification(criteria);
        return lessonTimetableTeacherRepository.count(specification);
    }

    /**
     * Function to convert {@link LessonTimetableTeacherCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LessonTimetableTeacher> createSpecification(LessonTimetableTeacherCriteria criteria) {
        Specification<LessonTimetableTeacher> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LessonTimetableTeacher_.id));
            }
            if (criteria.getPay() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPay(), LessonTimetableTeacher_.pay));
            }
            if (criteria.getLessonTimetableId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLessonTimetableId(),
                            root -> root.join(LessonTimetableTeacher_.lessonTimetable, JoinType.LEFT).get(LessonTimetable_.id)
                        )
                    );
            }
            if (criteria.getInternalUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInternalUserId(),
                            root -> root.join(LessonTimetableTeacher_.internalUser, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
