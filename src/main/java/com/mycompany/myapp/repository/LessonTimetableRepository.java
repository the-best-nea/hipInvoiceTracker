package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LessonTimetable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LessonTimetable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LessonTimetableRepository extends JpaRepository<LessonTimetable, Long>, JpaSpecificationExecutor<LessonTimetable> {}
