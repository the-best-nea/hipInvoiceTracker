package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LessonTimetable;
import com.mycompany.myapp.domain.LessonTimetableTeacher;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LessonTimetableTeacher entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LessonTimetableTeacherRepository extends JpaRepository<LessonTimetableTeacher, Long> {
    @Query(
        "select lessonTimetableTeacher from LessonTimetableTeacher lessonTimetableTeacher where lessonTimetableTeacher.internalUser.login = ?#{principal.username}"
    )
    List<LessonTimetableTeacher> findByInternalUserIsCurrentUser();

    LessonTimetableTeacher findByLessonTimetable(LessonTimetable lessonTimetable);
}
