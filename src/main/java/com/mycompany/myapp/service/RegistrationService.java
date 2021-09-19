package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.LessonInstance;
import com.mycompany.myapp.domain.RegistrationDetails;
import com.mycompany.myapp.domain.Student;
import com.mycompany.myapp.domain.StudentRegister;
import com.mycompany.myapp.repository.StudentRegisterRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final StudentRegisterRepository studentRegisterRepository;

    public RegistrationService(StudentRegisterRepository studentRegisterRepository) {
        this.studentRegisterRepository = studentRegisterRepository;
    }

    public void takeRegister(LessonInstance lessonInstance) {
        List<RegistrationDetails> registrationDetails = lessonInstance.getRegistrationDetails();
        registrationDetails.forEach(
            detail -> {
                Student student = new Student();
                student.setId(detail.getStudentId());

                StudentRegister studentRegister = new StudentRegister();
                studentRegister.setLessonTimetable(lessonInstance);
                studentRegister.setStudent(student);
                studentRegister.setAttended(detail.isAttended());
                studentRegister.setDateOfLesson(Instant.now());
                studentRegister.setCreatedOn(Instant.now());
                studentRegister.setUpdatedOn(Instant.now());

                studentRegisterRepository.save(studentRegister);
            }
        );
    }
}
