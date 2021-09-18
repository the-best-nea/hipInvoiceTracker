package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LessonTimetableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LessonTimetable.class);
        LessonTimetable lessonTimetable1 = new LessonTimetable();
        lessonTimetable1.setId(1L);
        LessonTimetable lessonTimetable2 = new LessonTimetable();
        lessonTimetable2.setId(lessonTimetable1.getId());
        assertThat(lessonTimetable1).isEqualTo(lessonTimetable2);
        lessonTimetable2.setId(2L);
        assertThat(lessonTimetable1).isNotEqualTo(lessonTimetable2);
        lessonTimetable1.setId(null);
        assertThat(lessonTimetable1).isNotEqualTo(lessonTimetable2);
    }
}
