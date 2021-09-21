package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.LessonTimetableStudent;
import com.mycompany.myapp.repository.LessonTimetableStudentRepository;
import com.mycompany.myapp.service.criteria.LessonTimetableStudentCriteria;
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
 * Service for executing complex queries for {@link LessonTimetableStudent} entities in the database.
 * The main input is a {@link LessonTimetableStudentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LessonTimetableStudent} or a {@link Page} of {@link LessonTimetableStudent} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LessonTimetableStudentQueryService extends QueryService<LessonTimetableStudent> {

    private final Logger log = LoggerFactory.getLogger(LessonTimetableStudentQueryService.class);

    private final LessonTimetableStudentRepository lessonTimetableStudentRepository;

    public LessonTimetableStudentQueryService(LessonTimetableStudentRepository lessonTimetableStudentRepository) {
        this.lessonTimetableStudentRepository = lessonTimetableStudentRepository;
    }

    /**
     * Return a {@link List} of {@link LessonTimetableStudent} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LessonTimetableStudent> findByCriteria(LessonTimetableStudentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LessonTimetableStudent> specification = createSpecification(criteria);
        return lessonTimetableStudentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link LessonTimetableStudent} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LessonTimetableStudent> findByCriteria(LessonTimetableStudentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LessonTimetableStudent> specification = createSpecification(criteria);
        return lessonTimetableStudentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LessonTimetableStudentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LessonTimetableStudent> specification = createSpecification(criteria);
        return lessonTimetableStudentRepository.count(specification);
    }

    /**
     * Function to convert {@link LessonTimetableStudentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LessonTimetableStudent> createSpecification(LessonTimetableStudentCriteria criteria) {
        Specification<LessonTimetableStudent> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LessonTimetableStudent_.id));
            }
            if (criteria.getPay() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPay(), LessonTimetableStudent_.pay));
            }
            if (criteria.getStudentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStudentId(),
                            root -> root.join(LessonTimetableStudent_.student, JoinType.LEFT).get(Student_.id)
                        )
                    );
            }
            if (criteria.getLessonTimetableId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLessonTimetableId(),
                            root -> root.join(LessonTimetableStudent_.lessonTimetable, JoinType.LEFT).get(LessonTimetable_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
