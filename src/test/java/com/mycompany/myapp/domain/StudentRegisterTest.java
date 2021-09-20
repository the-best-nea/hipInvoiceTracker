package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentRegisterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentRegister.class);
        StudentRegister studentRegister1 = new StudentRegister();
        studentRegister1.setId(1L);
        StudentRegister studentRegister2 = new StudentRegister();
        studentRegister2.setId(studentRegister1.getId());
        assertThat(studentRegister1).isEqualTo(studentRegister2);
        studentRegister2.setId(2L);
        assertThat(studentRegister1).isNotEqualTo(studentRegister2);
        studentRegister1.setId(null);
        assertThat(studentRegister1).isNotEqualTo(studentRegister2);
    }
}
