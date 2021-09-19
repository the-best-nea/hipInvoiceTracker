package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LessonTimetable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the LessonTimetable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LessonTimetableRepository extends JpaRepository<LessonTimetable, Long> {
}
