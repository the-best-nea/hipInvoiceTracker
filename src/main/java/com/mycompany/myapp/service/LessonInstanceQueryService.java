package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.LessonInstance;
import com.mycompany.myapp.repository.LessonInstanceRepository;
import com.mycompany.myapp.service.criteria.LessonInstanceCriteria;
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
 * Service for executing complex queries for {@link LessonInstance} entities in the database.
 * The main input is a {@link LessonInstanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LessonInstance} or a {@link Page} of {@link LessonInstance} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LessonInstanceQueryService extends QueryService<LessonInstance> {

    private final Logger log = LoggerFactory.getLogger(LessonInstanceQueryService.class);

    private final LessonInstanceRepository lessonInstanceRepository;

    public LessonInstanceQueryService(LessonInstanceRepository lessonInstanceRepository) {
        this.lessonInstanceRepository = lessonInstanceRepository;
    }

    /**
     * Return a {@link List} of {@link LessonInstance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LessonInstance> findByCriteria(LessonInstanceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LessonInstance> specification = createSpecification(criteria);
        return lessonInstanceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link LessonInstance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LessonInstance> findByCriteria(LessonInstanceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LessonInstance> specification = createSpecification(criteria);
        return lessonInstanceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LessonInstanceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LessonInstance> specification = createSpecification(criteria);
        return lessonInstanceRepository.count(specification);
    }

    /**
     * Function to convert {@link LessonInstanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LessonInstance> createSpecification(LessonInstanceCriteria criteria) {
        Specification<LessonInstance> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LessonInstance_.id));
            }
            if (criteria.getLessonName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLessonName(), LessonInstance_.lessonName));
            }
            if (criteria.getStartAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartAt(), LessonInstance_.startAt));
            }
            if (criteria.getEndAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndAt(), LessonInstance_.endAt));
            }
            if (criteria.getDayOfWeek() != null) {
                specification = specification.and(buildSpecification(criteria.getDayOfWeek(), LessonInstance_.dayOfWeek));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), LessonInstance_.description));
            }
            if (criteria.getCretedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCretedOn(), LessonInstance_.cretedOn));
            }
            if (criteria.getStudentRegisterId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStudentRegisterId(),
                            root -> root.join(LessonInstance_.studentRegisters, JoinType.LEFT).get(StudentRegister_.id)
                        )
                    );
            }
            if (criteria.getInternalUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInternalUserId(),
                            root -> root.join(LessonInstance_.internalUser, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
            if (criteria.getStudentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStudentId(),
                            root -> root.join(LessonInstance_.students, JoinType.LEFT).get(Student_.id)
                        )
                    );
            }
            if (criteria.getSubjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubjectId(),
                            root -> root.join(LessonInstance_.subject, JoinType.LEFT).get(Subject_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
