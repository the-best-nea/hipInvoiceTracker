package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LessonTimetableTeacherTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LessonTimetableTeacher.class);
        LessonTimetableTeacher lessonTimetableTeacher1 = new LessonTimetableTeacher();
        lessonTimetableTeacher1.setId(1L);
        LessonTimetableTeacher lessonTimetableTeacher2 = new LessonTimetableTeacher();
        lessonTimetableTeacher2.setId(lessonTimetableTeacher1.getId());
        assertThat(lessonTimetableTeacher1).isEqualTo(lessonTimetableTeacher2);
        lessonTimetableTeacher2.setId(2L);
        assertThat(lessonTimetableTeacher1).isNotEqualTo(lessonTimetableTeacher2);
        lessonTimetableTeacher1.setId(null);
        assertThat(lessonTimetableTeacher1).isNotEqualTo(lessonTimetableTeacher2);
    }
}
