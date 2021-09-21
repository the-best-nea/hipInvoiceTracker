package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LessonTimetable;
import com.mycompany.myapp.domain.LessonTimetableStudent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the LessonTimetableStudent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LessonTimetableStudentRepository
    extends JpaRepository<LessonTimetableStudent, Long>, JpaSpecificationExecutor<LessonTimetableStudent> {
    List<LessonTimetableStudent> findAllByLessonTimetable(LessonTimetable lessonTimetable);
}
