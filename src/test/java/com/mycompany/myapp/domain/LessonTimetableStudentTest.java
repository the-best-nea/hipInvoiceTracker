package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LessonTimetableStudentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LessonTimetableStudent.class);
        LessonTimetableStudent lessonTimetableStudent1 = new LessonTimetableStudent();
        lessonTimetableStudent1.setId(1L);
        LessonTimetableStudent lessonTimetableStudent2 = new LessonTimetableStudent();
        lessonTimetableStudent2.setId(lessonTimetableStudent1.getId());
        assertThat(lessonTimetableStudent1).isEqualTo(lessonTimetableStudent2);
        lessonTimetableStudent2.setId(2L);
        assertThat(lessonTimetableStudent1).isNotEqualTo(lessonTimetableStudent2);
        lessonTimetableStudent1.setId(null);
        assertThat(lessonTimetableStudent1).isNotEqualTo(lessonTimetableStudent2);
    }
}
