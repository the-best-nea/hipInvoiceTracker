package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LessonInstanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LessonInstance.class);
        LessonInstance lessonInstance1 = new LessonInstance();
        lessonInstance1.setId(1L);
        LessonInstance lessonInstance2 = new LessonInstance();
        lessonInstance2.setId(lessonInstance1.getId());
        assertThat(lessonInstance1).isEqualTo(lessonInstance2);
        lessonInstance2.setId(2L);
        assertThat(lessonInstance1).isNotEqualTo(lessonInstance2);
        lessonInstance1.setId(null);
        assertThat(lessonInstance1).isNotEqualTo(lessonInstance2);
    }
}
