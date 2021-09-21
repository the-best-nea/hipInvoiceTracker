package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.StudentRegister;
import com.mycompany.myapp.repository.StudentRegisterRepository;
import com.mycompany.myapp.service.criteria.StudentRegisterCriteria;
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
 * Service for executing complex queries for {@link StudentRegister} entities in the database.
 * The main input is a {@link StudentRegisterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StudentRegister} or a {@link Page} of {@link StudentRegister} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudentRegisterQueryService extends QueryService<StudentRegister> {

    private final Logger log = LoggerFactory.getLogger(StudentRegisterQueryService.class);

    private final StudentRegisterRepository studentRegisterRepository;

    public StudentRegisterQueryService(StudentRegisterRepository studentRegisterRepository) {
        this.studentRegisterRepository = studentRegisterRepository;
    }

    /**
     * Return a {@link List} of {@link StudentRegister} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StudentRegister> findByCriteria(StudentRegisterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StudentRegister> specification = createSpecification(criteria);
        return studentRegisterRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link StudentRegister} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentRegister> findByCriteria(StudentRegisterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StudentRegister> specification = createSpecification(criteria);
        return studentRegisterRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudentRegisterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StudentRegister> specification = createSpecification(criteria);
        return studentRegisterRepository.count(specification);
    }

    /**
     * Function to convert {@link StudentRegisterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StudentRegister> createSpecification(StudentRegisterCriteria criteria) {
        Specification<StudentRegister> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StudentRegister_.id));
            }
            if (criteria.getDateOfLesson() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateOfLesson(), StudentRegister_.dateOfLesson));
            }
            if (criteria.getPay() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPay(), StudentRegister_.pay));
            }
            if (criteria.getAttended() != null) {
                specification = specification.and(buildSpecification(criteria.getAttended(), StudentRegister_.attended));
            }
            if (criteria.getCreatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedOn(), StudentRegister_.createdOn));
            }
            if (criteria.getUpdatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedOn(), StudentRegister_.updatedOn));
            }
            if (criteria.getStudentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStudentId(),
                            root -> root.join(StudentRegister_.student, JoinType.LEFT).get(Student_.id)
                        )
                    );
            }
            if (criteria.getLessonTimetableId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLessonTimetableId(),
                            root -> root.join(StudentRegister_.lessonTimetable, JoinType.LEFT).get(LessonInstance_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
